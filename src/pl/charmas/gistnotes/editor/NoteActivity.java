package pl.charmas.gistnotes.editor;

import pl.charmas.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Activity umożliwiające edycję i podgląd notatki.
 * 
 * @author Michał Charmas <michal@charmas.pl>
 *
 */
public class NoteActivity extends Activity {
	Note note = new Note();

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_main);
        
        ActionBar actionBar = this.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        Tab editor = actionBar.newTab().setText(R.string.tab_editor).setTabListener(new NoteTabListener(new EditorFragment()));
        Tab preview = actionBar.newTab().setText(R.string.tab_preview).setTabListener(new NoteTabListener(new PreviewFragment()));
        
        actionBar.addTab(editor);
        actionBar.addTab(preview);
        actionBar.setDisplayShowTitleEnabled(false);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public Note getNote() {
		return note;
	}	
    
}