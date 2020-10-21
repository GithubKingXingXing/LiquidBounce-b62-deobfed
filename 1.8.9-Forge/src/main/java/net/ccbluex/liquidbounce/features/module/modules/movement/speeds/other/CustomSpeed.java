//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class CustomSpeed extends SpeedMode
{
    public CustomSpeed() {
        super("Custom");
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            final Speed speed = (Speed)ModuleManager.getModule(Speed.class);
            if (speed == null) {
                return;
            }
            CustomSpeed.mc.timer.timerSpeed = speed.customTimerValue.asFloat();
            if (CustomSpeed.mc.thePlayer.onGround) {
                MovementUtils.strafe(speed.customSpeedValue.asFloat());
                CustomSpeed.mc.thePlayer.motionY = speed.customYValue.asFloat();
            }
            else if (speed.customStrafeValue.asBoolean()) {
                MovementUtils.strafe(speed.customSpeedValue.asFloat());
            }
            else {
                MovementUtils.strafe();
            }
        }
        else {
            final EntityPlayerSP thePlayer = CustomSpeed.mc.thePlayer;
            final EntityPlayerSP thePlayer2 = CustomSpeed.mc.thePlayer;
            final double n = 0.0;
            thePlayer2.motionZ = n;
            thePlayer.motionX = n;
        }
    }
    
    @Override
    public void onEnable() {
        final Speed speed = (Speed)ModuleManager.getModule(Speed.class);
        if (speed == null) {
            return;
        }
        if (speed.resetXZValue.asBoolean()) {
            final EntityPlayerSP thePlayer = CustomSpeed.mc.thePlayer;
            final EntityPlayerSP thePlayer2 = CustomSpeed.mc.thePlayer;
            final double n = 0.0;
            thePlayer2.motionZ = n;
            thePlayer.motionX = n;
        }
        if (speed.resetYValue.asBoolean()) {
            CustomSpeed.mc.thePlayer.motionY = 0.0;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        CustomSpeed.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
