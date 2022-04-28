package at.joestr.zonemenu.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Class which handles creation of subcreate corners.
 *
 * @author joestr
 * @since ${project.version}
 * @version ${project.version}
 */
public class ZoneMenuSubcreateCorner {

    Plugin plugin = null;

    /**
     * Constructor of the {@link at.joestr.zonemenu.util.ZoneMenuSubcreateCorner
     * ZoneMenuSubcreateCorner} class.
     *
     * @param plugin A {@link org.bukkit.plugin.Plugin plugin}.
     */
    public ZoneMenuSubcreateCorner(Plugin plugin) {

        this.plugin = plugin;
    }

    /**
     * Creates an indicator for a corner.
     *
     * @param location A {@link org.bukkit.Location location} where the block
     * should be.
     * @param player A {@link org.bukkit.entity.Player player} to send the
     * blockchange to.
     * @param material Which {@link org.bukkit.Material material} should be
     * used.
     * @param data A {@link java.lang.Byte byte} which represents the data.
     */
    @SuppressWarnings("deprecation")
    public void create(Location location, Player player, Material material, byte data) {

        if (location == null || player == null || material == null) {

            return;
        }

        this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, () -> {
            player.sendBlockChange(location, material.createBlockData());
        }, 10L);
    }

    /**
     * Resets a beacon shown to a player via a blockchange.
     *
     * @param location A {@linkplain org.bukkit.Location location}.
     * @param player A {@linkplain Player player} to send the blockchange to.
     */
    @SuppressWarnings("deprecation")
    public void reset(Location location, Player player) {

        if (location == null || player == null) {
            return;
        }

        player.sendBlockChange(location, location.getBlock().getBlockData());
    }
}
