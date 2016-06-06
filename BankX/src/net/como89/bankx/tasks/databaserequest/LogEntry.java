package net.como89.bankx.tasks.databaserequest;

import java.util.UUID;

import net.como89.bankx.bank.ManageDatabase;
import net.como89.bankx.bank.logsystem.BookLog;

public class LogEntry extends Request {
	
	private BookLog booklog;

	public LogEntry(UUID uuid,BookLog bookLog) {
		super(uuid);
		this.booklog = bookLog;
	}

	@Override
	public void doAction() {
		ManageDatabase md = ManageDatabase.getDatabaseInstance();
		int id = md.addLogEntry(uuid, booklog.getName(), booklog.getDesc(), booklog.getDate(), booklog.getTransactionType(), booklog.getLogType());
		booklog.setID(id);
	}

}
