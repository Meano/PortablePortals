package net.meano.PortablePortals.Util;

import java.util.Random;

import org.bukkit.ChatColor;

public class RandomChatColor {
	public static ChatColor getColor(ChatColor... chatColors) {
		Random r = new Random();
		ChatColor[] colors;
		if (chatColors.length == 0)
			colors = ChatColor.values();
		else
			colors = chatColors;
		int i = r.nextInt(colors.length);
		while (!colors[i].isColor())
			i = r.nextInt(colors.length);
		ChatColor rc = colors[i];
		return rc;
	}

	/**
	 * @param chatColors
	 *            - Only put things here if you want to choose from these
	 *            formats
	 * @return the random format
	 */
	public static ChatColor getFormat(ChatColor... chatColors) {
		Random r = new Random();
		ChatColor[] colors;
		if (chatColors.length == 0)
			colors = ChatColor.values();
		else
			colors = chatColors;
		int i = r.nextInt(colors.length);
		while (!colors[i].isFormat())
			i = r.nextInt(colors.length);
		ChatColor rc = colors[i];
		return rc;
	}
}
