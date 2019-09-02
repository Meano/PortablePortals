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
import org.bukkit.NamespacedKey;
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
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
				if (cmd.getName().equalsIgnoreCase("PPortals")) {
						if ((sender instanceof Player) && sender.hasPermission("PortablePortals.Spawn")) {
								Player player = (Player) sender;
								ItemStack AddStars = PortalStar.clone();
								if (args.length > 0) {
										if (args[0].matches("\\d+")) {
												AddStars.setAmount(Integer.parseInt(args[0]));
										}
								}
								player.getInventory().addItem(AddStars);
						}

				} else if (cmd.getName().equalsIgnoreCase("PReload")) {
						if (sender.isOp()) {
								Files.reloadAll();
						}
				}
				return true;
		}

		@Override
		public void onDisable() {
				for (Portal portal : PortalManager.getPortals()) {
						Bukkit.broadcastMessage(ChatColor.RED + "服务器重启或停止的原因，传送之星提前关闭，会在传送门生成的地方掉落传送之星!");
						portal.getLocation().getWorld().dropItemNaturally(portal.getLocation(), portal.getItem());
						portal.removePortal();
						PortalManager.delPortal(portal);
				}
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

				PortalStar = PortalStarInitialize("无");
				ShapedRecipe portalCube =
					new ShapedRecipe(new NamespacedKey(this, "PortalStar"), this.PortalStar).shape(new String[] { "*#*", "#%#", "*#*" }).setIngredient('#', Material.EMERALD).setIngredient('*', Material.OBSIDIAN).setIngredient('%', Material.GOLDEN_APPLE);
				Bukkit.getServer().addRecipe(portalCube);
				
				Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
					@Override
					public void run() {
						if (!PortalManager.getPortals().isEmpty())
							for (Portal portal : PortalManager.getPortals())
								for (Entity e : portal.getLocation().getChunk().getEntities()) {
									if (e instanceof Player) {
										Location loc = PortalManager.getRoundedLocation(e.getLocation());
										if ((loc.getBlockX() == portal.getLocation().getBlockX()) && (loc.getBlockY() == portal.getLocation().getBlockY()) && (loc.getBlockZ() == portal.getLocation().getBlockZ()))
											e.teleport(portal.getTarget(), TeleportCause.PLUGIN);
									}
								}
					}
				}, Settings.portalRefreshTime(), 0);
		}

		public ItemStack PortalStarInitialize(String Target) {
				ItemStack Star = new ItemStack(Material.NETHER_STAR, 1);
				ItemMeta im = Star.getItemMeta();
				im.setDisplayName(Msgs.Portals_Title.getString());
				ArrayList<String> lores = new ArrayList<String>();
				lores.add(Msgs.Portals_LeftClickTo.getString());
				lores.add(Msgs.Portals_RightClickTo.getString());
				lores.add("" + ChatColor.WHITE + ChatColor.ITALIC + "------------");
				lores.add(Msgs.Portals_Target.getString(Target));
				im.setLore(lores);
				Star.setItemMeta(im);
				return Star;
		}

}
