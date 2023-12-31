package com.github.sweetsnowywitch.rpgkit.magic.blocks;

import com.github.sweetsnowywitch.rpgkit.magic.RPGKitMagicMod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public final static Block MAGIC_LIGHT = new MagicLightBlock(FabricBlockSettings.of(Material.WOOL).nonOpaque().noCollision().luminance(15).ticksRandomly());

    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier(RPGKitMagicMod.MOD_ID, "magic_light"), MAGIC_LIGHT);
    }
}
