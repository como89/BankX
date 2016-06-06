package net.como89.bankx.tasks.databaserequest;

import java.util.UUID;

import net.como89.bankx.bank.ManageDatabase;

public class UpdateBankName extends Request {

	private String oldName;
	private String newName;
	
	public UpdateBankName(UUID uuid,String oldName,String newName) {
		super(uuid);
		this.oldName = oldName;
		this.newName = newName;
	}

	@Override
	public void doAction() {
		ManageDatabase md = ManageDatabase.getDatabaseInstance();
		md.updateBankName(oldName, newName, uuid.toString());
	}

}
