//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.minecraft.event.HoverEvent;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import java.util.Collections;
import net.ccbluex.liquidbounce.features.module.modules.misc.ComponentOnHover;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.BackgroundShader;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.utils.render.ParticleUtils;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ GuiScreen.class })
public abstract class MixinGuiScreen
{
    @Shadow
    public Minecraft mc;
    @Shadow
    protected List<GuiButton> buttonList;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    protected FontRenderer fontRendererObj;
    
    @Shadow
    public void updateScreen() {
    }
    
    @Shadow
    protected abstract void handleComponentHover(final IChatComponent p0, final int p1, final int p2);
    
    @Shadow
    protected abstract void drawHoveringText(final List<String> p0, final int p1, final int p2);
    
    @Inject(method = { "drawWorldBackground" }, at = { @At("HEAD") })
    private void drawWorldBackground(final CallbackInfo callbackInfo) {
        final HUD hud = (HUD)ModuleManager.getModule(HUD.class);
        if (hud.inventoryParticle.asBoolean() && this.mc.thePlayer != null) {
            final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            final int width = scaledResolution.getScaledWidth();
            final int height = scaledResolution.getScaledHeight();
            ParticleUtils.drawParticles(Mouse.getX() * width / this.mc.displayWidth, height - Mouse.getY() * height / this.mc.displayHeight - 1);
        }
    }
    
    @Overwrite
    public void drawBackground(final int i) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        if (LiquidBounce.CLIENT.background == null) {
            BackgroundShader.BACKGROUND_SHADER.startShader();
            final Tessellator instance = Tessellator.getInstance();
            final WorldRenderer worldRenderer = instance.getWorldRenderer();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
            worldRenderer.pos(0.0, (double)this.height, 0.0).endVertex();
            worldRenderer.pos((double)this.width, (double)this.height, 0.0).endVertex();
            worldRenderer.pos((double)this.width, 0.0, 0.0).endVertex();
            worldRenderer.pos(0.0, 0.0, 0.0).endVertex();
            instance.draw();
            BackgroundShader.BACKGROUND_SHADER.stopShader();
        }
        else {
            final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            final int width = scaledResolution.getScaledWidth();
            final int height = scaledResolution.getScaledHeight();
            this.mc.getTextureManager().bindTexture(LiquidBounce.CLIENT.background);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, width, height, width, height, (float)width, (float)height);
            ParticleUtils.drawParticles(Mouse.getX() * width / this.mc.displayWidth, height - Mouse.getY() * height / this.mc.displayHeight - 1);
        }
    }
    
    @Inject(method = { "sendChatMessage(Ljava/lang/String;Z)V" }, at = { @At("HEAD") }, cancellable = true)
    private void messageSend(final String msg, final boolean addToChat, final CallbackInfo callbackInfo) {
        if (msg.startsWith(".") && addToChat) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
            LiquidBounce.CLIENT.commandManager.executeCommands(msg);
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "handleComponentHover" }, at = { @At("HEAD") })
    private void handleHoverOverComponent(final IChatComponent component, final int x, final int y, final CallbackInfo callbackInfo) {
        if (component == null || component.getChatStyle().getChatClickEvent() == null || !ModuleManager.getModule(ComponentOnHover.class).getState()) {
            return;
        }
        final ChatStyle chatStyle = component.getChatStyle();
        final ClickEvent clickEvent = chatStyle.getChatClickEvent();
        final HoverEvent hoverEvent = chatStyle.getChatHoverEvent();
        this.drawHoveringText(Collections.singletonList("§c§l" + clickEvent.getAction().getCanonicalName().toUpperCase() + ": §a" + clickEvent.getValue()), x, y - ((hoverEvent != null) ? 17 : 0));
    }
}
