package net.como89.bankx.bank;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.items.Items;

public class Utils {

	public static Player getOnlinePlayer(String playerName){
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getName().equals(playerName)){
				return player;
			}
		}
		return null;
	}
	
	public static OfflinePlayer getOfflinePlayer(String playerName){
		for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
			if(player.getName().equals(playerName)){
				return player;
			}
		}
		return null;
	}
	
	public static ArrayList<String> getAllFolder(File file) {
		File[] listFile = file.listFiles();
		ArrayList<String> listFolder = new ArrayList<String>();
		for(File folder : listFile){
			if(folder != null && folder.isDirectory()){
				listFolder.add(folder.getAbsolutePath());
			}
		}
		return listFolder;
	}

	public static File[] getFilesInDirectory(File directory){
		return directory.listFiles();
	}
	
	public static String getEnchantsInString(Map<Enchantment, Integer> listEnchants){
		String listEnchantment = null;
		if(listEnchants != null && !listEnchants.isEmpty()){
			listEnchantment = "";
			for(Enchantment enchant : listEnchants.keySet()){
				listEnchantment += enchant.getName() + ":" + listEnchants.get(enchant);
			}
		}
		
		return listEnchantment;
	}
	
	public static ItemStack getItemInItemStack(Items items){
		ItemStack itemStack = new ItemStack(Material.getMaterial(items.getTypeMaterial()),items.getAmount());
		ItemMeta itemM = itemStack.getItemMeta();
		if(items.hasDisplayName())
			itemM.setDisplayName(items.getDisplayName());
		
		if(items.hasLore())
			itemM.setLore(getLoresInList(items.getListLore()));
		
		itemStack.setItemMeta(itemM);
		
		if(items.hasEnchantments())
			itemStack.addUnsafeEnchantments(getEnchantmentMap(items.getEnchantmentList()));
		
		return itemStack;
	}
	
	public static ArrayList<String> getLoresInList(String loreList){
		String[] list = loreList.split("|");
		ArrayList<String> lore = new ArrayList<String>();
		for(String listLore : list){
			lore.add(listLore);
		}
		return lore;
	}
	
	public static Map<Enchantment,Integer> getEnchantmentMap(String enchantmentList){
		String[] enchant = enchantmentList.split(":");
		Map<Enchantment,Integer> enchantmentMap = new HashMap<>();
		int i = 1;
		Enchantment enchantment = null;
		int level = 0;
		for(String ligne : enchant){
			if(i == 1){
				enchantment = Enchantment.getByName(ligne);
			}else if(i == 2) {
				level = Integer.parseInt(ligne);
			}
			
			if(i == 2){
				enchantmentMap.put(enchantment, level);
				i = 1;
			}
		}
		return enchantmentMap;
	}
	
	public static String getLoresInString(List<String> lores){
		String loreInString = null;
		
		if(lores != null && !lores.isEmpty()){
			loreInString = "";
			for(int i = 0; i < lores.size(); i++){
				loreInString += lores.get(i) + (i + 1 < lores.size()?"|":"");
			}
		}
		return loreInString;
	}
}
