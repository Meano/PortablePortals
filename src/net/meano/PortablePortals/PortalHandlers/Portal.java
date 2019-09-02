package net.meano.PortablePortals.PortalHandlers;

import net.meano.PortablePortals.PortablePortals;
import net.meano.PortablePortals.Util.Direction;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Portal {
	private Location location;
	private ItemStack item;
	private Location target;
	private Player thrower;
	private int effectTimer;
	private int effect;
	private boolean direction = false;

	public Portal(Location location, Location target, ItemStack item, Player thrower) {
		this.location = location;
		this.target = target;
		this.item = item;
		this.thrower = thrower;
	}

	public boolean canCreatePortal() {
		Location checkloc = this.location.clone();
		String d = Direction.getCardinalDirection(this.thrower);
		for (Entity entity : checkloc.getChunk().getEntities()) {
			if (entity instanceof Player) {
				if (entity.getLocation().getBlock().getLocation().equals(checkloc.getBlock().getLocation())) {
					return false;
				}
			}
		}
		if (d.contains("East") || d.contains("West")) {
			if (checkloc.getWorld().getBlockAt((int) checkloc.getX() - 1, (int) checkloc.getY(), (int) checkloc.getZ()).getType().isAir())
				if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY(), (int) checkloc.getZ()).getType().isAir())
					if (checkloc.getWorld().getBlockAt((int) checkloc.getX() + 1, (int) checkloc.getY(), (int) checkloc.getZ()).getType().isAir())
						if (checkloc.getWorld().getBlockAt((int) checkloc.getX() - 1, (int) checkloc.getY() + 1, (int) checkloc.getZ()).getType().isAir())
							if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY() + 1, (int) checkloc.getZ()).getType().isAir())
								if (checkloc.getWorld().getBlockAt((int) checkloc.getX() + 1, (int) checkloc.getY() + 1, (int) checkloc.getZ()).getType().isAir())
									if (checkloc.getWorld().getBlockAt((int) checkloc.getX() - 1, (int) checkloc.getY() + 2, (int) checkloc.getZ()).getType().isAir())
										if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY() + 2, (int) checkloc.getZ()).getType().isAir())
											if (checkloc.getWorld().getBlockAt((int) checkloc.getX() + 1, (int) checkloc.getY() + 2, (int) checkloc.getZ()).getType().isAir())
												return true;
		} else if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY(), (int) checkloc.getZ() - 1).getType().isAir())
			if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY(), (int) checkloc.getZ()).getType().isAir())
				if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY(), (int) checkloc.getZ() + 1).getType().isAir())
					if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY() + 1, (int) checkloc.getZ() - 1).getType().isAir())
						if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY() + 1, (int) checkloc.getZ()).getType().isAir())
							if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY() + 1, (int) checkloc.getZ() + 1).getType().isAir())
								if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY() + 2, (int) checkloc.getZ() - 1).getType().isAir())
									if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY() + 2, (int) checkloc.getZ()).getType().isAir())
										if (checkloc.getWorld().getBlockAt((int) checkloc.getX(), (int) checkloc.getY() + 2, (int) checkloc.getZ() + 1).getType().isAir())
											return true;
		return false;
	}

	public void createPortal() {
		Location location2 = this.location.clone();
		String d = Direction.getCardinalDirection(this.thrower);
		if (d.contains("East") || d.contains("West")) {
			location2.getWorld().getBlockAt((int) location2.getX() - 1, (int) location2.getY(), (int) location2.getZ()).setType(Material.COBBLESTONE_WALL);
			location2.getWorld().getBlockAt((int) location2.getX() + 1, (int) location2.getY(), (int) location2.getZ()).setType(Material.COBBLESTONE_WALL);
			location2.getWorld().getBlockAt((int) location2.getX() - 1, (int) location2.getY() + 1, (int) location2.getZ()).setType(Material.COBBLESTONE_WALL);
			location2.getWorld().getBlockAt((int) location2.getX() + 1, (int) location2.getY() + 1, (int) location2.getZ()).setType(Material.COBBLESTONE_WALL);
			location2.getWorld().getBlockAt((int) location2.getX() - 1, (int) location2.getY() + 2, (int) location2.getZ()).setType(Material.STONE_BRICK_SLAB);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 2, (int) location2.getZ()).setType(Material.STONE_BRICK_SLAB);
			location2.getWorld().getBlockAt((int) location2.getX() + 1, (int) location2.getY() + 2, (int) location2.getZ()).setType(Material.STONE_BRICK_SLAB);
		} else {
			this.direction = true;
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY(), (int) location2.getZ() - 1).setType(Material.COBBLESTONE_WALL);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY(), (int) location2.getZ() + 1).setType(Material.COBBLESTONE_WALL);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 1, (int) location2.getZ() - 1).setType(Material.COBBLESTONE_WALL);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 1, (int) location2.getZ() + 1).setType(Material.COBBLESTONE_WALL);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 2, (int) location2.getZ() - 1).setType(Material.STONE_BRICK_SLAB);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 2, (int) location2.getZ()).setType(Material.STONE_BRICK_SLAB);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 2, (int) location2.getZ() + 1).setType(Material.STONE_BRICK_SLAB);
		}
	}

	/**
	 * @return the item
	 */
	public ItemStack getItem() {
		return this.item;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * @return the target
	 */
	public Location getTarget() {
		if ((this.target != null) && !this.target.getChunk().isLoaded())
			this.target.getChunk().load();
		return this.target.clone().add(0.5, 0.1, 0.5);
	}

	/**
	 * @return the thrower
	 */
	public Player getThrower() {
		return this.thrower;
	}

	public boolean isBlock(Location loc) {
		loc = PortalManager.getRoundedLocation(loc);
		for (int i = 0; i != 3; i++)
			for (int j = -1; j != 2; j++)
				if (((this.location.getBlockX() + j) == loc.getBlockX()) && ((this.location.getBlockY() + i) == loc.getBlockY()) && (this.location.getBlockZ() == loc.getBlockZ()))
					return true;
				else if ((this.location.getBlockX() == loc.getBlockX()) && ((this.location.getBlockY() + i) == loc.getBlockY()) && ((this.location.getBlockZ() + j) == loc.getBlockZ()))
					return true;
		return false;
	}

	public void playEffect() {
		final Location location2 = this.location.clone();
		this.effectTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(PortablePortals.me, new Runnable() {
			@Override
			public void run() {
				if (Portal.this.effect != 10) {
					location2.getWorld().playEffect(location2.clone().add(0, 1, 0), Effect.ENDER_SIGNAL, 5);
					location2.getWorld().playEffect(location2.clone(), Effect.ENDER_SIGNAL, 5);
					Portal.this.effect++;
				} else
					Bukkit.getScheduler().cancelTask(Portal.this.effectTimer);
			}
		}, 0, 20);
	}

	public void removePortal() {
		Location location2 = this.location.clone();
		if (!this.direction) {
			location2.getWorld().getBlockAt((int) location2.getX() - 1, (int) location2.getY(), (int) location2.getZ()).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY(), (int) location2.getZ()).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX() + 1, (int) location2.getY(), (int) location2.getZ()).setType(Material.AIR);

			location2.getWorld().getBlockAt((int) location2.getX() - 1, (int) location2.getY() + 1, (int) location2.getZ()).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 1, (int) location2.getZ()).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX() + 1, (int) location2.getY() + 1, (int) location2.getZ()).setType(Material.AIR);

			location2.getWorld().getBlockAt((int) location2.getX() - 1, (int) location2.getY() + 2, (int) location2.getZ()).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 2, (int) location2.getZ()).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX() + 1, (int) location2.getY() + 2, (int) location2.getZ()).setType(Material.AIR);
		} else {
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY(), (int) location2.getZ() - 1).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY(), (int) location2.getZ()).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY(), (int) location2.getZ() + 1).setType(Material.AIR);

			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 1, (int) location2.getZ() - 1).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 1, (int) location2.getZ()).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 1, (int) location2.getZ() + 1).setType(Material.AIR);

			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 2, (int) location2.getZ() - 1).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 2, (int) location2.getZ()).setType(Material.AIR);
			location2.getWorld().getBlockAt((int) location2.getX(), (int) location2.getY() + 2, (int) location2.getZ() + 1).setType(Material.AIR);
		}
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(ItemStack item) {
		this.item = item;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(Location target) {
		this.target = target;
	}

	/**
	 * @param thrower
	 *            the thrower to set
	 */
	public void setThrower(Player thrower) {
		this.thrower = thrower;
	}
}
