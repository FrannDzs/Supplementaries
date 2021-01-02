package net.mehvahdjukaar.supplementaries.renderers;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.UUID;


@OnlyIn(Dist.CLIENT)
public class CageItemRenderer extends ItemStackTileEntityRenderer {

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        //render block
        matrixStackIn.push();
        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
        BlockState state = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
        blockRenderer.renderBlock(state, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
        matrixStackIn.pop();

        CompoundNBT compound = stack.getTag();
        if(compound == null || compound.isEmpty())return;

        //render mob
        if(compound.contains("CachedJarMobValues") && compound.contains("JarMob")) {
            CompoundNBT cmp = compound.getCompound("JarMob");
            CompoundNBT cmp2 = compound.getCompound("CachedJarMobValues");

            EntityType<?> type = net.minecraft.util.registry.Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(cmp.getString("id")));

            World world = Minecraft.getInstance().world;
            if (world != null) {
                Entity e = type.create(world);
                if(e instanceof AgeableEntity)((AgeableEntity)e).setGrowingAge(cmp.getInt("Age"));
                else if(e instanceof ZombieEntity)((ZombieEntity) e).setChild(cmp.getBoolean("IsBaby"));
                else if(e instanceof PiglinEntity)((PiglinEntity) e).setChild(cmp.getBoolean("IsBaby"));
                if(cmp2.contains("oldID"))
                    e.setUniqueId(cmp2.getUniqueId("oldID"));

                if (e != null) {
                    float y = cmp2.getFloat("YOffset");
                    float s = cmp2.getFloat("Scale");
                    matrixStackIn.push();
                    matrixStackIn.translate(0.5, y, 0.5);
                    matrixStackIn.scale(-s, s, -s);
                    Minecraft.getInstance().getRenderManager().renderEntityStatic(e, 0.0D, 0.0D, 0.0D, 0.0F, 0, matrixStackIn, bufferIn, combinedLightIn);
                    matrixStackIn.pop();
                }
            }
        }
    }
}
