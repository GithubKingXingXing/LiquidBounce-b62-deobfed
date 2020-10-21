
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class SpectreOnGround extends SpeedMode
{
    private int speedUp;
    
    public SpectreOnGround() {
        super("SpectreOnGround");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
        if (!MovementUtils.isMoving() || SpectreOnGround.mc.thePlayer.movementInput.jump) {
            return;
        }
        if (this.speedUp >= 10) {
            if (SpectreOnGround.mc.thePlayer.onGround) {
                SpectreOnGround.mc.thePlayer.motionX = 0.0;
                SpectreOnGround.mc.thePlayer.motionZ = 0.0;
                this.speedUp = 0;
            }
            return;
        }
        if (SpectreOnGround.mc.thePlayer.onGround && SpectreOnGround.mc.gameSettings.keyBindForward.isKeyDown()) {
            final float f = SpectreOnGround.mc.thePlayer.rotationYaw * 0.017453292f;
            final EntityPlayerSP thePlayer = SpectreOnGround.mc.thePlayer;
            thePlayer.motionX -= MathHelper.sin(f) * 0.145f;
            final EntityPlayerSP thePlayer2 = SpectreOnGround.mc.thePlayer;
            thePlayer2.motionZ += MathHelper.cos(f) * 0.145f;
            event.setX(SpectreOnGround.mc.thePlayer.motionX);
            event.setY(0.005);
            event.setZ(SpectreOnGround.mc.thePlayer.motionZ);
            ++this.speedUp;
        }
    }
}
