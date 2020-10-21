
package net.ccbluex.liquidbounce.features.module.modules.player;

import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FarmKingBot", description = "Allows you to play FarmKing while being AFK.", category = ModuleCategory.PLAYER)
public class FarmKingBot extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        BlockPos targetBlockPos = null;
        for (final Entity entity : FarmKingBot.mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityItemFrame) {
                final EntityItemFrame entityItemFrame = (EntityItemFrame)entity;
                if (entityItemFrame.getDisplayedItem() == null || entityItemFrame.getDisplayedItem().getItem() == null) {
                    continue;
                }
                final ItemStack itemStack = entityItemFrame.getDisplayedItem();
                final Item item = itemStack.getItem();
                if (!item.getUnlocalizedName().equals("tile.cloth") || itemStack.getItemDamage() != 5) {
                    continue;
                }
                targetBlockPos = entity.getPosition();
                if (FarmKingBot.mc.thePlayer.getDistanceToEntity(entity) > 21.399999618530273) {
                    continue;
                }
                FarmKingBot.mc.getNetHandler().addToSendQueue((Packet)new C02PacketUseEntity(entity, C02PacketUseEntity.Action.INTERACT));
            }
        }
        if (targetBlockPos == null) {
            for (int radius = 40, x = -radius; x < radius; ++x) {
                for (int y = radius; y > -radius; --y) {
                    int z = -radius;
                    while (z < radius) {
                        final int xPos = (int)FarmKingBot.mc.thePlayer.posX + x;
                        final int yPos = (int)FarmKingBot.mc.thePlayer.posY + y;
                        final int zPos = (int)FarmKingBot.mc.thePlayer.posZ + z;
                        final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                        final Block block = FarmKingBot.mc.theWorld.getBlockState(blockPos).getBlock();
                        if (block instanceof BlockTallGrass) {
                            targetBlockPos = blockPos;
                            if (FarmKingBot.mc.thePlayer.getDistanceSq(blockPos) <= 21.399999618530273) {
                                FarmKingBot.mc.thePlayer.swingItem();
                                FarmKingBot.mc.playerController.clickBlock(blockPos, EnumFacing.DOWN);
                                break;
                            }
                            break;
                        }
                        else {
                            ++z;
                        }
                    }
                    if (targetBlockPos != null) {
                        break;
                    }
                }
                if (targetBlockPos != null) {
                    break;
                }
            }
        }
        if (targetBlockPos != null) {
            final double diffX = targetBlockPos.getX() + 0.5 - FarmKingBot.mc.thePlayer.posX;
            final double diffY = targetBlockPos.getY() + 0.5 - (FarmKingBot.mc.thePlayer.getEntityBoundingBox().minY + FarmKingBot.mc.thePlayer.getEyeHeight());
            final double diffZ = targetBlockPos.getZ() + 0.5 - FarmKingBot.mc.thePlayer.posZ;
            final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
            final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
            final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
            FarmKingBot.mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - FarmKingBot.mc.thePlayer.rotationYaw);
            FarmKingBot.mc.thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - FarmKingBot.mc.thePlayer.rotationPitch);
            if (FarmKingBot.mc.thePlayer.isCollidedHorizontally && FarmKingBot.mc.thePlayer.onGround) {
                FarmKingBot.mc.thePlayer.jump();
            }
            FarmKingBot.mc.gameSettings.keyBindForward.pressed = true;
        }
        else {
            FarmKingBot.mc.gameSettings.keyBindForward.pressed = false;
        }
    }
    
    @Override
    public void onDisable() {
        if (!Keyboard.isKeyDown(FarmKingBot.mc.gameSettings.keyBindForward.getKeyCode())) {
            FarmKingBot.mc.gameSettings.keyBindForward.pressed = false;
        }
        super.onDisable();
    }
}
