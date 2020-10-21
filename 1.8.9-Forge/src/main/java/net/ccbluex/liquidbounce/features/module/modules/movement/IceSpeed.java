
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.block.material.Material;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.init.Blocks;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "IceSpeed", description = "Allows you to walk faster on ice.", category = ModuleCategory.MOVEMENT)
public class IceSpeed extends Module
{
    private final ListValue modeValue;
    
    public IceSpeed() {
        this.modeValue = new ListValue("Mode", new String[] { "NCP", "AAC", "Spartan" }, "NCP");
    }
    
    @Override
    public void onEnable() {
        if (this.modeValue.asString().equalsIgnoreCase("NCP")) {
            Blocks.ice.slipperiness = 0.39f;
            Blocks.packed_ice.slipperiness = 0.39f;
        }
        super.onEnable();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        final String mode = this.modeValue.asString();
        if (mode.equalsIgnoreCase("NCP")) {
            Blocks.ice.slipperiness = 0.39f;
            Blocks.packed_ice.slipperiness = 0.39f;
        }
        else {
            Blocks.ice.slipperiness = 0.98f;
            Blocks.packed_ice.slipperiness = 0.98f;
        }
        if (IceSpeed.mc.thePlayer.onGround && !IceSpeed.mc.thePlayer.isOnLadder() && !IceSpeed.mc.thePlayer.isSneaking() && IceSpeed.mc.thePlayer.isSprinting() && IceSpeed.mc.thePlayer.movementInput.moveForward > 0.0) {
            if (mode.equalsIgnoreCase("AAC")) {
                final Material material = BlockUtils.getMaterial(IceSpeed.mc.thePlayer.getPosition().down());
                if (material == Material.ice || material == Material.packedIce) {
                    final EntityPlayerSP thePlayer = IceSpeed.mc.thePlayer;
                    thePlayer.motionX *= 1.342;
                    final EntityPlayerSP thePlayer2 = IceSpeed.mc.thePlayer;
                    thePlayer2.motionZ *= 1.342;
                    Blocks.ice.slipperiness = 0.6f;
                    Blocks.packed_ice.slipperiness = 0.6f;
                }
            }
            if (mode.equalsIgnoreCase("Spartan")) {
                final Material material = BlockUtils.getMaterial(IceSpeed.mc.thePlayer.getPosition().down());
                if (material == Material.ice || material == Material.packedIce) {
                    final Block upBlock = BlockUtils.getBlock(new BlockPos(IceSpeed.mc.thePlayer.posX, IceSpeed.mc.thePlayer.posY + 2.0, IceSpeed.mc.thePlayer.posZ));
                    if (!(upBlock instanceof BlockAir)) {
                        final EntityPlayerSP thePlayer3 = IceSpeed.mc.thePlayer;
                        thePlayer3.motionX *= 1.342;
                        final EntityPlayerSP thePlayer4 = IceSpeed.mc.thePlayer;
                        thePlayer4.motionZ *= 1.342;
                    }
                    else {
                        final EntityPlayerSP thePlayer5 = IceSpeed.mc.thePlayer;
                        thePlayer5.motionX *= 1.18;
                        final EntityPlayerSP thePlayer6 = IceSpeed.mc.thePlayer;
                        thePlayer6.motionZ *= 1.18;
                    }
                    Blocks.ice.slipperiness = 0.6f;
                    Blocks.packed_ice.slipperiness = 0.6f;
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        Blocks.ice.slipperiness = 0.98f;
        Blocks.packed_ice.slipperiness = 0.98f;
        super.onDisable();
    }
}
