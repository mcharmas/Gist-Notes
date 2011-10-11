/**
 * 
 */
package pl.charmas.gistnotes.editor;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.petebevin.markdown.MarkdownProcessor;

/**
 * @author Michał Charmas <michal@charmas.pl>
 *
 */
public class Note {
	public static final String NOTE = "note";	
	public static final String KEY_ID = "_id";
	public static final String KEY_FILENAME = "file_name";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_CONTENT = "content";

	public static final String AUTHORITY = "pl.charmas.gistnotes";
	public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/notes");
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.charmas.note";
	public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.charmas.note";
	public static final String[] FULL_PROJECTION = new String[] {
		Note.KEY_ID,
		Note.KEY_DESCRIPTION,
		Note.KEY_FILENAME,
		Note.KEY_CONTENT
	};
	

	private int id;
	private String description = "";	
	private String fileName = "";
	private String content = "";
	private boolean modified = true;
	private String html="";
	
	private MarkdownProcessor processor = new MarkdownProcessor();
	
	public Note() {		
	}
		
	public Note(int id, String description, String fileName, String content) {
		super();
		this.id = id;
		this.description = description;
		this.fileName = fileName;
		this.content = content;
	}
	
	/**
	 * @param c
	 */
	public Note(Cursor c) {		
		int descCol = c.getColumnIndexOrThrow(KEY_DESCRIPTION);
		int fileNameCol = c.getColumnIndexOrThrow(KEY_FILENAME);
		int contentCol = c.getColumnIndexOrThrow(KEY_CONTENT);
		int idCol = c.getColumnIndexOrThrow(KEY_ID);
		
		this.id = c.getInt(idCol);
		this.description = c.getString(descCol);
		this.fileName = c.getString(fileNameCol);
		this.content = c.getString(contentCol);		
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		this.modified = true;
	}
	
	public String render() {
		if(this.modified) {
			this.modified = false;
			this.html = processor.markdown(getContent());
		}
		return this.html;		
	}
	
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put(KEY_FILENAME, getFileName());
		values.put(KEY_DESCRIPTION, getDescription());
		values.put(KEY_CONTENT, getContent());
		return values;
	}
	
}
