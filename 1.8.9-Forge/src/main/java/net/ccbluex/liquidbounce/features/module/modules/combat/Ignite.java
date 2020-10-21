//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemBucket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.block.BlockAir;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.minecraft.init.Items;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Ignite", description = "Automatically sets targets aroung you on fire.", category = ModuleCategory.COMBAT)
public class Ignite extends Module
{
    private final BoolValue lighterValue;
    private final BoolValue lavaBucketValue;
    private final MSTimer msTimer;
    
    public Ignite() {
        this.lighterValue = new BoolValue("Lighter", true);
        this.lavaBucketValue = new BoolValue("Lava", true);
        this.msTimer = new MSTimer();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!this.msTimer.hasTimePassed(500L)) {
            return;
        }
        final int lighterInHotbar = this.lighterValue.asBoolean() ? InventoryUtils.findItem(36, 45, Items.flint_and_steel) : -1;
        final int lavaInHotbar = this.lavaBucketValue.asBoolean() ? InventoryUtils.findItem(26, 45, Items.lava_bucket) : -1;
        if (lighterInHotbar == -1 && lavaInHotbar == -1) {
            return;
        }
        final int fireInHotbar = (lighterInHotbar != -1) ? lighterInHotbar : lavaInHotbar;
        for (final Entity entity : Ignite.mc.theWorld.loadedEntityList) {
            if (EntityUtils.isSelected(entity, true) && !entity.isBurning()) {
                final BlockPos blockPos = entity.getPosition();
                if (Ignite.mc.thePlayer.getDistanceSq(blockPos) >= 22.3 || !BlockUtils.isReplaceable(blockPos)) {
                    continue;
                }
                if (!(BlockUtils.getBlock(blockPos) instanceof BlockAir)) {
                    continue;
                }
                RotationUtils.keepRotation = true;
                Ignite.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(fireInHotbar - 36));
                final ItemStack itemStack = Ignite.mc.thePlayer.inventoryContainer.getSlot(fireInHotbar).getStack();
                if (itemStack.getItem() instanceof ItemBucket) {
                    final double diffX = blockPos.getX() + 0.5 - Ignite.mc.thePlayer.posX;
                    final double diffY = blockPos.getY() + 0.5 - (Ignite.mc.thePlayer.getEntityBoundingBox().minY + Ignite.mc.thePlayer.getEyeHeight());
                    final double diffZ = blockPos.getZ() + 0.5 - Ignite.mc.thePlayer.posZ;
                    final double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
                    final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
                    final float pitch = (float)(-(Math.atan2(diffY, sqrt) * 180.0 / 3.141592653589793));
                    Ignite.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(Ignite.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Ignite.mc.thePlayer.rotationYaw), Ignite.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Ignite.mc.thePlayer.rotationPitch), Ignite.mc.thePlayer.onGround));
                    Ignite.mc.playerController.sendUseItem((EntityPlayer)Ignite.mc.thePlayer, (World)Ignite.mc.theWorld, itemStack);
                }
                else {
                    for (final EnumFacing side : EnumFacing.values()) {
                        final BlockPos neighbor = blockPos.offset(side);
                        if (BlockUtils.canBeClicked(neighbor)) {
                            final double diffX2 = neighbor.getX() + 0.5 - Ignite.mc.thePlayer.posX;
                            final double diffY2 = neighbor.getY() + 0.5 - (Ignite.mc.thePlayer.getEntityBoundingBox().minY + Ignite.mc.thePlayer.getEyeHeight());
                            final double diffZ2 = neighbor.getZ() + 0.5 - Ignite.mc.thePlayer.posZ;
                            final double sqrt2 = Math.sqrt(diffX2 * diffX2 + diffZ2 * diffZ2);
                            final float yaw2 = (float)(Math.atan2(diffZ2, diffX2) * 180.0 / 3.141592653589793) - 90.0f;
                            final float pitch2 = (float)(-(Math.atan2(diffY2, sqrt2) * 180.0 / 3.141592653589793));
                            Ignite.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(Ignite.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw2 - Ignite.mc.thePlayer.rotationYaw), Ignite.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch2 - Ignite.mc.thePlayer.rotationPitch), Ignite.mc.thePlayer.onGround));
                            if (Ignite.mc.playerController.onPlayerRightClick(Ignite.mc.thePlayer, Ignite.mc.theWorld, itemStack, neighbor, side.getOpposite(), new Vec3(side.getDirectionVec()))) {
                                Ignite.mc.thePlayer.swingItem();
                                break;
                            }
                        }
                    }
                }
                Ignite.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(Ignite.mc.thePlayer.inventory.currentItem));
                RotationUtils.keepRotation = false;
                Ignite.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(Ignite.mc.thePlayer.rotationYaw, Ignite.mc.thePlayer.rotationPitch, Ignite.mc.thePlayer.onGround));
                this.msTimer.reset();
                break;
            }
        }
    }
}
