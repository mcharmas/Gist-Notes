/**
 * 
 */
package pl.charmas.gistnotes.list;

import pl.charmas.R;
import pl.charmas.gistnotes.IWithNote;
import pl.charmas.gistnotes.Note;
import pl.charmas.gistnotes.PreviewFragment;
import pl.charmas.gistnotes.editor.NoteActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Activity pokazujące listę notatek. Umożlwiające synchronizację notatek
 * z chmurą i pokazujące wersje notatki.
 * 
 * @author Michał Charmas <michal@charmas.pl>
 *
 */
public class NotesListActivity extends Activity implements IWithNote {

	private Note note = null;
	PreviewFragment previewFragment = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_main);
		previewFragment = (PreviewFragment) getFragmentManager().findFragmentByTag("preview");
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_list, menu);
		inflater.inflate(R.menu.menu_main, menu);		
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_note:
			addNoteAction();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Start activity with blank note.
	 */
	private void addNoteAction() {
		Intent i = new Intent();
		i.setClass(getApplication(), NoteActivity.class);
		startActivity(i);
	}

	@Override
	public Note getNote() {		
		return note;
	}

	@Override
	public void setNote(Note n) {
		this.note = n;
		if(previewFragment!=null)
		{
			previewFragment.refresh();
		}
	}

}
