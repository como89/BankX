package net.como89.bankx.bank;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

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
import net.como89.bankx.bank.items.InventoryItems;
import net.como89.bankx.bank.items.Items;
import net.como89.bankx.bank.logsystem.BookLog;
import net.como89.bankx.bank.logsystem.TransactionType;
import net.como89.bankx.bank.logsystem.TypeLog;

public class ManageDatabase {
	
	private static ManageDatabase manageDatabase;

	private MyApi myapi;
	private String prefix;
	
	private ManageDatabase(String prefix){
		myapi = MyApiLib.createInstance("Bankx");
		this.prefix = prefix;
	}
	
	public static void initDatabaseInstance(String prefix,String... params) {
		manageDatabase = new ManageDatabase(prefix);
		manageDatabase.init(params);
	}
	
	public static ManageDatabase getDatabaseInstance() {
		return manageDatabase;
	}
	
	private void init(String... params) {
		if(params.length == 5) {
		myapi.init(params[0], Integer.parseInt(params[1]), params[2], params[3], params[4]);
		} else {
			myapi.init(params[0]);
		}
	}
	
	public boolean connectToDatabase(){	
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
		Columns bank_name = new Columns("BankName",TypeData.VARCHAR,250,false, false, false,false);
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
	
	public void addPocketOfPlayer(String UUID,double amount){
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
	
	public void updateAmountBank(String bankName, double amount,String UUID){
		try {
			TableProperties tableProperties = new TableProperties(prefix+"BankAccount",new String[]{"Amount"},new Object[]{amount});
			int playerID = getPlayerId(UUID);
			Condition condition = new Condition(new String[]{"BankName","Player_ID"},new Object[]{bankName,playerID},
					new TypeCondition[]{TypeCondition.EQUALS,TypeCondition.EQUALS},new boolean[]{true});
			myapi.updateValues(tableProperties, condition);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteBankAccount(String bankName,String UUID){
		try {
			int playerID = getPlayerId(UUID);
			Condition condition = new Condition(new String[]{"BankName","Player_ID"},new Object[]{bankName,playerID},
					new TypeCondition[]{TypeCondition.EQUALS,TypeCondition.EQUALS},new boolean[]{true});
			myapi.deleteRow(prefix+"BankAccount", condition);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalTypeException e) {
			e.printStackTrace();
		} catch (LengthTableException e) {
			e.printStackTrace();
		} 
	}
	
	public void createBankAccount(String bankName, String uuid){
		int playerID = getPlayerId(uuid);
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
	
	public void updateBankName(String olderName, String newName,String uuid){
		int playerID = getPlayerId(uuid);
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
	
	public void updateInventoryName(String bankAccountName, String olderName, String newName,String uuid){
		int bankId = getBankId(bankAccountName,uuid);
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

	public void loadAllData(ManagerAccount managerAccount) {
			List<Integer> listPlayerId = getAllPlayerId();
			for(int playerId : listPlayerId){
				if(playerId != -1){
					UUID playerUUID = getPlayerUUID(playerId);
					double amount = getAccountAmountOfPlayer(playerId);
					ArrayList<Integer> idBanks = getBanksIdOfPlayer(playerId);
					PlayerData playerData = new PlayerData(playerUUID,amount);
					if(idBanks.size() > 0){
						ArrayList<BankAccount> listBanks = getListOfBankAccount(idBanks);
						playerData.listBanksAccount = listBanks;
					}
					ArrayList<BookLog> listBook = getBookLogOfPlayer(playerId);
					playerData.listBookLog = listBook;
					managerAccount.bankData.listPlayerData.add(playerData);
				}
			}
		}
	
	private int getBankId(String bankName,String uuid){
		int id = -1;
		int idPlayer = getPlayerId(uuid);
		TableProperties tableProperties = new TableProperties(prefix+"BankAccount",new String [] {"ID_Bank"});
		Condition condition;
		try {
			condition = new Condition(new String[]{"BankName","Player_ID"},new Object[]{bankName,idPlayer},new TypeCondition[]{TypeCondition.EQUALS,TypeCondition.EQUALS},new boolean[] {true});
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
	
	private int getPlayerId(String UUID){
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

	public boolean insertInventory(String bankName,String inventoryName,Items[] listItems,int maxStackSize,String uuid){
		int id_Bank = getBankId(bankName, uuid);
		if(getInventoryID(inventoryName, id_Bank) == -1){
			try {
				TableProperties tableProperties = new TableProperties(prefix+"Inventories",new String[]{"Inventory_Name","Inventory_Size","Inventory_Stack_Size","Bank_ID"},new Object[]{inventoryName,listItems.length,maxStackSize,id_Bank});
				myapi.insertValues(tableProperties);
				int inventoryID = getInventoryID(inventoryName, id_Bank);
				for(int x = 0; x < listItems.length;x++){
					if(listItems[x] == null)continue;
					insertItem(listItems[x],inventoryID,x);
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
	
	public void updateInventory(String bankName,String inventoryName,Items[] listItems,int maxStackSize,String uuid){
		int id_Bank = getBankId(bankName, uuid);
		int id_inventory = getInventoryID(inventoryName,id_Bank);
		try {
			TableProperties tableProperties = new TableProperties(prefix+"Inventories",new String[]{"Inventory_Name","Inventory_Size","Inventory_Stack_Size"},new Object[]{inventoryName,listItems.length,maxStackSize});
			Condition condition = new Condition("Inventory_Name",inventoryName,TypeCondition.EQUALS);
			myapi.updateValues(tableProperties,condition);
			for(int x = 0; x < listItems.length;x++){
				int id_Item = getItemId(x,id_inventory);
				if(listItems[x] == null){
					if(id_Item != -1){
						removeItem(id_Item);
					}
					continue;
				}
				if(id_Item == -1){
					insertItem(listItems[x],id_inventory,x);
				} else {
				updateItem(listItems[x],id_inventory,x);
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
	
	
	public int addLogEntry(UUID playerUUID, String nameLog,String descriptionLog, String date, String transactType, String logType){
		try {
			int playerID = getPlayerId(playerUUID.toString());
			TableProperties tp = new TableProperties(prefix+"Logs",
					new String[]{"Log_PlayerID","Log_Name","Log_Description","Log_Date","Log_TransactType","Log_Type"},
					new Object[]{playerID,nameLog,descriptionLog,date,transactType,logType});
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
		return getBookId(playerUUID, nameLog, descriptionLog, date, transactType, logType);
	}
	
	private int getBookId(UUID playerUUID, String nameLog,String descriptionLog, String date, String transactType, String logType){
		int idBook = -1;	
		try {
			int playerID = getPlayerId(playerUUID.toString());
			TableProperties tp = new TableProperties(prefix+"Logs",new String[]{"ID_Log"});
			Condition condition = new Condition(new String[]{"Log_PlayerID","Log_Name","Log_Description","Log_Date","Log_TransactType","Log_Type"},
					new Object[]{playerID,nameLog,descriptionLog,date,transactType,logType},
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
	
	private int getItemId(int itemFrame,int inventory_id) {
		int id = -1;
		TableProperties tableProperties = new TableProperties(prefix+"Items",new String [] {"ID_Item"});
		Condition condition;
		try {
			condition = new Condition(new String[]{"Item_Frame","ID_Inventory"},
					new Object[] {itemFrame,inventory_id},
					new TypeCondition[]{TypeCondition.EQUALS,TypeCondition.EQUALS},new boolean[] {true});
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

	private void insertItem(Items item, int inventory_ID,int itemFrame) {
		try {
		TableProperties tableProperties = new TableProperties(prefix+"Items",new String[]{"Item_DisplayName","Item_Lores","Item_Enchantment","Item_Material","Item_Amount","Item_Frame","ID_Inventory"},
				new Object[]{item.hasDisplayName()?item.getDisplayName():null,
						item.hasLore()?item.getListLore():null,
								item.hasEnchantments()?item.getEnchantmentList():null,
										item.getTypeMaterial(),item.getAmount(),itemFrame,inventory_ID});
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
	
	private void updateItem(Items item, int inventory_ID, int itemFrame){
		try {
			TableProperties tableProperties = new TableProperties(prefix+"Items",
					new String[]{"Item_DisplayName","Item_Lores","Item_Enchantment","Item_Material","Item_Amount","Item_Frame","ID_Inventory"},
					new Object[]{item.hasDisplayName()?item.getDisplayName():null,
							item.hasLore()?item.getListLore():null,
									item.hasEnchantments()?item.getEnchantmentList():null,
											item.getTypeMaterial(),item.getAmount(),itemFrame,inventory_ID});
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
						ArrayList<InventoryItems> listInv = getInventoryList(id);
						bankAccount.listInventaire = listInv;
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
							BookLog bookLog = new BookLog(logName, logDescription, logDate, transactType, logType);
							bookLog.setID(id);
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
	
	private ArrayList<InventoryItems> getInventoryList(int bankId){
		ArrayList<InventoryItems> listInventory = new ArrayList<>();
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
							InventoryItems inv = new InventoryItems(inventoryName,size);
							inv.setMaxStackSize(stackSize);
							inv.setContents(getItems(id_inventory, size));
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
	
	private Items[] getItems(int inventoryId,int size) {
		Items[] items = new Items[size];
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
							Items item = new Items(itemDisplayName,itemMaterial,itemLores,itemEnchantment,itemAmount);
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
