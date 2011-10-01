/**
 * 
 */
package pl.charmas.gistnotes.editor;

import pl.charmas.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Michał Charmas <michal@charmas.pl>
 *
 */
public class PreviewFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_preview, container, false);
	}

}
