package com.github.sweetsnowywitch.csmprpgkit.mixin;

import com.github.sweetsnowywitch.csmprpgkit.classes.Perk;
import com.github.sweetsnowywitch.csmprpgkit.classes.listener.ClassReloadListener;
import com.github.sweetsnowywitch.csmprpgkit.classes.perks.ComboPerk;
import com.github.sweetsnowywitch.csmprpgkit.classes.perks.ModPerks;
import com.github.sweetsnowywitch.csmprpgkit.components.ModComponents;
import com.mojang.authlib.GameProfile;
import net.bettercombat.api.EntityPlayer_BetterCombat;
import net.bettercombat.logic.PlayerAttackProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class ComboMixin extends PlayerEntity implements PlayerAttackProperties, EntityPlayer_BetterCombat {

    public ComboMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method = "attack",
            at = {@At("TAIL")})
    public void attack(Entity target, CallbackInfo ci) {
        var perks = ModComponents.CLASS.get(this).getPerk(ModPerks.COMBO.typeId);

        for (var rawPerk : perks) {
            var perk = (ComboPerk) rawPerk;
            if (perk.getCombo() == (this.getComboCount() % perk.getMaxCombo())) {
                target.damage(DamageSource.player(this), (float) this.getAttributeValue(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE) + perk.getModifier());
            }
        }
    }
}
