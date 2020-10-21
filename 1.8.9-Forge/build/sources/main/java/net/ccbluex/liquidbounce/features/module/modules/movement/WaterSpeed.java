//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockLiquid;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "WaterSpeed", description = "Allows you to swim faster.", category = ModuleCategory.MOVEMENT)
public class WaterSpeed extends Module
{
    private final FloatValue speedValue;
    
    public WaterSpeed() {
        this.speedValue = new FloatValue("Speed", 1.2f, 1.1f, 1.5f);
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (WaterSpeed.mc.thePlayer.isInWater() && BlockUtils.getBlock(WaterSpeed.mc.thePlayer.getPosition()) instanceof BlockLiquid) {
            final float speed = this.speedValue.asFloat();
            final EntityPlayerSP thePlayer = WaterSpeed.mc.thePlayer;
            thePlayer.motionX *= speed;
            final EntityPlayerSP thePlayer2 = WaterSpeed.mc.thePlayer;
            thePlayer2.motionZ *= speed;
        }
    }
}
