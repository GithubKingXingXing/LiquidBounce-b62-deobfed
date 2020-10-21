//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACLowHop3 extends SpeedMode
{
    private boolean firstJump;
    private boolean waitForGround;
    
    public AACLowHop3() {
        super("AACLowHop3");
    }
    
    @Override
    public void onEnable() {
        this.firstJump = true;
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            if (AACLowHop3.mc.thePlayer.hurtTime <= 0) {
                if (AACLowHop3.mc.thePlayer.onGround) {
                    this.waitForGround = false;
                    if (!this.firstJump) {
                        this.firstJump = true;
                    }
                    AACLowHop3.mc.thePlayer.jump();
                    AACLowHop3.mc.thePlayer.motionY = 0.41;
                }
                else {
                    if (this.waitForGround) {
                        return;
                    }
                    if (AACLowHop3.mc.thePlayer.isCollidedHorizontally) {
                        return;
                    }
                    this.firstJump = false;
                    final EntityPlayerSP thePlayer = AACLowHop3.mc.thePlayer;
                    thePlayer.motionY -= 0.0149;
                }
                if (!AACLowHop3.mc.thePlayer.isCollidedHorizontally) {
                    MovementUtils.forward(this.firstJump ? 0.0016 : 0.001799);
                }
            }
            else {
                this.firstJump = true;
                this.waitForGround = true;
            }
        }
        else {
            AACLowHop3.mc.thePlayer.motionZ = 0.0;
            AACLowHop3.mc.thePlayer.motionX = 0.0;
        }
        final double speed = MovementUtils.getSpeed();
        AACLowHop3.mc.thePlayer.motionX = -(Math.sin(MovementUtils.getDirection()) * speed);
        AACLowHop3.mc.thePlayer.motionZ = Math.cos(MovementUtils.getDirection()) * speed;
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
