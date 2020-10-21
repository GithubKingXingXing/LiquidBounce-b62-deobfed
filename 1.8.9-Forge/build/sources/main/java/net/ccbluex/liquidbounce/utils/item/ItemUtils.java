
package net.ccbluex.liquidbounce.utils.item;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.JsonToNBT;
import java.util.Objects;
import net.minecraft.util.ResourceLocation;
import java.util.regex.Pattern;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ItemUtils
{
    public static ItemStack createItem(String itemArguments) {
        try {
            itemArguments = itemArguments.replace('&', '§');
            Item item = new Item();
            String[] args = null;
            int i = 1;
            int j = 0;
            for (int mode = 0; mode <= Math.min(12, itemArguments.length() - 2); ++mode) {
                args = itemArguments.substring(mode).split(Pattern.quote(" "));
                final ResourceLocation resourcelocation = new ResourceLocation(args[0]);
                item = (Item)Item.itemRegistry.getObject((Object)resourcelocation);
                if (item != null) {
                    break;
                }
            }
            if (item == null) {
                return null;
            }
            if (Objects.requireNonNull(args).length >= 2 && args[1].matches("\\d+")) {
                i = Integer.parseInt(args[1]);
            }
            if (args.length >= 3 && args[2].matches("\\d+")) {
                j = Integer.parseInt(args[2]);
            }
            final ItemStack itemstack = new ItemStack(item, i, j);
            if (args.length >= 4) {
                String NBT = "";
                for (int nbtcount = 3; nbtcount < args.length; ++nbtcount) {
                    NBT = String.valueOf(NBT) + " " + args[nbtcount];
                }
                itemstack.setTagCompound(JsonToNBT.getTagFromJson(NBT));
            }
            return itemstack;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
    
    public static int getEnchantment(final ItemStack itemStack, final Enchantment enchantment) {
        if (itemStack == null || itemStack.getEnchantmentTagList() == null || itemStack.getEnchantmentTagList().hasNoTags()) {
            return 0;
        }
        for (int i = 0; i < itemStack.getEnchantmentTagList().tagCount(); ++i) {
            final NBTTagCompound tagCompound = itemStack.getEnchantmentTagList().getCompoundTagAt(i);
            if (tagCompound.getShort("ench") == enchantment.effectId || tagCompound.getShort("id") == enchantment.effectId) {
                return tagCompound.getShort("lvl");
            }
        }
        return 0;
    }
}
