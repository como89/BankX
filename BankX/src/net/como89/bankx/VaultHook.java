package net.como89.bankx;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import net.como89.bankx.bank.api.BankXApi;
import net.como89.bankx.bank.api.BankXResponse;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

@SuppressWarnings("deprecation")
public class VaultHook implements Economy {
	
	private BankXApi bankX_api;
	private static String name;
	
	public VaultHook(BankX bankX){
		bankX_api = bankX.getBankXAPI();
	}
	
	public static void hookToVault(BankX bank){
		Logger log = bank.getLogger();
		name = "BankX";
		log.info("Initialise and register Vault ...");
		VaultHook vaultHook = new VaultHook(bank);
		Bukkit.getServicesManager().register(Economy.class, vaultHook, bank, ServicePriority.High);
		log.info("Initialization with vault finish.");
	}
	

	@Override
	public boolean isEnabled() {
		if(bankX_api == null){
			return false;
		}
		return bankX_api.isEnable();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hasBankSupport() {
		return true;
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	@Override
	public String format(double amount) {
		return "";
	}

	@Override
	public String currencyNamePlural() {
		return "";
	}

	@Override
	public String currencyNameSingular() {
		return "";
	}

	@Override
	public boolean hasAccount(String playerName) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		return bankX_api.hasPocketAccount(offlinePlayer.getUniqueId());
	}

	@Override
	public boolean hasAccount(String playerName, String worldName) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		return bankX_api.hasPocketAccount(offlinePlayer.getUniqueId());
	}

	@Override
	public double getBalance(String playerName) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		return bankX_api.getAmountAccount(offlinePlayer.getUniqueId());
	}

	@Override
	public double getBalance(String playerName, String world) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		return bankX_api.getAmountAccount(offlinePlayer.getUniqueId());
	}

	@Override
	public boolean has(String playerName, double amount) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		return bankX_api.getAmountAccount(offlinePlayer.getUniqueId()) >= amount;
	}

	@Override
	public boolean has(String playerName, String worldName, double amount) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		return bankX_api.getAmountAccount(offlinePlayer.getUniqueId()) >= amount;
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, double amount) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		BankXResponse bankXRes = bankX_api.removeAmountAccount(offlinePlayer.getUniqueId(), amount);
		double balance = bankX_api.getAmountAccount(offlinePlayer.getUniqueId());
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(amount,balance,ResponseType.SUCCESS,"Success");
			break;
		case ACCOUNT_NOT_EXIST:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Account not exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		case NOT_ENOUGHT_MONEY:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Not enought money");
			break;
		default:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, String worldName,
			double amount) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		BankXResponse bankXRes = bankX_api.removeAmountAccount(offlinePlayer.getUniqueId(), amount);
		double balance = bankX_api.getAmountAccount(offlinePlayer.getUniqueId());
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(amount,balance,ResponseType.SUCCESS,"Success");
			break;
		case ACCOUNT_NOT_EXIST:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Account not exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		case NOT_ENOUGHT_MONEY:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Not enought money");
			break;
		default:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, double amount) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		BankXResponse bankXRes = bankX_api.addAmountAccount(offlinePlayer.getUniqueId(), amount);
		double balance = bankX_api.getAmountAccount(offlinePlayer.getUniqueId());
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(amount,balance,ResponseType.SUCCESS,"Success");
			break;
		case ACCOUNT_NOT_EXIST:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Account not exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		default:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, String worldName,
			double amount) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		BankXResponse bankXRes = bankX_api.addAmountAccount(offlinePlayer.getUniqueId(), amount);
		double balance = bankX_api.getAmountAccount(offlinePlayer.getUniqueId());
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(amount,balance,ResponseType.SUCCESS,"Success");
			break;
		case ACCOUNT_NOT_EXIST:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Account not exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		default:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public EconomyResponse createBank(String name, String playerName) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		BankXResponse bankXRes = bankX_api.createBankAccount(name, offlinePlayer.getUniqueId());
		double balance = bankX_api.getAmountInBankAccount(name);
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(0,balance,ResponseType.SUCCESS,"Success");
			break;
		case BANK_ACCOUNT_ALREADY_EXIST:
			ecoRes = new EconomyResponse(0,balance,ResponseType.FAILURE,"Bank account already exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(0,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		default:
			ecoRes = new EconomyResponse(0,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		double balance = bankX_api.deleteBankAccount(name);
		if(balance == -1){
			return new EconomyResponse(0,balance,ResponseType.FAILURE,"Bank account not exist");
		}
		return new EconomyResponse(0,balance,ResponseType.SUCCESS,"Success");
	}

	@Override
	public EconomyResponse bankBalance(String name) {
		double balance = bankX_api.getAmountInBankAccount(name);
		if(balance == -1){
			return new EconomyResponse(0,balance,ResponseType.FAILURE,"Bank account not exist");
		}
		return new EconomyResponse(0,balance,ResponseType.SUCCESS,"Success");
	}

	@Override
	public EconomyResponse bankHas(String name, double amount) {
		double balance = bankX_api.getAmountInBankAccount(name);
		if(balance == -1){
			return new EconomyResponse(0,balance,ResponseType.FAILURE,"Bank account not exist");
		}
		if(balance < amount){
			return new EconomyResponse(0,balance,ResponseType.FAILURE,"Not enought money.");
		}
		return new EconomyResponse(0,balance,ResponseType.SUCCESS,"Success");
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		BankXResponse bankXRes = bankX_api.removeAmountBankAccount(name, amount);
		double balance = bankX_api.getAmountInBankAccount(name);
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(0,balance,ResponseType.SUCCESS,"Success");
			break;
		case BANK_ACCOUNT_NOT_EXIST:
			ecoRes = new EconomyResponse(0,balance,ResponseType.FAILURE,"Bank account not exist");
			break;
		case NOT_ENOUGHT_MONEY:
			ecoRes = new EconomyResponse(0,balance,ResponseType.FAILURE,"Not enought money.");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(0,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		default:
			ecoRes = new EconomyResponse(0,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		BankXResponse bankXRes = bankX_api.addAmountBankAccount(name, amount);
		double balance = bankX_api.getAmountInBankAccount(name);
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(0,balance,ResponseType.SUCCESS,"Success");
			break;
		case BANK_ACCOUNT_NOT_EXIST:
			ecoRes = new EconomyResponse(0,balance,ResponseType.FAILURE,"Bank account not exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(0,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		default:
			ecoRes = new EconomyResponse(0,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public EconomyResponse isBankOwner(String name, String playerName) {
		UUID owner = bankX_api.getOwnerBank(name);
		if(owner == null){
			return new EconomyResponse(0,0,ResponseType.FAILURE,"Bank account not exist");
		}
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		if(owner.equals(offlinePlayer.getUniqueId())){
			return new EconomyResponse(0,0,ResponseType.SUCCESS,"This player is the owner.");
		}
		return new EconomyResponse(0,0,ResponseType.FAILURE,"This player is not the owner of the bank account.");
	}

	@Override
	public EconomyResponse isBankMember(String name, String playerName) {
		return new EconomyResponse(-1,-1,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
	}

	@Override
	public List<String> getBanks() {
		return bankX_api.getListBank();
	}

	@Override
	public boolean createPlayerAccount(String playerName) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(String playerName, String worldName) {
		return false;
	}

	@Override
	public EconomyResponse createBank(String bankName, OfflinePlayer player) {
		BankXResponse bankXRes = bankX_api.createBankAccount(bankName, player.getUniqueId());
		double balance = bankX_api.getAmountInBankAccount(bankName);
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(0,balance,ResponseType.SUCCESS,"Success");
			break;
		case BANK_ACCOUNT_ALREADY_EXIST:
			ecoRes = new EconomyResponse(0,balance,ResponseType.FAILURE,"Bank account already exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(0,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		default:
			ecoRes = new EconomyResponse(0,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
		return false;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
		BankXResponse bankXRes = bankX_api.addAmountAccount(player.getUniqueId(), amount);
		double balance = bankX_api.getAmountAccount(player.getUniqueId());
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(amount,balance,ResponseType.SUCCESS,"Success");
			break;
		case ACCOUNT_NOT_EXIST:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Account not exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		default:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String world,
			double amount) {
		BankXResponse bankXRes = bankX_api.addAmountAccount(player.getUniqueId(), amount);
		double balance = bankX_api.getAmountAccount(player.getUniqueId());
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(amount,balance,ResponseType.SUCCESS,"Success");
			break;
		case ACCOUNT_NOT_EXIST:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Account not exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		default:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public double getBalance(OfflinePlayer player) {
		return bankX_api.getAmountAccount(player.getUniqueId());
	}

	@Override
	public double getBalance(OfflinePlayer player, String world) {
		return bankX_api.getAmountAccount(player.getUniqueId());
	}

	@Override
	public boolean has(OfflinePlayer player, double amount) {
		return bankX_api.getAmountAccount(player.getUniqueId()) >= amount;
	}

	@Override
	public boolean has(OfflinePlayer player, String world, double amount) {
		return bankX_api.getAmountAccount(player.getUniqueId()) >= amount;
	}

	@Override
	public boolean hasAccount(OfflinePlayer player) {
		return bankX_api.hasPocketAccount(player.getUniqueId());
	}

	@Override
	public boolean hasAccount(OfflinePlayer player, String world) {
		return bankX_api.hasPocketAccount(player.getUniqueId());
	}

	@Override
	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(-1,-1,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
	}

	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
		UUID owner = bankX_api.getOwnerBank(name);
		if(owner == null){
			return new EconomyResponse(0,0,ResponseType.FAILURE,"Bank account not exist");
		}
		if(owner.equals(player.getUniqueId())){
			return new EconomyResponse(0,0,ResponseType.SUCCESS,"This player is the owner.");
		}
		return new EconomyResponse(0,0,ResponseType.FAILURE,"This player is not the owner of the bank account.");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
		BankXResponse bankXRes = bankX_api.removeAmountAccount(player.getUniqueId(), amount);
		double balance = bankX_api.getAmountAccount(player.getUniqueId());
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(amount,balance,ResponseType.SUCCESS,"Success");
			break;
		case ACCOUNT_NOT_EXIST:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Account not exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		case NOT_ENOUGHT_MONEY:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Not enought money");
			break;
		default:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String world,
			double amount) {
		BankXResponse bankXRes = bankX_api.removeAmountAccount(player.getUniqueId(), amount);
		double balance = bankX_api.getAmountAccount(player.getUniqueId());
		EconomyResponse ecoRes = null;
		switch(bankXRes){
		case SUCCESS :
			ecoRes = new EconomyResponse(amount,balance,ResponseType.SUCCESS,"Success");
			break;
		case ACCOUNT_NOT_EXIST:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Account not exist");
			break;
		case ERROR:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Error in the plugin");
			break;
		case NOT_ENOUGHT_MONEY:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.FAILURE,"Not enought money");
			break;
		default:
			ecoRes = new EconomyResponse(amount,balance,ResponseType.NOT_IMPLEMENTED,"Not Implemented");
			break;
		}
		return ecoRes;
	}

}