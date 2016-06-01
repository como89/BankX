package net.como89.bankx.tasks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TaskManager implements Runnable {

	private static boolean ready = true;
	
	private ArrayList<Task> listTask;
	private Plugin plugin;
	
	public TaskManager(Plugin plugin){
		listTask = new ArrayList<>();
		this.plugin = plugin;
	}

	@Override
	public void run() {
		Iterator<Task> iterator = listTask.iterator();
		while(iterator.hasNext()) {
			Task task = iterator.next();
			if(task.nbSecondsRequest > 0){
				task.nbSecondsRequest--;
			} else if(task.taskName.equalsIgnoreCase("database")) {
				if(ready){
					ready = false;
					launchTaskAsynchronously(task);
					iterator.remove();
				}
			} else {
				launchTask(task);
				task.nbSecondsRequest = task.timeSeconds;
			}
		}
	}

	public void registerTask(Task task){
		listTask.add(task);
	}
	

	private void launchTask(Task task) {
		
		
	}
	
	private void launchTaskAsynchronously(Task task){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable(){
			
			@Override
			public void run() {
				task.update();
				ready = true;
			}
			
		});
	}
}
