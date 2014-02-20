package org.wimi.nve.editor;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class HighlightRule implements IRule
{
	private static char[][] delimiters = null;
	private char[] sequence;
	protected IToken token;

	public HighlightRule(String marker, IToken token)
	{
		// should be "*", "_", "**" or "***"
		sequence = marker.toCharArray();
		this.token = token;
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
				// Non-matching character detected, rewind the scanner back to
				// the start.
				// Do not unread the first character.
				for (int j = i; j > 0; j--)
					scanner.unread();
				return false;
			}
		}
		return true;
	}

	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 * @since 2.0
	 */
	public IToken evaluate(ICharacterScanner scanner)
	{
		// Should be connected only on the right side
		scanner.unread();
		boolean sawSpaceBefore = Character.isWhitespace(scanner.read());
		if (!sawSpaceBefore && scanner.getColumn() != 0)
		{
			return Token.UNDEFINED;
		}

		int c = scanner.read();

		// Should be connected only on right side
		if (c != sequence[0] || !sequenceDetected(scanner, sequence, false))
		{
			scanner.unread();
			return Token.UNDEFINED;
		}
		int readCount = sequence.length;
		if (delimiters == null)
		{
			delimiters = scanner.getLegalLineDelimiters();
		}

		// Start sequence detected
		int delimiterFound = 0;

		// Is it a list item marker, or just a floating *?
		if (sawSpaceBefore)
		{
			boolean after = Character.isWhitespace(scanner.read());
			scanner.unread();
			if (after)
				delimiterFound = 2;
		}

		while (delimiterFound < 2 && ( c = scanner.read() ) != ICharacterScanner.EOF)
		{
			readCount++;

			if (!sawSpaceBefore && c == sequence[0] && sequenceDetected(scanner, sequence, false))
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
			sawSpaceBefore = Character.isWhitespace(c);
		}

		// Reached ICharacterScanner.EOF
		for (; readCount > 0; readCount--)
			scanner.unread();

		return Token.UNDEFINED;
	}

}
