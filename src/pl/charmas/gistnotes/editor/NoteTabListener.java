/**
 * 
 */
package pl.charmas.gistnotes.editor;

import pl.charmas.R;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * @author Michał Charmas <michal@charmas.pl>
 *
 */
public class NoteTabListener implements TabListener{

	private Fragment mFragment;	
	
	public NoteTabListener(Fragment mFragment) {
		super();
		this.mFragment = mFragment;
	}

	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabReselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabSelected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.add(R.id.fragmentContent, mFragment, null);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	}

	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabUnselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(mFragment);		
	}

}
