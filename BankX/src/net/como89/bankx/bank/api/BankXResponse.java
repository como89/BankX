package net.como89.bankx.bank.api;

public enum BankXResponse {
/**
 * SUCCESS - If the operation is a success.
 */
	SUCCESS,
/**
 * ERROR - If the operation has an error. These error could be a mysql error or code error.
*/
	ERROR,
/**	 
* ACCOUNT_NOT_EXIST - If the player don't have an account.
*/
	ACCOUNT_NOT_EXIST,
/**	 
* NOT_ENOUGHT_MONEY - If the player don't have enought money.
*/
	NOT_ENOUGHT_MONEY,
/**	 
* BANK_ACCOUNT_NOT_EXIST - If the player don't have a bank account.
*/
	BANK_ACCOUNT_NOT_EXIST,
/**	 
* BANK_ACCOUNT_ALREADY_EXIST - If the player already have a bank account.
*/
	BANK_ACCOUNT_ALREADY_EXIST;
}
