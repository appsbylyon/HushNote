package com.appsbylyon.hushnote.FileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.format.Time;
import android.widget.Toast;

import com.appsbylyon.hushnote.Objects.Entry;
import com.appsbylyon.hushnote.Objects.Library;
import com.appsbylyon.hushnote.Objects.NoteBook;
import com.appsbylyon.hushnote.Security.Encrypter;

/**
 * Class that handles all loading and saving operations.
 * 
 * Modified: 7/25/2014
 * 
 * @author infinite
 *
 */
public class FileManager 
{
    private static final FileManager instance = new FileManager();
    
    
    private static final String LIBRARY_NAME = "Library.lib";
    private static final String ERROR_LOG = "file_manager_error_log";
    private static final String EXPORT_SUCCESS = "Notebook Successfully Exported";
    private static final String EXPORT_FAIL = "Notebook Export Failed";
    private static final String EXPORT_SD_UNWRITABLE = "Cannot Write To External SD... Is it mounted on a computer?";
    private static final String EXPORT_DIRECTORY_NOT_CREATED = "Export Failed, Cannot Create Export Directory";
    private static final String EXPORT_FAIL_WRITE_TEXT = "An Error Occured Writing Text";
    private static final String EXPORT_TEXTFILE_DIR = "/HushNote/Export/Text";
    private static final String EXPORT_NOTEBOOK_DIR = "/HushNote/Export/Notebook";
    private static final String EXPORT_OBJECT_FAIL = "Could Not Export Notebook File..";
    
    private Encrypter encrypt = Encrypter.getInstance();
    
    private Context appContext;
    
    private FileManager() 
    {
       // File currDir = new File(System.getProperty("user.dir")).getParentFile();
        //workingDir = currDir.getPath();
    }
    
    
    public static FileManager getInstance() 
    {
        return instance;
    }
    
    public void setAppContext(Context appContext) 
    {
    	this.appContext = appContext;
    }
    
    
    
    public String saveNotebook(NoteBook noteBook) 
    {
        if (noteBook == null) 
        {
        	logMessage("file.log", "passed in notebook is null");
        }
    	String fileName = noteBook.getTitle()+".nbk";
        NoteBook tempNotebook = null;
        if (noteBook.isEncrypted()) 
        {
            tempNotebook = encrypt.encryptNotebook(noteBook);
            if (tempNotebook == null) 
            {
            	return "Encryption Failed";
            }
        }
        else 
        {
            tempNotebook = noteBook;
        }
        try 
        {
            FileOutputStream FOS = appContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream OOS = new ObjectOutputStream(FOS);
            OOS.writeObject(tempNotebook);
            return "Save Successful";
        }
        catch (IOException IOE) 
        {
            return IOE.getMessage();
        }
    }
    
    public boolean deleteNotebook(String noteBookName) 
    {
    	String fileName = appContext.getFilesDir()+"/"+noteBookName+".nbk";
    	File file = new File(fileName);
        return file.delete();
    }
    
    public boolean deleteLibrary() 
    {
    	String fileName = appContext.getFilesDir()+"/"+LIBRARY_NAME;
    	File file = new File(fileName);
    	return file.delete();
    }
    
    public boolean confirmPassword(String noteBookName, String password) 
    {
    	NoteBook notebook = loadNotebook(noteBookName);
    	if (notebook != null) 
    	{
	    	String decryptedPassword = encrypt.decryptWithMasterKey(notebook.getPassword());
	    	if (password.compareTo(decryptedPassword) == 0) 
	    	{
	    		return true;
	    	}
	    	else 
	    	{
	    		return false;
	    	}
    	}
    	else 
    	{
    		this.logMessage(ERROR_LOG, "Returned Notebook was null on passwordConfirm method.");
    		Toast.makeText(appContext, "An error occured checking password...", Toast.LENGTH_SHORT).show();
    	}
    	return false;
    }
    
    public NoteBook loadEncryptedNotebook(String noteBookName, String password) 
    {
    	return encrypt.decryptNotebook(loadNotebook(noteBookName));
    }
    
    public NoteBook loadNotebook(String noteBookName) 
    {
    	String fileName = noteBookName+".nbk";
        NoteBook loadedBook = null;
        try 
        {
            FileInputStream FIS = appContext.openFileInput(fileName);
            ObjectInputStream OIS = new ObjectInputStream(FIS);
            loadedBook = (NoteBook) OIS.readObject();
        }
        catch (IOException IOE) 
        {
        	this.logMessage(ERROR_LOG, "IOException Loading Notebook: "+noteBookName+". Message: "+IOE.getMessage());
        } catch (ClassNotFoundException CNFE) {
        	this.logMessage(ERROR_LOG, "CNFException Loading Notebook: "+noteBookName+". Message: "+CNFE.getMessage());
            }
        return loadedBook;
    }
    
    public void saveLibrary(Library library) 
    {
    	try 
        {
            FileOutputStream FOS = appContext.openFileOutput(LIBRARY_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream OOS = new ObjectOutputStream(FOS);
            OOS.writeObject(library);
        }
        catch (IOException IOE) 
        {
            this.logMessage(ERROR_LOG, "IOException on Library Save. Message: "+IOE.getMessage());
        }
    }
    
    public Library getLibrary() 
    {
    	String fileName = LIBRARY_NAME;
    	Library library = null;
    	try 
    	{
    		FileInputStream fis = appContext.openFileInput(fileName);
    		ObjectInputStream ois = new ObjectInputStream(fis);
    		library = (Library) ois.readObject();
    		ois.close();
    	}
    	catch (FileNotFoundException FNFE) 
    	{
    		this.logMessage(ERROR_LOG, "FNFException on get library. Message: "+FNFE.getMessage());
    	}
    	catch (IOException IOE) 
        {
            this.logMessage(ERROR_LOG, "IOException on Load Library. Message: "+IOE.getMessage());
        } 
    	catch (ClassNotFoundException CNFE) 
    	{
			this.logMessage(ERROR_LOG, "CNFException of Load LIbrary. Message: "+CNFE.getMessage());
    	}
    	return library;
    }
    
    public void logMessage (String filename, String message) 
    {
    	try 
    	{
    		FileOutputStream  fos= appContext.openFileOutput(filename+".log", Context.MODE_APPEND);
    		OutputStreamWriter osw = new OutputStreamWriter(fos);
    		BufferedWriter bw = new BufferedWriter(osw);
    		bw.write(message);
    		bw.newLine();
    		bw.close();
    		
    	}
    	catch (IOException IOE) 
    	{
    		
    	}
    }
    
    public NoteBook importNotebook(String filePath) 
    {
    	NoteBook notebook = null;
    	File file = new File(filePath);
    	try 
    	{
    		FileInputStream fis = new FileInputStream(file);
    		ObjectInputStream ois = new ObjectInputStream(fis);
    		notebook = (NoteBook) ois.readObject();
    		ois.close();
    	}
    	catch (IOException IOE) 
    	{
    		this.logMessage(ERROR_LOG, "IOException fetching notebook: "+IOE.getMessage());
    	}
    	catch (ClassNotFoundException CNFE) 
    	{
    		this.logMessage(ERROR_LOG, "CNFException fetching notebook: "+CNFE.getMessage());
    	}
    	return notebook;
    }
    
    public boolean checkImportedPassword(String password, NoteBook notebook) 
    {
    	encrypt.setSlaveKey(password);
    	String decryptedPassword = encrypt.decryptWithSlaveKey(notebook.getPassword());
    	if (decryptedPassword.compareTo(password) == 0) 
    	{
    		return true;
    	}
    	return false;
    }
    
    public NoteBook decryptImportedNotebook(String password, NoteBook notebook) 
    {
    	return (encrypt.decryptFromImport(notebook, password));
    }
    
    public String exportNotebookFile(String notebookName) 
    {
    	String exportResult = FileManager.EXPORT_FAIL;
    	File fullPath = null;
    	File fullFile = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
        {
        	File exst = Environment.getExternalStorageDirectory();
            String exstPath = exst.getPath();
            fullPath = new File(exstPath+FileManager.EXPORT_NOTEBOOK_DIR);
            fullPath.mkdirs();
            if (fullPath.exists()) 
            {
            	NoteBook notebook = this.loadNotebook(notebookName);
            	if (notebook.isEncrypted()) 
            	{
            		notebook = encrypt.decryptNotebook(notebook);
            		notebook = encrypt.encryptForExport(notebook);
            	}
            	try 
            	{
            		fullFile = new File(fullPath.getPath()+"/"+notebookName+".nbk");
            		FileOutputStream fos = new FileOutputStream(fullFile);
            		ObjectOutputStream oos = new ObjectOutputStream(fos);
            		oos.writeObject(notebook);
            		oos.close();
            		exportResult = FileManager.EXPORT_SUCCESS;
            	}
            	catch (IOException IOE) 
            	{
            		this.logMessage(ERROR_LOG, "Error Writing Notebook Object: "+IOE.getMessage());
            		exportResult = FileManager.EXPORT_OBJECT_FAIL;
            	}
            }
            else 
            {
            	exportResult = EXPORT_DIRECTORY_NOT_CREATED;
            }
        }
        else 
        {
        	exportResult = FileManager.EXPORT_SD_UNWRITABLE;
        }
        
        if (fullFile.exists()) 
        {
        	MediaScannerConnection.scanFile(appContext, new String[] { fullFile.getAbsolutePath() }, null, null);
        }
    	return exportResult;
    }
    
    public String exportToTextfile(String notebookName) 
    {
    	String exportResult = FileManager.EXPORT_FAIL;
    	
    	File fullPath = null;
    	File fullFile = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
        {
        	File exst = Environment.getExternalStorageDirectory();
            String exstPath = exst.getPath();
            fullPath = new File(exstPath+FileManager.EXPORT_TEXTFILE_DIR);
            fullPath.mkdirs();
            if (fullPath.exists()) 
            {
            	NoteBook notebook = this.loadNotebook(notebookName);
            	if (notebook.isEncrypted()) 
            	{
            		notebook = encrypt.decryptNotebook(notebook);
            	}
            	fullFile = new File(fullPath.getPath()+"/"+notebookName+".txt");
            	Time time = new Time();
            	time.setToNow();
            	String timeString = time.format("%m/%d/%Y %H:%M");
            	String seperator = "";
            	for (int i = 0; i < 103; i++) 
            	{
            		seperator += "-";
            	}
            	try 
            	{
            		FileWriter fw = new FileWriter(fullFile);
            		BufferedWriter bw = new BufferedWriter(fw);
            		bw.newLine();
            		bw.write(" EXPORTED NOTEBOOK ");
            		bw.newLine();
            		bw.newLine();
            		bw.write( " Date Exported: "+timeString);
            		bw.newLine();
            		bw.newLine();
            		bw.write(" Notebook Title: "+notebook.getTitle());
            		bw.newLine();
            		bw.newLine();
            		bw.write(" Date Created: "+notebook.getStringDateCreated());
            		bw.newLine();
            		bw.write(" Date Modified: "+notebook.getStringDateModified());
            		bw.newLine();
            		bw.newLine();
            		bw.write(" ENTRIES ");
            		bw.newLine();
            		bw.write(seperator);
            		if (!notebook.hasEntries()) 
            		{
            			bw.newLine();
            			bw.write(" THIS NOTEBOOK HAS NO ENTRIES");
            			bw.newLine();
            			bw.write(seperator);
            		}
            		else 
            		{
            			for (Entry entry : notebook.getAllEntries()) 
            			{
            				bw.newLine();
            				bw.write(" Entry Title: "+entry.getTitle());
            				bw.newLine();
            				bw.newLine();
            				bw.write(" Date Created: "+entry.getStringDateCreated());
            				bw.newLine();
            				bw.write(" Date Modified: "+entry.getStringDateModified());
            				bw.newLine();
            				bw.newLine();
            				if (entry.getText().length() <= 100) 
            				{
            					bw.write(" "+entry.getText());
            					bw.newLine();
                			}
            				else 
            				{
            					String fullText = entry.getText();
            					boolean keepGoing = true;
            					String nonSpace = "\\S";
            			        Pattern nonSpacePattern = Pattern.compile(nonSpace);
            			        //Matcher spaceMatcher = spacePattern.matcher(s.toString());
            					while (keepGoing) 
            					{
            						String currLine = fullText.substring(0, 100);
            						fullText = fullText.substring(100, fullText.length());
            						if (fullText.length() < 100){keepGoing = false;}
            						Matcher currLineMatcher = nonSpacePattern.matcher(currLine.substring(currLine.length()-1, currLine.length()));
            						Matcher remainMatcher = nonSpacePattern.matcher(fullText.substring(0, 1));
            						if (currLineMatcher.matches() && remainMatcher.matches()) 
            						{
            							currLine += "-";
            						}
            						
            						while (currLine.subSequence(0, 1).toString().compareTo(" ") == 0) 
            						{
            							currLine = currLine.substring(1, currLine.length());
            						}
            						
            						bw.write(" "+currLine);
            						bw.newLine();
            						
            					}
            					bw.write(" "+fullText);
            					bw.newLine();
            				}
            				bw.newLine();
            				bw.write(seperator);
            			}
            		}
            		bw.newLine();
            		bw.newLine();
            		bw.write(" END OF NOTEBOOK");
            		bw.close();
            		exportResult = FileManager.EXPORT_SUCCESS;
            	}
            	catch (IOException IOE) 
            	{
            		this.logMessage(ERROR_LOG, "Error On Text Export: "+IOE.getMessage());
            		exportResult = FileManager.EXPORT_FAIL_WRITE_TEXT;
            	}
            }
            else 
            {
            	exportResult = EXPORT_DIRECTORY_NOT_CREATED;
            }
        }
        else 
        {
        	exportResult = FileManager.EXPORT_SD_UNWRITABLE;
        }
        
        if (fullFile.exists()) 
        {
        	MediaScannerConnection.scanFile(appContext, new String[] { fullFile.getAbsolutePath() }, null, null);
        }
    	return exportResult;
    }
    
    public void updateLibrary(NoteBook notebook, String oldTitle, Library library) 
    {
    	if (notebook.getTitle().compareTo(oldTitle) != 0) 
    	{
    		this.deleteNotebook(oldTitle);
    	}
    	this.saveNotebook(notebook);
    	this.saveLibrary(library);
    }
}
