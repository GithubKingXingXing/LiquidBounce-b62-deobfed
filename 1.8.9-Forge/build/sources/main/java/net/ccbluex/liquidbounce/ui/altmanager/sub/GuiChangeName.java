
package net.ccbluex.liquidbounce.ui.altmanager.sub;

import java.io.IOException;
import net.minecraft.util.Session;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiChangeName extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiTextField name;
    private String status;
    
    public GuiChangeName(final GuiScreen gui) {
        this.prevGui = gui;
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, "Change"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Back"));
        (this.name = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20)).setFocused(true);
        this.name.setText(this.mc.getSession().getUsername());
        this.name.setMaxStringLength(Integer.MAX_VALUE);
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        Gui.drawRect(30, 30, this.width - 30, this.height - 30, Integer.MIN_VALUE);
        this.drawCenteredString(this.mc.fontRendererObj, "Change Name", this.width / 2, 34, 16777215);
        this.drawCenteredString(this.mc.fontRendererObj, (this.status == null) ? "" : this.status, this.width / 2, this.height / 4 + 86, 16777215);
        this.name.drawTextBox();
        if (this.name.getText().isEmpty()) {
            this.drawCenteredString(this.fontRendererObj, "§7Username", this.width / 2 - 50, 65, 16777215);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.prevGui);
                break;
            }
            case 1: {
                if (this.name.getText().isEmpty()) {
                    this.status = "§c§lYou need to write a name.";
                    return;
                }
                if (!this.name.getText().equalsIgnoreCase(this.mc.getSession().getUsername())) {
                    this.status = "§c§lThe new name must look like the old one";
                    return;
                }
                this.mc.session = new Session(this.name.getText(), this.mc.getSession().getPlayerID(), this.mc.getSession().getToken(), this.mc.getSession().getSessionType().name());
                this.mc.displayGuiScreen(this.prevGui);
                break;
            }
        }
        super.actionPerformed(button);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.mc.displayGuiScreen(this.prevGui);
            return;
        }
        if (this.name.isFocused()) {
            this.name.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.name.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
}
