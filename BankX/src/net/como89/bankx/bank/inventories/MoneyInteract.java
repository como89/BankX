package net.como89.bankx.bank.inventories;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.ManagerAccount;

public class MoneyInteract extends InventoryInteract {

	public MoneyInteract(ManagerAccount managerAccount,ItemMeta itemM, Player player, Inventory inv) {
		super(managerAccount);
		super.inv = inv;
		super.itemM = itemM;
		super.player = player;
	}

	@Override
	public void interact() {
		if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Deposit")){
			inv = InventoriesBank.initialiseInventoryKeyPad(ChatColor.GREEN + ""+ ChatColor.BOLD + "Deposit",player.getUniqueId(),managerAccount);
		player.openInventory(inv);
		}
		else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.RED + "Withdraw")){
			inv = InventoriesBank.initialiseInventoryKeyPad(ChatColor.RED + "" + ChatColor.BOLD + "Withdraw",player.getUniqueId(),managerAccount);
		  player.openInventory(inv);;
		}
		else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Transfer")){
			managerAccount.addPlayerInventory(player.getName(),InventoriesBank.initialiseTransfertInventories(player.getName(),managerAccount));
			inv = managerAccount.getInventories(player.getName())[0];
			player.openInventory(inv);
		} else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.RED + "Cancel")){
			player.closeInventory();
			player.openInventory(InventoriesBank.initialiseInventoryMenu(managerAccount, managerAccount.getSelectedBankAccount(player.getUniqueId())));
		}
	}

}
