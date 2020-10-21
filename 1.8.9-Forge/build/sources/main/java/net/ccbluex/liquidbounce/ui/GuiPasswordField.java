
package net.ccbluex.liquidbounce.ui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiPasswordField extends GuiTextField
{
    public GuiPasswordField(final int componentId, final FontRenderer fontrendererObj, final int x, final int y, final int par5Width, final int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }
    
    public void drawTextBox() {
        final String text = this.getText();
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            stringBuilder.append('*');
        }
        this.setText(stringBuilder.toString());
        super.drawTextBox();
        this.setText(text);
    }
}
