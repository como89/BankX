package net.como89.bankx.bank.inventories;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;

public class BankInteract extends InventoryInteract {

	public BankInteract(ManagerAccount managerAccount,ItemMeta itemM, Player player, Inventory inv) {
		super(managerAccount);
		super.inv = inv;
		super.itemM = itemM;
		super.player = player;
	}

	@Override
	public void interact() {
		if (itemM.getDisplayName().contains("Items")) {
			if(player.getGameMode() == GameMode.CREATIVE){
				player.sendMessage(ChatColor.RED + Language.NO_CREATIVE.getMsg(managerAccount.getPlugin().getLanguage()));
				return;
			}	
			HashMap<String,ItemStack[]> listeInventaire = managerAccount.getAllInventoryOfTheBank(managerAccount.getSelectedBankAccount(player.getUniqueId()),player.getUniqueId());
			Set<String> inventoryNames = listeInventaire.keySet();
			inv = InventoriesBank.initialiseMultiInventory(inv,inventoryNames);
			player.closeInventory();
			player.openInventory(inv);
		} else if (itemM.getDisplayName().contains("Money")) {
			player.closeInventory();
			player.openInventory(InventoriesBank.initialiseInventoryMoney(managerAccount, managerAccount.getSelectedBankAccount(player.getUniqueId()),player.getUniqueId()));
		} else if(itemM.getDisplayName().contains("BookLog")){
			player.closeInventory();
			managerAccount.giveBookLog(player);
		} else if(itemM.getDisplayName().contains("Manage BankAccounts")){
			managerAccount.selectBankAccount(player.getUniqueId(), "");
			player.closeInventory();
			player.openInventory(InventoriesBank.initialiseInventoryChangeBank(managerAccount.getBanksAccountOfPlayer(player.getUniqueId())));
		}
	}

}
