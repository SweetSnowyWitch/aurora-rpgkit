package com.github.sweetsnowywitch.csmprpgkit.magic.form;

import com.github.sweetsnowywitch.csmprpgkit.magic.ServerSpellCast;
import com.github.sweetsnowywitch.csmprpgkit.magic.SpellForm;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class ItemForm extends SpellForm {
    public ItemForm() {
        super(ImmutableMap.of(), ImmutableMap.of());
    }

    @Override
    public void startCast(ServerSpellCast cast, ServerWorld world, @NotNull Entity caster) {
        ItemStack targetStack = null;
        if (caster instanceof LivingEntity le) {
            targetStack = le.getOffHandStack();
        } else {
            var items = caster.getHandItems();
            if (items.iterator().hasNext()) {
                targetStack = items.iterator().next();
            }
        }
        if (targetStack == null || targetStack.isEmpty()) {
            ModForms.SELF.startCast(cast, world, caster);
            return;
        }

        super.startCast(cast, world, caster);

        var replacement = cast.getSpell().onItemHit(cast, world, caster, targetStack);
        caster.equipStack(EquipmentSlot.OFFHAND, replacement);
    }

    @Override
    public void endCast(@NotNull ServerSpellCast cast, @NotNull ServerWorld world) {
        super.endCast(cast, world);
    }
}