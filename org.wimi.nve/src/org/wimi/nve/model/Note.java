/**
 *
 */
package org.wimi.nve.model;

import java.util.Date;

/**
 * @author mwirth
 */
public class Note extends ObservableNote
{
	private String title;
	private String text;

	// TODO: integrate Metadata?
	private Date lastChanged;
	private int lastCursorPos;

	/**
	 * @param title
	 * @param lastChanged
	 */
	public Note(String title)
	{
		super();
		this.title = title;

		// DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		// String format = formatter.format(new Date());

		lastChanged = new Date();
		lastCursorPos = 0;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		firePropertyChange("title", this.title, this.title = title);
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text)
	{
		firePropertyChange("text", this.text, this.text = text);
	}

	/**
	 * @return the lastChanged
	 */
	public Date getLastChanged()
	{
		return lastChanged;
	}

	/**
	 * @return the lastCursorPos
	 */
	public int getLastCursorPos()
	{
		return lastCursorPos;
	}

	/**
	 * @param lastCursorPos
	 *            the lastCursorPos to set
	 */
	public void setLastCursorPos(int lastCursorPos)
	{
		this.lastCursorPos = lastCursorPos;
	}
}
