package net.como89.bankx.npc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.como89.bankx.BankX;
import net.como89.bankx.bank.InventoriesBank;
import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;

public class Banker extends Trait {

	private ManagerAccount managerAccount;
	public Banker() {
		super("Banker");
		plugin = (BankX) Bukkit.getServer().getPluginManager().getPlugin("BankX");
		managerAccount = new ManagerAccount(plugin);
	}

	BankX plugin = null;
	
	@EventHandler
	public void clickEvent(NPCRightClickEvent event){
		NPC npc = event.getNPC();
		if(npc == this.getNPC())
		{
			Player player = event.getClicker();
			if(managerAccount.hasBankAccount(player.getUniqueId())){
				if(managerAccount.getSelectedBankAccount(player.getUniqueId()) == null){
					player.openInventory(InventoriesBank.initialiseInventoryChangeBank(managerAccount.getBanksAccountOfPlayer(player.getUniqueId())));
					return;
				}
				player.openInventory(InventoriesBank.initialiseInventoryMenu(managerAccount, managerAccount.getSelectedBankAccount(player.getUniqueId())));
				player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.WELCOME_MSG.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(),0.0,""));
			} else {
				player.sendMessage(ChatColor.RED + Language.NO_BANK_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()));
			}
		}
	}

}
