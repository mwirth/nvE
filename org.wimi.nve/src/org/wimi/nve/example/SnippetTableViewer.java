package org.wimi.nve.example;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class SnippetTableViewer
{
	static class ViewContentProvider implements IStructuredContentProvider
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

	static class ViewLabelProvider extends LabelProvider implements ITableLabelProvider
	{
		public String getColumnText(Object obj, int index)
		{
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index)
		{
			return null;
			// return getImage(obj);
		}
	}

	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);

		shell.setLayout(new GridLayout(1, false));
		final Text box = new Text(shell, SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		box.setLayoutData(gridData);

		Composite viewercomp = new Composite(shell, SWT.NONE);
		viewercomp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewercomp.setLayout(new FillLayout(SWT.VERTICAL));

		TableViewer viewer = new TableViewer(viewercomp, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());

		final Table table = viewer.getTable();
		// table.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		table.setSize(200, 200);

		int size = 128;
		String[] content = new String[size];

		for (int i = 0; i < size; i++)
		{
			content[i] = "Item " + i;
		}
		viewer.setInput(content);

		table.setTopIndex(95);
		shell.pack();
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
