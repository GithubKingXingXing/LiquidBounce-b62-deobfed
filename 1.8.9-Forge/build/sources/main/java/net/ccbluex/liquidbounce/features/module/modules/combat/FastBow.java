//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemBow;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FastBow", description = "Turns your bow into a machine gun.", category = ModuleCategory.COMBAT)
public class FastBow extends Module
{
    private final IntegerValue packetsValue;
    
    public FastBow() {
        this.packetsValue = new IntegerValue("Packets", 20, 3, 20);
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!FastBow.mc.thePlayer.isUsingItem()) {
            return;
        }
        if (FastBow.mc.thePlayer.inventory.getCurrentItem() != null && FastBow.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow) {
            FastBow.mc.getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, FastBow.mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
            final float yaw = RotationUtils.lookChanged ? RotationUtils.targetYaw : FastBow.mc.thePlayer.rotationYaw;
            final float pitch = RotationUtils.lookChanged ? RotationUtils.targetPitch : FastBow.mc.thePlayer.rotationPitch;
            for (int i = 0; i < this.packetsValue.asInteger(); ++i) {
                FastBow.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, true));
            }
            FastBow.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            FastBow.mc.thePlayer.itemInUseCount = FastBow.mc.thePlayer.inventory.getCurrentItem().getMaxItemUseDuration() - 1;
        }
    }
}
