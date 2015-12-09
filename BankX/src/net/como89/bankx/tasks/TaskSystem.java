package net.como89.bankx.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.como89.bankx.bank.BankAccount;
import net.como89.bankx.bank.FileManager;
import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;
import net.como89.bankx.bank.PlayerData;
import net.como89.bankx.bank.Utils;
import net.como89.bankx.bank.api.BankXResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

@Deprecated
public class TaskSystem extends BukkitRunnable {

	private ManagerAccount managerAccount;
	private TaskType taskType;
	
	public TaskSystem(ManagerAccount managerAccount,TaskType taskType){
		this.managerAccount = managerAccount;
		this.taskType = taskType;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void run() {
		switch(taskType){
		case FEESYSTEM :
			for(OfflinePlayer playerOffline : Bukkit.getOfflinePlayers()){
				for(BankAccount bankAccount : managerAccount.getBanksAccountOfPlayer(playerOffline.getUniqueId())){
					if(managerAccount.getAmountInBankAccount(bankAccount.getName(),playerOffline.getUniqueId()) != -1){
						double amountItem = 0.0;
						if((amountItem = getAmountOfAllItem(bankAccount.getName(),playerOffline.getUniqueId())) != 0.0){
							double total = amountItem * managerAccount.getPlugin().getFeeSystemAmount();
							if(managerAccount.removeAmountBankAccount(bankAccount.getName(),playerOffline.getUniqueId(), total) == BankXResponse.SUCCESS){
								managerAccount.removeBankInList(bankAccount.getName());
								if(playerOffline.isOnline()){
									playerOffline.getPlayer().sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.DEBITED_PAYMENT_ITEMS.getMsg(managerAccount.getPlugin().getLanguage()),bankAccount.getName(),total,""));
								}
							}
							else{
									if(managerAccount.checkBankInList(bankAccount.getName())){
										managerAccount.clearInventory(bankAccount.getName(),playerOffline.getUniqueId());
										if(playerOffline.isOnline()){
										playerOffline.getPlayer().sendMessage(ChatColor.RED + Language.INVENTORY_EMPTY.getMsg(managerAccount.getPlugin().getLanguage()));
										}
										managerAccount.removeBankInList(bankAccount.getName());
										continue;
									}
									if(playerOffline.isOnline()){
										playerOffline.getPlayer().sendMessage(ChatColor.RED + managerAccount.replaceTag(Language.WARNING_FINAL_PAYMENT.getMsg(managerAccount.getPlugin().getLanguage()),bankAccount.getName(),total,""));
									}
									managerAccount.addBankInList(bankAccount.getName());
								}
							}
						}
				}
			}
			break;
		case LOADING_DATA:
			if(managerAccount.getBankManager() != null && managerAccount.getPocketManager() != null){
				if(managerAccount.getBankManager().initiateReader()){
					managerAccount.getBankManager().readDataBank();
					managerAccount.getPlugin().logInfo("DataBank read!");
				}
				if(managerAccount.getPocketManager().initiateReader()){
					managerAccount.getPocketManager().readDataPocket();
					managerAccount.getPlugin().logInfo("DataPocket read!");
				}
				loadInventories();
			}
			break;
		}
	}
	
	private double getAmountOfAllItem(String bankName,UUID playerUUID){
		double amountItem = 0.0;
		HashMap<String, ItemStack[]> listeInventaire = managerAccount.getAllInventoryOfTheBank(bankName,playerUUID);
		if(listeInventaire.size() > 0){
			for(ItemStack[] inventaire : listeInventaire.values()){
				for(ItemStack item : inventaire){
					if(item != null){
						if(managerAccount.getPlugin().isCountStack()){
							amountItem += item.getAmount();
						}else{
							amountItem++;
						}
					}
				}
			}
		}
		return amountItem;
	}
	
	@Deprecated
	private void loadInventories(){
		ArrayList<String> allFolder = Utils.getAllFolder(new File("plugins/BankX/Data/"));
		for(String folderPath : allFolder){
			File folder = new File(folderPath);
			String bank = folder.getName().split("_")[0];
			File[] allFile = Utils.getFilesInDirectory(folder);
			for(File file : allFile){
				FileManager fileInvManager = new FileManager(file,true,managerAccount);
				Inventory inv = fileInvManager.getInventoryFromSerializableString();
				if(inv != null){
					BankAccount bankAccount = managerAccount.getBankAccount(bank,UUID.fromString(bank));
					if(bankAccount != null){
					    bankAccount.modifyInventoryObjects(inv.getTitle(),inv.getContents());
					}
				}
			}
		}
		for(PlayerData playerData : managerAccount.getAllPlayerAccounts()){
			UUID playerUUID = playerData.getUniqueId();
			for(BankAccount bankAccount : managerAccount.getBanksAccountOfPlayer(playerUUID)){
				if(managerAccount.getAllInventoryOfTheBank(bankAccount.getName(),playerUUID).size() == 0){
					 bankAccount.modifyInventoryObjects("Inventory", new ItemStack[54]);
				}
			}
		}
	managerAccount.getPlugin().logInfo("Inventories load!");
}
}
