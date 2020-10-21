//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "WallGlide", description = "Allows you to glide down walls.", category = ModuleCategory.MOVEMENT)
public class WallGlide extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!WallGlide.mc.thePlayer.isCollidedHorizontally) {
            return;
        }
        WallGlide.mc.thePlayer.motionY = -0.19;
    }
}
