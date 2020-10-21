//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.block.Block;
import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockLiquid;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "ReverseStep", description = "Allows you to step down blocks faster.", category = ModuleCategory.MOVEMENT)
public class ReverseStep extends Module
{
    private final FloatValue motionValue;
    private boolean jumped;
    
    public ReverseStep() {
        this.motionValue = new FloatValue("Motion", 1.0f, 0.21f, 1.0f);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("reversestep", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("motion")) {
                    this.chatSyntax(".reversestep <motion>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final float motion = Float.parseFloat(args[2]);
                        ReverseStep.this.motionValue.setValue(motion);
                        this.chat("§7ReverseStep motion was set to §8" + motion + "§7.");
                        ReverseStep$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax(".reversestep motion <value>");
            }
        });
    }
    
    @EventTarget(ignoreCondition = true)
    public void onUpdate(final UpdateEvent event) {
        if (ReverseStep.mc.thePlayer.onGround) {
            this.jumped = false;
        }
        if (ReverseStep.mc.thePlayer.motionY > 0.0) {
            this.jumped = true;
        }
        if (!this.getState()) {
            return;
        }
        if (BlockUtils.collideBlock(ReverseStep.mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid) || BlockUtils.collideBlock(new AxisAlignedBB(ReverseStep.mc.thePlayer.getEntityBoundingBox().maxX, ReverseStep.mc.thePlayer.getEntityBoundingBox().maxY, ReverseStep.mc.thePlayer.getEntityBoundingBox().maxZ, ReverseStep.mc.thePlayer.getEntityBoundingBox().minX, ReverseStep.mc.thePlayer.getEntityBoundingBox().minY - 0.01, ReverseStep.mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid)) {
            return;
        }
        if (!ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && !ReverseStep.mc.thePlayer.onGround && !ReverseStep.mc.thePlayer.movementInput.jump && ReverseStep.mc.thePlayer.motionY <= 0.0 && ReverseStep.mc.thePlayer.fallDistance <= 1.0f && !this.jumped) {
            ReverseStep.mc.thePlayer.motionY = -this.motionValue.asFloat();
        }
    }
    
    @EventTarget(ignoreCondition = true)
    public void onJump(final JumpEvent event) {
        this.jumped = true;
    }
}
