/**
 * 
 */
package pl.charmas.gistnotes.editor;

import pl.charmas.R;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * @author Michał Charmas <michal@charmas.pl>
 * 
 */
public class EditorFragment extends Fragment implements TextWatcher {

	private EditText editor;
	private EditText desc;
	private EditText fName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		View view = inflater.inflate(R.layout.fragment_editor, container, false);		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		editor = (EditText) getActivity().findViewById(R.id.editorView);
		desc = (EditText) getActivity().findViewById(R.id.editTextDesc);
		fName = (EditText) getActivity().findViewById(R.id.editTextFileName);
		
		editor.addTextChangedListener(this);
		desc.addTextChangedListener(this);
		fName.addTextChangedListener(this);		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_editor, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void updateNote() {
		Note n = getNoteActivity().getNote();

		n.setContent(retriveData(editor));
		n.setDescription(retriveData(desc));
		n.setFileName(retriveData(fName));
	}

	public String retriveData(EditText t) {
		if (t != null) {
			String data = t.getText().toString();
			return data;
		}
		return "";
	}

	@Override
	public void afterTextChanged(Editable s) {
		// Nothing to do here
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// Nothing to do here
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		updateNote();
		getNoteActivity().setEdited(true);
	}
	
	private NoteActivity getNoteActivity() {
		return (NoteActivity) this.getActivity();
	}

}
