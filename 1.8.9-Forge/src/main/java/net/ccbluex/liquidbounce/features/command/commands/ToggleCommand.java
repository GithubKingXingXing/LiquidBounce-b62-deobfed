//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.module.Module;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.command.Command;

public class ToggleCommand extends Command
{
    public ToggleCommand() {
        super("toggle", new String[] { "t" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length <= 1) {
            this.chatSyntax(".toggle <module>");
            return;
        }
        final Module module = ModuleManager.getModule(args[1]);
        if (module == null) {
            this.chat("Module '" + args[1] + "' not found.");
            return;
        }
        module.toggle();
        this.chat("Toggled module §8" + module.getName() + "§7.");
        ToggleCommand.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.bow"), 1.0f));
    }
}
