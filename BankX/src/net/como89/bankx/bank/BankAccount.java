package net.como89.bankx.bank;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.items.Items;

public class BankAccount implements Cloneable {

	private String accountName;
	private double balance;
	private HashMap<String,Items[]> listInventaire;
	
	public BankAccount(String accountName){
		this.accountName = accountName;
		this.balance = 0.0;
		listInventaire = new HashMap<String,Items[]>();
	}
	
	public BankAccount(String accountName,double balance){
		this.accountName = accountName;
		this.balance = balance;
		listInventaire = new HashMap<String,Items[]>();
	}
	
	public double getBalance(){
		return balance;
	}
	
	public String getName(){
		return accountName;
	}
	
	public void modifyInventoryObjects(String inventoryName,
			ItemStack[] itemList) {
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
		listInventaire.put(inventoryName, items);
	}
	
	public HashMap<String, ItemStack[]> getBankInventories() {
		HashMap<String, ItemStack[]> listBankInventories = new HashMap<String,ItemStack[]>();
		for(String inventoryName : listInventaire.keySet()){
			Items[] items = listInventaire.get(inventoryName);
			ItemStack[] listItems = new ItemStack[items.length];
			for(int i = 0; i < listItems.length;i++){
				if(items[i] == null)
					continue;
				
				listItems[i] = Utils.getItemInItemStack(items[i]);
			}
			listBankInventories.put(inventoryName, listItems);
		}
		return listBankInventories;
	}
	
	public void clearInventory(){
		listInventaire.clear();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() throws CloneNotSupportedException {
		BankAccount clone = (BankAccount) super.clone();
		Object ob = listInventaire.clone();
		if(!(ob instanceof HashMap)){
			throw new CloneNotSupportedException();
		}
		HashMap<String,Items[]> listInventories = (HashMap<String,Items[]>) ob;
		Set<String> listInvName = listInventories.keySet();
		for(String invName : listInvName){
			Items[] items = clone.listInventaire.get(invName);
			Items[] listItems = new Items[items.length];
			for(int i = 0; i < items.length;i++){
				Items item = items[i];
				if(item == null)
					continue;
				listItems[i] = (Items) item.clone();
			}
			listInventories.put(invName, listItems);
		}
		clone.listInventaire = listInventories;
		return clone;
	}
	
	void setBalance(double amount){
		this.balance = amount;
	}
	
	void setName(String name){
		this.accountName = name;
	}
	
	HashMap<String,Items[]> getBankItems(){
		return listInventaire;
	}
}
