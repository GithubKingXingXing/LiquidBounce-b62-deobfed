//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACLowHop2 extends SpeedMode
{
    private boolean legitJump;
    
    public AACLowHop2() {
        super("AACLowHop2");
    }
    
    @Override
    public void onEnable() {
        this.legitJump = true;
        AACLowHop2.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onDisable() {
        AACLowHop2.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onMotion() {
        AACLowHop2.mc.timer.timerSpeed = 1.0f;
        if (AACLowHop2.mc.thePlayer.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            AACLowHop2.mc.timer.timerSpeed = 1.09f;
            if (AACLowHop2.mc.thePlayer.onGround) {
                if (this.legitJump) {
                    AACLowHop2.mc.thePlayer.jump();
                    this.legitJump = false;
                    return;
                }
                AACLowHop2.mc.thePlayer.motionY = 0.34299999475479126;
                MovementUtils.strafe(0.534f);
            }
        }
        else {
            this.legitJump = true;
            AACLowHop2.mc.thePlayer.motionX = 0.0;
            AACLowHop2.mc.thePlayer.motionZ = 0.0;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
