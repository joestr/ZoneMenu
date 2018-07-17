package xyz.joestr.zonemenu.event.playerinteract;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import xyz.joestr.zonemenu.ZoneMenu;
import xyz.joestr.zonemenu.util.ZoneMenuPlayer;
import xyz.joestr.zonemenu.util.ZoneMenuSignType;
import xyz.joestr.zonemenu.util.ZoneMenuToolType;

/**
 * Class which handles player interaction with blocks
 * 
 * @author joestr
 * @since build_1
 * @version ${project.version}
 */
public class SubcreatePlayerInteract implements Listener {

	private ZoneMenu plugin;

	public SubcreatePlayerInteract(ZoneMenu zonemenu) {

		this.plugin = zonemenu;
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {

		// Grab player from the event
		Player player = event.getPlayer();

		// If the player is not in the map ...
		if(!this.plugin.zoneMenuPlayers.containsKey(player)) {
			
			// ... do not proceed.
			return;
		}
		
		ZoneMenuPlayer zoneMenuPlayer = this.plugin.zoneMenuPlayers.get(player);
		
		if ((player.getInventory().getItemInMainHand().getType() != Material.STICK)
				|| (this.plugin.zoneMenuPlayers.get(player).getToolType() != ZoneMenuToolType.SIGN)
				|| (this.plugin.zoneMenuPlayers.get(player).getSignType() != ZoneMenuSignType.SUBZONE)) {

			return;
		}

		// Initiliaze message
		String sign1 = "";

		// Check if event action is left-click
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			
			if (event.getClickedBlock().getLocation().equals(zoneMenuPlayer.getSubcreateCorner1())) {
				event.setCancelled(true);
				return;
			}

			zoneMenuPlayer.setSubcreateWorld(event.getClickedBlock().getLocation().getWorld());

			// Cancel the event
			event.setCancelled(true);

			// Reset corner
			this.plugin.resetSubcreateCorner(zoneMenuPlayer.getSubcreateCorner1(), player);
			
			// Set the 1st corner
			zoneMenuPlayer.setSubcreateCorner1(event.getClickedBlock().getLocation());
			
			// Create new corner
			this.plugin.createSubcreateCroner(zoneMenuPlayer.getSubcreateCorner1(), player, Material.GLOWSTONE, (byte) 0);

			// If all needed variables are set
			if ((zoneMenuPlayer.getSubcreateWorld() != null) && (zoneMenuPlayer.getSubcreateCorner1() != null) && (zoneMenuPlayer.getSubcreateCorner2() != null)) {
				// Reset beacons and create new ones
				this.plugin.resetSubcreateCorner(zoneMenuPlayer.getSubcreateCorner2(), player);
				this.plugin.createSubcreateCroner(zoneMenuPlayer.getSubcreateCorner2(), player, Material.SEA_LANTERN, (byte) 0);

				// Grab some values to work with
				World playerworld = zoneMenuPlayer.getSubcreateWorld();
				Location playerpos1 = zoneMenuPlayer.getSubcreateCorner1().clone();
				Location playerpos2 = zoneMenuPlayer.getSubcreateCorner2().clone();
				
				// Make a worldedit selection
				CuboidSelection cs = new CuboidSelection(playerworld, playerpos1, playerpos2);
				this.plugin.getWorldEditPlugin().setSelection(player, cs);

				// Set actionbar message
				sign1 = (String) plugin.configDelegate.getMap().get("event_sign_first")
						+ (String) ((String) plugin.configDelegate.getMap().get("event_sign_area")).replace("{0}",
								Integer.toString(this.plugin.getWorldEditPlugin().getSelection(player).getLength()
										* this.plugin.getWorldEditPlugin().getSelection(player).getWidth()));
			} else {
				// Set actionbar message
				sign1 = (String) plugin.configDelegate.getMap().get("event_sign_first");
			}

			// Send actiobar message to the player
			plugin.sendActionBarToPlayer(player, this.plugin.colorCode('&', sign1));
		}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if (event.getClickedBlock().getLocation().equals(zoneMenuPlayer.getSubcreateCorner2())) {
				event.setCancelled(true);
				return;
			}

			zoneMenuPlayer.setSubcreateWorld(event.getClickedBlock().getLocation().getWorld());

			// Cancel the event
			event.setCancelled(true);

			// Reset corner
			this.plugin.resetSubcreateCorner(zoneMenuPlayer.getSubcreateCorner2(), player);
			
			// Set the 2nd corner
			zoneMenuPlayer.setSubcreateCorner2(event.getClickedBlock().getLocation());
			
			// Create the corner
			this.plugin.createSubcreateCroner(zoneMenuPlayer.getSubcreateCorner2(), player, Material.SEA_LANTERN, (byte) 0);

			// If all needed variables are set
			if ((zoneMenuPlayer.getSubcreateWorld() != null) && (zoneMenuPlayer.getSubcreateCorner1() != null) && (zoneMenuPlayer.getSubcreateCorner2() != null)) {
				// Reset beacons and create new ones
				this.plugin.resetSubcreateCorner(zoneMenuPlayer.getSubcreateCorner1(), player);
				this.plugin.createSubcreateCroner(zoneMenuPlayer.getSubcreateCorner1(), player, Material.GLOWSTONE, (byte) 0);

				// Grab some values to work with
				World playerworld = zoneMenuPlayer.getSubcreateWorld();
				Location playerpos1 = zoneMenuPlayer.getSubcreateCorner1().clone();
				Location playerpos2 = zoneMenuPlayer.getSubcreateCorner2().clone();
				
				// Make a worldedit selection
				CuboidSelection cs = new CuboidSelection(playerworld, playerpos1, playerpos2);
				this.plugin.getWorldEditPlugin().setSelection(player, cs);

				// Set actionbar message
				sign1 = (String) plugin.configDelegate.getMap().get("event_sign_second")
						+ (String) ((String) plugin.configDelegate.getMap().get("event_sign_area")).replace("{0}",
								Integer.toString(this.plugin.getWorldEditPlugin().getSelection(player).getLength()
										* this.plugin.getWorldEditPlugin().getSelection(player).getWidth()));
			} else {
				// Set actionbar message
				sign1 = (String) plugin.configDelegate.getMap().get("event_sign_second");
			}

			// Send actiobar message to the player
			plugin.sendActionBarToPlayer(player, this.plugin.colorCode('&', sign1));
		}
	}
}