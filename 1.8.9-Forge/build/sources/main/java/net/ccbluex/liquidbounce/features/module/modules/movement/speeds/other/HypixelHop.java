
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class HypixelHop extends SpeedMode
{
    public HypixelHop() {
        super("HypixelHop");
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            if (HypixelHop.mc.thePlayer.onGround) {
                HypixelHop.mc.thePlayer.jump();
                float speed = (MovementUtils.getSpeed() < 0.56f) ? (MovementUtils.getSpeed() * 1.045f) : 0.56f;
                if (HypixelHop.mc.thePlayer.onGround && HypixelHop.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    speed *= 1.0f + 0.13f * (1 + HypixelHop.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier());
                }
                MovementUtils.strafe(speed);
                return;
            }
            if (HypixelHop.mc.thePlayer.motionY < 0.2) {
                final EntityPlayerSP thePlayer = HypixelHop.mc.thePlayer;
                thePlayer.motionY -= 0.02;
            }
            MovementUtils.strafe(MovementUtils.getSpeed() * 1.01889f);
        }
        else {
            final EntityPlayerSP thePlayer2 = HypixelHop.mc.thePlayer;
            final EntityPlayerSP thePlayer3 = HypixelHop.mc.thePlayer;
            final double n = 0.0;
            thePlayer3.motionZ = n;
            thePlayer2.motionX = n;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
