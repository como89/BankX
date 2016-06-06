package net.como89.bankx.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.items.InventoryItems;
import net.como89.bankx.bank.items.Items;

public class BankAccount {

	private String accountName;
	private double balance;
	ArrayList<InventoryItems> listInventaire;
	
	public BankAccount(String accountName){
		this.accountName = accountName;
		this.balance = 0.0;
		listInventaire = new ArrayList<>();
	}
	
	public BankAccount(String accountName,double balance){
		this.accountName = accountName;
		this.balance = balance;
		listInventaire = new ArrayList<>();
	}
	
	public double getBalance(){
		return balance;
	}
	
	public String getName(){
		return accountName;
	}
	
	public void putInventoriesItems(String inventoryName,
			ItemStack[] itemList,int maxStackSize) {
		Items[] items = new Items[itemList.length];
		for(int i = 0; i < items.length;i++){
			if(itemList[i] == null)
				continue;
			ItemStack item = itemList[i];
			String displayName = null;
			String loreList = null;
			String enchantmentList = null;
			if(item.hasItemMeta()){
				ItemMeta itemM = item.getItemMeta();
				
				if(itemM.hasDisplayName())
				displayName = itemM.getDisplayName();
				
				if(itemM.hasLore())
				loreList = Utils.getLoresInString(itemM.getLore());
			}
			Map<Enchantment,Integer> listEnchant = item.getEnchantments();
			if(listEnchant.size() > 0){
				enchantmentList = Utils.getEnchantsInString(listEnchant);
			}
			items[i] = new Items(displayName,item.getType().toString(),loreList,enchantmentList,item.getAmount());
		}
		InventoryItems inv = getInventoryItems(inventoryName);
		if(inv == null) {
		inv = new InventoryItems(inventoryName,itemList.length);
		}
		inv.setMaxStackSize(maxStackSize);
		inv.setContents(items);
		listInventaire.add(inv);
	}
	
	public HashMap<String, ItemStack[]> getBankInventories() {
		HashMap<String, ItemStack[]> listBankInventories = new HashMap<String,ItemStack[]>();
		for(InventoryItems inv : listInventaire){
			Items[] items = inv.getContents();
			ItemStack[] listItems = new ItemStack[items.length];
			for(int i = 0; i < listItems.length;i++){
				if(items[i] == null)
					continue;
				
				listItems[i] = Utils.getItemInItemStack(items[i]);
			}
			listBankInventories.put(inv.getName(), listItems);
		}
		return listBankInventories;
	}
	
	public void clearInventory(){
		listInventaire.clear();
	}
	
	void setBalance(double amount){
		this.balance = amount;
	}
	
	void setName(String name){
		this.accountName = name;
	}
	
	public InventoryItems getInventoryItems (String inventoryName) {
		for(InventoryItems inv : listInventaire) {
			if(inv.getName().equals(inventoryName)) {
				return inv;
			}
		}
		return null;
	}
}
