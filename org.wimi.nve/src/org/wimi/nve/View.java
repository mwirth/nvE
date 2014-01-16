package org.wimi.nve;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart {
	public static final String ID = "org.wimi.nve.view";

	private TableViewer viewer;

	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
			return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		// viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
		// | SWT.V_SCROLL);
		// viewer.setContentProvider(new ViewContentProvider());
		// viewer.setLabelProvider(new ViewLabelProvider());
		// // Provide the input to the ContentProvider
		// viewer.setInput(new String[] {"One", "Two", "Three"});

//		final Table table = new Table(parent, SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
//		TableColumn columnDate = new TableColumn(table, SWT.NONE);
//		TableColumn columnText = new TableColumn(table, SWT.NONE);
//		for (int i = 0; i < 10; i++) {
//			TableItem item = new TableItem(table, SWT.NONE);
//			item.setText(new String[] {"Text ...", "Datum"});
//		}
//		columnDate.pack();
//		columnText.pack();
//		
//		final TableEditor editor = new TableEditor(table);
//		//The editor must have the same size as the cell and must
//		//not be any smaller than 50 pixels.
//		editor.horizontalAlignment = SWT.LEFT;
//		editor.grabHorizontal = true;
//		editor.minimumWidth = 50;
//		// editing the second column
//		final int EDITABLE_COLUMN = 0;
		
//		table.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				// Clean up any previous editor control
//				Control oldEditor = editor.getEditor();
//				if (oldEditor != null) oldEditor.dispose();
//		
//				// Identify the selected row
//				TableItem item = (TableItem)e.item;
//				if (item == null)
//					return;
//		
//				// The control that will be the editor must be a child of the Table
//				Text newEditor = new Text(table, SWT.NONE);
//				newEditor.setText(item.getText(EDITABLE_COLUMN));
//				newEditor.addModifyListener(new ModifyListener() {
//					@Override
//					public void modifyText(ModifyEvent me) {
//						Text text = (Text)editor.getEditor();
//						editor.getItem().setText(EDITABLE_COLUMN, text.getText());
//					}
//				});
//				newEditor.selectAll();
//				newEditor.setFocus();
//				editor.setEditor(newEditor, item, EDITABLE_COLUMN);
//			}
//		});

		
		// create a table with 3 columns and fill with data
		final Table table = new Table(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		for (int i = 0; i < 100; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] {"cell " + i + " 0", 	"cell " + i + " 1", "cell " + i + " 2" });
		}
		column1.pack();
		column2.pack();
		column3.pack();

		// create a TableCursor to navigate around the table
		final TableCursor cursor = new TableCursor(table, SWT.NONE);
		// create an editor to edit the cell when the user hits "ENTER" 
		// while over a cell in the table
		final ControlEditor editor = new ControlEditor(cursor);
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		
		
		cursor.addSelectionListener(new SelectionAdapter() {
			// when the TableEditor is over a cell, select the corresponding row in 
			// the table
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.setSelection(new TableItem[] { cursor.getRow()});
			}
			// when the user hits "ENTER" in the TableCursor, pop up a text editor so that 
			// they can change the text of the cell
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				final Text text = new Text(cursor, SWT.NONE);
				TableItem row = cursor.getRow();
				int column = cursor.getColumn();
				text.setText(row.getText(column));
				text.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						// close the text editor and copy the data over 
						// when the user hits "ENTER"
						if (e.character == SWT.CR) {
							TableItem row = cursor.getRow();
							int column = cursor.getColumn();
							row.setText(column, text.getText());
							text.dispose();
						}
						// close the text editor when the user hits "ESC"
						if (e.character == SWT.ESC) {
							text.dispose();
						}
					}
				});
				// close the text editor when the user tabs away
				text.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						text.dispose();
					}
				});
				editor.setEditor(text);
				text.setFocus();
			}
		});
		// Hide the TableCursor when the user hits the "CTRL" or "SHIFT" key.
		// This allows the user to select multiple items in the table.
		cursor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CTRL
					|| e.keyCode == SWT.SHIFT
					|| (e.stateMask & SWT.CONTROL) != 0
					|| (e.stateMask & SWT.SHIFT) != 0) {
					cursor.setVisible(false);
				}
			}
		});
		// When the user double clicks in the TableCursor, pop up a text editor so that 
		// they can change the text of the cell.
		cursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				final Text text = new Text(cursor, SWT.NONE);
				TableItem row = cursor.getRow();
				int column = cursor.getColumn();
				text.setText(row.getText(column));
				text.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						// close the text editor and copy the data over 
						// when the user hits "ENTER"
						if (e.character == SWT.CR) {
							TableItem row = cursor.getRow();
							int column = cursor.getColumn();
							row.setText(column, text.getText());
							text.dispose();
						}
						// close the text editor when the user hits "ESC"
						if (e.character == SWT.ESC) {
							text.dispose();
						}
					}
				});
				// close the text editor when the user clicks away
				text.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						text.dispose();
					}
				});
				editor.setEditor(text);
				text.setFocus();
			}
		});
		
		// Show the TableCursor when the user releases the "SHIFT" or "CTRL" key.
		// This signals the end of the multiple selection task.
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CONTROL && (e.stateMask & SWT.SHIFT) != 0) return;
				if (e.keyCode == SWT.SHIFT && (e.stateMask & SWT.CONTROL) != 0) return;
				if (e.keyCode != SWT.CONTROL && (e.stateMask & SWT.CONTROL) != 0) return;
				if (e.keyCode != SWT.SHIFT && (e.stateMask & SWT.SHIFT) != 0) return;

				TableItem[] selection = table.getSelection();
				TableItem row = (selection.length == 0) ? table.getItem(table.getTopIndex()) : selection[0];
				table.showItem(row);
				cursor.setSelection(row, cursor.getColumn());
				cursor.setVisible(true);
				cursor.setFocus();
			}
		});
		
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		// viewer.getControl().setFocus();
	}
}