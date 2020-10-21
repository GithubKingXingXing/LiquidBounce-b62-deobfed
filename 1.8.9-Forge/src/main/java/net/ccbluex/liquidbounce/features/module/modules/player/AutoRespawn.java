//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiGameOver;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Ghost;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoRespawn", description = "Automatically respawns you after dying.", category = ModuleCategory.PLAYER)
public class AutoRespawn extends Module
{
    private final BoolValue instantValue;
    
    public AutoRespawn() {
        this.instantValue = new BoolValue("Instant", true);
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (ModuleManager.getModule(Ghost.class).getState()) {
            return;
        }
        if (this.instantValue.asBoolean()) {
            if (AutoRespawn.mc.thePlayer.getHealth() != 0.0f) {
                if (!AutoRespawn.mc.thePlayer.isDead) {
                    return;
                }
            }
        }
        else if (!(AutoRespawn.mc.currentScreen instanceof GuiGameOver) || ((GuiGameOver)AutoRespawn.mc.currentScreen).enableButtonsTimer < 20) {
            return;
        }
        AutoRespawn.mc.thePlayer.respawnPlayer();
        AutoRespawn.mc.displayGuiScreen((GuiScreen)null);
    }
}
