
package net.ccbluex.liquidbounce.event.events;

import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.ccbluex.liquidbounce.event.Event;

public class BlockBBEvent extends Event
{
    private final int x;
    private final int y;
    private final int z;
    private final Block block;
    private AxisAlignedBB axisAlignedBB;
    
    public BlockBBEvent(final BlockPos blockPos, final Block block, final AxisAlignedBB axisAlignedBB) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
        this.block = block;
        this.setBoundingBox(axisAlignedBB);
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return this.axisAlignedBB;
    }
    
    public void setBoundingBox(final AxisAlignedBB axisAlignedBB) {
        this.axisAlignedBB = axisAlignedBB;
    }
}
