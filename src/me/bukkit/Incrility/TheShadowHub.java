package me.bukkit.Incrility;


import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;


public class TheShadowHub extends JavaPlugin {


	public static String prefix = "§f[§eCompass§f] ";


	public static String use_Permission = "theshadowhub.use";

	public static String get_Permission = "theshadowbuh.get";

	public static String admin_Permission = "theshadowhub.admin";


	@Override

	public void onEnable() {


		getCommand("theshadowhub").setExecutor(new TheShadowHubCommand(this));

		Bukkit.getPluginManager().registerEvents(new TheShadowHubCommand(this), this);


		getConfig().options().header(

				"#############################################" + "\n" +

				"# - TheShadowHub by Incrility - #" + "\n" +

				"#############################################" + "\n" +

				"# Option 'executedByPlayer' means:" + "\n" +

				"# > true = Command is executed as the player typed it" + "\n" +

				"# > false = Command is executed by console (%n% is player name)" + "\n" +

				"# There can be only at most 6 lines in one inventory!" + "\n" +

				"# If get-compass-on-join-slot is -1 it will add the compass in a free slot" + "\n" +

				"#############################################" + "\n"

		);


		getConfig().addDefault("compass-name", "&f&lClick to Open!");

		getConfig().addDefault("compass-inventory-name", "&a&lServer Selector");

		getConfig().addDefault("get-compass-on-join", true);

		getConfig().addDefault("get-compass-on-join-slot", 1);

		getConfig().addDefault("get-compass-worlds", new String[] { "world", "world_nether", "world_the_end" });

		getConfig().addDefault("only-allow-command-lc_get-in-worlds", true);

		getConfig().addDefault("can-drop-compass", false);

		getConfig().addDefault("inventory-lines-amount", 4);

		getConfig().addDefault("sound-effect", true);


		getConfig().addDefault("options", new String[] { "spawn", "pvp" });


		getConfig().addDefault("data.spawn.name", "&e&lSpawn / Lobby");

		getConfig().addDefault("data.spawn.lore", new String[] { "&7Click this Item to execute command", "&8&l/warp spawn", "&7another line :D" });

		getConfig().addDefault("data.spawn.item", 2);

		getConfig().addDefault("data.spawn.cmd", "warp spawn");

		getConfig().addDefault("data.spawn.executedByPlayer", true);

		getConfig().addDefault("data.spawn.position-in-inventory", 10);


		getConfig().addDefault("data.pvp.name", "&c&lPvP Zone");

		getConfig().addDefault("data.pvp.lore", new String[] { "&cD&ei&as&bc&do", "&8&l/pvp <player> join" });

		getConfig().addDefault("data.pvp.item", 267);

		getConfig().addDefault("data.pvp.cmd", "pvp %n% join");

		getConfig().addDefault("data.pvp.executedByPlayer", false);

		getConfig().addDefault("data.pvp.position-in-inventory", 19);


		getConfig().options().copyHeader(true);

		getConfig().options().copyDefaults(true);


		saveConfig();

		reloadConfig();
	
	}



	@Override

	public void onDisable() {




	}


}

