package net.meano.PortablePortals;

import java.util.ArrayList;

import net.meano.PortablePortals.PortalHandlers.Portal;
import net.meano.PortablePortals.PortalHandlers.PortalManager;
import net.meano.PortablePortals.Util.Files;
import net.meano.PortablePortals.Util.Msgs;
import net.meano.PortablePortals.Util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PortablePortals extends JavaPlugin {

	public static Plugin me;
	public ItemStack PortalStar;
	public boolean update = false;
	public String name = "";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if (cmd.getName().equalsIgnoreCase("PPortals")) {
			if ((sender instanceof Player) && sender.hasPermission("PortablePortals.Spawn")) {
				Player player = (Player) sender;
				ItemStack AddStars = this.PortalStar.clone();
				if(args.length>0){
					if(args[0].matches("\\d+")){
						AddStars.setAmount(Integer.parseInt(args[0]));
					}
				}
				player.getInventory().addItem(AddStars);
			}

		} else if (cmd.getName().equalsIgnoreCase("PReload")){
			if (sender.isOp()){
				Files.reloadAll();
			}
		}
		return true;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		PortablePortals.me = this;
		getConfig().options().copyDefaults(true);
		saveConfig();
		Files.getMessages().options().copyDefaults(true);
		Files.saveMessages();
		
		PluginManager pm = getServer().getPluginManager();
		pm = getServer().getPluginManager();
		pm.registerEvents(new PortalsListeners(this), this);
		
		PortalStarInitialize();
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, 
			new Runnable() {
				@Override
				public void run() {
					if (!PortalManager.getPortals().isEmpty())
						for (Portal portal : PortalManager.getPortals())
							for (Entity e : portal.getLocation().getChunk().getEntities()) {
								if (e instanceof Player)
								{
									Location loc = PortalManager.getRoundedLocation(e.getLocation());
									if ((loc.getBlockX() == portal.getLocation().getBlockX())
										&& (loc.getBlockY() == portal.getLocation().getBlockY())
										&& (loc.getBlockZ() == portal.getLocation().getBlockZ()))
										if (!((Player) e).hasPermission("PortablePortals.Use")){
											break;
										}else{
											e.teleport(portal.getTarget(),TeleportCause.COMMAND);
										}
								}
							}
				}
			}, Settings.portalRefreshTime(), 0);

	}
	public void PortalStarInitialize(){
		this.PortalStar = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta im = this.PortalStar.getItemMeta();
		im.setDisplayName(Msgs.Portals_Title.getString());
		ArrayList<String> lores = new ArrayList<String>();
		lores.add(Msgs.Portals_LeftClickTo.getString());
		lores.add(Msgs.Portals_RightClickTo.getString());
		lores.add("" + ChatColor.WHITE + ChatColor.ITALIC + "------------");
		lores.add(Msgs.Portals_Target.getString("None"));
		im.setLore(lores);
		this.PortalStar.setItemMeta(im);
		ShapedRecipe portalCube = new ShapedRecipe(this.PortalStar)
				.shape(new String[] { "*#*", "#%#", "*#*" })
				.setIngredient('#', Material.EMERALD)
				.setIngredient('*', Material.OBSIDIAN)
				.setIngredient('%', Material.GOLDEN_APPLE);
		Bukkit.getServer().addRecipe(portalCube);
	}

}
