package com.github.sweetsnowywitch.rpgkit.classes;

import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Perk with tick() method that gets called every server tick.
 */
public interface ServerTickablePerk {
    void tick(ServerPlayerEntity entity);
}
