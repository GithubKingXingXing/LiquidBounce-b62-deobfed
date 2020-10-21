
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACBHop extends SpeedMode
{
    public AACBHop() {
        super("AACBHop");
    }
    
    @Override
    public void onMotion() {
        if (AACBHop.mc.thePlayer.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            AACBHop.mc.timer.timerSpeed = 1.08f;
            if (AACBHop.mc.thePlayer.onGround) {
                AACBHop.mc.thePlayer.motionY = 0.399;
                final float f = AACBHop.mc.thePlayer.rotationYaw * 0.017453292f;
                final EntityPlayerSP thePlayer = AACBHop.mc.thePlayer;
                thePlayer.motionX -= MathHelper.sin(f) * 0.2f;
                final EntityPlayerSP thePlayer2 = AACBHop.mc.thePlayer;
                thePlayer2.motionZ += MathHelper.cos(f) * 0.2f;
                AACBHop.mc.timer.timerSpeed = 2.0f;
            }
            else {
                final EntityPlayerSP thePlayer3 = AACBHop.mc.thePlayer;
                thePlayer3.motionY *= 0.97;
                final EntityPlayerSP thePlayer4 = AACBHop.mc.thePlayer;
                thePlayer4.motionX *= 1.008;
                final EntityPlayerSP thePlayer5 = AACBHop.mc.thePlayer;
                thePlayer5.motionZ *= 1.008;
            }
        }
        else {
            AACBHop.mc.thePlayer.motionX = 0.0;
            AACBHop.mc.thePlayer.motionZ = 0.0;
            AACBHop.mc.timer.timerSpeed = 1.0f;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onDisable() {
        AACBHop.mc.timer.timerSpeed = 1.0f;
    }
}
