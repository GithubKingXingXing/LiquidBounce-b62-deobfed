
package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemFood;
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

@ModuleInfo(name = "FastUse", description = "Allows you to use items faster.", category = ModuleCategory.PLAYER)
public class FastUse extends Module
{
    private final ListValue modeValue;
    private boolean usedTimer;
    
    public FastUse() {
        this.modeValue = new ListValue("Mode", new String[] { "Instant", "NCP", "AAC" }, "NCP");
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("fastuse", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".fastuse <mode>");
                    return;
                }
                if (args.length > 2 && FastUse.this.modeValue.contains(args[2])) {
                    FastUse.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7FastUse mode was set to §8" + FastUse.this.modeValue.asString().toUpperCase() + "§7.");
                    FastUse$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".fastuse mode §c<§8" + Strings.join(FastUse.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (FastUse.mc.thePlayer.isUsingItem() && (FastUse.mc.thePlayer.getItemInUse().getItem() instanceof ItemFood || FastUse.mc.thePlayer.getItemInUse().getItem() instanceof ItemPotion)) {
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "instant": {
                    for (int i = 0; i < 35; ++i) {
                        FastUse.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer());
                    }
                    FastUse.mc.playerController.onStoppedUsingItem((EntityPlayer)FastUse.mc.thePlayer);
                    break;
                }
                case "ncp": {
                    if (FastUse.mc.thePlayer.getItemInUseDuration() > 14) {
                        for (int i = 0; i < 20; ++i) {
                            FastUse.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer());
                        }
                        FastUse.mc.playerController.onStoppedUsingItem((EntityPlayer)FastUse.mc.thePlayer);
                        break;
                    }
                    break;
                }
                case "aac": {
                    FastUse.mc.timer.timerSpeed = 1.22f;
                    this.usedTimer = true;
                    break;
                }
            }
        }
        else if (this.usedTimer) {
            FastUse.mc.timer.timerSpeed = 1.0f;
            this.usedTimer = false;
        }
    }
    
    @Override
    public void onDisable() {
        if (this.usedTimer) {
            FastUse.mc.timer.timerSpeed = 1.0f;
            this.usedTimer = false;
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
