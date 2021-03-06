package com.palmergames.bukkit.towny.tasks;

import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockType;
import com.palmergames.bukkit.towny.utils.CombatUtil;

public class HealthRegenTimerTask extends TownyTimerTask {

	private final Server server;

	public HealthRegenTimerTask(Towny plugin, Server server) {

		super(plugin);
		this.server = server;
	}

	@Override
	public void run() {

		if (TownyAPI.getInstance().isWarTime())
			return;

		for (Player player : server.getOnlinePlayers()) {
			if (player.getHealth() <= 0)
				continue;
			
			// Is wilderness
			if (TownyAPI.getInstance().isWilderness(player.getLocation()))
				continue;
			
				incHealth(player);
		}
	}

	public void incHealth(Player player) {
		
		// Heal while in town.
		double currentHP = player.getHealth();
		double maxHP = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		if (currentHP < maxHP) {
			player.setHealth(Math.min(maxHP, ++currentHP));

			// Raise an event so other plugins can keep in sync.
			Bukkit.getServer().getPluginManager().callEvent(new EntityRegainHealthEvent(player, currentHP, RegainReason.REGEN));
		}
	}
}
