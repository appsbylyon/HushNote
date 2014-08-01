package com.appsbylyon.hushnote.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.appsbylyon.hushnote.Custom.NotebookViewAdapter;
import com.appsbylyon.hushnote.FileManager.FileManager;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Objects.Entry;
import com.appsbylyon.hushnote.Objects.NoteBook;
import com.appsbylyon.hushnote.Theme.Theme;
import com.appsbylyon.hushnote.fragments.AddEntry;
import com.appsbylyon.hushnote.fragments.AddEntry.AddEntryListener;
import com.appsbylyon.hushnote.fragments.ConfirmDialog;
import com.appsbylyon.hushnote.fragments.ConfirmDialog.ConfirmDialogListener;
import com.appsbylyon.hushnote.fragments.CustomContextMenu;
import com.appsbylyon.hushnote.fragments.CustomContextMenu.ContextMenuListener;
import com.appsbylyon.hushnote.fragments.ViewEntry;
import com.appsbylyon.hushnote.fragments.ViewEntry.ViewEntryListener;
/**
 * Class for the notebook view
 * 
 * Modified: 7/17/2014
 * 
 * @author Adam Lyon
 *
 */
public class NotebookView extends Activity implements OnClickListener, AddEntryListener, ViewEntryListener, ConfirmDialogListener, ContextMenuListener
{
	public static final int CONTEXT_OPTION_DELETE = 0;
	public static final int CONTEXT_OPTION_EDIT = 1;
	
	public static final String CONTEXT_DELETE_TEXT = "Delete";
	public static final String CONTEXT_EDIT_TEXT = "Edit";
	
	public static final int MODE_ADD_ENTRY = 1;
	public static final int MODE_EDIT_ENTRY = 2;
	public static final int CALLER_DELETE_ENTRY = 1;
	
	private static final int SELF_KILL_TIME = 5000;
	
	private static final String ENTRY_ADDED = "New Entry Added: ";
	private static final String ENTRY_UPDATED = "Entry Updated: ";
	private static final String ENTRY_REMOVED = "Entry Removed: ";
	
	private FileManager fileManager = FileManager.getInstance();
	
	private Font font = Font.getInstance();
	
	private Theme theme = Theme.getInstance();
	
	private ActionBar actionBar;
	
	private LinearLayout background;
	
	private TextView titleView;
	private TextView descView;
	private TextView dateCreatedView;
	private TextView dateModifiedView;
	private TextView entriesHeader;
	private TextView noEntriesLabel;
	
	private Button addEntryButton;
	
	private ListView entriesList;
	
	private NoteBook notebook;
	
	private String openNotebookName = "";
	private String password = "";
	private String addStatusText = "";
	
	private boolean isEncrypted = false;
	private boolean isSelfKilling = false;
	private boolean doNotKill = false;
	
	private int screenWidth;
	private int contextMenuSelectedItemPosition;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_notebook_view);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		Bundle bundle = this.getIntent().getExtras();
		
		openNotebookName = bundle.getString(getString(R.string.open_notebook_name));
		password = bundle.getString(getString(R.string.open_notebook_password));
		isEncrypted = bundle.getBoolean(getString(R.string.open_notebook_isencrypted));
		doNotKill = bundle.getBoolean(getString(R.string.notebook_view_do_not_kill));
		
		if (isEncrypted) 
		{
			notebook = fileManager.loadEncryptedNotebook(openNotebookName, password);
		}
		else 
		{
			notebook = fileManager.loadNotebook(openNotebookName);
		}
		actionBar = this.getActionBar();
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(theme.getSmallBorder()));
		
		background = (LinearLayout) this.findViewById(R.id.activity_notebook_view_background);
		background.setBackgroundResource(theme.getLargeBorder());
		
		titleView = (TextView) findViewById(R.id.notebook_view_title_text);
		titleView.setTypeface(font.getCurrentFont());
		titleView.setTextSize(font.getFontSize()+5);
		titleView.setText(notebook.getTitle());
		titleView.setTextColor(theme.getTextColor());
		
		descView = (TextView) findViewById(R.id.notebook_view_desc_text);
		descView.setTypeface(font.getCurrentFont());
		descView.setTextSize(font.getFontSize()+2);
		descView.setText(notebook.getDescription());
		descView.setTextColor(theme.getTextColor());
		
		dateCreatedView = (TextView) findViewById(R.id.notebook_view_date_created);
		dateCreatedView.setTypeface(font.getCurrentFont());
		dateCreatedView.setTextSize(font.getFontSize());
		dateCreatedView.setText("Created: "+notebook.getStringDateCreated());
		dateCreatedView.setTextColor(theme.getTextColor());
		
		dateModifiedView = (TextView) findViewById(R.id.notebook_view_date_modified);
		dateModifiedView.setTypeface(font.getCurrentFont());
		dateModifiedView.setTextSize(font.getFontSize());
		dateModifiedView.setText("Modified: "+notebook.getStringDateModified());
		dateModifiedView.setTextColor(theme.getTextColor());
		
		entriesHeader = (TextView) findViewById(R.id.notebook_view_entries_header);
		entriesHeader.setTypeface(font.getCurrentFont());
		entriesHeader.setTextSize(font.getFontSize()+2);
		entriesHeader.setTextColor(theme.getTextColor());
		
		noEntriesLabel = (TextView) findViewById(R.id.notebook_view_no_entries_label);
		noEntriesLabel.setTypeface(font.getCurrentFont());
		noEntriesLabel.setTextSize(font.getFontSize());
		noEntriesLabel.setTextColor(theme.getTextColor());
		
		addEntryButton = (Button) findViewById(R.id.notebook_view_add_entry_button);
		addEntryButton.setBackgroundResource(theme.getAddButton());
		addEntryButton.setOnClickListener(this);
		
		entriesList = (ListView) findViewById(R.id.notebook_view_entry_listview);
		
		updateEntryList();
		
		entriesList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) 
			{
				FragmentManager fm = NotebookView.this.getFragmentManager();
				ViewEntry viewEntry = new ViewEntry();
				Bundle bundle = new Bundle();
				bundle.putInt(getString(R.string.view_entry_selected_entry), position);
				viewEntry.setArguments(bundle);
				viewEntry.show(fm, "view_entry_fragment_layout");
			}
		});
		
		entriesList.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				contextMenuSelectedItemPosition = position;
				FragmentManager fm = NotebookView.this.getFragmentManager();
				CustomContextMenu menu = new CustomContextMenu();
				menu.addMenuItems(new ContextMenuItem(CONTEXT_DELETE_TEXT, theme.getTrash()));
				menu.addMenuItems(new ContextMenuItem(CONTEXT_EDIT_TEXT, theme.getEdit()));
				//menu.setAdapter();
				menu.show(fm, "context_menu_fragment");
				return true;
			}
			
		});
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		if (!isSelfKilling) 
		{
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			sharedPrefs.edit().putLong(getString(R.string.library_on_pause_time), System.currentTimeMillis()).apply();
			doNotKill = false;
		}
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		long pauseTime = sharedPrefs.getLong(getString(R.string.library_on_pause_time), 0);
		if (pauseTime != 0 ) 
		{
			long currTime = System.currentTimeMillis();
			if ((currTime - pauseTime) > NotebookView.SELF_KILL_TIME) 
			{
				if (notebook != null) 
				{
					if (notebook.isEncrypted()) 
					{
						if (!doNotKill) 
						{
							sharedPrefs.edit().putLong(getString(R.string.library_on_pause_time), 0).apply();
							isSelfKilling = true;
							finish();
						}
					}
				}
				
			}
		}
	}
	
	/**
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, view, menuInfo);
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.notebook_view_context_menu, menu);
		AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
		contextMenuSelectedItemPosition = acmi.position;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
		FragmentManager fm = this.getFragmentManager();
		
		Bundle bundle = new Bundle();
		
		switch (item.getItemId()) 
		{
			case R.id.action_edit_entry:
				AddEntry addEntry = new AddEntry();
				bundle.putInt(getString(R.string.add_entry_dialog_mode), NotebookView.MODE_EDIT_ENTRY);
				bundle.putInt(getString(R.string.add_entry_edit_position), contextMenuSelectedItemPosition);
				addEntry.setArguments(bundle);
				addEntry.show(fm, "add_entry_fragment");
				return true;
			case R.id.action_delete_entry:
				ConfirmDialog confirmDialog = new ConfirmDialog();
				bundle = new Bundle();
				bundle.putString(getString(R.string.confirm_dialog_title), getString(R.string.delete_entry_confirm_title));
				bundle.putString(getString(R.string.confirm_dialog_message), getString(R.string.delete_entry_confirm_message));
				bundle.putInt(getString(R.string.confirm_dialog_caller), NotebookView.CALLER_DELETE_ENTRY);
				confirmDialog.setArguments(bundle);
				confirmDialog.show(fm, "confirm_dialog_fragment");
				return true;
			default:
				return super.onContextItemSelected(item);
		}
		
	}
	*/
	@Override
	public void onClick(View view) 
	{
		if (view.equals(addEntryButton)) 
		{
			FragmentManager fm = this.getFragmentManager();
			AddEntry addEntry = new AddEntry();
			Bundle bundle = new Bundle();
			bundle.putInt(getString(R.string.add_entry_dialog_mode), NotebookView.MODE_ADD_ENTRY);
			addEntry.setArguments(bundle);
			addEntry.show(fm, "add_entry_fragment");
		}
		
	}

	@Override
	public void addNewEntry(Entry entry) 
	{
		notebook.addEntry(entry);
		notebook.setDateModified(System.currentTimeMillis());
		addStatusText = NotebookView.ENTRY_ADDED;
		new SaveNotebook().execute();
		updateEntryList();
		this.dateModifiedView.setText("Modified: " +notebook.getStringDateModified());
		
	}

	@Override
	public int getScreenWidth() 
	{
		return screenWidth;
	}
	
	private class SaveNotebook extends AsyncTask<Void, String, String>
	{
		@Override
		protected String doInBackground(Void... arg0) 
		{
			//int returnStatus = 0;
			String returnStatus = fileManager.saveNotebook(notebook);
			return returnStatus;
		}
		
		
		protected void onPostExecute(String status) 
		{
			Toast.makeText(NotebookView.this, addStatusText+status, Toast.LENGTH_SHORT).show();
			
		}
	}

	@Override
	public Entry getEntry(int entryNum) 
	{
		return notebook.getEntry(entryNum);
	}

	@Override
	public Entry getEntryToEdit(int position) 
	{
		return notebook.getEntry(position);
	}

	@Override
	public void entryEdited(Entry entry, int position) 
	{
		notebook.replaceEntry(entry, position);
		notebook.setDateModified(System.currentTimeMillis());
		addStatusText = NotebookView.ENTRY_UPDATED;
		new SaveNotebook().execute();
		updateEntryList();
		this.dateModifiedView.setText("Modified: " +notebook.getStringDateModified());
	}

	@Override
	public int getScreenWidthViewEntry() 
	{
		return this.screenWidth;
	}

	@Override
	public void choiceSelected(int choice, int caller) 
	{
		switch (caller) 
		{
		case NotebookView.CALLER_DELETE_ENTRY:
			if (choice ==  ConfirmDialog.CHOICE_CONFIRM) 
			{
				notebook.removeEntry(contextMenuSelectedItemPosition);
				notebook.setDateModified(System.currentTimeMillis());
				addStatusText = NotebookView.ENTRY_REMOVED;
				new SaveNotebook().execute();
				updateEntryList();
				this.dateModifiedView.setText("Modified: " +notebook.getStringDateModified());
			}
		}
	}
	
	private void updateEntryList() 
	{
		if (notebook.hasEntries()) 
		{
			noEntriesLabel.setVisibility(View.INVISIBLE);
			NotebookViewAdapter adapter = new NotebookViewAdapter(this, notebook.getAllEntries());
			entriesList.setAdapter(adapter);
		}
		else 
		{
			NotebookViewAdapter adapter = new NotebookViewAdapter(this, notebook.getAllEntries());
			entriesList.setAdapter(adapter);
			noEntriesLabel.setVisibility(View.VISIBLE);
		}
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
			bundle.putString(getString(R.string.confirm_dialog_title), getString(R.string.delete_entry_confirm_title));
			bundle.putString(getString(R.string.confirm_dialog_message), getString(R.string.delete_entry_confirm_message));
			bundle.putInt(getString(R.string.confirm_dialog_caller), NotebookView.CALLER_DELETE_ENTRY);
			confirmDialog.setArguments(bundle);
			confirmDialog.show(fm, "confirm_dialog_fragment");
			break;
		case CONTEXT_OPTION_EDIT:
			AddEntry addEntry = new AddEntry();
			bundle.putInt(getString(R.string.add_entry_dialog_mode), NotebookView.MODE_EDIT_ENTRY);
			bundle.putInt(getString(R.string.add_entry_edit_position), contextMenuSelectedItemPosition);
			addEntry.setArguments(bundle);
			addEntry.show(fm, "add_entry_fragment");
			
			break;
		}
		
	}
}// End of NotebookView class
