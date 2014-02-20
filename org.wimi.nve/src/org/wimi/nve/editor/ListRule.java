package org.wimi.nve.editor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class ListRule implements IRule
{
	protected IToken token;

	public ListRule(IToken token)
	{
		Assert.isNotNull(token);
		this.token = token;
	}

	public IToken evaluate(ICharacterScanner scanner)
	{
		if (scanner.getColumn() != 0)
		{
			return Token.UNDEFINED;
		}

		int count = 0;
		int c;
		while (( c = scanner.read() ) != ICharacterScanner.EOF)
		{
			count++;
			if (!Character.isWhitespace(c))
			{
				int nextChar = scanner.read();
				scanner.unread();
				if (( c == '*' || c == '+' || c == '-' ) && Character.isWhitespace(nextChar))
				{
					return token;
				}
				else
				{
					for (; count > 0; count--)
					{
						scanner.unread();
					}
					return Token.UNDEFINED;
				}
			}
		}

		for (; count > 0; count--)
		{
			scanner.unread();
		}

		return Token.UNDEFINED;
	}
}
