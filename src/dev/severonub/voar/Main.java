package dev.severonub.voar;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	String prefix = getConfig().getString("prefix");
	String prefixplugin = "§5[§fSeVoar§5] §r";
	
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		Bukkit.getConsoleSender().sendMessage(prefixplugin + "§aPlugin habilitado, criado por severonub");
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(prefixplugin + "§cPlugin desabilitado");
		HandlerList.unregisterAll();
	}
	public static Inventory menu = Bukkit.createInventory(null, 27, "Fly");
	
	public static boolean openMenu(Player player) {
		
		ItemStack ligar = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta ligar_meta = ligar.getItemMeta();
		ligar_meta.setDisplayName("§a§lLIGAR");
		
		ligar.setItemMeta(ligar_meta);
		
		ItemStack desligar = new ItemStack(Material.REDSTONE_BLOCK,1);
		ItemMeta desligar_meta = desligar.getItemMeta();
		desligar_meta.setDisplayName("§c§lDESLIGAR");
		desligar.setItemMeta(desligar_meta);
		
		menu.setItem(11, ligar);
		menu.setItem(15, desligar);
		player.openInventory(menu);
		return true;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;
		if(command.getName().equalsIgnoreCase("voar")) {
			if(p.hasPermission("sevoar.voar")) {
				if(sender instanceof Player) {
					openMenu(p);
					p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 12.0F, 1.0F);
				}
			}else {p.sendMessage(prefix.replace('&', '§') + getConfig().getString("no-perm").replace('&', '§'));}
		}
		return false;
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();
		Inventory inventory = e.getInventory();
		if(inventory.getName().equalsIgnoreCase("Fly") && e.getSlotType() != SlotType.OUTSIDE && e.getCurrentItem().getType() != Material.AIR) {
			ItemStack clicado = e.getCurrentItem();
			if(clicado.getItemMeta().getDisplayName().equals("§c§lDESLIGAR")) {
				if(p.hasPermission("sevoar.voar")) {
					p.setAllowFlight(false);
					p.sendMessage(prefix.replace('&', '§') + getConfig().getString("desligar-fly").replace('&', '§'));
					p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 12.0F, 1.0F);
				}else {p.sendMessage(prefix.replace('&', '§') + getConfig().getString("no-perm").replace('&', '§'));}
			}else if(clicado.getItemMeta().getDisplayName().equals("§a§lLIGAR")) {
				if(p.hasPermission("sevoar.voar")) {
					p.setAllowFlight(true);
					p.sendMessage(prefix.replace('&', '§') + getConfig().getString("ligar-fly").replace('&', '§'));
					p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 12.0F, 1.0F);
				}else {p.sendMessage(prefix.replace('&', '§') + getConfig().getString("no-perm").replace('&', '§'));}
			}
			e.setCancelled(true);
			p.closeInventory();
		}else {return;}
	}
}
