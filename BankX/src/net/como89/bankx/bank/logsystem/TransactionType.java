package net.como89.bankx.bank.logsystem;

public enum TransactionType {

	
	ADD("&2" + "ADD"),
	REMOVE("&4" +"REMOVE"),
	CREATE("&3" +"CREATE"),
	DELETE("&8" +"DELETE");
	
	
	private String type;
	
	private TransactionType(String type){
		this.type = type;
	}
	
	@Override
	public String toString(){
		return type;
	}
}
