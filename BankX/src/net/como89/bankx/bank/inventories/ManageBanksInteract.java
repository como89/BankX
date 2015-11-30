package net.como89.bankx.bank.inventories;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.BankAccount;
import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;

public class ManageBanksInteract extends InventoryInteract {

	public ManageBanksInteract(ManagerAccount managerAccount, ItemMeta itemM, Player player) {
		super(managerAccount);
		super.itemM = itemM;
		super.player = player;
	}

	@Override
	public void interact() {
		if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Add Bank")){
			ArrayList<BankAccount> listAccount = managerAccount.getBanksAccountOfPlayer(player.getUniqueId());
			managerAccount.createBankAccount(player.getUniqueId(), "bank" + listAccount.size());
			player.closeInventory();
			player.openInventory(InventoriesBank.initialiseInventoryChangeBank(listAccount));
		} else {
		String bankName = itemM.getDisplayName().replace(""+ChatColor.GREEN, "");
		player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.WELCOME_MSG.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(),0.0,""));
		managerAccount.selectBankAccount(player.getUniqueId(), bankName);
		player.closeInventory();
		player.openInventory(InventoriesBank.initialiseInventoryMenu(managerAccount, bankName));
		}
	}

}
