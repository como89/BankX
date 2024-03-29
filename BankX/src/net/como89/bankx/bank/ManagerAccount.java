package net.como89.bankx.bank;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import net.como89.bankx.bank.items.InventoryItems;
import net.como89.bankx.bank.logsystem.BookLog;
import net.como89.bankx.bank.logsystem.TransactionType;
import net.como89.bankx.bank.logsystem.TypeLog;
import net.como89.bankx.tasks.DatabaseTask;
import net.como89.bankx.tasks.TaskManager;
import net.como89.bankx.tasks.databaserequest.CreateBankAccount;
import net.como89.bankx.tasks.databaserequest.CreatePocket;
import net.como89.bankx.tasks.databaserequest.DeleteBankAccount;
import net.como89.bankx.tasks.databaserequest.LogEntry;
import net.como89.bankx.tasks.databaserequest.UpdateBankAmount;
import net.como89.bankx.tasks.databaserequest.UpdateBankName;
import net.como89.bankx.tasks.databaserequest.UpdateInventoryName;
import net.como89.bankx.tasks.databaserequest.UpdatePocket;

public class ManagerAccount {

	private BankX plugin;
	BankXData bankData;
	TaskManager taskManager;
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	private static ManagerAccount instance;

	public ManagerAccount(BankX plugin) {
		this.plugin = plugin;
		this.bankData = new BankXData();
			if(!plugin.isUseMySQL()){
				File fileSQL = new File("plugins/BankX/Data/database.db");
				if(!fileSQL.exists()){
					try {
						fileSQL.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				ManageDatabase.initDatabaseInstance("BankX_", "plugins/BankX/Data/database.db");
			} else {
				
			}
			ManageDatabase md = ManageDatabase.getDatabaseInstance();
			md.connectToDatabase();
			md.createTables();
			md.loadAllData(this);
			md.disconnect();
			plugin.getLogger().info("[Database] All data are load.");
			taskManager = new TaskManager(plugin);
			plugin.getLogger().info("[TaskManager] TaskManager is ready and run now.");
			Bukkit.getScheduler().runTaskTimer(plugin, taskManager, 20L, 20L);
			instance = this;
	}
	
	@Deprecated
	public static ManagerAccount getInstance() {
		return instance;
	}

	public BankX getPlugin() {
		return plugin;
	}
	
	public TaskManager getTaskManager() {
		return taskManager;
	}

	public double getAmountPocket(UUID playerUUID) {	
		PlayerData playerData = getPlayerData(playerUUID);
		return playerData == null?-1:playerData.moneyPocket;
	}

	public BankXResponse removeAmountPocket(UUID playerUUID, double money) {
		PlayerData playerData = getPlayerData(playerUUID);
		if (playerData != null) {
			double moneyPocket = playerData.moneyPocket;
			if (moneyPocket >= money) {
				double moneyTotal = moneyPocket - money;
				playerData.moneyPocket = moneyTotal;
				logIntoBook(playerUUID, "Remove money in wallet.", "Remove " + money + " " + plugin.getRepresentMoney() + " in " + Bukkit.getPlayer(playerUUID).getName() + " wallet.", new Date(System.currentTimeMillis()), TransactionType.REMOVE, TypeLog.MONEY);
				DatabaseTask dt = new DatabaseTask("Database",0,new UpdatePocket(playerUUID, moneyTotal));
				taskManager.registerTask(dt);
				return BankXResponse.SUCCESS;
			}
			return BankXResponse.NOT_ENOUGHT_MONEY;
		}
		return BankXResponse.ACCOUNT_NOT_EXIST;
	}

	public BankXResponse addAmountPocket(UUID playerUUID, double money) {
		PlayerData playerData = getPlayerData(playerUUID);
		if (playerData != null) {
			double moneyPocket = playerData.moneyPocket;
			double moneyTotal = moneyPocket + money;
			playerData.moneyPocket = moneyTotal;
			logIntoBook(playerUUID, "Add money in wallet.", "Add " + money + " " + plugin.getRepresentMoney() + " in " + Bukkit.getPlayer(playerUUID).getName() + " wallet.", new Date(System.currentTimeMillis()), TransactionType.ADD, TypeLog.MONEY);
			DatabaseTask dt = new DatabaseTask("Database",0,new UpdatePocket(playerUUID, moneyTotal));
			taskManager.registerTask(dt);
			return BankXResponse.SUCCESS;
		}
		return BankXResponse.ACCOUNT_NOT_EXIST;
	}

	public void createPocket(UUID playerUUID) {
		PlayerData playerData = getPlayerData(playerUUID);
		if (playerData == null) {
			double defaultAmount = plugin.getDefaultAmount();
			playerData = new PlayerData(playerUUID,defaultAmount);
			bankData.listPlayerData.add(playerData);
			DatabaseTask dt = new DatabaseTask("Database",0,new CreatePocket(playerUUID,defaultAmount));
			taskManager.registerTask(dt);
		}
	}

	public boolean hasPocketAccount(UUID playerUUID) {
		return getPlayerData(playerUUID) != null;
	}

	public boolean hasBankAccount(UUID playerUUID) {
		PlayerData playerData = getPlayerData(playerUUID);
		return playerData != null && playerData.listBanksAccount.size() > 0;
	}

	public BankXResponse createBankAccount(UUID playerUUID, String name) {
		PlayerData playerData = getPlayerData(playerUUID);
		if (!(playerData.listBanksAccount.size() > 0)) {
			ArrayList<BankAccount> listAccount = new ArrayList<BankAccount>();
			listAccount.add(new BankAccount(name));
			playerData.listBanksAccount = listAccount;
			logIntoBook(playerUUID, "Create bank account.", "Create "+name+" bank account.", 
						new Date(System.currentTimeMillis()), TransactionType.CREATE, TypeLog.MONEY);
			DatabaseTask dt = new DatabaseTask("Database",0,new CreateBankAccount(playerUUID,name));
			taskManager.registerTask(dt);
			return BankXResponse.SUCCESS;
		} else {
			boolean exist = false;
			for (BankAccount bank : playerData.listBanksAccount) {
				if (bank.getName().equals(name)) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				playerData.listBanksAccount.add(new BankAccount(name));
				logIntoBook(playerUUID, "Create bank account.", "Create "+name+" bank account.", 
							new Date(System.currentTimeMillis()), TransactionType.CREATE, TypeLog.MONEY);
				DatabaseTask dt = new DatabaseTask("Database",0,new CreateBankAccount(playerUUID,name));
				taskManager.registerTask(dt);
				return BankXResponse.SUCCESS;
			}
		}
		return BankXResponse.BANK_ACCOUNT_ALREADY_EXIST;
	}

	public double deleteBankAccount(String name, UUID ownerPlayer) {
		PlayerData playerData = getPlayerData(ownerPlayer);
			for (BankAccount bankAccount : playerData.listBanksAccount) {
				if (bankAccount.getName().equals(name)) {
					logIntoBook(ownerPlayer, "Delete bank account.", "Delete "+name+" bank account.", 
							new Date(System.currentTimeMillis()), TransactionType.DELETE, TypeLog.MONEY);
					playerData.listBanksAccount.remove(bankAccount);
					DatabaseTask dt = new DatabaseTask("Database",0,new DeleteBankAccount(ownerPlayer,name));
					taskManager.registerTask(dt);
					return bankAccount.getBalance();
				}
			}
		return -1;
	}

	public BankXResponse addAmountBankAccount(String name,UUID uuidPlayer, double amount) {
		BankAccount bankAccount = getBankAccount(name,uuidPlayer);
		if (bankAccount != null) {
			double totalBalance = bankAccount.getBalance() + amount;
			bankAccount.setBalance(totalBalance);
			logIntoBook(uuidPlayer, "Add money in bank account.", "Add " +amount+ " " + plugin.getRepresentMoney() + " in "+name+" bank account.", 
					new Date(System.currentTimeMillis()), TransactionType.ADD, TypeLog.MONEY);
			DatabaseTask dt = new DatabaseTask("Database",0,new UpdateBankAmount(uuidPlayer,totalBalance,name));
			taskManager.registerTask(dt);
			return BankXResponse.SUCCESS;
		}
		return BankXResponse.BANK_ACCOUNT_NOT_EXIST;
	}

	public BankXResponse removeAmountBankAccount(String bankName,UUID playerUUID, double amount) {
		BankAccount bankAccount = getBankAccount(bankName,playerUUID);
		if (bankAccount != null) {
			double moneyBank = bankAccount.getBalance();
			if (moneyBank >= amount) {
				double totalBalance = moneyBank - amount;
				bankAccount.setBalance(totalBalance);
				logIntoBook(playerUUID, "Remove money in bank account.", "Remove " +amount+ " " + plugin.getRepresentMoney() + " in "+bankName+" bank account.", 
						new Date(System.currentTimeMillis()), TransactionType.REMOVE, TypeLog.MONEY);
				DatabaseTask dt = new DatabaseTask("Database",0,new UpdateBankAmount(playerUUID,totalBalance,bankName));
				taskManager.registerTask(dt);
				return BankXResponse.SUCCESS;
			}
			return BankXResponse.NOT_ENOUGHT_MONEY;
		}
		return BankXResponse.BANK_ACCOUNT_NOT_EXIST;
	}
	
	public boolean changeBankName(String olderName,String newName,UUID playerUUID){
		BankAccount bankAccount = getBankAccount(olderName,playerUUID);
		if(bankAccount != null){
			if(!checkBankAccountExist(newName, playerUUID)){
				bankAccount.setName(newName);
				DatabaseTask dt = new DatabaseTask("Database",0,new UpdateBankName(playerUUID,olderName,newName));
				taskManager.registerTask(dt);
				return true;
			}
		}
		return false;
	}
	
	public boolean changeInventoryName(String bankName,String olderName,String newName,UUID playerUUID){
		BankAccount bankAccount = getBankAccount(bankName,playerUUID);
		if(bankAccount != null){
			boolean inventoryExist = false;
			String invenName = "";
			for(InventoryItems inv : bankAccount.listInventaire){
				String inventoryName = inv.getName();
				if(inventoryName.equals(olderName)){
					inventoryExist = true;
					invenName = inventoryName;
				} 
				if (inventoryName.equals(newName)) {
					inventoryExist = false;
					break;
				}
			}
			if(inventoryExist) {
				InventoryItems invLast = bankAccount.getInventoryItems(invenName);
				InventoryItems invNew = new InventoryItems(newName,invLast.getSize());
				invNew.setMaxStackSize(invLast.getMaxStackSize());
				invNew.setContents(invLast.getContents());
				bankAccount.listInventaire.remove(invLast);
				bankAccount.listInventaire.add(invNew);
				DatabaseTask dt = new DatabaseTask("Database",0,new UpdateInventoryName(playerUUID,olderName,newName,bankName));
				taskManager.registerTask(dt);
				return true;
			}
		}
		return false;
	}

	public void addAmountInOperation(UUID playerUUID, String amount) {
		if(amount == null || amount.isEmpty())return;
		if (bankData.amountOperation.get(playerUUID) != null) {
			bankData.amountOperation.put(playerUUID,
					bankData.amountOperation.get(playerUUID) + amount);
		} else {
			bankData.amountOperation.put(playerUUID, amount);
		}
	}

	public void clearInventory(String name,UUID playerUUID) {
		BankAccount bankAccount = getBankAccount(name,playerUUID);
		if(bankAccount != null){
			bankAccount.clearInventory();
		}
	}
	
	public void giveBookLog(Player player){
		PlayerData playerData = getPlayerData(player.getUniqueId());
		ItemStack itemBook = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookM = (BookMeta) itemBook.getItemMeta();
		bookM.setDisplayName(ChatColor.DARK_PURPLE + "BookLog");
		bookM.setAuthor(player.getName() + "'s log.");
		if(playerData.listBookLog.size() > 0){
			for(BookLog bookLog : playerData.listBookLog){
				bookM.addPage(ChatColor.translateAlternateColorCodes('&',bookLog.toString()));
			}
		}
		itemBook.setItemMeta(bookM);
		player.getInventory().addItem(itemBook);
	}

	public void clearAmountInOperation(UUID playerUUID) {
		bankData.amountOperation.remove(playerUUID);
	}

	public String getAmountInOperation(UUID playerUUID) {
		return bankData.amountOperation.get(playerUUID);
	}

	public double getAmountInBankAccount(String name,UUID playerUUID) {
		BankAccount bankAccount = getBankAccount(name,playerUUID);
		if (bankAccount != null) {
			return bankAccount.getBalance();
		}
		return -1;
	}
	
	public ArrayList<PlayerData> getAllPlayerAccounts(){
		return bankData.listPlayerData;
	}
	
	public ArrayList<BankAccount> getBanksAccountOfPlayer(UUID playerUUID){
		PlayerData playerData = getPlayerData(playerUUID);
		return playerData.listBanksAccount;
	}

	public String replaceTag(String msg, String player, double amount,String name) {
		String ligne = msg.replace("<money>",
				ChatColor.GOLD + df.format(amount) + " " + plugin.getRepresentMoney() + ChatColor.DARK_AQUA);
		ligne = ligne.replace("<player>", ChatColor.AQUA + player + ChatColor.DARK_AQUA);
		ligne = ligne.replace("<name>",ChatColor.GRAY + name + ChatColor.DARK_AQUA);
		return ligne;
	}

	public HashMap<String, ItemStack[]> getAllInventoryOfTheBank(String bankAccountName,UUID playerUUID) {
		BankAccount bankAccount = getBankAccount(bankAccountName,playerUUID);
		if(bankAccount != null){
			return bankAccount.getBankInventories();
		}
		return null;
	}

	public void addBankInList(String bank) {
		if (!checkBankInList(bank)) {
			bankData.listPlayerLastChance.add(bank);
		}
	}

	public void removeBankInList(String bank) {
		if (checkBankInList(bank)) {
			bankData.listPlayerLastChance.remove(bank);
		}
	}

	public boolean checkBankInList(String bank) {
		return bankData.listPlayerLastChance.contains(bank);
	}

	public void addPlayerInventory(String player, Inventory[] inv) {
		bankData.listInventories.put(player, inv);
	}

	public Inventory[] getInventories(String player) {
		return bankData.listInventories.get(player);
	}

	public void clearPlayerInventories(String player) {
		bankData.listInventories.remove(player);
	}
	
	public String getSelectedBankAccount(UUID playerUUID){
		BankAccount bankAccount = getBankAccount(bankData.selectedBankAccount.get(playerUUID),playerUUID);
		return bankAccount != null?bankAccount.getName():null;
	}
	
	public void selectBankAccount(UUID playerUUID, String bankName){
		bankData.selectedBankAccount.put(playerUUID, bankName);
	}

	public BankAccount getBankAccount(String name,UUID uuidPlayer) {
		PlayerData playerData = getPlayerData(uuidPlayer);
			for (BankAccount bankAccount : playerData.listBanksAccount) {
				if (bankAccount.getName().equals(name)) {
					return bankAccount;
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
		PlayerData playerData = getPlayerData(playerUUID);
		BookLog booklog = new BookLog(nameLog,descriptionLog, date, transactType, logType);
		DatabaseTask dt = new DatabaseTask("Database",0,new LogEntry(playerUUID,booklog));
		taskManager.registerTask(dt);
		playerData.listBookLog.add(booklog);
	}
	
	PlayerData getPlayerData(UUID playerUUID){
		for(PlayerData playerData : bankData.listPlayerData){
			if(playerData.playerUUID.equals(playerUUID)){
				return playerData;
			}
		}
		return null;
	}
}
