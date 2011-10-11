/**
 * 
 */
package pl.charmas.gistnotes;

import pl.charmas.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * @author Michał Charmas <michal@charmas.pl>
 *
 */
public class CloseAlertDialog extends DialogFragment{
    
	public static CloseAlertDialog newInstance(int title) {
        CloseAlertDialog frag = new CloseAlertDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((IDialogHandler)getActivity()).dialogPositiveClick();
                        }
                    }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((IDialogHandler)getActivity()).dialogNegativeClick();
                        }
                    }
                )
                .create();
    }

}
