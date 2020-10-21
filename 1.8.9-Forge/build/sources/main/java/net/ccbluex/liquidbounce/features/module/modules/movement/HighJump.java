
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockPane;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "HighJump", description = "Allows you to jump higher.", category = ModuleCategory.MOVEMENT)
public class HighJump extends Module
{
    private final FloatValue heightValue;
    private final ListValue modeValue;
    private final BoolValue glassValue;
    
    public HighJump() {
        this.heightValue = new FloatValue("Height", 2.0f, 1.1f, 5.0f);
        this.modeValue = new ListValue("Mode", new String[] { "Vanilla", "Damage", "AACv3", "DAC", "Mineplex" }, "Vanilla");
        this.glassValue = new BoolValue("OnlyGlassPane", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("highjump", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("height")) {
                        if (args.length > 2) {
                            try {
                                final float height = Float.parseFloat(args[2]);
                                HighJump.this.heightValue.setValue(height);
                                this.chat("§7HighJump height was set to §8" + height + "§7.");
                                HighJump$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".highjump height <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && HighJump.this.modeValue.contains(args[2])) {
                            HighJump.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7HighJump mode was set to §8" + HighJump.this.modeValue.asString().toUpperCase() + "§7.");
                            HighJump$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".highjump mode §c<§8" + Strings.join(HighJump.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                }
                this.chatSyntax(".highjump <height, mode>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.glassValue.asBoolean() && !(BlockUtils.getBlock(new BlockPos(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ)) instanceof BlockPane)) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "damage": {
                if (HighJump.mc.thePlayer.hurtTime > 0 && HighJump.mc.thePlayer.onGround) {
                    final EntityPlayerSP thePlayer = HighJump.mc.thePlayer;
                    thePlayer.motionY += 0.42f * this.heightValue.asFloat();
                    break;
                }
                break;
            }
            case "aacv3": {
                if (!HighJump.mc.thePlayer.onGround) {
                    final EntityPlayerSP thePlayer2 = HighJump.mc.thePlayer;
                    thePlayer2.motionY += 0.059;
                    break;
                }
                break;
            }
            case "dac": {
                if (!HighJump.mc.thePlayer.onGround) {
                    final EntityPlayerSP thePlayer3 = HighJump.mc.thePlayer;
                    thePlayer3.motionY += 0.049999;
                    break;
                }
                break;
            }
            case "mineplex": {
                if (!HighJump.mc.thePlayer.onGround) {
                    MovementUtils.strafe(0.35f);
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (this.glassValue.asBoolean() && !(BlockUtils.getBlock(new BlockPos(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ)) instanceof BlockPane)) {
            return;
        }
        if (!HighJump.mc.thePlayer.onGround) {
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "mineplex": {
                    final EntityPlayerSP thePlayer = HighJump.mc.thePlayer;
                    thePlayer.motionY += ((HighJump.mc.thePlayer.fallDistance == 0.0f) ? 0.0499 : 0.05);
                    break;
                }
            }
        }
    }
    
    @EventTarget
    public void onJump(final JumpEvent event) {
        if (this.glassValue.asBoolean() && !(BlockUtils.getBlock(new BlockPos(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ)) instanceof BlockPane)) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "vanilla": {
                event.setMotion(event.getMotion() * this.heightValue.asFloat());
                break;
            }
            case "mineplex": {
                event.setMotion(0.47f);
                break;
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
