
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.init.Blocks;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoBreak", description = "Automatically breaks the block you are looking at.", category = ModuleCategory.WORLD)
public class AutoBreak extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (AutoBreak.mc.objectMouseOver == null || AutoBreak.mc.objectMouseOver.getBlockPos() == null) {
            return;
        }
        AutoBreak.mc.gameSettings.keyBindAttack.pressed = (AutoBreak.mc.theWorld.getBlockState(AutoBreak.mc.objectMouseOver.getBlockPos()).getBlock() != Blocks.air);
    }
    
    @Override
    public void onDisable() {
        if (!GameSettings.isKeyDown(AutoBreak.mc.gameSettings.keyBindAttack)) {
            AutoBreak.mc.gameSettings.keyBindAttack.pressed = false;
        }
    }
}
