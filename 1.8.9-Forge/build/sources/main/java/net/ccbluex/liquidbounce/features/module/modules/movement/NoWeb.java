//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoWeb", description = "Prevents you from getting slowed down in webs.", category = ModuleCategory.MOVEMENT)
public class NoWeb extends Module
{
    private final ListValue modeValue;
    
    public NoWeb() {
        this.modeValue = new ListValue("Mode", new String[] { "None", "AAC", "LAAC", "Rewi" }, "None");
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("noweb", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".noweb <mode>");
                    return;
                }
                if (args.length > 2 && NoWeb.this.modeValue.contains(args[2])) {
                    NoWeb.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7NoWeb mode was set to §8" + NoWeb.this.modeValue.asString().toUpperCase() + "§7.");
                    NoWeb$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".noweb mode §c<§8" + Strings.join(NoWeb.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!NoWeb.mc.thePlayer.isInWeb) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "none": {
                NoWeb.mc.thePlayer.isInWeb = false;
                break;
            }
            case "aac": {
                NoWeb.mc.thePlayer.jumpMovementFactor = 0.59f;
                if (!NoWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    NoWeb.mc.thePlayer.motionY = 0.0;
                    break;
                }
                break;
            }
            case "laac": {
                NoWeb.mc.thePlayer.jumpMovementFactor = ((NoWeb.mc.thePlayer.movementInput.moveStrafe != 0.0f) ? 1.0f : 1.21f);
                if (!NoWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    NoWeb.mc.thePlayer.motionY = 0.0;
                }
                if (NoWeb.mc.thePlayer.onGround) {
                    NoWeb.mc.thePlayer.jump();
                    break;
                }
                break;
            }
            case "rewi": {
                NoWeb.mc.thePlayer.jumpMovementFactor = 0.42f;
                if (NoWeb.mc.thePlayer.onGround) {
                    NoWeb.mc.thePlayer.jump();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
