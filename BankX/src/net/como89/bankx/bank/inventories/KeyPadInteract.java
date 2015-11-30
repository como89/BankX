package net.como89.bankx.bank.inventories;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.BankAccount;
import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;
import net.como89.bankx.bank.Utils;

public class KeyPadInteract extends InventoryInteract {

	public KeyPadInteract(ManagerAccount managerAccount,ItemMeta itemM, Player player, Inventory inv) {
		super(managerAccount);
		super.itemM = itemM;
		super.player = player;
		super.inv = inv;
	}

	@Override
	public void interact() {
		
		String itemDisplayName = itemM.getDisplayName();
		String typeInventory = inv.getTitle();
		boolean buttonNumber = false;
		
		try {
			int nbAmountOperation = Integer.parseInt(itemDisplayName.replaceAll(ChatColor.AQUA + "", ""));
			buttonNumber = true;
			switch(nbAmountOperation) {
			
			case 0 :
				if(managerAccount.getAmountInOperation(player.getUniqueId()) == null){
					player.sendMessage(ChatColor.RED + Language.NO_BEGIN_ZERO.getMsg(managerAccount.getPlugin().getLanguage()));
					break;
				}
			default:
				changeAmountOperation(player.getUniqueId(), "" + nbAmountOperation, inv);
				player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
			}
		} catch (NumberFormatException e){}
		
		if(buttonNumber)
			return;
		
		if(itemDisplayName.contains("Ok")){
			if(managerAccount.getAmountInOperation(player.getUniqueId()) == null || managerAccount.getAmountInOperation(player.getUniqueId()).isEmpty()){
				player.sendMessage(ChatColor.RED + Language.OPERATION_NOT_EMPTY.getMsg(managerAccount.getPlugin().getLanguage()));
				return;
			}
			if(typeInventory.contains("Deposit")){
				double amountInOperation = Double.parseDouble(managerAccount.getAmountInOperation(player.getUniqueId()));
				if(managerAccount.getAmountPocket(player.getUniqueId()) >= amountInOperation){
				managerAccount.addAmountBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId()), amountInOperation);
				managerAccount.removeAmountPocket(player.getUniqueId(), amountInOperation);
				managerAccount.clearAmountInOperation(player.getUniqueId());
				player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.ADD_MONEY_BANK.getMsg(managerAccount.getPlugin().getLanguage()), player.getName(), amountInOperation,""));
				callCloseInventory(player);
				}
				else{
					player.sendMessage(ChatColor.RED + Language.NOT_ENOUGH_MONEY_IN_WALLET.getMsg(managerAccount.getPlugin().getLanguage()));
					callCloseInventory(player);
				}
			}
			else if(typeInventory.contains("Withdraw")){
				double amountInOperation = Double.parseDouble(managerAccount.getAmountInOperation(player.getUniqueId()));
				if(managerAccount.getAmountInBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId())) >= amountInOperation){
					managerAccount.addAmountPocket(player.getUniqueId(), amountInOperation);
					managerAccount.removeAmountBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId()), amountInOperation);
					managerAccount.clearAmountInOperation(player.getUniqueId());
					player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.REMOVE_MONEY_BANK.getMsg(managerAccount.getPlugin().getLanguage()), player.getName(), amountInOperation,""));
					callCloseInventory(player);
				}
				else{
					player.sendMessage(ChatColor.RED + managerAccount.replaceTag(Language.NOT_ENOUGH_MONEY_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()),"",0.0,managerAccount.getSelectedBankAccount(player.getUniqueId())));
					callCloseInventory(player);
				}
			 }
			else if (typeInventory.contains("Transfer to")){
				String name = inv.getTitle().replace("Transfer to ", "").replace(" bank", "").replace(""+ChatColor.AQUA, "").replace(""+ChatColor.GOLD, "");
				double amountInOperation = Double.parseDouble(managerAccount.getAmountInOperation(player.getUniqueId()));
				BankAccount bankAccount = managerAccount.getBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId()));
				if(managerAccount.getAmountInBankAccount(bankAccount.getName()) >= amountInOperation){
					if(inv.getTitle().endsWith("bank")){
						managerAccount.addAmountBankAccount(name, amountInOperation);
					} else {
						Player playerChoose = Utils.getOnlinePlayer(name);
						managerAccount.addAmountPocket(playerChoose.getUniqueId(), amountInOperation);
						playerChoose.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.RECEIVE_MONEY_FROM_PLAYER.getMsg(managerAccount.getPlugin().getLanguage()), player.getName(), amountInOperation, name));
					}
					managerAccount.removeAmountBankAccount(bankAccount.getName(), amountInOperation);
					managerAccount.clearAmountInOperation(player.getUniqueId());
					player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.REMOVE_MONEY_BANK.getMsg(managerAccount.getPlugin().getLanguage()), player.getName(), amountInOperation, (inv.getTitle().endsWith("bank")?" bank.":" wallet.")));
					callCloseInventory(player);
				}
				else{
					player.sendMessage(ChatColor.RED + managerAccount.replaceTag(Language.NOT_ENOUGH_MONEY_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()),"",0.0,managerAccount.getSelectedBankAccount(player.getUniqueId())));
					callCloseInventory(player);
				}
			}
		} else if(itemDisplayName.contains("Correction")){
			managerAccount.clearAmountInOperation(player.getUniqueId());
			changeAmountOperation(player.getUniqueId(), "", inv);
			player.sendMessage(ChatColor.DARK_AQUA + Language.CLEAR_AMOUNT_OPERATION.getMsg(managerAccount.getPlugin().getLanguage()));
		}
		else if(itemDisplayName.contains("Cancel")){
			callCloseInventory(player);
			callOpenInventory(player,InventoriesBank.initialiseInventoryMenu(managerAccount, managerAccount.getSelectedBankAccount(player.getUniqueId())));
		}
		
	}
	
	private void changeAmountOperation(UUID playerUUID, String amount,Inventory inv){
		ItemStack item = inv.getItem(42);
		if(item != null){
		ItemMeta meta = item.getItemMeta();
		if(meta.getDisplayName().contains("Ok")){
			managerAccount.addAmountInOperation(playerUUID, amount);
			String newDisplay = meta.getDisplayName().substring(0,4);
			String amountInOperation = managerAccount.getAmountInOperation(playerUUID);
			if(amountInOperation != null) {
				newDisplay += " - " + amountInOperation + " " +  managerAccount.getPlugin().getRepresentMoney();
			}
			meta.setDisplayName(newDisplay);
			item.setItemMeta(meta);
			inv.setItem(42, item);
		}
		}
	}
	
	private void callCloseInventory(final Player player){
		Bukkit.getScheduler().scheduleSyncDelayedTask(managerAccount.getPlugin(), new Runnable(){
			@Override
			public void run() {
				player.closeInventory();
			}	
		},1L);
	}
	
	private void callOpenInventory(final Player player, final Inventory inv){
		Bukkit.getScheduler().runTaskLater(managerAccount.getPlugin(), new Runnable(){
			@Override
			public void run() {
				player.openInventory(inv);
			}}, 2L);
	}

}
