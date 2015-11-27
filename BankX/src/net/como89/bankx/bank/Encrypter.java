package net.como89.bankx.bank;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.como89.bankx.lib.apache.commons.codec.binary.Base64;

@Deprecated
public class Encrypter
{

	SecretKeySpec secretKey = null;
	Cipher aesCipher = null;

	/**
	*
	* Encrypter 
	*	
	*	@author Nba_Yoh
	*	@version 1.0
	*	@param stringKey : The secret key used to encrypt.
	*/
	Encrypter(String stringKey)
	{
		byte[] key = stringKey.getBytes();
		SecretKeySpec secretKeySpec = null;
		Cipher desCipher = null;

		try
		{
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			
			secretKeySpec = new SecretKeySpec(key, "AES");

			desCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		}
		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchPaddingException e)
		{
			e.printStackTrace();
		}

		this.secretKey = secretKeySpec;
		this.aesCipher = desCipher;
	}

	/**
	*
	* Used to encrypt a string. 
	*	
	*	@author Nba_Yoh
	*	@version 1.0
	*	@param s : The string that you want to encrypt.
	*	@return Return the encrypted string.
	*/
	String encrypt(String s)
	{

		String encryptedString = null;
		
		byte[] iv = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);

		try
		{
			aesCipher.init(Cipher.ENCRYPT_MODE, secretKey,ivspec);
			byte[] encode = aesCipher.doFinal(s.getBytes());
			encryptedString = new Base64().encodeAsString(encode);

		}
		catch(InvalidKeyException e)
		{
			e.printStackTrace();
		}
		catch(IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		catch(BadPaddingException e)
		{
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} 

		return encryptedString;
	}
	
	/**
	*
	* Used to encrypt a string. 
	*	
	*	@author Nba_Yoh
	*	@version 1.0
	*	@param s : The string that you want to decrypt.
	*	@return Return the decrypted string.
	*/
	String decrypt(String s)
	{
		String encryptedString = null;
		byte[] iv = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);

		try
		{
			aesCipher.init(Cipher.DECRYPT_MODE, secretKey,ivspec);
			byte[] decode = new Base64().decode(s.getBytes());
			decode = aesCipher.doFinal(decode);
			encryptedString = new String(decode);

		}
		catch(Exception e){
			e.printStackTrace();
		}

		return encryptedString;
	}
}

