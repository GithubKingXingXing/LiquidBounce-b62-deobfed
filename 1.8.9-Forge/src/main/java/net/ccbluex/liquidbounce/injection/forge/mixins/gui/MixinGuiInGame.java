
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.utils.ClassUtils;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ GuiIngame.class })
public abstract class MixinGuiInGame
{
    @Shadow
    protected abstract void renderHotbarItem(final int p0, final int p1, final int p2, final float p3, final EntityPlayer p4);
    
    @Inject(method = { "renderScoreboard" }, at = { @At("HEAD") }, cancellable = true)
    private void renderScoreboard(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(NoScoreboard.class).getState()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "renderTooltip" }, at = { @At("HEAD") }, cancellable = true)
    private void renderTooltip(final ScaledResolution sr, final float partialTicks, final CallbackInfo callbackInfo) {
        final HUD hud = (HUD)ModuleManager.getModule(HUD.class);
        if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && hud.getState() && hud.blackHotbarValue.asBoolean()) {
            final EntityPlayer entityPlayer = (EntityPlayer)Minecraft.getMinecraft().getRenderViewEntity();
            final int middleScreen = sr.getScaledWidth() / 2;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GuiIngame.drawRect(middleScreen - 91, sr.getScaledHeight() - 24, middleScreen + 90, sr.getScaledHeight(), Integer.MIN_VALUE);
            GuiIngame.drawRect(middleScreen - 91 - 1 + entityPlayer.inventory.currentItem * 20 + 1, sr.getScaledHeight() - 24, middleScreen - 91 - 1 + entityPlayer.inventory.currentItem * 20 + 22, sr.getScaledHeight() - 22 - 1 + 24, Integer.MAX_VALUE);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            for (int j = 0; j < 9; ++j) {
                final int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                final int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityPlayer);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            LiquidBounce.CLIENT.eventManager.callEvent(new Render2DEvent(partialTicks));
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "renderTooltip" }, at = { @At("RETURN") })
    private void renderTooltipPost(final ScaledResolution sr, final float partialTicks, final CallbackInfo callbackInfo) {
        if (!ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
            LiquidBounce.CLIENT.eventManager.callEvent(new Render2DEvent(partialTicks));
        }
    }
    
    @Inject(method = { "renderPumpkinOverlay" }, at = { @At("HEAD") }, cancellable = true)
    private void renderPumpkinOverlay(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = (AntiBlind)ModuleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && antiBlind.getPumpkinEffect().asBoolean()) {
            callbackInfo.cancel();
        }
    }
}
