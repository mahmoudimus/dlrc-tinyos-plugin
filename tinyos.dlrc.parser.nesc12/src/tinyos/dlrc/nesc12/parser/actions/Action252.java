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
package tinyos.dlrc.nesc12.parser.actions;
import tinyos.dlrc.nesc12.lexer.Token;
import tinyos.dlrc.nesc12.lexer.Lexer;
import tinyos.dlrc.nesc12.parser.ast.*;
import tinyos.dlrc.nesc12.parser.ast.nodes.*;
import tinyos.dlrc.nesc12.parser.ast.nodes.declaration.*;
import tinyos.dlrc.nesc12.parser.ast.nodes.definition.*;
import tinyos.dlrc.nesc12.parser.ast.nodes.error.*;
import tinyos.dlrc.nesc12.parser.ast.nodes.expression.*;
import tinyos.dlrc.nesc12.parser.ast.nodes.general.*;
import tinyos.dlrc.nesc12.parser.ast.nodes.nesc.*;
import tinyos.dlrc.nesc12.parser.ast.nodes.statement.*;
import tinyos.dlrc.nesc12.parser.*;
public final class Action252 implements ParserAction{
	public final java_cup.runtime.Symbol do_action(
		int                        CUP$parser$act_num,
		java_cup.runtime.lr_parser CUP$parser$parser,
		java.util.Stack            CUP$parser$stack,
		int                        CUP$parser$top,
		parser                     parser)
		throws java.lang.Exception{
	java_cup.runtime.Symbol CUP$parser$result;
 // n_attribute_initializer_list ::= c_round_open error c_round_close 
            {
              InitializerList RESULT =null;
		int roleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		Token ro = (Token)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rcright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
		Token rc = (Token)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new InitializerList();
			RESULT.setRanges( roleft, rcright );
			RESULT.addError( parser.errorNode( "attribute initializer", "expression list", ro.getRight(), rc.getLeft() ) ); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("n_attribute_initializer_list",150, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

	}
}