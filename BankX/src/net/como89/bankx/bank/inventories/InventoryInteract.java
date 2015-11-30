package net.como89.bankx.bank.inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.ManagerAccount;

public abstract class InventoryInteract {

	protected ManagerAccount managerAccount;
	protected Inventory inv;
	protected ItemMeta itemM;
	protected Player player;
	protected ItemStack item;
	
	public InventoryInteract(ManagerAccount managerAccount){
		this.managerAccount = managerAccount;
	}
	
	public abstract void interact();
}
