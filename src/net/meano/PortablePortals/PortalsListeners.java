package net.meano.PortablePortals;

import java.util.ArrayList;
import net.meano.PortablePortals.PortalHandlers.Portal;
import net.meano.PortablePortals.PortalHandlers.PortalManager;
import net.meano.PortablePortals.Util.Msgs;
import net.meano.PortablePortals.Util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PortalsListeners implements Listener {

	public PortablePortals plugin;

	public PortalsListeners(PortablePortals instance) {
		this.plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerAttemptToBreakPortal(BlockBreakEvent event) {
		for (Portal portal : PortalManager.getPortals())
			if (portal.isBlock(event.getBlock().getLocation()))
				event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerUsePortal(final PlayerInteractEvent e) {
		if (e.getItem() != null)
			if (e.getItem().getType().equals(Material.NETHER_STAR))
				if (e.getItem().getItemMeta().hasLore())
					if (e.getPlayer().hasPermission("PortablePortals.Create")) {
						// 清除废旧的刷出来的星星
						if (e.getItem().getItemMeta().getLore().get(1).contains("右键点击")) {
							Location tar = PortalManager.getTargetFromItem(e.getItem());
							if (tar == null) {
								e.getPlayer().getInventory().remove(e.getItem());
								return;
							}
						}
						final Player player = e.getPlayer();
						if (!e.getItem().getItemMeta().getLore().get(0).equals(Msgs.Portals_LeftClickTo.getString())) {
							player.sendMessage(ChatColor.RED + "不能使用！请联系管理员！");
							return;
						}
						if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
							Location loc = e.getClickedBlock().getLocation().clone().add(0.0, 1.0, 0.0);
							if (loc.getWorld().getName().equalsIgnoreCase("world_nether") && loc.getBlockY() > 120) {
								player.sendMessage(ChatColor.RED + "无法设定目的地！");
								return;
							}
							String locationString = PortalManager.getLocationToString(loc);
							ItemStack portal = e.getItem().clone();
							portal.setAmount(1);
							ItemMeta im = portal.getItemMeta();
							im.setDisplayName(Msgs.Portals_Title.getString());
							ArrayList<String> lores = new ArrayList<String>();
							lores.add(Msgs.Portals_LeftClickTo.getString());
							lores.add(Msgs.Portals_RightClickTo.getString());
							lores.add("" + ChatColor.WHITE + ChatColor.ITALIC + "------------");
							lores.add(Msgs.Portals_Target.getString(locationString));
							player.sendMessage(locationString);
							im.setLore(lores);
							portal.setItemMeta(im);
							if (e.getItem().getAmount() != 1)
								e.getItem().setAmount(e.getItem().getAmount() - 1);
							else
								player.setItemInHand(null);

							player.getInventory().addItem(portal);
							player.sendMessage(Msgs.Portals_TargetSet.getString());
						} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) { // Save
							if (player.hasPermission("PortablePortals.Create")) {
								if (hasItemRequired(player)) {
									Location target = PortalManager.getTargetFromItem(e.getItem());
									if (target == null) { // 未设置目的地
										player.sendMessage(Msgs.Portals_NoTarget.getString());
									} else if (target.getWorld().getName().equalsIgnoreCase("world_nether") && target.getBlockY() > 120) {
										player.sendMessage(ChatColor.RED + "无法到达目的地！");
										return;
									} else {
										final Portal portal = PortalManager.addPortal(e.getClickedBlock().getLocation(), target, e.getItem(), player);
										// player.updateInventory();
										player.getWorld().createExplosion(portal.getLocation(), 0.0F, false);

										if (!portal.canCreatePortal()) {
											player.sendMessage(Msgs.Portals_NotEnoughRoom.getString());
											PortalManager.delPortal(portal);
										} else {
											player.getInventory().remove(e.getItem());
											player.updateInventory();
											portal.createPortal();
											player.sendMessage(Msgs.Portals_PortalOpened.getString()); // 传送门已经打开
											portal.playEffect();
											Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PortablePortals.me, new Runnable() {
												@Override
												public void run() {
													if (player.isOnline() && player.getInventory().firstEmpty() != -1)
														player.getInventory().addItem(portal.getItem());
													else
														portal.getLocation().getWorld().dropItemNaturally(portal.getLocation(), portal.getItem());
													player.updateInventory();
													player.sendMessage(Msgs.Portals_PortalClosed.getString()); // 发出传送门关闭的消息
													portal.removePortal(); // 移除传送门
													PortalManager.delPortal(portal); // 删除传送门
												}
											}, Settings.stayOpenTime());
										}
									}
								} else
									player.sendMessage(Msgs.Portals_NeedItem.getString(Settings.getItemRequired().name()));
							}
						}
						e.setUseInteractedBlock(Result.DENY);
						e.setUseItemInHand(Result.DENY);
						e.setCancelled(true);
					}
	}

	public boolean hasItemRequired(Player player) {
		if (!Settings.isItemRequired()){
			return true;
		}else if (player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}else if (player.getInventory().contains(Settings.getItemRequired())) {
			for (ItemStack RequireItem : player.getInventory().getContents()) {
				if(RequireItem == null) continue;
				if (RequireItem.getType().equals(Settings.getItemRequired())) {
					if (RequireItem.getAmount() != 1){
						RequireItem.setAmount(RequireItem.getAmount() - 1);
					}else{
						player.getInventory().remove(RequireItem);
					}
					player.updateInventory();
					return true;
				}
			}
		}
		return false;
	}
}
