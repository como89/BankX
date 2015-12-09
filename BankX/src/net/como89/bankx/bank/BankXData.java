package net.como89.bankx.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.inventory.Inventory;

public class BankXData {
	
	ArrayList<PlayerData> listPlayerData;
	HashMap<UUID,String> amountOperation; 
	ArrayList<String> listPlayerLastChance;
	HashMap<String,Inventory[]> listInventories;
	HashMap<UUID,String> selectedBankAccount;
	
	BankXData(){
		listPlayerData = new ArrayList<PlayerData>();
		amountOperation = new HashMap<UUID,String>();
		listPlayerLastChance = new ArrayList<String>();
		listInventories = new HashMap<String,Inventory[]>();
		selectedBankAccount  = new HashMap<UUID,String>();
	}
}
