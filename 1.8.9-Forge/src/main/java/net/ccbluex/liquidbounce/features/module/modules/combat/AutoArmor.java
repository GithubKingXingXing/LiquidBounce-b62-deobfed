
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.minecraft.item.ItemStack;
import net.ccbluex.liquidbounce.injection.implementations.IItemStack;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.item.Armor;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoArmor", description = "Automatically equips the best armor in your inventory.", category = ModuleCategory.COMBAT)
public class AutoArmor extends Module
{
    private final IntegerValue maxDelayValue;
    private final IntegerValue minDelayValue;
    private final BoolValue invOpenValue;
    private final BoolValue simulateInventory;
    private final BoolValue noMoveValue;
    private final IntegerValue itemDelayValue;
    private final BoolValue hotbarValue;
    private final MSTimer msTimer;
    private long delay;
    
    public AutoArmor() {
        this.maxDelayValue = new IntegerValue("MaxDelay", 200, 0, 400) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int minCPS = AutoArmor.this.minDelayValue.asInteger();
                if (minCPS > Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(minCPS);
                }
            }
        };
        this.minDelayValue = new IntegerValue("MinDelay", 100, 0, 400) {
            @Override
            protected void onChanged(final Object oldValue, final Object newValue) {
                final int maxDelay = AutoArmor.this.maxDelayValue.asInteger();
                if (maxDelay < Integer.parseInt(String.valueOf(newValue))) {
                    this.setValue(maxDelay);
                }
            }
        };
        this.invOpenValue = new BoolValue("InvOpen", false);
        this.simulateInventory = new BoolValue("SimulateInventory", true);
        this.noMoveValue = new BoolValue("NoMove", false);
        this.itemDelayValue = new IntegerValue("ItemDelay", 0, 0, 5000);
        this.hotbarValue = new BoolValue("Hotbar", true);
        this.msTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("autoarmor", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("maxdelay")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (AutoArmor.this.minDelayValue.asInteger() > value) {
                                    this.chat("MinDelay can't higher as MaxDelay!");
                                    return;
                                }
                                AutoArmor.this.maxDelayValue.setValue(value);
                                AutoArmor.this.delay = TimeUtils.randomDelay(AutoArmor.this.minDelayValue.asInteger(), AutoArmor.this.maxDelayValue.asInteger());
                                this.chat("§7AutoArmor maxDelay was set to §8" + value + "§7.");
                                AutoArmor$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".autoarmor maxdelay <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("mindelay")) {
                        if (args.length > 2) {
                            try {
                                final int value = Integer.parseInt(args[2]);
                                if (AutoArmor.this.maxDelayValue.asInteger() < value) {
                                    this.chat("MinDelay can't higher as MaxDelay!");
                                    return;
                                }
                                AutoArmor.this.minDelayValue.setValue(value);
                                AutoArmor.this.delay = TimeUtils.randomDelay(AutoArmor.this.minDelayValue.asInteger(), AutoArmor.this.maxDelayValue.asInteger());
                                this.chat("§7AutoArmor minDelay was set to §8" + value + "§7.");
                                AutoArmor$3.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".autoarmor mindelay <value>");
                        return;
                    }
                }
                this.chatSyntax(".autoarmor <maxdelay, mindelay>");
            }
        });
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (!this.msTimer.hasTimePassed(this.delay) || (AutoArmor.mc.thePlayer.openContainer != null && AutoArmor.mc.thePlayer.openContainer.windowId != 0)) {
            return;
        }
        int item = -1;
        int hotbarItem = -1;
        for (int slot = 0; slot <= 3; ++slot) {
            final int[] idArray = Armor.getArmorArray(slot);
            if (AutoArmor.mc.thePlayer.inventory.armorInventory[slot] == null) {
                item = this.getArmor(idArray, 9, 45);
                hotbarItem = this.getArmor(idArray, 36, 45);
            }
            else if (this.hasBetter(slot, idArray)) {
                item = Armor.getArmorSlot(slot);
            }
            if (item != -1) {
                break;
            }
        }
        if ((!this.noMoveValue.asBoolean() || !MovementUtils.isMoving()) && (!this.invOpenValue.asBoolean() || AutoArmor.mc.currentScreen instanceof GuiInventory) && item != -1) {
            final boolean openInventory = this.simulateInventory.asBoolean() && !(AutoArmor.mc.currentScreen instanceof GuiInventory);
            if (openInventory) {
                AutoArmor.mc.getNetHandler().addToSendQueue((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            }
            AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, item, 0, 1, (EntityPlayer)AutoArmor.mc.thePlayer);
            this.msTimer.reset();
            this.delay = TimeUtils.randomDelay((int)this.minDelayValue.asObject(), (int)this.maxDelayValue.asObject());
            if (openInventory) {
                AutoArmor.mc.getNetHandler().addToSendQueue((Packet)new C0DPacketCloseWindow());
            }
        }
        else if (this.hotbarValue.asBoolean() && hotbarItem != -1) {
            AutoArmor.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(hotbarItem - 36));
            AutoArmor.mc.getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(AutoArmor.mc.thePlayer.inventoryContainer.getSlot(hotbarItem).getStack()));
            AutoArmor.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(AutoArmor.mc.thePlayer.inventory.currentItem));
            this.msTimer.reset();
            this.delay = TimeUtils.randomDelay((int)this.minDelayValue.asObject(), (int)this.maxDelayValue.asObject());
        }
    }
    
    private boolean hasBetter(final int slot, final int[] type) {
        int armorIndex = -1;
        int inventoryIndex = -1;
        int inventorySlot = -1;
        for (int i = 0; i < type.length; ++i) {
            if (AutoArmor.mc.thePlayer.inventory.armorInventory[slot] != null && Item.getIdFromItem(AutoArmor.mc.thePlayer.inventory.armorInventory[slot].getItem()) == type[i]) {
                armorIndex = i;
                break;
            }
        }
        for (int i = 0; i < type.length; ++i) {
            if ((inventorySlot = this.getArmorItem(type[i], 9, 45)) != -1) {
                inventoryIndex = i;
                break;
            }
        }
        return inventoryIndex > -1 && (inventoryIndex < armorIndex || (inventoryIndex == armorIndex && ItemUtils.getEnchantment(AutoArmor.mc.thePlayer.inventory.armorInventory[slot], Enchantment.protection) < ItemUtils.getEnchantment(AutoArmor.mc.thePlayer.inventoryContainer.getSlot(inventorySlot).getStack(), Enchantment.protection)));
    }
    
    private int getArmor(final int[] ids, final int startSlot, final int endSlot) {
        for (final int id : ids) {
            final int i = this.getArmorItem(id, startSlot, endSlot);
            if (i != -1) {
                return i;
            }
        }
        return -1;
    }
    
    private int getArmorItem(final int id, final int startSlot, final int endSlot) {
        int bestSlot = -1;
        for (int index = startSlot; index < endSlot; ++index) {
            final ItemStack itemStack = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack != null) {
                if (System.currentTimeMillis() - ((IItemStack)itemStack).getItemDelay() >= this.itemDelayValue.asInteger()) {
                    if (Item.getIdFromItem(itemStack.getItem()) == id && (bestSlot == -1 || ItemUtils.getEnchantment(itemStack, Enchantment.protection) >= ItemUtils.getEnchantment(AutoArmor.mc.thePlayer.inventoryContainer.getSlot(bestSlot).getStack(), Enchantment.protection))) {
                        bestSlot = index;
                    }
                }
            }
        }
        return bestSlot;
    }
}
