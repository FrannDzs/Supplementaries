package net.mehvahdjukaar.supplementaries.mixins;

import net.mehvahdjukaar.supplementaries.client.Textures;
import net.mehvahdjukaar.supplementaries.world.data.Tags;
import net.minecraft.inventory.container.ShulkerBoxSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxSlot.class)
public abstract class ShulkerBoxSlotMixin {

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    public void isItemValid(ItemStack itemStackIn, CallbackInfoReturnable<Boolean> info ) {
        ITag<Item> t = ItemTags.getCollection().get(Tags.SHULKER_BLACKLIST_TAG);
        if(t!=null && itemStackIn.getItem().isIn(t))
            info.setReturnValue(false);
    }
}
