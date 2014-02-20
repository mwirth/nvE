package org.wimi.nve.editor;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;

public class HeaderRule extends PatternRule
{
	public HeaderRule(IToken token)
	{
		super("#", null, token, (char) 0, true, true);
		setColumnConstraint(0);
	}
}
