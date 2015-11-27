package net.como89.bankx.bank;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

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
