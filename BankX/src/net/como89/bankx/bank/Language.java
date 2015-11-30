package net.como89.bankx.bank;

public enum Language {
	
	/*
	 * Language enum (French, English)
	 */
	WALLET_SHOW("Votre porte-monnaie est à <money>.","Your wallet is <money>."),
	WALLET_SHOW_PLAYER("Le porte-monnaie de <player> est à <money>.","The wallet of <player> is <money>."),
	PLAYER_OFFLINE("Vous ne pouvez pas envoyer de l'argent à un joueur hors-ligne.","You can not send money to a player offline."),
	PAY_PLAYER("Vous avez donner <money> à <player>","You pay <money> to <player>."),
	RECEIVE_MONEY_FROM_PLAYER("Vous avez reçu <money> de <player>.","You have received <money> from <player>."),
	NOT_ENOUGH_MONEY_IN_WALLET("Vous n'avez pas assez d'argent sur vous.","You don't have enough money in your wallet."),
	PARAMETER_NOT_ENTER_CORRECTLY("Un paramètre à la commande précédente n'a pas été entrer correctement.","A parameter to the previous command was not entered correctly."),
	NO_PERMISSION("Vous n'avez pas la permission pour faire cette commande.","You do not have permission to do this command."),
	ADD_MONEY("Vous avez ajouter <money> à <player>","You add <money> to <player>."),
	REMOVE_MONEY("Vous avez retirer <money> à <player>","You remove <money> to <player>."),
	LOST_MONEY("Vous avez perdu <money> de <player>","You lost <money> from <player>."),
	OPERATION_NOT_EMPTY("Vous devez entrer un nombre.","You need to enter a number."),
	DELETE_BANK("Votre compte en banque <name> est supprimé. Vous avez reçu <money> dans vos poches.","The bankAccount <name> is deleted. You receive <money> in your wallet."),
	CREATE_BANK("Votre compte en banque <name> est créé. Vous avez ajouté <money> dans votre compte en banque.","The bankAccount <name> is created. You add <money> in it."),
	NOT_ENOUGH_MONEY_ON_HIM("Le joueur n'a pas assez d'argent sur lui.","The player does not have enough money on him/her."),
	PLAYER_NOT_EXIST("Ce joueur n'existe pas.","This player doesn't exist."),
	CLOSE_BANK_ACCOUNT("Fermeture du compte de banque.","Close bank account."),
	NO_BANK_ACCOUNT("Vous n'avez pas de compte de banque.","You don't have a bank account."),
	ADD_MONEY_BANK("Ajout de <money> dans ton compte de banque.","Add <money> from bank account."),
	REMOVE_MONEY_BANK("Retrait de <money> dans ton compte de banque.","Remove <money> from bank account."),
	BANK_ACCOUNT_SHOW("Votre compte en banque <name> est à <money>.","The bank account <name> is <money>."),
	BANK_ACCOUNT_PLAYER_SHOW("Le compte en banque <name> de <player> est à <money>.","The bank account <name> of <player> is <money> ."),
	BANK_ACCOUNT_ALREADY_EXIST("Ce compte en banque existe déjà.","This bank account already exist."),
	PLAYER_NOT_HAVE_BANK_ACCOUNT("Ce joueur n'a pas de compte en banque.","This player don't have a bank account."),
	DEBITED_PAYMENT_ITEMS("Le paiement de <money> a été débité de votre compte pour le stockage des items dans vos coffres.","Payment <money> was debited from your account for storing items in your chests."),
	INVENTORY_EMPTY("Tous les inventaires ont été vider car vous n'aviez pas l'argent nécessaire pour les stockés.","All inventories were empty because you did not have the money needed for storage."),
	WARNING_FINAL_PAYMENT("Attention !!! - Le dernier paiement d'un total de <money> pour le stockage des items dans vos coffres n'a pas fonctionné. "
			+ "Veuillez vous assurez d'avoir l'argent nécessaire dans votre coffre avant le prochain jour / la prochaine semaine, sinon tous vos items seront supprimés.","Warning! - The final payment of a total of <money> for storage of items in your chest did not work. "
			+ "Please make sure you have the money in your chest before the next day / next week, otherwise all your items will be deleted."),
	CHANGE_ACCOUNT_NAME("Le nom a été changé pour <name>.","The name has been change for <name>."),
	ACCOUNT_INVENTORY_NOT_EXIST("Le compte|inventaire <name> n'existe pas.","The account|inventory <name> doesn't exist."),
	TRANSFERT_MONEY_PLAYER("Vous avez transférés <money> au compte <name> du joueur <player>.","You have transfer <money> to <player>'s <name> account."),
	NOT_ENOUGH_MONEY_ACCOUNT("Vous n'avez pas assez d'argent dans votre compte <name>.","You don't have enough money in <name> bank account."),
	CLEAR_AMOUNT_OPERATION("Remise à zéro du précédent montant que vous avez sélectionné!","Clears the current amount that was previous selected!"),
	BANK_ACCOUNT_NOT_EXIST("Ce compte en banque n'existe pas.","This bank account doesn't exist."),
	WELCOME_MSG("Re bonjour, <player>, comment puis-je vous aider aujourd'hui?","Welcome back, <player>, how can I help you today?"),
	NOT_YOURSELF("Vous ne pouvez pas envoyer de l'argent à vous même.","You can't send money to yourself."),
	NO_CREATIVE("Le gamemode creative n'est pas permis pour ouvrir les inventaires.","The creative gamemode is not permit to open inventories."),
	NO_BEGIN_ZERO("Vous ne pouvez pas commencer par zéro.","You can't begin with zero.");
	
	private String msgFrench;
	private String msgEnglish;
	
	private Language(String msgFrench,String msgEnglish){
		this.msgFrench = msgFrench;
		this.msgEnglish = msgEnglish;
	}
	
	public String getMsg(String language){
		return language.equalsIgnoreCase("french")?msgFrench:msgEnglish;
	}
}
