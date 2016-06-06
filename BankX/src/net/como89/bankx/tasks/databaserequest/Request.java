package net.como89.bankx.tasks.databaserequest;

import java.util.UUID;

public abstract class Request {

	protected UUID uuid;
	
	public Request(UUID uuid) {
		this.uuid = uuid;
	}

	public abstract void doAction();

}
