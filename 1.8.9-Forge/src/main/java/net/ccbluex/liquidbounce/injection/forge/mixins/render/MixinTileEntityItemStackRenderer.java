
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySkull;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ TileEntityItemStackRenderer.class })
public class MixinTileEntityItemStackRenderer
{
    @Shadow
    private TileEntityBanner banner;
    @Shadow
    private TileEntityEnderChest enderChest;
    @Shadow
    private TileEntityChest field_147718_c;
    @Shadow
    private TileEntityChest field_147717_b;
    
    @Overwrite
    public void renderByItem(final ItemStack itemStackIn) {
        if (itemStackIn.getItem() == Items.banner) {
            this.banner.setItemValues(itemStackIn);
            TileEntityRendererDispatcher.instance.renderTileEntityAt((TileEntity)this.banner, 0.0, 0.0, 0.0, 0.0f);
        }
        else if (itemStackIn.getItem() == Items.skull) {
            GameProfile gameprofile = null;
            if (itemStackIn.hasTagCompound()) {
                final NBTTagCompound nbttagcompound = itemStackIn.getTagCompound();
                try {
                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                        gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                    }
                    else if (nbttagcompound.hasKey("SkullOwner", 8) && nbttagcompound.getString("SkullOwner").length() > 0) {
                        final GameProfile lvt_2_2_ = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                        gameprofile = TileEntitySkull.updateGameprofile(lvt_2_2_);
                        nbttagcompound.removeTag("SkullOwner");
                        nbttagcompound.setTag("SkullOwner", (NBTBase)NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                    }
                }
                catch (Exception ex) {}
            }
            if (TileEntitySkullRenderer.instance != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(-0.5f, 0.0f, -0.5f);
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                GlStateManager.disableCull();
                TileEntitySkullRenderer.instance.renderSkull(0.0f, 0.0f, 0.0f, EnumFacing.UP, 0.0f, itemStackIn.getMetadata(), gameprofile, -1);
                GlStateManager.enableCull();
                GlStateManager.popMatrix();
            }
        }
        else {
            final Block block = Block.getBlockFromItem(itemStackIn.getItem());
            if (block == Blocks.ender_chest) {
                TileEntityRendererDispatcher.instance.renderTileEntityAt((TileEntity)this.enderChest, 0.0, 0.0, 0.0, 0.0f);
            }
            else if (block == Blocks.trapped_chest) {
                TileEntityRendererDispatcher.instance.renderTileEntityAt((TileEntity)this.field_147718_c, 0.0, 0.0, 0.0, 0.0f);
            }
            else if (block != Blocks.chest) {
                ForgeHooksClient.renderTileItem(itemStackIn.getItem(), itemStackIn.getMetadata());
            }
            else {
                TileEntityRendererDispatcher.instance.renderTileEntityAt((TileEntity)this.field_147717_b, 0.0, 0.0, 0.0, 0.0f);
            }
        }
    }
}
