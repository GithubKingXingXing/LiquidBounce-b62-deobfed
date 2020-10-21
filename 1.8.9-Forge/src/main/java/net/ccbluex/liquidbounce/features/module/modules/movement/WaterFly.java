//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "WaterFly", description = "Allows you to move through water faster.", category = ModuleCategory.MOVEMENT)
public class WaterFly extends Module
{
    private final FloatValue motionValue;
    
    public WaterFly() {
        this.motionValue = new FloatValue("Motion", 0.5f, 0.1f, 1.0f);
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (WaterFly.mc.thePlayer.isInWater()) {
            event.setY(this.motionValue.asFloat());
            WaterFly.mc.thePlayer.motionY = this.motionValue.asFloat();
        }
    }
}
