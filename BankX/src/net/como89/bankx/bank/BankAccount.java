package net.como89.bankx.bank;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class BankAccount {

	private String accountName;
	private double balance;
	private HashMap<String,ItemStack[]> listInventaire;
	
	public BankAccount(String accountName){
		this.accountName = accountName;
		this.balance = 0.0;
		listInventaire = new HashMap<String,ItemStack[]>();
	}
	
	public BankAccount(String accountName,double balance){
		this.accountName = accountName;
		this.balance = balance;
		listInventaire = new HashMap<String,ItemStack[]>();
	}
	
	public double getBalance(){
		return balance;
	}
	
	public String getName(){
		return accountName;
	}
	
	void setBalance(double amount){
		this.balance = amount;
	}
	
	void setName(String name){
		this.accountName = name;
	}
	
	public void modifyInventoryObjects(String inventoryName,
			ItemStack[] items) {
		listInventaire.put(inventoryName, items);
	}
	
	public HashMap<String, ItemStack[]> getBankInventories() {
		return listInventaire;
	}
	
	public void clearInventory(){
		listInventaire.clear();
	}
}
