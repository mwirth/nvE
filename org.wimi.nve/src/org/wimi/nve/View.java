package org.wimi.nve;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.wimi.nve.model.Note;

public class View extends ViewPart
{
	public static final String ID = "org.wimi.nve.view";

	private Note actNote;

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
			if (parent instanceof List)
			{
				return ( (List<?>) parent ).toArray();
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
		List<Note> dummyNotes = getDummyNotes();

		viewer.setInput(dummyNotes);

		final Text text = new Text(sashForm, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setLayoutData(gridData);
		sashForm.setWeights(new int[] { 1, 1 });

		DataBindingContext dbc = new DataBindingContext();

		// binding for the TextField which should display the text for the Note
		IObservableValue targetText = WidgetProperties.text(SWT.Modify).observe(text);
		IObservableValue targetBox = WidgetProperties.text(SWT.Modify).observe(box);

		IViewerObservableValue actSelectedNote = ViewerProperties.singleSelection().observe(viewer);

		// observe the text attribute of the selection
		IObservableValue textDetailValue = BeanProperties.value(Note.class, "text").observeDetail(actSelectedNote);
		IObservableValue boxDetailValue = BeanProperties.value(Note.class, "title").observeDetail(actSelectedNote);

		dbc.bindValue(targetText, textDetailValue);
		dbc.bindValue(targetBox, boxDetailValue);

		// add listeners
		box.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.keyCode == SWT.ARROW_DOWN)
				{
					int selRow = table.getSelectionIndex();
					if (selRow == -1)
					{
						selRow = 0;
					}
					else
					{
						selRow++;
					}
					actNote = (Note) viewer.getElementAt(selRow);
					viewer.setSelection(new StructuredSelection(actNote), true);
					e.doit = false;
					box.selectAll();
				}
				else if (e.keyCode == SWT.ARROW_UP)
				{
					int selRow = table.getSelectionIndex();

					int selectionIndex = table.getSelectionIndex();
					if (selectionIndex == -1 || selectionIndex == 0)
					{
						selRow = 0;
					}
					else
					{
						selRow--;
					}
					actNote = (Note) viewer.getElementAt(selRow);
					viewer.setSelection(new StructuredSelection(actNote), true);
					e.doit = false;
					box.selectAll();
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
						// Note note = (Note) ( (StructuredSelection) viewer.getSelection() ).getFirstElement();
						// edit current note
						text.setFocus();
						text.setSelection(actNote.getLastCursorPos());
					}
				}
			}
		});

		text.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.stateMask == SWT.COMMAND && e.character == 'l')
				{
					System.out.println("command+l pressed");
					box.selectAll();
					box.setFocus();
				}
				else if (e.keyCode == SWT.ESC)
				{
					System.out.println("ESC pressed");
					// Note note = (Note) ( (StructuredSelection) viewer.getSelection() ).getFirstElement();
					// actNote.setLastCursorPos(text.getCaretPosition());
					Text t = (Text) e.widget;
					int caretPosition = t.getCaretPosition();
					actNote.setLastCursorPos(caretPosition);
					// clear the selection
					StructuredSelection sel = new StructuredSelection();
					viewer.setSelection(sel);

					box.setFocus();
				}
			}
		});

		text.addFocusListener(new FocusAdapter()
		{
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt.events.FocusEvent)
			 */
			@Override
			public void focusLost(FocusEvent e)
			{
				Text t = (Text) e.widget;
				int caretPosition = t.getCaretPosition();
				// do not set position if ESC was pressed
				System.out.println("actNote: " + actNote.getTitle() + " cursorPos: " + caretPosition);
				actNote.setLastCursorPos(caretPosition);
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

	private static List<Note> getDummyNotes()
	{
		int size = 100;
		List<Note> notes = new ArrayList<Note>(size);
		// Note[] notes = new Note[size];
		for (int i = 0; i < size; i++)
		{
			Note n = new Note("Titel" + i + " ...");
			notes.add(n);
			n.setText("Text" + i + " ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ENDE");
		}
		return notes;
	}
}
