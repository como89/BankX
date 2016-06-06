package net.como89.bankx.tasks;

import net.como89.bankx.BankX;
import net.como89.bankx.bank.BankAccount;
import net.como89.bankx.bank.ManageDatabase;
import net.como89.bankx.bank.PlayerData;

public class TaskDatabase extends Task {
	
	private EnumDatabaseRequest enumDataBaseRequest;
	private PlayerData playerData;
	private BankX plugin;
	private ManageDatabase manageDatabase;

	public TaskDatabase(String taskName, int timeSeconds,EnumDatabaseRequest enumDataBaseRequest,PlayerData playerData,BankX plugin,ManageDatabase manageDatabase) {
		super(taskName, timeSeconds);
		this.enumDataBaseRequest = enumDataBaseRequest;
		try {
			this.playerData = (PlayerData) playerData.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		this.plugin = plugin;
		this.manageDatabase = manageDatabase;
	}

	@Override
	public void update() {
		if(playerData == null){
			plugin.getLogger().warning("Problem with player data. PlayerData == null");
			return;
		}
		switch(enumDataBaseRequest){
		case UPDATE_POCKET :
			manageDatabase.updateAmountPocket(playerData.getUniqueId().toString(),playerData.getMoneyPocket());
			break;
		case ADD_POCKET :
			manageDatabase.addPocketOfPlayer(playerData.getUniqueId().toString(), playerData.getMoneyPocket());
			break;
		case UPDATE_BANK_AMOUNT :
			for(BankAccount bankAccount : playerData.getListBanks()){
				manageDatabase.updateAmountBank(bankAccount.getName(), bankAccount.getBalance(),playerData.getUniqueId().toString());
			}
			break;
		case CREATE_BANK_ACCOUNT :
			
			break;
		case DELETE_BANK_ACCOUNT :
			
			break;
		}
	}
	
		public enum EnumDatabaseRequest {
			
			UPDATE_POCKET,
			ADD_POCKET,
			UPDATE_BANK_AMOUNT,
			CREATE_BANK_ACCOUNT,
			DELETE_BANK_ACCOUNT,
			;
			
		}

}
