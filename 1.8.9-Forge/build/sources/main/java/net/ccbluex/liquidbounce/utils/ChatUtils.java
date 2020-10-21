
package net.ccbluex.liquidbounce.utils;

import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;

public final class ChatUtils
{
    public static void displayChatMessage(final String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent("{text:\"" + message + "\"}"));
    }
}
