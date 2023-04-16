package com.github.sweetsnowywitch.csmprpgkit.items;

import com.github.sweetsnowywitch.csmprpgkit.client.ClientRPGKitMod;
import com.github.sweetsnowywitch.csmprpgkit.client.ClientSpellBuildHandler;
import com.github.sweetsnowywitch.csmprpgkit.magic.SpellForm;
import com.github.sweetsnowywitch.csmprpgkit.magic.form.ModForms;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class SpellItem extends Item {
    public SpellItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    private void doCast(SpellForm form) {
        ClientRPGKitMod.SPELL_BUILD_HANDLER.doCast(form);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack inHand = user.getStackInHand(hand);
        if (!world.isClient) {
            return TypedActionResult.success(inHand);
        }

        this.doCast(ModForms.RAY);

        return TypedActionResult.success(inHand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!selected) {
            // TODO: Apply to random items in inventory?
            if (world.isClient) {
                this.doCast(ModForms.SELF);
            }

            stack.decrement(1);
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}