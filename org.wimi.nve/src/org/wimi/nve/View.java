package org.wimi.nve;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
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
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.wimi.nve.editor.ViewerConfiguration;
import org.wimi.nve.model.notesmodel.Model;
import org.wimi.nve.model.notesmodel.Note;
import org.wimi.nve.model.notesmodel.NotesmodelFactory;
import org.wimi.nve.model.notesmodel.NotesmodelPackage;
import org.wimi.nve.model.notesmodel.impl.NotesmodelPackageImpl;
import org.wimi.nve.table.NoteFilter;
import org.wimi.nve.util.ColorStore;

public class View extends ViewPart
{
	public static final String ID = "org.wimi.nve.view";

	private Note actNote;
	private Model model;

	private Text titleBox;
	private Text filterBox;

	private TableViewer tableviewer;

	private SourceViewer sourceviewer;
	private Document doc;

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
					Date lastChanged = note.getLastChange();
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
		parent.setLayout(new GridLayout(2, false));

		Composite textComposite = new Composite(parent, SWT.NONE);
		textComposite.setLayout(new GridLayout(2, false));
		textComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Browser browser = new Browser(parent, SWT.NONE);
		GridData browserGD = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
		browserGD.widthHint = SWT.DEFAULT;
		browserGD.heightHint = SWT.DEFAULT;
		browser.setLayoutData(browserGD);

		filterBox = new Text(textComposite, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		filterBox.setLayoutData(gd);

		titleBox = new Text(textComposite, SWT.BORDER);

		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		tableviewer = new TableViewer(sashForm, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		tableviewer.setLabelProvider(new ViewLabelProvider());
		getSite().setSelectionProvider(tableviewer);
		// hookDoubleClickCommand();

		model = load();
		// model = createModel();

		ObservableListContentProvider contentProvider = new ObservableListContentProvider();

		// put all attributes (from class Note) that are going to be shown into a map and associate the column title
		HashMap<EAttribute, String> attributeMap = new HashMap<EAttribute, String>();
		attributeMap.put(NotesmodelPackage.Literals.NOTE__TITLE, "Title");

		// create a column for each attribute & setup the databinding
		for (EAttribute attribute : attributeMap.keySet())
		{
			// create a new column
			TableViewerColumn tvc = new TableViewerColumn(tableviewer, SWT.LEFT);
			// determine the attribute that should be observed
			IObservableMap map = EMFProperties.value(attribute).observeDetail(contentProvider.getKnownElements());
			tvc.setLabelProvider(new ObservableMapCellLabelProvider(map));
			// set the column title & set the size
			tvc.getColumn().setText(attributeMap.get(attribute));
			tvc.getColumn().setWidth(80);
		}
		tableviewer.setContentProvider(contentProvider);

		final Table table = tableviewer.getTable();
		table.setLinesVisible(true);

		// set the model (which is a list of persons)
		tableviewer.setInput(EMFProperties.list(NotesmodelPackage.Literals.MODEL__NOTES).observe(model));

		final NoteFilter filter = new NoteFilter();
		tableviewer.addFilter(filter);

		sourceviewer = new SourceViewer(sashForm, new VerticalRuler(0), SWT.V_SCROLL | SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		doc = new Document();
		doc.set("");
		sourceviewer.setDocument(doc);
		ViewerConfiguration conf = new ViewerConfiguration(new ColorStore(), null, browser);
		sourceviewer.configure(conf);

		sashForm.setWeights(new int[] { 1, 1 });

		DataBindingContext dbc = new DataBindingContext();

		// binding for the TextField which should display the text for the Note
		IObservableValue targetText = WidgetProperties.text(SWT.Modify).observe(sourceviewer.getTextWidget());
		IObservableValue targetBox = WidgetProperties.text(SWT.Modify).observe(titleBox);

		IObservableValue actSelectedNote = ViewersObservables.observeSingleSelection(tableviewer);

		IObservableValue textDetailValue =
			EMFProperties.value(NotesmodelPackage.Literals.NOTE__TEXT).observeDetail(actSelectedNote);
		IObservableValue boxDetailValue =
			EMFProperties.value(NotesmodelPackage.Literals.NOTE__TITLE).observeDetail(actSelectedNote);

		dbc.bindValue(targetText, textDetailValue);
		dbc.bindValue(targetBox, boxDetailValue);

		tableviewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				// TODO Auto-generated method stub
				// filterBox.setText(actNote.getTitle());
			}
		});

		titleBox.addTraverseListener(new TraverseListener()
		{

			@Override
			public void keyTraversed(TraverseEvent e)
			{
				System.out.println("traversed");
				int length = titleBox.getText().length();
				if (e.keyCode == SWT.CR && actNote != null && length == 0)
				{
					System.out.println("actNote: " + actNote.getTitle());
					model.getNotes().remove(actNote);
				}
			}
		});

		filterBox.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{
				if (filterBox.getText().length() <= 0)
				{
					filter.setSearchText("");
					tableviewer.refresh();
				}
			}
		});

		// add listeners
		filterBox.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.keyCode == SWT.ARROW_DOWN)
				{
					int selRow = table.getSelectionIndex();
					int length = tableviewer.getTable().getItems().length;
					if (selRow == -1)
					{
						selRow = 0;
					}
					else
					{
						selRow++;
					}
					if (selRow >= length)
					{
						selRow = 0;
					}
					actNote = (Note) tableviewer.getElementAt(selRow);
					tableviewer.setSelection(new StructuredSelection(actNote), true);
					e.doit = false;
					filterBox.selectAll();
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
					actNote = (Note) tableviewer.getElementAt(selRow);
					tableviewer.setSelection(new StructuredSelection(actNote), true);
					e.doit = false;
					filterBox.selectAll();
				}
				else if (e.keyCode == SWT.CR)
				{
					int selectionIndex = table.getSelectionIndex();
					if (selectionIndex == -1)
					{
						// create a new note
						Note note = createNote(filterBox.getText());
						actNote = note;
						// actNote = (Note) viewer.getElementAt(i);
						tableviewer.setSelection(new StructuredSelection(actNote), true);
						StyledText text = sourceviewer.getTextWidget();
						text.setFocus();
					}
					else
					{
						// Note note = (Note) ( (StructuredSelection) viewer.getSelection() ).getFirstElement();
						// edit current note
						StyledText text = sourceviewer.getTextWidget();
						text.setFocus();
						text.setSelection(actNote.getLastCursorPos());
					}
				}
			}

			/*
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			@Override
			public void keyReleased(KeyEvent e)
			{
				if (Character.isLetterOrDigit(e.character) || e.character == SWT.BS || e.character == SWT.DEL)
				{
					String t = filterBox.getText();
					filter.setSearchText(t);
					tableviewer.refresh();
					// search first match
					TableItem[] items = tableviewer.getTable().getItems();
					for (int i = 0; i < items.length; i++)
					{

						if (items[i].getText().startsWith(t))
						{
							actNote = (Note) tableviewer.getElementAt(i);
							tableviewer.setSelection(new StructuredSelection(actNote), true);
							return;
						}
					}
					// no fit Title found
					StructuredSelection sel = new StructuredSelection();
					tableviewer.setSelection(sel);
				}
			}
		});

		final StyledText text = sourceviewer.getTextWidget();

		text.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (Util.isMac())
				{
					if (e.stateMask == SWT.COMMAND && e.character == 'l')
					{
						System.out.println("command+l pressed");
						filterBox.selectAll();
						filterBox.setFocus();
					}
				}
				else
				{
					System.out.println("char" + e.character);
					System.out.println("code" + e.keyCode);
					System.out.println("mask" + e.stateMask);

					if (e.keyCode == 108 && e.stateMask == 262144 && e.character == '\f')
					{
						System.out.println("strg+l pressed");
						System.out.println("command+l pressed");
						filterBox.selectAll();
						filterBox.setFocus();
					}

					if (e.keyCode == SWT.CTRL && e.keyCode == SWT.CTRL)
					{
						System.out.println("strg+l pressed");
					}

					if (e.keyCode == SWT.CTRL)
					{
						System.out.println("strg pressed");
					}
					if (e.keyCode == SWT.CTRL && e.character == 'l')
					{
						System.out.println("strg+l pressed");
						filterBox.selectAll();
						filterBox.setFocus();
					}
				}

				if (e.keyCode == SWT.ESC)
				{
					System.out.println("ESC pressed");
					// Note note = (Note) ( (StructuredSelection) viewer.getSelection() ).getFirstElement();
					// actNote.setLastCursorPos(text.getCaretPosition());
					StyledText t = (StyledText) e.widget;
					int caretPosition = t.getCaretOffset();
					actNote.setLastCursorPos(caretPosition);
					// clear the selection
					StructuredSelection sel = new StructuredSelection();
					tableviewer.setSelection(sel);

					filterBox.setFocus();
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{

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
				if (actNote == null)
					return;

				StyledText t = (StyledText) e.widget;
				int caretPosition = t.getCaretOffset();
				// do not set position if ESC was pressed
				System.out.println("actNote: " + actNote.getTitle() + " cursorPos: " + caretPosition);
				actNote.setLastCursorPos(caretPosition);

				System.out.println("saving ...");
				saveModel(model);
			}
		});

		text.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				if (text.isFocusControl())
					return;

				String typedTitle = filterBox.getText();
				String detailText = text.getText();
				int indexOf = detailText.indexOf(typedTitle);
				if (indexOf == -1)
					return;

				text.setSelection(indexOf, indexOf + typedTitle.length());
			}
		});
	}

	/**
	 * @param text
	 */
	private Note createNote(String title)
	{
		NotesmodelPackageImpl.init();

		NotesmodelFactory factory = NotesmodelFactory.eINSTANCE;

		Note note = factory.createNote();
		note.setTitle(title);
		note.setText("");
		model.getNotes().add(note);
		return note;
	}

	/**
	 * @param text
	 */
	private void createDatabinding(Text text)
	{
		// 1. Observe changes in selection.
		IObservableValue selection = ViewersObservables.observeSingleSelection(tableviewer);

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
		NotesmodelPackageImpl.init();

		NotesmodelFactory factory = NotesmodelFactory.eINSTANCE;

		List<Note> notes = new ArrayList<Note>(size);
		// Note[] notes = new Note[size];
		for (int i = 0; i < size; i++)
		{
			Note note = factory.createNote();
			note.setTitle("Titel" + i + " ...");
			notes.add(note);
			note.setText("Text" + i + " ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ENDE");
		}
		return notes;
	}

	private static Model createModel()
	{
		int size = 100;
		NotesmodelFactory factory = NotesmodelFactory.eINSTANCE;

		Model root = factory.createModel();
		for (int i = 0; i < size; i++)
		{
			Note note = factory.createNote();
			note.setTitle("Titel" + i + " ...");
			note.setText("Text" + i + " ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ENDE");
			root.getNotes().add(note);
		}
		return root;
	}

	private void saveModel(Model model)
	{
		// Register the XMI resource factory for the .nvEnotes extension
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("nvEnotes", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// create a resource
		Resource resource = resSet.createResource(URI.createURI("notes/My.nvEnotes"));

		// Get the first model element and cast it to the right type, in my example everything is hierarchical included in this
		// first node
		resource.getContents().add(model);

		// now save the content.
		try
		{
			resource.save(Collections.EMPTY_MAP);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Model load()
	{
		// Initialize the model
		NotesmodelPackage.eINSTANCE.eClass();

		// Register the XMI resource factory for the .website extension

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("nvEnotes", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// Get the resource
		Resource resource = resSet.getResource(URI.createURI("notes/My.nvEnotes"), true);
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		Model loadedModel = (Model) resource.getContents().get(0);
		return loadedModel;
	}

	// private void hookDoubleClickCommand()
	// {
	// viewer.addDoubleClickListener(new IDoubleClickListener()
	// {
	// public void doubleClick(DoubleClickEvent event)
	// {
	// IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
	// try
	// {
	// handlerService.executeCommand("org.wimi.nve.editor.openeditor", null);
	// }
	// catch (Exception ex)
	// {
	// throw new RuntimeException("org.wimi.nve.editor.openeditor not found");
	// }
	// }
	// });
	// }

	private void sendNote(Note note)
	{
		BundleContext ctx = FrameworkUtil.getBundle(View.class).getBundleContext();
		if (ctx == null)
		{
			System.err.println("can not send results, BundleContext not available");
			return;
		}

		ServiceReference<EventAdmin> ref = ctx.getServiceReference(EventAdmin.class);
		if (ref == null)
		{
			System.err.println("can not send results, EventAdminService not available");
			return;
		}

		EventAdmin eventAdmin = ctx.getService(ref);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("NOTE", note);

		Event event = new Event("viewcommunication/syncEvent-", properties);
		eventAdmin.sendEvent(event);
	}
}
