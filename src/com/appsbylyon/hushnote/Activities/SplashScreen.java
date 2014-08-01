package com.appsbylyon.hushnote.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.LinearLayout;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.FileManager.FileManager;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Security.Encrypter;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Splash Screen Activity
 * 
 * Modified: 7/25/2014
 * 
 * @author Adam Lyon
 *
 */
public class SplashScreen extends Activity 
{
	private static final int SHOW_SPLASH_TIME = 3000;
	
	private SharedPreferences sharedPrefs;
	
	private Font font = Font.getInstance();
	
	private Encrypter encrypt = Encrypter.getInstance();
	
	private FileManager fileManager = FileManager.getInstance();
	
	private Theme theme = Theme.getInstance();
	
	private LinearLayout background;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_splash_screen);
		background = (LinearLayout) findViewById(R.id.activity_splash_screen_background);
		
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
		
		int themeId = sharedPrefs.getInt(getString(R.string.pref_theme_id), Theme.METAL_THEME);
		
		theme.setTheme(themeId);
		
		background.setBackgroundResource(theme.getLargeBorder());
		
		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				Intent mainActivity = new Intent(SplashScreen.this, LibraryView.class);
				startActivity(mainActivity);
				finish();
			}
			
		}, SHOW_SPLASH_TIME);
		
		initialize();
	}// end of onCreate method
	
	private void initialize() 
	{
		boolean isFirstRun = sharedPrefs.getBoolean(getString(R.string.pref_first_run_key), true);
		
		if (isFirstRun) 
		{
			String masterKey = encrypt.generateMasterKey();
			Editor edit = sharedPrefs.edit();
			edit.putBoolean(getString(R.string.pref_first_run_key), false);
			edit.putString(getString(R.string.pref_master_key), masterKey);
			edit.putInt(getString(R.string.pref_font_size_key), Font.DEFAULT_FONT_SIZE);
			edit.putInt(getString(R.string.pref_font_id_key), Font.DEFAULT_FONT);
			edit.apply();
		}
		String masterKey = sharedPrefs.getString(getString(R.string.pref_master_key), "");
		int fontSize = sharedPrefs.getInt(getString(R.string.pref_font_size_key), Font.DEFAULT_FONT_SIZE);
		int fontId = sharedPrefs.getInt(getString(R.string.pref_font_id_key), Font.DEFAULT_FONT);
		//Toast.makeText(getApplication(), "MasterKey: "+masterKey+"\nFontSize: "+fontSize+"\nFont Id: "+fontId, Toast.LENGTH_LONG).show();
		addFonts();
		font.setCurrentFont(fontId);
		font.setFontSize(fontSize);
		font.setSharedPrefs(sharedPrefs, getString(R.string.pref_font_id_key), getString(R.string.pref_font_size_key));
		encrypt.setMasterKey(masterKey);
		fileManager.setAppContext(getApplicationContext());
	}
	
	private void addFonts() 
	{
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/36daysag.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/AA_typewriter.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/aescrawl.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/ammys_handwriting.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/bocuma.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/combustw.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/galvaniz.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/grotesq.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/hannahs_messy_handwriting.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/madscrwl.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/anglicantext.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/canterbury.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/cutelove.ttf"));
		font.setMonkeyPos(font.getNumberOfFonts());
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/iloveyoumonkey.ttf"));
		//font.addFont(Typeface.createFromAsset(getAssets(), "fonts/panicstricken.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/cybercaligraphic.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/tumbleweed.ttf"));
		font.addFont(Typeface.createFromAsset(getAssets(), "fonts/zenda.ttf"));
		
	}
}// End of SplashScreen class
