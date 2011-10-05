/**
 * 
 */
package pl.charmas.gistnotes.db;

import pl.charmas.gistnotes.editor.Note;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
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
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case CODE_NOTES:
            count = db.delete(TABLE_NAME, selection, selectionArgs);
            break;

        case CODE_NOTES_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.delete(TABLE_NAME, Note.KEY_ID + "=" + noteId
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri uri) {
		switch(sUriMatcher.match(uri)) {
		case CODE_NOTES:
		case CODE_NOTES_ID:
			return Note.CONTENT_TYPE_ITEM;
		
		default:
			throw new IllegalArgumentException("Unknown URI "+ uri);
		}
		
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
        // Validate the requested uri
        if (sUriMatcher.match(uri) != CODE_NOTES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        //Duplicate not to change given values
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        // Make sure that the fields are all set
        if (values.containsKey(Note.KEY_DESCRIPTION) == false) {
            Resources r = Resources.getSystem();
            values.put(Note.KEY_DESCRIPTION, r.getString(android.R.string.untitled));
        }

        if (values.containsKey(Note.KEY_CONTENT) == false) {
            values.put(Note.KEY_CONTENT, "");
        }

        if (values.containsKey(Note.KEY_FILENAME) == false) {
            values.put(Note.KEY_FILENAME, "");
        }        

        // Put row in DB
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        long rowId = db.insert(TABLE_NAME, Note.NOTE, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Note.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        // Putting failed
        throw new SQLException("Failed to insert row into " + uri);	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
	    SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case CODE_NOTES:
            count = db.update(TABLE_NAME, values, where, whereArgs);
            break;

        case CODE_NOTES_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.update(TABLE_NAME, values, Note.KEY_ID + "=" + noteId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(Note.AUTHORITY, "notes", CODE_NOTES);
		sUriMatcher.addURI(Note.AUTHORITY, "notes/#", CODE_NOTES_ID);
	}

}
