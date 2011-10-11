/**
 * 
 */
package pl.charmas.gistnotes.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import pl.charmas.gistnotes.Note;
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
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author Michał Charmas <michal@charmas.pl>
 * 
 */

public class NotesProvider extends ContentProvider {
	private static final String TAG = "NotesProvider";
	
	private static final String DB_NAME = "gist_notes.db";
	private static final int DB_VERSION = 3;
	
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
		
        try {
			backupDb();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		if (sortOrder==null || sortOrder=="") {
		         sortOrder = Note.KEY_DESCRIPTION;
		}
		
		SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
//		boolean empty = c.moveToFirst();
//		String ss = c.getString(0);
//		String sss = c.getString(1);
//		String ssss = c.getString(2);
//		String sssss = c.getString(3);
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
        //long rowId = db.insert(TABLE_NAME, Note.NOTE, values);
        long rowId = db.insertOrThrow(TABLE_NAME, null, values);
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
	
		
	
	
	private void backupDb() throws IOException {
	    File sd = Environment.getExternalStorageDirectory();
	    File data = Environment.getDataDirectory();

	    if (sd.canWrite()) {

	        String currentDBPath = "/data/pl.charmas/databases/gist_notes.db";
	        String backupDBPath = "/charmas_logs/gist_notes.db";

	        File currentDB = new File(data, currentDBPath);
	        File backupDB = new File(sd, backupDBPath);

	        if (backupDB.exists())
	            backupDB.delete();

	        if (currentDB.exists()) {
	            makeLogsFolder();

	            copy(currentDB, backupDB);
	       }

	        String dbFilePath = backupDB.getAbsolutePath();
	   }
	}

	 private void makeLogsFolder() {
	    try {
	        File sdFolder = new File(Environment.getExternalStorageDirectory(), "/charmas_logs/");
	        sdFolder.mkdirs();
	    }
	    catch (Exception e) {}
	  }

	private void copy(File from, File to) throws FileNotFoundException, IOException {
	    FileChannel src = null;
	    FileChannel dst = null;
	    try {
	        src = new FileInputStream(from).getChannel();
	        dst = new FileOutputStream(to).getChannel();
	        dst.transferFrom(src, 0, src.size());
	    }
	    finally {
	        if (src != null)
	            src.close();
	        if (dst != null)
	            dst.close();
	    }
	}

}
