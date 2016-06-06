package net.como89.bankx.bank.items;

public class InventoryItems {

	private Items[] listItems;
	private String inventoryName;
	private int maxStackSize;
	
	public InventoryItems(String inventoryName,int size) {
		this.inventoryName = inventoryName;
		this.listItems = new Items[size];
	}
	
	public String getName() {
		return inventoryName;
	}
	
	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	public void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
	}
	
	public Items[] getContents() {
		return listItems;
	}
	
	public void setContents(Items[] listItems) {
		this.listItems = listItems;
	}
	
	public int getSize() {
		return listItems.length;
	}
}
