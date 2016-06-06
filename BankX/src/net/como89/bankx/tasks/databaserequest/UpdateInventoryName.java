package net.como89.bankx.tasks.databaserequest;

import java.util.UUID;

import net.como89.bankx.bank.ManageDatabase;

public class UpdateInventoryName extends Request {

	private String bankName,oldName, newName;
	
	public UpdateInventoryName(UUID uuid,String oldName,String newName,String bankName) {
		super(uuid);
		this.bankName = bankName;
		this.oldName = oldName;
		this.newName = newName;
	}

	@Override
	public void doAction() {
		ManageDatabase md = ManageDatabase.getDatabaseInstance();
		md.updateInventoryName(bankName, oldName, newName,uuid.toString());
	}

}
