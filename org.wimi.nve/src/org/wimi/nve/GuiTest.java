/**
 *
 */
package org.wimi.nve;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * @author mwirth
 */
public class GuiTest extends Composite
{
	private Text text;
	private Table table;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GuiTest(Composite parent, int style)
	{
		super(parent, style);
		setLayout(new GridLayout(1, false));

		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setBounds(0, 0, 64, 19);

		TableViewer tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		table.setBounds(0, 0, 18, 81);

	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
