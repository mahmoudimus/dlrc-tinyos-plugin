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
package tinyos.dlrc.nesc12.collector.actions;
import tinyos.dlrc.nesc12.parser.StringRepository;
import tinyos.dlrc.nesc12.parser.ScopeStack;
import tinyos.dlrc.nesc12.parser.RawParser;
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
import tinyos.dlrc.nesc12.collector.*;
public final class Action311 implements ParserAction{
	public final java_cup.runtime.Symbol do_action(
		int                        CUP$parser$act_num,
		java_cup.runtime.lr_parser CUP$parser$parser,
		java.util.Stack            CUP$parser$stack,
		int                        CUP$parser$top,
		parser                     parser)
		throws java.lang.Exception{
	java_cup.runtime.Symbol CUP$parser$result;
 // n_template_parameter ::= c_declaration_specifiers 
            {
              TemplateParameter RESULT =null;
		DeclarationSpecifierList s = (DeclarationSpecifierList)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
		 RESULT = new TemplateParameter( s, null ); 
              CUP$parser$result = parser.getSymbolFactory().newSymbol("n_template_parameter",161, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
            }
          return CUP$parser$result;

	}
}