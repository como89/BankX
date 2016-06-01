package net.como89.bankx.tasks.databaserequest;

import java.util.UUID;

import net.como89.bankx.bank.ManageDatabase;

public class UpdatePocket extends Request {

	private double money;
	
	public UpdatePocket(UUID uuid,double money) {
		super(uuid);
		this.money = money;
	}
	
	@Override
	public void doAction() {
		ManageDatabase md = ManageDatabase.getDatabaseInstance();
		md.updateAmountPocket(uuid.toString(), money);
	}
}
