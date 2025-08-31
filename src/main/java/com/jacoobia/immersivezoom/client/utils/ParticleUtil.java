package com.jacoobia.immersivezoom.client.utils;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class ParticleUtil {

    /**
     * Plays a mystical particle effect around a world position for ~1 second (20 ticks),
     * then runs the provided callback on the server thread.
     * @param level Server level (must be server-side).
     * @param centre World-space centre of the effect.
     * @param onComplete Callback to run after the effect finishes (nullable).
     */
    public static void playMysticalEffect(ServerLevel level, Vec3 centre, SimpleParticleType type, Runnable onComplete) {
        playMysticalEffect(level, centre, 20, type, onComplete); // 20 ticks ≈ 1s
    }

    /**
     * Generalized version with configurable duration in ticks.
     * @param level Server level (must be server-side).
     * @param centre World-space centre of the effect.
     * @param durationTicks How many ticks to run the effect.
     * @param onComplete Callback to run after the effect finishes (nullable).
     */
    public static void playMysticalEffect(ServerLevel level, Vec3 centre, int durationTicks, SimpleParticleType type, Runnable onComplete) {
        if (level == null || level.isClientSide()) return;
        if (durationTicks <= 0) {
            if (onComplete != null) level.getServer().execute(onComplete);
            return;
        }

        final int startTick = level.getServer().getTickCount();

        for (int i = 0; i < durationTicks; i++) {
            final int runAt = startTick + i;
            level.getServer().tell(new TickTask(runAt, () -> spawnMysticalParticles(level, centre, type)));
        }

        if (onComplete != null) {
            final int runAt = startTick + durationTicks;
            level.getServer().tell(new TickTask(runAt, onComplete));
        }
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

}
