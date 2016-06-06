package net.como89.bankx.tasks.databaserequest;

import java.util.UUID;

import net.como89.bankx.bank.ManageDatabase;

public class DeleteBankAccount extends Request {

	private String bankName;
	
	public DeleteBankAccount(UUID uuid,String bankName) {
		super(uuid);
		this.bankName = bankName;
	}

	@Override
	public void doAction() {
		ManageDatabase md = ManageDatabase.getDatabaseInstance();
		md.deleteBankAccount(bankName, uuid.toString());
	}

}
