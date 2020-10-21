
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "HeadRotations", description = "Allows you to see server-sided head rotations.", category = ModuleCategory.RENDER)
public class HeadRotations extends Module
{
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (RotationUtils.lastLook != null) {
            HeadRotations.mc.thePlayer.rotationYawHead = RotationUtils.lastLook[0];
        }
    }
}
