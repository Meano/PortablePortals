package net.meano.PortablePortals.Util;

import net.meano.PortablePortals.PortablePortals;

import org.bukkit.Material;

public class Settings {

	public static boolean checkForUpdate() {
		return PortablePortals.me.getConfig().getBoolean("Check for new update");
	}

	public static Material getItemRequired() {
		return Material.getMaterial(PortablePortals.me.getConfig().getString("Require Item To Use.Item"));
	}

	public static boolean isItemRequired() {
		return PortablePortals.me.getConfig().getBoolean("Require Item To Use.Enabled");
	}

	public static int portalRefreshTime() {
		return PortablePortals.me.getConfig().getInt("Portals.Check If In Portal Refresh Time") * 20;
	}
	
	public static String Language(){
		return PortablePortals.me.getConfig().getString("MessageLanguage");
	}
	
	public static int stayOpenTime() {
		return PortablePortals.me.getConfig().getInt("Portals.Stay Open Time") * 20;
	}
}
