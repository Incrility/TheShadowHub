
package me.bukkit.Incrility;


import java.util.ArrayList;

import java.util.List;


import org.bukkit.Bukkit;

import org.bukkit.ChatColor;

import org.bukkit.Material;

import org.bukkit.command.Command;

import org.bukkit.command.CommandExecutor;

import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;

import org.bukkit.event.block.Action;

import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.event.player.PlayerChangedWorldEvent;

import org.bukkit.event.player.PlayerDropItemEvent;

import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.plugin.Plugin;


public class TheShadowHubCommand implements CommandExecutor, Listener {


	private Plugin pl;

	public TheShadowHubCommand(Plugin plugin) {

		this.pl = plugin;

	}


	@Override

	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {


		if (cs instanceof Player) {


			Player p = (Player) cs;


			if (args.length == 0 && p.hasPermission(TheShadowHub.use_Permission)) {


				openLobbyCompassOnPlayer(p);


			} else if (args[0].equals("help") || args[0].equals("info")) {


				p.sendMessage(TheShadowHub.prefix + "�e---------- TheShadowHub ----------");

				p.sendMessage(TheShadowHub.prefix + "�e> TheShadowHub Plugin by �lIncrility");

				p.sendMessage(TheShadowHub.prefix + "�e----------------------------------");


			} else if (args[0].equals("get") && (p.hasPermission(TheShadowHub.get_Permission) || p.hasPermission(TheShadowHub.admin_Permission))) {


				if (pl.getConfig().getBoolean("only-allow-command-lc_get-in-worlds") && !pl.getConfig().getStringList("get-compass-worlds").contains(p.getWorld().getName())) {

					p.sendMessage(TheShadowHub.prefix + "�eThe ShadowHub is disabled for this world!");

					return true;

				}


				p.sendMessage(TheShadowHub.prefix + "�eGiving you your Server Compass!");


				ItemStack compass = new ItemStack(Material.COMPASS);

				ItemMeta compassMeta = compass.getItemMeta();

				compassMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("compass-name")));

				compass.setItemMeta(compassMeta);

				p.getInventory().addItem(compass);


			} else if (args[0].equals("reload") && (p.hasPermission(TheShadowHub.get_Permission) || p.hasPermission(TheShadowHub.admin_Permission))) {


				p.sendMessage(TheShadowHub.prefix + "�aConfig reloaded!");

				pl.reloadConfig();


			} else {


				cs.sendMessage(TheShadowHub.prefix + "�cNot valid arguments or to few permissions.");

			}


		} else {

			cs.sendMessage(TheShadowHub.prefix + "�cYou're not a valid player.");

		}


		return true;


	}


	@EventHandler

	public void onClick(PlayerInteractEvent event) {


		if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {


			Player p = event.getPlayer();


			try {


				if (p.getInventory().getItemInMainHand().getType() == Material.COMPASS && p.hasPermission(TheShadowHub.use_Permission)) {


					if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("compass-name")))) {


						event.setCancelled(true);

						openLobbyCompassOnPlayer(p);


					}


				}


			} catch (Exception e) {


			}


		}


	}


	public void openLobbyCompassOnPlayer(Player p) {


		Inventory inv = Bukkit.createInventory(null, pl.getConfig().getInt("inventory-lines-amount") * 9, ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("compass-inventory-name")));


		List<String> options = pl.getConfig().getStringList("options");


		for (String option : options) {


			@SuppressWarnings("deprecation")

			ItemStack istack = new ItemStack(Material.getMaterial(pl.getConfig().getInt("data." + option + ".item")));

			ItemMeta imeta = istack.getItemMeta();

			imeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("data." + option + ".name")));

			List<String> lores = pl.getConfig().getStringList("data." + option + ".lore");

			List<String> newLores = new ArrayList<String>();

			for (String lore : lores) {

				newLores.add(ChatColor.translateAlternateColorCodes('&', lore));

			}

			imeta.setLore(newLores);

			istack.setItemMeta(imeta);


			inv.setItem(pl.getConfig().getInt("data." + option + ".position-in-inventory"), istack);


		}


		p.openInventory(inv);


	}


	@SuppressWarnings("deprecation")

	@EventHandler

	public void onClickOnItem(InventoryClickEvent event) {


		if (event.getWhoClicked() instanceof Player) {


			Player p = (Player) event.getWhoClicked();


			if (event.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("compass-inventory-name"))) && event.getSlot() == event.getRawSlot()) {


				event.setCancelled(true);


				try {


					if (event.getCurrentItem().getType() != Material.AIR && event.getCurrentItem().hasItemMeta()) {


						ItemStack invstack = event.getCurrentItem();


						List<String> options = pl.getConfig().getStringList("options");


						for (String option : options) {


							if (invstack.getType() == Material.getMaterial(pl.getConfig().getInt("data." + option + ".item"))) {

								if (invstack.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("data." + option + ".name")))) {


									String cmd = pl.getConfig().getString("data." + option + ".cmd");


									if (pl.getConfig().getBoolean("sound-effect")) {


									


									}


									if (pl.getConfig().getBoolean("data." + option + ".executedByPlayer")) {


										Bukkit.dispatchCommand(p, cmd);


									} else {


										System.out.println(TheShadowHub.prefix + "Next command executed by TheShadowHub Plugin of Player " + p.getName());

										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%n%", p.getName()));


									}


									break;


								}

							}


						}


						p.closeInventory();


					}


				} catch (Exception e) {


				}


			}


		}


	}


	@EventHandler

	public void onDrop(PlayerDropItemEvent event) {


		ItemStack compass = event.getItemDrop().getItemStack();


		if (compass.getType() == Material.COMPASS && compass.hasItemMeta() && compass.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("compass-name")))) {


			if (pl.getConfig().getBoolean("can-drop-compass") == false) {


				event.setCancelled(true);


			}


		}


	}


	@EventHandler

	public void onJoin(PlayerJoinEvent event) {


		Player p = event.getPlayer();


		if (pl.getConfig().getBoolean("get-compass-on-join")) {


			ItemStack compass = configuratedCompass();


			if (p.getInventory().contains(compass) == false) {


				if (pl.getConfig().getInt("get-compass-on-join-slot") >= 0) {


					p.getInventory().setItem(pl.getConfig().getInt("get-compass-on-join-slot"), compass);


				} else {


					p.getInventory().addItem(compass);


				}


			}


		}


	}


	@EventHandler

	public void onChangeWorld(PlayerChangedWorldEvent event) {


		Player p = event.getPlayer();


		if (pl.getConfig().getStringList("get-compass-worlds").contains(p.getWorld().getName())) {


			if (p.getInventory().contains(configuratedCompass()) == false) {


				p.getInventory().addItem(configuratedCompass());


			}


		} else {


			p.getInventory().remove(Material.COMPASS);


		}


	}


	public ItemStack configuratedCompass() {


		ItemStack compass = new ItemStack(Material.COMPASS);

		ItemMeta compassMeta = compass.getItemMeta();

		compassMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("compass-name")));

		compass.setItemMeta(compassMeta);


		return compass;


	}


}


