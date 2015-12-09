package net.como89.bankx.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;
import net.como89.bankx.bank.api.BankXResponse;

public class BankCommands implements CommandExecutor {
	
	private ManagerAccount managerAccount;
	
	public BankCommands(ManagerAccount managerAccount){
		this.managerAccount = managerAccount;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("You aren't a player. You didn't have permission to do this command.");
			return true;
		}
		Player player = (Player) sender;
		String cmdName = cmd.getName();
		if(cmdName.equalsIgnoreCase("bank")){
			if(args.length == 1 && args[0].equalsIgnoreCase("help")){
				player.sendMessage("" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Help Command - Bank");
				player.sendMessage(ChatColor.GOLD + "/bank create [name] [amount]" + ChatColor.DARK_AQUA + " - create a bank account. You must specify the name of the account. The amount is optional. Zero is accepted.");
				player.sendMessage(ChatColor.GOLD + "/bank delete [name] " + ChatColor.DARK_AQUA + " - delete the bank account. The money from the bank account go back in your wallet.");
				player.sendMessage(ChatColor.GOLD + "/bank rename account <olderName> <newName> " + ChatColor.DARK_AQUA + " - Rename the account with a new name.");
				player.sendMessage(ChatColor.GOLD + "/bank rename inventory <bankAccountName> <olderName> <newName> " + ChatColor.DARK_AQUA + " - Rename the inventory into the bank account with a new name.");
				return true;
			}
			if(args.length == 2){
				if(args[0].equalsIgnoreCase("delete")){
					String name = args[1];
					double amount = managerAccount.deleteBankAccount(name,player.getUniqueId());
					if(amount != -1){
						player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.DELETE_BANK.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(), amount,name));
						managerAccount.addAmountPocket(player.getUniqueId(), amount);
					} else {
						player.sendMessage(ChatColor.RED + Language.BANK_ACCOUNT_NOT_EXIST.getMsg(managerAccount.getPlugin().getLanguage()));
					}
					return true;
				}
			}
			if(args.length >= 2){
				if(args[0].equalsIgnoreCase("create")){
					String name = args[1];
					double amount = 0.0;
					if(args.length > 2){
						amount = Double.parseDouble(args[2]);
					}
					if(managerAccount.getAmountPocket(player.getUniqueId()) >= amount){
						if(managerAccount.createBankAccount(player.getUniqueId(),name) == BankXResponse.SUCCESS){
							managerAccount.addAmountBankAccount(name,player.getUniqueId(), amount);
							player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.CREATE_BANK.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(), amount,name));
							managerAccount.removeAmountPocket(player.getUniqueId(), amount);
							return true;
						}
						else{
							player.sendMessage(ChatColor.RED + Language.BANK_ACCOUNT_ALREADY_EXIST.getMsg(managerAccount.getPlugin().getLanguage()));
							return true;
						}
					}
					player.sendMessage(ChatColor.RED + Language.NOT_ENOUGH_MONEY_ON_HIM.getMsg(managerAccount.getPlugin().getLanguage()));
					return true;
				}
			} 
			if(args.length == 4){
				if(args[0].equalsIgnoreCase("rename")){
					if(args[1].equalsIgnoreCase("account")){
						String olderName = args[2];
						String newName = args[3];
						if(managerAccount.changeBankName(olderName, newName,player.getUniqueId())){
							player.sendMessage(ChatColor.GREEN + managerAccount.replaceTag(Language.CHANGE_ACCOUNT_NAME.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(),0, newName));
							return true;
						}
						player.sendMessage(ChatColor.RED + managerAccount.replaceTag(Language.ACCOUNT_INVENTORY_NOT_EXIST.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(),0,olderName));
						return true;
					} 
				}
			}
			if(args.length == 5){
				if(args[0].equalsIgnoreCase("rename")){
					if(args[1].equalsIgnoreCase("inventory")){
						String bankName = args[2];
						String olderName = args[3];
						String newName = args[4];
						if(managerAccount.changeInventoryName(bankName, olderName, newName,player.getUniqueId())){
							player.sendMessage(ChatColor.GREEN + managerAccount.replaceTag(Language.CHANGE_ACCOUNT_NAME.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(),0, newName));
							return true;
						}
						player.sendMessage(ChatColor.RED + managerAccount.replaceTag(Language.ACCOUNT_INVENTORY_NOT_EXIST.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(),0,olderName));
						return true;
					}
				}
			}
		}
		return false;
	}

}
