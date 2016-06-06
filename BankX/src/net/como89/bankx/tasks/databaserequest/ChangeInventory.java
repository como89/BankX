package net.como89.bankx.tasks.databaserequest;

import java.util.UUID;

import net.como89.bankx.bank.ManageDatabase;
import net.como89.bankx.bank.items.InventoryItems;

public class ChangeInventory extends Request {

	private String bankName;
	private InventoryItems inv;
	
	public ChangeInventory(UUID uuid,String bankName,InventoryItems inv) {
		super(uuid);
		this.bankName = bankName;
		this.inv = inv;
	}

	@Override
	public void doAction() {
		ManageDatabase md = ManageDatabase.getDatabaseInstance();
		if(!md.insertInventory(bankName,inv.getName(),inv.getContents(),inv.getMaxStackSize(),uuid.toString())) {
			md.updateInventory(bankName, inv.getName(), inv.getContents(), inv.getMaxStackSize(), uuid.toString());
		}
	}

}
