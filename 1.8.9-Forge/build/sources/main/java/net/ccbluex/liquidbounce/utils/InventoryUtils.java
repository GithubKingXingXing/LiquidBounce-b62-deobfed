//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

public final class InventoryUtils
{
    public static int findItem(final int startSlot, final int endSlot, final Item item) {
        for (int i = startSlot; i < endSlot; ++i) {
            final ItemStack stack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean hasSpaceHotbar() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null) {
                return true;
            }
        }
        return false;
    }
}
