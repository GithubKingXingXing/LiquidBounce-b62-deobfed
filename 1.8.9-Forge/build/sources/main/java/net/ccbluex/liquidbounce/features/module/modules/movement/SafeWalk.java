//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "SafeWalk", description = "Prevents you from falling down as if you were sneaking.", category = ModuleCategory.MOVEMENT)
public class SafeWalk extends Module
{
    private final BoolValue airSafeValue;
    
    public SafeWalk() {
        this.airSafeValue = new BoolValue("AirSafe", false);
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (this.airSafeValue.asBoolean() || SafeWalk.mc.thePlayer.onGround) {
            event.setSafeWalk(true);
        }
    }
}
