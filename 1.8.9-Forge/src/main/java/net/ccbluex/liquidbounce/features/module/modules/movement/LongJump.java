//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "LongJump", description = "Allows you to jump further.", category = ModuleCategory.MOVEMENT)
public class LongJump extends Module
{
    private final ListValue modeValue;
    private final FloatValue ncpBoostValue;
    private final BoolValue autoJumpValue;
    private boolean jumped;
    private boolean canBoost;
    private boolean teleported;
    private boolean canMineplexBoost;
    
    public LongJump() {
        this.modeValue = new ListValue("Mode", new String[] { "NCP", "AACv1", "AACv2", "AACv3", "Mineplex", "Mineplex2", "Mineplex3" }, "NCP");
        this.ncpBoostValue = new FloatValue("NCPBoost", 4.25f, 1.0f, 10.0f);
        this.autoJumpValue = new BoolValue("AutoJump", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("longjump", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && LongJump.this.modeValue.contains(args[2])) {
                            LongJump.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7LongJump mode was set to §8" + LongJump.this.modeValue.asString().toUpperCase() + "§7.");
                            LongJump$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".longjump mode §c<§8" + Strings.join(LongJump.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("ncpboost")) {
                        if (args.length > 2) {
                            try {
                                final float value = Float.parseFloat(args[2]);
                                LongJump.this.ncpBoostValue.setValue(value);
                                this.chat("§7LongJump NCP-Boost was set to §8" + value + "§7.");
                                LongJump$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".longjump ncpboost <value>");
                        return;
                    }
                }
                this.chatSyntax(".longjump <mode, ncpboost>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (LadderJump.jumped) {
            MovementUtils.strafe(MovementUtils.getSpeed() * 1.08f);
        }
        final String noCheat = this.modeValue.asString();
        if (this.jumped) {
            if (LongJump.mc.thePlayer.onGround) {
                this.jumped = false;
                this.canMineplexBoost = false;
                if (noCheat.equalsIgnoreCase("NCP")) {
                    LongJump.mc.thePlayer.motionX = 0.0;
                    LongJump.mc.thePlayer.motionZ = 0.0;
                }
                return;
            }
            final String lowerCase = noCheat.toLowerCase();
            switch (lowerCase) {
                case "ncp": {
                    MovementUtils.strafe(MovementUtils.getSpeed() * (this.canBoost ? this.ncpBoostValue.asFloat() : 1.0f));
                    this.canBoost = false;
                    break;
                }
                case "aacv1": {
                    final EntityPlayerSP thePlayer = LongJump.mc.thePlayer;
                    thePlayer.motionY += 0.05999;
                    MovementUtils.strafe(MovementUtils.getSpeed() * 1.08f);
                    break;
                }
                case "aacv2":
                case "mineplex3": {
                    LongJump.mc.thePlayer.jumpMovementFactor = 0.09f;
                    final EntityPlayerSP thePlayer2 = LongJump.mc.thePlayer;
                    thePlayer2.motionY += 0.01321;
                    LongJump.mc.thePlayer.jumpMovementFactor = 0.08f;
                    MovementUtils.strafe();
                    break;
                }
                case "aacv3": {
                    final EntityPlayerSP player = LongJump.mc.thePlayer;
                    if (player.fallDistance > 0.5f && !this.teleported) {
                        final double value = 3.0;
                        final EnumFacing horizontalFacing = player.getHorizontalFacing();
                        double x = 0.0;
                        double z = 0.0;
                        switch (horizontalFacing) {
                            case NORTH: {
                                z = -value;
                                break;
                            }
                            case EAST: {
                                x = value;
                                break;
                            }
                            case SOUTH: {
                                z = value;
                                break;
                            }
                            case WEST: {
                                x = -value;
                                break;
                            }
                        }
                        player.setPosition(player.posX + x, player.posY, player.posZ + z);
                        this.teleported = true;
                        break;
                    }
                    break;
                }
                case "mineplex": {
                    final EntityPlayerSP thePlayer3 = LongJump.mc.thePlayer;
                    thePlayer3.motionY += 0.01321;
                    LongJump.mc.thePlayer.jumpMovementFactor = 0.08f;
                    MovementUtils.strafe();
                    break;
                }
                case "mineplex2": {
                    if (!this.canMineplexBoost) {
                        break;
                    }
                    LongJump.mc.thePlayer.jumpMovementFactor = 0.1f;
                    if (LongJump.mc.thePlayer.fallDistance > 1.5f) {
                        LongJump.mc.thePlayer.jumpMovementFactor = 0.0f;
                        LongJump.mc.thePlayer.motionY = -10.0;
                    }
                    MovementUtils.strafe();
                    break;
                }
            }
        }
        if (this.autoJumpValue.asBoolean() && LongJump.mc.thePlayer.onGround && MovementUtils.isMoving()) {
            this.jumped = true;
            LongJump.mc.thePlayer.jump();
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if ("mineplex3".equalsIgnoreCase(this.modeValue.asString()) && LongJump.mc.thePlayer.fallDistance != 0.0f) {
            final EntityPlayerSP thePlayer = LongJump.mc.thePlayer;
            thePlayer.motionY += 0.037;
        }
    }
    
    @EventTarget(ignoreCondition = true)
    public void onJump(final JumpEvent event) {
        this.jumped = true;
        this.canBoost = true;
        this.teleported = false;
        if (this.getState()) {
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "mineplex": {
                    event.setMotion(event.getMotion() * 4.08f);
                    break;
                }
                case "mineplex2": {
                    if (LongJump.mc.thePlayer.isCollidedHorizontally) {
                        event.setMotion(2.31f);
                        this.canMineplexBoost = true;
                        LongJump.mc.thePlayer.onGround = false;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
