package org.wimi.nve;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.wimi.nve.model.Note;

public class View extends ViewPart
{
	public static final String ID = "org.wimi.nve.view";

	private TableViewer viewer;

	/**
	 * The content provider class is responsible for providing objects to the view. It can wrap existing objects in adapters or
	 * simply return objects as-is. These objects may be sensitive to the current input of the view, or ignore it and always show
	 * the same content (like Task List, for example).
	 */
	class ViewContentProvider implements IStructuredContentProvider
	{
		public void inputChanged(Viewer v, Object oldInput, Object newInput)
		{
		}

		public void dispose()
		{
		}

		public Object[] getElements(Object parent)
		{
			if (parent instanceof Object[])
			{
				return (Object[]) parent;
			}
			return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider
	{
		public String getColumnText(Object obj, int index)
		{
			if (obj instanceof Note)
			{
				Note note = (Note) obj;
				if (index == 0)
					return note.getTitle();
				else if (index == 1)
				{
					DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
					Date lastChanged = note.getLastChanged();
					String format = formatter.format(lastChanged);
					return format;
				}
			}

			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index)
		{
			return null;
			// return getImage(obj);
		}

		public Image getImage(Object obj)
		{
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent)
	{
		parent.setLayout(new GridLayout(1, false));
		final Text box = new Text(parent, SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		box.setLayoutData(gridData);

		Composite viewercomp = new Composite(parent, SWT.NONE);
		viewercomp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewercomp.setLayout(new FillLayout(SWT.VERTICAL));

		viewer = new TableViewer(viewercomp, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());

		final Table table = viewer.getTable();

		// // Provide the input to the ContentProvider
		Note[] dummyNotes = getDummyNotes();

		viewer.setInput(dummyNotes);

		final Text text = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setLayoutData(gridData);

		// // create a TableCursor to navigate around the table
		// final TableCursor cursor = new TableCursor(table, SWT.NONE);
		//
		// // create an editor to edit the cell when the user hits "ENTER" while over a cell in the table
		// final ControlEditor editor = new ControlEditor(cursor);
		// editor.grabHorizontal = true;
		// editor.grabVertical = true;

		// createDatabinding(text);

		box.addKeyListener(new KeyListener()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.keyCode == SWT.ARROW_DOWN)
				{
					int selectionIndex = table.getSelectionIndex();
					if (selectionIndex == -1)
						table.setSelection(0);
					else
						table.setSelection(selectionIndex + 1);

				}
				else if (e.keyCode == SWT.ARROW_UP)
				{
					int selectionIndex = table.getSelectionIndex();
					if (selectionIndex == -1 || selectionIndex == 0)
						table.setSelection(0);
					else
						table.setSelection(selectionIndex - 1);
				}
				TableItem selectedItem = table.getSelection()[0];
				if (selectedItem == null)
				{
					box.setText("");
					text.setText("");
					return;
				}
				Note selectedNote = (Note) selectedItem.getData();
				String title = selectedNote.getTitle();
				box.setText(title);
				box.setSelection(0, title.length() - 1);
				text.setText(selectedNote.getText());
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				System.out.println("selectionChaged");
			}
		});

		// cursor.addSelectionListener(new SelectionAdapter()
		// {
		// // when the TableEditor is over a cell, select the corresponding row in the table
		// @Override
		// public void widgetSelected(SelectionEvent e)
		// {
		// table.setSelection(new TableItem[] { cursor.getRow() });
		// }
		//
		// // when the user hits "ENTER" in the TableCursor, pop up a text editor so that they can change the text of the cell
		// @Override
		// public void widgetDefaultSelected(SelectionEvent e)
		// {
		// final Text text = new Text(cursor, SWT.NONE);
		// TableItem row = cursor.getRow();
		// int column = cursor.getColumn();
		// text.setText(row.getText(column));
		// text.addKeyListener(new KeyAdapter()
		// {
		// @Override
		// public void keyPressed(KeyEvent e)
		// {
		// // close the text editor and copy the data over when the user hits "ENTER"
		// if (e.character == SWT.CR)
		// {
		// TableItem row = cursor.getRow();
		// int column = cursor.getColumn();
		// row.setText(column, text.getText());
		// text.dispose();
		// }
		// // close the text editor when the user hits "ESC"
		// if (e.character == SWT.ESC)
		// {
		// text.dispose();
		// }
		// }
		// });
		//
		// // close the text editor when the user tabs away
		// text.addFocusListener(new FocusAdapter()
		// {
		// @Override
		// public void focusLost(FocusEvent e)
		// {
		// text.dispose();
		// }
		// });
		// editor.setEditor(text);
		// text.setFocus();
		// }
		// });
		//
		// // Hide the TableCursor when the user hits the "CTRL" or "SHIFT" key.
		// // This allows the user to select multiple items in the table.
		// cursor.addKeyListener(new KeyAdapter()
		// {
		// @Override
		// public void keyPressed(KeyEvent e)
		// {
		// if (e.keyCode == SWT.CTRL || e.keyCode == SWT.SHIFT || ( e.stateMask & SWT.CONTROL ) != 0
		// || ( e.stateMask & SWT.SHIFT ) != 0)
		// {
		// cursor.setVisible(false);
		// }
		// }
		// });
		//
		// // When the user double clicks in the TableCursor, pop up a text editor so that they can change the text of the cell.
		// cursor.addMouseListener(new MouseAdapter()
		// {
		// @Override
		// public void mouseDown(MouseEvent e)
		// {
		// final Text text = new Text(cursor, SWT.NONE);
		// TableItem row = cursor.getRow();
		// int column = cursor.getColumn();
		// text.setText(row.getText(column));
		// text.addKeyListener(new KeyAdapter()
		// {
		// @Override
		// public void keyPressed(KeyEvent e)
		// {
		// // close the text editor and copy the data over when the user hits "ENTER"
		// if (e.character == SWT.CR)
		// {
		// TableItem row = cursor.getRow();
		// int column = cursor.getColumn();
		// row.setText(column, text.getText());
		// text.dispose();
		// }
		//
		// // close the text editor when the user hits "ESC"
		// if (e.character == SWT.ESC)
		// {
		// text.dispose();
		// }
		// }
		// });
		//
		// // close the text editor when the user clicks away
		// text.addFocusListener(new FocusAdapter()
		// {
		// @Override
		// public void focusLost(FocusEvent e)
		// {
		// text.dispose();
		// }
		// });
		// editor.setEditor(text);
		// text.setFocus();
		// }
		// });
		//
		// // Show the TableCursor when the user releases the "SHIFT" or "CTRL" key.
		// // This signals the end of the multiple selection task.
		// table.addKeyListener(new KeyAdapter()
		// {
		// @Override
		// public void keyReleased(KeyEvent e)
		// {
		// if (e.keyCode == SWT.CONTROL && ( e.stateMask & SWT.SHIFT ) != 0)
		// return;
		// if (e.keyCode == SWT.SHIFT && ( e.stateMask & SWT.CONTROL ) != 0)
		// return;
		// if (e.keyCode != SWT.CONTROL && ( e.stateMask & SWT.CONTROL ) != 0)
		// return;
		// if (e.keyCode != SWT.SHIFT && ( e.stateMask & SWT.SHIFT ) != 0)
		// return;
		//
		// TableItem[] selection = table.getSelection();
		// TableItem row = ( selection.length == 0 ) ? table.getItem(table.getTopIndex()) : selection[0];
		// table.showItem(row);
		// cursor.setSelection(row, cursor.getColumn());
		// cursor.setVisible(true);
		// cursor.setFocus();
		// }
		// });

	}

	/**
	 * @param text
	 */
	private void createDatabinding(Text text)
	{
		// 1. Observe changes in selection.
		IObservableValue selection = ViewersObservables.observeSingleSelection(viewer);

		// 2. Observe the name property of the current selection.
		IObservableValue detailObservable = BeansObservables.observeDetailValue(selection, "text", String.class);

		// 3. Bind the Text widget to the name detail (selection's
		// name).
		new DataBindingContext().bindValue(SWTObservables.observeText(text, SWT.None), detailObservable, new UpdateValueStrategy(
			false, UpdateValueStrategy.POLICY_NEVER), null);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()
	{
		// viewer.getControl().setFocus();
	}

	private static Note[] getDummyNotes()
	{
		int size = 100;
		Note[] notes = new Note[size];
		for (int i = 0; i < size; i++)
		{
			notes[i] = new Note("Titel" + i + " ...");
			notes[i].setText("Text" + i + " ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ENDE");
		}
		return notes;
	}
}
