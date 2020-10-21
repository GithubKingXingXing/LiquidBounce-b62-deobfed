//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.potion.Potion;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Sprint", description = "Automatically sprints all the time.", category = ModuleCategory.MOVEMENT)
public class Sprint extends Module
{
    public final BoolValue allDirectionsValue;
    public final BoolValue blindnessValue;
    public final BoolValue foodValue;
    public final BoolValue checkServerSide;
    
    public Sprint() {
        this.allDirectionsValue = new BoolValue("AllDirections", true);
        this.blindnessValue = new BoolValue("Blindness", true);
        this.foodValue = new BoolValue("Food", true);
        this.checkServerSide = new BoolValue("CheckServerSide", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("sprint", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1 && args[1].equalsIgnoreCase("alldirections")) {
                    Sprint.this.allDirectionsValue.setValue(!Sprint.this.allDirectionsValue.asBoolean());
                    this.chat("§7Sprint AllDirections was toggled §8" + (Sprint.this.allDirectionsValue.asBoolean() ? "on" : "off") + "§7.");
                    Sprint$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".sprint <alldirections>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!MovementUtils.isMoving() || Sprint.mc.thePlayer.isSneaking() || (this.blindnessValue.asBoolean() && Sprint.mc.thePlayer.isPotionActive(Potion.blindness)) || (this.foodValue.asBoolean() && Sprint.mc.thePlayer.getFoodStats().getFoodLevel() <= 6.0f && !Sprint.mc.thePlayer.capabilities.allowFlying) || (this.checkServerSide.asBoolean() && !this.allDirectionsValue.asBoolean() && RotationUtils.lookChanged && RotationUtils.getRotationDifference(Sprint.mc.thePlayer.rotationYaw, Sprint.mc.thePlayer.rotationPitch) > 30.0)) {
            Sprint.mc.thePlayer.setSprinting(false);
            return;
        }
        if (this.allDirectionsValue.asBoolean() || Sprint.mc.thePlayer.movementInput.moveForward >= 0.8f) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
    }
}
