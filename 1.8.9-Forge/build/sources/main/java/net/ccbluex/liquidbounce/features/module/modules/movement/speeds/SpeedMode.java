//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class SpeedMode
{
    public final String modeName;
    protected static final Minecraft mc;
    
    public SpeedMode(final String modeName) {
        this.modeName = modeName;
    }
    
    public boolean isActive() {
        final Speed speed = (Speed)ModuleManager.getModule(Speed.class);
        return speed != null && !SpeedMode.mc.thePlayer.isSneaking() && speed.getState() && speed.modeValue.asString().equals(this.modeName);
    }
    
    public abstract void onMotion();
    
    public abstract void onUpdate();
    
    public abstract void onMove(final MoveEvent p0);
    
    public void onTick() {
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
