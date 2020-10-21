//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoWalk", description = "Automatically makes you walk.", category = ModuleCategory.MOVEMENT)
public class AutoWalk extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        AutoWalk.mc.gameSettings.keyBindForward.pressed = true;
    }
    
    @Override
    public void onDisable() {
        if (!GameSettings.isKeyDown(AutoWalk.mc.gameSettings.keyBindForward)) {
            AutoWalk.mc.gameSettings.keyBindForward.pressed = false;
        }
    }
}
