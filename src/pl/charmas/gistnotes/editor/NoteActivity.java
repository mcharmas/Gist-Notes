package pl.charmas.gistnotes.editor;

import pl.charmas.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Activity umożliwiające edycję i podgląd notatki.
 * 
 * @author Michał Charmas <michal@charmas.pl>
 * 
 */
public class NoteActivity extends Activity implements IDialogHandler, IWithNote {

	Note note = new Note();
	boolean edited = false;

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_main);

		ActionBar actionBar = this.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab editor = actionBar.newTab().setText(R.string.tab_editor)
				.setTabListener(new NoteTabListener(new EditorFragment()));
		Tab preview = actionBar.newTab().setText(R.string.tab_preview)
				.setTabListener(new NoteTabListener(new PreviewFragment()));

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save:
			saveAction();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean saveAction() {
		Note n = getNote();
		if (!n.getDescription().equals("")) {
			getContentResolver().insert(Note.CONTENT_URI, n.getContentValues());
			Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT)
					.show();
			edited = false;
			return true;
		} else {
			Toast.makeText(this, R.string.toast_no_description,
					Toast.LENGTH_SHORT).show();
			return false;

		}
	}

	@Override
	public void dialogPositiveClick() {
		if(this.saveAction())
		{
			this.finish();			
		}
	}

	@Override
	public void dialogNegativeClick() {
		this.setEdited(false);
		this.finish();
	}

	@Override
	public void finish() {
		if (isEdited()) {
			CloseAlertDialog dialog = CloseAlertDialog
					.newInstance(R.string.alert_title);
			dialog.show(getFragmentManager(), "dialog");
		} else {
			super.finish();
		}
	}

	@Override
	public void setNote(Note n) {
		this.note = n;		
	}

}