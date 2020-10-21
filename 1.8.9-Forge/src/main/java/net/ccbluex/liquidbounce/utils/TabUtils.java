//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.gui.GuiTextField;

public final class TabUtils
{
    public static void tab(final GuiTextField... textFields) {
        for (int i = 0; i < textFields.length; ++i) {
            final GuiTextField textField = textFields[i];
            if (textField.isFocused()) {
                textField.setFocused(false);
                if (++i >= textFields.length) {
                    i = 0;
                }
                textFields[i].setFocused(true);
                break;
            }
        }
    }
}
