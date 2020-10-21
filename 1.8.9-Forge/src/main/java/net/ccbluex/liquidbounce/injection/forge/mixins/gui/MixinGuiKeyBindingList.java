//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiKeyBindingList;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.GuiSlot;

@Mixin({ GuiKeyBindingList.class })
public abstract class MixinGuiKeyBindingList extends GuiSlot
{
    public MixinGuiKeyBindingList(final Minecraft mcIn, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(mcIn, width, height, topIn, bottomIn, slotHeightIn);
    }
    
    @Overwrite
    protected int getScrollBarX() {
        return this.width - 5;
    }
}
