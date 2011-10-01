/**
 * 
 */
package pl.charmas.gistnotes.editor;

import pl.charmas.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Michał Charmas <michal@charmas.pl>
 *
 */
public class EditorFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_editor, container, false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_editor, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	
	
}
