package xyz.joestr.zonemenu.subcommand;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import xyz.joestr.zonemenu.ZoneMenu;
import xyz.joestr.zonemenu.enumeration.ZoneMenuTool;

public class SubCommandZoneSign {
	
	ZoneMenu plugin = null;
	
	public SubCommandZoneSign(ZoneMenu plugin) {
		this.plugin = plugin;
	}
	
	public void process(Player player) {
		// Check if players inventory contains a stick
		if (!player.getInventory().contains(Material.STICK)) {

			// Add a stick to players inventory
			player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.STICK, 1) });
		}

		// Send player a message
		player.sendMessage(this.plugin.colorCode('&', (String) this.plugin.configDelegate.getMap().get("head")));
		player.sendMessage(this.plugin.colorCode('&', (String) this.plugin.configDelegate.getMap().get("zone_sign")));

		// Put player into a map where his name and the current action are stored
		this.plugin.tool.put(player, ZoneMenuTool.SIGN);
	}
}
