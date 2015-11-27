package net.como89.bankx.bank;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoriesBank {
	
	private static boolean initialise = false;
	public static ItemStack[] itemsChoice = new ItemStack[9];

	public static void initialiseInventory()
	{
		if(!initialise)
		{
			initialise = true;
			//Transfert type inventory
			ItemStack online = new ItemStack(Material.SKULL_ITEM,1);
			ItemMeta itemM = online.getItemMeta();
			itemM.setDisplayName(ChatColor.AQUA + "To Player (Online only)");
			online.setItemMeta(itemM);
			online.setDurability((short) SkullType.PLAYER.ordinal());
			
			ItemStack cancelButton = new ItemStack(Material.IRON_DOOR);
			itemM = cancelButton.getItemMeta();
			itemM.setDisplayName(ChatColor.RED + "Cancel");
			cancelButton.setItemMeta(itemM);
			
			ItemStack offline = new ItemStack(Material.ENDER_CHEST,1);
			itemM = offline.getItemMeta();
			itemM.setDisplayName(ChatColor.DARK_AQUA + "To Banks");
			offline.setItemMeta(itemM);
			
			itemsChoice[0] = online;
			itemsChoice[1] = offline;
			itemsChoice[8] = cancelButton;
		}
	}
	
	public static Inventory initialiseInventoryChangeBank(ArrayList<BankAccount> listBankAccount){
		int sizeInventory = 9;
		int numberOfBankAccount = listBankAccount.size();
		if(numberOfBankAccount >= 9 && numberOfBankAccount < 18){
			sizeInventory = 18;
		} else if(numberOfBankAccount >= 18 && numberOfBankAccount < 27){
			sizeInventory = 27;
		} else if(numberOfBankAccount >= 27 && numberOfBankAccount < 36) {
			sizeInventory = 36;
		} else if(numberOfBankAccount >= 36 && numberOfBankAccount < 45){
			sizeInventory = 45;
		} else if(numberOfBankAccount >= 45 && numberOfBankAccount <= 54){
			sizeInventory = 54;
		}
		Inventory inv = Bukkit.createInventory(null, sizeInventory,ChatColor.BOLD + "Manage Banks : ");
		ItemStack item;
		if(listBankAccount != null){
			for(BankAccount bankAccount : listBankAccount){
				item  = new ItemStack(Material.ENDER_CHEST);
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.setDisplayName(ChatColor.GREEN + bankAccount.getName());
				item.setItemMeta(itemMeta);
				inv.addItem(item);
			}
			ItemStack addBank = new ItemStack(Material.EMERALD);
			ItemMeta itemM = addBank.getItemMeta();
			itemM.setDisplayName(ChatColor.GREEN + "Add Bank");
			addBank.setItemMeta(itemM);
			inv.addItem(addBank);
			
		}
		return inv;
	}
	
	public static Inventory initialiseInventoryMenu(ManagerAccount managerAccount,String bankName){
		//Bank inventory
		ItemStack bankItems = new ItemStack(Material.CHEST,1);
		ItemMeta itemM = bankItems.getItemMeta();
		itemM.setDisplayName(ChatColor.GOLD + "Items");
		bankItems.setItemMeta(itemM);
		
		ItemStack bankMoney = new ItemStack(Material.EMERALD,1);
		itemM = bankMoney.getItemMeta();
		itemM.setDisplayName(ChatColor.GREEN + "Money");
		bankMoney.setItemMeta(itemM);
		
		ItemStack displayBalance = new ItemStack(Material.SKULL_ITEM);
		itemM = displayBalance.getItemMeta();
		itemM.setDisplayName(ChatColor.GOLD + "Balance");
		displayBalance.setItemMeta(itemM);
		displayBalance.setDurability((short) SkullType.PLAYER.ordinal());
		displayBalance = selectBalanceDisplay(displayBalance,managerAccount,bankName);
		
		ItemStack bookLog = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookM = (BookMeta) bookLog.getItemMeta();
		bookM.setDisplayName(ChatColor.DARK_PURPLE + "BookLog");
		bookM.setAuthor("Click on me to see your log.");
		bookLog.setItemMeta(bookM);
		
		ItemStack changeBank = new ItemStack(Material.TRIPWIRE_HOOK);
		itemM = changeBank.getItemMeta();
		itemM.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Manage BankAccounts");
		changeBank.setItemMeta(itemM);
		
		Inventory invBank = Bukkit.createInventory(null, 9, ChatColor.BOLD + "Bank menu '" 
		+ ChatColor.DARK_GREEN + ChatColor.BOLD + bankName 
		+ ChatColor.RESET + ChatColor.BOLD + "'");
		invBank.setItem(0, bankItems);
		invBank.setItem(1, bankMoney);
		invBank.setItem(6, bookLog);
		invBank.setItem(7, changeBank);
		invBank.setItem(8, displayBalance);
		
		return invBank;
	}
	
	public static Inventory initialiseInventoryMoney(ManagerAccount managerAccount,String bankName){
		//Money Inventory
		ItemStack addMoney = new ItemStack(Material.EMERALD,1);
		ItemMeta itemM = addMoney.getItemMeta();
		itemM.setDisplayName(ChatColor.GREEN + "Deposit");
		addMoney.setItemMeta(itemM);
		
		ItemStack removeMoney = new ItemStack(Material.REDSTONE,1);
		itemM = removeMoney.getItemMeta();
		itemM.setDisplayName(ChatColor.RED + "Withdraw");
		removeMoney.setItemMeta(itemM);
		
		ItemStack transfertMoney = new ItemStack(Material.IRON_INGOT);
		itemM = transfertMoney.getItemMeta();
		itemM.setDisplayName(ChatColor.GRAY + "Transfer");
		transfertMoney.setItemMeta(itemM);
		
		ItemStack displayBalance = new ItemStack(Material.SKULL_ITEM);
		itemM = displayBalance.getItemMeta();
		itemM.setDisplayName(ChatColor.GOLD + "Balance");
		displayBalance.setItemMeta(itemM);
		displayBalance.setDurability((short) SkullType.PLAYER.ordinal());
		displayBalance = selectBalanceDisplay(displayBalance,managerAccount,bankName);
		
		ItemStack cancelButton = new ItemStack(Material.WOOD_DOOR);
		itemM = cancelButton.getItemMeta();
		itemM.setDisplayName(ChatColor.RED + "Cancel");
		cancelButton.setItemMeta(itemM);
		
		Inventory invMoney = Bukkit.createInventory(null, 9,ChatColor.GREEN + "" + ChatColor.BOLD + "Money");
		
		invMoney.setItem(0, addMoney);
		invMoney.setItem(1, transfertMoney);
		invMoney.setItem(2, removeMoney);
		invMoney.setItem(7, cancelButton);
		invMoney.setItem(8, displayBalance);
		
		return invMoney;
	}
	
	public static ItemStack selectBalanceDisplay(ItemStack itemBalance,ManagerAccount managerAccount,String bankName){
		ItemMeta itemm = itemBalance.getItemMeta();
		ArrayList<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GRAY + "Balance Bank Account : " + ChatColor.GOLD + managerAccount.getBankAccount(bankName).getBalance() + ChatColor.GREEN + " "+ managerAccount.getPlugin().getRepresentMoney());
		lores.add(ChatColor.BLUE + "Balance Wallet  : " + ChatColor.GOLD + managerAccount.getAmountPocket(managerAccount.getOwnerBank(bankName)) + ChatColor.GREEN + " " + managerAccount.getPlugin().getRepresentMoney());
		itemm.setLore(lores);
		itemBalance.setItemMeta(itemm);
		return itemBalance;
	}
}
