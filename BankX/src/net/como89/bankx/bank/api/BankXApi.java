package net.como89.bankx.bank.api;

import java.util.Map;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import net.como89.bankx.bank.ManagerAccount;
/**
 * The class api
 * @author como89
 * @version 0.5b
 * @since ----
 */
public class BankXApi {
	private ManagerAccount managerAccount;

	public BankXApi(ManagerAccount managerAccount){
		this.managerAccount = managerAccount;
	}
	/**
	 * This method check if the plugin is enable.
	 * @return boolean - The plugin is enable or not.
	 */
	public boolean isEnable(){
		return managerAccount.getPlugin().isEnabled();
	}
	
	/**
	 * This method add amount to the account of the player
	 * @param playerUUID - The player UUID.
	 * @param amount - The amount to add.
	 * @return BankXResponse - SUCCESS, ACCOUNT_NOT_EXIST
	 */
	public BankXResponse addAmountAccount(UUID playerUUID, double amount){
		return managerAccount.addAmountPocket(playerUUID, amount);
	}
	/**
	 * This method remove amount to the account of the player if he has enought money.
	 * @param playerUUID - The player UUID.
	 * @param amount - The amount to remove.
	 * @return BankXResponse - SUCCESS, ACCOUNT_NOT_EXIST, NOT_ENOUGHT_MONEY
	 */
	public BankXResponse removeAmountAccount(UUID playerUUID,double amount){
		return managerAccount.removeAmountPocket(playerUUID, amount);
	}
	
	/**
	 * This method return the amount in the account of the player.
	 * @param playerUUID -The player UUID.
	 * @return double - Return the amount in the account. If the amount is -1.0, the account don't exist.
	 */
	public double getAmountAccount(UUID playerUUID){
		return managerAccount.getAmountPocket(playerUUID);
	}
	
	
	/**
	 * This method return true if the player has a pocket account.
	 * @param playerUUID -The player UUID.
	 * @return boolean - return true if the player has a pocket account.
	 */
	public boolean hasPocketAccount(UUID playerUUID){
		return managerAccount.hasPocketAccount(playerUUID);
	}
	
	/**
	 * This method return true if the player has a bank account.
	 * @param playerUUID -The player UUID.
	 * @return boolean - return true if the player has a bank account.
	 */
	public boolean hasBankAccount(UUID playerUUID){
		return managerAccount.hasBankAccount(playerUUID);
	}
	
	
	/**
	 * This method create a bank account for the player specified and add a amount in it.
	 * @param name - The bank name.
	 * @param playerUUID - The player UUID.
	 * @return BankXResponse - SUCCESS, BANK_ACCOUNT_ALREADY_EXIST
	 */
	public BankXResponse createBankAccount(String name,UUID playerUUID){
		return managerAccount.createBankAccount(playerUUID, name);
	}
	
	/**
	 * This method delete the bank account of the player specified and return the amount of the bank account.
	 * @param name -The bank name.
	 * @param uuidPlayer - The uuid of the player.
	 * @return double - Amount of the deleted bank account. If return -1, the player don't have a bank account.
	 */
	public double deleteBankAccount(String name,UUID uuidPlayer){
		return managerAccount.deleteBankAccount(name,uuidPlayer);
	}
	
	/**
	 * This method add amount to the bank account.
	 * @param name - The bank name.
	 * @param amount - The amount to add.
	 * @return BankXResponse - SUCCESS, BANK_ACCOUNT_NOT_EXIST
	 */
	public BankXResponse addAmountBankAccount(String name,UUID playerUUID,double amount){
		return managerAccount.addAmountBankAccount(name,playerUUID, amount);
	}
	/**
	 * This method remove amount to the bank account.
	 * @param name - The bank name.
	 * @param amount - The amount to remove.
	 * @return BankXResponse - SUCCESS, BANK_ACCOUNT_NOT_EXIST, NOT_ENOUGHT_MONEY
	 */
	public BankXResponse removeAmountBankAccount(String name,UUID playerUUID,double amount){
		return managerAccount.removeAmountBankAccount(name,playerUUID, amount);
	}
	
	/**
	 * This method return the amount in the bank account.
	 * @param name - The bank name.
	 * @return double - Return the amount in the bank account. If the amount is -1.0, the bank account don't exist.
	 */
	public double getAmountInBankAccount(String name, UUID playerUUID){
		return managerAccount.getAmountInBankAccount(name,playerUUID);
	}
	/**
	 * This method return a HashMap of all inventory of the bank.
	 * @param bankName - The bank name.
	 * @return Map<String,ItemStack[]> - Return a Map with all inventory of the player.
	 * Return a empty Map if the player don't have inventories and return null if the bank account not exist.
	 */
	public Map<String,ItemStack[]> getAllInventoryContent(String bankName,UUID playerUUID){
		return managerAccount.getAllInventoryOfTheBank(bankName,playerUUID);
	}
}
