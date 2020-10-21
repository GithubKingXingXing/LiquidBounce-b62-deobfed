
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoClip", description = "Allows you to freely move through walls (A sandblock has to fall on your head).", category = ModuleCategory.MOVEMENT)
public class NoClip extends Module
{
    @Override
    public void onDisable() {
        if (NoClip.mc.thePlayer == null) {
            return;
        }
        NoClip.mc.thePlayer.noClip = false;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        NoClip.mc.thePlayer.noClip = true;
        NoClip.mc.thePlayer.fallDistance = 0.0f;
        NoClip.mc.thePlayer.onGround = false;
        NoClip.mc.thePlayer.capabilities.isFlying = false;
        NoClip.mc.thePlayer.motionX = 0.0;
        NoClip.mc.thePlayer.motionY = 0.0;
        NoClip.mc.thePlayer.motionZ = 0.0;
        final float speed = 0.32f;
        NoClip.mc.thePlayer.jumpMovementFactor = speed;
        if (NoClip.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP thePlayer = NoClip.mc.thePlayer;
            thePlayer.motionY += speed;
        }
        if (NoClip.mc.gameSettings.keyBindSneak.isKeyDown()) {
            final EntityPlayerSP thePlayer2 = NoClip.mc.thePlayer;
            thePlayer2.motionY -= speed;
        }
    }
}
