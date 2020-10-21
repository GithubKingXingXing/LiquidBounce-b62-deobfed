//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiChat;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "InventoryMove", description = "Allows you to walk while an inventory is opened.", category = ModuleCategory.MOVEMENT)
public class InventoryMove extends Module
{
    public final BoolValue aacAdditionProValue;
    public final BoolValue noMoveClicks;
    
    public InventoryMove() {
        this.aacAdditionProValue = new BoolValue("AACAdditionPro", false);
        this.noMoveClicks = new BoolValue("NoMoveClicks", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("inventorymove", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("aacadditionpro")) {
                        InventoryMove.this.aacAdditionProValue.setValue(!InventoryMove.this.aacAdditionProValue.asBoolean());
                        this.chat("§7InventoryMove AACAdditionPro was toggled §8" + (InventoryMove.this.aacAdditionProValue.asBoolean() ? "on" : "off") + "§7.");
                        InventoryMove$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("NoMoveClicks")) {
                        InventoryMove.this.noMoveClicks.setValue(!InventoryMove.this.noMoveClicks.asBoolean());
                        this.chat("§7InventoryMove NoMoveClicks was toggled §8" + (InventoryMove.this.noMoveClicks.asBoolean() ? "on" : "off") + "§7.");
                        InventoryMove$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                }
                this.chatSyntax(".inventorymove <aacadditionpro, nomoveclicks>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (InventoryMove.mc.thePlayer != null && !(InventoryMove.mc.currentScreen instanceof GuiChat) && !(InventoryMove.mc.currentScreen instanceof GuiIngameMenu)) {
            InventoryMove.mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindForward);
            InventoryMove.mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindBack);
            InventoryMove.mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindRight);
            InventoryMove.mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindLeft);
            InventoryMove.mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindJump);
            InventoryMove.mc.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindSprint);
        }
    }
    
    @Override
    public void onDisable() {
        if (!GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindForward) || InventoryMove.mc.currentScreen != null) {
            InventoryMove.mc.gameSettings.keyBindForward.pressed = false;
        }
        if (!GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindBack) || InventoryMove.mc.currentScreen != null) {
            InventoryMove.mc.gameSettings.keyBindBack.pressed = false;
        }
        if (!GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindRight) || InventoryMove.mc.currentScreen != null) {
            InventoryMove.mc.gameSettings.keyBindRight.pressed = false;
        }
        if (!GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindLeft) || InventoryMove.mc.currentScreen != null) {
            InventoryMove.mc.gameSettings.keyBindLeft.pressed = false;
        }
        if (!GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindJump) || InventoryMove.mc.currentScreen != null) {
            InventoryMove.mc.gameSettings.keyBindJump.pressed = false;
        }
        if (!GameSettings.isKeyDown(InventoryMove.mc.gameSettings.keyBindSprint) || InventoryMove.mc.currentScreen != null) {
            InventoryMove.mc.gameSettings.keyBindSprint.pressed = false;
        }
    }
    
    @Override
    public String getTag() {
        return this.aacAdditionProValue.asBoolean() ? "AACAdditionPro" : null;
    }
}
