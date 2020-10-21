//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.item;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.renderer.RenderHelper;
import net.ccbluex.liquidbounce.features.module.modules.render.SwingAnimation;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemMap;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ ItemRenderer.class })
public abstract class MixinItemRenderer
{
    @Shadow
    private float prevEquippedProgress;
    @Shadow
    private float equippedProgress;
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    private ItemStack itemToRender;
    
    @Shadow
    protected abstract void rotateArroundXAndY(final float p0, final float p1);
    
    @Shadow
    protected abstract void setLightMapFromPlayer(final AbstractClientPlayer p0);
    
    @Shadow
    protected abstract void rotateWithPlayerRotations(final EntityPlayerSP p0, final float p1);
    
    @Shadow
    protected abstract void renderItemMap(final AbstractClientPlayer p0, final float p1, final float p2, final float p3);
    
    @Shadow
    protected abstract void transformFirstPersonItem(final float p0, final float p1);
    
    @Shadow
    protected abstract void performDrinking(final AbstractClientPlayer p0, final float p1);
    
    @Shadow
    protected abstract void doBlockTransformations();
    
    @Shadow
    protected abstract void doBowTransformations(final float p0, final AbstractClientPlayer p1);
    
    @Shadow
    protected abstract void doItemUsedTransformations(final float p0);
    
    @Shadow
    public abstract void renderItem(final EntityLivingBase p0, final ItemStack p1, final ItemCameraTransforms.TransformType p2);
    
    @Shadow
    protected abstract void renderPlayerArm(final AbstractClientPlayer p0, final float p1, final float p2);
    
    @Overwrite
    public void renderItemInFirstPerson(final float partialTicks) {
        final float f = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        final AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.thePlayer;
        final float f2 = abstractclientplayer.getSwingProgress(partialTicks);
        final float f3 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        final float f4 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f3, f4);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations((EntityPlayerSP)abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        if (this.itemToRender != null) {
            final KillAura killAura = (KillAura)ModuleManager.getModule(KillAura.class);
            if (this.itemToRender.getItem() instanceof ItemMap) {
                this.renderItemMap(abstractclientplayer, f3, f, f2);
            }
            else if (abstractclientplayer.getItemInUseCount() > 0 || (this.itemToRender.getItem() instanceof ItemSword && killAura.blocking)) {
                final EnumAction enumaction = killAura.blocking ? EnumAction.BLOCK : this.itemToRender.getItemUseAction();
                switch (enumaction) {
                    case NONE: {
                        this.transformFirstPersonItem(f, 0.0f);
                        break;
                    }
                    case EAT:
                    case DRINK: {
                        this.performDrinking(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f, f2);
                        break;
                    }
                    case BLOCK: {
                        this.transformFirstPersonItem(f + 0.1f, f2);
                        this.doBlockTransformations();
                        GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                        break;
                    }
                    case BOW: {
                        this.transformFirstPersonItem(f, f2);
                        this.doBowTransformations(partialTicks, abstractclientplayer);
                        break;
                    }
                }
            }
            else {
                if (!ModuleManager.getModule(SwingAnimation.class).getState()) {
                    this.doItemUsedTransformations(f2);
                }
                this.transformFirstPersonItem(f, f2);
            }
            this.renderItem((EntityLivingBase)abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!abstractclientplayer.isInvisible()) {
            this.renderPlayerArm(abstractclientplayer, f, f2);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
    
    @Inject(method = { "renderFireInFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    private void renderFireInFirstPerson(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = (AntiBlind)ModuleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && antiBlind.getFireEffect().asBoolean()) {
            callbackInfo.cancel();
        }
    }
}
