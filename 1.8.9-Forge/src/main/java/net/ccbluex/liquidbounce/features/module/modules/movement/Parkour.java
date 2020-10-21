//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Parkour", description = "Automatically jumps when reaching the edge of a block.", category = ModuleCategory.MOVEMENT)
public class Parkour extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (MovementUtils.isMoving() && Parkour.mc.thePlayer.onGround && !Parkour.mc.thePlayer.isSneaking() && !Parkour.mc.gameSettings.keyBindSneak.isKeyDown() && !Parkour.mc.gameSettings.keyBindJump.isKeyDown() && Parkour.mc.theWorld.getCollidingBoundingBoxes((Entity)Parkour.mc.thePlayer, Parkour.mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty()) {
            Parkour.mc.thePlayer.jump();
        }
    }
}
