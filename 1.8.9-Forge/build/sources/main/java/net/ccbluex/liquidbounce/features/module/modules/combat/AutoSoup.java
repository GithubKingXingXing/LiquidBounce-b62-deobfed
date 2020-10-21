
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.minecraft.init.Items;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoSoup", description = "Makes you automatically eat soup whenever your health is low.", category = ModuleCategory.COMBAT)
public class AutoSoup extends Module
{
    private final FloatValue healthValue;
    private final IntegerValue delayValue;
    private final BoolValue openInventoryValue;
    private final MSTimer msTimer;
    
    public AutoSoup() {
        this.healthValue = new FloatValue("Health", 15.0f, 0.0f, 20.0f);
        this.delayValue = new IntegerValue("Delay", 150, 0, 500);
        this.openInventoryValue = new BoolValue("OpenInv", false);
        this.msTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("autosoup", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("health")) {
                    this.chatSyntax(".autosoup <health>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final float value = Float.parseFloat(args[2]);
                        AutoSoup.this.healthValue.setValue(value);
                        this.chat("§7AutoSoup health was set to §8" + value + "§7.");
                        AutoSoup$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".autosoup health <value>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!this.msTimer.hasTimePassed(this.delayValue.asInteger())) {
            return;
        }
        final int soupInHotbar = InventoryUtils.findItem(36, 45, Items.mushroom_stew);
        if (AutoSoup.mc.thePlayer.getHealth() <= this.healthValue.asFloat() && soupInHotbar != -1) {
            AutoSoup.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(soupInHotbar - 36));
            AutoSoup.mc.getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(AutoSoup.mc.thePlayer.inventoryContainer.getSlot(soupInHotbar).getStack()));
            AutoSoup.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            AutoSoup.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(AutoSoup.mc.thePlayer.inventory.currentItem));
            this.msTimer.reset();
            return;
        }
        final int soupInInventory = InventoryUtils.findItem(9, 36, Items.mushroom_stew);
        if (soupInInventory != -1 && InventoryUtils.hasSpaceHotbar()) {
            if (this.openInventoryValue.asBoolean() && !(AutoSoup.mc.currentScreen instanceof GuiInventory)) {
                return;
            }
            AutoSoup.mc.playerController.windowClick(0, soupInInventory, 0, 1, (EntityPlayer)AutoSoup.mc.thePlayer);
            this.msTimer.reset();
        }
    }
    
    @Override
    public String getTag() {
        return String.valueOf(this.healthValue.asFloat());
    }
}
