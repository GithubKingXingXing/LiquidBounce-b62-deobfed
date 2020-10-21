
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.item.ItemBlock;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Scaffold", description = "Automatically places blocks beneath your feet.", category = ModuleCategory.WORLD, keyBind = 23)
public class Scaffold extends Module
{
    public final ListValue modeValue;
    private final IntegerValue maxDelayValue;
    private final IntegerValue minDelayValue;
    private final BoolValue autoBlockValue;
    private final BoolValue stayAutoBlock;
    public final BoolValue sprintValue;
    private final BoolValue swingValue;
    private final BoolValue searchValue;
    private final BoolValue eagleValue;
    private final BoolValue placeableDelay;
    private final IntegerValue blocksToEagleValue;
    private final IntegerValue expandLengthValue;
    private final BoolValue rotationsValue;
    private final BoolValue keepRotation;
    private final BoolValue zitterValue;
    private final ListValue zitterModeValue;
    private final FloatValue zitterSpeed;
    private final FloatValue zitterStrength;
    private final FloatValue timerValue;
    private final FloatValue speedModifierValue;
    private final BoolValue sameYValue;
    private final BoolValue airSafeValue;
    private final BoolValue counterDisplayValue;
    private final BoolValue markValue;
    private BlockPos targetBlock;
    private EnumFacing targetFacing;
    private Vec3 targetVec;
    private int launchY;
    private float yaw;
    private float pitch;
    private int slot;
    private boolean switchAACAD;
    private final MSTimer delayTimer;
    private int placedBlocksWithoutEagle;
    private final MSTimer zitterTimer;
    private long delay;
    
    public Scaffold() {
        this.modeValue = new ListValue("Mode", new String[] { "Normal", "Rewinside", "Expand" }, "Normal");
        this.maxDelayValue = new IntegerValue("MaxDelay", 0, 0, 1000) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = Scaffold.this.minDelayValue.asInteger();
                if (i > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
            }
        };
        this.minDelayValue = new IntegerValue("MinDelay", 0, 0, 1000) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int i = Scaffold.this.maxDelayValue.asInteger();
                if (i < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(i);
                }
            }
        };
        this.autoBlockValue = new BoolValue("AutoBlock", true);
        this.stayAutoBlock = new BoolValue("StayAutoBlock", false);
        this.sprintValue = new BoolValue("Sprint", true);
        this.swingValue = new BoolValue("Swing", true);
        this.searchValue = new BoolValue("Search", true);
        this.eagleValue = new BoolValue("Eagle", false);
        this.placeableDelay = new BoolValue("PlaceableDelay", false);
        this.blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10);
        this.expandLengthValue = new IntegerValue("ExpandLength", 5, 1, 6);
        this.rotationsValue = new BoolValue("Rotations", true);
        this.keepRotation = new BoolValue("KeepRotation", false);
        this.zitterValue = new BoolValue("Zitter", false);
        this.zitterModeValue = new ListValue("ZitterMode", new String[] { "Teleport", "Smooth" }, "Teleport");
        this.zitterSpeed = new FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f);
        this.zitterStrength = new FloatValue("ZitterStrength", 0.072f, 0.05f, 0.2f);
        this.timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f);
        this.speedModifierValue = new FloatValue("SpeedModifier", 1.0f, 0.0f, 2.0f);
        this.sameYValue = new BoolValue("SameY", false);
        this.airSafeValue = new BoolValue("AirSafe", false);
        this.counterDisplayValue = new BoolValue("Counter", true);
        this.markValue = new BoolValue("Mark", false);
        this.yaw = Float.NaN;
        this.pitch = Float.NaN;
        this.delayTimer = new MSTimer();
        this.placedBlocksWithoutEagle = 0;
        this.zitterTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("scaffold", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Scaffold.this.modeValue.contains(args[2])) {
                            Scaffold.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Scaffold mode was set to §8" + Scaffold.this.modeValue.asString().toUpperCase() + "§7.");
                            Scaffold$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".scaffold mode §c<§8" + Strings.join(Scaffold.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else {
                        if (args[1].equalsIgnoreCase("autoblock")) {
                            Scaffold.this.autoBlockValue.setValue(!Scaffold.this.autoBlockValue.asBoolean());
                            this.chat("§7Scaffold autoblock was toggled §8" + (Scaffold.this.autoBlockValue.asBoolean() ? "on" : "off") + "§7.");
                            Scaffold$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("swing")) {
                            Scaffold.this.swingValue.setValue(!Scaffold.this.swingValue.asBoolean());
                            this.chat("§7Scaffold swing was toggled §8" + (Scaffold.this.swingValue.asBoolean() ? "on" : "off") + "§7.");
                            Scaffold$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("expandlength")) {
                            if (args.length > 2) {
                                try {
                                    final int value = Integer.parseInt(args[2]);
                                    Scaffold.this.expandLengthValue.setValue(value);
                                    this.chat("§7Scaffold ExpandLength was set to §8" + value + "§7.");
                                    Scaffold$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".scaffold expandlength <Length>");
                            return;
                        }
                        else if (args[1].equalsIgnoreCase("maxdelay")) {
                            if (args.length > 2) {
                                try {
                                    final int value = Integer.parseInt(args[2]);
                                    if (Scaffold.this.minDelayValue.asInteger() > value) {
                                        this.chat("MinDelay can't higher as MaxDelay!");
                                        return;
                                    }
                                    Scaffold.this.maxDelayValue.setValue(value);
                                    Scaffold.this.delay = TimeUtils.randomDelay(Scaffold.this.minDelayValue.asInteger(), Scaffold.this.maxDelayValue.asInteger());
                                    this.chat("§7Scaffold maxdelay was set to §8" + value + "§7.");
                                    Scaffold$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".scaffold maxdelay <value>");
                            return;
                        }
                        else if (args[1].equalsIgnoreCase("mindelay")) {
                            if (args.length > 2) {
                                try {
                                    final int value = Integer.parseInt(args[2]);
                                    if (Scaffold.this.maxDelayValue.asInteger() < value) {
                                        this.chat("MinDelay can't higher as MaxDelay!");
                                        return;
                                    }
                                    Scaffold.this.minDelayValue.setValue(value);
                                    Scaffold.this.delay = TimeUtils.randomDelay(Scaffold.this.minDelayValue.asInteger(), Scaffold.this.maxDelayValue.asInteger());
                                    this.chat("§7Scaffold mindelay was set to §8" + value + "§7.");
                                    Scaffold$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".scaffold mindelay <value>");
                            return;
                        }
                        else if (args[1].equalsIgnoreCase("timer")) {
                            if (args.length > 2) {
                                try {
                                    final float value2 = Float.parseFloat(args[2]);
                                    Scaffold.this.timerValue.setValue(value2);
                                    this.chat("§7Scaffold Timer was set to §8" + value2 + "§7.");
                                    Scaffold$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".scaffold timer <value>");
                            return;
                        }
                        else if (args[1].equalsIgnoreCase("speedmodifier")) {
                            if (args.length > 2) {
                                try {
                                    final float value2 = Float.parseFloat(args[2]);
                                    Scaffold.this.speedModifierValue.setValue(value2);
                                    this.chat("§7Scaffold SpeedModifier was set to §8" + value2 + "§7.");
                                    Scaffold$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                                }
                                catch (NumberFormatException exception) {
                                    this.chatSyntaxError();
                                }
                                return;
                            }
                            this.chatSyntax(".scaffold speedmodifier <value>");
                            return;
                        }
                    }
                }
                this.chatSyntax(".scaffold <mode, mindelay, maxdelay, autoblock, swing, expandlength, timer, speedmodifier>");
            }
        });
    }
    
    @Override
    public void onEnable() {
        this.launchY = (int)Scaffold.mc.thePlayer.posY;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        Scaffold.mc.timer.timerSpeed = this.timerValue.asFloat();
        if (Scaffold.mc.thePlayer.onGround) {
            if (!GameSettings.isKeyDown(Scaffold.mc.gameSettings.keyBindRight)) {
                Scaffold.mc.gameSettings.keyBindRight.pressed = false;
            }
            if (!GameSettings.isKeyDown(Scaffold.mc.gameSettings.keyBindLeft)) {
                Scaffold.mc.gameSettings.keyBindLeft.pressed = false;
            }
            final String value = this.modeValue.asString();
            if (value.equalsIgnoreCase("Rewinside")) {
                MovementUtils.strafe(0.2f);
                Scaffold.mc.thePlayer.motionY = 0.0;
            }
            if (this.zitterValue.asBoolean() && this.zitterModeValue.asString().equalsIgnoreCase("smooth")) {
                if (this.zitterTimer.hasTimePassed(100L)) {
                    this.switchAACAD = !this.switchAACAD;
                    this.zitterTimer.reset();
                }
                if (this.switchAACAD) {
                    Scaffold.mc.gameSettings.keyBindRight.pressed = true;
                    Scaffold.mc.gameSettings.keyBindLeft.pressed = false;
                }
                else {
                    Scaffold.mc.gameSettings.keyBindRight.pressed = false;
                    Scaffold.mc.gameSettings.keyBindLeft.pressed = true;
                }
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (Scaffold.mc.thePlayer == null) {
            return;
        }
        final Packet packet = event.getPacket();
        if (packet instanceof C09PacketHeldItemChange) {
            final C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange)packet;
            this.slot = packetHeldItemChange.getSlotId();
        }
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (this.rotationsValue.asBoolean() && this.keepRotation.asBoolean() && !Float.isNaN(this.yaw) && !Float.isNaN(this.pitch)) {
            RotationUtils.setTargetRotation(this.yaw, this.pitch);
        }
        final String mode = this.modeValue.asString();
        switch (event.getEventState()) {
            case PRE: {
                if (this.eagleValue.asBoolean()) {
                    if (this.placedBlocksWithoutEagle >= this.blocksToEagleValue.asInteger()) {
                        Scaffold.mc.gameSettings.keyBindSneak.pressed = (Scaffold.mc.theWorld.getBlockState(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ)).getBlock() == Blocks.air);
                        this.placedBlocksWithoutEagle = 0;
                    }
                    else {
                        ++this.placedBlocksWithoutEagle;
                    }
                }
                if (this.zitterValue.asBoolean() && this.zitterModeValue.asString().equalsIgnoreCase("teleport")) {
                    final EntityPlayerSP player = Scaffold.mc.thePlayer;
                    double yaw = player.rotationYaw;
                    MovementUtils.strafe(this.zitterSpeed.asFloat());
                    if (this.switchAACAD) {
                        yaw += 90.0;
                    }
                    else {
                        yaw -= 90.0;
                    }
                    yaw = Math.toRadians(yaw);
                    final EntityPlayerSP entityPlayerSP = player;
                    entityPlayerSP.motionX -= Math.sin(yaw) * this.zitterStrength.asFloat();
                    final EntityPlayerSP entityPlayerSP2 = player;
                    entityPlayerSP2.motionZ += Math.cos(yaw) * this.zitterStrength.asFloat();
                    this.switchAACAD = !this.switchAACAD;
                }
                final String lowerCase = mode.toLowerCase();
                switch (lowerCase) {
                    default: {
                        final BlockPos blockPosition = (Scaffold.mc.thePlayer.posY == (int)Scaffold.mc.thePlayer.posY + 0.5) ? new BlockPos((Entity)Scaffold.mc.thePlayer) : new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY, Scaffold.mc.thePlayer.posZ).down();
                        if (!BlockUtils.isReplaceable(blockPosition) || this.search(blockPosition)) {
                            return;
                        }
                        if (this.searchValue.asBoolean()) {
                            for (int x = -1; x <= 1; ++x) {
                                for (int z = -1; z <= 1; ++z) {
                                    if (this.search(blockPosition.add(x, 0, z))) {
                                        return;
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case "expand": {
                        int i = 0;
                        while (i < this.expandLengthValue.asInteger()) {
                            final BlockPos blockPos = new BlockPos(Scaffold.mc.thePlayer.posX + ((Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST) ? (-i) : ((Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST) ? i : 0)), Scaffold.mc.thePlayer.posY - ((Scaffold.mc.thePlayer.posY == (int)Scaffold.mc.thePlayer.posY + 0.5) ? 0.0 : 1.0), Scaffold.mc.thePlayer.posZ + ((Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH) ? (-i) : ((Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH) ? i : 0)));
                            final PlaceInfo placeInfo = PlaceInfo.get(blockPos);
                            if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                this.targetBlock = placeInfo.getBlockPos();
                                this.targetFacing = placeInfo.getEnumFacing();
                                this.targetVec = placeInfo.getVec3();
                                if (this.rotationsValue.asBoolean()) {
                                    RotationUtils.faceBlockPacket(blockPos);
                                    break;
                                }
                                break;
                            }
                            else {
                                ++i;
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case POST: {
                if (this.targetBlock == null || this.targetFacing == null || this.targetVec == null) {
                    if (this.placeableDelay.asBoolean()) {
                        this.delayTimer.reset();
                    }
                    return;
                }
                if (!this.delayTimer.hasTimePassed(this.delay) || (this.sameYValue.asBoolean() && this.launchY != (int)Scaffold.mc.thePlayer.posY)) {
                    return;
                }
                int blockSlot = Integer.MAX_VALUE;
                ItemStack itemStack = Scaffold.mc.thePlayer.getHeldItem();
                if (Scaffold.mc.thePlayer.getHeldItem() == null || !(Scaffold.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
                    if (!this.autoBlockValue.asBoolean()) {
                        return;
                    }
                    blockSlot = this.findBlock();
                    if (blockSlot == -1) {
                        return;
                    }
                    Scaffold.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(blockSlot - 36));
                    itemStack = Scaffold.mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack();
                }
                if (Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, itemStack, this.targetBlock, this.targetFacing, this.targetVec)) {
                    this.delayTimer.reset();
                    this.delay = TimeUtils.randomDelay(this.minDelayValue.asInteger(), this.maxDelayValue.asInteger());
                    if (Scaffold.mc.thePlayer.onGround) {
                        final EntityPlayerSP thePlayer = Scaffold.mc.thePlayer;
                        thePlayer.motionX *= this.speedModifierValue.asFloat();
                        final EntityPlayerSP thePlayer2 = Scaffold.mc.thePlayer;
                        thePlayer2.motionZ *= this.speedModifierValue.asFloat();
                    }
                    if (this.swingValue.asBoolean()) {
                        Scaffold.mc.thePlayer.swingItem();
                    }
                    else {
                        Scaffold.mc.getNetHandler().addToSendQueue((Packet)new C0APacketAnimation());
                    }
                }
                if (!this.stayAutoBlock.asBoolean() && blockSlot != Integer.MAX_VALUE) {
                    Scaffold.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(Scaffold.mc.thePlayer.inventory.currentItem));
                }
                this.targetBlock = null;
                this.targetFacing = null;
                this.targetVec = null;
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (Scaffold.mc.thePlayer == null) {
            return;
        }
        if (!GameSettings.isKeyDown(Scaffold.mc.gameSettings.keyBindSneak)) {
            Scaffold.mc.gameSettings.keyBindSneak.pressed = false;
        }
        if (!GameSettings.isKeyDown(Scaffold.mc.gameSettings.keyBindRight)) {
            Scaffold.mc.gameSettings.keyBindRight.pressed = false;
        }
        if (!GameSettings.isKeyDown(Scaffold.mc.gameSettings.keyBindLeft)) {
            Scaffold.mc.gameSettings.keyBindLeft.pressed = false;
        }
        this.yaw = Float.NaN;
        this.pitch = Float.NaN;
        Scaffold.mc.timer.timerSpeed = 1.0f;
        if (this.slot != Scaffold.mc.thePlayer.inventory.currentItem) {
            Scaffold.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(Scaffold.mc.thePlayer.inventory.currentItem));
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (this.airSafeValue.asBoolean() || Scaffold.mc.thePlayer.onGround) {
            event.setSafeWalk(true);
        }
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (this.counterDisplayValue.asBoolean()) {
            GlStateManager.pushMatrix();
            final BlockOverlay blockOverlay = (BlockOverlay)ModuleManager.getModule(BlockOverlay.class);
            if (blockOverlay.getState() && blockOverlay.infoValue.asBoolean() && Scaffold.mc.objectMouseOver != null && Scaffold.mc.objectMouseOver.getBlockPos() != null && Scaffold.mc.theWorld.getBlockState(Scaffold.mc.objectMouseOver.getBlockPos()).getBlock() != null && BlockUtils.canBeClicked(Scaffold.mc.objectMouseOver.getBlockPos()) && Scaffold.mc.theWorld.getWorldBorder().contains(Scaffold.mc.objectMouseOver.getBlockPos())) {
                GlStateManager.translate(0.0f, 15.0f, 0.0f);
            }
            final String info = "Blocks: §7" + this.getBlocksAmount();
            final ScaledResolution scaledResolution = new ScaledResolution(Scaffold.mc);
            RenderUtils.drawBorderedRect((float)(scaledResolution.getScaledWidth() / 2 - 2), (float)(scaledResolution.getScaledHeight() / 2 + 5), (float)(scaledResolution.getScaledWidth() / 2 + Fonts.font40.getStringWidth(info) + 2), (float)(scaledResolution.getScaledHeight() / 2 + 16), 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            GlStateManager.resetColor();
            Fonts.font40.drawString(info, scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 2 + 7, Color.WHITE.getRGB());
            GlStateManager.popMatrix();
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (!this.markValue.asBoolean()) {
            return;
        }
        for (int i = 0; i < (this.modeValue.asString().equalsIgnoreCase("Expand") ? (this.expandLengthValue.asInteger() + 1) : 2); ++i) {
            final BlockPos blockPos = new BlockPos(Scaffold.mc.thePlayer.posX + ((Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST) ? (-i) : ((Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST) ? i : 0)), Scaffold.mc.thePlayer.posY - ((Scaffold.mc.thePlayer.posY == (int)Scaffold.mc.thePlayer.posY + 0.5) ? 0.0 : 1.0), Scaffold.mc.thePlayer.posZ + ((Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH) ? (-i) : ((Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH) ? i : 0)));
            final PlaceInfo placeInfo = PlaceInfo.get(blockPos);
            if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                RenderUtils.drawBlockBox(blockPos, new Color(68, 117, 255, 100), false);
                break;
            }
        }
    }
    
    private boolean search(final BlockPos blockPosition) {
        if (!BlockUtils.isReplaceable(blockPosition)) {
            return false;
        }
        final Vec3 eyesPos = RotationUtils.getEyesPos();
        final Vec3 posVec = new Vec3((Vec3i)blockPosition).addVector(0.5, 0.5, 0.5);
        final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = blockPosition.offset(side);
            final Vec3 dirVec = new Vec3(side.getDirectionVec());
            final Vec3 hitVec = posVec.add(new Vec3(dirVec.xCoord * 0.5, dirVec.yCoord * 0.5, dirVec.zCoord * 0.5));
            if (BlockUtils.canBeClicked(neighbor) && eyesPos.squareDistanceTo(hitVec) <= 18.0625 && distanceSqPosVec <= eyesPos.squareDistanceTo(posVec.add(dirVec))) {
                if (Scaffold.mc.theWorld.rayTraceBlocks(eyesPos, hitVec, false, true, false) == null) {
                    final double diffX = hitVec.xCoord - eyesPos.xCoord;
                    final double diffY = hitVec.yCoord - eyesPos.yCoord;
                    final double diffZ = hitVec.zCoord - eyesPos.zCoord;
                    final double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
                    this.yaw = MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f);
                    this.pitch = MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))));
                    if (this.rotationsValue.asBoolean()) {
                        RotationUtils.setTargetRotation(this.yaw, this.pitch);
                    }
                    final Vec3 rotationVector = RotationUtils.getVectorForRotation(this.pitch, this.yaw);
                    final Vec3 vector = eyesPos.addVector(rotationVector.xCoord * 4.0, rotationVector.yCoord * 4.0, rotationVector.zCoord * 4.0);
                    final MovingObjectPosition obj = Scaffold.mc.theWorld.rayTraceBlocks(eyesPos, vector, false, false, true);
                    if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        if (obj.getBlockPos().equals((Object)neighbor)) {
                            this.targetBlock = neighbor;
                            this.targetFacing = side.getOpposite();
                            this.targetVec = hitVec;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private int findBlock() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }
    
    private int getBlocksAmount() {
        int amount = 0;
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                amount += itemStack.stackSize;
            }
        }
        return amount;
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
