//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.minecraft.block.Block;
import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockLiquid;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoFall", description = "Prevents you from taking fall damage.", category = ModuleCategory.PLAYER)
public class NoFall extends Module
{
    private final ListValue modeValue;
    private int state;
    private boolean jumped;
    private final TickTimer spartanTimer;
    
    public NoFall() {
        this.modeValue = new ListValue("Mode", new String[] { "SpoofGround", "NoGround", "Packet", "AAC", "LAAC", "AAC3.3.11", "AAC3.3.15", "Spartan", "CubeCraft" }, "SpoofGround");
        this.spartanTimer = new TickTimer();
    }
    
    @EventTarget(ignoreCondition = true)
    public void onUpdate(final UpdateEvent event) {
        if (NoFall.mc.thePlayer.onGround) {
            this.jumped = false;
        }
        if (NoFall.mc.thePlayer.motionY > 0.0) {
            this.jumped = true;
        }
        if (!this.getState() || ModuleManager.getModule(FreeCam.class).getState()) {
            return;
        }
        if (BlockUtils.collideBlock(NoFall.mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid) || BlockUtils.collideBlock(new AxisAlignedBB(NoFall.mc.thePlayer.getEntityBoundingBox().maxX, NoFall.mc.thePlayer.getEntityBoundingBox().maxY, NoFall.mc.thePlayer.getEntityBoundingBox().maxZ, NoFall.mc.thePlayer.getEntityBoundingBox().minX, NoFall.mc.thePlayer.getEntityBoundingBox().minY - 0.01, NoFall.mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid)) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "packet": {
                if (NoFall.mc.thePlayer.fallDistance > 2.0f) {
                    NoFall.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer(true));
                    break;
                }
                break;
            }
            case "cubecraft": {
                if (NoFall.mc.thePlayer.fallDistance > 2.0f) {
                    NoFall.mc.thePlayer.onGround = false;
                    NoFall.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
                    break;
                }
                break;
            }
            case "aac": {
                if (NoFall.mc.thePlayer.fallDistance > 2.0f) {
                    NoFall.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer(true));
                    this.state = 2;
                }
                else if (this.state == 2 && NoFall.mc.thePlayer.fallDistance < 2.0f) {
                    NoFall.mc.thePlayer.motionY = 0.1;
                    this.state = 3;
                    return;
                }
                switch (this.state) {
                    case 3: {
                        NoFall.mc.thePlayer.motionY = 0.1;
                        this.state = 4;
                        break;
                    }
                    case 4: {
                        NoFall.mc.thePlayer.motionY = 0.1;
                        this.state = 5;
                        break;
                    }
                    case 5: {
                        NoFall.mc.thePlayer.motionY = 0.1;
                        this.state = 1;
                        break;
                    }
                }
                break;
            }
            case "laac": {
                if (!this.jumped && NoFall.mc.thePlayer.onGround && !NoFall.mc.thePlayer.isOnLadder() && !NoFall.mc.thePlayer.isInWater() && !NoFall.mc.thePlayer.isInWeb) {
                    NoFall.mc.thePlayer.motionY = -6.0;
                    break;
                }
                break;
            }
            case "aac3.3.11": {
                if (NoFall.mc.thePlayer.fallDistance > 2.0f) {
                    final EntityPlayerSP thePlayer = NoFall.mc.thePlayer;
                    final EntityPlayerSP thePlayer2 = NoFall.mc.thePlayer;
                    final double n2 = 0.0;
                    thePlayer2.motionZ = n2;
                    thePlayer.motionX = n2;
                    NoFall.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY - 0.001, NoFall.mc.thePlayer.posZ, NoFall.mc.thePlayer.onGround));
                    NoFall.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer(true));
                    break;
                }
                break;
            }
            case "aac3.3.15": {
                if (NoFall.mc.thePlayer.fallDistance > 2.0f) {
                    NoFall.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, Double.NaN, NoFall.mc.thePlayer.posZ, false));
                    NoFall.mc.thePlayer.fallDistance = -9999.0f;
                    break;
                }
                break;
            }
            case "spartan": {
                this.spartanTimer.update();
                if (NoFall.mc.thePlayer.fallDistance > 1.5 && this.spartanTimer.hasTimePassed(10)) {
                    NoFall.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY + 10.0, NoFall.mc.thePlayer.posZ, true));
                    NoFall.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY - 10.0, NoFall.mc.thePlayer.posZ, true));
                    this.spartanTimer.reset();
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        final String mode = this.modeValue.asString();
        if (packet instanceof C03PacketPlayer && mode.equalsIgnoreCase("SpoofGround")) {
            ((C03PacketPlayer)packet).onGround = true;
        }
        if (packet instanceof C03PacketPlayer && mode.equalsIgnoreCase("NoGround")) {
            ((C03PacketPlayer)packet).onGround = false;
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (BlockUtils.collideBlock(NoFall.mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid) || BlockUtils.collideBlock(new AxisAlignedBB(NoFall.mc.thePlayer.getEntityBoundingBox().maxX, NoFall.mc.thePlayer.getEntityBoundingBox().maxY, NoFall.mc.thePlayer.getEntityBoundingBox().maxZ, NoFall.mc.thePlayer.getEntityBoundingBox().minX, NoFall.mc.thePlayer.getEntityBoundingBox().minY - 0.01, NoFall.mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid)) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "laac": {
                if (!this.jumped && !NoFall.mc.thePlayer.onGround && !NoFall.mc.thePlayer.isOnLadder() && !NoFall.mc.thePlayer.isInWater() && !NoFall.mc.thePlayer.isInWeb && NoFall.mc.thePlayer.motionY < 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget(ignoreCondition = true)
    public void onJump(final JumpEvent event) {
        this.jumped = true;
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
