/**
 * 
 */
package pl.charmas.gistnotes.list;

import pl.charmas.gistnotes.IWithNote;
import pl.charmas.gistnotes.Note;
import android.app.ListFragment;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * @author Michał Charmas <michal@charmas.pl>
 * 
 */
public class NotesListFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Cursor c = getActivity().managedQuery(Note.CONTENT_URI,
				Note.FULL_PROJECTION, null, null, null);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, c,
				new String[] { Note.KEY_DESCRIPTION },
				new int[] { android.R.id.text1 });
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Uri uri = ContentUris.withAppendedId(Note.CONTENT_URI, id);
		Cursor c = getActivity().managedQuery(uri, Note.FULL_PROJECTION, null,
				null, null);
		if (c.getCount() == 1) {
			c.moveToFirst();
			try {
				Note n = new Note(c);
				((IWithNote) getActivity()).setNote(n);
			} catch (IllegalArgumentException e) {
				((IWithNote) getActivity()).setNote(null);
			}
		}
	}

}
