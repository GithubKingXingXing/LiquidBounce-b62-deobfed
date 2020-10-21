
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "BugUp", description = "Automatically setbacks you after falling a certain distance.", category = ModuleCategory.MOVEMENT)
public class BugUp extends Module
{
    private final ListValue modeValue;
    private final FloatValue fallDistanceValue;
    private double prevX;
    private double prevY;
    private double prevZ;
    
    public BugUp() {
        this.modeValue = new ListValue("Mode", new String[] { "TeleportBack", "FlyFlag", "OnGroundSpoof" }, "FlyFlag");
        this.fallDistanceValue = new FloatValue("FallDistance", 2.0f, 1.0f, 5.0f);
    }
    
    @Override
    public void onDisable() {
        this.prevX = 0.0;
        this.prevY = 0.0;
        this.prevZ = 0.0;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent e) {
        if (BugUp.mc.thePlayer.onGround) {
            this.prevX = BugUp.mc.thePlayer.posX;
            this.prevY = BugUp.mc.thePlayer.posY;
            this.prevZ = BugUp.mc.thePlayer.posZ;
        }
        if (BugUp.mc.thePlayer.fallDistance > this.fallDistanceValue.asFloat()) {
            final String mode = this.modeValue.asString();
            final String lowerCase = mode.toLowerCase();
            switch (lowerCase) {
                case "teleportback": {
                    BugUp.mc.thePlayer.setPositionAndUpdate((double)(int)this.prevX, (double)(int)this.prevY, (double)(int)this.prevZ);
                    BugUp.mc.thePlayer.fallDistance = 0.0f;
                    BugUp.mc.thePlayer.motionY = 0.0;
                    break;
                }
                case "flyflag": {
                    final EntityPlayerSP thePlayer = BugUp.mc.thePlayer;
                    thePlayer.motionY += 0.1;
                    BugUp.mc.thePlayer.fallDistance = 0.0f;
                    break;
                }
                case "ongroundspoof": {
                    BugUp.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer(true));
                    break;
                }
            }
        }
    }
}
