package net.como89.bankx.bank.inventories;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.BankAccount;
import net.como89.bankx.bank.ManagerAccount;

public class InventoriesMenuInteract extends InventoryInteract {

	public InventoriesMenuInteract(ManagerAccount managerAccount,ItemStack item, ItemMeta itemM,Player player, Inventory inv) {
		super(managerAccount);
		super.item = item;
		super.inv = inv;
		super.itemM = itemM;
		super.player = player;
	}

	@Override
	public void interact() {
		if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Add inventory")){
			BankAccount bankAccount = managerAccount.getBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId()),player.getUniqueId());
			String inventoryName = "Inventory" + bankAccount.getBankInventories().size();
			bankAccount.modifyInventoryObjects(inventoryName,new ItemStack[9]);
			player.closeInventory();
			HashMap<String,ItemStack[]> listeInventaire = managerAccount.getAllInventoryOfTheBank(managerAccount.getSelectedBankAccount(player.getUniqueId()),player.getUniqueId());
			Set<String> inventoryNames = listeInventaire.keySet();
			inv = InventoriesBank.initialiseMultiInventory(inv,inventoryNames);
			player.openInventory(inv);
		}
		else if(item.getType() == Material.CHEST){
			player.closeInventory();
			HashMap<String,ItemStack[]> listeInventaire = managerAccount.getAllInventoryOfTheBank(managerAccount.getSelectedBankAccount(player.getUniqueId()),player.getUniqueId());
			ItemStack[] inventaire = listeInventaire.get(itemM.getDisplayName().replace(""+ChatColor.AQUA, ""));
			if(inventaire != null){
			inv = Bukkit.createInventory(null,inventaire.length,itemM.getDisplayName().replace(""+ChatColor.AQUA, ""));
			inv.setContents(inventaire);
			player.openInventory(inv);
			}
		}
	}

}
