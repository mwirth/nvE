package org.wimi.nve.example;

import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class DatabindingTableApp extends ApplicationWindow
{
	private static class Sorter extends ViewerSorter
	{
		public int compare(Viewer viewer, Object e1, Object e2)
		{
			Person item1 = (Person) e1;
			Person item2 = (Person) e2;
			String s1 = item1.getLast() + item1.getFirst();
			String s2 = item2.getLast() + item2.getFirst();
			return s1.compareTo(s2);
		}
	}

	@SuppressWarnings("unused")
	private DataBindingContext m_bindingContext;
	private Table table;
	private TableViewer tableViewer;
	@SuppressWarnings("unused")
	private Workgroup workgroup;
	private Set memberSet;
	private Action clearSelectionAction;

	/**
	 * Create the application window.
	 */
	public DatabindingTableApp()
	{
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		// Create the composite
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Add TableColumnLayout
		TableColumnLayout layout = new TableColumnLayout();
		composite.setLayout(layout);

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setSorter(new Sorter());
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection sel = (IStructuredSelection) tableViewer.getSelection();
				Person person = (Person) sel.getFirstElement();
				if (person != null)
				{
					System.out.println("Selected : " + person.getFirst() + " " + person.getLast());
				}
				else
				{
					System.out.println("Selection cleared!");
				}
			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			public void doubleClick(DoubleClickEvent event)
			{
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				Person person = (Person) sel.getFirstElement();
				if (person != null)
				{
					System.out.println("Double-click on : " + person.getFirst() + " " + person.getLast());
				}
			}
		});
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnFirst = tableViewerColumn.getColumn();
		layout.setColumnData(tblclmnFirst, new ColumnWeightData(2, ColumnWeightData.MINIMUM_WIDTH, true));
		tblclmnFirst.setText("First");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnLast = tableViewerColumn_1.getColumn();
		// Specify width using weights
		layout.setColumnData(tblclmnLast, new ColumnWeightData(2, ColumnWeightData.MINIMUM_WIDTH, true));
		tblclmnLast.setText("Last");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTitle = tableViewerColumn_2.getColumn();
		// Specify width using weights
		layout.setColumnData(tblclmnTitle, new ColumnWeightData(4, ColumnWeightData.MINIMUM_WIDTH, true));
		tblclmnTitle.setText("Title");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnEmail = tableViewerColumn_3.getColumn();
		// Specify width using weights
		layout.setColumnData(tblclmnEmail, new ColumnWeightData(6, ColumnWeightData.MINIMUM_WIDTH, true));
		tblclmnEmail.setText("Email");

		initWorkgroup();
		m_bindingContext = initDataBindings();

		return container;
	}

	private void initWorkgroup()
	{
		Workgroup w = new Workgroup();
		Person p = new Person();
		p.setId(1);
		p.setFirst("John");
		p.setLast("Smith");
		p.setTitle("Manager");
		p.setEmail("jsmith@somecompany.com");
		w.add(p);
		p = new Person();
		p.setId(2);
		p.setFirst("Sam");
		p.setLast("Johnson");
		p.setTitle("Supervisor");
		p.setEmail("sjohnson@somecompany.com");
		w.add(p);
		this.setWorkgroup(w);
	}

	private void setWorkgroup(Workgroup workgroup)
	{
		this.workgroup = workgroup;
		this.memberSet = workgroup.getMemberSet();
	}

	/**
	 * Create the actions.
	 */
	private void createActions()
	{
		// Create the actions
		{
			clearSelectionAction = new Action("Clear Selection")
			{
				@Override
				public void run()
				{
					StructuredSelection sel = new StructuredSelection();
					tableViewer.setSelection(sel);
					super.run();
				}

			};
		}
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager()
	{
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style)
	{
		ToolBarManager toolBarManager = new ToolBarManager(style);
		toolBarManager.add(clearSelectionAction);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager()
	{
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[])
	{
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable()
		{
			public void run()
			{
				try
				{
					DatabindingTableApp window = new DatabindingTableApp();
					window.setBlockOnOpen(true);
					window.open();
					Display.getCurrent().dispose();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("TableViewer with Databinding");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(450, 300);
	}

	protected DataBindingContext initDataBindings()
	{
		DataBindingContext bindingContext = new DataBindingContext();
		//
		ObservableSetContentProvider setContentProvider = new ObservableSetContentProvider();
		tableViewer.setContentProvider(setContentProvider);
		//
		IObservableMap[] observeMaps =
			PojoObservables.observeMaps(setContentProvider.getKnownElements(), Person.class, new String[] { "first", "last",
				"title", "email" });
		tableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		//
		WritableSet writableSet = new WritableSet(memberSet, Person.class);
		tableViewer.setInput(writableSet);
		//
		return bindingContext;
	}
}
