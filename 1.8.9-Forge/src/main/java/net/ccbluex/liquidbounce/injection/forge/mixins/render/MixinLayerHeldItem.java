//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.Overwrite;
import java.util.UUID;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ LayerHeldItem.class })
public class MixinLayerHeldItem
{
    @Shadow
    @Final
    private RendererLivingEntity<?> livingEntityRenderer;
    
    @Overwrite
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        ItemStack itemstack = entitylivingbaseIn.getHeldItem();
        if (itemstack != null) {
            GlStateManager.pushMatrix();
            if (this.livingEntityRenderer.getMainModel().isChild) {
                final float f = 0.5f;
                GlStateManager.translate(0.0f, 0.625f, 0.0f);
                GlStateManager.rotate(-20.0f, -1.0f, 0.0f, 0.0f);
                GlStateManager.scale(f, f, f);
            }
            final UUID uuid = entitylivingbaseIn.getUniqueID();
            final EntityPlayer entityplayer = Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(uuid);
            if (entityplayer != null && entityplayer.isBlocking()) {
                if (entitylivingbaseIn.isSneaking()) {
                    ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0325f);
                    GlStateManager.translate(-0.58f, 0.3f, -0.2f);
                    GlStateManager.rotate(-24390.0f, 137290.0f, -2009900.0f, -2054900.0f);
                }
                else {
                    ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0325f);
                    GlStateManager.translate(-0.48f, 0.2f, -0.2f);
                    GlStateManager.rotate(-24390.0f, 137290.0f, -2009900.0f, -2054900.0f);
                }
            }
            else {
                ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625f);
            }
            GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer)entitylivingbaseIn).fishEntity != null) {
                itemstack = new ItemStack((Item)Items.fishing_rod, 0);
            }
            final Item item = itemstack.getItem();
            final Minecraft minecraft = Minecraft.getMinecraft();
            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2) {
                GlStateManager.translate(0.0f, 0.1875f, -0.3125f);
                GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                final float f2 = 0.375f;
                GlStateManager.scale(-f2, -f2, f2);
            }
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0f, 0.203125f, 0.0f);
            }
            minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }
}
