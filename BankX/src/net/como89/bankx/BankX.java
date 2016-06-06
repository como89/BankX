package net.como89.bankx;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.como89.bankx.bank.ManagerAccount;
import net.como89.bankx.bank.api.BankXApi;
import net.como89.bankx.bank.inventories.InventoriesBank;
import net.como89.bankx.commands.BankCommands;
import net.como89.bankx.commands.MoneyCommands;
import net.como89.bankx.events.InventoryInteraction;
import net.como89.bankx.events.PlayerConnection;
import net.como89.bankx.events.PlayerInteraction;
import net.como89.bankx.npc.Banker;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BankX extends JavaPlugin {
	
	private static Logger log;
	private static PluginDescriptionFile pdf;
	private static BankXApi bankXApi;
	
	private ManagerAccount managerAccount;
	
	private boolean minecraftNPC;
	private boolean useFeeSystem;
	private boolean countStack;
	private boolean useMysql;
	
	private double defaultAmount;
	private double feeSystemAmount;
	
	private String representMoney;
	private String feeSystem;
	private String typeFeeSystem;
	private String language;

	public void onEnable()
	{
		long timeStart = System.currentTimeMillis();
		File dossierData = new File("plugins/BankX/Data/");
		dossierData.mkdirs();
		this.saveDefaultConfig();
		log = this.getLogger();
		pdf = this.getDescription();
		log.warning("DEVBUILD - This build is a dev build. You can have bugs and crashes. Please report the bugs, if you encounter them.");
		if(getServer().getPluginManager().getPlugin("MyApi") == null || !getServer().getPluginManager().getPlugin("MyApi").getDescription().getVersion().equals("1.3")){
			logWarning("You need MyApi 1.3 to use this plugin.");
			this.setEnabled(false);
			return;
		}
		loadConfig();
		if(!this.isEnabled()){
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		if(!minecraftNPC){
			if(getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
				getLogger().log(Level.SEVERE, "Citizens 2.0.10 and + not found or not enabled");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
			logInfo("Use Citizens!");
			CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(Banker.class).withName("Banker"));
		}
		else {
			logInfo("Use Minecraft NPC!");
		}
		bankXApi = new BankXApi(managerAccount);
//		if(useFeeSystem){
//			logInfo("Start FeeSystem!");
//			if(feeSystem.equalsIgnoreCase("minutes")){
//			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TaskStoringItems(managerAccount), 20 * 60, 20 * 60);
//			}
//			if(feeSystem.equalsIgnoreCase("daily")){
//			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TaskSystem(managerAccount,TaskType.FEESYSTEM), 20 * 86400, 20 * 86400);
//			}
//			else if(feeSystem.equalsIgnoreCase("weekly")){
//			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TaskSystem(managerAccount,TaskType.FEESYSTEM), 20 * 604800, 20 * 604800);
//			}
//		}
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new InventoryInteraction(managerAccount), this);
		pm.registerEvents(new PlayerConnection(managerAccount), this);
		pm.registerEvents(new PlayerInteraction(managerAccount), this);
		
		getCommand("money").setExecutor(new MoneyCommands(managerAccount));
		getCommand("bank").setExecutor(new BankCommands(managerAccount));
		
		InventoriesBank.initialiseInventory();
		
		if(this.getServer().getPluginManager().getPlugin("Vault") != null){
		VaultHook.hookToVault(this);
		}
		
		logInfo("Made by <<" + pdf.getAuthors().get(0) + ">> and test by <<"+pdf.getAuthors().get(1)+">>");
		logInfo("Its take " + (System.currentTimeMillis() - timeStart) + " milliseconds to load this plugin.");
		logInfo("Plugin enable!");
	}

	public BankXApi getBankXAPI(){
		return bankXApi;
	}
	
	@Override
	public void onDisable()
	{
		logInfo("Plugin disable!");
	}
	
	public double getDefaultAmount(){
		return defaultAmount;
	}
	
	public boolean isNpcMinecraft(){
		return minecraftNPC;
	}
	
	public String getLanguage(){
		return language;
	}
	
	public String getRepresentMoney(){
		return representMoney;
	}
	
	public boolean isUseMySQL(){
		return useMysql;
	}
	
	public boolean isFeeSystem(){
		return useFeeSystem;
	}
	
	public double getFeeSystemAmount(){
		return feeSystemAmount;
	}
	
	public boolean isCountStack(){
		return countStack;
	}
	
	public String getFeeSystem(){
		return feeSystem;
	}
	
	public String getTypeFeeSystem(){
		return typeFeeSystem;
	}
	
	public void logInfo(String message)
	{
		log.info(message);
	}
	
	public void logWarning(String message){
		log.warning(message);
	}
	
	private void loadConfig(){
		this.reloadConfig();
		language = this.getConfig().getString("language");
		defaultAmount = this.getConfig().getDouble("default_amount");
		minecraftNPC = this.getConfig().getBoolean("minecraft_npc");
		representMoney = this.getConfig().getString("money");
		useFeeSystem = this.getConfig().getBoolean("feeSystem.use");
		if(useFeeSystem){
			feeSystemAmount = this.getConfig().getDouble("feeSystem.money");
			feeSystem = this.getConfig().getString("feeSystem.system");
			typeFeeSystem = this.getConfig().getString("feeSystem.type");
			countStack = this.getConfig().getBoolean("feeSystem.countStack");
		}
		useMysql = this.getConfig().getBoolean("mysql.use");
		managerAccount = new ManagerAccount(this);
//		if(useMysql){
//				boolean connected = manageDatabase.connectToDatabase(this.getConfig().getString("mysql.host"),
//						this.getConfig().getInt("mysql.port"),
//						this.getConfig().getString("mysql.username"),
//						this.getConfig().getString("mysql.password"),
//						this.getConfig().getString("mysql.database"));
//				if(connected){
//					if(!manageDatabase.createTables()){
//						log.warning("Problems with database. Check your log.");
//					}
//					manageDatabase.loadAllData(managerAccount);
//					log.info("[Database] All data are load.");
//				}
//		}
	}
}
