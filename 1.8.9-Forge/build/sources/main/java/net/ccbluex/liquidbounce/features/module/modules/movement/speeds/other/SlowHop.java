//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class SlowHop extends SpeedMode
{
    public SlowHop() {
        super("SlowHop");
    }
    
    @Override
    public void onMotion() {
        if (SlowHop.mc.thePlayer.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (SlowHop.mc.thePlayer.onGround) {
                SlowHop.mc.thePlayer.jump();
            }
            else {
                MovementUtils.strafe(MovementUtils.getSpeed() * 1.011f);
            }
        }
        else {
            SlowHop.mc.thePlayer.motionX = 0.0;
            SlowHop.mc.thePlayer.motionZ = 0.0;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
