package com.jacoobia.immersivezoom.server.utils.ritual;

import lombok.Getter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

@Getter
public class Ritual {

    private final ServerLevel level;
    private final UUID player;
    private final ItemEntity shard;
    private final Vec3 from;
    private final Vec3 to;
    private int age;

    public Ritual(ServerLevel level, UUID player, ItemEntity shard, Vec3 from, Vec3 to) {
        this.level = level;
        this.player = player;
        this.shard = shard;
        this.from = from;
        this.to = to;
    }

    public int getNextAge() {
        return age++;
    }

}
