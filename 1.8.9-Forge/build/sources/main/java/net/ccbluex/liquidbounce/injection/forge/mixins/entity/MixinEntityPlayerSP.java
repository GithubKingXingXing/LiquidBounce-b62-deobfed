
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.block.Block;

import java.util.Iterator;
import java.util.List;

import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.event.events.StepConfirmEvent;
import net.ccbluex.liquidbounce.event.events.StepEvent;
import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.minecraft.potion.Potion;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiScreen;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.event.events.PushOutEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.ccbluex.liquidbounce.features.module.modules.render.NoSwing;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.features.module.modules.fun.Derp;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AntiHunger;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.MovementInput;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP extends MixinAbstractClientPlayer {
    @Shadow
    public int sprintingTicksLeft;
    @Shadow
    public float timeInPortal;
    @Shadow
    public float prevTimeInPortal;
    @Shadow
    public MovementInput movementInput;
    @Shadow
    @Final
    public NetHandlerPlayClient sendQueue;
    @Shadow
    protected int sprintToggleTimer;
    @Shadow
    protected Minecraft mc;
    @Shadow
    private boolean serverSprintState;
    @Shadow
    private float horseJumpPower;
    @Shadow
    private int horseJumpPowerCounter;
    @Shadow
    private boolean serverSneakState;
    @Shadow
    private double lastReportedPosX;
    @Shadow
    private int positionUpdateTicks;
    @Shadow
    private double lastReportedPosY;
    @Shadow
    private double lastReportedPosZ;
    @Shadow
    private float lastReportedYaw;
    @Shadow
    private float lastReportedPitch;

    @Shadow
    public abstract void playSound(final String p0, final float p1, final float p2);

    @Shadow
    public abstract void setSprinting(final boolean p0);

    @Shadow
    protected abstract boolean pushOutOfBlocks(final double p0, final double p1, final double p2);

    @Shadow
    public abstract void sendPlayerAbilities();

    @Shadow
    protected abstract void sendHorseJump();

    @Shadow
    public abstract boolean isRidingHorse();

    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    protected abstract boolean isCurrentViewEntity();

    @Overwrite
    public void onUpdateWalkingPlayer() {
        try {
            LiquidBounce.CLIENT.eventManager.callEvent(new MotionEvent(EventState.PRE));
            final InventoryMove inventoryMove = (InventoryMove) ModuleManager.getModule(InventoryMove.class);
            final Sneak sneak = (Sneak) ModuleManager.getModule(Sneak.class);
            final boolean fakeSprint = (inventoryMove.getState() && inventoryMove.aacAdditionProValue.asBoolean()) || ModuleManager.getModule(AntiHunger.class).getState() || (sneak.getState() && (!MovementUtils.isMoving() || !sneak.stopMoveValue.asBoolean()) && sneak.modeValue.asString().equalsIgnoreCase("MineSecure"));
            final boolean sprinting = this.isSprinting() && !fakeSprint;
            if (sprinting != this.serverSprintState) {
                if (sprinting) {
                    this.sendQueue.addToSendQueue((Packet) new C0BPacketEntityAction((Entity) this, C0BPacketEntityAction.Action.START_SPRINTING));
                } else {
                    this.sendQueue.addToSendQueue((Packet) new C0BPacketEntityAction((Entity) this, C0BPacketEntityAction.Action.STOP_SPRINTING));
                }
                this.serverSprintState = sprinting;
            }
            final boolean sneaking = this.isSneaking();
            if (sneaking != this.serverSneakState && !sneak.getState() && !sneak.modeValue.asString().equalsIgnoreCase("Legit")) {
                if (sneaking) {
                    this.sendQueue.addToSendQueue((Packet) new C0BPacketEntityAction((Entity) this, C0BPacketEntityAction.Action.START_SNEAKING));
                } else {
                    this.sendQueue.addToSendQueue((Packet) new C0BPacketEntityAction((Entity) this, C0BPacketEntityAction.Action.STOP_SNEAKING));
                }
                this.serverSneakState = sneaking;
            }
            if (this.isCurrentViewEntity()) {
                float yaw = this.rotationYaw;
                float pitch = this.rotationPitch;
                final float lastReportedYaw = RotationUtils.lastLook[0];
                final float lastReportedPitch = RotationUtils.lastLook[1];
                final Derp derp = (Derp) ModuleManager.getModule(Derp.class);
                if (derp.getState()) {
                    final float[] rot = derp.getRotation();
                    yaw = rot[0];
                    pitch = rot[1];
                }
                if (RotationUtils.lookChanged) {
                    yaw = RotationUtils.targetYaw;
                    pitch = RotationUtils.targetPitch;
                }
                final double xDiff = this.posX - this.lastReportedPosX;
                final double yDiff = this.getEntityBoundingBox().minY - this.lastReportedPosY;
                final double zDiff = this.posZ - this.lastReportedPosZ;
                final double yawDiff = yaw - lastReportedYaw;
                final double pitchDiff = pitch - lastReportedPitch;
                boolean moved = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff > 9.0E-4 || this.positionUpdateTicks >= 20;
                final boolean rotated = yawDiff != 0.0 || pitchDiff != 0.0;
                if (this.ridingEntity == null) {
                    if (moved && rotated) {
                        this.sendQueue.addToSendQueue((Packet) new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, this.getEntityBoundingBox().minY, this.posZ, yaw, pitch, this.onGround));
                    } else if (moved) {
                        this.sendQueue.addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.onGround));
                    } else if (rotated) {
                        this.sendQueue.addToSendQueue((Packet) new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, this.onGround));
                    } else {
                        this.sendQueue.addToSendQueue((Packet) new C03PacketPlayer(this.onGround));
                    }
                } else {
                    this.sendQueue.addToSendQueue((Packet) new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0, this.motionZ, yaw, pitch, this.onGround));
                    moved = false;
                }
                ++this.positionUpdateTicks;
                if (moved) {
                    this.lastReportedPosX = this.posX;
                    this.lastReportedPosY = this.getEntityBoundingBox().minY;
                    this.lastReportedPosZ = this.posZ;
                    this.positionUpdateTicks = 0;
                }
                if (rotated) {
                    this.lastReportedYaw = this.rotationYaw;
                    this.lastReportedPitch = this.rotationPitch;
                }
            }
            LiquidBounce.CLIENT.eventManager.callEvent(new MotionEvent(EventState.POST));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = {"swingItem"}, at = {@At("HEAD")}, cancellable = true)
    private void swingItem(final CallbackInfo callbackInfo) {
        final NoSwing noSwing = (NoSwing) ModuleManager.getModule(NoSwing.class);
        if (noSwing.getState()) {
            callbackInfo.cancel();
            if (!noSwing.serverSideValue.asBoolean()) {
                this.sendQueue.addToSendQueue((Packet) new C0APacketAnimation());
            }
        }
    }

    @Inject(method = {"pushOutOfBlocks"}, at = {@At("HEAD")}, cancellable = true)
    private void onPushOutOfBlocks(final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final PushOutEvent event = new PushOutEvent();
        event.setCancelled(this.noClip);
        LiquidBounce.CLIENT.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Overwrite
    @Override
    public void onLivingUpdate() {
        LiquidBounce.CLIENT.eventManager.callEvent(new UpdateEvent());
        if (this.sprintingTicksLeft > 0) {
            --this.sprintingTicksLeft;
            if (this.sprintingTicksLeft == 0) {
                this.setSprinting(false);
            }
        }
        if (this.sprintToggleTimer > 0) {
            --this.sprintToggleTimer;
        }
        this.prevTimeInPortal = this.timeInPortal;
        if (this.inPortal) {
            if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
                this.mc.displayGuiScreen((GuiScreen) null);
            }
            if (this.timeInPortal == 0.0f) {
                this.mc.getSoundHandler().playSound((ISound) PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4f + 0.8f));
            }
            this.timeInPortal += 0.0125f;
            if (this.timeInPortal >= 1.0f) {
                this.timeInPortal = 1.0f;
            }
            this.inPortal = false;
        } else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
            this.timeInPortal += 0.006666667f;
            if (this.timeInPortal > 1.0f) {
                this.timeInPortal = 1.0f;
            }
        } else {
            if (this.timeInPortal > 0.0f) {
                this.timeInPortal -= 0.05f;
            }
            if (this.timeInPortal < 0.0f) {
                this.timeInPortal = 0.0f;
            }
        }
        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }
        final boolean flag = this.movementInput.jump;
        final boolean flag2 = this.movementInput.sneak;
        final float f = 0.8f;
        final boolean flag3 = this.movementInput.moveForward >= f;
        this.movementInput.updatePlayerMoveState();
        final NoSlow noSlow = (NoSlow) ModuleManager.getModule(NoSlow.class);
        final KillAura killAura = (KillAura) ModuleManager.getModule(KillAura.class);
        if (this.getHeldItem() != null && (this.isUsingItem() || (this.getHeldItem().getItem() instanceof ItemSword && killAura.blocking)) && !this.isRiding()) {
            final String mode = noSlow.getMode(this.getHeldItem().getItem()).toLowerCase();
            if (noSlow.getState() && !mode.equalsIgnoreCase("off")) {
                final String lowerCase = mode.toLowerCase();
                switch (lowerCase) {
                    case "aac": {
                        final MovementInput movementInput = this.movementInput;
                        movementInput.moveStrafe *= 0.6f;
                        final MovementInput movementInput2 = this.movementInput;
                        movementInput2.moveForward *= 0.6f;
                        break;
                    }
                    case "spartan": {
                        final MovementInput movementInput3 = this.movementInput;
                        movementInput3.moveStrafe *= 0.6f;
                        final MovementInput movementInput4 = this.movementInput;
                        movementInput4.moveForward *= 0.6f;
                        break;
                    }
                    case "killswitch": {
                        final MovementInput movementInput5 = this.movementInput;
                        movementInput5.moveStrafe *= 0.7f;
                        final MovementInput movementInput6 = this.movementInput;
                        movementInput6.moveForward *= 0.7f;
                        break;
                    }
                    case "antiaura": {
                        final MovementInput movementInput7 = this.movementInput;
                        movementInput7.moveStrafe *= 0.7f;
                        final MovementInput movementInput8 = this.movementInput;
                        movementInput8.moveForward *= 0.7f;
                        break;
                    }
                    case "nnc": {
                        if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                            break;
                        }
                        if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow && this.mc.thePlayer.getItemInUseDuration() >= 25) {
                            break;
                        }
                        final MovementInput movementInput9 = this.movementInput;
                        movementInput9.moveStrafe *= 0.2f;
                        final MovementInput movementInput10 = this.movementInput;
                        movementInput10.moveForward *= 0.2f;
                        this.sprintToggleTimer = 0;
                        break;
                    }
                    case "custom": {
                        final float v = 1.0f - noSlow.customReducementValue.asFloat();
                        final MovementInput movementInput11 = this.movementInput;
                        movementInput11.moveStrafe *= v;
                        final MovementInput movementInput12 = this.movementInput;
                        movementInput12.moveForward *= v;
                        break;
                    }
                }
            } else {
                final MovementInput movementInput13 = this.movementInput;
                movementInput13.moveStrafe *= 0.2f;
                final MovementInput movementInput14 = this.movementInput;
                movementInput14.moveForward *= 0.2f;
                this.sprintToggleTimer = 0;
            }
        }
        this.pushOutOfBlocks(this.posX - this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + this.width * 0.35);
        this.pushOutOfBlocks(this.posX - this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - this.width * 0.35);
        this.pushOutOfBlocks(this.posX + this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - this.width * 0.35);
        this.pushOutOfBlocks(this.posX + this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + this.width * 0.35);
        final Sprint sprint = (Sprint) ModuleManager.getModule(Sprint.class);
        final boolean flag4 = !sprint.foodValue.asBoolean() || this.getFoodStats().getFoodLevel() > 6.0f || this.capabilities.allowFlying;
        if (this.onGround && !flag2 && !flag3 && this.movementInput.moveForward >= f && !this.isSprinting() && flag4 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
                this.sprintToggleTimer = 7;
            } else {
                this.setSprinting(true);
            }
        }
        if (!this.isSprinting() && this.movementInput.moveForward >= f && flag4 && (noSlow.getState() || !this.isUsingItem()) && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.setSprinting(true);
        }
        final Scaffold scaffold = (Scaffold) ModuleManager.getModule(Scaffold.class);
        if ((scaffold.getState() && !scaffold.sprintValue.asBoolean()) || (sprint.getState() && sprint.checkServerSide.asBoolean() && !sprint.allDirectionsValue.asBoolean() && RotationUtils.lookChanged && RotationUtils.getRotationDifference(this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch) > 30.0)) {
            this.setSprinting(false);
        }
        if (this.isSprinting() && ((sprint.getState() && sprint.allDirectionsValue.asBoolean() && this.movementInput.moveForward < f) || this.isCollidedHorizontally || !flag4)) {
            this.setSprinting(false);
        }
        if (this.capabilities.allowFlying) {
            if (this.mc.playerController.isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            } else if (!flag && this.movementInput.jump) {
                if (this.flyToggleTimer == 0) {
                    this.flyToggleTimer = 7;
                } else {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }
        if (this.capabilities.isFlying && this.isCurrentViewEntity()) {
            if (this.movementInput.sneak) {
                this.motionY -= this.capabilities.getFlySpeed() * 3.0f;
            }
            if (this.movementInput.jump) {
                this.motionY += this.capabilities.getFlySpeed() * 3.0f;
            }
        }
        if (this.isRidingHorse()) {
            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter == 0) {
                    this.horseJumpPower = 0.0f;
                }
            }
            if (flag && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            } else if (!flag && this.movementInput.jump) {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0f;
            } else if (flag) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter < 10) {
                    this.horseJumpPower = this.horseJumpPowerCounter * 0.1f;
                } else {
                    this.horseJumpPower = 0.8f + 2.0f / (this.horseJumpPowerCounter - 9) * 0.1f;
                }
            }
        } else {
            this.horseJumpPower = 0.0f;
        }
        super.onLivingUpdate();
        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        final MoveEvent moveEvent = new MoveEvent(x, y, z);
        LiquidBounce.CLIENT.eventManager.callEvent(moveEvent);
        if (moveEvent.isCancelled()) {
            return;
        }
        x = moveEvent.getX();
        y = moveEvent.getY();
        z = moveEvent.getZ();
        if (this.noClip) {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
            this.posY = this.getEntityBoundingBox().minY;
            this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
        } else {
            this.worldObj.theProfiler.startSection("move");
            final double d0 = this.posX;
            final double d2 = this.posY;
            final double d3 = this.posZ;
            if (this.isInWeb) {
                this.isInWeb = false;
                x *= 0.25;
                y *= 0.05000000074505806;
                z *= 0.25;
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            double d4 = x;
            final double d5 = y;
            double d6 = z;
            final boolean flag = this.onGround && this.isSneaking();
            if (flag || moveEvent.safeWalk) {
                final double d7 = 0.05;
                while (x != 0.0 && this.worldObj.getCollidingBoundingBoxes((Entity) this, this.getEntityBoundingBox().offset(x, -1.0, 0.0)).isEmpty()) {
                    if (x < d7 && x >= -d7) {
                        x = 0.0;
                    } else if (x > 0.0) {
                        x -= d7;
                    } else {
                        x += d7;
                    }
                    d4 = x;
                }
                while (z != 0.0 && this.worldObj.getCollidingBoundingBoxes((Entity) this, this.getEntityBoundingBox().offset(0.0, -1.0, z)).isEmpty()) {
                    if (z < d7 && z >= -d7) {
                        z = 0.0;
                    } else if (z > 0.0) {
                        z -= d7;
                    } else {
                        z += d7;
                    }
                    d6 = z;
                }
                while (x != 0.0 && z != 0.0 && this.worldObj.getCollidingBoundingBoxes((Entity) this, this.getEntityBoundingBox().offset(x, -1.0, z)).isEmpty()) {
                    if (x < d7 && x >= -d7) {
                        x = 0.0;
                    } else if (x > 0.0) {
                        x -= d7;
                    } else {
                        x += d7;
                    }
                    d4 = x;
                    if (z < d7 && z >= -d7) {
                        z = 0.0;
                    } else if (z > 0.0) {
                        z -= d7;
                    } else {
                        z += d7;
                    }
                    d6 = z;
                }
            }
            final List<AxisAlignedBB> list1 = (List<AxisAlignedBB>) this.worldObj.getCollidingBoundingBoxes((Entity) this, this.getEntityBoundingBox().addCoord(x, y, z));
            final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            for (final AxisAlignedBB axisalignedbb2 : list1) {
                y = axisalignedbb2.calculateYOffset(this.getEntityBoundingBox(), y);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
            final boolean flag2 = this.onGround || (d5 != y && d5 < 0.0);
            for (final AxisAlignedBB axisalignedbb3 : list1) {
                x = axisalignedbb3.calculateXOffset(this.getEntityBoundingBox(), x);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0, 0.0));
            for (final AxisAlignedBB axisalignedbb4 : list1) {
                z = axisalignedbb4.calculateZOffset(this.getEntityBoundingBox(), z);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, z));
            if (this.stepHeight > 0.0f && flag2 && (d4 != x || d6 != z)) {
                final StepEvent stepEvent = new StepEvent(this.stepHeight);
                LiquidBounce.CLIENT.eventManager.callEvent(stepEvent);
                final double d8 = x;
                final double d9 = y;
                final double d10 = z;
                final AxisAlignedBB axisalignedbb5 = this.getEntityBoundingBox();
                this.setEntityBoundingBox(axisalignedbb);
                y = stepEvent.getStepHeight();
                final List<AxisAlignedBB> list2 = (List<AxisAlignedBB>) this.worldObj.getCollidingBoundingBoxes((Entity) this, this.getEntityBoundingBox().addCoord(d4, y, d6));
                AxisAlignedBB axisalignedbb6 = this.getEntityBoundingBox();
                final AxisAlignedBB axisalignedbb7 = axisalignedbb6.addCoord(d4, 0.0, d6);
                double d11 = y;
                for (final AxisAlignedBB axisalignedbb8 : list2) {
                    d11 = axisalignedbb8.calculateYOffset(axisalignedbb7, d11);
                }
                axisalignedbb6 = axisalignedbb6.offset(0.0, d11, 0.0);
                double d12 = d4;
                for (final AxisAlignedBB axisalignedbb9 : list2) {
                    d12 = axisalignedbb9.calculateXOffset(axisalignedbb6, d12);
                }
                axisalignedbb6 = axisalignedbb6.offset(d12, 0.0, 0.0);
                double d13 = d6;
                for (final AxisAlignedBB axisalignedbb10 : list2) {
                    d13 = axisalignedbb10.calculateZOffset(axisalignedbb6, d13);
                }
                axisalignedbb6 = axisalignedbb6.offset(0.0, 0.0, d13);
                AxisAlignedBB axisalignedbb11 = this.getEntityBoundingBox();
                double d14 = y;
                for (final AxisAlignedBB axisalignedbb12 : list2) {
                    d14 = axisalignedbb12.calculateYOffset(axisalignedbb11, d14);
                }
                axisalignedbb11 = axisalignedbb11.offset(0.0, d14, 0.0);
                double d15 = d4;
                for (final AxisAlignedBB axisalignedbb13 : list2) {
                    d15 = axisalignedbb13.calculateXOffset(axisalignedbb11, d15);
                }
                axisalignedbb11 = axisalignedbb11.offset(d15, 0.0, 0.0);
                double d16 = d6;
                for (final AxisAlignedBB axisalignedbb14 : list2) {
                    d16 = axisalignedbb14.calculateZOffset(axisalignedbb11, d16);
                }
                axisalignedbb11 = axisalignedbb11.offset(0.0, 0.0, d16);
                final double d17 = d12 * d12 + d13 * d13;
                final double d18 = d15 * d15 + d16 * d16;
                if (d17 > d18) {
                    x = d12;
                    z = d13;
                    y = -d11;
                    this.setEntityBoundingBox(axisalignedbb6);
                } else {
                    x = d15;
                    z = d16;
                    y = -d14;
                    this.setEntityBoundingBox(axisalignedbb11);
                }
                for (final AxisAlignedBB axisalignedbb15 : list2) {
                    y = axisalignedbb15.calculateYOffset(this.getEntityBoundingBox(), y);
                }
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
                if (d8 * d8 + d10 * d10 >= x * x + z * z) {
                    x = d8;
                    y = d9;
                    z = d10;
                    this.setEntityBoundingBox(axisalignedbb5);
                } else {
                    LiquidBounce.CLIENT.eventManager.callEvent(new StepConfirmEvent());
                }
            }
            this.worldObj.theProfiler.endSection();
            this.worldObj.theProfiler.startSection("rest");
            this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
            this.posY = this.getEntityBoundingBox().minY;
            this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
            this.isCollidedHorizontally = (d4 != x || d6 != z);
            this.isCollidedVertically = (d5 != y);
            this.onGround = (this.isCollidedVertically && d5 < 0.0);
            this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);
            final int i = MathHelper.floor_double(this.posX);
            final int j = MathHelper.floor_double(this.posY - 0.20000000298023224);
            final int k = MathHelper.floor_double(this.posZ);
            BlockPos blockpos = new BlockPos(i, j, k);
            Block block1 = this.worldObj.getBlockState(blockpos).getBlock();
            if (block1.getMaterial() == Material.air) {
                final Block block2 = this.worldObj.getBlockState(blockpos.down()).getBlock();
                if (block2 instanceof BlockFence || block2 instanceof BlockWall || block2 instanceof BlockFenceGate) {
                    block1 = block2;
                    blockpos = blockpos.down();
                }
            }
            this.updateFallState(y, this.onGround, block1, blockpos);
            if (d4 != x) {
                this.motionX = 0.0;
            }
            if (d6 != z) {
                this.motionZ = 0.0;
            }
            if (d5 != y) {
                block1.onLanded(this.worldObj, (Entity) this);
            }
            if (this.canTriggerWalking() && !flag && this.ridingEntity == null) {
                final double d19 = this.posX - d0;
                double d20 = this.posY - d2;
                final double d21 = this.posZ - d3;
                if (block1 != Blocks.ladder) {
                    d20 = 0.0;
                }
                if (block1 != null && this.onGround) {
                    block1.onEntityCollidedWithBlock(this.worldObj, blockpos, (Entity) this);
                }
                this.distanceWalkedModified += (float) (MathHelper.sqrt_double(d19 * d19 + d21 * d21) * 0.6);
                this.distanceWalkedOnStepModified += (float) (MathHelper.sqrt_double(d19 * d19 + d20 * d20 + d21 * d21) * 0.6);
                if (this.distanceWalkedOnStepModified > this.getNextStepDistance() && block1.getMaterial() != Material.air) {
                    this.setNextStepDistance((int) this.distanceWalkedOnStepModified + 1);
                    if (this.isInWater()) {
                        float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224 + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224) * 0.35f;
                        if (f > 1.0f) {
                            f = 1.0f;
                        }
                        this.playSound(this.getSwimSound(), f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                    }
                    this.playStepSound(blockpos, block1);
                }
            }
            try {
                this.doBlockCollisions();
            } catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            final boolean flag3 = this.isWet();
            if (this.worldObj.isFlammableWithin(this.getEntityBoundingBox().contract(0.001, 0.001, 0.001))) {
                this.dealFireDamage(1);
                if (!flag3) {
                    this.setFire(this.getFire() + 1);
                    if (this.getFire() == 0) {
                        this.setFire(8);
                    }
                }
            } else if (this.getFire() <= 0) {
                this.setFire(-this.fireResistance);
            }
            if (flag3 && this.getFire() > 0) {
                this.playSound("random.fizz", 0.7f, 1.6f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                this.setFire(-this.fireResistance);
            }
            this.worldObj.theProfiler.endSection();
        }
    }
}
