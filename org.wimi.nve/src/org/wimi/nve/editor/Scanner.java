/**
 * Copyright winterwell Mathematics Ltd.
 * @author Daniel Winterstein 13 Jan 2007
 */
package org.wimi.nve.editor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.wimi.nve.Activator;
import org.wimi.nve.util.ColorStore;

public class Scanner extends RuleBasedScanner
{
	ColorStore colorTable;

	public Scanner(ColorStore ct)
	{
		this.colorTable = ct;
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

		Token bold = new Token(new TextAttribute(ct.getColor(new RGB(0, 0, 0)), null, SWT.BOLD));
		Token headings = new Token(new TextAttribute(ct.getColor(new RGB(255, 0, 0)), null, SWT.BOLD));
		Token comment = new Token(new TextAttribute(ct.getColor(new RGB(0, 255, 0))));
		Token highlight = new Token(new TextAttribute(ct.getColor(new RGB(0, 0, 255)), null, SWT.ITALIC));
		Token list = new Token(new TextAttribute(ct.getColor(new RGB(128, 128, 0)), null, SWT.BOLD));
		Token link = new Token(new TextAttribute(ct.getColor(new RGB(0, 128, 128)), null, TextAttribute.UNDERLINE));
		Token code = new Token(new TextAttribute(ct.getColor(new RGB(0, 0, 0))));

		IRule[] rules =
			new IRule[] { new LinkRule(link), new HeaderRule(headings), new ListRule(list), new HighlightRule("_", highlight),
				new HighlightRule("***", highlight), new HighlightRule("**", bold), new HighlightRule("*", highlight),
				new HighlightRule("``", code), new HighlightRule("`", code), new MultiLineRule("<!--", "-->", comment), };

		setRules(rules);
	}
}
