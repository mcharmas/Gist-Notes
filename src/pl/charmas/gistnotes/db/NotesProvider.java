/**
 * 
 */
package pl.charmas.gistnotes.db;

import pl.charmas.gistnotes.editor.Note;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * @author Michał Charmas <michal@charmas.pl>
 * 
 */

public class NotesProvider extends ContentProvider {
	private static final String TAG = "NotesProvider";
	
	private static final String DB_NAME = "gist_notes.db";
	private static final int DB_VERSION = 0;
	
	private static final String TABLE_NAME = "Notes";
	
	private static final UriMatcher sUriMatcher;

	private static final int CODE_NOTES = 0;
	private static final int CODE_NOTES_ID = 1;
	
	public class DBOpenHelper extends SQLiteOpenHelper {
				
		/**
		 * @param context
		 * @param name
		 * @param factory
		 * @param version
		 */
		public DBOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
					+ Note.KEY_ID + " INTEGER PRIMARY KEY,"
					+ Note.KEY_DESCRIPTION + " TEXT,"
					+ Note.KEY_FILENAME + " TEXT,"
					+ Note.KEY_CONTENT + " TEXT"
					+ ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ". Whiping all data!");
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
			onCreate(db);
		}

	}

	private DBOpenHelper mDbOpenHelper;

	@Override
	public boolean onCreate() {
		mDbOpenHelper = new DBOpenHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);

		if(sUriMatcher.match(uri)==CODE_NOTES_ID) {
			qb.appendWhere(Note.KEY_ID + "=" + uri.getPathSegments().get(1));
		}
		
		SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}	

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(Note.AUTHORITY, "notes", CODE_NOTES);
		sUriMatcher.addURI(Note.AUTHORITY, "notes/#", CODE_NOTES_ID);
	}

}
