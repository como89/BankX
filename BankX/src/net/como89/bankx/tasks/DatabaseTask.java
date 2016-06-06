package net.como89.bankx.tasks;

import net.como89.bankx.bank.ManageDatabase;
import net.como89.bankx.tasks.databaserequest.Request;

public class DatabaseTask extends Task {
	
	private Request request;

	public DatabaseTask(String taskName, int timeSeconds,Request request) {
		super(taskName, timeSeconds);
		this.request = request;
	}

	@Override
	public void update() {
		if(request == null)
			return;
		ManageDatabase md = ManageDatabase.getDatabaseInstance();
		md.connectToDatabase();
		request.doAction();
		md.disconnect();
	}

}
