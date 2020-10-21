//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Tracers", description = "Draws a line to targets around you.", category = ModuleCategory.RENDER)
public class Tracers extends Module
{
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        for (final Entity entity : Tracers.mc.theWorld.loadedEntityList) {
            if (entity != null && entity != Tracers.mc.thePlayer && EntityUtils.isSelected(entity, false)) {
                RenderUtils.drawTracer(entity, EntityUtils.isFriend(entity) ? new Color(0, 0, 255, 150) : new Color(255, 255, 255, 150));
            }
        }
    }
}
