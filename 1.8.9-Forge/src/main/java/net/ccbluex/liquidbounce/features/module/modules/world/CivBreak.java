//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.block.material.Material;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.init.Blocks;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.event.events.ClickBlockEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "CivBreak", description = "Allows you to break blocks instantly.", category = ModuleCategory.WORLD)
public class CivBreak extends Module
{
    private BlockPos blockPos;
    private EnumFacing enumFacing;
    private final BoolValue airResetValue;
    private final BoolValue rangeResetValue;
    private final BoolValue rotationsValue;
    
    public CivBreak() {
        this.airResetValue = new BoolValue("Air-Reset", true);
        this.rangeResetValue = new BoolValue("Range-Reset", true);
        this.rotationsValue = new BoolValue("Rotations", true);
    }
    
    @EventTarget
    public void onBlockClick(final ClickBlockEvent event) {
        if (BlockUtils.getBlock(event.getClickedBlock()) == Blocks.bedrock) {
            return;
        }
        this.blockPos = event.getClickedBlock();
        this.enumFacing = event.getEnumFacing();
        CivBreak.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, this.enumFacing));
        CivBreak.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.enumFacing));
    }
    
    @EventTarget
    public void onUpdate(final MotionEvent event) {
        if (event.getEventState() == EventState.POST && this.blockPos != null && CivBreak.mc.theWorld.getBlockState(this.blockPos).getBlock().getMaterial() != Material.air && CivBreak.mc.thePlayer.getDistanceSq(this.blockPos) < 22.399999618530273) {
            CivBreak.mc.thePlayer.swingItem();
            if (this.rotationsValue.asBoolean()) {
                RotationUtils.faceBlockPacket(this.blockPos);
            }
            CivBreak.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, this.enumFacing));
            CivBreak.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.enumFacing));
            CivBreak.mc.playerController.clickBlock(this.blockPos, this.enumFacing);
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (this.blockPos != null) {
            if ((this.airResetValue.asBoolean() && CivBreak.mc.theWorld.getBlockState(this.blockPos).getBlock().getMaterial() == Material.air) || (this.rangeResetValue.asBoolean() && CivBreak.mc.thePlayer.getDistanceSq(this.blockPos) >= 22.399999618530273)) {
                this.blockPos = null;
                return;
            }
            RenderUtils.drawBlockBox(this.blockPos, Color.RED, true);
        }
    }
}
