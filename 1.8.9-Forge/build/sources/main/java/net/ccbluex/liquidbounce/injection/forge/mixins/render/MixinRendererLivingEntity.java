//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.Overwrite;
import java.awt.Color;
import co.uk.hexeption.utils.OutlineUtils;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ RendererLivingEntity.class })
public abstract class MixinRendererLivingEntity extends MixinRender
{
    @Shadow
    protected ModelBase mainModel;
    
    @Inject(method = { "doRender" }, at = { @At("HEAD") })
    private <T extends EntityLivingBase> void injectChamsPre(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo callbackInfo) {
        final Chams chams = (Chams)ModuleManager.getModule(Chams.class);
        if (chams.getState() && chams.targetsValue.asBoolean() && EntityUtils.isSelected((Entity)entity, false)) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1000000.0f);
        }
    }
    
    @Inject(method = { "doRender" }, at = { @At("RETURN") })
    private <T extends EntityLivingBase> void injectChamsPost(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo callbackInfo) {
        final Chams chams = (Chams)ModuleManager.getModule(Chams.class);
        if (chams.getState() && chams.targetsValue.asBoolean() && EntityUtils.isSelected((Entity)entity, false)) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(32823);
        }
    }
    
    @Inject(method = { "canRenderName" }, at = { @At("HEAD") }, cancellable = true)
    private <T extends EntityLivingBase> void canRenderName(final T entity, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!ESP.renderNameTags || (ModuleManager.getModule(NameTags.class).getState() && EntityUtils.isSelected((Entity)entity, false))) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
    
    @Overwrite
    protected <T extends EntityLivingBase> void renderModel(final T entitylivingbaseIn, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float scaleFactor) {
        final boolean visible = !entitylivingbaseIn.isInvisible();
        final TrueSight trueSight = (TrueSight)ModuleManager.getModule(TrueSight.class);
        final boolean semiVisible = !visible && (!entitylivingbaseIn.isInvisibleToPlayer((EntityPlayer)Minecraft.getMinecraft().thePlayer) || (trueSight.getState() && trueSight.entitiesValue.asBoolean()));
        if (visible || semiVisible) {
            if (!this.bindEntityTexture(entitylivingbaseIn)) {
                return;
            }
            if (semiVisible) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 0.15f);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569f);
            }
            final ESP esp = (ESP)ModuleManager.getModule(ESP.class);
            if (esp.getState() && EntityUtils.isSelected((Entity)entitylivingbaseIn, false)) {
                final Minecraft mc = Minecraft.getMinecraft();
                final boolean fancyGraphics = mc.gameSettings.fancyGraphics;
                mc.gameSettings.fancyGraphics = false;
                final float gamma = mc.gameSettings.gammaSetting;
                mc.gameSettings.gammaSetting = 100000.0f;
                final String lowerCase = esp.modeValue.asString().toLowerCase();
                switch (lowerCase) {
                    case "wireframe": {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        RenderUtils.glColor(esp.getColor((Entity)entitylivingbaseIn));
                        GL11.glLineWidth(esp.wireframeWidth.asFloat());
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    }
                    case "outline": {
                        ClientUtils.disableFastRender();
                        GlStateManager.resetColor();
                        final Color color = esp.getColor((Entity)entitylivingbaseIn);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderOne(esp.outlineWidth.asFloat());
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderTwo();
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderThree();
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFour(color);
                        this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                        break;
                    }
                }
                mc.gameSettings.fancyGraphics = fancyGraphics;
                mc.gameSettings.gammaSetting = gamma;
            }
            this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            if (semiVisible) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }
}
