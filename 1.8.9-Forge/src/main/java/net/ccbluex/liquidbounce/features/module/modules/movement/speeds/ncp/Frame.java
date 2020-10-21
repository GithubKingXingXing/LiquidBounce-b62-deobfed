//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class Frame extends SpeedMode
{
    private int motionTicks;
    private boolean move;
    private final TickTimer tickTimer;
    
    public Frame() {
        super("Frame");
        this.tickTimer = new TickTimer();
    }
    
    @Override
    public void onMotion() {
        if (Frame.mc.thePlayer.movementInput.moveForward > 0.0f || Frame.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
            final double speed = 4.25;
            if (Frame.mc.thePlayer.onGround) {
                Frame.mc.thePlayer.jump();
                if (this.motionTicks == 1) {
                    this.tickTimer.reset();
                    if (this.move) {
                        Frame.mc.thePlayer.motionX = 0.0;
                        Frame.mc.thePlayer.motionZ = 0.0;
                        this.move = false;
                    }
                    this.motionTicks = 0;
                }
                else {
                    this.motionTicks = 1;
                }
            }
            else if (!this.move && this.motionTicks == 1 && this.tickTimer.hasTimePassed(5)) {
                final EntityPlayerSP thePlayer = Frame.mc.thePlayer;
                thePlayer.motionX *= 4.25;
                final EntityPlayerSP thePlayer2 = Frame.mc.thePlayer;
                thePlayer2.motionZ *= 4.25;
                this.move = true;
            }
            if (!Frame.mc.thePlayer.onGround) {
                MovementUtils.strafe();
            }
            this.tickTimer.update();
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
