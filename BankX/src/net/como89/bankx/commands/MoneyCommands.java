package net.como89.bankx.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.como89.bankx.bank.BankAccount;
import net.como89.bankx.bank.Language;
import net.como89.bankx.bank.ManagerAccount;
import net.como89.bankx.bank.Utils;
import net.como89.bankx.bank.api.BankXResponse;

public class MoneyCommands implements CommandExecutor {
	
	private ManagerAccount managerAccount;
	
	public MoneyCommands(ManagerAccount managerAccount){
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
		if(cmdName.equalsIgnoreCase("money")){
			if(args.length == 0){
				player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.WALLET_SHOW.getMsg(managerAccount.getPlugin().getLanguage()), player.getName(), managerAccount.getAmountPocket(player.getUniqueId()),""));
				ArrayList<BankAccount> listBanks = (ArrayList<BankAccount>) managerAccount.getBanksAccountOfPlayer(player.getUniqueId());
				if(listBanks != null && listBanks.size() > 0){
					player.sendMessage("" + ChatColor.DARK_AQUA + ChatColor.BOLD + "List of banks");
					for(BankAccount bank : listBanks){
						player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.BANK_ACCOUNT_SHOW.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(),bank.getBalance(),bank.getName()));
					}
				}else{
					player.sendMessage(ChatColor.RED + Language.NO_BANK_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()));
				}
				return true;
			}
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("help")){
					player.sendMessage("" + ChatColor.GOLD + ChatColor.BOLD + "Help Command - Money");
					player.sendMessage(ChatColor.GOLD + "/money " + ChatColor.GRAY +  " -  displays his wallet and his money in the bank account.");
					player.sendMessage(ChatColor.GOLD + "/money [player] " + ChatColor.GRAY +  " - displays the wallet of the player and the money of the player in each bank account.");
					player.sendMessage(ChatColor.GOLD + "/money pay [player] [money] " + ChatColor.GRAY +  " - can pay money to a player from a wallet to another.");
					if(player.isOp()){
					player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "-- ADMIN commands --");
					player.sendMessage(ChatColor.GOLD + "/money add [player] [money] " + ChatColor.GRAY +  " - add money in the wallet for the specified player. For oneself, mark your nickname.");
					player.sendMessage(ChatColor.GOLD + "/money remove [player] [money] " + ChatColor.GRAY +  " - removes the money in the wallet for the specified player.");
					}
					return true;
				}
				String playerName = args[0];
				OfflinePlayer playerArg = Utils.getOfflinePlayer(playerName);
				if(playerArg == null){
					player.sendMessage(ChatColor.RED + Language.PLAYER_NOT_EXIST.getMsg(managerAccount.getPlugin().getLanguage()));
					return true;
				}
				player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.WALLET_SHOW_PLAYER.getMsg(managerAccount.getPlugin().getLanguage()), playerName, managerAccount.getAmountPocket(playerArg.getUniqueId()),""));
				ArrayList<BankAccount> listBanks = (ArrayList<BankAccount>) managerAccount.getBanksAccountOfPlayer(playerArg.getUniqueId());
				if(listBanks != null && listBanks.size() > 0){
					player.sendMessage("" + ChatColor.DARK_AQUA +  ChatColor.BOLD + "List of banks");
					for(BankAccount bank : listBanks){
						player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.BANK_ACCOUNT_PLAYER_SHOW.getMsg(managerAccount.getPlugin().getLanguage()),playerName,bank.getBalance(),bank.getName()));
					}
				}
				else{
					player.sendMessage(ChatColor.RED + Language.PLAYER_NOT_HAVE_BANK_ACCOUNT.getMsg(managerAccount.getPlugin().getLanguage()));
				}
				return true;
			}
			if(args.length == 3){
				if(args[0].equalsIgnoreCase("pay")){
					String playerName = args[1];
					if(playerName.equalsIgnoreCase(player.getName())){
						player.sendMessage(ChatColor.RED + Language.NOT_YOURSELF.getMsg(managerAccount.getPlugin().getLanguage()));
						return true;
					}
					OfflinePlayer playerArg = Utils.getOnlinePlayer(playerName);
					if(playerArg == null){
						player.sendMessage(ChatColor.RED + Language.PLAYER_OFFLINE.getMsg(managerAccount.getPlugin().getLanguage()));
						return true;
					}
					Player playerArgO = playerArg.getPlayer(); 
					try{
					double amount = Double.parseDouble(args[2]);
						if(managerAccount.hasPocketAccount(playerArgO.getUniqueId())){
							if(managerAccount.removeAmountPocket(player.getUniqueId(), amount) == BankXResponse.SUCCESS){
								managerAccount.addAmountPocket(playerArgO.getUniqueId(), amount);
								player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.PAY_PLAYER.getMsg(managerAccount.getPlugin().getLanguage()),playerName, amount,""));
								playerArgO.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.RECEIVE_MONEY_FROM_PLAYER.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(), amount,""));
								return true;
							}
							player.sendMessage(ChatColor.RED + Language.NOT_ENOUGH_MONEY_IN_WALLET.getMsg(managerAccount.getPlugin().getLanguage()));
							return true;
						}
					}
					catch(Exception ex){
						player.sendMessage(ChatColor.RED + Language.PARAMETER_NOT_ENTER_CORRECTLY.getMsg(managerAccount.getPlugin().getLanguage()));
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("add")){
					String playerName = args[1];
					if(!player.isOp()){
						player.sendMessage(ChatColor.RED + Language.NO_PERMISSION.getMsg(managerAccount.getPlugin().getLanguage()));
						return true;
					}
					OfflinePlayer playerArg = Utils.getOnlinePlayer(playerName);
					if(playerArg == null){
						player.sendMessage(ChatColor.RED + Language.PLAYER_OFFLINE.getMsg(managerAccount.getPlugin().getLanguage()));
						return true;
					}
					Player playerArgO = playerArg.getPlayer();
					try{
						double amount = Double.parseDouble(args[2]);
							if(managerAccount.hasPocketAccount(playerArgO.getUniqueId())){
									managerAccount.addAmountPocket(playerArgO.getUniqueId(), amount);
									player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.ADD_MONEY.getMsg(managerAccount.getPlugin().getLanguage()),playerName, amount,""));
									playerArgO.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.RECEIVE_MONEY_FROM_PLAYER.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(), amount,""));
									return true;
							}
						}
						catch(Exception ex){
							player.sendMessage(ChatColor.RED + Language.PARAMETER_NOT_ENTER_CORRECTLY.getMsg(managerAccount.getPlugin().getLanguage()));
							return true;
						}
				}
				if(args[0].equalsIgnoreCase("remove")){
					String playerName = args[1];
					if(!player.isOp()){
						player.sendMessage(ChatColor.RED + Language.NO_PERMISSION.getMsg(managerAccount.getPlugin().getLanguage()));
						return true;
					}
					OfflinePlayer playerArg = Utils.getOnlinePlayer(playerName);
					if(playerArg == null){
						player.sendMessage(ChatColor.RED + Language.PLAYER_OFFLINE.getMsg(managerAccount.getPlugin().getLanguage()));
						return true;
					}
					Player playerArgO = playerArg.getPlayer();
					try{
						double amount = Double.parseDouble(args[2]);
							if(managerAccount.hasPocketAccount(playerArgO.getUniqueId())){
									if(managerAccount.removeAmountPocket(playerArgO.getUniqueId(), amount) == BankXResponse.SUCCESS){
									player.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.REMOVE_MONEY.getMsg(managerAccount.getPlugin().getLanguage()),playerName, amount,""));
									playerArgO.sendMessage(ChatColor.DARK_AQUA + managerAccount.replaceTag(Language.RECEIVE_MONEY_FROM_PLAYER.getMsg(managerAccount.getPlugin().getLanguage()),player.getName(), amount,""));
									return true;
									}
									player.sendMessage(ChatColor.RED + Language.NOT_ENOUGH_MONEY_ON_HIM.getMsg(managerAccount.getPlugin().getLanguage()));
									return true;
							}
						}
						catch(Exception ex){
							player.sendMessage(ChatColor.RED + Language.PARAMETER_NOT_ENTER_CORRECTLY.getMsg(managerAccount.getPlugin().getLanguage()));
							return true;
						}
				}
			}
		}
		return false;
	}

}
