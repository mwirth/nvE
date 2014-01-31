package org.wimi.nve.table;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.wimi.nve.model.notesmodel.Note;

public class NoteFilter extends ViewerFilter
{
	private String searchString;

	public void setSearchText(String s)
	{
		this.searchString = s;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element)
	{
		if (searchString == null || searchString.length() == 0 || element == null)
			return true;

		Note n = (Note) element;

		if (n.getTitle().indexOf(searchString) != -1)
			return true;

		if (n.getText().indexOf(searchString) != -1)
			return true;

		return false;
	}
}
