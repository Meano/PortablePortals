package net.meano.PortablePortals;

import java.util.Map;
import java.util.UUID;
import java.util.Date;
import java.util.HashMap;
import net.meano.PortablePortals.PortalHandlers.Portal;
import net.meano.PortablePortals.PortalHandlers.PortalManager;
import net.meano.PortablePortals.Util.Msgs;
import net.meano.PortablePortals.Util.Settings;
import net.meano.Residence.Residence;
import net.meano.Residence.protection.ClaimedResidence;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class PortalsListeners implements Listener {

	public PortablePortals plugin;

	public PortalsListeners(PortablePortals instance) {
		this.plugin = instance;
	}
	
	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		Recipe CraftRecipe = event.getRecipe();
		Material CraftType = CraftRecipe.getResult().getType();
		if (CraftType.equals(Material.BEACON)) {
			InventoryView CraftView = event.getView();
			ItemStack Star = CraftView.getItem(5);
			if (Star.getItemMeta().hasLore()) {
				event.setCancelled(true);
				return;
			}
			return;
		}
	}

	@EventHandler
	public void onPlayerTelebport(PlayerTeleportEvent event) {
		if (!event.getCause().equals(TeleportCause.PLUGIN))
			return;
		Location To = event.getTo();
		Player TeleportPlayer = event.getPlayer();

		if (TeleportPlayer.isOp())       
			return;

		ClaimedResidence ToRes = Residence.getResidenceManager().getByLoc(To);
		if (ToRes == null)
			return;
		if (ToRes.getOwner().equalsIgnoreCase(TeleportPlayer.getName()))
			return;
		if (ToRes.getPermissions().playerHas(TeleportPlayer.getName(), "tp", false))
			return;
		TeleportPlayer.sendMessage(ChatColor.RED + "抱歉，你的目的地不是你的领地，移动取消！请向领主申请TP权限方可传送。");
		event.setTo(event.getFrom());
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerAttemptToBreakPortal(BlockBreakEvent event) {
		for (Portal portal : PortalManager.getPortals())
			if (portal.isBlock(event.getBlock().getLocation()))
				event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		for (Portal portal : PortalManager.getPortals())
			if (portal.isBlock(event.getBlock().getLocation()))
				event.setCancelled(true);
	}
	
	private Map<Player, Date> PlayerSetTargetTimestampMap = new HashMap<Player, Date>();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerUsePortal(final PlayerInteractEvent e) {
		if (e.getItem() == null) {
			return;
		}
		final Player player = e.getPlayer();
		ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
		
		if(!(itemInMainHand.getType().equals(Material.NETHER_STAR) && itemInMainHand.getItemMeta().hasLore() && e.getItem().equals(itemInMainHand))) {
			return;
		}
		e.setUseInteractedBlock(Result.DENY);
		e.setUseItemInHand(Result.DENY);
		e.setCancelled(true);

		if (!e.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
			return;
		}
		
		if (!itemInMainHand.getItemMeta().getLore().get(0).startsWith(Msgs.Portals_LeftClickTo.getString().substring(0, 8))) {
			player.sendMessage(ChatColor.RED + "传送之星不能使用！请联系管理员！");
			return;
		}

		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(player.getInventory().firstEmpty() == -1){
				player.sendMessage(ChatColor.RED + "背包已满！");
				return;
			}
			Location loc = e.getClickedBlock().getLocation().clone().add(0.0, 1.0, 0.0);
			if (loc.getWorld().getName().equalsIgnoreCase("world_nether") && loc.getBlockY() > 120) {
				player.sendMessage(ChatColor.RED + "无法设定目的地！");
				return;
			}
			Location target = PortalManager.getTargetFromItem(e.getItem());
			boolean IsTargetChange = (target != null);
			if(IsTargetChange) {
				Date LastClickTimestamp = PlayerSetTargetTimestampMap.get(player);
				Date NowClickTimestamp = new Date();
				if(LastClickTimestamp == null || (NowClickTimestamp.getTime() - LastClickTimestamp.getTime() > 5000)) {
					PlayerSetTargetTimestampMap.put(player, NowClickTimestamp);
					player.sendMessage(ChatColor.GREEN + "请在5秒内再次设置以确认更改传送目的地！");
					return;
				}
				PlayerSetTargetTimestampMap.remove(player);
			}
			String locationString = PortalManager.getLocationToString(loc);
			ItemStack portal = ((PortablePortals) PortablePortals.me).PortalStarInitialize(locationString);
			if (e.getItem().getAmount() != 1) {
				e.getItem().setAmount(e.getItem().getAmount() - 1);
			}
			else {
				player.getInventory().setItemInMainHand(null);
			}

			player.updateInventory();
			player.getInventory().addItem(portal);
			player.updateInventory();
			player.sendMessage(IsTargetChange ? Msgs.Portals_TargetChange.getString() : Msgs.Portals_TargetSet.getString());
		}
		else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Location target = PortalManager.getTargetFromItem(e.getItem());
			if (target == null) {
				player.sendMessage(Msgs.Portals_NoTarget.getString());
				return;
			}
			
			if (target.getWorld().getName().equalsIgnoreCase("world_nether") && target.getBlockY() > 120) {
				player.sendMessage(ChatColor.RED + "无法到达目的地！");
				return;
			}
			
			final Portal portal = PortalManager.addPortal(e.getClickedBlock().getLocation(), target, e.getItem(), player);
			player.getWorld().createExplosion(portal.getLocation(), 0.0F, false);
			if (!portal.canCreatePortal()) {
				player.sendMessage(Msgs.Portals_NotEnoughRoom.getString());
				PortalManager.delPortal(portal);
				return;
			}
			else if(!hasItemRequired(player)) {
				player.sendMessage(Msgs.Portals_NeedItem.getString());
				PortalManager.delPortal(portal);
				return;
			}
			player.getInventory().removeItem(e.getItem());
			player.updateInventory();
			portal.createPortal();
			player.sendMessage(Msgs.Portals_PortalOpened.getString());
			portal.playEffect();
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PortablePortals.me, new Runnable() {
				@Override
				public void run() {
					UUID playerUUID = player.getUniqueId();
					Player playernow = Bukkit.getPlayer(playerUUID);
					if (playernow != null && playernow.isOnline() && playernow.getInventory().firstEmpty() != -1) {
						playernow.getInventory().addItem(portal.getItem());
						playernow.updateInventory();
						playernow.sendMessage(Msgs.Portals_PortalClosed.getString()); // 发出传送门关闭的消息
					}
					else
					{
						Item dropStar = portal.getLocation().getWorld().dropItem(portal.getTarget(), portal.getItem());
						String dropMessage = " 传送之星在: " + PortalManager.getLocationToString(dropStar.getLocation()) + " 掉落！";
						if(playernow != null) playernow.sendMessage(Msgs.Portals_PortalClosed.getString() + dropMessage);
						plugin.getLogger().info(player.getName() + dropMessage);
					}
					portal.removePortal(); // 移除传送门
					PortalManager.delPortal(portal); // 删除传送门
				}
			}, Settings.stayOpenTime());
		}
	}

	public boolean hasItemRequired(Player player) {
		if (!Settings.isItemRequired()) {
			return true;
		}
		else if (player.getGameMode().equals(GameMode.CREATIVE)) {
			return true;
		}
		else if (player.getInventory().contains(Settings.getItemRequired())) {
			for (ItemStack RequireItem : player.getInventory().getContents()) {
				if(RequireItem == null) continue;
				if (RequireItem.getType().equals(Settings.getItemRequired())) {
					if (RequireItem.getAmount() != 1) {
						RequireItem.setAmount(RequireItem.getAmount() - 1);
					}
					else {
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
