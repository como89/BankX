package net.como89.bankx.bank;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.como89.bankx.BankX;
import net.como89.bankx.bank.api.BankXResponse;
import net.como89.bankx.bank.logsystem.BookLog;
import net.como89.bankx.bank.logsystem.TransactionType;
import net.como89.bankx.bank.logsystem.TypeLog;

@SuppressWarnings("deprecation")
public class ManagerAccount {

	private BankX plugin;
	private ManageDatabase manageDatabase;
	private FileManager filePocketManager;
	private FileManager fileBankManager;
	private static final DecimalFormat df = new DecimalFormat("0.00");

	public ManagerAccount(BankX plugin) {
		this.plugin = plugin;
		this.manageDatabase = null;
			if(!plugin.isUseMySQL()){
				File fileSQL = new File("plugins/BankX/Data/database.db");
				if(!fileSQL.exists()){
					try {
						fileSQL.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					fileBankManager = new FileManager(new File("plugins/BankX/Data/BankData.dat"),false);
					filePocketManager = new FileManager(new File("plugins/BankX/Data/PocketData.dat"),false);
				}
				manageDatabase = new ManageDatabase("BankX_");
				manageDatabase.openSQLFile("plugins/BankX/Data/database.db");
				manageDatabase.createTables();
				manageDatabase.loadAllData();
				plugin.getLogger().info("[Database] All data are load.");
			}
	}

	public BankX getPlugin() {
		return plugin;
	}
	
	public ManageDatabase getManageDatabase(){
		return manageDatabase;
	}
	
	public FileManager getBankManager(){
		return fileBankManager;
	}
	
	public FileManager getPocketManager(){
		return filePocketManager;
	}
	
	public void setManageDatabase(ManageDatabase manageDatabase){
		this.manageDatabase = manageDatabase;
	}

	public double getAmountPocket(UUID playerUUID) {
		if (BankData.listPocket.containsKey(playerUUID)) {
			return BankData.listPocket.get(playerUUID);
		}
		return -1;
	}

	public BankXResponse removeAmountPocket(UUID playerUUID, double money) {
		if (BankData.listPocket.containsKey(playerUUID)) {
			double moneyPocket = BankData.listPocket.get(playerUUID);
			if (moneyPocket >= money) {
				double moneyTotal = moneyPocket - money;
				BankData.listPocket.put(playerUUID, moneyTotal);
				logIntoBook(playerUUID, "Remove money in wallet.", "Remove " + money + " " + plugin.getRepresentMoney() + " in " + Bukkit.getPlayer(playerUUID).getName() + " wallet.", new Date(System.currentTimeMillis()), TransactionType.REMOVE, TypeLog.MONEY);
				if(manageDatabase != null){
					if(manageDatabase.getPlayerId(playerUUID.toString()) != -1){
						manageDatabase.updateAmountPocket(playerUUID.toString(),moneyTotal);
					}
				}
				return BankXResponse.SUCCESS;
			}
			return BankXResponse.NOT_ENOUGHT_MONEY;
		}
		return BankXResponse.ACCOUNT_NOT_EXIST;
	}

	public BankXResponse addAmountPocket(UUID playerUUID, double money) {
		if (BankData.listPocket.containsKey(playerUUID)) {
			double moneyPocket = BankData.listPocket.get(playerUUID);
			double moneyTotal = moneyPocket + money;
			BankData.listPocket.put(playerUUID, moneyTotal);
			logIntoBook(playerUUID, "Add money in wallet.", "Add " + money + " " + plugin.getRepresentMoney() + " in " + Bukkit.getPlayer(playerUUID).getName() + " wallet.", new Date(System.currentTimeMillis()), TransactionType.ADD, TypeLog.MONEY);
			if(manageDatabase != null){
				if(manageDatabase.getPlayerId(playerUUID.toString()) != -1){
					manageDatabase.updateAmountPocket(playerUUID.toString(), moneyTotal);
				}
			}
			return BankXResponse.SUCCESS;
		}
		return BankXResponse.ACCOUNT_NOT_EXIST;
	}

	public void createPocket(UUID playerUUID) {
		if (!BankData.listPocket.containsKey(playerUUID)) {
			double defaultAmount = plugin.getDefaultAmount();
			BankData.listPocket.put(playerUUID, defaultAmount);
			if(manageDatabase != null){
				if(manageDatabase.getPlayerId(playerUUID.toString()) == -1){
					manageDatabase.insertPlayer(playerUUID.toString(), defaultAmount);
					logIntoBook(playerUUID, "Create wallet", "Create wallet for " + Bukkit.getPlayer(playerUUID).getName() + ".", new Date(System.currentTimeMillis()), TransactionType.CREATE, TypeLog.MONEY);
				}
			}
		}
	}

	public boolean hasPocketAccount(UUID playerUUID) {
		return BankData.listPocket.containsKey(playerUUID);
	}

	public boolean hasBankAccount(UUID playerUUID) {
		if(!BankData.listBank.containsKey(playerUUID))
			return false;
		return BankData.listBank.get(playerUUID).size() > 0;
	}

	public BankXResponse createBankAccount(UUID playerUUID, String name) {
		if (!BankData.listBank.containsKey(playerUUID)) {
			ArrayList<BankAccount> listAccount = new ArrayList<BankAccount>();
			listAccount.add(new BankAccount(name));
			BankData.listBank.put(playerUUID, listAccount);
			if(manageDatabase != null){
				if(manageDatabase.getBankId(name) != -1){
					return BankXResponse.BANK_ACCOUNT_ALREADY_EXIST;
				}
				logIntoBook(playerUUID, "Create bank account.", "Create "+name+" bank account.", 
						new Date(System.currentTimeMillis()), TransactionType.CREATE, TypeLog.MONEY);
				int playerID = manageDatabase.getPlayerId(playerUUID.toString());
				manageDatabase.createBankAccount(name, playerID);
			}
			return BankXResponse.SUCCESS;
		} else {
			boolean exist = false;
			for (BankAccount bank : BankData.listBank.get(playerUUID)) {
				if (bank.getName().equals(name)) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				BankData.listBank.get(playerUUID).add(new BankAccount(name));
				if(manageDatabase != null){
					if(manageDatabase.getBankId(name) != -1){
						return BankXResponse.BANK_ACCOUNT_ALREADY_EXIST;
					}
					logIntoBook(playerUUID, "Create bank account.", "Create "+name+" bank account.", 
							new Date(System.currentTimeMillis()), TransactionType.CREATE, TypeLog.MONEY);
					int playerID = manageDatabase.getPlayerId(playerUUID.toString());
					manageDatabase.createBankAccount(name, playerID);
				}
				return BankXResponse.SUCCESS;
			}
		}
		return BankXResponse.BANK_ACCOUNT_ALREADY_EXIST;
	}

	public double deleteBankAccount(String name) {
		for (ArrayList<BankAccount> listBankAccount : BankData.listBank
				.values()) {
			for (BankAccount bankAccount : listBankAccount) {
				if (bankAccount.getName().equals(name)) {
					logIntoBook(getOwnerBank(name), "Delete bank account.", "Delete "+name+" bank account.", 
							new Date(System.currentTimeMillis()), TransactionType.DELETE, TypeLog.MONEY);
					listBankAccount.remove(bankAccount);
					if(manageDatabase != null){
						manageDatabase.deleteBankAccount(name);
					}
					return bankAccount.getBalance();
				}
			}
		}
		return -1;
	}

	public List<String> getListBank() {
		List<String> listBank = new ArrayList<String>();
		for (ArrayList<BankAccount> listBankAccount : BankData.listBank
				.values()) {
			for (BankAccount bankAccount : listBankAccount) {
				listBank.add(bankAccount.getName());
			}
		}
		return listBank;
	}

	public BankXResponse addAmountBankAccount(String name, double amount) {
		BankAccount bankAccount = getBankAccount(name);
		if (bankAccount != null) {
			double totalBalance = bankAccount.getBalance() + amount;
			bankAccount.setBalance(totalBalance);
			logIntoBook(getOwnerBank(name), "Add money in bank account.", "Add " +amount+ " " + plugin.getRepresentMoney() + " in "+name+" bank account.", 
					new Date(System.currentTimeMillis()), TransactionType.ADD, TypeLog.MONEY);
			if(manageDatabase != null){
				if(manageDatabase.getBankId(name) != -1){
					manageDatabase.updateAmountBank(name, totalBalance);
				}
			}
			return BankXResponse.SUCCESS;
		}
		return BankXResponse.BANK_ACCOUNT_NOT_EXIST;
	}

	public BankXResponse removeAmountBankAccount(String name, double amount) {
		BankAccount bankAccount = getBankAccount(name);
		if (bankAccount != null) {
			double moneyBank = bankAccount.getBalance();
			if (moneyBank >= amount) {
				double totalBalance = moneyBank - amount;
				bankAccount.setBalance(totalBalance);
				logIntoBook(getOwnerBank(name), "Remove money in bank account.", "Remove " +amount+ " " + plugin.getRepresentMoney() + " in "+name+" bank account.", 
						new Date(System.currentTimeMillis()), TransactionType.REMOVE, TypeLog.MONEY);
				if(manageDatabase != null){
					if(manageDatabase.getBankId(name) != -1){
						manageDatabase.updateAmountBank(name, totalBalance);
					}
				}
				return BankXResponse.SUCCESS;
			}
			return BankXResponse.NOT_ENOUGHT_MONEY;
		}
		return BankXResponse.BANK_ACCOUNT_NOT_EXIST;
	}

	public BankXResponse accountPocketExist(UUID playerUUID) {
		if (BankData.listPocket.containsKey(playerUUID)) {
			return BankXResponse.SUCCESS;
		}
		return BankXResponse.ACCOUNT_NOT_EXIST;
	}
	
	public boolean changeBankName(String olderName,String newName,UUID playerUUID){
		BankAccount bankAccount = getBankAccount(olderName);
		if(bankAccount != null){
			if(!checkBankAccountExist(newName, playerUUID)){
				bankAccount.setName(newName);
				int playerID = manageDatabase.getPlayerId(playerUUID.toString());
				manageDatabase.updateBankName(olderName, newName, playerID);
				return true;
			}
		}
		return false;
	}
	
	public boolean changeInventoryName(String bankName,String olderName,String newName){
		BankAccount bankAccount = getBankAccount(bankName);
		if(bankAccount != null){
			boolean inventoryExist = false;
			String invenName = "";
			for(String inventoryName : bankAccount.getBankInventories().keySet()){
				if(inventoryName.equals(olderName)){
					inventoryExist = true;
					invenName = inventoryName;
				} 
				if (inventoryName.equals(newName)) {
					inventoryExist = false;
					break;
				}
			}
			if(inventoryExist){
				ItemStack[] items = bankAccount.getBankInventories().get(invenName);
				bankAccount.getBankInventories().remove(invenName);
				bankAccount.getBankInventories().put(newName, items);
				manageDatabase.updateInventoryName(bankName, olderName, newName);
				return true;
			}
		}
		return false;
	}

	public void addAmountInOperation(UUID playerUUID, String amount) {
		if(amount == null || amount.isEmpty())return;
		if (BankData.amountOperation.get(playerUUID) != null) {
			BankData.amountOperation.put(playerUUID,
					BankData.amountOperation.get(playerUUID) + amount);
		} else {
			BankData.amountOperation.put(playerUUID, amount);
		}
	}

	public void clearInventory(String name) {
		BankAccount bankAccount = getBankAccount(name);
		if(bankAccount != null){
			bankAccount.clearInventory();
		}
	}
	
	public void giveBookLog(Player player){
		ItemStack itemBook = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookM = (BookMeta) itemBook.getItemMeta();
		bookM.setDisplayName(ChatColor.DARK_PURPLE + "BookLog");
		bookM.setAuthor(player.getName() + "'s log.");
		if(BankData.listBookLog.containsKey(player.getUniqueId())){
			for(BookLog bookLog : BankData.listBookLog.get(player.getUniqueId())){
				bookM.addPage(ChatColor.translateAlternateColorCodes('&',bookLog.toString()));
			}
		}
		itemBook.setItemMeta(bookM);
		player.getInventory().addItem(itemBook);
	}

	public void clearAmountInOperation(UUID playerUUID) {
		BankData.amountOperation.remove(playerUUID);
	}

	public String getAmountInOperation(UUID playerUUID) {
		return BankData.amountOperation.get(playerUUID);
	}

	public double getAmountInBankAccount(String name) {
		BankAccount bankAccount = getBankAccount(name);
		if (bankAccount != null) {
			return bankAccount.getBalance();
		}
		return -1;
	}
	
	public ArrayList<BankAccount> getBanksAccountOfPlayer(UUID playerUUID){
		return BankData.listBank.get(playerUUID);
	}
	
	public UUID getOwnerBank(String name){
		for(UUID playerName : BankData.listBank.keySet()){
			for(BankAccount bank : BankData.listBank.get(playerName)){
				if(bank.getName().equals(name)){
					return playerName;
				}
			}
		}
		return null;
	}

	public String replaceTag(String msg, String player, double amount,String name) {
		String ligne = msg.replace("<money>",
				ChatColor.GOLD + df.format(amount) + " " + plugin.getRepresentMoney() + ChatColor.DARK_AQUA);
		ligne = ligne.replace("<player>", ChatColor.AQUA + player + ChatColor.DARK_AQUA);
		ligne = ligne.replace("<name>",ChatColor.GRAY + name + ChatColor.DARK_AQUA);
		return ligne;
	}

	public ArrayList<UUID> getAllPlayerAccount() {
		ArrayList<UUID> listPlayer = new ArrayList<UUID>();
		for (UUID player : BankData.listBank.keySet()) {
			listPlayer.add(player);
		}
		return listPlayer;
	}

	public HashMap<String, ItemStack[]> getAllInventoryOfTheBank(String bankAccountName) {
		BankAccount bankAccount = getBankAccount(bankAccountName);
		if(bankAccount != null){
			return bankAccount.getBankInventories();
		}
		return null;
	}

	public void addBankInList(String bank) {
		if (!checkBankInList(bank)) {
			BankData.listPlayerLastChance.add(bank);
		}
	}

	public void removeBankInList(String bank) {
		if (checkBankInList(bank)) {
			BankData.listPlayerLastChance.remove(bank);
		}
	}

	public boolean checkBankInList(String bank) {
		return BankData.listPlayerLastChance.contains(bank);
	}

	public void addPlayerInventory(String player, Inventory[] inv) {
		BankData.listInventories.put(player, inv);
	}

	public Inventory[] getInventories(String player) {
		return BankData.listInventories.get(player);
	}

	public void clearPlayerInventories(String player) {
		BankData.listInventories.remove(player);
	}
	
	public String getSelectedBankAccount(UUID playerUUID){
		BankAccount bankAccount = getBankAccount(BankData.selectedBankAccount.get(playerUUID));
		return bankAccount != null?bankAccount.getName():null;
	}
	
	public void selectBankAccount(UUID playerUUID, String bankName){
		BankData.selectedBankAccount.put(playerUUID, bankName);
	}

	public BankAccount getBankAccount(String name) {
		for (ArrayList<BankAccount> listBankAccount : BankData.listBank
				.values()) {
			for (BankAccount bankAccount : listBankAccount) {
				if (bankAccount.getName().equals(name)) {
					return bankAccount;
				}
			}
		}
		return null;
	}
	
	private boolean checkBankAccountExist(String name,UUID playerUUID){
		ArrayList<BankAccount> listBankAccount = getBanksAccountOfPlayer(playerUUID);
		if(listBankAccount != null && !listBankAccount.isEmpty()){
			for(BankAccount bankAccount : listBankAccount){
				if(bankAccount.getName().equals(name)){
					return true;
				}
			}
		}
		return false;
	}
	
	private void logIntoBook(UUID playerUUID, String nameLog,String descriptionLog, Date date, TransactionType transactType, TypeLog logType){
		ArrayList<BookLog> listBookLog = null;
		if(!BankData.listBookLog.containsKey(playerUUID)){
			listBookLog = new ArrayList<BookLog>();
		} else {
			listBookLog = BankData.listBookLog.get(playerUUID);
		}
		manageDatabase.addLogEntry(playerUUID, nameLog,descriptionLog, date, transactType, logType);
		int bookId = manageDatabase.getBookId(playerUUID, nameLog,descriptionLog, date, transactType, logType);
		listBookLog.add(new BookLog(nameLog,descriptionLog, date, transactType, logType,bookId));
		BankData.listBookLog.put(playerUUID, listBookLog);
	}
}
