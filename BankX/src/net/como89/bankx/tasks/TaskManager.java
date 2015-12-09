package net.como89.bankx.tasks;

import java.util.ArrayList;
import java.util.Iterator;

public class TaskManager implements Runnable {

	private ArrayList<Task> listTask;
	
	public TaskManager(){
		listTask = new ArrayList<>();
	}

	@Override
	public void run() {
		Iterator<Task> iterator = listTask.iterator();
		while(iterator.hasNext()) {
			Task task = iterator.next();
			if(task.nbSecondsRequest > 0){
				task.nbSecondsRequest--;
			} else if(task.taskName.equalsIgnoreCase("database")) {
				launchTask(task);
				iterator.remove();
			} else {
				launchTask(task);
				task.nbSecondsRequest = task.timeSeconds;
			}
		}
	}
	
	public void registerTask(Task task){
		listTask.add(task);
	}
	
	private void launchTask(Task task){
		
	}
}
