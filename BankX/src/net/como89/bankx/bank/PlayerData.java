package net.como89.bankx.bank;

import java.util.ArrayList;
import java.util.UUID;

import net.como89.bankx.bank.logsystem.BookLog;

public class PlayerData {

	UUID playerUUID;
	
	double moneyPocket;
	ArrayList<BankAccount> listBanksAccount;
	ArrayList<BookLog> listBookLog;
	
	
	public PlayerData(UUID playerUUID,double moneyPocket, ArrayList<BankAccount> listBank, ArrayList<BookLog> listBookLog){
		this.playerUUID = playerUUID;
		this.moneyPocket = moneyPocket;
		this.listBanksAccount = listBank;
		this.listBookLog = listBookLog;
	}
	
	public PlayerData(UUID playerUUID,double moneyPocket){
		this.playerUUID = playerUUID;
		this.moneyPocket = moneyPocket;
		this.listBanksAccount = new ArrayList<BankAccount>();
		this.listBookLog = new ArrayList<BookLog>();
	}
	
	public UUID getUniqueId(){
		return playerUUID;
	}
	
	public double getMoneyPocket(){
		return moneyPocket;
	}
	
	public ArrayList<BankAccount> getListBanks(){
		return listBanksAccount;
	}
	
	public ArrayList<BookLog> getListLog(){
		return listBookLog;
	}
}
