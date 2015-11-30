package net.como89.bankx.bank.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;
import net.como89.bankx.bank.Utils;

public class TransfertTypeInteract extends InventoryInteract {

	public TransfertTypeInteract(ManagerAccount managerAccount,ItemStack item, Player player, Inventory inv) {
		super(managerAccount);
		super.inv = inv;
		super.item = item;
		super.player = player;
	}

	@Override
	public void interact() {
		if(item.getType() == Material.SKULL_ITEM){
			String name = inv.getTitle().replace("Transfer Type to ", "");
			if(Utils.getOnlinePlayer(name) != null){
				player.closeInventory();
				inv = InventoriesBank.initialiseInventoryKeyPad(ChatColor.AQUA + "Transfer to " + ChatColor.GOLD + name,player.getUniqueId(),managerAccount);
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
//			player.closeInventory();
//			String name = inv.getTitle().replace("Transfer type to ", "");
//			inv = initialiseInventoryKeyPad(ChatColor.AQUA + "Transfer to " + ChatColor.GOLD + name + ChatColor.AQUA + " bank");
//			player.openInventory(inv);
//		}
	}

}
