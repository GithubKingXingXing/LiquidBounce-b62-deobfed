
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventListener;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACHop350 extends SpeedMode implements EventListener
{
    public AACHop350() {
        super("AACHop3.5.0");
        LiquidBounce.CLIENT.eventManager.registerListener(this);
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (event.getEventState() == EventState.POST && MovementUtils.isMoving() && !AACHop350.mc.thePlayer.isInWater() && !AACHop350.mc.thePlayer.isInLava()) {
            final EntityPlayerSP thePlayer = AACHop350.mc.thePlayer;
            thePlayer.jumpMovementFactor += 0.00208f;
            if (AACHop350.mc.thePlayer.fallDistance <= 1.0f) {
                if (AACHop350.mc.thePlayer.onGround) {
                    AACHop350.mc.thePlayer.jump();
                    final EntityPlayerSP thePlayer2 = AACHop350.mc.thePlayer;
                    thePlayer2.motionX *= 1.0118000507354736;
                    final EntityPlayerSP thePlayer3 = AACHop350.mc.thePlayer;
                    thePlayer3.motionZ *= 1.0118000507354736;
                }
                else {
                    final EntityPlayerSP thePlayer4 = AACHop350.mc.thePlayer;
                    thePlayer4.motionY -= 0.014700000174343586;
                    final EntityPlayerSP thePlayer5 = AACHop350.mc.thePlayer;
                    thePlayer5.motionX *= 1.0013799667358398;
                    final EntityPlayerSP thePlayer6 = AACHop350.mc.thePlayer;
                    thePlayer6.motionZ *= 1.0013799667358398;
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        if (AACHop350.mc.thePlayer.onGround) {
            final EntityPlayerSP thePlayer = AACHop350.mc.thePlayer;
            final EntityPlayerSP thePlayer2 = AACHop350.mc.thePlayer;
            final double n = 0.0;
            thePlayer2.motionZ = n;
            thePlayer.motionX = n;
        }
    }
    
    @Override
    public void onDisable() {
        AACHop350.mc.thePlayer.jumpMovementFactor = 0.02f;
    }
    
    @Override
    public boolean handleEvents() {
        return this.isActive();
    }
}
