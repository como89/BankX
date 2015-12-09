package net.como89.bankx.tasks;

public class TaskDatabase extends Task {
	
	private String request;
	

	public TaskDatabase(String taskName, int timeSeconds,String request) {
		super(taskName, timeSeconds);
		this.request = request;
		//PrepareData
	}

	@Override
	public void update() {
		
		
	}

}
