//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.ClickBlockEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoTool", description = "Automatically selects the best tool in your inventory to mine a block.", category = ModuleCategory.PLAYER)
public class AutoTool extends Module
{
    @EventTarget
    public void onClick(final ClickBlockEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        this.switchSlot(event.getClickedBlock());
    }
    
    public void switchSlot(final BlockPos blockPos) {
        float bestSpeed = 1.0f;
        int bestSlot = -1;
        final Block block = AutoTool.mc.theWorld.getBlockState(blockPos).getBlock();
        for (int i = 0; i < 9; ++i) {
            final ItemStack item = AutoTool.mc.thePlayer.inventory.getStackInSlot(i);
            if (item != null) {
                final float speed = item.getStrVsBlock(block);
                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }
        }
        if (bestSlot != -1) {
            AutoTool.mc.thePlayer.inventory.currentItem = bestSlot;
        }
    }
}
