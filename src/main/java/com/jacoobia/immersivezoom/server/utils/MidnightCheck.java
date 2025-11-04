package com.jacoobia.immersivezoom.server.utils;

import com.jacoobia.immersivezoom.common.Translations;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.registry.SoundRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Hums at players carrying an Unattuned Amethyst Shard when midnight starts in the overworld,
 * hinting that it's time for the attunement ritual. Also hums at players who arrive in the
 * overworld (login or dimension change) during the midnight window.
 */
public class MidnightCheck {

    private static final long HUM_TOLERANCE = 1000L;
    private static final int CHECK_INTERVAL_TICKS = 20;
    private static final int ARRIVAL_DELAY_TICKS = 60;

    private static Boolean wasMidnight = null;
    private static final Map<UUID, Integer> PENDING_ARRIVALS = new HashMap<>();

    /**
     * Queues a midnight check for a player arriving in the overworld that
     * run after ARRIVAL_DELAY_TICKS, so that players don't mioss the message
     * due to loading screens
     * @param player the arriving player
     */
    public static void scheduleArrival(ServerPlayer player) {
        PENDING_ARRIVALS.put(player.getUUID(), ARRIVAL_DELAY_TICKS);
    }

    /**
     * Trigger point for the midnight "hum" message for all users
     * who qualify for (i.e they're holding an unattuned crystal)
     * @param level the overworld
     */
    public static void tickOverworld(ServerLevel level) {
        if (level.getGameTime() % CHECK_INTERVAL_TICKS != 0) return;

        boolean isMidnight = TimeUtils.isAroundMidnight(level, HUM_TOLERANCE);
        if (isMidnight && Boolean.FALSE.equals(wasMidnight)) {
            for (ServerPlayer player : level.players()) {
                playEffectIfCarryingShard(player);
            }
        }
        wasMidnight = isMidnight;
    }

    /**
     * Called every server tick & runs a delay for the queued 'arrivals'
     * and runs their midnight check
     * @param server the server
     */
    public static void tickArrivals(MinecraftServer server) {
        if (PENDING_ARRIVALS.isEmpty()) return;

        Iterator<Map.Entry<UUID, Integer>> iterator = PENDING_ARRIVALS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            int ticksLeft = entry.getValue() - 1;
            if (ticksLeft > 0) {
                entry.setValue(ticksLeft);
                continue;
            }

            iterator.remove();

            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
            if (player != null
                    && player.level().dimension() == Level.OVERWORLD
                    && TimeUtils.isAroundMidnight(player.serverLevel(), HUM_TOLERANCE)) {
                playEffectIfCarryingShard(player);
            }
        }
    }

    /**
     * Clears all state, so nothing carries over between singleplayer worlds
     */
    public static void reset() {
        PENDING_ARRIVALS.clear();
        wasMidnight = null;
    }

    // TODO: Add some kind of cool particle effect to this
    private static void playEffectIfCarryingShard(ServerPlayer player) {
        if (player.getInventory().contains(stack ->
                stack.is(ItemRegistry.UNATTUNED_AMETHYST_SHARD))) {
            player.displayClientMessage(Translations.Lore.midnight, true);
            player.level().playSound(
                    null, player.getX(), player.getY(), player.getZ(),
                    SoundRegistry.HUM.get(), SoundSource.PLAYERS, 1.0F, 1.0F
            );
        }
    }

}
