package com.jacoobia.immersivezoom.server.utils.ritual;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.utils.ParticleUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static org.apache.commons.lang3.math.NumberUtils.FLOAT_ONE;

/**
 * The attunement ritual!
 * Happens only at midnight
 * The player right clicks, the shard floats into the air in front of them
 * and then starts to spin and "collect moonlight", then smoke covers it and
 * the item swaps out for the attuned version and it drops to the ground
 */
public class AttunementRitual {

    private static final int RISE_END = 10;
    private static final int ABSORB_END = 70;
    private static final int SWAP_AT = 80;
    private static final int RELEASE_AT = 90;

    private static final double RISE_START_DISTANCE = 0.5;
    private static final double RISE_START_DROP = 0.6;
    private static final float LIFT_PITCH = 1.4F;
    private static final double FLOAT_DISTANCE = 1.2; // blocks in front of the player
    private static final double FLOAT_DROP = 0.3;     // below eye height
    private static final double ITEM_CENTRE_HEIGHT = 0.5; // entity position to the visual centre of the bobbing item
    private static final int RELEASE_PICKUP_DELAY = 20;
    private static final String TAG = MOD_ID + ":ritual";

    private static final List<Ritual> ACTIVE_RITUALS = new ArrayList<>();

    public static boolean isRunningFor(Player player) {
        return ACTIVE_RITUALS.stream().anyMatch(ritual -> ritual.getPlayer().equals(player.getUUID()));
    }

    /**
     * Starts the ritual, the given shard becomes the floating entity
     * @param level the level the player is in
     * @param player the player performing the ritual
     * @param shard the single shard taken from the player's hand
     */
    public static void start(ServerLevel level, ServerPlayer player, ItemStack shard) {
        Vec3 forward = Vec3.directionFromRotation(0, player.getYRot());
        Vec3 to = player.getEyePosition().add(forward.scale(FLOAT_DISTANCE)).subtract(0, FLOAT_DROP, 0);
        Vec3 from = player.getEyePosition().add(forward.scale(RISE_START_DISTANCE)).subtract(0, RISE_START_DROP, 0);

        ItemEntity entity = new ItemEntity(level, from.x, from.y, from.z, shard, 0, 0, 0);
        entity.setNoGravity(true);
        entity.setNeverPickUp();
        level.addFreshEntity(entity);
        entity.getPersistentData().putBoolean(TAG, true);

        level.playSound(null, entity.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, FLOAT_ONE, LIFT_PITCH);
        ACTIVE_RITUALS.add(new Ritual(level, player.getUUID(), entity, from, to));
    }

    /**
     * Updates each active ritual every tick (and removes if the ritual tick reports as done)
     */
    public static void tick() {
        ACTIVE_RITUALS.removeIf(AttunementRitual::tick);
    }

    /**
     * Releases a ritual shard left floating by a restart or chunk unload
     * @param entity an ItemEntity joining a level
     */
    public static void releaseIfStale(ItemEntity entity) {
        if (entity.getPersistentData().getBoolean(TAG)) release(entity);
    }

    /**
     * Clears all state, so nothing carries over between singleplayer worlds
     */
    public static void reset() {
        ACTIVE_RITUALS.clear();
    }

    /**
     * Players the next animation step based on the amount of ticks lived
     * @return true when the ritual has finished
     */
    private static boolean tick(Ritual ritual) {
        ItemEntity entity = ritual.getShard();
        if (entity.isRemoved()) return true;
        int ticksLived = ritual.getNextAge();

        if (ticksLived < RISE_END) {
            Vec3 next = ritual.getFrom().lerp(ritual.getTo(), easeOut((ticksLived + 1) / (double) RISE_END));
            entity.setDeltaMovement(next.subtract(entity.position()));
            entity.hasImpulse = true;
        } else {
            entity.setDeltaMovement(Vec3.ZERO);
            if (ticksLived == RISE_END) entity.hasImpulse = true;
        }

        Vec3 centre = entity.position().add(0, ITEM_CENTRE_HEIGHT, 0);

        if (ticksLived > RISE_END && ticksLived <= ABSORB_END) ParticleUtil.moonlight(ritual.getLevel(), centre, ticksLived);
        if (ticksLived > ABSORB_END && ticksLived <= RELEASE_AT) ParticleUtil.smoke(ritual.getLevel(), centre);

        if (ticksLived == SWAP_AT) {
            entity.setItem(ItemRegistry.ATTUNED_AMETHYST_SHARD.get().getDefaultInstance());
            ritual.getLevel().playSound(null, entity.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, FLOAT_ONE, FLOAT_ONE);
        }
        if (ticksLived == RELEASE_AT) {
            release(entity);
            return true;
        }
        return false;
    }

    private static void release(ItemEntity entity) {
        entity.setNoGravity(false);
        entity.setPickUpDelay(RELEASE_PICKUP_DELAY);
        entity.getPersistentData().remove(TAG);
    }

    private static double easeOut(double t) {
        return 1 - (1 - t) * (1 - t);
    }

}
