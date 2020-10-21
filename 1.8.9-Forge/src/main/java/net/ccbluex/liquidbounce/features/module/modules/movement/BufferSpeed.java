
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.BlockAir;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockSlab;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockSlime;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "BufferSpeed", description = "Allows you to walk faster on slabs and stairs.", category = ModuleCategory.MOVEMENT)
public class BufferSpeed extends Module
{
    private final BoolValue speedLimitValue;
    private final FloatValue maxSpeedValue;
    private final BoolValue bufferValue;
    private final BoolValue stairsValue;
    private final FloatValue stairsBoostValue;
    private final ListValue stairsModeValue;
    private final BoolValue slabsValue;
    private final FloatValue slabsBoostValue;
    private final ListValue slabsModeValue;
    private final BoolValue iceValue;
    private final FloatValue iceBoostValue;
    private final BoolValue snowValue;
    private final FloatValue snowBoostValue;
    private final BoolValue snowPortValue;
    private final BoolValue wallValue;
    private final FloatValue wallBoostValue;
    private final ListValue wallModeValue;
    private final BoolValue headBlockValue;
    private final FloatValue headBlockBoostValue;
    private final BoolValue slimeValue;
    private final BoolValue airStrafeValue;
    private final BoolValue noHurtValue;
    private double speed;
    private boolean down;
    private boolean forceDown;
    private boolean fastHop;
    private boolean hadFastHop;
    private boolean legitHop;
    
    public BufferSpeed() {
        this.speedLimitValue = new BoolValue("SpeedLimit", true);
        this.maxSpeedValue = new FloatValue("MaxSpeed", 2.0f, 1.0f, 5.0f);
        this.bufferValue = new BoolValue("Buffer", true);
        this.stairsValue = new BoolValue("Stairs", true);
        this.stairsBoostValue = new FloatValue("StairsBoost", 1.87f, 1.0f, 2.0f);
        this.stairsModeValue = new ListValue("StairsMode", new String[] { "Old", "New" }, "New");
        this.slabsValue = new BoolValue("Slabs", true);
        this.slabsBoostValue = new FloatValue("SlabsBoost", 1.87f, 1.0f, 2.0f);
        this.slabsModeValue = new ListValue("SlabsMode", new String[] { "Old", "New" }, "New");
        this.iceValue = new BoolValue("Ice", false);
        this.iceBoostValue = new FloatValue("IceBoost", 1.342f, 1.0f, 2.0f);
        this.snowValue = new BoolValue("Snow", true);
        this.snowBoostValue = new FloatValue("SnowBoost", 1.87f, 1.0f, 2.0f);
        this.snowPortValue = new BoolValue("SnowPort", true);
        this.wallValue = new BoolValue("Wall", true);
        this.wallBoostValue = new FloatValue("WallBoost", 1.87f, 1.0f, 2.0f);
        this.wallModeValue = new ListValue("WallMode", new String[] { "Old", "New" }, "New");
        this.headBlockValue = new BoolValue("HeadBlock", true);
        this.headBlockBoostValue = new FloatValue("HeadBlockBoost", 1.87f, 1.0f, 2.0f);
        this.slimeValue = new BoolValue("Slime", true);
        this.airStrafeValue = new BoolValue("AirStrafe", false);
        this.noHurtValue = new BoolValue("NoHurt", true);
        this.speed = 0.0;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (ModuleManager.getModule(Speed.class).getState() || (this.noHurtValue.asBoolean() && BufferSpeed.mc.thePlayer.hurtTime > 0)) {
            this.reset();
            return;
        }
        final BlockPos blockPos = new BlockPos(BufferSpeed.mc.thePlayer.posX, BufferSpeed.mc.thePlayer.getEntityBoundingBox().minY, BufferSpeed.mc.thePlayer.posZ);
        if (this.forceDown || (this.down && BufferSpeed.mc.thePlayer.motionY == 0.0)) {
            BufferSpeed.mc.thePlayer.motionY = -1.0;
            this.down = false;
            this.forceDown = false;
        }
        if (this.fastHop) {
            BufferSpeed.mc.thePlayer.speedInAir = 0.0211f;
            this.hadFastHop = true;
        }
        else if (this.hadFastHop) {
            BufferSpeed.mc.thePlayer.speedInAir = 0.02f;
            this.hadFastHop = false;
        }
        if (!MovementUtils.isMoving() || BufferSpeed.mc.thePlayer.isSneaking() || BufferSpeed.mc.thePlayer.isInWater()) {
            this.reset();
            return;
        }
        if (BufferSpeed.mc.thePlayer.onGround) {
            this.fastHop = false;
            if (this.slimeValue.asBoolean() && (BlockUtils.getBlock(blockPos.down()) instanceof BlockSlime || BlockUtils.getBlock(blockPos) instanceof BlockSlime)) {
                BufferSpeed.mc.thePlayer.jump();
                BufferSpeed.mc.thePlayer.motionY = 0.08;
                final EntityPlayerSP thePlayer = BufferSpeed.mc.thePlayer;
                thePlayer.motionX *= 1.132;
                final EntityPlayerSP thePlayer2 = BufferSpeed.mc.thePlayer;
                thePlayer2.motionZ *= 1.132;
                this.down = true;
                return;
            }
            if (this.slabsValue.asBoolean() && BlockUtils.getBlock(blockPos) instanceof BlockSlab) {
                final String lowerCase = this.slabsModeValue.asString().toLowerCase();
                switch (lowerCase) {
                    case "old": {
                        this.boost(this.slabsBoostValue.asFloat());
                        return;
                    }
                    case "new": {
                        this.fastHop = true;
                        if (this.legitHop) {
                            BufferSpeed.mc.thePlayer.jump();
                            BufferSpeed.mc.thePlayer.onGround = false;
                            this.legitHop = false;
                            return;
                        }
                        BufferSpeed.mc.thePlayer.onGround = false;
                        MovementUtils.strafe(0.375f);
                        BufferSpeed.mc.thePlayer.jump();
                        BufferSpeed.mc.thePlayer.motionY = 0.41;
                        return;
                    }
                }
            }
            if (this.stairsValue.asBoolean() && (BlockUtils.getBlock(blockPos.down()) instanceof BlockStairs || BlockUtils.getBlock(blockPos) instanceof BlockStairs)) {
                final String lowerCase2 = this.stairsModeValue.asString().toLowerCase();
                switch (lowerCase2) {
                    case "old": {
                        this.boost(this.stairsBoostValue.asFloat());
                        return;
                    }
                    case "new": {
                        this.fastHop = true;
                        if (this.legitHop) {
                            BufferSpeed.mc.thePlayer.jump();
                            BufferSpeed.mc.thePlayer.onGround = false;
                            this.legitHop = false;
                            return;
                        }
                        BufferSpeed.mc.thePlayer.onGround = false;
                        MovementUtils.strafe(0.375f);
                        BufferSpeed.mc.thePlayer.jump();
                        BufferSpeed.mc.thePlayer.motionY = 0.41;
                        return;
                    }
                }
            }
            this.legitHop = true;
            if (this.headBlockValue.asBoolean() && BlockUtils.getBlock(blockPos.up(2)) != Blocks.air) {
                this.boost(this.headBlockBoostValue.asFloat());
                return;
            }
            if (this.iceValue.asBoolean() && (BlockUtils.getBlock(blockPos.down()) == Blocks.ice || BlockUtils.getBlock(blockPos.down()) == Blocks.packed_ice)) {
                this.boost(this.iceBoostValue.asFloat());
                return;
            }
            if (this.snowValue.asBoolean() && BlockUtils.getBlock(blockPos) == Blocks.snow_layer && (this.snowPortValue.asBoolean() || BufferSpeed.mc.thePlayer.posY - (int)BufferSpeed.mc.thePlayer.posY >= 0.125)) {
                if (BufferSpeed.mc.thePlayer.posY - (int)BufferSpeed.mc.thePlayer.posY >= 0.125) {
                    this.boost(this.snowBoostValue.asFloat());
                }
                else {
                    BufferSpeed.mc.thePlayer.jump();
                    this.forceDown = true;
                }
                return;
            }
            if (this.wallValue.asBoolean()) {
                final String lowerCase3 = this.wallModeValue.asString().toLowerCase();
                switch (lowerCase3) {
                    case "old": {
                        if ((BufferSpeed.mc.thePlayer.isCollidedHorizontally && EntityUtils.isNearBlock()) || !(BlockUtils.getBlock(new BlockPos(BufferSpeed.mc.thePlayer.posX, BufferSpeed.mc.thePlayer.posY + 2.0, BufferSpeed.mc.thePlayer.posZ)) instanceof BlockAir)) {
                            this.boost(this.wallBoostValue.asFloat());
                            return;
                        }
                        break;
                    }
                    case "new": {
                        if (EntityUtils.isNearBlock() && !BufferSpeed.mc.thePlayer.movementInput.jump) {
                            BufferSpeed.mc.thePlayer.jump();
                            BufferSpeed.mc.thePlayer.motionY = 0.08;
                            final EntityPlayerSP thePlayer3 = BufferSpeed.mc.thePlayer;
                            thePlayer3.motionX *= 0.99;
                            final EntityPlayerSP thePlayer4 = BufferSpeed.mc.thePlayer;
                            thePlayer4.motionZ *= 0.99;
                            this.down = true;
                            return;
                        }
                        break;
                    }
                }
            }
            final float currentSpeed = MovementUtils.getSpeed();
            if (this.speed < currentSpeed) {
                this.speed = currentSpeed;
            }
            if (this.bufferValue.asBoolean() && this.speed > 0.20000000298023224) {
                this.speed /= 1.0199999809265137;
                MovementUtils.strafe((float)this.speed);
            }
        }
        else {
            this.speed = 0.0;
            if (this.airStrafeValue.asBoolean()) {
                MovementUtils.strafe();
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.reset();
    }
    
    @Override
    public void onDisable() {
        this.reset();
    }
    
    private void reset() {
        if (BufferSpeed.mc.thePlayer == null) {
            return;
        }
        this.legitHop = true;
        this.speed = 0.0;
        if (this.hadFastHop) {
            BufferSpeed.mc.thePlayer.speedInAir = 0.02f;
            this.hadFastHop = false;
        }
    }
    
    private void boost(final float boost) {
        final EntityPlayerSP thePlayer = BufferSpeed.mc.thePlayer;
        thePlayer.motionX *= boost;
        final EntityPlayerSP thePlayer2 = BufferSpeed.mc.thePlayer;
        thePlayer2.motionZ *= boost;
        this.speed = MovementUtils.getSpeed();
        if (this.speedLimitValue.asBoolean() && this.speed > this.maxSpeedValue.asFloat()) {
            this.speed = this.maxSpeedValue.asFloat();
        }
    }
}
