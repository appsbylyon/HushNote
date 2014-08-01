package com.appsbylyon.hushnote.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Custom.ContextMenuItem;
import com.appsbylyon.hushnote.Custom.LibraryViewAdapter;
import com.appsbylyon.hushnote.FileManager.FileManager;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Objects.Library;
import com.appsbylyon.hushnote.Objects.NoteBook;
import com.appsbylyon.hushnote.Objects.NotebookSummary;
import com.appsbylyon.hushnote.Theme.Theme;
import com.appsbylyon.hushnote.fragments.ConfirmDialog;
import com.appsbylyon.hushnote.fragments.ConfirmDialog.ConfirmDialogListener;
import com.appsbylyon.hushnote.fragments.CreateNotebook;
import com.appsbylyon.hushnote.fragments.CreateNotebook.CreateNotebookListener;
import com.appsbylyon.hushnote.fragments.CustomContextMenu;
import com.appsbylyon.hushnote.fragments.CustomContextMenu.ContextMenuListener;
import com.appsbylyon.hushnote.fragments.ExportNotebook;
import com.appsbylyon.hushnote.fragments.ExportNotebook.ExportNotebookListener;
import com.appsbylyon.hushnote.fragments.FontChooser;
import com.appsbylyon.hushnote.fragments.FontChooser.FontChosen;
import com.appsbylyon.hushnote.fragments.PasswordEntry;
import com.appsbylyon.hushnote.fragments.PasswordEntry.PasswordEntryListener;
import com.appsbylyon.hushnote.fragments.ThemeChooser;
import com.appsbylyon.hushnote.fragments.ThemeChooser.ThemeChooserListener;

/**
 * Library View Class
 * 
 * Modified: 7/11/2014
 * 
 * @author Adam Lyon
 *
 */
public class LibraryView extends Activity implements FontChosen, OnClickListener, CreateNotebookListener, ConfirmDialogListener, PasswordEntryListener, ExportNotebookListener, ThemeChooserListener, ContextMenuListener 
{
	public static final int CONTEXT_OPTION_DELETE = 0;
	public static final int CONTEXT_OPTION_EXPORT = 1;
	public static final int CONTEXT_OPTION_EDIT = 2;
	
	public static final String CONTEXT_DELETE_TEXT = "Delete";
	public static final String CONTEXT_EXPORT_TEXT = "Export";
	public static final String CONTEXT_EDIT_TEXT = "Edit";
	
	
	
	private static final int CONFIRM_DIALOG_DELETE_NOTEBOOK = 1;
	private static final int OPEN_ENCRYPTED_NOTEBOOK = 2;
	private static final int EXPORT_NOTEBOOK = 3;
	private static final int IMPORT_NOTEBOOK = 4;
	private static final int EDIT_NOTEBOOK = 5;
	private static final int SAVE_LIBRARY_FAILURE = 0;
	private static final int SAVE_LIBRARY_SAVE_SUCCESS = 1;
	private static final int SAVE_LIBRARY_DELETE_SUCCESS = 2;
	private static final int REQUEST_CODE_GET_FILE = 1;
	
	private LinearLayout background;
	
	private Theme theme = Theme.getInstance();
	
	private TextView headerLabel;
	private TextView noLibraryLabel;
	
	private ActionBar actionBar;
	
	private Button createNotebook;
	
	private ListView libraryList;
	
	private Font font = Font.getInstance();
	
	private FileManager fileManager = FileManager.getInstance();
	
	private Library library;
	
	private NoteBook bookToSave;
	private String bookToDelete;
	private NoteBook bookToImport;
	private NoteBook bookToEdit;
	
	private int screenWidth;
	private int screenHeight;
	private int contextMenuSelectedItemPosition;
	private int clickedPosition;
	private int exportPosition;
	
	private boolean doesABookNeedSaved = false;
	private boolean doesABookNeedDeleted = false;
	
	private String oldBookTitle;
	
	private NotebookSummary summaryToEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_view);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		//saveLibrary();
	}
	
	@Override
	public View onCreatePanelView(int featureId) 
	{
		actionBar = this.getActionBar();
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(theme.getSmallBorder()));
		
		background = (LinearLayout) findViewById(R.id.library_view_background);
		background.setBackgroundResource(theme.getLargeBorder());
		
		libraryList = (ListView) findViewById(R.id.library_list_view);
		
		headerLabel = (TextView) findViewById(R.id.library_label);
		headerLabel.setTypeface(font.getCurrentFont());
		headerLabel.setTextSize((float) font.getFontSize());
		headerLabel.setTextColor(theme.getTextColor());
		
		noLibraryLabel = (TextView) findViewById(R.id.library_view_no_library_label);
		noLibraryLabel.setTypeface(font.getCurrentFont());
		noLibraryLabel.setTextSize(font.getFontSize());
		noLibraryLabel.setTextColor(theme.getTextColor());
		
		
		createNotebook = (Button) findViewById(R.id.library_view_add_notebook_button);
		createNotebook.setBackgroundResource(theme.getAddButton());
		createNotebook.setOnClickListener(this);
		
		libraryList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) 
			{
				if(library.getSummaryAtPosition(position).isEncrypted()) 
				{
					clickedPosition = position;
					FragmentManager fm = LibraryView.this.getFragmentManager();
					PasswordEntry passwordEntry = new PasswordEntry();
					Bundle bundle = new Bundle();
					bundle.putInt(getString(R.string.enter_password_caller), OPEN_ENCRYPTED_NOTEBOOK);
					bundle.putString(getString(R.string.enter_password_message_text), 
							"This Notebook is Protected! \nEnter Password to Continue..");
					passwordEntry.setArguments(bundle);
					passwordEntry.show(fm, "enter_password_fragment");
					
				}
				else 
				{
					openNotebook(library.getSummaryAtPosition(position).getTitle(), false, "");
				}
			}
		});
		
		libraryList.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				contextMenuSelectedItemPosition = position;
				FragmentManager fm = LibraryView.this.getFragmentManager();
				CustomContextMenu menu = new CustomContextMenu();
				menu.addMenuItems(new ContextMenuItem(CONTEXT_DELETE_TEXT, theme.getTrash()));
				menu.addMenuItems(new ContextMenuItem(CONTEXT_EXPORT_TEXT, theme.getExport()));
				menu.addMenuItems(new ContextMenuItem(CONTEXT_EDIT_TEXT, theme.getEdit()));
				//menu.setAdapter();
				menu.show(fm, "context_menu_fragment");
				return true;
			}
			
		});
		
		new LoadLibrary().execute();
		return null;
	}
	
	private void showToast(String message) 
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.library_view, menu);
		return true;
	}
	
	
	/**
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		super.onPrepareOptionsMenu(menu);
		int menuSize = menu.size();
		for (int i = 0; i < menuSize; i++) 
		{
			MenuItem item = menu.getItem(i);
			item.se
			View myView = View.inflate(this, R.layout.menu_item, null);
			String text = item.getTitle().toString();
			TextView textView = (TextView) myView.findViewById(R.id.menu_menu_item);
			textView.setTypeface(font.getCurrentFont());
			textView.setTextSize(font.getFontSize());
			textView.setTextColor(theme.getTextColor());
			textView.setText(text);
			item.setActionView(myView);
			
			
		}
		return true;
		
	}
	*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		FragmentManager fm = this.getFragmentManager();
		
		switch (id) 
		{
		case R.id.action_import_notebook:
			Intent intent = new Intent(this, FileChooser.class);
			this.startActivityForResult(intent, REQUEST_CODE_GET_FILE);
			break;
		case R.id.action_choose_font:
			FontChooser fontChooser = new FontChooser();
			fontChooser.show(fm, "font_chooser_fragment");
			break;
		case R.id.action_theme:
			ThemeChooser themeChooser = new ThemeChooser();
			themeChooser.show(fm, "theme_chooser_background");
			break;
		case R.id.action_help:
			Intent helpIntent = new Intent(this, Help.class);
			this.startActivity(helpIntent);
			break;
		case R.id.action_about_fonts:
			Intent aboutFontIntent = new Intent(this, AboutFonts.class);
			this.startActivity(aboutFontIntent);
			break;
		case R.id.action_about:
			Intent aboutIntent = new Intent(this, About.class);
			this.startActivity(aboutIntent);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		switch (requestCode) 
		{
		case REQUEST_CODE_GET_FILE:
			
			if(resultCode == RESULT_OK) 
			{
				String filePath = data.getStringExtra(getString(R.string.file_choose_result_value));
				NoteBook notebook = fileManager.importNotebook(filePath);
				if (notebook!=null) 
				{
					if(!notebook.isEncrypted()) 
					{
						if (library == null) 
						{
							library = new Library();
						}
						NotebookSummary summary = new NotebookSummary(notebook.getTitle(), notebook.getDescription(), false);
						library.addSummary(summary);
						bookToSave = notebook;
						doesABookNeedSaved = true;
						new SaveLibrary().execute();
					}
					else  
					{
						bookToImport = notebook;
						FragmentManager fm = this.getFragmentManager();
						PasswordEntry passwordEntry = new PasswordEntry();
						Bundle bundle = new Bundle();
						bundle.putInt(getString(R.string.enter_password_caller), LibraryView.IMPORT_NOTEBOOK);
						bundle.putString(getString(R.string.enter_password_message_text), 
								"This Notebook is Protected! \nEnter Password to Continue..");
						passwordEntry.setArguments(bundle);
						passwordEntry.show(fm, "enter_password_fragment");
					}
					
				}
			}
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_library_view,
					container, false);
			return rootView;
		}
	}
	
	public void libraryLoaded() 
	{
		if (library != null) 
		{
			noLibraryLabel.setVisibility(View.INVISIBLE);
			libraryList.setVisibility(View.VISIBLE);
			LibraryViewAdapter adapter = new LibraryViewAdapter(this, library.getAllSummaries());
			libraryList.setAdapter(adapter);
		}
		else 
		{
			libraryList.setVisibility(View.INVISIBLE);
			noLibraryLabel.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void fontChosen() 
	{
		headerLabel.setTypeface(font.getCurrentFont());
		headerLabel.setTextSize((float) font.getFontSize());
		if (library != null) 
		{
			LibraryViewAdapter adapter = new LibraryViewAdapter(this, library.getAllSummaries());
			libraryList.setAdapter(adapter);
		}
		else 
		{
			noLibraryLabel.setTypeface(font.getCurrentFont());
			noLibraryLabel.setTextSize(font.getFontSize());
		}
		
	}
	
	private class LoadLibrary extends AsyncTask<Void, Library, Library> 
	{

		@Override
		protected Library doInBackground(Void... arg0) 
		{
			Library library = fileManager.getLibrary();
			return library;
		}
		
		protected void onPostExecute(Library loadedLibrary) 
		{
			library = loadedLibrary;
			libraryLoaded();
		}
		
	}
	
	private class SaveLibrary extends AsyncTask<Void, Integer, Integer>
	{
		@Override
		protected Integer doInBackground(Void... arg0) 
		{
			int returnStatus = SAVE_LIBRARY_FAILURE;
			if (library.getNumberOfSummaries() > 0) 
			{
				fileManager.saveLibrary(library);
			}
			else 
			{
				fileManager.deleteLibrary();
				library = null;
			}
			if (doesABookNeedSaved) 
			{
				fileManager.saveNotebook(bookToSave);
				doesABookNeedSaved = false;
				returnStatus = SAVE_LIBRARY_SAVE_SUCCESS;
			}
			if (doesABookNeedDeleted) 
			{
				fileManager.deleteNotebook(bookToDelete);
				doesABookNeedDeleted = false;
				returnStatus = SAVE_LIBRARY_DELETE_SUCCESS;
			}
			return returnStatus;
		}
		
		
		protected void onPostExecute(Integer status) 
		{
			libraryLoaded();
			switch(status) 
			{
			case LibraryView.SAVE_LIBRARY_FAILURE:
				Toast.makeText(LibraryView.this, "Operation Failed", Toast.LENGTH_SHORT).show();
				break;
			case LibraryView.SAVE_LIBRARY_SAVE_SUCCESS:
				Toast.makeText(LibraryView.this, "Notebook Saved", Toast.LENGTH_SHORT).show();
				break;
			case LibraryView.SAVE_LIBRARY_DELETE_SUCCESS:
				Toast.makeText(LibraryView.this, "Notebook Deleted", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
	
	@Override
	public int getScreenWidth() 
	{
		return screenWidth;
	}

	@Override
	public int getScreenHeight() {
		return screenHeight;
	}

	@Override
	public void onClick(View view) 
	{
		if (view == createNotebook) 
		{
			FragmentManager fm = this.getFragmentManager();
			Bundle bundle = new Bundle();
			bundle.putInt(getString(R.string.create_notebook_mode), CreateNotebook.MODE_CREATE);
			CreateNotebook createNewNotebook = new CreateNotebook();
			createNewNotebook.setArguments(bundle);
			createNewNotebook.show(fm, "create_notebook_fragment");
		}
		
	}

	@Override
	public int newNotebookGetScreenWidth() 
	{
		return screenWidth;
	}

	@Override
	public int newNotebookGetScreenHeight() 
	{
		return screenHeight;
	}

	@Override
	public void newNoteBookCreated(NoteBook notebook,
			NotebookSummary notebookSummary) 
	{
		if (library == null) 
		{
			library = new Library();
		}
		library.addSummary(notebookSummary);
		doesABookNeedSaved = true;
		bookToSave = notebook;
		if (notebook == null) 
		{
			this.showToast("notebook is null...");
		}
		else 
		{
			new SaveLibrary().execute();
		}
	}

	@Override
	public void choiceSelected(int choice, int caller) 
	{
		switch(caller) 
		{
		case CONFIRM_DIALOG_DELETE_NOTEBOOK:
			if (choice == ConfirmDialog.CHOICE_CONFIRM) 
			{
				if (library.getSummaryAtPosition(contextMenuSelectedItemPosition).isEncrypted()) 
				{
					FragmentManager fm = this.getFragmentManager();
					PasswordEntry passwordEntry = new PasswordEntry();
					Bundle bundle = new Bundle();
					bundle.putInt(getString(R.string.enter_password_caller), caller);
					bundle.putString(getString(R.string.enter_password_message_text), 
							"This Notebook is Protected! \nEnter Password to Continue..");
					passwordEntry.setArguments(bundle);
					passwordEntry.show(fm, "enter_password_fragment");
				}
				else 
				{
					deleteNotebook();
				}
			}
		}
		
	}

	@Override
	public void passwordEntered(int choice, int caller, String password) 
	{
		switch (caller) 
		{
		case CONFIRM_DIALOG_DELETE_NOTEBOOK:
			if (choice == PasswordEntry.CHOICE_CONFIRM) 
			{
				if(fileManager.confirmPassword(library.getSummaryAtPosition(contextMenuSelectedItemPosition).getTitle(), password)) 
				{
					deleteNotebook();
				}
				else 
				{
					Toast.makeText(this, "Unable To Delete. Password Did Not Match", Toast.LENGTH_SHORT).show();
				}
			}
		break;
		case OPEN_ENCRYPTED_NOTEBOOK:
			if (choice == PasswordEntry.CHOICE_CONFIRM) 
			{
				if(fileManager.confirmPassword(library.getSummaryAtPosition(clickedPosition).getTitle(), password)) 
				{
					openNotebook(library.getSummaryAtPosition(clickedPosition).getTitle(), true, password);
				}
				else 
				{
					Toast.makeText(this, "Unable To Open Notebook. Password Did Not Match", Toast.LENGTH_SHORT).show();
				}
			}
		break;
		case EXPORT_NOTEBOOK:
			if (choice == PasswordEntry.CHOICE_CONFIRM) 
			{
				if(fileManager.confirmPassword(library.getSummaryAtPosition(contextMenuSelectedItemPosition).getTitle(), password)) 
				{
					exportNotebook(contextMenuSelectedItemPosition);
				}
				else 
				{
					Toast.makeText(this, "Unable To Export Notebook. Password Did Not Match", Toast.LENGTH_SHORT).show();
				}
			}
		break;
		case IMPORT_NOTEBOOK:
			if (choice == PasswordEntry.CHOICE_CONFIRM) 
			{
				if (fileManager.checkImportedPassword(password, bookToImport)) 
				{
					if (library == null) 
					{
						library = new Library();
					}
					bookToSave = fileManager.decryptImportedNotebook(password, bookToImport);
					NotebookSummary summary = new NotebookSummary(bookToSave.getTitle(), bookToSave.getDescription(), true);
					library.addSummary(summary);
					doesABookNeedSaved = true;
					new SaveLibrary().execute();
				}
				else 
				{
					Toast.makeText(this, "Invalid Password. Import Aborted.", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case EDIT_NOTEBOOK:
			if (choice == PasswordEntry.CHOICE_CONFIRM) 
			{
				if(fileManager.confirmPassword(library.getSummaryAtPosition(contextMenuSelectedItemPosition).getTitle(), password)) 
				{
					editNotebook(contextMenuSelectedItemPosition, password);
				}
				else 
				{
					Toast.makeText(this, "Unable To Edit Notebook. Password Did Not Match", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	
		
		
		
	}
	
	private void exportNotebook(int position) 
	{
		exportPosition = position;
		FragmentManager fm = this.getFragmentManager();
		ExportNotebook export = new ExportNotebook();
		export.show(fm, "export_notebook_fragment");
	}
	
	private void deleteNotebook() 
	{
		bookToDelete = library.getSummaryAtPosition(contextMenuSelectedItemPosition).getTitle();
		doesABookNeedDeleted = true;
		library.removeSummaryAtPosition(contextMenuSelectedItemPosition);
		new SaveLibrary().execute();
	}
	
	private void openNotebook(String name, boolean isEncrypted, String password) 
	{
		Intent intent = new Intent(LibraryView.this, NotebookView.class);
		intent.putExtra(getString(R.string.open_notebook_name), name);
		intent.putExtra(getString(R.string.open_notebook_isencrypted), isEncrypted);
		intent.putExtra(getString(R.string.open_notebook_password), password);
		intent.putExtra(getString(R.string.notebook_view_do_not_kill),true);
		startActivity(intent);
	}

	@Override
	public void exportOptionSelected(int exportOption) 
	{
		String notebookTitle = library.getSummaryAtPosition(exportPosition).getTitle();
		String result =  "";
		switch(exportOption) 
		{
		case ExportNotebook.EXPORT_AS_NOTEBOOK:
			result = fileManager.exportNotebookFile(notebookTitle);
			Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
			break;
		case ExportNotebook.EXPORT_AS_TEXTFILE:
			result = fileManager.exportToTextfile(notebookTitle);
			Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
			break;
		}
		
	}

	@Override
	public void themeChosen() 
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPrefs.edit().putInt(getString(R.string.pref_theme_id), theme.getCurrTheme()).apply();
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(theme.getSmallBorder()));
		
		background.setBackgroundResource(theme.getLargeBorder());
		headerLabel.setTextColor(theme.getTextColor());
		noLibraryLabel.setTextColor(theme.getTextColor());
		createNotebook.setBackgroundResource(theme.getAddButton());
		libraryLoaded();
		
	}

	@Override
	public void contextItemSelected(int contextOption) 
	{
		FragmentManager fm = this.getFragmentManager();
		Bundle bundle = new Bundle();
		
		switch (contextOption) 
		{
		case CONTEXT_OPTION_DELETE:
			ConfirmDialog confirmDialog = new ConfirmDialog();
			bundle.putString(getString(R.string.confirm_dialog_title), "Delete: "+library.getSummaryAtPosition(contextMenuSelectedItemPosition).getTitle());
			bundle.putString(getString(R.string.confirm_dialog_message), "Are You Sure You Want To Delete The Notebook: "+library.getSummaryAtPosition(contextMenuSelectedItemPosition).getTitle()+"?");
			bundle.putInt(getString(R.string.confirm_dialog_caller), CONFIRM_DIALOG_DELETE_NOTEBOOK);
			confirmDialog.setArguments(bundle);
			confirmDialog.show(fm, "confirm_dialog_fragment");
			break;
		case CONTEXT_OPTION_EXPORT:
			if (library.getSummaryAtPosition(contextMenuSelectedItemPosition).isEncrypted()) 
			{
				PasswordEntry passwordEntry = new PasswordEntry();
				bundle.putInt(getString(R.string.enter_password_caller), LibraryView.EXPORT_NOTEBOOK);
				bundle.putString(getString(R.string.enter_password_message_text), 
						"This Notebook is Protected! \nEnter Password to Continue..");
				passwordEntry.setArguments(bundle);
				passwordEntry.show(fm, "enter_password_fragment");
			}
			else 
			{
				exportNotebook(contextMenuSelectedItemPosition);
			}
			break;
		case CONTEXT_OPTION_EDIT:
			if (library.getSummaryAtPosition(contextMenuSelectedItemPosition).isEncrypted()) 
			{
				PasswordEntry passwordEntry = new PasswordEntry();
				bundle.putInt(getString(R.string.enter_password_caller), LibraryView.EDIT_NOTEBOOK);
				bundle.putString(getString(R.string.enter_password_message_text), 
						"This Notebook is Protected! \nEnter Password to Continue..");
				passwordEntry.setArguments(bundle);
				passwordEntry.show(fm, "enter_password_fragment");
			}
			else 
			{
				editNotebook(contextMenuSelectedItemPosition, null);
			}
			break;
		}
		
	}

	private void editNotebook(int positionToEdit, String password) 
	{
		Toast.makeText(this, "AT SOME POINT YOU WILL BE ABLE TO EDIT THIS NOTEBOOK", Toast.LENGTH_SHORT).show();
		summaryToEdit = library.getSummaryAtPosition(positionToEdit);
		FragmentManager fm = this.getFragmentManager();
		Bundle bundle = new Bundle();
		bundle.putInt(getString(R.string.create_notebook_mode), CreateNotebook.MODE_EDIT);
		bundle.putInt(getString(R.string.create_notebook_edit_position), positionToEdit);
		if (summaryToEdit.isEncrypted()) 
		{
			bundle.putString(getString(R.string.create_notebook_edit_password), password);
			bookToEdit = fileManager.loadEncryptedNotebook(summaryToEdit.getTitle(), password);
			
		}
		else 
		{
			bookToEdit = fileManager.loadNotebook(summaryToEdit.getTitle());
		}
		CreateNotebook createNewNotebook = new CreateNotebook();
		createNewNotebook.setArguments(bundle);
		createNewNotebook.show(fm, "create_notebook_fragment");
	}

	@Override
	public NotebookSummary getSummaryToEdit() 
	{
		return summaryToEdit;
	}

	@Override
	public void summaryEdited(NotebookSummary summary, int editPosition, boolean isSecured, String password) 
	{
		oldBookTitle = bookToEdit.getTitle();
		
		bookToEdit.setTitle(summary.getTitle());
		bookToEdit.setDescription(summary.getDescription());
		bookToEdit.setEncrypted(isSecured);
		if (isSecured) 
		{
			bookToEdit.setPassword(password);
		}
		else 
		{
			bookToEdit.setPassword("");
		}
			
		bookToEdit.setDateModified(System.currentTimeMillis());
		library.replaceSummary(summary, editPosition);
		new UpdateLibrary().execute();
		
	}
	
	private class UpdateLibrary extends AsyncTask<Void, Integer, Integer>
	{
		@Override
		protected Integer doInBackground(Void... arg0) 
		{
			int returnStatus = SAVE_LIBRARY_FAILURE;
			fileManager.updateLibrary(bookToEdit, oldBookTitle, library);
			return returnStatus;
		}
		
		
		protected void onPostExecute(Integer status) 
		{
			Toast.makeText(LibraryView.this, "Notebook Edited", Toast.LENGTH_SHORT).show();
			libraryLoaded();
		}
	}
}
