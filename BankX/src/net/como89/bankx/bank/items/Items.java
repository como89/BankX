package net.como89.bankx.bank.items;

public class Items implements Cloneable {

	private String displayName;
	private String typeMaterial;
	private String listLore;
	private String enchantmentList;
	private int itemAmount;
	
	public Items(String displayName,String typeMaterial, String listLore,
			String enchantmentList,int itemAmount){
		this.displayName = displayName;
		this.typeMaterial = typeMaterial;
		this.listLore = listLore;
		this.enchantmentList = enchantmentList;
		this.itemAmount = itemAmount;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	public String getTypeMaterial(){
		return typeMaterial;
	}
	
	public boolean hasDisplayName(){
		return displayName != null;
	}
	
	public String getListLore(){
		return listLore;
	}
	
	public boolean hasLore(){
		return listLore != null;
	}
	
	public String getEnchantmentList(){
		return enchantmentList;
	}
	
	public boolean hasEnchantments(){
		return enchantmentList != null;
	}
	
	public int getAmount(){
		return itemAmount;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Items clone = (Items) super.clone();
		return clone;
	}
}
