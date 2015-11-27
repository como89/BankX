package net.como89.bankx.bank.logsystem;

public enum TypeLog {

	MONEY("&b" + "Money"),
	INVENTORY("&6"+ "Inventory");
	
	private String type;
	
	private TypeLog(String type){
		this.type = type;
	}
	
	@Override
	public String toString(){
		return type;
	}
}
