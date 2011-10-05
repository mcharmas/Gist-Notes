/**
 * 
 */
package pl.charmas.gistnotes.editor;

import android.net.Uri;

import com.petebevin.markdown.MarkdownProcessor;

/**
 * @author Michał Charmas <michal@charmas.pl>
 *
 */
public class Note {
	public static final String AUTHORITY = "pl.charmas.gistnotes";
	
	public static final String KEY_ID = "id";
	public static final String KEY_FILENAME = "file_name";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_CONTENT = "content";
	
	public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/notes");

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
	
}
