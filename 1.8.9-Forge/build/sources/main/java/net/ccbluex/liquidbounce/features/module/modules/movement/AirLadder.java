
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.block.BlockVine;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockLadder;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AirLadder", description = "Allows you to climb up ladders/vines without touching them.", category = ModuleCategory.MOVEMENT)
public class AirLadder extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if ((BlockUtils.getBlock(new BlockPos(AirLadder.mc.thePlayer.posX, AirLadder.mc.thePlayer.posY + 1.0, AirLadder.mc.thePlayer.posZ)) instanceof BlockLadder && AirLadder.mc.thePlayer.isCollidedHorizontally) || BlockUtils.getBlock(new BlockPos(AirLadder.mc.thePlayer.posX, AirLadder.mc.thePlayer.posY, AirLadder.mc.thePlayer.posZ)) instanceof BlockVine || BlockUtils.getBlock(new BlockPos(AirLadder.mc.thePlayer.posX, AirLadder.mc.thePlayer.posY + 1.0, AirLadder.mc.thePlayer.posZ)) instanceof BlockVine) {
            AirLadder.mc.thePlayer.motionY = 0.15;
            AirLadder.mc.thePlayer.motionX = 0.0;
            AirLadder.mc.thePlayer.motionZ = 0.0;
        }
    }
}
