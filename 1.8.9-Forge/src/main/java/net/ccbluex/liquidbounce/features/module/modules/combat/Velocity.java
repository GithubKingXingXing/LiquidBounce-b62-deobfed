//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.utils.MovementUtils;
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

@ModuleInfo(name = "Velocity", description = "Allows you to modify the amount of knowckback you take.", category = ModuleCategory.COMBAT)
public class Velocity extends Module
{
    private final FloatValue horizontalValue;
    private final FloatValue verticalValue;
    private final ListValue modeValue;
    private final FloatValue reverseStrengthValue;
    private final FloatValue reverse2StrenghtValue;
    private final FloatValue aacPushXZReducerValue;
    private final BoolValue aacPushYReducerValue;
    private long velocityTime;
    private boolean gotVelocity;
    private boolean gotHurt;
    
    public Velocity() {
        this.horizontalValue = new FloatValue("Horizontal", 0.0f, 0.0f, 1.0f);
        this.verticalValue = new FloatValue("Vertical", 0.0f, 0.0f, 1.0f);
        this.modeValue = new ListValue("Mode", new String[] { "Simple", "AAC", "AACPush", "AACZero", "Jump", "Reverse", "Reverse2", "Glitch" }, "Simple");
        this.reverseStrengthValue = new FloatValue("ReverseStrength", 1.0f, 0.1f, 1.0f);
        this.reverse2StrenghtValue = new FloatValue("Reverse2Strength", 0.05f, 0.02f, 0.1f);
        this.aacPushXZReducerValue = new FloatValue("AACPushXZReducer", 2.0f, 1.0f, 3.0f);
        this.aacPushYReducerValue = new BoolValue("AACPushYReducer", true);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("velocity", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("horizontal")) {
                        if (args.length > 2) {
                            try {
                                final float value = Float.parseFloat(args[2]);
                                Velocity.this.horizontalValue.setValue(value);
                                this.chat("§7Velocity horizontal was set to §8" + value + "§7.");
                                Velocity$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".velocity horizontal <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("vertical")) {
                        if (args.length > 2) {
                            try {
                                final float value = Float.parseFloat(args[2]);
                                Velocity.this.verticalValue.setValue(value);
                                this.chat("§7Velocity vertical was set to §8" + value + "§7.");
                                Velocity$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".velocity vertical <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Velocity.this.modeValue.contains(args[2])) {
                            Velocity.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Velocity mode was set to §8" + Velocity.this.modeValue.asString().toUpperCase() + "§7.");
                            Velocity$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".velocity mode §c<§8" + Strings.join(Velocity.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("ReverseStrength")) {
                        if (args.length > 2) {
                            try {
                                final float value = Float.parseFloat(args[2]);
                                Velocity.this.reverseStrengthValue.setValue(value);
                                this.chat("§7Velocity reversestrength was set to §8" + value + "§7.");
                                Velocity$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".velocity reversestrength <value>");
                        return;
                    }
                }
                this.chatSyntax(".velocity <horizontal, vertical, mode, reversestrength>");
            }
        });
    }
    
    @Override
    public void onDisable() {
        if (Velocity.mc.thePlayer == null) {
            return;
        }
        Velocity.mc.thePlayer.speedInAir = 0.02f;
        super.onDisable();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (Velocity.mc.thePlayer.isInWater()) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "reverse": {
                if (!this.gotVelocity) {
                    break;
                }
                if (!Velocity.mc.thePlayer.onGround && !Velocity.mc.thePlayer.isInWater() && !Velocity.mc.thePlayer.isInLava() && !Velocity.mc.thePlayer.isInWeb) {
                    MovementUtils.strafe(MovementUtils.getSpeed() * this.reverseStrengthValue.asFloat());
                    break;
                }
                if (System.currentTimeMillis() - this.velocityTime > 80L) {
                    this.gotVelocity = false;
                    break;
                }
                break;
            }
            case "aac": {
                if (this.velocityTime != 0L && System.currentTimeMillis() - this.velocityTime > 80L) {
                    final EntityPlayerSP thePlayer = Velocity.mc.thePlayer;
                    thePlayer.motionX *= this.horizontalValue.asFloat();
                    final EntityPlayerSP thePlayer2 = Velocity.mc.thePlayer;
                    thePlayer2.motionZ *= this.verticalValue.asFloat();
                    this.velocityTime = 0L;
                    break;
                }
                break;
            }
            case "jump": {
                if (Velocity.mc.thePlayer.hurtTime > 0 && Velocity.mc.thePlayer.onGround) {
                    Velocity.mc.thePlayer.motionY = 0.42;
                    final float f = Velocity.mc.thePlayer.rotationYaw * 0.017453292f;
                    final EntityPlayerSP thePlayer3 = Velocity.mc.thePlayer;
                    thePlayer3.motionX -= MathHelper.sin(f) * 0.2f;
                    final EntityPlayerSP thePlayer4 = Velocity.mc.thePlayer;
                    thePlayer4.motionZ += MathHelper.cos(f) * 0.2f;
                    break;
                }
                break;
            }
            case "aacpush": {
                if (Velocity.mc.thePlayer.movementInput.jump) {
                    break;
                }
                if (this.velocityTime != 0L && System.currentTimeMillis() - this.velocityTime > 80L) {
                    this.velocityTime = 0L;
                }
                if (Velocity.mc.thePlayer.hurtTime > 0 && Velocity.mc.thePlayer.motionX != 0.0 && Velocity.mc.thePlayer.motionZ != 0.0) {
                    Velocity.mc.thePlayer.onGround = true;
                }
                if (Velocity.mc.thePlayer.hurtResistantTime > 0 && this.aacPushYReducerValue.asBoolean()) {
                    final EntityPlayerSP thePlayer5 = Velocity.mc.thePlayer;
                    thePlayer5.motionY -= 0.0145;
                }
                if (Velocity.mc.thePlayer.hurtResistantTime >= 19) {
                    final double reduce = this.aacPushXZReducerValue.asDouble();
                    final EntityPlayerSP thePlayer6 = Velocity.mc.thePlayer;
                    thePlayer6.motionX /= reduce;
                    final EntityPlayerSP thePlayer7 = Velocity.mc.thePlayer;
                    thePlayer7.motionZ /= reduce;
                    break;
                }
                break;
            }
            case "glitch": {
                Velocity.mc.thePlayer.noClip = this.gotVelocity;
                if (Velocity.mc.thePlayer.hurtTime < 8 && Velocity.mc.thePlayer.hurtTime > 6) {
                    Velocity.mc.thePlayer.motionY = 0.4;
                }
                this.gotVelocity = false;
                break;
            }
            case "reverse2": {
                if (!this.gotVelocity) {
                    Velocity.mc.thePlayer.speedInAir = 0.02f;
                    break;
                }
                if (Velocity.mc.thePlayer.hurtTime > 0) {
                    this.gotHurt = true;
                }
                if (!Velocity.mc.thePlayer.onGround && !Velocity.mc.thePlayer.isInWater() && !Velocity.mc.thePlayer.isInLava() && !Velocity.mc.thePlayer.isInWeb) {
                    if (this.gotHurt) {
                        Velocity.mc.thePlayer.speedInAir = this.reverse2StrenghtValue.asFloat();
                        break;
                    }
                    break;
                }
                else {
                    if (System.currentTimeMillis() - this.velocityTime > 80L) {
                        this.gotVelocity = false;
                        this.gotHurt = false;
                        break;
                    }
                    break;
                }
                break;
            }
            case "aaczero": {
                if (Velocity.mc.thePlayer.hurtTime <= 0) {
                    this.gotVelocity = false;
                    break;
                }
                if (!this.gotVelocity || Velocity.mc.thePlayer.onGround) {
                    break;
                }
                if (Velocity.mc.thePlayer.fallDistance > 2.0f) {
                    break;
                }
                Velocity.mc.thePlayer.addVelocity(0.0, -1.0, 0.0);
                Velocity.mc.thePlayer.onGround = true;
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof S12PacketEntityVelocity && Velocity.mc.thePlayer != null && Velocity.mc.theWorld != null) {
            final S12PacketEntityVelocity packetEntityVelocity = (S12PacketEntityVelocity)packet;
            if (Velocity.mc.theWorld.getEntityByID(packetEntityVelocity.getEntityID()) == Velocity.mc.thePlayer) {
                this.velocityTime = System.currentTimeMillis();
                final String mode = this.modeValue.asString();
                final String lowerCase = mode.toLowerCase();
                switch (lowerCase) {
                    case "simple": {
                        final double horizontal = this.horizontalValue.asFloat();
                        final double vertical = this.verticalValue.asFloat();
                        if (horizontal == 0.0 && vertical == 0.0) {
                            event.setCancelled(true);
                        }
                        packetEntityVelocity.motionX = (int)(packetEntityVelocity.getMotionX() * horizontal);
                        packetEntityVelocity.motionY = (int)(packetEntityVelocity.getMotionY() * vertical);
                        packetEntityVelocity.motionZ = (int)(packetEntityVelocity.getMotionZ() * horizontal);
                        break;
                    }
                    case "reverse":
                    case "reverse2":
                    case "aaczero": {
                        this.gotVelocity = true;
                        break;
                    }
                    case "glitch": {
                        if (!Velocity.mc.thePlayer.onGround) {
                            break;
                        }
                        event.setCancelled(this.gotVelocity = true);
                        break;
                    }
                }
            }
        }
        if (packet instanceof S27PacketExplosion) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onJump(final JumpEvent event) {
        if (Velocity.mc.thePlayer == null || Velocity.mc.thePlayer.isInWater()) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "aacpush":
            case "aaczero": {
                if (Velocity.mc.thePlayer.hurtTime > 0) {
                    event.setCancelled(true);
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
