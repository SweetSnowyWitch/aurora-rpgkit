package com.github.sweetsnowywitch.rpgkit.magic.particle;

import com.github.sweetsnowywitch.rpgkit.magic.RPGKitMagicMod;
import com.mojang.serialization.Codec;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {
    public static final ParticleType<GenericSpellParticleEffect> GENERIC_SPELL = new ParticleType<>(
            false, GenericSpellParticleEffect.FACTORY
    ) {
        @Override
        public Codec<GenericSpellParticleEffect> getCodec() {
            return GenericSpellParticleEffect.CODEC;
        }
    };

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, Identifier.of(RPGKitMagicMod.MOD_ID, "generic_spell"), GENERIC_SPELL);
    }
}
