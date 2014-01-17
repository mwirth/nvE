/**
 *
 */
package org.wimi.nve.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

/**
 * @author mwirth
 */
public class Note
{
	private String title;
	private String text;
	private Date lastChanged;

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

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
		this.title = title;
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
		this.text = text;
	}

	/**
	 * @return the lastChanged
	 */
	public Date getLastChanged()
	{
		return lastChanged;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		changeSupport.removePropertyChangeListener(listener);
	}
}
