package com.jacoobia.immersivezoom.client.render;

import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.utils.BeltPlacement;
import lombok.NoArgsConstructor;
import com.jacoobia.immersivezoom.client.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A damped pendulum per player, so the belt spyglass swings from where it's "clipped" on
 * Simulated on every client from movement it already has so nothing is synced across the network
 * <a href="https://www.myphysicslab.com/pendulum/pendulum-en.html">...</a>
 */
@NoArgsConstructor
public final class BeltPhysics {

    private static final double GRAVITY = 0.15;
    private static final double PUSH = 1.2;
    private static final double DRAG = 0.06;
    private static final double DAMPING = 0.85;
    private static final double MAX_ACCEL = 0.5;
    private static final double MAX_SWING = Math.toRadians(45);
    private static final double BODY_LIMIT = Math.toRadians(5);

    private static final Map<AbstractClientPlayer, BeltPhysics> STATES = new WeakHashMap<>();

    private Vec3 lastPoint;
    private Vec3 lastVelocity = Vec3.ZERO;

    private double pitch, pitchO, pitchSpeed;
    private double roll, rollO, rollSpeed;

    /**
     * Steps every loaded player's spyglass by one tick
     * Only runs for enabled/visible belt spyglasses otherwise we're running pointless physics sims
     * @param level the client level
     */
    public static void tickAll(ClientLevel level) {
        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        boolean renderAll = ClientConfig.RENDER_BELT_SPYGLASSES.get();

        for (AbstractClientPlayer player : level.players()) {
            SpyglassSlotData data = player.getData(AttachmentRegistry.HIP_SPYGLASS);
            if (!renderAll || !data.visible() || data.stack().isEmpty()
                    || !player.shouldRender(camera.x, camera.y, camera.z)) {
                STATES.remove(player);
                continue;
            }
            STATES.computeIfAbsent(player, p -> new BeltPhysics()).tick(player, data.placement());
        }
    }

    /**
     * @param player the player wearing the spyglass
     * @param partialTicks the render partial tick
     * @return the swing forward/back in degrees, positive swings the bottom backwards
     */
    public static float pitch(AbstractClientPlayer player, float partialTicks) {
        BeltPhysics sway = STATES.get(player);
        return sway == null ? 0f : (float) Math.toDegrees(Mth.lerp(partialTicks, sway.pitchO, sway.pitch));
    }

    /**
     * @param player the player wearing the spyglass
     * @param partialTicks the render partial tick
     * @return the sideways swing in degrees, positive swings the bottom to the player's right
     */
    public static float roll(AbstractClientPlayer player, float partialTicks) {
        BeltPhysics sway = STATES.get(player);
        return sway == null ? 0f : (float) Math.toDegrees(Mth.lerp(partialTicks, sway.rollO, sway.roll));
    }

    private void tick(AbstractClientPlayer player, BeltPlacement placement) {
        pitchO = pitch;
        rollO = roll;

        double yaw = player.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 left = new Vec3(Math.cos(yaw), 0, Math.sin(yaw));
        Vec3 forward = new Vec3(-Math.sin(yaw), 0, Math.cos(yaw));

        Vec3 point = player.position()
                .add(left.scale(ScopeSlotHipModelLayer.offsetX(placement)))
                .add(forward.scale(-ScopeSlotHipModelLayer.offsetZ(placement)));

        if (lastPoint == null) {
            lastPoint = point;
            return;
        }

        Vec3 velocity = point.subtract(lastPoint);
        Vec3 accel = velocity.subtract(lastVelocity);
        if (accel.length() > MAX_ACCEL) accel = accel.normalize().scale(MAX_ACCEL);
        lastPoint = point;
        lastVelocity = velocity;

        double driveForward = accel.dot(forward) * PUSH + velocity.dot(forward) * DRAG;
        double driveLeft = accel.dot(left) * PUSH + velocity.dot(left) * DRAG;

        pitchSpeed = (pitchSpeed - GRAVITY * Math.sin(pitch) + driveForward * Math.cos(pitch)) * DAMPING;
        rollSpeed = (rollSpeed - GRAVITY * Math.sin(roll) + driveLeft * Math.cos(roll)) * DAMPING;
        pitch += pitchSpeed;
        roll += rollSpeed;

        double minPitch = -MAX_SWING, maxPitch = MAX_SWING;
        double minRoll = -MAX_SWING, maxRoll = MAX_SWING;
        switch (placement) {
            case BACK_LEFT, BACK_RIGHT -> minPitch = -BODY_LIMIT;
            case FRONT_LEFT, FRONT_RIGHT -> maxPitch = BODY_LIMIT;
            case SIDE_LEFT -> maxRoll = BODY_LIMIT;
            case SIDE_RIGHT -> minRoll = -BODY_LIMIT;
        }

        if (pitch < minPitch || pitch > maxPitch) {
            pitch = Mth.clamp(pitch, minPitch, maxPitch);
            pitchSpeed = 0;
        }
        if (roll < minRoll || roll > maxRoll) {
            roll = Mth.clamp(roll, minRoll, maxRoll);
            rollSpeed = 0;
        }
    }

}
