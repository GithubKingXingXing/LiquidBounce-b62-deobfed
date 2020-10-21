//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Eagle", description = "Makes you eagle (aka. FastBridge).", category = ModuleCategory.PLAYER)
public class Eagle extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        Eagle.mc.gameSettings.keyBindSneak.pressed = (Eagle.mc.theWorld.getBlockState(new BlockPos(Eagle.mc.thePlayer.posX, Eagle.mc.thePlayer.posY - 1.0, Eagle.mc.thePlayer.posZ)).getBlock() == Blocks.air);
    }
    
    @Override
    public void onDisable() {
        if (Eagle.mc.thePlayer == null) {
            return;
        }
        if (!GameSettings.isKeyDown(Eagle.mc.gameSettings.keyBindSneak)) {
            Eagle.mc.gameSettings.keyBindSneak.pressed = false;
        }
    }
}
