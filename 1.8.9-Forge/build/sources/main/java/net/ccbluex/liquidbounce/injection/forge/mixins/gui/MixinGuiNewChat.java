//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.util.Iterator;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.lwjgl.opengl.GL11;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.ChatLine;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;
    @Shadow
    private int scrollPos;
    @Shadow
    private boolean isScrolled;
    @Shadow
    @Final
    private List<ChatLine> chatLines;
    
    @Shadow
    public abstract int getLineCount();
    
    @Shadow
    public abstract boolean getChatOpen();
    
    @Shadow
    public abstract float getChatScale();
    
    @Shadow
    public abstract int getChatWidth();
    
    @Shadow
    public abstract void deleteChatLine(final int p0);
    
    @Shadow
    public abstract void scroll(final int p0);
    
    @Inject(method = { "drawChat" }, at = { @At("HEAD") }, cancellable = true)
    private void drawChat(final int p_drawChat_1_, final CallbackInfo callbackInfo) {
        final HUD hud = (HUD)ModuleManager.getModule(HUD.class);
        if (hud.getState() && hud.fontChatValue.asBoolean()) {
            callbackInfo.cancel();
            if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
                final int lvt_2_1_ = this.getLineCount();
                boolean lvt_3_1_ = false;
                int lvt_4_1_ = 0;
                final int lvt_5_1_ = this.drawnChatLines.size();
                final float lvt_6_1_ = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
                if (lvt_5_1_ > 0) {
                    if (this.getChatOpen()) {
                        lvt_3_1_ = true;
                    }
                    final float lvt_7_1_ = this.getChatScale();
                    final int lvt_8_1_ = MathHelper.ceiling_float_int(this.getChatWidth() / lvt_7_1_);
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(2.0f, 20.0f, 0.0f);
                    GlStateManager.scale(lvt_7_1_, lvt_7_1_, 1.0f);
                    for (int lvt_9_1_ = 0; lvt_9_1_ + this.scrollPos < this.drawnChatLines.size() && lvt_9_1_ < lvt_2_1_; ++lvt_9_1_) {
                        final ChatLine lvt_10_1_ = this.drawnChatLines.get(lvt_9_1_ + this.scrollPos);
                        if (lvt_10_1_ != null) {
                            final int lvt_11_1_ = p_drawChat_1_ - lvt_10_1_.getUpdatedCounter();
                            if (lvt_11_1_ < 200 || lvt_3_1_) {
                                double lvt_12_1_ = lvt_11_1_ / 200.0;
                                lvt_12_1_ = 1.0 - lvt_12_1_;
                                lvt_12_1_ *= 10.0;
                                lvt_12_1_ = MathHelper.clamp_double(lvt_12_1_, 0.0, 1.0);
                                lvt_12_1_ *= lvt_12_1_;
                                int lvt_14_1_ = (int)(255.0 * lvt_12_1_);
                                if (lvt_3_1_) {
                                    lvt_14_1_ = 255;
                                }
                                lvt_14_1_ *= (int)lvt_6_1_;
                                ++lvt_4_1_;
                                if (lvt_14_1_ > 3) {
                                    final int lvt_15_1_ = 0;
                                    final int lvt_16_1_ = -lvt_9_1_ * 9;
                                    Gui.drawRect(lvt_15_1_, lvt_16_1_ - 9, lvt_15_1_ + lvt_8_1_ + 4, lvt_16_1_, lvt_14_1_ / 2 << 24);
                                    final String lvt_17_1_ = lvt_10_1_.getChatComponent().getFormattedText();
                                    Fonts.font40.drawStringWithShadow(lvt_17_1_, lvt_15_1_ + 2.0f, (float)(lvt_16_1_ - 8), 16777215 + (lvt_14_1_ << 24));
                                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                                    GlStateManager.resetColor();
                                }
                            }
                        }
                    }
                    if (lvt_3_1_) {
                        final int lvt_9_1_ = Fonts.font40.FONT_HEIGHT;
                        GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                        final int lvt_10_2_ = lvt_5_1_ * lvt_9_1_ + lvt_5_1_;
                        final int lvt_11_1_ = lvt_4_1_ * lvt_9_1_ + lvt_4_1_;
                        final int lvt_12_2_ = this.scrollPos * lvt_11_1_ / lvt_5_1_;
                        final int lvt_13_1_ = lvt_11_1_ * lvt_11_1_ / lvt_10_2_;
                        if (lvt_10_2_ != lvt_11_1_) {
                            final int lvt_14_1_ = (lvt_12_2_ > 0) ? 170 : 96;
                            final int lvt_15_2_ = this.isScrolled ? 13382451 : 3355562;
                            Gui.drawRect(0, -lvt_12_2_, 2, -lvt_12_2_ - lvt_13_1_, lvt_15_2_ + (lvt_14_1_ << 24));
                            Gui.drawRect(2, -lvt_12_2_, 1, -lvt_12_2_ - lvt_13_1_, 13421772 + (lvt_14_1_ << 24));
                        }
                    }
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    
    @Inject(method = { "getChatComponent" }, at = { @At("HEAD") }, cancellable = true)
    private void getChatComponent(final int p_getChatComponent_1_, final int p_getChatComponent_2_, final CallbackInfoReturnable<IChatComponent> callbackInfo) {
        final HUD hud = (HUD)ModuleManager.getModule(HUD.class);
        if (hud.getState() && hud.fontChatValue.asBoolean()) {
            if (!this.getChatOpen()) {
                callbackInfo.setReturnValue(null);
            }
            else {
                final ScaledResolution lvt_3_1_ = new ScaledResolution(this.mc);
                final int lvt_4_1_ = lvt_3_1_.getScaleFactor();
                final float lvt_5_1_ = this.getChatScale();
                int lvt_6_1_ = p_getChatComponent_1_ / lvt_4_1_ - 3;
                int lvt_7_1_ = p_getChatComponent_2_ / lvt_4_1_ - 27;
                lvt_6_1_ = MathHelper.floor_float(lvt_6_1_ / lvt_5_1_);
                lvt_7_1_ = MathHelper.floor_float(lvt_7_1_ / lvt_5_1_);
                if (lvt_6_1_ >= 0 && lvt_7_1_ >= 0) {
                    final int lvt_8_1_ = Math.min(this.getLineCount(), this.drawnChatLines.size());
                    if (lvt_6_1_ <= MathHelper.floor_float(this.getChatWidth() / this.getChatScale()) && lvt_7_1_ < Fonts.font40.FONT_HEIGHT * lvt_8_1_ + lvt_8_1_) {
                        final int lvt_9_1_ = lvt_7_1_ / Fonts.font40.FONT_HEIGHT + this.scrollPos;
                        if (lvt_9_1_ >= 0 && lvt_9_1_ < this.drawnChatLines.size()) {
                            final ChatLine lvt_10_1_ = this.drawnChatLines.get(lvt_9_1_);
                            int lvt_11_1_ = 0;
                            for (final IChatComponent lvt_13_1_ : lvt_10_1_.getChatComponent()) {
                                if (lvt_13_1_ instanceof ChatComponentText) {
                                    lvt_11_1_ += Fonts.font40.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)lvt_13_1_).getChatComponentText_TextValue(), false));
                                    if (lvt_11_1_ > lvt_6_1_) {
                                        callbackInfo.setReturnValue(lvt_13_1_);
                                        return;
                                    }
                                    continue;
                                }
                            }
                        }
                        callbackInfo.setReturnValue(null);
                    }
                    else {
                        callbackInfo.setReturnValue(null);
                    }
                }
                else {
                    callbackInfo.setReturnValue(null);
                }
            }
        }
    }
}
