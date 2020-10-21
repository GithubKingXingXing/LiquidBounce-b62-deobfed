
package net.ccbluex.liquidbounce.utils.item;

import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;

public final class Armor
{
    private static final int[] HELMET;
    private static final int[] CHESTPLATE;
    private static final int[] LEGGINGS;
    private static final int[] BOOTS;
    
    public static int[] getArmorArray(final int type) {
        switch (type) {
            case 0: {
                return Armor.BOOTS;
            }
            case 1: {
                return Armor.LEGGINGS;
            }
            case 2: {
                return Armor.CHESTPLATE;
            }
            case 3: {
                return Armor.HELMET;
            }
            default: {
                return null;
            }
        }
    }
    
    public static int[] getArmorArray(final ItemArmor itemArmor) {
        switch (itemArmor.armorType) {
            case 3: {
                return Armor.BOOTS;
            }
            case 2: {
                return Armor.LEGGINGS;
            }
            case 1: {
                return Armor.CHESTPLATE;
            }
            case 0: {
                return Armor.HELMET;
            }
            default: {
                return null;
            }
        }
    }
    
    public static int getArmorSlot(final int type) {
        switch (type) {
            case 0: {
                return 8;
            }
            case 1: {
                return 7;
            }
            case 2: {
                return 6;
            }
            case 3: {
                return 5;
            }
            default: {
                return -1;
            }
        }
    }
    
    static {
        HELMET = new int[] { Item.getIdFromItem((Item)Items.diamond_helmet), Item.getIdFromItem((Item)Items.iron_helmet), Item.getIdFromItem((Item)Items.chainmail_helmet), Item.getIdFromItem((Item)Items.golden_helmet), Item.getIdFromItem((Item)Items.leather_helmet) };
        CHESTPLATE = new int[] { Item.getIdFromItem((Item)Items.diamond_chestplate), Item.getIdFromItem((Item)Items.iron_chestplate), Item.getIdFromItem((Item)Items.chainmail_chestplate), Item.getIdFromItem((Item)Items.golden_chestplate), Item.getIdFromItem((Item)Items.leather_chestplate) };
        LEGGINGS = new int[] { Item.getIdFromItem((Item)Items.diamond_leggings), Item.getIdFromItem((Item)Items.iron_leggings), Item.getIdFromItem((Item)Items.chainmail_leggings), Item.getIdFromItem((Item)Items.golden_leggings), Item.getIdFromItem((Item)Items.leather_leggings) };
        BOOTS = new int[] { Item.getIdFromItem((Item)Items.diamond_boots), Item.getIdFromItem((Item)Items.iron_boots), Item.getIdFromItem((Item)Items.chainmail_boots), Item.getIdFromItem((Item)Items.golden_boots), Item.getIdFromItem((Item)Items.leather_boots) };
    }
}
