package com.jacoobia.immersivezoom.server.utils;

import com.jacoobia.immersivezoom.server.SpyglassAttachments;
import com.jacoobia.immersivezoom.server.SpyglassState;
import net.minecraft.server.level.ServerPlayer;

public class ServerUtils {

    public static SpyglassState getServerState(ServerPlayer serverPlayer) {
        return serverPlayer.getData(SpyglassAttachments.SPYGLASS_STATE.get());
    }

}
