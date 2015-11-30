package net.como89.bankx.bank.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.como89.bankx.bank.ManagerAccount;

public class TransfertPageInteract extends InventoryInteract {

	public TransfertPageInteract(ManagerAccount managerAccount,ItemStack item, ItemMeta itemM, Player player, Inventory inv) {
		super(managerAccount);
		super.inv = inv;
		super.item = item;
		super.itemM = itemM;
		super.player = player;
	}

	@Override
	public void interact() {
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

}
