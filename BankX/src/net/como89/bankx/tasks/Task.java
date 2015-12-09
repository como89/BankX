package net.como89.bankx.tasks;

public abstract class Task {

	protected String taskName;
	protected int timeSeconds;
	protected int nbSecondsRequest;
	
	public Task(String taskName,int timeSeconds){
		this.taskName = taskName;
		this.timeSeconds = timeSeconds;
		this.nbSecondsRequest = timeSeconds;
	}
	
	public abstract void update();
}
