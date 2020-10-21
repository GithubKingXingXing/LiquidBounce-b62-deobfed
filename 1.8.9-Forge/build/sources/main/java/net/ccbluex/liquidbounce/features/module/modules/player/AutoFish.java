
package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.item.ItemFishingRod;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoFish", description = "Automatically catches fish when using a rod.", category = ModuleCategory.PLAYER)
public class AutoFish extends Module
{
    private final MSTimer rodOutTimer;
    
    public AutoFish() {
        this.rodOutTimer = new MSTimer();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (AutoFish.mc.thePlayer.getHeldItem() == null || !(AutoFish.mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod)) {
            return;
        }
        if ((this.rodOutTimer.hasTimePassed(500L) && AutoFish.mc.thePlayer.fishEntity == null) || (AutoFish.mc.thePlayer.fishEntity != null && AutoFish.mc.thePlayer.fishEntity.motionX == 0.0 && AutoFish.mc.thePlayer.fishEntity.motionZ == 0.0 && AutoFish.mc.thePlayer.fishEntity.motionY != 0.0)) {
            AutoFish.mc.rightClickMouse();
            this.rodOutTimer.reset();
        }
    }
}
