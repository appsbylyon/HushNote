package com.appsbylyon.hushnote.Security;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import com.appsbylyon.hushnote.FileManager.FileManager;
import com.appsbylyon.hushnote.Objects.Entry;
import com.appsbylyon.hushnote.Objects.NoteBook;

/**
 *Class that handles all encryption operations
 * 
 * Modified: 7/17/2014
 * 
 * @author Adam Lyon
 */
public class Encrypter
{
    private static final Encrypter instance = new Encrypter();
    
    private FileManager fm;
    
    private static final String ALGORITHM = "AES";
    private static final String ALGORITHM_FULL = "AES";
    
    private SecretKeySpec masterKey = null;
    private SecretKeySpec slaveKey = null;
    /**
    private String masterEncryptedValue = "";
    private String masterDecryptedValue = "";
    private String slaveEncryptedValue = "";
    private String slaveDecryptedValue = "";
    */
    
    private Encrypter(){}
    
    public static Encrypter getInstance() 
    {
        return instance;
    }
    
    private static String encrypt(final String plainMessage, final SecretKeySpec symKey) 
    {
    	try 
    	{
    		final byte[] encodedMessage = plainMessage.getBytes(Charset.forName("UTF-8"));
    		
    		final Cipher cipher = Cipher.getInstance(ALGORITHM_FULL);
    		cipher.init(Cipher.ENCRYPT_MODE, symKey);
    		final byte[] encryptedMessage = cipher.doFinal(encodedMessage);
    		final String ivAndEncryptedMessageBase64 = Base64.encodeToString(encryptedMessage, Base64.DEFAULT);
    		return ivAndEncryptedMessageBase64;
    	} catch (InvalidKeyException e)
    	{
            throw new IllegalArgumentException("key argument does not contain a valid AES key");
    	} catch (GeneralSecurityException e) 
    	{
            throw new IllegalStateException("Unexpected exception during encryption" + e.getMessage(), e);
        } 
    }
    
    private static String decrypt(final String encryptedString, final SecretKeySpec symKey) 
    {
    	try 
    	{
			final byte[] encryptedBytes = Base64.decode(encryptedString, Base64.DEFAULT);
			final Cipher cipher = Cipher.getInstance(ALGORITHM_FULL);
			cipher.init(Cipher.DECRYPT_MODE, symKey);
			final byte[] encodedMessage = cipher.doFinal(encryptedBytes);
			final String message = new String(encodedMessage);
			
			return message;
		} catch (InvalidKeyException e) 
		{
			throw new IllegalArgumentException("key argument does not contain a valid AES key");
		} catch (BadPaddingException e) 
		{
			// you'd better know about padding oracle attacks
			return "Bad Padding Exception";
		} catch (GeneralSecurityException e) 
		{
			throw new IllegalStateException("Unexpected exception during decryption: "+e.getMessage(), e);
		} 
		
	}
    
    public String decryptWithSlaveKey(String valueToDecrypt) 
    {
        return Encrypter.decrypt(valueToDecrypt, slaveKey);
    	/**
    	try 
        {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, slaveKey);
            byte[] decodedValue =Base64.decode(valueToDecrypt, Base64.DEFAULT);
            byte[] decValue = c.doFinal(decodedValue);
            slaveDecryptedValue = new String(decValue);
            return true;
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException E) 
        {
            return false;
        }
        */
    }
    
    /**
    public String getSlaveDecryptedValue() 
    {
        return slaveDecryptedValue;
    }
    */
    
    public String encryptWithSlaveKey(String valueToEncrypt) 
    {
        return Encrypter.encrypt(valueToEncrypt, slaveKey);
    	/**
    	try 
        {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, slaveKey);
            byte[] encValue = cipher.doFinal(valueToEncrypt.getBytes());
            slaveEncryptedValue = Base64.encodeToString(encValue, Base64.DEFAULT);
            return true;
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException E) 
        {
            return false;
        }
        */
    }
    
    /**
    public String getSlaveEncryptedValue() 
    {
        return slaveEncryptedValue;
    }
    */
    
    public String decryptWithMasterKey(String valueToDecrypt) 
    {
        return Encrypter.decrypt(valueToDecrypt, masterKey);
    	/**
    	try 
        {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, masterKey);
            byte[] decodedValue =Base64.decode(valueToDecrypt, Base64.DEFAULT);
            byte[] decValue = c.doFinal(decodedValue);
            masterDecryptedValue = new String(decValue);
            return true;
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException E) 
        {
            return false;
        }
        */
    }
    
    /**
    public String getMasterDecryptedValue() 
    {
        return masterDecryptedValue;
    }
    */
    
    public String encryptWithMasterKey(String valueToEncrypt) 
    {
        return Encrypter.encrypt(valueToEncrypt, masterKey);
    	/**
    	try 
        {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, masterKey);
            byte[] encValue = cipher.doFinal(valueToEncrypt.getBytes());
            masterEncryptedValue = Base64.encodeToString(encValue, Base64.DEFAULT);
            return true;
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException E) 
        {
            return false;
        }
        */
    }// end of encryptWithMasterKey method
    
    /**
    public String getMasterEncryptedValue() 
    {
        return masterEncryptedValue;
    }
    */
    
    public boolean setSlaveKey(String keyValue) 
    {
        while (keyValue.length() < 17) 
        {
            keyValue += keyValue;
        }
        keyValue = keyValue.substring(0, 16);
        SecretKeySpec key = generateKey(keyValue);
        if (key != null) 
        {
            slaveKey = key;
            return true;
        }
        return false;
    }// End of setSlaveKey method
    
    
    public boolean setMasterKey(String keyValue) 
    {
        SecretKeySpec key = generateKey(keyValue);
        if (key != null) 
        {
            masterKey = key;
            return true;
        }
        return false;        
    }// End of setMasterKey method
    
    
    private SecretKeySpec generateKey(String keyValue) 
    {
        SecretKeySpec key = null;
        try 
        {
            key = new SecretKeySpec(keyValue.getBytes(), ALGORITHM);
        }
        catch (Exception E) 
        {
            return key;
        }
        return key;
    }// End of generateKey method
    
    public String generateMasterKey() 
    {
        String randString = "";
        for(int i = 0; i < 16; i++ ) 
        {
            int currChar = 0;
            boolean validChar = false;
            while (!validChar) 
            {
                currChar = (int) (Math.random()* 75) + 48;
                if (!((currChar >= 58 && currChar <= 64) || (currChar >= 91 && currChar <= 97))) 
                {
                    validChar = true;
                }
            }
            randString += String.valueOf(Character.toChars(currChar));
        }
        return randString;
    }
    
    public NoteBook encryptNotebook(NoteBook bookToEncrypt) 
	{
		NoteBook encryptedNotebook = new NoteBook(false);
		String encPassword = this.encryptWithMasterKey(bookToEncrypt.getPassword());
		if (encPassword == null) 
		{
			encPassword = "ENC PASSWORD IS NULL";
		}
		this.logMessage("password: "+bookToEncrypt.getPassword()+" Enc Password: "+encPassword);
		encryptedNotebook.setPassword(encPassword);
		encryptedNotebook.setDateCreated(bookToEncrypt.getDateCreated());
		encryptedNotebook.setDateModified(bookToEncrypt.getDateModified());
		encryptedNotebook.setTitle(bookToEncrypt.getTitle());
		encryptedNotebook.setDescription(bookToEncrypt.getDescription());
		encryptedNotebook.setEncrypted(bookToEncrypt.isEncrypted());
		this.setSlaveKey(bookToEncrypt.getPassword());
		for (Entry tempEntry: bookToEncrypt.getAllEntries()) 
		{
			Entry encryptedEntry = new Entry(false);
			
			encryptedEntry.setDateCreated(this.encryptWithSlaveKey(tempEntry.getDateCreated()));
			encryptedEntry.setDateModified(this.encryptWithSlaveKey(tempEntry.getDateModified()));
			encryptedEntry.setTitle(this.encryptWithSlaveKey(tempEntry.getTitle()), false);
			encryptedEntry.setText(this.encryptWithSlaveKey(tempEntry.getText()), false);
			
			encryptedNotebook.addEntry(encryptedEntry);
		}
		return encryptedNotebook;
	}
	
	public NoteBook decryptNotebook(NoteBook bookToDecrypt) 
	{
		NoteBook decryptedNotebook = null;
		decryptedNotebook = new NoteBook(false);
		this.setSlaveKey(this.decryptWithMasterKey(bookToDecrypt.getPassword()));
		decryptedNotebook.setPassword(this.decryptWithMasterKey(bookToDecrypt.getPassword()));
		decryptedNotebook.setDateCreated(bookToDecrypt.getDateCreated());
		decryptedNotebook.setDateModified(bookToDecrypt.getDateModified());
		decryptedNotebook.setTitle(bookToDecrypt.getTitle());
		decryptedNotebook.setEncrypted(true);
		decryptedNotebook.setDescription(bookToDecrypt.getDescription());
		
		for (Entry tempEntry : bookToDecrypt.getAllEntries()) 
		{
			Entry decryptedEntry = new Entry(false);
			
			decryptedEntry.setDateCreated(this.decryptWithSlaveKey(tempEntry.getDateCreated()));
			decryptedEntry.setDateModified(this.decryptWithSlaveKey(tempEntry.getDateModified()));
			decryptedEntry.setTitle(this.decryptWithSlaveKey(tempEntry.getTitle()), false);
			decryptedEntry.setText(this.decryptWithSlaveKey(tempEntry.getText()), false);
			
			decryptedNotebook.addEntry(decryptedEntry);
		}
		return decryptedNotebook;
	}
	
	public NoteBook encryptForExport(NoteBook bookToEncrypt) 
	{
		NoteBook encryptedNotebook = new NoteBook(false);
		this.setSlaveKey(bookToEncrypt.getPassword());
		String encPassword = this.encryptWithSlaveKey(bookToEncrypt.getPassword());
		encryptedNotebook.setPassword(encPassword);
		encryptedNotebook.setDateCreated(bookToEncrypt.getDateCreated());
		encryptedNotebook.setDateModified(bookToEncrypt.getDateModified());
		encryptedNotebook.setTitle(bookToEncrypt.getTitle());
		encryptedNotebook.setDescription(bookToEncrypt.getDescription());
		encryptedNotebook.setEncrypted(true);
		this.setSlaveKey(bookToEncrypt.getPassword());
		for (Entry tempEntry: bookToEncrypt.getAllEntries()) 
		{
			Entry encryptedEntry = new Entry(false);
			
			encryptedEntry.setDateCreated(this.encryptWithSlaveKey(tempEntry.getDateCreated()));
			encryptedEntry.setDateModified(this.encryptWithSlaveKey(tempEntry.getDateModified()));
			encryptedEntry.setTitle(this.encryptWithSlaveKey(tempEntry.getTitle()), false);
			encryptedEntry.setText(this.encryptWithSlaveKey(tempEntry.getText()), false);
			
			encryptedNotebook.addEntry(encryptedEntry);
		}
		return encryptedNotebook;
	}
	
	public NoteBook decryptFromImport(NoteBook bookToDecrypt, String password) 
	{
		NoteBook decryptedNotebook = new NoteBook(false);
		this.setSlaveKey(password);
		decryptedNotebook.setPassword(this.decryptWithSlaveKey(bookToDecrypt.getPassword()));
		decryptedNotebook.setDateCreated(bookToDecrypt.getDateCreated());
		decryptedNotebook.setDateModified(bookToDecrypt.getDateModified());
		decryptedNotebook.setTitle(bookToDecrypt.getTitle());
		decryptedNotebook.setEncrypted(true);
		decryptedNotebook.setDescription(bookToDecrypt.getDescription());
		
		for (Entry tempEntry : bookToDecrypt.getAllEntries()) 
		{
			Entry decryptedEntry = new Entry(false);
			
			decryptedEntry.setDateCreated(this.decryptWithSlaveKey(tempEntry.getDateCreated()));
			decryptedEntry.setDateModified(this.decryptWithSlaveKey(tempEntry.getDateModified()));
			decryptedEntry.setTitle(this.decryptWithSlaveKey(tempEntry.getTitle()), false);
			decryptedEntry.setText(this.decryptWithSlaveKey(tempEntry.getText()), false);
			
			decryptedNotebook.addEntry(decryptedEntry);
		}
		return decryptedNotebook;
	}
	
	private void logMessage(String message) 
	{
		if (fm == null) 
		{
			fm = FileManager.getInstance();
		}
		fm.logMessage("encrypter", message);
	}
}// End of Encrypter Class