package net.como89.bankx.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import net.como89.bankx.bank.BankAccount;
import net.como89.bankx.bank.InventoriesBank;
import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;
import net.como89.bankx.bank.Utils;
import net.como89.bankx.tasks.TaskSystem;
import net.como89.bankx.tasks.TaskType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EventPluginListener implements Listener {

	private ManagerAccount managerAccount;

	public EventPluginListener(ManagerAccount manageAccount) {
		this.managerAccount = manageAccount;
	}
	
	@EventHandler
	public void playerInteractOnEntity(PlayerInteractEntityEvent event){
		Player p = event.getPlayer();
		Entity entity = event.getRightClicked();
		if(entity.getType() == EntityType.VILLAGER){
			if(managerAccount.getPlugin().isNpcMinecraft()){
				Villager villager = (Villager) entity;
				String villagerName = villager.getCustomName();
				if(villagerName != null){
					if(villagerName.toLowerCase().contains("banker")){	
						event.setCancelled(true);
						if(managerAccount.hasBankAccount(p.getUniqueId())){
							String bankName = "";
							if((bankName = managerAccount.getSelectedBankAccount(p.getUniqueId())) == null){
								p.openInventory(InventoriesBank.initialiseInventoryChangeBank(managerAccount.getBanksAccountOfPlayer(p.getUniqueId())));
								return;
							}
							p.openInventory(InventoriesBank.initialiseInventoryMenu(managerAccount,bankName));
							p.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.WELCOME_MSG.getMsg(managerAccount.getPlugin().getLanguage()),p.getName(),0.0,""));
						} else {
							p.sendMessage(ChatColor.RED + Language.NO_BANK_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()));
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void playerCloseInventory(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
			if (event.getInventory().getTitle().contains("Deposit") 
					|| event.getInventory().getTitle().contains("Withdraw")) {
				closeInventory(player);
			}
			else if(event.getInventory().getTitle().contains("Transfer to")){
				managerAccount.clearPlayerInventories(player.getName());
				closeInventory(player);
			}
			else{
			BankAccount bankAccount = managerAccount.getBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId()));
			boolean existInventory = false;
			if(bankAccount == null || bankAccount.getBankInventories() == null)
				return;
			for(String inventoryName : bankAccount.getBankInventories().keySet()){
				if(inventoryName.equals(event.getInventory().getTitle())){
					existInventory = true;
					break;
				}
			}
				if(existInventory){
					bankAccount.modifyInventoryObjects(event.getInventory().getTitle(), event.getInventory().getContents());
						int id_Bank = managerAccount.getManageDatabase().getBankId(bankAccount.getName());
						if(!managerAccount.getManageDatabase().insertInventory(event.getInventory(), id_Bank)){
							 managerAccount.getManageDatabase().updateInventory(event.getInventory(), id_Bank);
						}
					closeInventory(player);
				}
			}
	}

	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		if(managerAccount.hasPocketAccount(playerUUID)){
			managerAccount.createPocket(playerUUID);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void interactPlayerInventory(InventoryClickEvent event) {
		HumanEntity human = event.getWhoClicked();
		Player player = (Player) human;
		ItemStack item = event.getCurrentItem();
		if(item == null){
			return;
		}
			ItemMeta itemM = item.getItemMeta();
			if(itemM != null)
			{
				Inventory inv = event.getInventory();
				if(inv.getType() != InventoryType.CHEST)return;
				if((event.isRightClick() || event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.COLLECT_TO_CURSOR || event.isShiftClick()) && (inv.getTitle().contains("Bank") ||
						inv.getTitle().contains("Inventories") || inv.getTitle().contains("Money") || inv.getTitle().contains("Transfer") || inv.getTitle().contains("Deposit") || inv.getTitle().contains("Withdraw") || inv.getTitle().contains("Choose the bank account"))){
					event.setCancelled(true);
				}
				if(itemM.getDisplayName() == null) return;
				if (inv.getTitle().contains(ChatColor.BOLD + "Bank")) {
					if (itemM.getDisplayName().contains("Items")) {
						if(player.getGameMode() == GameMode.CREATIVE){
							player.sendMessage(ChatColor.RED + Language.NO_CREATIVE.getMsg(managerAccount.getPlugin().getLanguage()));
							return;
						}	
						HashMap<String,ItemStack[]> listeInventaire = managerAccount.getAllInventoryOfTheBank(managerAccount.getSelectedBankAccount(player.getUniqueId()));
						Set<String> inventoryNames = listeInventaire.keySet();
						inv = initialiseMultiInventory(inv,inventoryNames);
						player.closeInventory();
						player.openInventory(inv);
					} else if (itemM.getDisplayName().contains("Money")) {
						player.closeInventory();
						player.openInventory(InventoriesBank.initialiseInventoryMoney(managerAccount, managerAccount.getSelectedBankAccount(player.getUniqueId())));
					} else if(itemM.getDisplayName().contains("BookLog")){
						player.closeInventory();
						managerAccount.giveBookLog(player);
					} else if(itemM.getDisplayName().contains("Manage BankAccounts")){
						managerAccount.selectBankAccount(player.getUniqueId(), "");
						player.closeInventory();
						player.openInventory(InventoriesBank.initialiseInventoryChangeBank(managerAccount.getBanksAccountOfPlayer(player.getUniqueId())));
					}
				}
				else if(inv.getTitle().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "Money")){
					if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Deposit")){
						inv = initialiseInventoryKeyPad(ChatColor.GREEN + ""+ ChatColor.BOLD + "Deposit",player.getUniqueId());
					player.openInventory(inv);
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.RED + "Withdraw")){
						inv = initialiseInventoryKeyPad(ChatColor.RED + "" + ChatColor.BOLD + "Withdraw",player.getUniqueId());
					  player.openInventory(inv);;
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Transfer")){
						managerAccount.addPlayerInventory(player.getName(),initialiseTransfertInventories(player.getName()));
						inv = managerAccount.getInventories(player.getName())[0];
						player.openInventory(inv);
					} else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.RED + "Cancel")){
						player.closeInventory();
						player.openInventory(InventoriesBank.initialiseInventoryMenu(managerAccount, managerAccount.getSelectedBankAccount(player.getUniqueId())));
					}
				}
				else if(inv.getTitle().contains("Manage Banks")){
					if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Add Bank")){
						ArrayList<BankAccount> listAccount = managerAccount.getBanksAccountOfPlayer(player.getUniqueId());
						managerAccount.createBankAccount(player.getUniqueId(), "bank" + listAccount.size());
						player.closeInventory();
						player.openInventory(InventoriesBank.initialiseInventoryChangeBank(listAccount));
					} else {
					String bankName = itemM.getDisplayName().replace(""+ChatColor.GREEN, "");
					player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.WELCOME_MSG.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(),0.0,""));
					managerAccount.selectBankAccount(player.getUniqueId(), bankName);
					player.closeInventory();
					player.openInventory(InventoriesBank.initialiseInventoryMenu(managerAccount, bankName));
					}
				}
				else if(inv.getTitle().contains("Deposit") || inv.getTitle().contains("Withdraw") || inv.getTitle().contains("Transfer to")){
					if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "0")){
						if(managerAccount.getAmountInOperation(player.getUniqueId()) != null){
							changeAmountOperation(player.getUniqueId(), "0", inv);
						player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
						}
						else{
							player.sendMessage(ChatColor.RED + "You can't begin with 0.");
						}
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "1")){
						changeAmountOperation(player.getUniqueId(), "1", inv);
						player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "2")){
						changeAmountOperation(player.getUniqueId(), "2", inv);
						player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "3")){
						changeAmountOperation(player.getUniqueId(), "3", inv);
						player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "4")){
						changeAmountOperation(player.getUniqueId(), "4", inv);
						player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "5")){
						changeAmountOperation(player.getUniqueId(), "5", inv);
						player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "6")){
						changeAmountOperation(player.getUniqueId(), "6", inv);
						player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "7")){
						changeAmountOperation(player.getUniqueId(), "7", inv);
						player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "8")){
						changeAmountOperation(player.getUniqueId(), "8", inv);
						player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
					}
					else if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "9")){
						changeAmountOperation(player.getUniqueId(), "9", inv);
						player.sendMessage(ChatColor.BLUE + managerAccount.getAmountInOperation(player.getUniqueId()));
					}
					else if(itemM.getDisplayName().contains("Ok")){
						if(managerAccount.getAmountInOperation(player.getUniqueId()) == null || managerAccount.getAmountInOperation(player.getUniqueId()).isEmpty()){
							player.sendMessage(ChatColor.RED + Language.OPERATION_NOT_EMPTY.getMsg(managerAccount.getPlugin().getLanguage()));
							return;
						}
						if(inv.getTitle().contains("Deposit")){
							double amountInOperation = Double.parseDouble(managerAccount.getAmountInOperation(player.getUniqueId()));
							if(managerAccount.getAmountPocket(player.getUniqueId()) >= amountInOperation){
							managerAccount.addAmountBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId()), amountInOperation);
							managerAccount.removeAmountPocket(player.getUniqueId(), amountInOperation);
							managerAccount.clearAmountInOperation(player.getUniqueId());
							if(managerAccount.getManageDatabase() == null){
							Bukkit.getScheduler().runTask(managerAccount.getPlugin(), new TaskSystem(managerAccount,TaskType.SAVING_DATA));
							}
							player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.ADD_MONEY_BANK.getMsg(managerAccount.getPlugin().getLanguage()), player.getName(), amountInOperation,""));
							callCloseInventory(player);
							}
							else{
								player.sendMessage(ChatColor.RED + Language.NOT_ENOUGH_MONEY_IN_WALLET.getMsg(managerAccount.getPlugin().getLanguage()));
								callCloseInventory(player);
							}
						}
						else if(inv.getTitle().contains("Withdraw")){
							double amountInOperation = Double.parseDouble(managerAccount.getAmountInOperation(player.getUniqueId()));
							if(managerAccount.getAmountInBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId())) >= amountInOperation){
								managerAccount.addAmountPocket(player.getUniqueId(), amountInOperation);
								managerAccount.removeAmountBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId()), amountInOperation);
								managerAccount.clearAmountInOperation(player.getUniqueId());
								if(managerAccount.getManageDatabase() == null){
									Bukkit.getScheduler().runTask(managerAccount.getPlugin(), new TaskSystem(managerAccount,TaskType.SAVING_DATA));
									}
								player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.REMOVE_MONEY_BANK.getMsg(managerAccount.getPlugin().getLanguage()), player.getName(), amountInOperation,""));
								callCloseInventory(player);
							}
							else{
								player.sendMessage(ChatColor.RED + managerAccount.replaceTag(Language.NOT_ENOUGH_MONEY_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()),"",0.0,managerAccount.getSelectedBankAccount(player.getUniqueId())));
								callCloseInventory(player);
							}
						 }
						else if (inv.getTitle().contains("Transfer to")){
							String name = inv.getTitle().replace("Transfer to ", "").replace(" bank", "").replace(""+ChatColor.AQUA, "").replace(""+ChatColor.GOLD, "");
							double amountInOperation = Double.parseDouble(managerAccount.getAmountInOperation(player.getUniqueId()));
							BankAccount bankAccount = managerAccount.getBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId()));
							if(managerAccount.getAmountInBankAccount(bankAccount.getName()) >= amountInOperation){
								if(inv.getTitle().endsWith("bank")){
									managerAccount.addAmountBankAccount(name, amountInOperation);
								} else {
									Player playerChoose = Utils.getOnlinePlayer(name);
									managerAccount.addAmountPocket(playerChoose.getUniqueId(), amountInOperation);
									playerChoose.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.RECEIVE_MONEY_FROM_PLAYER.getMsg(managerAccount.getPlugin().getLanguage()), player.getName(), amountInOperation, name));
								}
								managerAccount.removeAmountBankAccount(bankAccount.getName(), amountInOperation);
								managerAccount.clearAmountInOperation(player.getUniqueId());
								player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.REMOVE_MONEY_BANK.getMsg(managerAccount.getPlugin().getLanguage()), player.getName(), amountInOperation, (inv.getTitle().endsWith("bank")?" bank.":" wallet.")));
								callCloseInventory(player);
							}
							else{
								player.sendMessage(ChatColor.RED + managerAccount.replaceTag(Language.NOT_ENOUGH_MONEY_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()),"",0.0,managerAccount.getSelectedBankAccount(player.getUniqueId())));
								callCloseInventory(player);
							}
						}
					}
					else if(itemM.getDisplayName().contains("Correction")){
						managerAccount.clearAmountInOperation(player.getUniqueId());
						changeAmountOperation(player.getUniqueId(), "", inv);
						player.sendMessage(ChatColor.DARK_AQUA + Language.CLEAR_AMOUNT_OPERATION.getMsg(managerAccount.getPlugin().getLanguage()));
					}
					else if(itemM.getDisplayName().contains("Cancel")){
						callCloseInventory(player);
						callOpenInventory(player,InventoriesBank.initialiseInventoryMenu(managerAccount, managerAccount.getSelectedBankAccount(player.getUniqueId())));
					}
				}
				else if(inv.getTitle().contains("Inventories")){
					if(itemM.getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Add inventory")){
						BankAccount bankAccount = managerAccount.getBankAccount(managerAccount.getSelectedBankAccount(player.getUniqueId()));
						String inventoryName = "Inventory" + bankAccount.getBankInventories().size();
						bankAccount.modifyInventoryObjects(inventoryName,new ItemStack[9]);
						player.closeInventory();
						HashMap<String,ItemStack[]> listeInventaire = managerAccount.getAllInventoryOfTheBank(managerAccount.getSelectedBankAccount(player.getUniqueId()));
						Set<String> inventoryNames = listeInventaire.keySet();
						inv = initialiseMultiInventory(inv,inventoryNames);
						player.openInventory(inv);
					}
					else if(item.getType() == Material.CHEST){
						player.closeInventory();
						HashMap<String,ItemStack[]> listeInventaire = managerAccount.getAllInventoryOfTheBank(managerAccount.getSelectedBankAccount(player.getUniqueId()));
						ItemStack[] inventaire = listeInventaire.get(itemM.getDisplayName().replace(""+ChatColor.AQUA, ""));
						if(inventaire != null){
						inv = Bukkit.createInventory(null,inventaire.length,itemM.getDisplayName().replace(""+ChatColor.AQUA, ""));
						inv.setContents(inventaire);
						player.openInventory(inv);
						}
					}
				}
				else if(inv.getTitle().contains("Transfer Page #")){
					if(item.getType() == Material.SKULL_ITEM){
						player.closeInventory();
						String name = itemM.getDisplayName();
						String invName = "Transfer Type to <player>";
						invName = invName.replace("<player>", name);
						inv = Bukkit.createInventory(null, 9,invName);
						inv.setContents(InventoriesBank.itemsChoice);
						player.openInventory(inv);
					}
					else if(item.getType() == Material.WOOD_DOOR){
						player.closeInventory();
						String name = itemM.getDisplayName();
						String[] splitName = name.split("#");
						try{
						int index = Integer.parseInt(splitName[1]);
						inv = managerAccount.getInventories(player.getName())[index - 1];
						player.openInventory(inv);
						}catch(NumberFormatException e){
							e.printStackTrace();
						}
					} else if(item.getType() == Material.IRON_DOOR){
						player.closeInventory();
						player.openInventory(InventoriesBank.initialiseInventoryMoney(managerAccount, managerAccount.getSelectedBankAccount(player.getUniqueId())));
					}
				}
				else if(inv.getTitle().contains("Transfer Type to")){
						if(item.getType() == Material.SKULL_ITEM){
							String name = inv.getTitle().replace("Transfer Type to ", "");
							if(Utils.getOnlinePlayer(name) != null){
								player.closeInventory();
								inv = initialiseInventoryKeyPad(ChatColor.AQUA + "Transfer to " + ChatColor.GOLD + name,player.getUniqueId());
								player.openInventory(inv);
							}
							else{
								player.sendMessage(ChatColor.RED + Language.PLAYER_OFFLINE.getMsg(managerAccount.getPlugin().getLanguage()));
							}
						} else if(item.getType() == Material.IRON_DOOR){
							player.closeInventory();
							player.openInventory(InventoriesBank.initialiseInventoryMoney(managerAccount, managerAccount.getSelectedBankAccount(player.getUniqueId())));
						}
						//else if(item.getType() == Material.REDSTONE_TORCH_OFF){
//							player.closeInventory();
//							String name = inv.getTitle().replace("Transfer type to ", "");
//							inv = initialiseInventoryKeyPad(ChatColor.AQUA + "Transfer to " + ChatColor.GOLD + name + ChatColor.AQUA + " bank");
//							player.openInventory(inv);
//						}
				}
			}
	}
	
	private Inventory[] initialiseTransfertInventories(String playerName){
		OfflinePlayer[] listPlayer = Bukkit.getOfflinePlayers();
			int nbMenu = listPlayer.length / 54;
			Inventory[] listInv = new Inventory[1 + nbMenu];
			for(int i = 0; i < listInv.length;i++){
				listInv[i] = Bukkit.createInventory(null, 54,"Transfer Page #"+(i+1));
				if(listInv.length > 1){
				ItemStack gotoNext = new ItemStack(Material.WOOD_DOOR,1);
				ItemMeta im = gotoNext.getItemMeta();
				im.setDisplayName((i + 1 == listPlayer.length)?ChatColor.AQUA + "Page #1":ChatColor.AQUA + "Page #" + i + 2);
				gotoNext.setItemMeta(im);
				listInv[i].setItem(52, gotoNext);
				}
				ItemStack cancelButton = new ItemStack(Material.IRON_DOOR);
				ItemMeta itemM = cancelButton.getItemMeta();
				itemM.setDisplayName(ChatColor.RED + "Cancel");
				cancelButton.setItemMeta(itemM);
				listInv[i].setItem(53, cancelButton);
				
				int count = 0;
				for(OfflinePlayer offlinePlayer : listPlayer){
					ItemStack skull = makeItemSkull(offlinePlayer.getName());
					if(listInv[i].getItem(count) == null && managerAccount.hasBankAccount(offlinePlayer.getUniqueId()) && !playerName.equals(offlinePlayer.getName())){
						listInv[i].setItem(count,skull);
					}
					count++;
				}
				
				ItemStack itemglass = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)4);
				itemM = itemglass.getItemMeta();
				itemM.setDisplayName(" ");
				itemglass.setItemMeta(itemM);
				for(int j = 0;j < listInv[i].getSize();j++){
					if(listInv[i].getItem(j) == null)
					listInv[i].setItem(j, itemglass);
				}
		}
			return listInv;
	}
	
	private ItemStack makeItemSkull(String playerName){
		ItemStack item = new ItemStack(Material.SKULL_ITEM);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(playerName);
		item.setItemMeta(im);
		item.setDurability((short) SkullType.PLAYER.ordinal());
		return item;
	}
	
	private void changeAmountOperation(UUID playerUUID, String amount,Inventory inv){
		ItemStack item = inv.getItem(42);
		if(item != null){
		ItemMeta meta = item.getItemMeta();
		if(meta.getDisplayName().contains("Ok")){
			managerAccount.addAmountInOperation(playerUUID, amount);
			String newDisplay = meta.getDisplayName().substring(0,4);
			String amountInOperation = managerAccount.getAmountInOperation(playerUUID);
			if(amountInOperation != null) {
				newDisplay += " - " + amountInOperation + " " +  managerAccount.getPlugin().getRepresentMoney();
			}
			meta.setDisplayName(newDisplay);
			item.setItemMeta(meta);
			inv.setItem(42, item);
		}
		}
	}

	private Inventory initialiseMultiInventory(Inventory inv,Set<String> listInventory) {
		inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Inventories");
		for(int i = 0; i < listInventory.size();i++){
			ItemStack item = new ItemStack(Material.CHEST,1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + (String) listInventory.toArray()[i]);
			item.setItemMeta(meta);
			inv.setItem(i, item);
		}
		for(ItemStack items : inv.getContents()){
			if(items == null){
				ItemStack item = new ItemStack(Material.EMERALD,1);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.AQUA + "Add inventory");
				item.setItemMeta(meta);
				inv.addItem(item);
				break;
			}
		}
		return inv;
	}

	private Inventory initialiseInventoryKeyPad(String inventoryName,UUID playerUUID){
		Inventory inv = Bukkit.createInventory(null, 54, inventoryName);
		ItemStack[] items = new ItemStack[9];
		ItemStack item = new ItemStack(Material.IRON_INGOT,1);
		ItemMeta im;
		for(int i = 0; i < items.length; i++){
			items[i] = new ItemStack(Material.IRON_INGOT);
			if(i == 0){
			items[i].setAmount(i - 1);
			} else {
				items[i].setAmount(i + 1);
			}
			im = items[i].getItemMeta();
			im.setDisplayName(ChatColor.AQUA + "" + (i + 1));
			items[i].setItemMeta(im);
		}
		im = item.getItemMeta();
		item.setAmount(0);
		im.setDisplayName(ChatColor.AQUA + "0");
		item.setItemMeta(im);
		inv.setItem(31, item);
		
		inv.setItem(3, items[0]);
		inv.setItem(4, items[1]);
		inv.setItem(5, items[2]);
		inv.setItem(12, items[3]);
		inv.setItem(13, items[4]);
		inv.setItem(14, items[5]);
		inv.setItem(21, items[6]);
		inv.setItem(22, items[7]);
		inv.setItem(23, items[8]);
		
		ItemStack ok = new ItemStack(Material.EMERALD,1);
		im = ok.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "Ok");
		ok.setItemMeta(im);
		
		ItemStack correction = new ItemStack(Material.GOLD_INGOT,1);
		im = correction.getItemMeta();
		im.setDisplayName(ChatColor.YELLOW + "Correction");
		correction.setItemMeta(im);
		
		ItemStack cancel = new ItemStack(Material.REDSTONE,1);
		im = cancel.getItemMeta();
		im.setDisplayName(ChatColor.RED + "Cancel");
		cancel.setItemMeta(im);
		
		ItemStack displayBalance = new ItemStack(Material.SKULL_ITEM);
		im = displayBalance.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "Balance");
		displayBalance.setItemMeta(im);
		displayBalance.setDurability((short) SkullType.PLAYER.ordinal());
		
		inv.setItem(42, ok);
		inv.setItem(43, correction);
		inv.setItem(53, InventoriesBank.selectBalanceDisplay(displayBalance, managerAccount, managerAccount.getSelectedBankAccount(playerUUID)));
		inv.setItem(44, cancel);
		return inv;
	}
	
	private void closeInventory(Player player){
		managerAccount.clearAmountInOperation(player.getUniqueId());
		player.sendMessage(ChatColor.DARK_AQUA + Language.CLOSE_BANK_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()));
	}
	
	private void callCloseInventory(final Player player){
		Bukkit.getScheduler().scheduleSyncDelayedTask(managerAccount.getPlugin(), new Runnable(){
			@Override
			public void run() {
				player.closeInventory();
			}	
		},1L);
	}
	
	private void callOpenInventory(final Player player, final Inventory inv){
		Bukkit.getScheduler().runTaskLater(managerAccount.getPlugin(), new Runnable(){
			@Override
			public void run() {
				player.openInventory(inv);
			}}, 2L);
	}
}
