
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.StepConfirmEvent;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Phase;
import net.ccbluex.liquidbounce.event.events.StepEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockAir;
import net.minecraft.world.World;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Step", description = "Allows you to step up blocks.", category = ModuleCategory.MOVEMENT)
public class Step extends Module {
    private final ListValue modeValue;
    private final FloatValue heightValue;
    private final IntegerValue delayValue;
    private boolean isStep;
    private double stepX;
    private double stepY;
    private double stepZ;
    private boolean spartanSwitch;
    private boolean isAACStep;
    private final MSTimer msTimer;

    public Step() {
        this.modeValue = new ListValue("Mode", new String[]{"Vanilla", "Jump", "NCP", "AAC", "LAAC", "AAC3.3.4", "OldNCP", "Spartan", "Rewinside"}, "NCP");
        this.heightValue = new FloatValue("Height", 1.0f, 0.6f, 10.0f);
        this.delayValue = new IntegerValue("Delay", 0, 0, 500);
        this.msTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("step", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("height")) {
                        if (args.length > 2) {
                            try {
                                final float height = Float.parseFloat(args[2]);
                                Step.this.heightValue.setValue(height);
                                this.chat("§7Step height was set to §8" + height + "§7.");
                                Step$1.mc.getSoundHandler().playSound((ISound) PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            } catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".step height <value>");
                        return;
                    } else if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Step.this.modeValue.contains(args[2])) {
                            Step.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Step mode was set to §8" + Step.this.modeValue.asString().toUpperCase() + "§7.");
                            Step$1.mc.getSoundHandler().playSound((ISound) PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".step mode §c<§8" + Strings.join(Step.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    } else if (args[1].equalsIgnoreCase("delay")) {
                        if (args.length > 2) {
                            try {
                                final int delay = Integer.parseInt(args[2]);
                                Step.this.delayValue.setValue(delay);
                                this.chat("§7Step delay was set to §8" + delay + "§7.");
                                Step$1.mc.getSoundHandler().playSound((ISound) PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            } catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".step delay <value>");
                        return;
                    }
                }
                this.chatSyntax(".step <height, mode, delay>");
            }
        });
    }

    @Override
    public void onDisable() {
        if (Step.mc.thePlayer == null) {
            return;
        }
        Step.mc.thePlayer.stepHeight = 0.5f;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "jump": {
                if (Step.mc.thePlayer.isCollidedHorizontally && Step.mc.thePlayer.onGround && !Step.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Step.mc.thePlayer.jump();
                    break;
                }
                break;
            }
            case "laac": {
                if (Step.mc.thePlayer.isCollidedHorizontally && !Step.mc.thePlayer.isOnLadder() && !Step.mc.thePlayer.isInWater() && !Step.mc.thePlayer.isInLava() && !Step.mc.thePlayer.isInWeb) {
                    if (Step.mc.thePlayer.onGround && this.msTimer.hasTimePassed(this.delayValue.asInteger())) {
                        this.isStep = true;
                        final EntityPlayerSP thePlayer = Step.mc.thePlayer;
                        thePlayer.motionY += 0.620000001490116;
                        final float f = Step.mc.thePlayer.rotationYaw * 0.017453292f;
                        final EntityPlayerSP thePlayer2 = Step.mc.thePlayer;
                        thePlayer2.motionX -= MathHelper.sin(f) * 0.2f;
                        final EntityPlayerSP thePlayer3 = Step.mc.thePlayer;
                        thePlayer3.motionZ += MathHelper.cos(f) * 0.2f;
                        this.msTimer.reset();
                    }
                    Step.mc.thePlayer.onGround = true;
                    break;
                }
                this.isStep = false;
                break;
            }
            case "aac3.3.4": {
                if (!Step.mc.thePlayer.isCollidedHorizontally || !MovementUtils.isMoving()) {
                    this.isAACStep = false;
                    break;
                }
                if (Step.mc.thePlayer.onGround) {
                    final double yaw = Math.toRadians(Step.mc.thePlayer.rotationYaw);
                    final double x = -Math.sin(yaw) * 1.0;
                    final double z = Math.cos(yaw) * 1.0;
                    final BlockPos blockPos = new BlockPos(Step.mc.thePlayer.posX + x, Step.mc.thePlayer.posY + 1.0, Step.mc.thePlayer.posZ + z);
                    final Block block = BlockUtils.getBlock(blockPos);
                    final AxisAlignedBB axisAlignedBB = block.getCollisionBoundingBox((World) Step.mc.theWorld, blockPos, BlockUtils.getState(blockPos));
                    if (!(BlockUtils.getBlock(new BlockPos(Step.mc.thePlayer.posX + x, Step.mc.thePlayer.posY, Step.mc.thePlayer.posZ + z)) instanceof BlockAir) && (axisAlignedBB == null || block instanceof BlockSnow)) {
                        final EntityPlayerSP thePlayer4 = Step.mc.thePlayer;
                        thePlayer4.motionX *= 1.26;
                        final EntityPlayerSP thePlayer5 = Step.mc.thePlayer;
                        thePlayer5.motionZ *= 1.26;
                        Step.mc.thePlayer.jump();
                        this.isAACStep = true;
                    }
                }
                if (this.isAACStep && Step.mc.thePlayer.moveStrafing == 0.0f) {
                    final EntityPlayerSP thePlayer6 = Step.mc.thePlayer;
                    thePlayer6.motionY -= 0.015;
                    Step.mc.thePlayer.jumpMovementFactor = 0.3f;
                    break;
                }
                break;
            }
        }
    }

    @EventTarget
    public void onStep(final StepEvent event) {
        if (Step.mc.thePlayer == null) {
            return;
        }
        if (ModuleManager.getModule(Phase.class).getState()) {
            event.setStepHeight(0.0f);
            return;
        }
        final Fly fly = (Fly) ModuleManager.getModule(Fly.class);
        if (fly.getState()) {
            final String flyMode = fly.modeValue.asString();
            if (flyMode.equalsIgnoreCase("Hypixel") || flyMode.equalsIgnoreCase("OtherHypixel") || flyMode.equalsIgnoreCase("LatestHypixel") || flyMode.equalsIgnoreCase("Rewinside") || (flyMode.equalsIgnoreCase("Mineplex") && Step.mc.thePlayer.inventory.getCurrentItem() == null)) {
                event.setStepHeight(0.0f);
                return;
            }
        }
        final String mode = this.modeValue.asString();
        if (!Step.mc.thePlayer.onGround || !this.msTimer.hasTimePassed(this.delayValue.asInteger()) || mode.equalsIgnoreCase("Jump") || mode.equalsIgnoreCase("LAAC") || mode.equalsIgnoreCase("AAC3.3.4")) {
            event.setStepHeight(Step.mc.thePlayer.stepHeight = 0.5f);
            return;
        }
        final float height = this.heightValue.asFloat();
        event.setStepHeight(Step.mc.thePlayer.stepHeight = height);
        if (event.getStepHeight() > 0.5f) {
            this.isStep = true;
            this.stepX = Step.mc.thePlayer.posX;
            this.stepY = Step.mc.thePlayer.posY;
            this.stepZ = Step.mc.thePlayer.posZ;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onStepConfirm(final StepConfirmEvent event) {
        if (this.isStep) {
            final String mode = this.modeValue.asString();
            if (Step.mc.thePlayer.getEntityBoundingBox().minY - this.stepY > 0.5) {
                if (mode.equalsIgnoreCase("NCP") || mode.equalsIgnoreCase("AAC")) {
                    Step.mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                    Step.mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                    this.msTimer.reset();
                } else if (mode.equalsIgnoreCase("Spartan")) {
                    if (this.spartanSwitch) {
                        Step.mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                        Step.mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                        Step.mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                    } else {
                        Step.mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.6, this.stepZ, false));
                    }
                    this.spartanSwitch = !this.spartanSwitch;
                    this.msTimer.reset();
                } else if (mode.equalsIgnoreCase("Rewinside")) {
                    Step.mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                    Step.mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                    Step.mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                    this.msTimer.reset();
                }
            }
            this.isStep = false;
            this.stepX = 0.0;
            this.stepY = 0.0;
            this.stepZ = 0.0;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;
            if (this.isStep && this.modeValue.asString().equalsIgnoreCase("OldNCP")) {
                final C03PacketPlayer c03PacketPlayer = packetPlayer;
                c03PacketPlayer.y += 0.07;
                this.isStep = false;
            }
        }
    }

    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
