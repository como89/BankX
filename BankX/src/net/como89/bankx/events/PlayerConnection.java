package net.como89.bankx.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.como89.bankx.bank.ManagerAccount;

public class PlayerConnection implements Listener {
	
	private ManagerAccount managerAccount;
	
	public PlayerConnection(ManagerAccount managerAccount){
		this.managerAccount = managerAccount;
	}

	@EventHandler
	public void onPlayerRejoinServer(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		if(managerAccount.hasPocketAccount(playerUUID)){
			managerAccount.createPocket(playerUUID);
		}
	}
}
