//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AntiAFK", description = "Prevents you from getting kicked for being AFK.", category = ModuleCategory.PLAYER)
public class AntiAFK extends Module
{
    private final MSTimer timer;
    
    public AntiAFK() {
        this.timer = new MSTimer();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        AntiAFK.mc.gameSettings.keyBindForward.pressed = true;
        if (this.timer.hasTimePassed(500L)) {
            final EntityPlayerSP thePlayer = AntiAFK.mc.thePlayer;
            thePlayer.rotationYaw += 180.0f;
            this.timer.reset();
        }
    }
    
    @Override
    public void onDisable() {
        if (!GameSettings.isKeyDown(AntiAFK.mc.gameSettings.keyBindForward)) {
            AntiAFK.mc.gameSettings.keyBindForward.pressed = false;
        }
    }
}
