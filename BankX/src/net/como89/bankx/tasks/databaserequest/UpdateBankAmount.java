package net.como89.bankx.tasks.databaserequest;

import java.util.UUID;

import net.como89.bankx.bank.ManageDatabase;

public class UpdateBankAmount extends Request {

	private double money;
	private String bankName;
	
	public UpdateBankAmount(UUID uuid,double money,String bankName) {
		super(uuid);
		this.money = money;
		this.bankName = bankName;
	}

	@Override
	public void doAction() {
		ManageDatabase md = ManageDatabase.getDatabaseInstance();
		md.updateAmountBank(bankName, money, uuid.toString());
	}

}
