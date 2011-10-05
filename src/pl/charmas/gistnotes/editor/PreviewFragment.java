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
import android.webkit.WebView;

/**
 * @author Michał Charmas <michal@charmas.pl>
 *
 */
public class PreviewFragment extends Fragment {

	WebView webView = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_preview, container, false); 			
		webView = (WebView) v.findViewById(R.id.textView1);

		return v;
	}

	@Override
	public void onResume() {
  		String data = ((NoteActivity)getActivity()).getNote().render();
		webView.loadData(data, "text/html", "UTF-8");
		super.onResume();
	}
	
	
}
