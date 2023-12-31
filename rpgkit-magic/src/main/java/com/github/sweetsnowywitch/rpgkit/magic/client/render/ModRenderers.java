package com.github.sweetsnowywitch.rpgkit.magic.client.render;

import com.github.sweetsnowywitch.rpgkit.magic.client.model.SpellChargeModel;
import com.github.sweetsnowywitch.rpgkit.magic.entities.ModEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EmptyEntityRenderer;

@Environment(EnvType.CLIENT)
public class ModRenderers {
    public static void register() {
        EntityRendererRegistry.register(ModEntities.SPELL_RAY, SpellRayRenderer::new);
        EntityRendererRegistry.register(ModEntities.SOUND_BARRIER, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SPELL_BLAST, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.PERSISTENT_MAGIC, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FORCEFIELD, EmptyEntityRenderer::new);

        EntityRendererRegistry.register(ModEntities.SPELL_CHARGE, SpellChargeRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(SpellChargeModel.MAIN, SpellChargeModel::getTexturedModelData);
    }
}
