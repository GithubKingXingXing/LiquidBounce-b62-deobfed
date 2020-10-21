
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.events.StepEvent;
import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockAir;
import net.ccbluex.liquidbounce.event.events.BlockBBEvent;
import net.minecraft.potion.Potion;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import java.math.RoundingMode;
import java.math.BigDecimal;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.ccbluex.liquidbounce.utils.VectorUtils;
import net.minecraft.util.Vec3;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Fly", description = "Allows you to fly in survival mode.", category = ModuleCategory.MOVEMENT, keyBind = 33)
public class Fly extends Module
{
    public final ListValue modeValue;
    private final FloatValue vanillaSpeedValue;
    private final BoolValue vanillaKickBypassValue;
    private final FloatValue mineplexSpeedValue;
    private final FloatValue ncpMotionValue;
    private final FloatValue aacSpeedValue;
    private final FloatValue aacMotion;
    private final FloatValue aacMotion2;
    private final IntegerValue neruxVaceTicks;
    private final BoolValue hypixelLatestBoostValue;
    private final IntegerValue hypixelLatestBoostDelayValue;
    private final FloatValue hypixelLatestBoostTimerValue;
    private final BoolValue markValue;
    private double startY;
    private final MSTimer flyTimer;
    private final MSTimer groundTimer;
    private boolean noPacketModify;
    private double aacJump;
    private int aac3delay;
    private int aac3glideDelay;
    private boolean noFlag;
    private final MSTimer mineSecureVClipTimer;
    private final TickTimer spartanTimer;
    private final MSTimer hypixelTimer;
    private long minesuchtTP;
    private boolean hypixelSwitch;
    private final MSTimer mineplexTimer;
    private boolean wasDead;
    private final TickTimer latestHypixelTimer;
    private int boostHypixelState;
    private double moveSpeed;
    private double lastDistance;
    private final TickTimer cubecraft2TickTimer;
    private final TickTimer cubecraftTeleportTickTimer;
    private final TickTimer freeHypixelTimer;
    private float freeHypixelYaw;
    private float freeHypixelPitch;
    
    public Fly() {
        this.modeValue = new ListValue("Mode", new String[] { "Vanilla", "SmoothVanilla", "NCP", "OldNCP", "AAC", "AACv3", "FastAACv3", "AAC3.3.12", "AAC3.3.12-Glide", "AAC3.3.13", "Gomme", "Flag", "CubeCraft", "InfinityCubeCraft", "InfinityVCubeCraft", "Hypixel", "OtherHypixel", "LatestHypixel", "BoostHypixel", "FreeHypixel", "Rewinside", "TeleportRewinside", "Mineplex", "KeepAlive", "MineSecure", "Spartan", "Spartan2", "BugSpartan", "KillSwitch", "HawkEye", "Minesucht", "Jetpack", "HAC", "WatchCat", "NeruxVace" }, "Vanilla");
        this.vanillaSpeedValue = new FloatValue("VanillaSpeed", 2.0f, 0.0f, 5.0f);
        this.vanillaKickBypassValue = new BoolValue("VanillaKickBypass", false);
        this.mineplexSpeedValue = new FloatValue("MineplexSpeed", 1.0f, 0.5f, 10.0f);
        this.ncpMotionValue = new FloatValue("NCPMotion", 0.0f, 0.0f, 1.0f);
        this.aacSpeedValue = new FloatValue("AACSpeed", 0.3f, 0.0f, 1.0f);
        this.aacMotion = new FloatValue("AAC3.3.12-Motion", 10.0f, 0.1f, 10.0f);
        this.aacMotion2 = new FloatValue("AAC3.3.13-Motion", 10.0f, 0.1f, 10.0f);
        this.neruxVaceTicks = new IntegerValue("NeruxVace-Ticks", 6, 0, 20);
        this.hypixelLatestBoostValue = new BoolValue("HypixelLatest-Boost", true);
        this.hypixelLatestBoostDelayValue = new IntegerValue("HypixelLatest-BoostDelay", 1200, 0, 2000);
        this.hypixelLatestBoostTimerValue = new FloatValue("HypixelLatest-BoostTimer", 1.0f, 0.0f, 5.0f);
        this.markValue = new BoolValue("Mark", true);
        this.flyTimer = new MSTimer();
        this.groundTimer = new MSTimer();
        this.mineSecureVClipTimer = new MSTimer();
        this.spartanTimer = new TickTimer();
        this.hypixelTimer = new MSTimer();
        this.mineplexTimer = new MSTimer();
        this.latestHypixelTimer = new TickTimer();
        this.boostHypixelState = 1;
        this.cubecraft2TickTimer = new TickTimer();
        this.cubecraftTeleportTickTimer = new TickTimer();
        this.freeHypixelTimer = new TickTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("fly", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Fly.this.modeValue.contains(args[2])) {
                            Fly.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Fly mode was set to §8" + Fly.this.modeValue.asString().toUpperCase() + "§7.");
                            Fly$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".fly mode §c<§8" + Strings.join(Fly.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("vanillaspeed")) {
                        if (args.length > 2) {
                            try {
                                final float speed = Float.parseFloat(args[2]);
                                Fly.this.vanillaSpeedValue.setValue(speed);
                                this.chat("§7Fly vanillaspeed was set to §8" + speed + "§7.");
                                Fly$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".fly vanillaspeed <speed>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("ncpmotion")) {
                        if (args.length > 2) {
                            try {
                                final float motion = Float.parseFloat(args[2]);
                                Fly.this.ncpMotionValue.setValue(motion);
                                this.chat("§7Fly ncpmotion was set to §8" + motion + "§7.");
                                Fly$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".fly ncpmotion <motion>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("aacspeed")) {
                        if (args.length > 2) {
                            try {
                                final float speed = Float.parseFloat(args[2]);
                                Fly.this.aacSpeedValue.setValue(speed);
                                this.chat("§7Fly aacspeed was set to §8" + speed + "§7.");
                                Fly$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".fly aacspeed <speed>");
                        return;
                    }
                }
                this.chatSyntax(".fly <mode, vanillaspeed, ncpmotion, aacspeed>");
            }
        });
    }
    
    @Override
    public void onEnable() {
        if (Fly.mc.thePlayer == null) {
            return;
        }
        this.flyTimer.reset();
        this.noPacketModify = true;
        final double x = Fly.mc.thePlayer.posX;
        final double y = Fly.mc.thePlayer.posY;
        final double z = Fly.mc.thePlayer.posZ;
        final String mode = this.modeValue.asString();
        final String lowerCase = mode.toLowerCase();
        switch (lowerCase) {
            case "ncp": {
                if (!Fly.mc.thePlayer.onGround) {
                    break;
                }
                for (int i = 0; i < 65; ++i) {
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1, z, true));
                final EntityPlayerSP thePlayer = Fly.mc.thePlayer;
                thePlayer.motionX *= 0.1;
                final EntityPlayerSP thePlayer2 = Fly.mc.thePlayer;
                thePlayer2.motionZ *= 0.1;
                Fly.mc.thePlayer.swingItem();
                break;
            }
            case "oldncp": {
                if (!Fly.mc.thePlayer.onGround) {
                    break;
                }
                for (int i = 0; i < 4; ++i) {
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.01, z, false));
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                Fly.mc.thePlayer.jump();
                Fly.mc.thePlayer.swingItem();
                break;
            }
            case "bugspartan": {
                for (int i = 0; i < 65; ++i) {
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1, z, true));
                final EntityPlayerSP thePlayer3 = Fly.mc.thePlayer;
                thePlayer3.motionX *= 0.1;
                final EntityPlayerSP thePlayer4 = Fly.mc.thePlayer;
                thePlayer4.motionZ *= 0.1;
                Fly.mc.thePlayer.swingItem();
                break;
            }
            case "infinitycubecraft": {
                ChatUtils.displayChatMessage("§8[§c§lCubeCraft-§a§lFly§8] §aPlace a block before landing.");
                break;
            }
            case "infinityvcubecraft": {
                ChatUtils.displayChatMessage("§8[§c§lCubeCraft-§a§lFly§8] §aPlace a block before landing.");
                Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 2.0, Fly.mc.thePlayer.posZ);
                break;
            }
            case "boosthypixel": {
                if (!Fly.mc.thePlayer.onGround) {
                    break;
                }
                for (int i = 0; i <= 64; ++i) {
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 0.0625, Fly.mc.thePlayer.posZ, false));
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, i >= 64));
                }
                Fly.mc.thePlayer.jump();
                this.boostHypixelState = 1;
                this.moveSpeed = 0.1;
                this.lastDistance = 0.0;
                break;
            }
        }
        this.startY = Fly.mc.thePlayer.posY;
        this.aacJump = -3.8;
        this.noPacketModify = false;
        if (mode.equalsIgnoreCase("freehypixel")) {
            this.freeHypixelTimer.reset();
            Fly.mc.thePlayer.setPositionAndUpdate(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 0.21, Fly.mc.thePlayer.posZ);
            this.freeHypixelYaw = Fly.mc.thePlayer.rotationYaw;
            this.freeHypixelPitch = Fly.mc.thePlayer.rotationPitch;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.wasDead = false;
        if (Fly.mc.thePlayer == null) {
            return;
        }
        this.noFlag = false;
        final String flyMode = this.modeValue.asString();
        if (!flyMode.toUpperCase().startsWith("AAC") && !flyMode.equalsIgnoreCase("LatestHypixel") && !flyMode.equalsIgnoreCase("CubeCraft")) {
            Fly.mc.thePlayer.motionX = 0.0;
            Fly.mc.thePlayer.motionY = 0.0;
            Fly.mc.thePlayer.motionZ = 0.0;
        }
        Fly.mc.thePlayer.capabilities.isFlying = false;
        Fly.mc.timer.timerSpeed = 1.0f;
        Fly.mc.thePlayer.speedInAir = 0.02f;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        final String flyMode = this.modeValue.asString();
        final float vanillaSpeed = this.vanillaSpeedValue.asFloat();
        final String lowerCase = flyMode.toLowerCase();
        switch (lowerCase) {
            case "vanilla": {
                Fly.mc.thePlayer.capabilities.isFlying = false;
                Fly.mc.thePlayer.motionY = 0.0;
                Fly.mc.thePlayer.motionX = 0.0;
                Fly.mc.thePlayer.motionZ = 0.0;
                if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP thePlayer = Fly.mc.thePlayer;
                    thePlayer.motionY += vanillaSpeed;
                }
                if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EntityPlayerSP thePlayer2 = Fly.mc.thePlayer;
                    thePlayer2.motionY -= vanillaSpeed;
                }
                MovementUtils.strafe(vanillaSpeed);
                this.handleVanillaKickBypass();
                break;
            }
            case "smoothvanilla": {
                Fly.mc.thePlayer.capabilities.isFlying = true;
                this.handleVanillaKickBypass();
                break;
            }
            case "cubecraft": {
                Fly.mc.timer.timerSpeed = 0.6f;
                this.cubecraftTeleportTickTimer.update();
                break;
            }
            case "infinitycubecraft": {
                Fly.mc.timer.timerSpeed = 0.6f;
                if (Fly.mc.thePlayer.onGround || this.startY <= Fly.mc.thePlayer.posY) {
                    this.cubecraft2TickTimer.reset();
                    this.cubecraftTeleportTickTimer.reset();
                    break;
                }
                this.cubecraft2TickTimer.update();
                this.cubecraftTeleportTickTimer.update();
                if (this.cubecraft2TickTimer.hasTimePassed(3)) {
                    Fly.mc.thePlayer.motionY = 0.07;
                    this.cubecraft2TickTimer.reset();
                    break;
                }
                break;
            }
            case "infinityvcubecraft": {
                Fly.mc.timer.timerSpeed = 0.5f;
                if (Fly.mc.thePlayer.onGround || this.startY <= Fly.mc.thePlayer.posY) {
                    this.cubecraft2TickTimer.reset();
                    this.cubecraftTeleportTickTimer.reset();
                    break;
                }
                this.cubecraft2TickTimer.update();
                this.cubecraftTeleportTickTimer.update();
                if (this.cubecraft2TickTimer.hasTimePassed(3)) {
                    Fly.mc.thePlayer.motionY = 0.07;
                    this.cubecraft2TickTimer.reset();
                    break;
                }
                break;
            }
            case "ncp": {
                Fly.mc.thePlayer.motionY = -this.ncpMotionValue.asFloat();
                if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Fly.mc.thePlayer.motionY = -0.5;
                }
                MovementUtils.strafe();
                break;
            }
            case "oldncp": {
                if (this.startY > Fly.mc.thePlayer.posY) {
                    Fly.mc.thePlayer.motionY = -1.0E-33;
                }
                if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Fly.mc.thePlayer.motionY = -0.2;
                }
                if (Fly.mc.gameSettings.keyBindJump.isKeyDown() && Fly.mc.thePlayer.posY < this.startY - 0.1) {
                    Fly.mc.thePlayer.motionY = 0.2;
                }
                MovementUtils.strafe();
                break;
            }
            case "aac": {
                if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    this.aacJump += 0.2;
                }
                if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    this.aacJump -= 0.2;
                }
                if (this.startY + this.aacJump > Fly.mc.thePlayer.posY) {
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer(true));
                    Fly.mc.thePlayer.motionY = 0.8;
                    MovementUtils.strafe(this.aacSpeedValue.asFloat());
                }
                MovementUtils.strafe();
                break;
            }
            case "aacv3": {
                if (this.aac3delay == 2) {
                    Fly.mc.thePlayer.motionY = 0.1;
                }
                else if (this.aac3delay > 2) {
                    this.aac3delay = 0;
                }
                ++this.aac3delay;
                break;
            }
            case "fastaacv3": {
                if (this.aac3delay == 2) {
                    Fly.mc.thePlayer.motionY = 0.1;
                }
                else if (this.aac3delay > 2) {
                    this.aac3delay = 0;
                }
                if (Fly.mc.thePlayer.movementInput.moveStrafe == 0.0) {
                    Fly.mc.thePlayer.jumpMovementFactor = 0.08f;
                }
                else {
                    Fly.mc.thePlayer.jumpMovementFactor = 0.0f;
                }
                ++this.aac3delay;
                break;
            }
            case "gomme": {
                Fly.mc.thePlayer.capabilities.isFlying = true;
                if (this.aac3delay == 2) {
                    final EntityPlayerSP thePlayer3 = Fly.mc.thePlayer;
                    thePlayer3.motionY += 0.05;
                }
                else if (this.aac3delay > 2) {
                    final EntityPlayerSP thePlayer4 = Fly.mc.thePlayer;
                    thePlayer4.motionY -= 0.05;
                    this.aac3delay = 0;
                }
                ++this.aac3delay;
                if (!this.noFlag) {
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, Fly.mc.thePlayer.onGround));
                }
                if (Fly.mc.thePlayer.posY <= 0.0) {
                    this.noFlag = true;
                    break;
                }
                break;
            }
            case "flag": {
                Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.thePlayer.posX + Fly.mc.thePlayer.motionX * 999.0, Fly.mc.thePlayer.posY + (Fly.mc.gameSettings.keyBindJump.isKeyDown() ? 1.5624 : 1.0E-8) - (Fly.mc.gameSettings.keyBindSneak.isKeyDown() ? 0.0624 : 2.0E-8), Fly.mc.thePlayer.posZ + Fly.mc.thePlayer.motionZ * 999.0, Fly.mc.thePlayer.rotationYaw, Fly.mc.thePlayer.rotationPitch, true));
                Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.thePlayer.posX + Fly.mc.thePlayer.motionX * 999.0, Fly.mc.thePlayer.posY - 6969.0, Fly.mc.thePlayer.posZ + Fly.mc.thePlayer.motionZ * 999.0, Fly.mc.thePlayer.rotationYaw, Fly.mc.thePlayer.rotationPitch, true));
                Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX + Fly.mc.thePlayer.motionX * 11.0, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ + Fly.mc.thePlayer.motionZ * 11.0);
                Fly.mc.thePlayer.motionY = 0.0;
                break;
            }
            case "keepalive": {
                Fly.mc.getNetHandler().addToSendQueue((Packet)new C00PacketKeepAlive());
                Fly.mc.thePlayer.capabilities.isFlying = false;
                Fly.mc.thePlayer.motionY = 0.0;
                Fly.mc.thePlayer.motionX = 0.0;
                Fly.mc.thePlayer.motionZ = 0.0;
                if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP thePlayer5 = Fly.mc.thePlayer;
                    thePlayer5.motionY += vanillaSpeed;
                }
                if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EntityPlayerSP thePlayer6 = Fly.mc.thePlayer;
                    thePlayer6.motionY -= vanillaSpeed;
                }
                MovementUtils.strafe(vanillaSpeed);
                break;
            }
            case "minesecure": {
                Fly.mc.thePlayer.capabilities.isFlying = false;
                if (!Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Fly.mc.thePlayer.motionY = -0.009999999776482582;
                }
                Fly.mc.thePlayer.motionX = 0.0;
                Fly.mc.thePlayer.motionZ = 0.0;
                MovementUtils.strafe(vanillaSpeed);
                if (this.mineSecureVClipTimer.hasTimePassed(150L) && Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 5.0, Fly.mc.thePlayer.posZ, false));
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(0.5, -1000.0, 0.5, false));
                    final double yaw = Math.toRadians(Fly.mc.thePlayer.rotationYaw);
                    final double x = -Math.sin(yaw) * 0.4;
                    final double z = Math.cos(yaw) * 0.4;
                    Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX + x, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ + z);
                    this.mineSecureVClipTimer.reset();
                    break;
                }
                break;
            }
            case "killswitch": {
                Fly.mc.thePlayer.capabilities.isFlying = true;
                break;
            }
            case "hawkeye": {
                Fly.mc.thePlayer.motionY = ((Fly.mc.thePlayer.motionY <= -0.42) ? 0.42 : -0.42);
                break;
            }
            case "hac": {
                final EntityPlayerSP thePlayer7 = Fly.mc.thePlayer;
                thePlayer7.motionX *= 0.8;
                final EntityPlayerSP thePlayer8 = Fly.mc.thePlayer;
                thePlayer8.motionZ *= 0.8;
                Fly.mc.thePlayer.motionY = ((Fly.mc.thePlayer.motionY <= -0.42) ? 0.42 : -0.42);
                break;
            }
            case "teleportrewinside": {
                final Vec3 vectorStart = new Vec3(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ);
                final Vec3 vectorEnd = new VectorUtils(vectorStart, -Fly.mc.thePlayer.rotationYaw, -Fly.mc.thePlayer.rotationPitch, 9.9).getEndVector();
                Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorEnd.xCoord, Fly.mc.thePlayer.posY + 2.0, vectorEnd.zCoord, true));
                Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorStart.xCoord, Fly.mc.thePlayer.posY + 2.0, vectorStart.zCoord, true));
                Fly.mc.thePlayer.motionY = 0.0;
                break;
            }
            case "minesucht": {
                final double posX = Fly.mc.thePlayer.posX;
                final double posY = Fly.mc.thePlayer.posY;
                final double posZ = Fly.mc.thePlayer.posZ;
                if (!Fly.mc.gameSettings.keyBindForward.isKeyDown()) {
                    break;
                }
                if (System.currentTimeMillis() - this.minesuchtTP > 99L) {
                    final Vec3 vec3 = Fly.mc.thePlayer.getPositionEyes(0.0f);
                    final Vec3 vec4 = Fly.mc.thePlayer.getLook(0.0f);
                    final Vec3 vec5 = vec3.addVector(vec4.xCoord * 7.0, vec4.yCoord * 7.0, vec4.zCoord * 7.0);
                    if (Fly.mc.thePlayer.fallDistance > 0.8) {
                        Fly.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 50.0, posZ, false));
                        Fly.mc.thePlayer.fall(100.0f, 100.0f);
                        Fly.mc.thePlayer.fallDistance = 0.0f;
                        Fly.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 20.0, posZ, true));
                    }
                    Fly.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vec5.xCoord, Fly.mc.thePlayer.posY + 50.0, vec5.zCoord, true));
                    Fly.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
                    Fly.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vec5.xCoord, posY, vec5.zCoord, true));
                    Fly.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
                    this.minesuchtTP = System.currentTimeMillis();
                    break;
                }
                Fly.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, false));
                Fly.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, true));
                break;
            }
            case "jetpack": {
                if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Fly.mc.effectRenderer.spawnEffectParticle(EnumParticleTypes.FLAME.getParticleID(), Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 0.2, Fly.mc.thePlayer.posZ, -Fly.mc.thePlayer.motionX, -0.5, -Fly.mc.thePlayer.motionZ, new int[0]);
                    final EntityPlayerSP thePlayer9 = Fly.mc.thePlayer;
                    thePlayer9.motionY += 0.15;
                    final EntityPlayerSP thePlayer10 = Fly.mc.thePlayer;
                    thePlayer10.motionX *= 1.1;
                    final EntityPlayerSP thePlayer11 = Fly.mc.thePlayer;
                    thePlayer11.motionZ *= 1.1;
                    break;
                }
                break;
            }
            case "mineplex": {
                if (Fly.mc.thePlayer.inventory.getCurrentItem() == null) {
                    if (Fly.mc.gameSettings.keyBindJump.isKeyDown() && this.mineplexTimer.hasTimePassed(100L)) {
                        Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 0.6, Fly.mc.thePlayer.posZ);
                        this.mineplexTimer.reset();
                    }
                    if (Fly.mc.thePlayer.isSneaking() && this.mineplexTimer.hasTimePassed(100L)) {
                        Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY - 0.6, Fly.mc.thePlayer.posZ);
                        this.mineplexTimer.reset();
                    }
                    final BlockPos blockPos = new BlockPos(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.getEntityBoundingBox().minY - 1.0, Fly.mc.thePlayer.posZ);
                    final Vec3 vec6 = new Vec3((Vec3i)blockPos).addVector(0.4000000059604645, 0.4000000059604645, 0.4000000059604645).add(new Vec3(EnumFacing.UP.getDirectionVec()));
                    Fly.mc.playerController.onPlayerRightClick(Fly.mc.thePlayer, Fly.mc.theWorld, Fly.mc.thePlayer.inventory.getCurrentItem(), blockPos, EnumFacing.UP, new Vec3(vec6.xCoord * 0.4000000059604645, vec6.yCoord * 0.4000000059604645, vec6.zCoord * 0.4000000059604645));
                    MovementUtils.strafe(0.27f);
                    Fly.mc.timer.timerSpeed = 1.0f + this.mineplexSpeedValue.asFloat();
                    break;
                }
                Fly.mc.timer.timerSpeed = 1.0f;
                this.setState(false);
                ChatUtils.displayChatMessage("§8[§c§lMineplex-§a§lFly§8] §aSelect an empty slot to fly.");
                break;
            }
            case "aac3.3.12": {
                if (Fly.mc.thePlayer.posY < -70.0) {
                    Fly.mc.thePlayer.motionY = this.aacMotion.asFloat();
                }
                Fly.mc.timer.timerSpeed = 1.0f;
                if (Keyboard.isKeyDown(29)) {
                    Fly.mc.timer.timerSpeed = 0.2f;
                    Fly.mc.rightClickDelayTimer = 0;
                    break;
                }
                break;
            }
            case "aac3.3.12-glide": {
                if (!Fly.mc.thePlayer.onGround) {
                    ++this.aac3glideDelay;
                }
                if (this.aac3glideDelay == 2) {
                    Fly.mc.timer.timerSpeed = 1.0f;
                }
                if (this.aac3glideDelay == 12) {
                    Fly.mc.timer.timerSpeed = 0.1f;
                }
                if (this.aac3glideDelay >= 12 && !Fly.mc.thePlayer.onGround) {
                    this.aac3glideDelay = 0;
                    Fly.mc.thePlayer.motionY = 0.015;
                    break;
                }
                break;
            }
            case "aac3.3.13": {
                if (Fly.mc.thePlayer.isDead) {
                    this.wasDead = true;
                }
                if (this.wasDead || Fly.mc.thePlayer.onGround) {
                    this.wasDead = false;
                    Fly.mc.thePlayer.motionY = this.aacMotion2.asFloat();
                    Fly.mc.thePlayer.onGround = false;
                }
                Fly.mc.timer.timerSpeed = 1.0f;
                if (Keyboard.isKeyDown(29)) {
                    Fly.mc.timer.timerSpeed = 0.2f;
                    Fly.mc.rightClickDelayTimer = 0;
                    break;
                }
                break;
            }
            case "watchcat": {
                MovementUtils.strafe(0.15f);
                Fly.mc.thePlayer.setSprinting(true);
                if (Fly.mc.thePlayer.posY < this.startY + 2.0) {
                    Fly.mc.thePlayer.motionY = Math.random() * 0.5;
                    break;
                }
                if (this.startY > Fly.mc.thePlayer.posY) {
                    MovementUtils.strafe(0.0f);
                    break;
                }
                break;
            }
            case "spartan": {
                Fly.mc.thePlayer.motionY = 0.0;
                this.spartanTimer.update();
                if (this.spartanTimer.hasTimePassed(12)) {
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 8.0, Fly.mc.thePlayer.posZ, true));
                    Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY - 8.0, Fly.mc.thePlayer.posZ, true));
                    this.spartanTimer.reset();
                    break;
                }
                break;
            }
            case "spartan2": {
                MovementUtils.strafe(0.264f);
                if (Fly.mc.thePlayer.ticksExisted % 8 == 0) {
                    Fly.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 10.0, Fly.mc.thePlayer.posZ, true));
                    break;
                }
                break;
            }
            case "neruxvace": {
                if (!Fly.mc.thePlayer.onGround) {
                    ++this.aac3glideDelay;
                }
                if (this.aac3glideDelay >= this.neruxVaceTicks.asInteger() && !Fly.mc.thePlayer.onGround) {
                    this.aac3glideDelay = 0;
                    Fly.mc.thePlayer.motionY = 0.015;
                    break;
                }
                break;
            }
            case "latesthypixel": {
                final int boostDelay = this.hypixelLatestBoostDelayValue.asInteger();
                if (this.hypixelLatestBoostValue.asBoolean() && !this.flyTimer.hasTimePassed(boostDelay)) {
                    Fly.mc.timer.timerSpeed = 1.0f + this.hypixelLatestBoostTimerValue.asFloat() * (this.flyTimer.hasTimeLeft(boostDelay) / (float)boostDelay);
                }
                this.latestHypixelTimer.update();
                if (this.latestHypixelTimer.hasTimePassed(2)) {
                    Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 1.0E-5, Fly.mc.thePlayer.posZ);
                    this.latestHypixelTimer.reset();
                    break;
                }
                break;
            }
            case "freehypixel": {
                if (this.freeHypixelTimer.hasTimePassed(10)) {
                    Fly.mc.thePlayer.capabilities.isFlying = true;
                    break;
                }
                Fly.mc.thePlayer.rotationYaw = this.freeHypixelYaw;
                Fly.mc.thePlayer.rotationPitch = this.freeHypixelPitch;
                final EntityPlayerSP thePlayer12 = Fly.mc.thePlayer;
                final EntityPlayerSP thePlayer13 = Fly.mc.thePlayer;
                final EntityPlayerSP thePlayer14 = Fly.mc.thePlayer;
                final double motionX = 0.0;
                thePlayer14.motionY = motionX;
                thePlayer13.motionZ = motionX;
                thePlayer12.motionX = motionX;
                if (this.startY == new BigDecimal(Fly.mc.thePlayer.posY).setScale(3, RoundingMode.HALF_DOWN).doubleValue()) {
                    this.freeHypixelTimer.update();
                    break;
                }
                break;
            }
            case "bugspartan": {
                Fly.mc.thePlayer.capabilities.isFlying = false;
                Fly.mc.thePlayer.motionY = 0.0;
                Fly.mc.thePlayer.motionX = 0.0;
                Fly.mc.thePlayer.motionZ = 0.0;
                if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP thePlayer15 = Fly.mc.thePlayer;
                    thePlayer15.motionY += vanillaSpeed;
                }
                if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EntityPlayerSP thePlayer16 = Fly.mc.thePlayer;
                    thePlayer16.motionY -= vanillaSpeed;
                }
                MovementUtils.strafe(vanillaSpeed);
                break;
            }
        }
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (this.modeValue.asString().equalsIgnoreCase("boosthypixel")) {
            switch (event.getEventState()) {
                case PRE: {
                    this.latestHypixelTimer.update();
                    if (this.latestHypixelTimer.hasTimePassed(2)) {
                        Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 1.0E-5, Fly.mc.thePlayer.posZ);
                        this.latestHypixelTimer.reset();
                    }
                    Fly.mc.thePlayer.motionY = 0.0;
                    break;
                }
                case POST: {
                    final double xDist = Fly.mc.thePlayer.posX - Fly.mc.thePlayer.prevPosX;
                    final double zDist = Fly.mc.thePlayer.posZ - Fly.mc.thePlayer.prevPosZ;
                    this.lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
                    break;
                }
            }
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final String flyMode = this.modeValue.asString();
        if (!this.markValue.asBoolean() || flyMode.equalsIgnoreCase("Vanilla") || flyMode.equalsIgnoreCase("SmoothVanilla") || flyMode.equalsIgnoreCase("LAAC") || flyMode.equalsIgnoreCase("KillSwitch")) {
            return;
        }
        final double y = this.startY + 2.0;
        RenderUtils.drawPlatform(Fly.mc.thePlayer.posX, y, Fly.mc.thePlayer.posZ, (Fly.mc.thePlayer.getEntityBoundingBox().maxY < y) ? new Color(0, 255, 0, 90) : new Color(255, 0, 0, 90), 1.0);
        final String lowerCase = flyMode.toLowerCase();
        switch (lowerCase) {
            case "aac": {
                RenderUtils.drawPlatform(Fly.mc.thePlayer.posX, this.startY + this.aacJump, Fly.mc.thePlayer.posZ, new Color(0, 0, 255, 90), 1.0);
                break;
            }
            case "aac3.3.12": {
                RenderUtils.drawPlatform(Fly.mc.thePlayer.posX, -70.0, Fly.mc.thePlayer.posZ, new Color(0, 0, 255, 90), 1.0);
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (this.noPacketModify) {
            return;
        }
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            final String mode = this.modeValue.asString();
            if (mode.equalsIgnoreCase("NCP") || mode.equalsIgnoreCase("Hypixel") || mode.equalsIgnoreCase("OtherHypixel") || mode.equalsIgnoreCase("Rewinside") || (mode.equalsIgnoreCase("Mineplex") && Fly.mc.thePlayer.inventory.getCurrentItem() == null)) {
                packetPlayer.onGround = true;
                if (mode.equalsIgnoreCase("Hypixel")) {
                    if (this.hypixelSwitch) {
                        final C03PacketPlayer c03PacketPlayer = packetPlayer;
                        c03PacketPlayer.y += 1.1E-5;
                    }
                    this.hypixelSwitch = !this.hypixelSwitch;
                }
                if (mode.equalsIgnoreCase("OtherHypixel") && this.hypixelTimer.hasTimePassed(100L)) {
                    final C03PacketPlayer c03PacketPlayer2 = packetPlayer;
                    c03PacketPlayer2.y += 0.001;
                    this.hypixelTimer.reset();
                }
            }
            if (mode.equalsIgnoreCase("LatestHypixel") || mode.equalsIgnoreCase("BoostHypixel")) {
                packetPlayer.onGround = false;
            }
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "cubecraft": {
                if (this.cubecraftTeleportTickTimer.hasTimePassed(2)) {
                    final double yaw = Math.toRadians(Fly.mc.thePlayer.rotationYaw);
                    event.x = -Math.sin(yaw) * 2.4;
                    event.z = Math.cos(yaw) * 2.4;
                    this.cubecraftTeleportTickTimer.reset();
                    break;
                }
                final double yaw = Math.toRadians(Fly.mc.thePlayer.rotationYaw);
                event.x = -Math.sin(yaw) * 0.2;
                event.z = Math.cos(yaw) * 0.2;
                break;
            }
            case "infinitycubecraft":
            case "infinityvcubecraft": {
                if (Fly.mc.thePlayer.onGround) {
                    break;
                }
                if (this.startY <= Fly.mc.thePlayer.posY) {
                    break;
                }
                if (this.cubecraftTeleportTickTimer.hasTimePassed(4) && !Fly.mc.thePlayer.isSneaking()) {
                    final double yaw = Math.toRadians(Fly.mc.thePlayer.rotationYaw);
                    event.x = -Math.sin(yaw) * 2.0;
                    event.z = Math.cos(yaw) * 2.0;
                    this.cubecraftTeleportTickTimer.reset();
                    break;
                }
                final double yaw = Math.toRadians(Fly.mc.thePlayer.rotationYaw);
                event.x = -Math.sin(yaw) * 0.2;
                event.z = Math.cos(yaw) * 0.2;
                break;
            }
            case "boosthypixel": {
                if (!MovementUtils.isMoving()) {
                    final double n2 = 0.0;
                    event.z = n2;
                    event.x = n2;
                    break;
                }
                final double amplifier = 1.0 + (Fly.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (0.2 * (Fly.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1)) : 0.0);
                final double baseSpeed = 0.29 * amplifier;
                switch (this.boostHypixelState) {
                    case 1: {
                        this.moveSpeed = (Fly.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56 : 2.034) * baseSpeed;
                        this.boostHypixelState = 2;
                        break;
                    }
                    case 2: {
                        this.moveSpeed *= 2.16;
                        this.boostHypixelState = 3;
                        break;
                    }
                    case 3: {
                        this.moveSpeed = this.lastDistance - ((Fly.mc.thePlayer.ticksExisted % 2 == 0) ? 0.0103 : 0.0123) * (this.lastDistance - baseSpeed);
                        this.boostHypixelState = 4;
                        break;
                    }
                    default: {
                        this.moveSpeed = this.lastDistance - this.lastDistance / 159.8;
                        break;
                    }
                }
                this.moveSpeed = Math.max(this.moveSpeed, 0.3);
                final double yaw2 = MovementUtils.getDirection();
                final EntityPlayerSP thePlayer = Fly.mc.thePlayer;
                final double n3 = -Math.sin(yaw2) * this.moveSpeed;
                event.x = n3;
                thePlayer.motionX = n3;
                final EntityPlayerSP thePlayer2 = Fly.mc.thePlayer;
                final double n4 = Math.cos(yaw2) * this.moveSpeed;
                event.z = n4;
                thePlayer2.motionZ = n4;
                break;
            }
            case "freehypixel": {
                if (!this.freeHypixelTimer.hasTimePassed(10)) {
                    final double x = 0.0;
                    event.z = x;
                    event.y = x;
                    event.x = x;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onBB(final BlockBBEvent event) {
        if (Fly.mc.thePlayer == null) {
            return;
        }
        final String flyMode = this.modeValue.asString();
        if (event.getBlock() instanceof BlockAir && (flyMode.equalsIgnoreCase("OtherHypixel") || flyMode.equalsIgnoreCase("Hypixel") || flyMode.equalsIgnoreCase("LatestHypixel") || flyMode.equalsIgnoreCase("BoostHypixel") || flyMode.equalsIgnoreCase("Rewinside") || (flyMode.equalsIgnoreCase("Mineplex") && Fly.mc.thePlayer.inventory.getCurrentItem() == null)) && event.getY() < Fly.mc.thePlayer.posY) {
            event.setBoundingBox(AxisAlignedBB.fromBounds((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)(event.getX() + 1), Fly.mc.thePlayer.posY, (double)(event.getZ() + 1)));
        }
    }
    
    @EventTarget
    public void onJump(final JumpEvent e) {
        final String flyMode = this.modeValue.asString();
        if (flyMode.equalsIgnoreCase("Hypixel") || flyMode.equalsIgnoreCase("OtherHypixel") || flyMode.equalsIgnoreCase("LatestHypixel") || flyMode.equalsIgnoreCase("BoostHypixel") || flyMode.equalsIgnoreCase("Rewinside") || (flyMode.equalsIgnoreCase("Mineplex") && Fly.mc.thePlayer.inventory.getCurrentItem() == null)) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onStep(final StepEvent e) {
        final String flyMode = this.modeValue.asString();
        if (flyMode.equalsIgnoreCase("Hypixel") || flyMode.equalsIgnoreCase("OtherHypixel") || flyMode.equalsIgnoreCase("LatestHypixel") || flyMode.equalsIgnoreCase("BoostHypixel") || flyMode.equalsIgnoreCase("Rewinside") || (flyMode.equalsIgnoreCase("Mineplex") && Fly.mc.thePlayer.inventory.getCurrentItem() == null)) {
            e.setStepHeight(0.0f);
        }
    }
    
    private void handleVanillaKickBypass() {
        if (!this.vanillaKickBypassValue.asBoolean() || !this.groundTimer.hasTimePassed(1000L)) {
            return;
        }
        final double ground = this.calculateGround();
        for (double posY = Fly.mc.thePlayer.posY; posY > ground; posY -= 8.0) {
            Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, posY, Fly.mc.thePlayer.posZ, true));
            if (posY - 8.0 < ground) {
                break;
            }
        }
        Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, ground, Fly.mc.thePlayer.posZ, true));
        for (double posY = ground; posY < Fly.mc.thePlayer.posY; posY += 8.0) {
            Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, posY, Fly.mc.thePlayer.posZ, true));
            if (posY + 8.0 > Fly.mc.thePlayer.posY) {
                break;
            }
        }
        Fly.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, true));
        this.groundTimer.reset();
    }
    
    private double calculateGround() {
        final AxisAlignedBB playerBoundingBox = Fly.mc.thePlayer.getEntityBoundingBox();
        for (double blockHeight = 1.0, ground = Fly.mc.thePlayer.posY; ground > 0.0; ground -= blockHeight) {
            final AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);
            if (Fly.mc.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05) {
                    return ground + blockHeight;
                }
                ground += blockHeight;
                blockHeight = 0.05;
            }
        }
        return 0.0;
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
