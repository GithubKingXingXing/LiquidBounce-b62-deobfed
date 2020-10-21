
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACGround extends SpeedMode
{
    public AACGround() {
        super("AACGround");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        AACGround.mc.timer.timerSpeed = ((Speed)ModuleManager.getModule(Speed.class)).aacGroundTimerValue.asFloat();
        AACGround.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(AACGround.mc.thePlayer.posX, AACGround.mc.thePlayer.posY, AACGround.mc.thePlayer.posZ, true));
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onDisable() {
        AACGround.mc.timer.timerSpeed = 1.0f;
    }
}
