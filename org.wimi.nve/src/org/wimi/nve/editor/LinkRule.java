package org.wimi.nve.editor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class LinkRule implements IRule
{
	private static char[][] delimiters = null;
	protected IToken token;

	public LinkRule(IToken token)
	{
		Assert.isNotNull(token);
		this.token = token;
	}

	public IToken getSuccessToken()
	{
		return token;
	}

	// see org.eclipse.jface.text.rules.PatternRule
	protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed)
	{
		for (int i = 1; i < sequence.length; i++)
		{
			int c = scanner.read();
			if (c == ICharacterScanner.EOF && eofAllowed)
			{
				return true;
			}
			else if (c != sequence[i])
			{
				scanner.unread();
				for (int j = i - 1; j > 0; j--)
					scanner.unread();
				return false;
			}
		}
		return true;
	}

	public IToken evaluate(ICharacterScanner scanner)
	{
		int c;
		if (( c = scanner.read() ) != '[')
		{
			if (( c != 'h' || ( !sequenceDetected(scanner, "http://".toCharArray(), false) && !sequenceDetected(scanner,
				"https://".toCharArray(), false) ) ) && ( c != 'f' || !sequenceDetected(scanner, "ftp://".toCharArray(), false) ))
			{
				scanner.unread();
				return Token.UNDEFINED;
			}

			if (delimiters == null)
			{
				scanner.unread();
				return Token.UNDEFINED;
			}

			while (( c = scanner.read() ) != ICharacterScanner.EOF && !Character.isWhitespace(c))
			{
				for (int i = 0; i < delimiters.length; i++)
				{
					if (c == delimiters[i][0] && sequenceDetected(scanner, delimiters[i], true))
					{
						return token;
					}
				}
			}
			return token;
		}
		if (delimiters == null)
		{
			delimiters = scanner.getLegalLineDelimiters();
		}
		int readCount = 1;

		boolean sequenceFound = false;
		int delimiterFound = 0;
		while (( c = scanner.read() ) != ICharacterScanner.EOF && delimiterFound < 2)
		{
			readCount++;
			if (!sequenceFound && c == ']')
			{
				c = scanner.read();
				if (c == '(')
				{
					readCount++;
					sequenceFound = true;
				}
				else
				{
					scanner.unread();
				}
			}
			else if (c == ')')
			{
				return token;
			}

			int i;
			for (i = 0; i < delimiters.length; i++)
			{
				if (c == delimiters[i][0] && sequenceDetected(scanner, delimiters[i], true))
				{
					delimiterFound++;
					break;
				}
			}
			if (i == delimiters.length)
				delimiterFound = 0;
		}

		for (; readCount > 0; readCount--)
			scanner.unread();
		return Token.UNDEFINED;
	}

}
