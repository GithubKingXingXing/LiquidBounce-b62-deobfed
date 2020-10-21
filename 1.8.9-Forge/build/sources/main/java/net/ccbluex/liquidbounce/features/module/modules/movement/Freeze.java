
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Freeze", description = "Allows you to stay stuck in mid air.", category = ModuleCategory.MOVEMENT)
public class Freeze extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        Freeze.mc.thePlayer.isDead = true;
        Freeze.mc.thePlayer.rotationYaw = Freeze.mc.thePlayer.cameraYaw;
        Freeze.mc.thePlayer.rotationPitch = Freeze.mc.thePlayer.cameraPitch;
    }
    
    @Override
    public void onDisable() {
        Freeze.mc.thePlayer.isDead = false;
    }
}
