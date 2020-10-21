//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class YPort2 extends SpeedMode
{
    public YPort2() {
        super("YPort2");
    }
    
    @Override
    public void onMotion() {
        if (YPort2.mc.thePlayer.isOnLadder() || YPort2.mc.thePlayer.isInWater() || YPort2.mc.thePlayer.isInLava() || YPort2.mc.thePlayer.isInWeb || !MovementUtils.isMoving()) {
            return;
        }
        if (YPort2.mc.thePlayer.onGround) {
            YPort2.mc.thePlayer.jump();
        }
        else {
            YPort2.mc.thePlayer.motionY = -1.0;
        }
        MovementUtils.strafe();
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
