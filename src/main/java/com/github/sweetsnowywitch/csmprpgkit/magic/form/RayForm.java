package com.github.sweetsnowywitch.csmprpgkit.magic.form;

import com.github.sweetsnowywitch.csmprpgkit.entities.ModEntities;
import com.github.sweetsnowywitch.csmprpgkit.entities.SpellRayEntity;
import com.github.sweetsnowywitch.csmprpgkit.items.ModItems;
import com.github.sweetsnowywitch.csmprpgkit.magic.*;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RayForm extends SpellForm implements ChanneledForm {
    public RayForm() {
        super(ImmutableMap.of(), ImmutableMap.of());
    }

    @Override
    public void startCast(ServerSpellCast cast, ServerWorld world, @NotNull Entity caster) {
        var ray = new SpellRayEntity(ModEntities.SPELL_RAY, world);
        cast.customData.putUuid("RayEntityUUID", ray.getUuid());
        ray.setCast(cast);

        ray.setAimOrigin(caster.getEyePos());
        var pos = caster.getPos();
        if (caster instanceof PlayerEntity pe) {
            pos = pos.add(pe.getHandPosOffset(ModItems.SPELL_ITEM));
            pos = pos.add(0, 0.7, 0);
        }
        ray.setPosition(pos);
        ray.setYaw(caster.getHeadYaw());
        ray.setPitch(caster.getPitch());
        ray.setMaxAge(this.getMaxChannelDuration(cast.getSpell(), cast.getReactions()));
        world.spawnEntity(ray);

        super.startCast(cast, world, caster);
    }

    @Override
    public void endCast(ServerSpellCast cast, ServerWorld world) {
        super.endCast(cast, world);

        if (cast.customData.contains("RayEntityUUID")) {
            var ent = world.getEntity(cast.customData.getUuid("RayEntityUUID"));
            if (ent != null) {
                ent.discard();
            }
        }
    }

    @Override
    public int getMaxChannelDuration(Spell cast, List<SpellReaction> reactions) {
        return 5*20;
    }

    @Override
    public void channelTick(ServerSpellCast cast, @NotNull Entity caster) {
        if (cast.customData.containsUuid("RayEntityUUID")) {
            var ray = (SpellRayEntity)((ServerWorld)caster.getWorld()).getEntity(cast.customData.getUuid("RayEntityUUID"));
            if (ray != null) {
                ray.setAimOrigin(caster.getEyePos());
                var pos = caster.getPos();
                if (caster instanceof PlayerEntity pe) {
                    pos = pos.add(pe.getHandPosOffset(ModItems.SPELL_ITEM));
                    pos = pos.add(0, 0.7, 0);
                }
                ray.setPosition(pos);
                ray.setYaw(caster.getHeadYaw());
                ray.setPitch(caster.getPitch());
                ray.setMaxAge(ray.age + 5);
            }
        }
    }
}
