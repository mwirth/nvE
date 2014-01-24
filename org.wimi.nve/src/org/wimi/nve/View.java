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
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
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

		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewer = new TableViewer(sashForm, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());

		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		// // Provide the input to the ContentProvider
		Note[] dummyNotes = getDummyNotes();

		viewer.setInput(dummyNotes);

		final Text text = new Text(sashForm, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setLayoutData(gridData);
		sashForm.setWeights(new int[] { 1, 1 });

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

					handleSelection();
				}
				else if (e.keyCode == SWT.ARROW_UP)
				{
					int selectionIndex = table.getSelectionIndex();
					if (selectionIndex == -1 || selectionIndex == 0)
						table.setSelection(0);
					else
						table.setSelection(selectionIndex - 1);

					handleSelection();
				}
				else if (e.keyCode == SWT.CR)
				{
					int selectionIndex = table.getSelectionIndex();
					if (selectionIndex == -1)
					{
						// create a new note
					}
					else
					{
						// edit current note
						text.setFocus();
					}
				}
			}

			private void handleSelection()
			{
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
				text.setSelection(selectedNote.getLastCursorPos());
			}
		});

		text.addKeyListener(new KeyListener()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.stateMask == SWT.COMMAND && e.character == 'l')
				{
					System.out.println("command+l pressed");
					box.setFocus();
				}
				else if (e.keyCode == SWT.ESC)
				{
					System.out.println("ESC pressed");
					box.setText("");
					box.setFocus();
				}
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				// TODO Auto-generated method stub

			}
		});

		text.addFocusListener(new FocusListener()
		{

			@Override
			public void focusLost(FocusEvent e)
			{
				String text2 = text.getText();
				int caretPosition = text.getCaretPosition();
				TableItem selectedItem = table.getSelection()[0];
				if (selectedItem == null)
				{
					box.setText("");
					text.setText("");
					return;
				}
				Note selectedNote = (Note) selectedItem.getData();
				selectedNote.setText(text2);
				selectedNote.setLastCursorPos(caretPosition);
			}

			@Override
			public void focusGained(FocusEvent e)
			{
				// TODO Auto-generated method stub

			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				System.out.println("selectionChaged");
				TableItem selectedItem = table.getSelection()[0];
				Note selectedNote = (Note) selectedItem.getData();
				String title = selectedNote.getTitle();
				box.setText(title);
				box.setSelection(0, title.length() - 1);
				text.setText(selectedNote.getText());
				text.setFocus();
				text.setSelection(selectedNote.getLastCursorPos());
			}
		});
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
