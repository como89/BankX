package net.como89.bankx.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.como89.bankx.bank.logsystem.BookLog;

import org.bukkit.inventory.Inventory;

public class BankData {

	HashMap<UUID,Double> listPocket;
	HashMap<UUID,ArrayList<BankAccount>> listBank;
	HashMap<UUID,String> amountOperation; 
	HashMap<UUID,ArrayList<BookLog>> listBookLog;
	ArrayList<String> listPlayerLastChance;
	HashMap<String,Inventory[]> listInventories;
	HashMap<UUID,String> selectedBankAccount;
	
	BankData(){
		listPocket = new HashMap<UUID,Double>();
		listBank = new HashMap<UUID,ArrayList<BankAccount>>();
		amountOperation = new HashMap<UUID,String>();
		listBookLog = new HashMap<UUID,ArrayList<BookLog>>();
		listPlayerLastChance = new ArrayList<String>();
		listInventories = new HashMap<String,Inventory[]>();
		selectedBankAccount  = new HashMap<UUID,String>();
	}
}
