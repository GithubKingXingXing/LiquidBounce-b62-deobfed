
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockAir;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACPort extends SpeedMode
{
    public AACPort() {
        super("AACPort");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        final float f = AACPort.mc.thePlayer.rotationYaw * 0.017453292f;
        for (double d = 0.2; d <= ((Speed)ModuleManager.getModule(Speed.class)).portMax.asFloat(); d += 0.2) {
            final double x = AACPort.mc.thePlayer.posX - MathHelper.sin(f) * d;
            final double z = AACPort.mc.thePlayer.posZ + MathHelper.cos(f) * d;
            if (AACPort.mc.thePlayer.posY < (int)AACPort.mc.thePlayer.posY + 0.5 && !(BlockUtils.getBlock(new BlockPos(x, AACPort.mc.thePlayer.posY, z)) instanceof BlockAir)) {
                break;
            }
            AACPort.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, AACPort.mc.thePlayer.posY, z, true));
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
