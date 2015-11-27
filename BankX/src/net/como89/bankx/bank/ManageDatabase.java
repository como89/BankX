package net.como89.bankx.bank;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.como89.bankx.bank.logsystem.BookLog;
import net.como89.bankx.bank.logsystem.TransactionType;
import net.como89.bankx.bank.logsystem.TypeLog;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.como89.myapi.MyApiLib;
import ca.como89.myapi.api.ApiResponse;
import ca.como89.myapi.api.MyApi;
import ca.como89.myapi.api.TableData;
import ca.como89.myapi.api.mysql.Columns;
import ca.como89.myapi.api.mysql.Condition;
import ca.como89.myapi.api.mysql.TableProperties;
import ca.como89.myapi.api.mysql.TypeCondition;
import ca.como89.myapi.api.mysql.TypeData;
import ca.como89.myapi.api.mysql.exception.IllegalTypeException;
import ca.como89.myapi.api.mysql.exception.LengthTableException;

public class ManageDatabase {

	private MyApi myapi;
	private String prefix;
	
	/*stat.execute("CREATE TABLE IF NOT EXISTS PLAYERS (ID INT NOT NULL AUTO_INCREMENT,"
	+ "PLAYER VARCHAR(120) NOT NULL,AMOUNT_POCKET DOUBLE PRECISION NOT NULL, PRIMARY KEY (ID))");
stat.execute("CREATE TABLE IF NOT EXISTS BANK_ACCOUNT (ID_BANK INT NOT NULL AUTO_INCREMENT,"
	+ "PLAYER_ID INT NOT NULL, BANKNAME VARCHAR(120) NOT NULL, AMOUNT DOUBLE PRECISION NOT NULL, CONSTRAINT FK_PLAYER_NUMBER FOREIGN KEY (PLAYER_ID) REFERENCES PLAYERS(ID), PRIMARY KEY(ID_BANK))");*/
	
	public ManageDatabase(String prefix){
		myapi = MyApiLib.createInstance("Bankx");
		this.prefix = prefix;
	}
	
	public boolean connectToDatabase(String host, int port, String userName, String password, String database){
		myapi.init(host, port, userName, password, database);
		try {
			myapi.connect();
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			Bukkit.getLogger().warning("Cannot connect to database! Please verify your entries in the config!");
		}
		return false;
	}
	
	public boolean openSQLFile(String link){
		myapi.init(link);
		try {
			myapi.connect();
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			Bukkit.getLogger().warning("Cannot read the SQL file, please check if the file exist!");
			e.printStackTrace();
		}
		return false;
	}
	
	public void disconnect(){
		try {
			myapi.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean createTables(){
		boolean noError = false;
		List<Columns> listColumns = new ArrayList<Columns>();
		
		Columns id = new Columns("ID", TypeData.INT, 0, false, true, true,false);
		Columns playerName = new Columns("UUID", TypeData.VARCHAR, 250, false, false, false,true);
		Columns amount_pocket = new Columns("Amount_Pocket",TypeData.DOUBLE,20,2,false, false, false,false);
		listColumns.add(id);
		listColumns.add(playerName);
		listColumns.add(amount_pocket);
		ApiResponse apiResp = myapi.createTable(prefix+"Players", listColumns, true);
		
		if(apiResp == ApiResponse.SUCCESS){
			noError = true;
		}else{
			return false;
		}
		
		listColumns.clear();
		id = new Columns("ID_Bank", TypeData.INT, 0, false, true, true,false);
		Columns player_id = new Columns("Player_ID",TypeData.INT,0,false, false, false,false);
		Columns bank_name = new Columns("BankName",TypeData.VARCHAR,250,false, false, false,true);
		Columns amount = new Columns("Amount",TypeData.DOUBLE,20,2,false,false,false,false);
		listColumns.add(id);
		listColumns.add(player_id);
		listColumns.add(bank_name);
		listColumns.add(amount);
		apiResp = myapi.createTable(prefix+"BankAccount", listColumns, true);
		
		if(apiResp == ApiResponse.SUCCESS){
			noError = true;
		} else{
			return false;
		}
		
		listColumns.clear();
		id = new Columns("ID_Inventory",TypeData.INT,0,false,true,true,false);
		Columns inventoryName = new Columns("Inventory_Name",TypeData.VARCHAR,254,false,false,false,false);
		Columns inventorySize = new Columns("Inventory_Size",TypeData.INT,0,false,false,false,false);
		Columns inventoryStackSize = new Columns("Inventory_Stack_Size",TypeData.INT,0,false,false,false,false);
		player_id = new Columns("Bank_ID",TypeData.INT,0,false, false, false,false);
		listColumns.add(id);
		listColumns.add(inventoryName);
		listColumns.add(inventorySize);
		listColumns.add(inventoryStackSize);
		listColumns.add(player_id);
		apiResp = myapi.createTable(prefix+"Inventories", listColumns, true);
		if(apiResp == ApiResponse.SUCCESS){
			noError = true;
		} else {
			return false;
		}
		
		listColumns.clear();
		id = new Columns("ID_Item", TypeData.INT, 0, false, true, true,false);
		Columns itemDisplayName = new Columns("Item_DisplayName",TypeData.VARCHAR,250,true,false,false,false);
		Columns itemLore = new Columns("Item_Lores",TypeData.TEXT,1024,true,false,false,false);
		Columns itemMaterial = new Columns("Item_Material",TypeData.VARCHAR,250,false,false,false,false);
		Columns itemEnchantment = new Columns("Item_Enchantment",TypeData.TEXT,1024,true,false,false,false);
		Columns itemAmount = new Columns("Item_Amount",TypeData.INT,0,false,false,false,false);
		Columns itemFrame = new Columns("Item_Frame", TypeData.INT,0,false,false,false,false);
		Columns id_Inventory = new Columns("ID_Inventory",TypeData.INT,0,false,false,false,false);
		listColumns.add(id);
		listColumns.add(itemDisplayName);
		listColumns.add(itemLore);
		listColumns.add(itemMaterial);
		listColumns.add(itemEnchantment);
		listColumns.add(itemAmount);
		listColumns.add(itemFrame);
		listColumns.add(id_Inventory);
		apiResp = myapi.createTable(prefix+"Items", listColumns, true);
		
		if(apiResp == ApiResponse.SUCCESS){
			noError = true;
		} else{
			return false;
		}
		listColumns.clear();
		id = new Columns("ID_Log",TypeData.INT,0,false,true,true,false);
		Columns logPlayerUUID = new Columns("Log_PlayerID",TypeData.INT,0,false,false,false,true);
		Columns logName = new Columns("Log_Name",TypeData.VARCHAR,254,false,false,false,false);
		Columns logDescription = new Columns("Log_Description",TypeData.VARCHAR,254,false,false,false,false);
		Columns logDate = new Columns("Log_Date",TypeData.VARCHAR,254,false,false,false,false);
		Columns logTransactType = new Columns("Log_TransactType",TypeData.VARCHAR,254,false,false,false,false);
		Columns logType = new Columns("Log_Type",TypeData.VARCHAR,254,false,false,false,false);
		listColumns.add(id);
		listColumns.add(logPlayerUUID);
		listColumns.add(logName);
		listColumns.add(logDescription);
		listColumns.add(logDate);
		listColumns.add(logTransactType);
		listColumns.add(logType);
		apiResp = myapi.createTable(prefix+"Logs", listColumns, true);
		
		if(apiResp == ApiResponse.SUCCESS){
			noError = true;
		} else {
			noError = false;
		}
		return noError;
	}
	
	public void insertPlayer(String UUID,double amount){
		try {
			TableProperties tableProperties = new TableProperties(prefix+"Players",new String[] {"UUID","Amount_Pocket"},new Object[] {UUID,amount});
			myapi.insertValues(tableProperties);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateAmountPocket(String UUID, double amount){
		try {
			TableProperties tableProperties = new TableProperties(prefix+"Players", new String[] {"Amount_Pocket"}, new Object[] {amount});
			Condition condition = new Condition("UUID",UUID,TypeCondition.EQUALS);
			myapi.updateValues(tableProperties, condition);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
	}
	
	public void updateAmountBank(String bankName, double amount){
		try {
			TableProperties tableProperties = new TableProperties(prefix+"BankAccount",new String[]{"Amount"},new Object[]{amount});
			Condition condition = new Condition("BankName",bankName,TypeCondition.EQUALS);
			myapi.updateValues(tableProperties, condition);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteBankAccount(String bankName){
		try {
			Condition condition = new Condition("BankName",bankName,TypeCondition.EQUALS);
			myapi.deleteRow(prefix+"BankAccount", condition);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} 
	}
	
	public void createBankAccount(String bankName, int playerID){
		try {
			TableProperties tableProperties = new TableProperties(prefix+"BankAccount",new String[]{"Player_ID","BankName","Amount"},new Object[]{playerID,bankName,0.0});
			myapi.insertValues(tableProperties);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateBankName(String olderName, String newName,int playerID){
		int bankId = getBankId(olderName);
		if(bankId != -1){
			Condition condition;
			try {
				TableProperties tableProperties = new TableProperties(prefix+"BankAccount",new String[]{"BankName"},new Object[]{newName});
				condition = new Condition(new String[]{"BankName","Player_ID"},new Object[]{olderName,playerID},new TypeCondition[]{TypeCondition.EQUALS,TypeCondition.EQUALS},new boolean[]{true});
				myapi.updateValues(tableProperties, condition);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalTypeException e) {
				e.printStackTrace();
			} catch (LengthTableException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void updateInventoryName(String bankAccountName, String olderName, String newName){
		int bankId = getBankId(bankAccountName);
		int inventoryId = getInventoryID(olderName, bankId);
		if(bankId != -1 && inventoryId != -1){
			Condition condition;
			try {
				TableProperties tableProperties = new TableProperties(prefix+"Inventories",new String[]{"Inventory_Name"},new Object[]{newName});
				condition = new Condition(new String[]{"Bank_ID","ID_Inventory"},new Object[]{bankId,inventoryId},new TypeCondition[]{TypeCondition.EQUALS,TypeCondition.EQUALS},new boolean[]{true});
				myapi.updateValues(tableProperties, condition);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalTypeException e) {
				e.printStackTrace();
			} catch (LengthTableException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public int getBankId(String bankName){
		int id = -1;
		TableProperties tableProperties = new TableProperties(prefix+"BankAccount",new String [] {"ID_Bank"});
		Condition condition;
		try {
			condition = new Condition("BankName",bankName,TypeCondition.EQUALS);
			TableData tabledata = myapi.selectValues(tableProperties, condition);
			if(tabledata.getResponse() == ApiResponse.SUCCESS){
				if(tabledata.getMapValue().size() != 0){
					id = (Integer) tabledata.getMapValue().get(0);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int getPlayerId(String UUID){
		int id = -1;
		TableProperties tableProperties = new TableProperties(prefix+"Players",new String [] {"ID"});
		Condition condition;
		try {
			condition = new Condition("UUID",UUID,TypeCondition.EQUALS);
			TableData tabledata = myapi.selectValues(tableProperties, condition);
			if(tabledata.getResponse() == ApiResponse.SUCCESS){
				if(tabledata.getMapValue().size() != 0){
					id = (Integer) tabledata.getMapValue().get(0);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
		return id;
	}

	public void loadAllData() {
			List<Integer> listPlayerId = getAllPlayerId();
			for(int playerId : listPlayerId){
				if(playerId != -1){
					UUID playerUUID = getPlayerUUID(playerId);
					double amount = getAccountAmountOfPlayer(playerId);
					ArrayList<Integer> idBanks = getBanksIdOfPlayer(playerId);
					BankData.listPocket.put(playerUUID, amount);
					if(idBanks.size() > 0){
						ArrayList<BankAccount> listBanks = getListOfBankAccount(idBanks);
						BankData.listBank.put(playerUUID, listBanks);
					}
					ArrayList<BookLog> listBook = getBookLogOfPlayer(playerId);
					BankData.listBookLog.put(playerUUID, listBook);
				}
			}
		}
	
	private UUID getPlayerUUID(int playerId) {
		String uuid = "";
		TableProperties tableProperties = new TableProperties(prefix+"Players",new String [] {"UUID"});
		Condition condition;
		try {
			condition = new Condition("ID",playerId,TypeCondition.EQUALS);
			TableData tabledata = myapi.selectValues(tableProperties, condition);
			if(tabledata.getResponse() == ApiResponse.SUCCESS){
				if(tabledata.getMapValue().size() != 0){
					uuid = (String) tabledata.getMapValue().get(0);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
		return uuid == null || uuid.isEmpty()?null:UUID.fromString(uuid);
	}

	private List<Integer> getAllPlayerId() {
		List<Integer> listId = new ArrayList<>();
		TableProperties tableProperties = new TableProperties(prefix+"Players",new String [] {"ID"});
		try {
			TableData tabledata = myapi.selectValues(tableProperties,null);
			if(tabledata.getResponse() == ApiResponse.SUCCESS){
				if(tabledata.getMapValue().size() != 0){
					for(Object ob : tabledata.getMapValue().values()){
							if(ob instanceof Integer){
								listId.add((Integer) ob);
							}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
		return listId;
	}

	public boolean insertInventory(Inventory inventory,int id_Bank){
		if(getInventoryID(inventory.getName(), id_Bank) == -1){
			try {
				TableProperties tableProperties = new TableProperties(prefix+"Inventories",new String[]{"Inventory_Name","Inventory_Size","Inventory_Stack_Size","Bank_ID"},new Object[]{inventory.getName(),inventory.getSize(),inventory.getMaxStackSize(),id_Bank});
				myapi.insertValues(tableProperties);
				int inventoryID = getInventoryID(inventory.getName(), id_Bank);
				ItemStack[] items = inventory.getContents();
				for(int x = 0; x < items.length;x++){
					if(items[x] == null)continue;
					insertItem(items[x],inventoryID,x);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalTypeException e) {
				e.printStackTrace();
			} catch (LengthTableException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	public void updateInventory(Inventory inventory,int id_bank){
		int id_inventory = getInventoryID(inventory.getName(),id_bank);
		try {
			TableProperties tableProperties = new TableProperties(prefix+"Inventories",new String[]{"Inventory_Name","Inventory_Size","Inventory_Stack_Size"},new Object[]{inventory.getName(),inventory.getSize(),inventory.getMaxStackSize()});
			Condition condition = new Condition("Inventory_Name",inventory.getName(),TypeCondition.EQUALS);
			myapi.updateValues(tableProperties,condition);
			ItemStack[] items = inventory.getContents();
			for(int x = 0; x < items.length;x++){
				int id_Item = getItemId(x);
				if(items[x] == null){
					if(id_Item != -1){
						removeItem(id_Item);
					}
					continue;
				}
				if(id_Item == -1){
					insertItem(items[x],id_inventory,x);
				} else {
				updateItem(items[x],id_inventory,x);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addLogEntry(UUID playerUUID, String nameLog,String descriptionLog, Date date, TransactionType transactType, TypeLog logType){
		try {
			int playerID = getPlayerId(playerUUID.toString());
			TableProperties tp = new TableProperties(prefix+"Logs",
					new String[]{"Log_PlayerID","Log_Name","Log_Description","Log_Date","Log_TransactType","Log_Type"},
					new Object[]{playerID,nameLog,descriptionLog,new SimpleDateFormat("yyyy-MM-dd").format(date),transactType.toString(),logType.toString()});
			myapi.insertValues(tp);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getBookId(UUID playerUUID, String nameLog,String descriptionLog, Date date, TransactionType transactType, TypeLog logType){
		int idBook = -1;	
		try {
			int playerID = getPlayerId(playerUUID.toString());
			TableProperties tp = new TableProperties(prefix+"Logs",new String[]{"ID_Log"});
			Condition condition = new Condition(new String[]{"Log_PlayerID","Log_Name","Log_Description","Log_Date","Log_TransactType","Log_Type"},
					new Object[]{playerID,nameLog,descriptionLog,new SimpleDateFormat("yyyy-MM-dd").format(date),transactType.toString(),logType.toString()},
					new TypeCondition[]{TypeCondition.EQUALS,TypeCondition.EQUALS,TypeCondition.EQUALS,TypeCondition.EQUALS,TypeCondition.EQUALS,TypeCondition.EQUALS},
					new boolean[]{true,true,true,true,true});
			TableData td = myapi.selectValues(tp, condition);
			if(td.getResponse() == ApiResponse.SUCCESS){
				if(td.getMapValue().size() != 0){
				idBook = (Integer) td.getMapValue().get(0);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
		return idBook;
	}
	
	private int getItemId(int itemFrame) {
		int id = -1;
		TableProperties tableProperties = new TableProperties(prefix+"Items",new String [] {"ID_Item"});
		Condition condition;
		try {
			condition = new Condition("Item_Frame",itemFrame,TypeCondition.EQUALS);
			TableData tabledata = myapi.selectValues(tableProperties, condition);
			if(tabledata.getResponse() == ApiResponse.SUCCESS){
				if(tabledata.getMapValue().size() != 0){
					id = (Integer) tabledata.getMapValue().get(0);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	private void removeItem(int itemID){
		try {
			Condition condition = new Condition("ID_Item",itemID,TypeCondition.EQUALS);
			myapi.deleteRow(prefix+"Items", condition);
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalTypeException e) {
				e.printStackTrace();
			}
	}

	private void insertItem(ItemStack item, int inventory_ID,int itemFrame) {
		try {
		TableProperties tableProperties = new TableProperties(prefix+"Items",new String[]{"Item_DisplayName","Item_Lores","Item_Enchantment","Item_Material","Item_Amount","Item_Frame","ID_Inventory"},
				new Object[]{item.hasItemMeta()?item.getItemMeta().getDisplayName():null,
						item.hasItemMeta()?Utils.getLoresInString(item.getItemMeta().getLore()):null,
								item.getEnchantments().size() > 0?Utils.getEnchantsInString(item.getItemMeta().getEnchants()):null,
										item.getType().toString(),item.getAmount(),itemFrame,inventory_ID});
		myapi.insertValues(tableProperties);
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateItem(ItemStack item, int inventory_ID, int itemFrame){
		try {
			TableProperties tableProperties = new TableProperties(prefix+"Items",new String[]{"Item_DisplayName","Item_Lores","Item_Enchantment","Item_Material","Item_Amount","Item_Frame","ID_Inventory"},new Object[]{item.hasItemMeta()?item.getItemMeta().getDisplayName():null,item.hasItemMeta()?Utils.getLoresInString(item.getItemMeta().getLore()):null,item.hasItemMeta()?Utils.getEnchantsInString(item.getItemMeta().getEnchants()):null,item.getType().toString(),item.getAmount(),itemFrame,inventory_ID});
			Condition condition = new Condition("Item_Frame",itemFrame,TypeCondition.EQUALS);
			myapi.updateValues(tableProperties,condition);
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalTypeException e) {
				e.printStackTrace();
			} catch (LengthTableException e) {
				e.printStackTrace();
			}
	}
		
	private int getInventoryID(String inventoryName,int id_Bank){
		int id = -1;
		TableProperties tableProperties = new TableProperties(prefix+"Inventories",new String [] {"ID_Inventory"});
		Condition condition;
		try {
			condition = new Condition("Bank_ID",id_Bank,TypeCondition.EQUALS);
			TableData tabledata = myapi.selectValues(tableProperties, condition);
			if(tabledata.getResponse() == ApiResponse.SUCCESS){
				if(tabledata.getMapValue().size() != 0){
					id = (Integer) tabledata.getMapValue().get(0);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	private ArrayList<BankAccount> getListOfBankAccount(ArrayList<Integer> idBanks){
		ArrayList<BankAccount> listBank = new ArrayList<BankAccount>();
		for(int id : idBanks){
			TableProperties tableProperties = new TableProperties(prefix+"BankAccount",new String[]{"BankName","Amount"});
			Condition condition;
			try{
				condition = new Condition("ID_Bank",id,TypeCondition.EQUALS);
				TableData td = myapi.selectValues(tableProperties, condition);
				if(td.getResponse() == ApiResponse.SUCCESS){
					Map<Integer, Object> data = td.getMapValue();
					if(data.size() != 0){
						String bankName = (String) data.get(0);
						double amount = (Double) data.get(1);
						BankAccount bankAccount = new BankAccount(bankName,amount);
						ArrayList<Inventory> listInv = getInventoryList(id);
						for(Inventory inv : listInv){
							bankAccount.getBankInventories().put(inv.getName(), inv.getContents());
						}
						listBank.add(bankAccount);
					}
				}
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			} catch (LengthTableException e) {
				e.printStackTrace();
			} catch (IllegalTypeException e) {
				e.printStackTrace();
			}
		}
		return listBank;
	}
	
	private ArrayList<BookLog> getBookLogOfPlayer(int playerId){
		ArrayList<BookLog> listBookLog = new ArrayList<BookLog>();
		try {
			TableProperties tp = new TableProperties(prefix+"Logs",
					new String[]{"ID_Log","Log_Name","Log_Description","Log_Date","Log_TransactType","Log_Type"});
			Condition condition = new Condition("Log_PlayerID",playerId,TypeCondition.EQUALS);
			TableData td = myapi.selectValues(tp, condition);
			if(td.getResponse() == ApiResponse.SUCCESS){
				Map<Integer,Object> data = td.getMapValue();
				if(data.size() != 0){
					int id = -1;
					String logName = "";
					String logDescription = "";
					Date logDate = null;
					TransactionType transactType = null;
					TypeLog logType = null;
					int indexTable = 1;
					for(int index = 0; index < data.size();index++){
						Object value = data.get(index);
						if(indexTable == 1){
							id = (Integer) value;
						}else if(indexTable == 2){
							logName = (String) value;
						} else if(indexTable == 3){
							logDescription = (String) value;
						} else if(indexTable == 4){
							String dateInString = (String) value;
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							logDate = dateFormat.parse(dateInString);
						} else if(indexTable == 5){
							String transactTypeInString = (String) value;
							if(TransactionType.ADD.toString().equals(transactTypeInString)){
								transactType = TransactionType.ADD;
							} else if(TransactionType.CREATE.toString().equals(transactTypeInString)){
								transactType = TransactionType.CREATE;
							} else if(TransactionType.DELETE.toString().equals(transactTypeInString)){
								transactType = TransactionType.DELETE;
							} else if(TransactionType.REMOVE.toString().equals(transactTypeInString)){
								transactType = TransactionType.REMOVE;
							}
						} else if(indexTable == 6){
							String logTypeInString = (String) value;
							if(TypeLog.INVENTORY.toString().equals(logTypeInString)){
								logType = TypeLog.INVENTORY;
							} else if(TypeLog.MONEY.toString().equals(logTypeInString)){
								logType = TypeLog.MONEY;
							}
						}
						int indexModulo = (index + 1) % 6;
						if(indexModulo == 0) {
							BookLog bookLog = new BookLog(logName, logDescription, logDate, transactType, logType, id);
							listBookLog.add(bookLog);
							indexTable = 1;
						} else {
							indexTable++;
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return listBookLog;
	}
	
	private double getAccountAmountOfPlayer(int playerId){
		double amount = 0.0;
		TableProperties tableProperties = new TableProperties(prefix+"Players",new String [] {"Amount_Pocket"});
		Condition condition;
		try {
			condition = new Condition("ID",playerId,TypeCondition.EQUALS);
			TableData tabledata = myapi.selectValues(tableProperties, condition);
			if(tabledata.getResponse() == ApiResponse.SUCCESS){
				Map<Integer, Object> data = tabledata.getMapValue();
				if(data.size() != 0){
					amount = (Double) data.get(0);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
		return amount;
	}
	
	private ArrayList<Inventory> getInventoryList(int bankId){
		ArrayList<Inventory> listInventory = new ArrayList<Inventory>();
		TableProperties tableProperties = new TableProperties(prefix+"Inventories",new String [] {"ID_Inventory","Inventory_Name","Inventory_Size","Inventory_Stack_Size"});
		Condition condition;
		try {
			condition = new Condition("Bank_ID",bankId,TypeCondition.EQUALS);
			TableData tabledata = myapi.selectValues(tableProperties, condition);
			if(tabledata.getResponse() == ApiResponse.SUCCESS){
				Map<Integer, Object> data = tabledata.getMapValue();
				if(data.size() != 0){
					int indexTable = 1;
					int id_inventory = -1;
					String inventoryName = "";
					int size = 0;
					int stackSize = 0;
					for(int index = 0; index < data.size();index++){
						Object value = data.get(index);
						if(indexTable == 1){
							id_inventory = (Integer) value;
						} else if(indexTable == 2){
							inventoryName = (String) value;
						} else if(indexTable == 3){
							size = (Integer) value;
						} else if(indexTable == 4){
							stackSize = (Integer) value;
						}
						
						if(indexTable < 4){
							indexTable++;
						} else {
							Inventory inv = Bukkit.createInventory(null, size, inventoryName);
							inv.setMaxStackSize(stackSize);
							inv.setContents(getItemStacks(id_inventory, size));
							listInventory.add(inv);
							indexTable = 1;
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
		return listInventory;
	}
	
	private ItemStack[] getItemStacks(int inventoryId,int size) {
		ItemStack[] items = new ItemStack[size];
		try {
			TableProperties tableProperties = new TableProperties(prefix+"Items",new String [] {"Item_DisplayName","Item_Lores","Item_Material","Item_Enchantment","Item_Amount","Item_Frame"});
			Condition condition = new Condition("ID_Inventory",inventoryId,TypeCondition.EQUALS);
			TableData tabledata = myapi.selectValues(tableProperties, condition);
			if(tabledata.getResponse() == ApiResponse.SUCCESS){
				Map<Integer, Object> data = tabledata.getMapValue();
				if(data.size() != 0){
					int indexTable = 1;
					String itemDisplayName = "";
					String itemMaterial = "";
					String itemLores = "";
					String itemEnchantment = "";
					int itemAmount = 0;
					int itemFrame = 0;
					for(int index = 0; index < data.size();index++){
						Object value = data.get(index);
						if(indexTable== 1){
							itemDisplayName = (String) value;
						} else if(indexTable == 2){
							itemLores = (String) value;
						} else if(indexTable == 3){
							itemMaterial = (String) value;
						} else if(indexTable == 4){
							itemEnchantment = (String) value;
						} else if(indexTable == 5){
							itemAmount = (Integer) value;
						} else if(indexTable == 6){
							itemFrame = (Integer) value;
						}
						
						if(indexTable < 6){
							indexTable++;
						} else {
							ItemStack item = new ItemStack(Material.getMaterial(itemMaterial),itemAmount);
							if(!itemDisplayName.equals("null") || !itemLores.equals("null") || !itemEnchantment.equals("null")){
								ItemMeta itemM = item.getItemMeta();
								if(!itemDisplayName.equals("null")){
								itemM.setDisplayName(itemDisplayName);
								}
								if(!itemLores.equals("null")){
									String[] list = itemLores.split("|");
									List<String> lore = new ArrayList<String>();
									for(String listLore : list){
										lore.add(listLore);
									}
									itemM.setLore(lore);
								}
								if(!itemEnchantment.equals("null")){
									String[] enchant = itemEnchantment.split(":");
									int i = 1;
									Enchantment enchantment = null;
									int level = 0;
									for(String ligne : enchant){
										if(i == 1){
											enchantment = Enchantment.getByName(ligne);
										}else if(i == 2) {
											level = Integer.parseInt(ligne);
										}
										
										if(i == 2){
											itemM.addEnchant(enchantment, level,true);
											i = 1;
										}
									}
								}
								item.setItemMeta(itemM);
							}
							items[itemFrame] = item;
							indexTable = 1;
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	private ArrayList<Integer> getBanksIdOfPlayer(int playerId){
		ArrayList<Integer> listIds = new ArrayList<Integer>();
		TableProperties tableProperties = new TableProperties(prefix+"BankAccount",new String[]{"ID_Bank"});
		Condition condition;
		try{
			condition = new Condition("Player_ID",playerId,TypeCondition.EQUALS);
			TableData td = myapi.selectValues(tableProperties, condition);
			if(td.getResponse() == ApiResponse.SUCCESS){
				Map<Integer, Object> data = td.getMapValue();
				if(data.size() != 0){
					for(Object id : data.values()){
					listIds.add((Integer) id);
					}
				}
			}
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		}
		return listIds;
	}
}
