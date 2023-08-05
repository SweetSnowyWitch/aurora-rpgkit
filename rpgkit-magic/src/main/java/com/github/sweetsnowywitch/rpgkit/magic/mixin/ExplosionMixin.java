package com.github.sweetsnowywitch.rpgkit.magic.mixin;

import com.github.sweetsnowywitch.rpgkit.magic.effects.use.special.WardEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow
    public abstract List<BlockPos> getAffectedBlocks();

    @Shadow protected abstract ExplosionBehavior chooseBehavior(@Nullable Entity entity);

    @Shadow @Final private World world;

    @Shadow @Final private float power;

    @Inject(at = @At("TAIL"), method = "collectBlocksAndDamageEntities()V")
    public void collectBlocksAndDamageEntities(CallbackInfo ci) {
        var me = (Explosion) (Object) this;
        var world = ((ExplosionAccessor) me).getWorld();
        var behavior = (ExplosionBehavior) this.chooseBehavior(me.getCausingEntity());
        if (!(world instanceof ServerWorld sw)) {
            return;
        }

        this.getAffectedBlocks().removeIf(pos -> WardEffect.isBlockProtected(sw, pos));
        this.getAffectedBlocks().removeIf(pos -> !behavior.canDestroyBlock(me, world, pos, world.getBlockState(pos), this.power));
    }
}
