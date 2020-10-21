//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoClicker", description = "Constantly clicks when holding down a mouse button.", category = ModuleCategory.COMBAT)
public class AutoClicker extends Module
{
    private final IntegerValue maxCPSValue;
    private final IntegerValue minCPSValue;
    private final BoolValue rightValue;
    private final BoolValue leftValue;
    private final BoolValue jitterValue;
    private long rightDelay;
    private long rightLastSwing;
    private long leftDelay;
    private long leftLastSwing;
    
    public AutoClicker() {
        this.maxCPSValue = new IntegerValue("MaxCPS", 8, 1, 20) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int minCPS = AutoClicker.this.minCPSValue.asInteger();
                if (minCPS > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(minCPS);
                }
            }
        };
        this.minCPSValue = new IntegerValue("MinCPS", 5, 1, 20) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int maxCPS = AutoClicker.this.maxCPSValue.asInteger();
                if (maxCPS < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(maxCPS);
                }
            }
        };
        this.rightValue = new BoolValue("Right", true);
        this.leftValue = new BoolValue("Left", true);
        this.jitterValue = new BoolValue("Jitter", false);
        this.rightDelay = TimeUtils.randomClickDelay(this.minCPSValue.asInteger(), this.maxCPSValue.asInteger());
        this.leftDelay = TimeUtils.randomClickDelay(this.minCPSValue.asInteger(), this.maxCPSValue.asInteger());
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("autoclicker", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("maxcps")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (value <= 20) {
                                    if (value < 1) {
                                        this.chat("CPS can't lower than 1.");
                                        return;
                                    }
                                    if (AutoClicker.this.minCPSValue.asInteger() > value) {
                                        this.chat("MinCPS can't higher as MaxCPS!");
                                        return;
                                    }
                                    AutoClicker.this.minCPSValue.setValue(value);
                                    AutoClicker.this.rightDelay = TimeUtils.randomClickDelay(AutoClicker.this.minCPSValue.asInteger(), AutoClicker.this.maxCPSValue.asInteger());
                                    AutoClicker.this.leftDelay = TimeUtils.randomClickDelay(AutoClicker.this.minCPSValue.asInteger(), AutoClicker.this.maxCPSValue.asInteger());
                                    this.chat("§7AutoClicker maxCPS was set to §8" + value + "§7.");
                                    AutoClicker$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                else {
                                    this.chat("CPS can't higher as 20.");
                                }
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".autoclicker maxcps <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("mincps")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (value <= 20) {
                                    if (value < 1) {
                                        this.chat("CPS can't lower than 1.");
                                        return;
                                    }
                                    if (AutoClicker.this.maxCPSValue.asInteger() < value) {
                                        this.chat("MinCPS can't higher as MaxCPS!");
                                        return;
                                    }
                                    AutoClicker.this.minCPSValue.setValue(value);
                                    AutoClicker.this.rightDelay = TimeUtils.randomClickDelay(AutoClicker.this.minCPSValue.asInteger(), AutoClicker.this.maxCPSValue.asInteger());
                                    AutoClicker.this.leftDelay = TimeUtils.randomClickDelay(AutoClicker.this.minCPSValue.asInteger(), AutoClicker.this.maxCPSValue.asInteger());
                                    this.chat("§7AutoClicker MinCPS was set to §8" + value + "§7.");
                                    AutoClicker$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                else {
                                    this.chat("CPS can't higher as 20.");
                                }
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".autoclicker mincps <value>");
                        return;
                    }
                }
                this.chatSyntax(".autoclicker <mincps, maxcps>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (System.currentTimeMillis() - this.leftLastSwing >= this.leftDelay) {
            if (AutoClicker.mc.gameSettings.keyBindAttack.isKeyDown() && this.leftValue.asBoolean() && System.currentTimeMillis() - this.leftLastSwing >= this.leftDelay) {
                AutoClicker.mc.clickMouse();
                this.leftLastSwing = System.currentTimeMillis();
                this.leftDelay = TimeUtils.randomClickDelay(this.minCPSValue.asInteger(), this.maxCPSValue.asInteger());
            }
            if (AutoClicker.mc.gameSettings.keyBindUseItem.isKeyDown() && !AutoClicker.mc.thePlayer.isUsingItem() && this.rightValue.asBoolean() && System.currentTimeMillis() - this.rightLastSwing >= this.rightDelay) {
                AutoClicker.mc.rightClickMouse();
                this.rightLastSwing = System.currentTimeMillis();
                this.rightDelay = TimeUtils.randomClickDelay(this.minCPSValue.asInteger(), this.maxCPSValue.asInteger());
            }
        }
        if (this.jitterValue.asBoolean() && ((this.leftValue.asBoolean() && AutoClicker.mc.gameSettings.keyBindAttack.isKeyDown()) || (this.rightValue.asBoolean() && AutoClicker.mc.gameSettings.keyBindUseItem.isKeyDown() && !AutoClicker.mc.thePlayer.isUsingItem()))) {
            final boolean yaw = RandomUtils.getRandom().nextBoolean();
            final boolean pitch = RandomUtils.getRandom().nextBoolean();
            final boolean yawNegative = RandomUtils.getRandom().nextBoolean();
            final boolean pitchNegative = RandomUtils.getRandom().nextBoolean();
            if (yaw) {
                final EntityPlayerSP thePlayer = AutoClicker.mc.thePlayer;
                thePlayer.rotationYaw += (yawNegative ? (-RandomUtils.nextFloat(0.0f, 1.0f)) : RandomUtils.nextFloat(0.0f, 1.0f));
            }
            if (pitch) {
                final EntityPlayerSP thePlayer2 = AutoClicker.mc.thePlayer;
                thePlayer2.rotationPitch += (pitchNegative ? (-RandomUtils.nextFloat(0.0f, 1.0f)) : RandomUtils.nextFloat(0.0f, 1.0f));
                if (AutoClicker.mc.thePlayer.rotationPitch > 90.0f) {
                    AutoClicker.mc.thePlayer.rotationPitch = 90.0f;
                }
                else if (AutoClicker.mc.thePlayer.rotationPitch < -90.0f) {
                    AutoClicker.mc.thePlayer.rotationPitch = -90.0f;
                }
            }
        }
    }
}
