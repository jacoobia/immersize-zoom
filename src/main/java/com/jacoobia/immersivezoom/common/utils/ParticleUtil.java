package com.jacoobia.immersivezoom.common.utils;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleUtil {

    private static final List<Effect> ACTIVE = new ArrayList<>();

    /**
     * Plays a mystical particle effect around a world position for ~1 second (20 ticks)
     * then runs the provided callback on the server thread
     * @param level Server level (must be server-side)
     * @param centre World-space centre of the effect
     * @param onComplete Callback to run after the effect finishes (nullable)
     */
    public static void playMysticalEffect(ServerLevel level, Vec3 centre, SimpleParticleType type, Runnable onComplete) {
        playMysticalEffect(level, centre, 20, type, onComplete); // 20 ticks ≈ 1s
    }

    /**
     * Generalized version with configurable duration in ticks
     * @param level Server level (must be server-side)
     * @param centre World-space centre of the effect
     * @param durationTicks How many ticks to run the effect
     * @param onComplete Callback to run after the effect finishes (nullable)
     */
    public static void playMysticalEffect(ServerLevel level, Vec3 centre, int durationTicks, SimpleParticleType type, Runnable onComplete) {
        if (level == null || level.isClientSide()) return;
        ACTIVE.add(new Effect(level, centre, type, onComplete, durationTicks));
    }

    /**
     * Spawns this tick's particles for every running effect and completes finished ones.
     * Called every server tick.
     */
    public static void tickEffects() {
        List<Runnable> finished = new ArrayList<>();

        Iterator<Effect> iterator = ACTIVE.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            if (effect.ticksLeft-- > 0) {
                spawnMysticalParticles(effect.level, effect.centre, effect.type);
            } else {
                iterator.remove();
                if (effect.onComplete != null) finished.add(effect.onComplete);
            }
        }

        finished.forEach(Runnable::run);
    }

    /**
     * Drops all running effects, so none carry over between singleplayer worlds
     */
    public static void clearEffects() {
        ACTIVE.clear();
    }

    private static void spawnMysticalParticles(ServerLevel level, Vec3 center, SimpleParticleType type) {
        final int particlesPerTick = 10;
        final double radius = 0.5;
        final double height = 0.5;
        final double speed = 0.0;

        for (int n = 0; n < particlesPerTick; n++) {
            double dx = (level.random.nextDouble() - 0.5) * 2.0 * radius;
            double dy = level.random.nextDouble() * height;
            double dz = (level.random.nextDouble() - 0.5) * 2.0 * radius;

            level.sendParticles(
                    type,
                    center.x + dx,
                    center.y + dy,
                    center.z + dz,
                    1,
                    0.0, 0.0, 0.0,
                    speed
            );
        }
    }

    private static final class Effect {
        final ServerLevel level;
        final Vec3 centre;
        final SimpleParticleType type;
        final Runnable onComplete;
        int ticksLeft;

        Effect(ServerLevel level, Vec3 centre, SimpleParticleType type, Runnable onComplete, int ticksLeft) {
            this.level = level;
            this.centre = centre;
            this.type = type;
            this.onComplete = onComplete;
            this.ticksLeft = ticksLeft;
        }
    }

}
