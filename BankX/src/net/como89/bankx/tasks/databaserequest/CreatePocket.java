package net.como89.bankx.tasks.databaserequest;

import java.util.UUID;

import net.como89.bankx.bank.ManageDatabase;

public class CreatePocket extends Request {

	private double money;
	
	public CreatePocket(UUID uuid,double money) {
		super(uuid);
		this.money = money;
	}

	@Override
	public void doAction() {
		ManageDatabase md = ManageDatabase.getDatabaseInstance();
		md.addPocketOfPlayer(uuid.toString(), money);
	}

}
