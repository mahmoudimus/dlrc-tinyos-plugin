/*
 * Dlrc 2, NesC development in Eclipse.
 * Copyright (C) 2009 DLRC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Web:  http://tos-ide.ethz.ch
 * Mail: tos-ide@tik.ee.ethz.ch
 */
package tinyos.dlrc.editors;

import java.util.Arrays;

import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.TypedRegion;

/**
 * @deprecated seems not to be used anywhere? And returns wrong results anyway,
 * strings are not considered. You might be better off using {@link NesCCodeReader}
 */
@Deprecated
public class NesCHeuristicScanner implements Symbols{

    /* character constants */
    private static final char LBRACE= '{';
    private static final char RBRACE= '}';
    private static final char LPAREN= '(';
    private static final char RPAREN= ')';
    private static final char SEMICOLON= ';';
    private static final char COLON= ':';
    private static final char COMMA= ',';
    private static final char LBRACKET= '[';
    private static final char RBRACKET= ']';
    private static final char QUESTIONMARK= '?';
    private static final char EQUAL= '=';
    private static final char LANGLE= '<';
    private static final char RANGLE= '>';

    /**
     * Returned by all methods when the requested position could not be found, or if a
     * {@link BadLocationException} was thrown while scanning.
     */
    public static final int NOT_FOUND= -1;

    /**
     * Special bound parameter that means either -1 (backward scanning) or
     * <code>fDocument.getLength()</code> (forward scanning).
     */
    public static final int UNBOUND= -2;

    /* internal scan state */

    /** the most recently read character. */
    private char fChar;
    /** the most recently read position. */
    private int fPos;

    /* preset stop conditions */
    private final StopCondition fNonWSDefaultPart= new NonWhitespaceDefaultPartition();
    private final static StopCondition fNonWS= new NonWhitespace();
    private final StopCondition fNonIdent= new NonJavaIdentifierPartDefaultPartition();


    /**
     * Stops upon a non-whitespace (as defined by {@link Character#isWhitespace(char)}) character.
     */
    private static class NonWhitespace extends StopCondition {
        /*
         * @see org.eclipse.jdt.internal.ui.text.JavaHeuristicScanner.StopCondition#stop(char)
         */
        @Override
        public boolean stop(char ch, int position, boolean forward) {
            return !Character.isWhitespace(ch);
        }
    }

    /**
     * Stops upon a non-whitespace character in the default partition.
     *
     * @see NonWhitespace
     */
    private final class NonWhitespaceDefaultPartition extends NonWhitespace {
        /*
         * @see org.eclipse.jdt.internal.ui.text.JavaHeuristicScanner.StopCondition#stop(char)
         */
        @Override
        public boolean stop(char ch, int position, boolean forward) {
            return super.stop(ch, position, true) && isDefaultPartition(position);
        }

        /*
         * @see org.eclipse.jdt.internal.ui.text.JavaHeuristicScanner.StopCondition#nextPosition(int, boolean)
         */
        @Override
        public int nextPosition(int position, boolean forward) {
            ITypedRegion partition= getPartition(position);
            if (fPartition.equals(partition.getType()))
                return super.nextPosition(position, forward);

            if (forward) {
                int end= partition.getOffset() + partition.getLength();
                if (position < end)
                    return end;
            } else {
                int offset= partition.getOffset();
                if (position > offset)
                    return offset - 1;
            }
            return super.nextPosition(position, forward);
        }
    }

    /**
     * Stops upon a non-java identifier (as defined by {@link Character#isJavaIdentifierPart(char)}) character.
     */
    private static class NonJavaIdentifierPart extends StopCondition {
        /*
         * @see org.eclipse.jdt.internal.ui.text.JavaHeuristicScanner.StopCondition#stop(char)
         */
        @Override
        public boolean stop(char ch, int position, boolean forward) {
            return !Character.isJavaIdentifierPart(ch);
        }
    }

    /**
     * Stops upon a non-java identifier character in the default partition.
     *
     * @see NonJavaIdentifierPart
     */
    private final class NonJavaIdentifierPartDefaultPartition extends NonJavaIdentifierPart {
        /*
         * @see org.eclipse.jdt.internal.ui.text.JavaHeuristicScanner.StopCondition#stop(char)
         */
        @Override
        public boolean stop(char ch, int position, boolean forward) {
            return super.stop(ch, position, true) || !isDefaultPartition(position);
        }

        /*
         * @see org.eclipse.jdt.internal.ui.text.JavaHeuristicScanner.StopCondition#nextPosition(int, boolean)
         */
        @Override
        public int nextPosition(int position, boolean forward) {
            ITypedRegion partition= getPartition(position);
            if (fPartition.equals(partition.getType()))
                return super.nextPosition(position, forward);

            if (forward) {
                int end= partition.getOffset() + partition.getLength();
                if (position < end)
                    return end;
            } else {
                int offset= partition.getOffset();
                if (position > offset)
                    return offset - 1;
            }
            return super.nextPosition(position, forward);
        }
    }

    /**
     * Specifies the stop condition, upon which the <code>scanXXX</code> methods will decide whether
     * to keep scanning or not. This interface may implemented by clients.
     */
    private static abstract class StopCondition {
        /**
         * Instructs the scanner to return the current position.
         *
         * @param ch the char at the current position
         * @param position the current position
         * @param forward the iteration direction
         * @return <code>true</code> if the stop condition is met.
         */
        public abstract boolean stop(char ch, int position, boolean forward);

        /**
         * Asks the condition to return the next position to query. The default
         * is to return the next/previous position.
         *
         * @return the next position to scan
         */
        public int nextPosition(int position, boolean forward) {
            return forward ? position + 1 : position - 1;
        }
    }

    /**
     * Stops upon a character in the default partition that matches the given character list.
     */
    private final class CharacterMatch extends StopCondition {
        private final char[] fChars;

        /**
         * Creates a new instance.
         * @param ch the single character to match
         */
        public CharacterMatch(char ch) {
            this(new char[] {ch});
        }

        /**
         * Creates a new instance.
         * @param chars the chars to match.
         */
        public CharacterMatch(char[] chars) {
            Assert.isNotNull(chars);
            Assert.isTrue(chars.length > 0);
            fChars= chars;
            Arrays.sort(chars);
        }

        /*
         * @see org.eclipse.jdt.internal.ui.text.JavaHeuristicScanner.StopCondition#stop(char, int)
         */
        @Override
        public boolean stop(char ch, int position, boolean forward) {
            return Arrays.binarySearch(fChars, ch) >= 0 && isDefaultPartition(position);
        }

        /*
         * @see org.eclipse.jdt.internal.ui.text.JavaHeuristicScanner.StopCondition#nextPosition(int, boolean)
         */
        @Override
        public int nextPosition(int position, boolean forward) {
            ITypedRegion partition= getPartition(position);
            if (fPartition.equals(partition.getType()))
                return super.nextPosition(position, forward);

            if (forward) {
                int end= partition.getOffset() + partition.getLength();
                if (position < end)
                    return end;
            } else {
                int offset= partition.getOffset();
                if (position > offset)
                    return offset - 1;
            }
            return super.nextPosition(position, forward);
        }
    }


    /** The document being scanned. */
    private IDocument fDocument;
    /** The partitioning being used for scanning. */
    private String fPartitioning;
    /** The partition to scan in. */
    private String fPartition;


    /**
     * Creates a new instance.
     *
     * @param document the document to scan
     * @param partitioning the partitioning to use for scanning
     * @param partition the partition to scan in
     */
    public NesCHeuristicScanner(IDocument document, String partitioning, String partition) {
        Assert.isNotNull(document);
        Assert.isNotNull(partitioning);
        Assert.isNotNull(partition);
        fDocument= document;
        fPartitioning= partitioning;
        fPartition= partition;
    }


    public NesCHeuristicScanner(IDocument document) {
        this(document, INesCPartitions.NESC_PARTITIONING, IDocument.DEFAULT_CONTENT_TYPE);
    }


    /**
     * Returns the position of the opening peer character (backward search). Any scopes introduced by closing peers
     * are skipped. All peers accounted for must reside in the default partition.
     *
     * <p>Note that <code>start</code> must not point to the closing peer, but to the first
     * character being searched.</p>
     *
     * @param start the start position
     * @param openingPeer the opening peer character (e.g. '{')
     * @param closingPeer the closing peer character (e.g. '}')
     * @return the matching peer character position, or <code>NOT_FOUND</code>
     */
    public int findOpeningPeer(int start, char openingPeer, char closingPeer) {
        Assert.isTrue(start < fDocument.getLength());

        try {
            int depth= 1;
            start += 1;
            while (true) {
                start= scanBackward(start - 1, UNBOUND, new CharacterMatch(new char[] {openingPeer, closingPeer}));
                if (start == NOT_FOUND)
                    return NOT_FOUND;

                if (fDocument.getChar(start) == closingPeer)
                    depth++;
                else
                    depth--;

                if (depth == 0)
                    return start;
            }

        } catch (BadLocationException e) {
            return NOT_FOUND;
        }
    }

    /**
     * Checks whether <code>position</code> resides in a default (Java) partition of <code>fDocument</code>.
     *
     * @param position the position to be checked
     * @return <code>true</code> if <code>position</code> is in the default partition of <code>fDocument</code>, <code>false</code> otherwise
     */
    public boolean isDefaultPartition(int position) {
        Assert.isTrue(position >= 0);
        Assert.isTrue(position <= fDocument.getLength());

        try {
            return fPartition.equals(TextUtilities.getContentType(fDocument, fPartitioning, position, false));
        } catch (BadLocationException e) {
            return false;
        }
    }

    /**
     * Returns the partition at <code>position</code>.
     *
     * @param position the position to get the partition for
     * @return the partition at <code>position</code> or a dummy zero-length
     *         partition if accessing the document fails
     */
    private ITypedRegion getPartition(int position) {
        Assert.isTrue(position >= 0);
        Assert.isTrue(position <= fDocument.getLength());

        try {
            return TextUtilities.getPartition(fDocument, fPartitioning, position, false);
        } catch (BadLocationException e) {
            return new TypedRegion(position, 0, "__no_partition_at_all"); //$NON-NLS-1$
        }

    }

    /**
     * Finds the highest position <code>p</code> in <code>fDocument</code> such that <code>bound</code> &lt; <code>p</code> &lt;= <code>start</code>
     * and <code>condition.stop(fDocument.getChar(p), p)</code> evaluates to <code>true</code>.
     *
     * @param start the first character position in <code>fDocument</code> to be considered
     * @param bound the first position in <code>fDocument</code> to not consider any more, with <code>bound</code> &lt; <code>start</code>, or <code>UNBOUND</code>
     * @param condition the <code>StopCondition</code> to check
     * @return the highest position in (<code>bound</code>, <code>start</code> for which <code>condition</code> holds, or <code>NOT_FOUND</code> if none can be found
     */
    public int scanBackward(int start, int bound, StopCondition condition) {
        if (bound == UNBOUND)
            bound= -1;

        Assert.isTrue(bound >= -1);
        Assert.isTrue(start < fDocument.getLength() );

        try {
            fPos= start;
            while (fPos > bound) {

                fChar= fDocument.getChar(fPos);
                if (condition.stop(fChar, fPos, false))
                    return fPos;

                fPos= condition.nextPosition(fPos, false);
            }
        } catch (BadLocationException e) {
        }
        return NOT_FOUND;
    }

    /**
     * Finds the highest position in <code>fDocument</code> such that the position is &lt;= <code>position</code>
     * and &gt; <code>bound</code> and <code>fDocument.getChar(position) == ch</code> evaluates to <code>true</code> for at least one
     * ch in <code>chars</code> and the position is in the default partition.
     *
     * @param position the first character position in <code>fDocument</code> to be considered
     * @param bound the first position in <code>fDocument</code> to not consider any more, with <code>bound</code> &lt; <code>position</code>, or <code>UNBOUND</code>
     * @param ch the <code>char</code> to search for
     * @return the highest position of one element in <code>chars</code> in (<code>bound</code>, <code>position</code>] that resides in a Java partition, or <code>NOT_FOUND</code> if none can be found
     */
    public int scanBackward(int position, int bound, char ch) {
        return scanBackward(position, bound, new CharacterMatch(ch));
    }

    /**
     * Returns the most recent internal scan position.
     *
     * @return the most recent internal scan position.
     */
    public int getPosition() {
        return fPos;
    }

    /**
     * Returns one of the keyword constants or <code>TokenIDENT</code> for a scanned identifier.
     *
     * @param s a scanned identifier
     * @return one of the constants defined in {@link Symbols}
     */
    private int getToken(String s) {
        Assert.isNotNull(s);

        switch (s.length()) {
            case 2:
                if ("if".equals(s)) //$NON-NLS-1$
                    return TokenIF;
                if ("do".equals(s)) //$NON-NLS-1$
                    return TokenDO;
                break;
            case 3:
                if ("for".equals(s)) //$NON-NLS-1$
                    return TokenFOR;
                if ("try".equals(s)) //$NON-NLS-1$
                    return TokenTRY;
                if ("new".equals(s)) //$NON-NLS-1$
                    return TokenNEW;
                break;
            case 4:
                if ("case".equals(s)) //$NON-NLS-1$
                    return TokenCASE;
                if ("else".equals(s)) //$NON-NLS-1$
                    return TokenELSE;
                if ("enum".equals(s)) //$NON-NLS-1$
                    return TokenENUM;
                if ("goto".equals(s)) //$NON-NLS-1$
                    return TokenGOTO;
                break;
            case 5:
                if ("break".equals(s)) //$NON-NLS-1$
                    return TokenBREAK;
                if ("catch".equals(s)) //$NON-NLS-1$
                    return TokenCATCH;
                if ("class".equals(s)) //$NON-NLS-1$
                    return TokenCLASS;
                if ("while".equals(s)) //$NON-NLS-1$
                    return TokenWHILE;
                break;
            case 6:
                if ("return".equals(s)) //$NON-NLS-1$
                    return TokenRETURN;
                if ("static".equals(s)) //$NON-NLS-1$
                    return TokenSTATIC;
                if ("switch".equals(s)) //$NON-NLS-1$
                    return TokenSWITCH;
                break;
            case 7:
                if ("default".equals(s)) //$NON-NLS-1$
                    return TokenDEFAULT;
                if ("finally".equals(s)) //$NON-NLS-1$
                    return TokenFINALLY;
                break;
            case 9:
                if ("interface".equals(s)) //$NON-NLS-1$
                    return TokenINTERFACE;
                break;
            case 12:
                if ("synchronized".equals(s)) //$NON-NLS-1$
                    return TokenSYNCHRONIZED;
                break;
        }
        return TokenIDENT;
    }

    /**
     * Returns the next token in backward direction, starting at <code>start</code>, and not extending
     * further than <code>bound</code>. The return value is one of the constants defined in {@link Symbols}.
     * After a call, {@link #getPosition()} will return the position just before the scanned token
     * starts (i.e. the next position that will be scanned).
     *
     * @param start the first character position in the document to consider
     * @param bound the first position not to consider any more
     * @return a constant from {@link Symbols} describing the previous token
     */
    public int previousToken(int start, int bound) {
        int pos= scanBackward(start, bound, fNonWSDefaultPart);
        if (pos == NOT_FOUND)
            return TokenEOF;

        fPos--;

        switch (fChar) {
            case LBRACE:
                return TokenLBRACE;
            case RBRACE:
                return TokenRBRACE;
            case LBRACKET:
                return TokenLBRACKET;
            case RBRACKET:
                return TokenRBRACKET;
            case LPAREN:
                return TokenLPAREN;
            case RPAREN:
                return TokenRPAREN;
            case SEMICOLON:
                return TokenSEMICOLON;
            case COLON:
                return TokenCOLON;
            case COMMA:
                return TokenCOMMA;
            case QUESTIONMARK:
                return TokenQUESTIONMARK;
            case EQUAL:
                return TokenEQUAL;
            case LANGLE:
                return TokenLESSTHAN;
            case RANGLE:
                return TokenGREATERTHAN;
        }

        // else
        if (Character.isJavaIdentifierPart(fChar)) {
            // assume an ident or keyword
            int from, to= pos + 1;
            pos= scanBackward(pos - 1, bound, fNonIdent);
            if (pos == NOT_FOUND)
                from= bound == UNBOUND ? 0 : bound + 1;
            else
                from= pos + 1;

            String identOrKeyword;
            try {
                identOrKeyword= fDocument.get(from, to - from);
            } catch (BadLocationException e) {
                return TokenEOF;
            }

            return getToken(identOrKeyword);


        } else {
            // operators, number literals etc
            return TokenOTHER;
        }

    }

    /**
     * Finds the lowest position <code>p</code> in <code>fDocument</code> such that <code>start</code> &lt;= p &lt;
     * <code>bound</code> and <code>condition.stop(fDocument.getChar(p), p)</code> evaluates to <code>true</code>.
     *
     * @param start the first character position in <code>fDocument</code> to be considered
     * @param bound the first position in <code>fDocument</code> to not consider any more, with <code>bound</code> &gt; <code>start</code>, or <code>UNBOUND</code>
     * @param condition the <code>StopCondition</code> to check
     * @return the lowest position in [<code>start</code>, <code>bound</code>) for which <code>condition</code> holds, or <code>NOT_FOUND</code> if none can be found
     */
    public int scanForward(int start, int bound, StopCondition condition) {
        Assert.isTrue(start >= 0);

        if (bound == UNBOUND)
            bound= fDocument.getLength();

        Assert.isTrue(bound <= fDocument.getLength());

        try {
            fPos= start;
            while (fPos < bound) {

                fChar= fDocument.getChar(fPos);
                if (condition.stop(fChar, fPos, true))
                    return fPos;

                fPos= condition.nextPosition(fPos, true);
            }
        } catch (BadLocationException e) {
        }
        return NOT_FOUND;
    }


    /**
     * Finds the lowest position in <code>fDocument</code> such that the position is &gt;= <code>position</code>
     * and &lt; <code>bound</code> and <code>fDocument.getChar(position) == ch</code> evaluates to <code>true</code>
     * and the position is in the default partition.
     *
     * @param position the first character position in <code>fDocument</code> to be considered
     * @param bound the first position in <code>fDocument</code> to not consider any more, with <code>bound</code> &gt; <code>position</code>, or <code>UNBOUND</code>
     * @param ch the <code>char</code> to search for
     * @return the lowest position of <code>ch</code> in (<code>bound</code>, <code>position</code>] that resides in a Java partition, or <code>NOT_FOUND</code> if none can be found
     */
    public int scanForward(int position, int bound, char ch) {
        return scanForward(position, bound, new CharacterMatch(ch));
    }


    /**
     * Returns the position of the closing peer character (forward search). Any scopes introduced by opening peers
     * are skipped. All peers accounted for must reside in the default partition.
     *
     * <p>Note that <code>start</code> must not point to the opening peer, but to the first
     * character being searched.</p>
     *
     * @param start the start position
     * @param openingPeer the opening peer character (e.g. '{')
     * @param closingPeer the closing peer character (e.g. '}')
     * @return the matching peer character position, or <code>NOT_FOUND</code>
     */
    public int findClosingPeer(int start, final char openingPeer, final char closingPeer) {
        Assert.isNotNull(fDocument);
        Assert.isTrue(start >= 0);

        try {
            int depth= 1;
            start -= 1;
            while (true) {
                start= scanForward(start + 1, UNBOUND, new CharacterMatch(new char[] {openingPeer, closingPeer}));
                if (start == NOT_FOUND)
                    return NOT_FOUND;

                if (fDocument.getChar(start) == openingPeer)
                    depth++;
                else
                    depth--;

                if (depth == 0)
                    return start;
            }

        } catch (BadLocationException e) {
            return NOT_FOUND;
        }
    }


    /**
     * Finds the lowest position in <code>fDocument</code> such that the position is &gt;= <code>position</code>
     * and &lt; <code>bound</code> and <code>fDocument.getChar(position) == ch</code> evaluates to <code>true</code> for at least one
     * ch in <code>chars</code> and the position is in the default partition.
     *
     * @param position the first character position in <code>fDocument</code> to be considered
     * @param bound the first position in <code>fDocument</code> to not consider any more, with <code>bound</code> &gt; <code>position</code>, or <code>UNBOUND</code>
     * @param chars an array of <code>char</code> to search for
     * @return the lowest position of a non-whitespace character in [<code>position</code>, <code>bound</code>) that resides in a Java partition, or <code>NOT_FOUND</code> if none can be found
     */
    public int scanForward(int position, int bound, char[] chars) {
        return scanForward(position, bound, new CharacterMatch(chars));
    }

    /**
     * Finds the smallest position in <code>fDocument</code> such that the position is &gt;= <code>position</code>
     * and &lt; <code>bound</code> and <code>Character.isWhitespace(fDocument.getChar(pos))</code> evaluates to <code>false</code>.
     *
     * @param position the first character position in <code>fDocument</code> to be considered
     * @param bound the first position in <code>fDocument</code> to not consider any more, with <code>bound</code> &gt; <code>position</code>, or <code>UNBOUND</code>
     * @return the smallest position of a non-whitespace character in [<code>position</code>, <code>bound</code>), or <code>NOT_FOUND</code> if none can be found
     */
    public int findNonWhitespaceForwardInAnyPartition(int position, int bound) {
        return scanForward(position, bound, fNonWS);
    }

    /**
     * Finds the highest position in <code>fDocument</code> such that the position is &lt;= <code>position</code>
     * and &gt; <code>bound</code> and <code>fDocument.getChar(position) == ch</code> evaluates to <code>true</code> for at least one
     * ch in <code>chars</code> and the position is in the default partition.
     *
     * @param position the first character position in <code>fDocument</code> to be considered
     * @param bound the first position in <code>fDocument</code> to not consider any more, with <code>bound</code> &lt; <code>position</code>, or <code>UNBOUND</code>
     * @param chars an array of <code>char</code> to search for
     * @return the highest position of one element in <code>chars</code> in (<code>bound</code>, <code>position</code>] that resides in a Java partition, or <code>NOT_FOUND</code> if none can be found
     */
    public int scanBackward(int position, int bound, char[] chars) {
        return scanBackward(position, bound, new CharacterMatch(chars));
    }
}
