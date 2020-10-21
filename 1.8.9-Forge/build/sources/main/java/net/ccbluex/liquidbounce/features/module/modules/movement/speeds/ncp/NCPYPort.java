
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class NCPYPort extends SpeedMode
{
    private int jumps;
    
    public NCPYPort() {
        super("NCPYPort");
    }
    
    @Override
    public void onMotion() {
        if (NCPYPort.mc.thePlayer.isOnLadder() || NCPYPort.mc.thePlayer.isInWater() || NCPYPort.mc.thePlayer.isInLava() || NCPYPort.mc.thePlayer.isInWeb || !MovementUtils.isMoving() || NCPYPort.mc.thePlayer.isInWater()) {
            return;
        }
        if (this.jumps >= 4 && NCPYPort.mc.thePlayer.onGround) {
            this.jumps = 0;
        }
        if (NCPYPort.mc.thePlayer.onGround) {
            NCPYPort.mc.thePlayer.motionY = ((this.jumps <= 1) ? 0.41999998688697815 : 0.4000000059604645);
            final float f = NCPYPort.mc.thePlayer.rotationYaw * 0.017453292f;
            final EntityPlayerSP thePlayer = NCPYPort.mc.thePlayer;
            thePlayer.motionX -= MathHelper.sin(f) * 0.2f;
            final EntityPlayerSP thePlayer2 = NCPYPort.mc.thePlayer;
            thePlayer2.motionZ += MathHelper.cos(f) * 0.2f;
            ++this.jumps;
        }
        else if (this.jumps <= 1) {
            NCPYPort.mc.thePlayer.motionY = -5.0;
        }
        MovementUtils.strafe();
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
