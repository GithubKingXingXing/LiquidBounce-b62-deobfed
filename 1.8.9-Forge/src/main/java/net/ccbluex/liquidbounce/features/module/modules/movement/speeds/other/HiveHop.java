//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class HiveHop extends SpeedMode
{
    public HiveHop() {
        super("HiveHop");
    }
    
    @Override
    public void onEnable() {
        HiveHop.mc.thePlayer.speedInAir = 0.0425f;
        HiveHop.mc.timer.timerSpeed = 1.04f;
    }
    
    @Override
    public void onDisable() {
        HiveHop.mc.thePlayer.speedInAir = 0.02f;
        HiveHop.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (MovementUtils.isMoving()) {
            if (HiveHop.mc.thePlayer.onGround) {
                HiveHop.mc.thePlayer.motionY = 0.3;
            }
            HiveHop.mc.thePlayer.speedInAir = 0.0425f;
            HiveHop.mc.timer.timerSpeed = 1.04f;
            MovementUtils.strafe();
        }
        else {
            final EntityPlayerSP thePlayer = HiveHop.mc.thePlayer;
            final EntityPlayerSP thePlayer2 = HiveHop.mc.thePlayer;
            final double n = 0.0;
            thePlayer2.motionZ = n;
            thePlayer.motionX = n;
            HiveHop.mc.thePlayer.speedInAir = 0.02f;
            HiveHop.mc.timer.timerSpeed = 1.0f;
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
