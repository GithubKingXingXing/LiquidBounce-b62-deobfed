//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class MineplexGround extends SpeedMode
{
    private boolean spoofSlot;
    private float speed;
    
    public MineplexGround() {
        super("MineplexGround");
        this.speed = 0.0f;
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving() || !MineplexGround.mc.thePlayer.onGround || MineplexGround.mc.thePlayer.inventory.getCurrentItem() == null || MineplexGround.mc.thePlayer.isUsingItem()) {
            return;
        }
        this.spoofSlot = false;
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = MineplexGround.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null) {
                MineplexGround.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(i - 36));
                this.spoofSlot = true;
                break;
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving() || !MineplexGround.mc.thePlayer.onGround || MineplexGround.mc.thePlayer.isUsingItem()) {
            this.speed = 0.0f;
            return;
        }
        if (!this.spoofSlot && MineplexGround.mc.thePlayer.inventory.getCurrentItem() != null) {
            ChatUtils.displayChatMessage("§8[§c§lMineplex§aSpeed§8] §cYou need one empty slot.");
            return;
        }
        final BlockPos blockPos = new BlockPos(MineplexGround.mc.thePlayer.posX, MineplexGround.mc.thePlayer.getEntityBoundingBox().minY - 1.0, MineplexGround.mc.thePlayer.posZ);
        final Vec3 vec = new Vec3((Vec3i)blockPos).addVector(0.4000000059604645, 0.4000000059604645, 0.4000000059604645).add(new Vec3(EnumFacing.UP.getDirectionVec()));
        MineplexGround.mc.playerController.onPlayerRightClick(MineplexGround.mc.thePlayer, MineplexGround.mc.theWorld, (ItemStack)null, blockPos, EnumFacing.UP, new Vec3(vec.xCoord * 0.4000000059604645, vec.yCoord * 0.4000000059604645, vec.zCoord * 0.4000000059604645));
        final float targetSpeed = ((Speed)ModuleManager.getModule(Speed.class)).mineplexGroundSpeedValue.asFloat();
        if (targetSpeed > this.speed) {
            this.speed += targetSpeed / 8.0f;
        }
        if (this.speed >= targetSpeed) {
            this.speed = targetSpeed;
        }
        MovementUtils.strafe(this.speed);
        if (!this.spoofSlot) {
            MineplexGround.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(MineplexGround.mc.thePlayer.inventory.currentItem));
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onDisable() {
        this.speed = 0.0f;
        MineplexGround.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(MineplexGround.mc.thePlayer.inventory.currentItem));
    }
}
