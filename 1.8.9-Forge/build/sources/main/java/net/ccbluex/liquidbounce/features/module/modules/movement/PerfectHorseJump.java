
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "PerfectHorseJump", description = "Automatically jumps when the jump bar of a horse is filled up completely.", category = ModuleCategory.MOVEMENT)
public class PerfectHorseJump extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        PerfectHorseJump.mc.thePlayer.horseJumpPowerCounter = 9;
        PerfectHorseJump.mc.thePlayer.horseJumpPower = 1.0f;
    }
}
