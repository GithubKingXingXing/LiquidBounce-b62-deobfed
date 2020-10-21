
package net.ccbluex.liquidbounce.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import java.io.IOException;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import com.google.gson.JsonElement;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import net.ccbluex.liquidbounce.utils.misc.NetworkUtils;
import com.google.gson.JsonParser;
import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class GuiCredits extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiList list;
    private final List<Credit> credits;
    
    public GuiCredits(final GuiScreen prevGui) {
        this.credits = new ArrayList<Credit>();
        this.prevGui = prevGui;
    }
    
    public void initGui() {
        (this.list = new GuiList(this)).registerScrollButtons(7, 8);
        this.list.elementClicked(-1, false, 0, 0);
        new Thread(this::loadCredits).start();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 30, "Back"));
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawRect(this.width / 4, 40, this.width, this.height - 40, Integer.MIN_VALUE);
        if (this.list.getSelectedSlot() != -1) {
            final Credit credit = this.credits.get(this.list.getSelectedSlot());
            int y = 45;
            Fonts.font40.drawString("Name: " + credit.getName(), (float)(this.width / 4 + 5), (float)y, Color.WHITE.getRGB(), true);
            if (credit.getTwitterName() != null) {
                y += Fonts.font40.FONT_HEIGHT;
                Fonts.font40.drawString("Twitter: " + credit.getTwitterName(), (float)(this.width / 4 + 5), (float)y, Color.WHITE.getRGB(), true);
            }
            if (credit.getYoutubeName() != null) {
                y += Fonts.font40.FONT_HEIGHT;
                Fonts.font40.drawString("YouTube: " + credit.getYoutubeName(), (float)(this.width / 4 + 5), (float)y, Color.WHITE.getRGB(), true);
            }
            y += Fonts.font40.FONT_HEIGHT;
            for (final String s : credit.getCredits()) {
                y += Fonts.font40.FONT_HEIGHT;
                Fonts.font40.drawString(s, (float)(this.width / 4 + 5), (float)y, Color.WHITE.getRGB(), true);
            }
        }
        Fonts.font40.drawCenteredString("Credits", this.width / 2, 6, 16777215);
        if (this.credits.isEmpty()) {
            this.drawCenteredString((FontRenderer)Fonts.font40, "Loading...", this.width / 8, this.height / 2, Color.WHITE.getRGB());
            RenderUtils.drawLoadingCircle((float)(this.width / 8), (float)(this.height / 2 - 40));
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private void loadCredits() {
        this.credits.clear();
        try {
            final JsonElement jsonElement = new JsonParser().parse(NetworkUtils.readContent("https://ccbluex.github.io/FileCloud/LiquidBounce/credits.json"));
            if (jsonElement instanceof JsonNull) {
                return;
            }
            final JsonObject jsonObject = (JsonObject)jsonElement;
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                final JsonObject creditJson = entry.getValue().getAsJsonObject();
                final List<String> userCredits = new ArrayList<String>();
                creditJson.get("Credits").getAsJsonObject().entrySet().forEach(stringJsonElementEntry -> userCredits.add(stringJsonElementEntry.getValue().getAsString()));
                this.credits.add(new Credit(this.getInfoFromJson(creditJson, "Name"), this.getInfoFromJson(creditJson, "TwitterName"), this.getInfoFromJson(creditJson, "YouTubeName"), userCredits));
            }
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("Failed to load credits.", (Throwable)e);
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
    
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }
    
    private String getInfoFromJson(final JsonObject jsonObject, final String key) {
        return jsonObject.has(key) ? jsonObject.get(key).getAsString() : null;
    }
    
    class Credit
    {
        private final String name;
        private final String twitterName;
        private final String youtubeName;
        private final List<String> credits;
        
        Credit(final String name, final String twitterName, final String youtubeName, final List<String> credits) {
            this.name = name;
            this.twitterName = twitterName;
            this.youtubeName = youtubeName;
            this.credits = credits;
        }
        
        String getName() {
            return this.name;
        }
        
        String getTwitterName() {
            return this.twitterName;
        }
        
        String getYoutubeName() {
            return this.youtubeName;
        }
        
        List<String> getCredits() {
            return this.credits;
        }
    }
    
    private class GuiList extends GuiSlot
    {
        private int selectedSlot;
        
        GuiList(final GuiScreen prevGui) {
            super(Minecraft.getMinecraft(), prevGui.width / 4, prevGui.height, 40, prevGui.height - 40, 15);
        }
        
        protected boolean isSelected(final int id) {
            return this.selectedSlot == id;
        }
        
        protected int getSize() {
            return GuiCredits.this.credits.size();
        }
        
        int getSelectedSlot() {
            if (this.selectedSlot > GuiCredits.this.credits.size()) {
                this.selectedSlot = -1;
            }
            return this.selectedSlot;
        }
        
        protected void elementClicked(final int index, final boolean doubleClick, final int var3, final int var4) {
            this.selectedSlot = index;
        }
        
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            final Credit credit = GuiCredits.this.credits.get(entryID);
            Fonts.font40.drawCenteredString(credit.getName(), this.width / 2, p_180791_3_ + 2, Color.WHITE.getRGB(), true);
        }
        
        protected void drawBackground() {
        }
    }
}
