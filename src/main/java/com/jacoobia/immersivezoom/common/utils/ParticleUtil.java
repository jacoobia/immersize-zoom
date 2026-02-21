package com.jacoobia.immersivezoom.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import static com.jacoobia.immersivezoom.common.utils.NumberUtils.INT_ONE;
import static com.jacoobia.immersivezoom.common.utils.NumberUtils.INT_ZERO;

public class ParticleUtil {

    private static final double MOTE_SPEED = 0.09;
    private static final int RITUAL_MOTES_PER_TICK = 3;
    private static final double RITUAL_RADIUS_MIN = 0.6;
    private static final double RITUAL_RADIUS_RANGE = 0.8;
    private static final double RITUAL_HEIGHT_MIN = 2.5;
    private static final double RITUAL_HEIGHT_RANGE = 1.5;
    private static final int RITUAL_GLOW_COUNT = 2;
    private static final double RITUAL_GLOW_SPREAD = 0.05;
    private static final int RITUAL_CHIME_DELAY = 15;
    private static final float RITUAL_CHIME_VOLUME = 0.6F;
    private static final float RITUAL_CHIME_PITCH = 1.2F;
    private static final float RITUAL_CHIME_PITCH_RISE = 100F; // ticks per +1 pitch

    private static final int NUDGE_MOTES_PER_TICK = 1;
    private static final double NUDGE_MOTE_SPEED = 0.02;
    private static final double NUDGE_RADIUS_MIN = 0.5;
    private static final double NUDGE_RADIUS_RANGE = 0.5;
    private static final double NUDGE_HEIGHT_MIN = -0.7; // knee to just above the head
    private static final double NUDGE_HEIGHT_RANGE = 1.8;

    private static final int SMOKE_END_ROD_COUNT = 6;
    private static final double SMOKE_END_ROD_SPREAD = 0.25;
    private static final double SMOKE_END_ROD_SPEED = 0.02;
    private static final int SMOKE_COUNT = 8;
    private static final double SMOKE_SPREAD = 0.2;
    private static final double SMOKE_SPEED = 0.01;

    /**
     * Streams moonlight motes down from above into a point
     * @param level the server level
     * @param centre the point the motes converge on
     * @param age ticks into the ritual, used to pace the chimes
     */
    public static void moonlight(ServerLevel level, Vec3 centre, int age) {
        for (int i = 0; i < RITUAL_MOTES_PER_TICK; i++) {
            spawnMote(level, centre, RITUAL_RADIUS_MIN, RITUAL_RADIUS_RANGE, RITUAL_HEIGHT_MIN, RITUAL_HEIGHT_RANGE, MOTE_SPEED);
        }
        if (age % RITUAL_CHIME_DELAY == INT_ZERO) {
            level.sendParticles(ParticleTypes.GLOW, centre.x, centre.y, centre.z, RITUAL_GLOW_COUNT,
                    RITUAL_GLOW_SPREAD, RITUAL_GLOW_SPREAD, RITUAL_GLOW_SPREAD, INT_ZERO);
            level.playSound(null, BlockPos.containing(centre), SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.PLAYERS, RITUAL_CHIME_VOLUME, RITUAL_CHIME_PITCH + age / RITUAL_CHIME_PITCH_RISE);
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
        for (int i = 0; i < NUDGE_MOTES_PER_TICK; i++) {
            spawnMote(level, centre, NUDGE_RADIUS_MIN, NUDGE_RADIUS_RANGE, NUDGE_HEIGHT_MIN, NUDGE_HEIGHT_RANGE, NUDGE_MOTE_SPEED);
        }
    }

    /**
     * A burst of smoke and end rods around a point, one packet per type
     * @param level the server level
     * @param centre the centre of the burst
     */
    public static void smoke(ServerLevel level, Vec3 centre) {
        level.sendParticles(ParticleTypes.END_ROD, centre.x, centre.y, centre.z, SMOKE_END_ROD_COUNT,
                SMOKE_END_ROD_SPREAD, SMOKE_END_ROD_SPREAD, SMOKE_END_ROD_SPREAD, SMOKE_END_ROD_SPEED);
        level.sendParticles(ParticleTypes.SMOKE, centre.x, centre.y, centre.z, SMOKE_COUNT,
                SMOKE_SPREAD, SMOKE_SPREAD, SMOKE_SPREAD, SMOKE_SPEED);
    }

    /**
     * Spawns one end rod mote at a random point in a ring around the centre, moving towards it
     */
    private static void spawnMote(ServerLevel level, Vec3 centre, double radiusMin, double radiusRange,
                                  double heightMin, double heightRange, double speed) {
        RandomSource random = level.random;
        double angle = random.nextDouble() * Math.PI * 2;
        double radius = radiusMin + random.nextDouble() * radiusRange;
        double height = heightMin + random.nextDouble() * heightRange;
        Vec3 start = centre.add(Math.cos(angle) * radius, height, Math.sin(angle) * radius);
        Vec3 velocity = centre.subtract(start).scale(speed);
        level.sendParticles(ParticleTypes.END_ROD, start.x, start.y, start.z, INT_ZERO, velocity.x, velocity.y, velocity.z, INT_ONE);
    }

}
