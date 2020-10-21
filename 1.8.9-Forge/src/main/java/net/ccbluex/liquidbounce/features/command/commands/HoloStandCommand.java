//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.features.command.Command;

public class HoloStandCommand extends Command
{
    public HoloStandCommand() {
        super("holostand", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length <= 4) {
            this.chatSyntax(".holostand <x> <y> <z> <message...>");
            return;
        }
        if (!HoloStandCommand.mc.thePlayer.capabilities.isCreativeMode) {
            this.chat("You need creative mode.");
            return;
        }
        try {
            final double x = Double.parseDouble(args[1]);
            final double y = Double.parseDouble(args[2]);
            final double z = Double.parseDouble(args[3]);
            final String message = StringUtils.toCompleteString(args, 4);
            final ItemStack itemStack = new ItemStack((Item)Items.armor_stand);
            final NBTTagCompound base = new NBTTagCompound();
            final NBTTagCompound entityTag = new NBTTagCompound();
            entityTag.setInteger("Invisible", 1);
            entityTag.setString("CustomName", message);
            entityTag.setInteger("CustomNameVisible", 1);
            entityTag.setInteger("NoGravity", 1);
            final NBTTagList position = new NBTTagList();
            position.appendTag((NBTBase)new NBTTagDouble(x));
            position.appendTag((NBTBase)new NBTTagDouble(y));
            position.appendTag((NBTBase)new NBTTagDouble(z));
            entityTag.setTag("Pos", (NBTBase)position);
            base.setTag("EntityTag", (NBTBase)entityTag);
            itemStack.setTagCompound(base);
            itemStack.setStackDisplayName("§c§lHolo§eStand");
            Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C10PacketCreativeInventoryAction(36, itemStack));
            this.chat("You got the HoloStand.");
        }
        catch (NumberFormatException exception) {
            this.chatSyntaxError();
        }
    }
}
