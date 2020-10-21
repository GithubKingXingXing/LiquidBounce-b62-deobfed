//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemSword;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "TNTBlock", description = "Automatically blocks with your sword when TNT around you explodes.", category = ModuleCategory.COMBAT)
public class TNTBlock extends Module
{
    private final IntegerValue fuseValue;
    private final BoolValue autoSwordValue;
    private boolean blocked;
    
    public TNTBlock() {
        this.fuseValue = new IntegerValue("Fuse", 10, 0, 20);
        this.autoSwordValue = new BoolValue("AutoSword", true);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("tntblock", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("fuse")) {
                    this.chatSyntax(".tntblock <fuse>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final int value = Integer.parseInt(args[2]);
                        TNTBlock.this.fuseValue.setValue(value);
                        this.chat("§7TNTBlock fuse was set to §8" + value + "§7.");
                        TNTBlock$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".tntblock fuse <value>");
            }
        });
    }
    
    @EventTarget
    public void onMotionUpdate(final MotionEvent event) {
        for (final Entity entity : TNTBlock.mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed && TNTBlock.mc.thePlayer.getDistanceToEntity(entity) <= 9.0) {
                final EntityTNTPrimed tntPrimed = (EntityTNTPrimed)entity;
                if (tntPrimed.fuse <= this.fuseValue.asInteger()) {
                    if (this.autoSwordValue.asBoolean()) {
                        int slot = -1;
                        float bestDamage = 1.0f;
                        for (int i = 0; i < 9; ++i) {
                            final ItemStack itemStack = TNTBlock.mc.thePlayer.inventory.getStackInSlot(i);
                            if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
                                final float itemDamage = ((ItemSword)itemStack.getItem()).getDamageVsEntity() + 4.0f;
                                if (itemDamage > bestDamage) {
                                    bestDamage = itemDamage;
                                    slot = i;
                                }
                            }
                        }
                        if (slot != -1 && slot != TNTBlock.mc.thePlayer.inventory.currentItem) {
                            TNTBlock.mc.thePlayer.inventory.currentItem = slot;
                            TNTBlock.mc.playerController.updateController();
                        }
                    }
                    if (TNTBlock.mc.thePlayer.getHeldItem() != null && TNTBlock.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                        TNTBlock.mc.gameSettings.keyBindUseItem.pressed = true;
                        this.blocked = true;
                    }
                    return;
                }
                continue;
            }
        }
        if (this.blocked && !GameSettings.isKeyDown(TNTBlock.mc.gameSettings.keyBindUseItem)) {
            TNTBlock.mc.gameSettings.keyBindUseItem.pressed = false;
            this.blocked = false;
        }
    }
}
