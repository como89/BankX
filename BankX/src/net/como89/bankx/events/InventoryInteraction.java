package net.como89.bankx.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.BankAccount;
import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;
import net.como89.bankx.bank.inventories.BankInteract;
import net.como89.bankx.bank.inventories.InventoriesMenuInteract;
import net.como89.bankx.bank.inventories.InventoryInteract;
import net.como89.bankx.bank.inventories.KeyPadInteract;
import net.como89.bankx.bank.inventories.ManageBanksInteract;
import net.como89.bankx.bank.inventories.MoneyInteract;
import net.como89.bankx.bank.inventories.TransfertPageInteract;
import net.como89.bankx.bank.inventories.TransfertTypeInteract;
import net.como89.bankx.tasks.DatabaseTask;
import net.como89.bankx.tasks.databaserequest.ChangeInventory;
import net.como89.bankx.tasks.databaserequest.UpdateInventoryName;

public class InventoryInteraction implements Listener {
	
	private ManagerAccount managerAccount;
	
	public InventoryInteraction(ManagerAccount managerAccount){
		this.managerAccount = managerAccount;
	}

	@EventHandler
	public void onInventoryClosed(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
			if (event.getInventory().getTitle().contains("Deposit") 
					|| event.getInventory().getTitle().contains("Withdraw")) {
				closeInventory(player);
			}
			else if(event.getInventory().getTitle().contains("Transfer to")){
				managerAccount.clearPlayerInventories(player.getName());
				closeInventory(player);
			}
			else{
			BankAccount bankAccount = managerAccount.getBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId()),player.getUniqueId());
			boolean existInventory = false;
			if(bankAccount == null)
				return;
			for(String inventoryName : bankAccount.getBankInventories().keySet()){
				if(inventoryName.equals(event.getInventory().getTitle())){
					existInventory = true;
					break;
				}
			}
				if(existInventory){
					bankAccount.putInventoriesItems(event.getInventory().getTitle(), event.getInventory().getContents(),event.getInventory().getMaxStackSize());
					DatabaseTask dt = new DatabaseTask("Database",0,new ChangeInventory(player.getUniqueId(),event.getInventory().getTitle(),bankAccount.getInventoryItems(event.getInventory().getTitle())));	
					managerAccount.getTaskManager().registerTask(dt);
					closeInventory(player);
				}
			}
	}
	
	@EventHandler
	public void onPlayerClickInventory(InventoryClickEvent event) {
		HumanEntity human = event.getWhoClicked();
		Player player = (Player) human;
		ItemStack item = event.getCurrentItem();
		
		if(item == null)
			return;
		
			ItemMeta itemM = item.getItemMeta();
			Inventory inv = event.getInventory();
			String invTitle = inv.getTitle();
			
			if(itemM == null || inv.getType() != InventoryType.CHEST || itemM.getDisplayName() == null)
				return;
				
				InventoryInteract inventoryInteract = null;
				
				if (invTitle.contains(ChatColor.BOLD + "Bank")) {
					inventoryInteract = new BankInteract(managerAccount, itemM, player, inv);
				}
				else if(invTitle.equals(ChatColor.GREEN + "" + ChatColor.BOLD + "Money")){
					inventoryInteract = new MoneyInteract(managerAccount, itemM, player, inv);
				}
				else if(invTitle.contains("Manage Banks")){
					inventoryInteract = new ManageBanksInteract(managerAccount, itemM, player);
				}
				else if(invTitle.contains("Deposit") || invTitle.contains("Withdraw") || invTitle.contains("Transfer to")){
					inventoryInteract = new KeyPadInteract(managerAccount, itemM, player, inv);
				}
				else if(invTitle.contains("Inventories")){
					inventoryInteract = new InventoriesMenuInteract(managerAccount, item, itemM, player, inv);
				}
				else if(invTitle.contains("Transfer Page #")){
					inventoryInteract = new TransfertPageInteract(managerAccount, item, itemM, player, inv);
				}
				else if(invTitle.contains("Transfer Type to")){
					inventoryInteract = new TransfertTypeInteract(managerAccount, item, player, inv);
				}
				
				if(inventoryInteract != null){
					inventoryInteract.interact();
					event.setCancelled(true);
				}
	}
	
	private void closeInventory(Player player){
		managerAccount.clearAmountInOperation(player.getUniqueId());
		player.sendMessage(ChatColor.DARK_AQUA + Language.CLOSE_BANK_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()));
	}
}
