package net.meano.PortablePortals.Util;

import java.util.Random;

import org.bukkit.ChatColor;

/**
 * If anyone wants to use this class for their own plugin go ahead, this is a
 * class you can also find in my gists(On Github). If you have any
 * suggestions/additions that you think would help Infected feel free to message
 * me or fork the gist
 * 
 * @author Sniperz
 */

public class RandomChatColor {

	/**
	 * @param chatColors
	 *            - Only put things here if you want to choose from these colors
	 * @return the random color
	 */
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
