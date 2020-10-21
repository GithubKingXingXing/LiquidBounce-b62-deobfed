//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FastStairs", description = "Allows you to climb up stairs faster.", category = ModuleCategory.MOVEMENT)
public class FastStairs extends Module
{
    private final ListValue modeValue;
    private final BoolValue longJumpValue;
    private boolean canJump;
    
    public FastStairs() {
        this.modeValue = new ListValue("Mode", new String[] { "NCP", "AAC", "LAAC" }, "NCP");
        this.longJumpValue = new BoolValue("LongJump", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("faststairs", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".faststairs <mode>");
                    return;
                }
                if (args.length > 2 && FastStairs.this.modeValue.contains(args[2])) {
                    FastStairs.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7FastStairs mode was set to §8" + FastStairs.this.modeValue.asString().toUpperCase() + "§7.");
                    FastStairs$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".faststairs mode §c<§8" + Strings.join(FastStairs.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (FastStairs.mc.thePlayer == null || ModuleManager.getModule(Speed.class).getState()) {
            return;
        }
        final BlockPos blockPos = new BlockPos(FastStairs.mc.thePlayer.posX, FastStairs.mc.thePlayer.getEntityBoundingBox().minY, FastStairs.mc.thePlayer.posZ);
        if (FastStairs.mc.thePlayer.onGround && FastStairs.mc.thePlayer.movementInput.moveForward > 0.0) {
            final String mode = this.modeValue.asString();
            if (BlockUtils.getBlock(blockPos) instanceof BlockStairs) {
                FastStairs.mc.thePlayer.setPosition(FastStairs.mc.thePlayer.posX, FastStairs.mc.thePlayer.posY + 0.5, FastStairs.mc.thePlayer.posZ);
                final EntityPlayerSP thePlayer = FastStairs.mc.thePlayer;
                thePlayer.motionX *= (mode.equalsIgnoreCase("NCP") ? 1.4 : (mode.equalsIgnoreCase("AAC") ? 1.5 : (mode.equalsIgnoreCase("AAC") ? 1.499 : 1.0)));
                final EntityPlayerSP thePlayer2 = FastStairs.mc.thePlayer;
                thePlayer2.motionZ *= (mode.equalsIgnoreCase("NCP") ? 1.4 : (mode.equalsIgnoreCase("AAC") ? 1.5 : (mode.equalsIgnoreCase("AAC") ? 1.499 : 1.0)));
            }
            if (BlockUtils.getBlock(blockPos.down()) instanceof BlockStairs) {
                final EntityPlayerSP thePlayer3 = FastStairs.mc.thePlayer;
                thePlayer3.motionX *= 1.3;
                final EntityPlayerSP thePlayer4 = FastStairs.mc.thePlayer;
                thePlayer4.motionZ *= 1.3;
                if (mode.equalsIgnoreCase("LAAC")) {
                    final EntityPlayerSP thePlayer5 = FastStairs.mc.thePlayer;
                    thePlayer5.motionX *= 1.18;
                    final EntityPlayerSP thePlayer6 = FastStairs.mc.thePlayer;
                    thePlayer6.motionZ *= 1.18;
                }
                this.canJump = true;
            }
            else if ((mode.equalsIgnoreCase("LAAC") || mode.equalsIgnoreCase("AAC")) && FastStairs.mc.thePlayer.onGround && this.canJump) {
                if (this.longJumpValue.asBoolean()) {
                    FastStairs.mc.thePlayer.jump();
                    final EntityPlayerSP thePlayer7 = FastStairs.mc.thePlayer;
                    thePlayer7.motionX *= 1.35;
                    final EntityPlayerSP thePlayer8 = FastStairs.mc.thePlayer;
                    thePlayer8.motionZ *= 1.35;
                }
                this.canJump = false;
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
