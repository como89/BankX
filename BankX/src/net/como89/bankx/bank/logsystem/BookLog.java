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
	
	public BookLog(String name,String description,Date date, TransactionType transacType,TypeLog logType,int id){
		this.name = name;
		this.description = description;
		this.date = date;
		this.transacType = transacType;
		this.logType = logType;
		this.id = id;
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
