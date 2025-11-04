package com.jacoobia.immersivezoom.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class ParticleUtil {

    private static final double MOTE_SPEED = 0.09;
    private static final int RITUAL_CHIME_DELAY = 15;
    private static final int NUDGE_MOTES_PER_TICK = 1;
    private static final double NUDGE_MOTE_SPEED = 0.02;

    /**
     * Streams moonlight motes down from above into a point. With count 0, sendParticles
     * spawns exactly at the position and treats the spread arguments as velocity
     * @param level the server level
     * @param centre the point the motes converge on
     * @param age ticks into the ritual, used to pace the chimes
     */
    public static void moonlight(ServerLevel level, Vec3 centre, int age) {
        RandomSource random = level.random;
        for (int i = 0; i < 3; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double radius = 0.6 + random.nextDouble() * 0.8;
            double height = 2.5 + random.nextDouble() * 1.5;
            Vec3 start = centre.add(Math.cos(angle) * radius, height, Math.sin(angle) * radius);
            Vec3 velocity = centre.subtract(start).scale(MOTE_SPEED);
            level.sendParticles(ParticleTypes.END_ROD, start.x, start.y, start.z, 0, velocity.x, velocity.y, velocity.z, 1.0);
        }
        if (age % RITUAL_CHIME_DELAY == 0) {
            level.sendParticles(ParticleTypes.GLOW, centre.x, centre.y, centre.z, 2, 0.05, 0.05, 0.05, 0);
            level.playSound(null, BlockPos.containing(centre), SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.PLAYERS, 0.6F, 1.2F + age / 100F);
        }
    }

    /**
     * A few moonlight motes drifting slowly in around a point, too slow to arrive before
     * they fade, as a subtle hint that the ritual is possible. Called each tick while it plays,
     * with the centre following the player
     * @param level the server level
     * @param centre the point the motes drift towards
     */
    public static void moonlightNudge(ServerLevel level, Vec3 centre) {
        RandomSource random = level.random;
        for (int i = 0; i < NUDGE_MOTES_PER_TICK; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double radius = 0.5 + random.nextDouble() * 0.5;
            double height = -0.7 + random.nextDouble() * 1.8; // knee to just above the head
            Vec3 start = centre.add(Math.cos(angle) * radius, height, Math.sin(angle) * radius);
            Vec3 velocity = centre.subtract(start).scale(NUDGE_MOTE_SPEED);
            level.sendParticles(ParticleTypes.END_ROD, start.x, start.y, start.z, 0, velocity.x, velocity.y, velocity.z, 1.0);
        }
    }

    /**
     * A burst of smoke and end rods around a point, one packet per type
     * @param level the server level
     * @param centre the centre of the burst
     */
    public static void smoke(ServerLevel level, Vec3 centre) {
        level.sendParticles(ParticleTypes.END_ROD, centre.x, centre.y, centre.z, 6, 0.25, 0.25, 0.25, 0.02);
        level.sendParticles(ParticleTypes.SMOKE, centre.x, centre.y, centre.z, 8, 0.2, 0.2, 0.2, 0.01);
    }

}
