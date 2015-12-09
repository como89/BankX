package net.como89.bankx.bank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import net.como89.bankx.lib.tacoserialization.InventorySerialization;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Deprecated
public class FileManager {

	private File file;
	private BufferedReader reader;
	private boolean inventorySave;
	private ManagerAccount managerAccount;

	public FileManager(File file, boolean inventorySave,ManagerAccount managerAccount) {
		this.file = file;
		this.inventorySave = inventorySave;
		this.managerAccount = managerAccount;
	}

	public boolean initiateReader() {
		if (!inventorySave) {
			try {
				reader = new BufferedReader(new FileReader(file));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public void readDataBank() {
		if (reader != null) {
			String line = "";
			String ligne = "";
			Encrypter en = new Encrypter("54bank78");
			try {
				while ((line = reader.readLine()) != null) {
					ligne += en.decrypt(line);
				}
				if(ligne.isEmpty()){
					return;
				}
						String[] lines = ligne.split("=");
						int totalPlayers = lines.length / 2;
						for (int x = 0; x <= totalPlayers; x += 2) {
							ArrayList<BankAccount> listBanks = new ArrayList<BankAccount>();
							String data = lines[x + 1];
							String[] dataTab = data.split(":");
							int totalBanks = dataTab.length / 2;
							for (int y = 0; y <= totalBanks; y += 2) {
								String name = dataTab[y];
								double balance = 0.0;
								try {
									balance = Double
											.parseDouble(dataTab[y + 1]);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
								listBanks.add(new BankAccount(name, balance));
							}
							UUID playerUUID = UUID.fromString(lines[x]);
							PlayerData playerData = managerAccount.getPlayerData(playerUUID);
							if(playerData == null){
								playerData = new PlayerData(playerUUID,0.0);
								playerData.listBanksAccount = listBanks;
								managerAccount.bankData.listPlayerData.add(playerData);
							}
					}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void readDataPocket() {
		if (reader != null) {
			String line = "";
			String ligne = "";
			try {
				Encrypter en = new Encrypter("54Pocket90");
				while ((line = reader.readLine()) != null) {
					ligne += line;
				}
				if(ligne.isEmpty())
					return;
				
				ligne = en.decrypt(ligne);
					String[] lines = ligne.split(":");
					OfflinePlayer offlinePlayer = null;
					try {
					offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(lines[0]));
					} catch(IllegalArgumentException e){
						e.printStackTrace();
					}
					PlayerData playerData = managerAccount.getPlayerData(offlinePlayer.getUniqueId());
					if(playerData != null) {
						playerData.moneyPocket = Double.parseDouble(lines[1]);
					}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Inventory getInventoryFromSerializableString() {
		if (file == null)
			return null;
		if (!file.exists() || file.isDirectory()
				|| !file.getAbsolutePath().endsWith(".invsaveSeria"))
			return null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String serializedString = br.readLine();
			int invSize = Integer.parseInt(br.readLine());
			br.close();
			ItemStack[] itemStacks = InventorySerialization.getInventory(
					serializedString, invSize);
			Inventory inv = Bukkit.createInventory(null, invSize, file
					.getName().replace(".invsaveSeria", ""));
			inv.setContents(itemStacks);
			return inv;
		} catch (IOException e) {
			return null;
		}
	}
}
