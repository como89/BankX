package net.como89.bankx.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;
import net.como89.bankx.bank.inventories.InventoriesBank;

public class PlayerInteraction implements Listener {
	
	private ManagerAccount managerAccount;
	
	public PlayerInteraction(ManagerAccount managerAccount){
		this.managerAccount = managerAccount;
	}

	@EventHandler
	public void onInteractionWithEntity(PlayerInteractEntityEvent event){
		Player p = event.getPlayer();
		Entity entity = event.getRightClicked();
		if(entity.getType() == EntityType.VILLAGER){
			if(managerAccount.getPlugin().isNpcMinecraft()){
				Villager villager = (Villager) entity;
				String villagerName = villager.getCustomName();
				if(villagerName != null){
					if(villagerName.toLowerCase().contains("banker")){	
						event.setCancelled(true);
						if(managerAccount.hasBankAccount(p.getUniqueId())){
							String bankName = "";
							if((bankName = managerAccount.getSelectedBankAccount(p.getUniqueId())) == null){
								p.openInventory(InventoriesBank.initialiseInventoryChangeBank(managerAccount.getBanksAccountOfPlayer(p.getUniqueId())));
								return;
							}
							p.openInventory(InventoriesBank.initialiseInventoryMenu(managerAccount,bankName));
							p.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.WELCOME_MSG.getMsg(managerAccount.getPlugin().getLanguage()),p.getName(),0.0,""));
						} else {
							p.sendMessage(ChatColor.RED + Language.NO_BANK_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()));
						}
					}
				}
			}
		}
	}
}
