//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "LadderJump", description = "Boosts you up when touching a ladder.", category = ModuleCategory.MOVEMENT)
public class LadderJump extends Module
{
    static boolean jumped;
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (LadderJump.mc.thePlayer.onGround) {
            if (LadderJump.mc.thePlayer.isOnLadder()) {
                LadderJump.mc.thePlayer.motionY = 1.5;
                LadderJump.jumped = true;
            }
            else {
                LadderJump.jumped = false;
            }
        }
        else if (!LadderJump.mc.thePlayer.isOnLadder() && LadderJump.jumped) {
            final EntityPlayerSP thePlayer = LadderJump.mc.thePlayer;
            thePlayer.motionY += 0.059;
        }
    }
}
