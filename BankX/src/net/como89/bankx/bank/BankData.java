package net.como89.bankx.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.como89.bankx.bank.logsystem.BookLog;

import org.bukkit.inventory.Inventory;

public class BankData {

	static HashMap<UUID,Double> listPocket = new HashMap<UUID,Double>();
	static HashMap<UUID,ArrayList<BankAccount>> listBank = new HashMap<UUID,ArrayList<BankAccount>>();
	static HashMap<UUID,String> amountOperation = new HashMap<UUID,String>();
	static HashMap<UUID,ArrayList<BookLog>> listBookLog = new HashMap<UUID,ArrayList<BookLog>>();
	static ArrayList<String> listPlayerLastChance = new ArrayList<String>();
	static HashMap<String,Inventory[]> listInventories = new HashMap<String,Inventory[]>();
	static HashMap<UUID,String> selectedBankAccount = new HashMap<UUID,String>();
}
