/**
 * 
 */
package org.wimi.nve.example;

/**
 * @author mwirth
 *
 */
public class Main
{
	public static void main(String[] args) {
	    final Display display = new Display();
	    final Shell shell = new Shell(display);
	
	    WizardDialog wizardDialog = new WizardDialog(shell, new MyWizard());
	    wizardDialog.open();
	
	    while (!shell.isDisposed()) {
	        if (!display.readAndDispatch()) {
	            display.sleep();
	        }
	    }
	    display.dispose();
	}
}

