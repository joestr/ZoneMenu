/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.joestr.zonemenu.command;

import at.joestr.javacommon.configuration.LanguageConfiguration;
import at.joestr.javacommon.spigotutils.MessageHelper;
import at.joestr.zonemenu.configuration.CurrentEntries;
import at.joestr.zonemenu.util.ZoneMenuManager;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

/**
 *
 * @author Joel
 */
public class CommandZoneRemovemember implements TabExecutor {

  private static final Logger LOG = Logger.getLogger(CommandZoneRemovemember.class.getName());

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      new MessageHelper(LanguageConfiguration.getInstance().getResolver())
        .locale(Locale.ENGLISH)
        .path(CurrentEntries.LANG_GEN_NOT_A_PLAYER.toString())
        .prefixPath(CurrentEntries.LANG_PREFIX.toString())
        .showPrefix(true)
        .receiver(sender)
        .send();
      return true;
    }

    if (args.length != 2) {
      return false;
    }

    String zoneName = args[1];
    Player player = (Player) sender;

    if (!ZoneMenuManager.getInstance().zoneMenuPlayers.containsKey(player)) {

      new MessageHelper(LanguageConfiguration.getInstance().getResolver())
        .locale(Locale.ENGLISH)
        .path(CurrentEntries.LANG_GEN_NO_ZONE.toString())
        .prefixPath(CurrentEntries.LANG_PREFIX.toString())
        .showPrefix(true)
        .receiver(sender)
        .send();
      return true;
    }

    ZoneMenuManager.getInstance().futuristicRegionProcessing(player, true, (List<ProtectedRegion> t, Throwable u) -> {
      ProtectedRegion protectedregion = null;

      if (t.isEmpty()) {
        new MessageHelper(LanguageConfiguration.getInstance().getResolver())
          .locale(Locale.ENGLISH)
          .path(CurrentEntries.LANG_GEN_NO_ZONE.toString())
          .prefixPath(CurrentEntries.LANG_PREFIX.toString())
          .showPrefix(true)
          .receiver(sender)
          .send();
        return;
      }

      for (ProtectedRegion pr : t) {
        if (pr.getId().equalsIgnoreCase(zoneName)) {
          protectedregion = pr;
        }
      }

      if (protectedregion == null) {
        new MessageHelper(LanguageConfiguration.getInstance().getResolver())
          .locale(Locale.ENGLISH)
          .path(CurrentEntries.LANG_GEN_NOT_EXISTING_ZONE.toString())
          .prefixPath(CurrentEntries.LANG_PREFIX.toString())
          .showPrefix(true)
          .receiver(sender)
          .send();
        return;
      }

      DefaultDomain domainmembers = protectedregion.getMembers();

      if (!domainmembers.contains(
        ZoneMenuManager.getInstance().getWorldGuardPlugin().wrapOfflinePlayer(Bukkit.getServer().getOfflinePlayer(args[2])))) {

        new MessageHelper(LanguageConfiguration.getInstance().getResolver())
          .locale(Locale.ENGLISH)
          .path(CurrentEntries.LANG_CMD_ZONE_REMOVEMEMBER_NOT_A_MEMBER.toString())
          .prefixPath(CurrentEntries.LANG_PREFIX.toString())
          .showPrefix(true)
          .receiver(sender)
          .send();
        return;
      }

      domainmembers.removePlayer(
        ZoneMenuManager.getInstance().getWorldGuardPlugin().wrapOfflinePlayer(Bukkit.getServer().getOfflinePlayer(args[2])));
      protectedregion.setMembers(domainmembers);

      new MessageHelper(LanguageConfiguration.getInstance().getResolver())
        .locale(Locale.ENGLISH)
        .path(CurrentEntries.LANG_CMD_ZONE_REMOVEMEMBER_SUCCESS.toString())
        .prefixPath(CurrentEntries.LANG_PREFIX.toString())
        .showPrefix(true)
        .receiver(sender)
        .send();
    });

    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
    return List.of();
  }
}
