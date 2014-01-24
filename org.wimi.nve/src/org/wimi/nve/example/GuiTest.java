/**
 *
 */
package org.wimi.nve.example;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
	private Text box;
	private Table table;
	private Text text;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GuiTest(Composite parent, int style)
	{
		super(parent, style);
		setLayout(new GridLayout(1, false));

		box = new Text(this, SWT.BORDER);
		box.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		box.setBounds(0, 0, 64, 19);

		SashForm sashForm = new SashForm(this, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewer tableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setBounds(0, 0, 18, 81);

		text = new Text(sashForm, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		sashForm.setWeights(new int[] { 1, 1 });

	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
