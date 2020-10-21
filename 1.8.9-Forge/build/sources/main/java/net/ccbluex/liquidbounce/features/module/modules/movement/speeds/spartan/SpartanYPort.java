
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spartan;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class SpartanYPort extends SpeedMode
{
    private int airMoves;
    
    public SpartanYPort() {
        super("SpartanYPort");
    }
    
    @Override
    public void onMotion() {
        if (SpartanYPort.mc.gameSettings.keyBindForward.isKeyDown() && !SpartanYPort.mc.gameSettings.keyBindJump.isKeyDown()) {
            if (SpartanYPort.mc.thePlayer.onGround) {
                SpartanYPort.mc.thePlayer.jump();
                this.airMoves = 0;
            }
            else {
                SpartanYPort.mc.timer.timerSpeed = 1.08f;
                if (this.airMoves >= 3) {
                    SpartanYPort.mc.thePlayer.jumpMovementFactor = 0.0275f;
                }
                if (this.airMoves >= 4 && this.airMoves % 2 == 0.0) {
                    SpartanYPort.mc.thePlayer.motionY = -0.3199999928474426 - 0.009 * Math.random();
                    SpartanYPort.mc.thePlayer.jumpMovementFactor = 0.0238f;
                }
                ++this.airMoves;
            }
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
