package xyz.joestr.zonemenu.command.subcommand;

import java.util.List;
import java.util.concurrent.Executors;

import org.bukkit.entity.Player;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.util.DomainInputResolver;
import com.sk89q.worldguard.protection.util.DomainInputResolver.UserLocatorPolicy;
import com.sk89q.worldguard.util.profile.resolver.ProfileService;

import xyz.joestr.zonemenu.ZoneMenu;

public class SubCommandZoneAddmember {

	ZoneMenu plugin = null;

	public SubCommandZoneAddmember(ZoneMenu plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public void process(Player player, String[] args) {

		if (args.length != 3) {

			// Wrong usage of the /zone command
			player.sendMessage(this.plugin.colorCode('&', (String) this.plugin.configDelegate.getMap().get("head")));
			player.sendMessage(
					this.plugin.colorCode('&', (String) this.plugin.configDelegate.getMap().get("usage_message"))
							.replace("{0}", "/zone addmember <Zone> <Player>"));

			return;
		}

		plugin.futuristicRegionProcessing(player, true, (List<ProtectedRegion> t, Throwable u) -> {

			// Initialise new region
			ProtectedRegion protectedregion = null;

			if (t.isEmpty()) {

				player.sendMessage(
						this.plugin.colorCode('&', (String) this.plugin.configDelegate.getMap().get("head")));
				player.sendMessage(
						this.plugin.colorCode('&', (String) this.plugin.configDelegate.getMap().get("no_zone")));

				return;
			}

			for (ProtectedRegion pr : t) {

				if (pr.getId().equalsIgnoreCase(args[1])) {

					protectedregion = pr;
				}
			}

			// Check if region in invalid
			if (protectedregion == null) {

				player.sendMessage(
						this.plugin.colorCode('&', (String) this.plugin.configDelegate.getMap().get("head")));
				player.sendMessage(this.plugin.colorCode('&',
						((String) this.plugin.configDelegate.getMap().get("not_exisiting_zone")).replace("{0}",
								args[1])));
				return;
			}

			// Grab the members
			DefaultDomain domainmembers = protectedregion.getMembers();

			// Check if mebers list contains the specified player
			if (domainmembers.contains(
					plugin.worldGuardPlugin.wrapOfflinePlayer(plugin.getServer().getOfflinePlayer(args[2])))) {

				player.sendMessage(plugin.colorCode('&', (String) plugin.configDelegate.getMap().get("head")));
				player.sendMessage(plugin
						.colorCode('&', (String) plugin.configDelegate.getMap().get("zone_addmember_already_member"))
						.replace("{0}", args[2]));

				return;
			}

			// Initiliaze a variable for later
			final ProtectedRegion protectedregionforguava = protectedregion;

			// From:
			// https://worldguard.enginehub.org/en/latest/developer/regions/protected-region/#domains
			// (modified)
			// start ---
			// Google's Guava library provides useful concurrency classes.
			// The following executor would be re-used in your plugin.
			ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

			String[] input = new String[] { args[2] };
			ProfileService profiles = plugin.worldGuardPlugin.getProfileService();
			DomainInputResolver resolver = new DomainInputResolver(profiles, input);
			resolver.setLocatorPolicy(UserLocatorPolicy.UUID_AND_NAME);
			ListenableFuture<DefaultDomain> future = executor.submit(resolver);

			// Add a callback using Guava
			Futures.addCallback(future, new FutureCallback<DefaultDomain>() {

				public void onSuccess(DefaultDomain result) {

					protectedregionforguava.getMembers().addPlayer(
							plugin.worldGuardPlugin.wrapOfflinePlayer(plugin.getServer().getOfflinePlayer(args[2])));
					player.sendMessage(plugin.colorCode('&', (String) plugin.configDelegate.getMap().get("head")));
					player.sendMessage(
							plugin.colorCode('&', (String) plugin.configDelegate.getMap().get("zone_addmember"))
									.replace("{0}", args[2])
									.replace("{1}", args[1]));
				}

				public void onFailure(Throwable throwable) {

					player.sendMessage(plugin.colorCode('&', (String) plugin.configDelegate.getMap().get("head")));
					player.sendMessage(plugin
							.colorCode('&', (String) plugin.configDelegate.getMap().get("zone_addmember_not_existing"))
							.replace("{0}", args[2]));
				}
			});
			// end ---
		});
	}
}