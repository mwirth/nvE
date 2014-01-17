package org.wimi.nve.example;

import org.eclipse.jface.wizard.Wizard;

public class MyWizard extends Wizard
{
	protected MyWizardPage one;

	public MyWizard()
	{
		super();
	}

	@Override
	public void addPages()
	{
		one = new MyWizardPage("Page One");
		addPage(one);
	}

	@Override
	public boolean performFinish()
	{
		return false;
	}

}
