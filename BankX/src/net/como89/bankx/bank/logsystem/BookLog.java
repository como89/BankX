package net.como89.bankx.bank.logsystem;

import java.text.SimpleDateFormat;
import java.util.Date;


public class BookLog {
	
	private String name;
	private String description;
	private Date date;
	private TransactionType transacType;
	private TypeLog logType;
	private int id;
	
	public BookLog(String name,String description,Date date, TransactionType transacType,TypeLog logType){
		this.name = name;
		this.description = description;
		this.date = date;
		this.transacType = transacType;
		this.logType = logType;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return description;
	}
	
	public String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	
	public String getTransactionType() {
		return transacType.toString();
	}
	
	public String getLogType() {
		return logType.toString();
	}
	
	@Override
	public String toString(){
		return "&lDate: " + "&0" + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n" 
				+ "&l" + "Type of transaction: " + transacType + "\n" 
				+ "&0&l" + "Type: " + logType + " \n" 
				+ "&0&l" +  "Name: &0" + name + "\n" 
				+ "&0&l" + "Description: " + "&0" + description + "\n" 
				+ "&l" + "ID_Log: " + "&0" + id;
	}
}
