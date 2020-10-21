//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.client.gui.GuiChat;
import net.ccbluex.liquidbounce.event.events.ScreenEvent;
import net.ccbluex.liquidbounce.event.events.KeyEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "HUD", description = "Toggles visibility of the HUD.", category = ModuleCategory.RENDER)
@SideOnly(Side.CLIENT)
public class HUD extends Module
{
    public final BoolValue blackHotbarValue;
    public final BoolValue inventoryParticle;
    private final BoolValue blurValue;
    public final BoolValue fontChatValue;
    
    public HUD() {
        this.blackHotbarValue = new BoolValue("BlackHotbar", true);
        this.inventoryParticle = new BoolValue("InventoryParticle", false);
        this.blurValue = new BoolValue("Blur", false);
        this.fontChatValue = new BoolValue("FontChat", false);
        this.state = true;
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("hud", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("blackhotbar")) {
                        HUD.this.blackHotbarValue.setValue(!HUD.this.blackHotbarValue.asBoolean());
                        this.chat("§7HUD BlackHotbar was toggled §8" + (HUD.this.blackHotbarValue.asBoolean() ? "on" : "off") + "§7.");
                        HUD$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("Blur")) {
                        HUD.this.blurValue.setValue(!HUD.this.blurValue.asBoolean());
                        this.chat("§7HUD Blur was toggled §8" + (HUD.this.blurValue.asBoolean() ? "on" : "off") + "§7.");
                        HUD$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                }
                this.chatSyntax(".hud <blackhotbar, blur>");
            }
        });
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (LiquidBounce.CLIENT.hud == null || HUD.mc.currentScreen instanceof GuiHudDesigner) {
            return;
        }
        LiquidBounce.CLIENT.hud.render();
    }
    
    @EventTarget
    public void onKey(final KeyEvent event) {
        LiquidBounce.CLIENT.hud.handleKey('a', event.getKey());
    }
    
    @EventTarget(ignoreCondition = true)
    public void onScreenChange(final ScreenEvent event) {
        if (HUD.mc.theWorld == null || HUD.mc.thePlayer == null) {
            return;
        }
        if (this.getState() && event.getGuiScreen() != null && !(event.getGuiScreen() instanceof GuiChat) && !(event.getGuiScreen() instanceof GuiHudDesigner) && this.blurValue.asBoolean()) {
            HUD.mc.entityRenderer.loadShader(new ResourceLocation("LiquidBounce".toLowerCase() + "/blur.json"));
        }
        else {
            HUD.mc.entityRenderer.stopUseShader();
        }
    }
    
    @Override
    public boolean showArray() {
        return false;
    }
}
