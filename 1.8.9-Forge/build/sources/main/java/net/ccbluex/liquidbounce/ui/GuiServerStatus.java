
package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import com.google.gson.internal.LinkedTreeMap;
import net.ccbluex.liquidbounce.utils.misc.NetworkUtils;
import com.google.gson.Gson;
import java.util.List;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiScreen;

public class GuiServerStatus extends GuiScreen
{
    private final GuiScreen prevGui;
    private final Map<String, String> status;
    
    public GuiServerStatus(final GuiScreen prevGui) {
        this.status = new HashMap<String, String>();
        this.prevGui = prevGui;
    }
    
    public void initGui() {
        new Thread(this::loadInformations).start();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 145, "Back"));
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        int i = this.height / 4 + 40;
        Gui.drawRect(this.width / 2 - 115, i - 5, this.width / 2 + 115, this.height / 4 + 43 + (this.status.keySet().isEmpty() ? 10 : (this.status.keySet().size() * Fonts.font40.FONT_HEIGHT)), Integer.MIN_VALUE);
        if (this.status.isEmpty()) {
            this.drawCenteredString((FontRenderer)Fonts.font40, "Loading...", this.width / 2, this.height / 4 + 40, Color.WHITE.getRGB());
        }
        else {
            for (final String server : this.status.keySet()) {
                final String color = this.status.get(server);
                this.drawCenteredString((FontRenderer)Fonts.font40, "§c§l" + server + ": " + (color.equalsIgnoreCase("red") ? "§c" : (color.equalsIgnoreCase("yellow") ? "§e" : "§a")) + (color.equalsIgnoreCase("red") ? "Offline" : (color.equalsIgnoreCase("yellow") ? "Slow" : "Online")), this.width / 2, i, Color.WHITE.getRGB());
                i += Fonts.font40.FONT_HEIGHT;
            }
        }
        Fonts.fontBold180.drawCenteredString("Server Status", (int)(this.width / 2.0f), (int)(this.height / 8.0f + 5.0f), 4673984, true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private void loadInformations() {
        this.status.clear();
        try {
            final List<LinkedTreeMap> linkedTreeMaps = (List<LinkedTreeMap>)new Gson().fromJson(NetworkUtils.readContent("https://status.mojang.com/check"), (Class)List.class);
            for (final LinkedTreeMap linkedTreeMap : linkedTreeMaps) {
                for (final Map.Entry<String, String> entry : linkedTreeMap.entrySet()) {
                    this.status.put(entry.getKey(), entry.getValue());
                }
            }
        }
        catch (IOException e) {
            this.status.put("status.mojang.com/check", "red");
        }
    }
    
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.prevGui);
                break;
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.mc.displayGuiScreen(this.prevGui);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
