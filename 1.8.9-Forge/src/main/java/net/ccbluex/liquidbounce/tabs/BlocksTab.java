//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.tabs;

import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;

public final class BlocksTab extends CreativeTabs
{
    public BlocksTab() {
        super("Special blocks");
        this.setBackgroundImageName("item_search.png");
    }
    
    public void displayAllReleventItems(final List<ItemStack> itemList) {
        itemList.add(new ItemStack(Blocks.command_block));
        itemList.add(new ItemStack(Items.command_block_minecart));
        itemList.add(new ItemStack(Blocks.barrier));
        itemList.add(new ItemStack(Blocks.dragon_egg));
        itemList.add(new ItemStack(Blocks.brown_mushroom_block));
        itemList.add(new ItemStack(Blocks.red_mushroom_block));
        itemList.add(new ItemStack(Blocks.farmland));
        itemList.add(new ItemStack(Blocks.mob_spawner));
        itemList.add(new ItemStack(Blocks.lit_furnace));
    }
    
    public Item getTabIconItem() {
        return new ItemStack(Blocks.command_block).getItem();
    }
    
    public String getTranslatedTabLabel() {
        return "Special blocks";
    }
    
    public boolean hasSearchBar() {
        return true;
    }
}
