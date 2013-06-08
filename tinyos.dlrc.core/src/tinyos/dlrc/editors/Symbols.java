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

/**
 * Symbols for the heuristic nesc scanner.
 *
 * @since 3.0
 */
public interface Symbols {
	int TokenEOF= -1;
	int TokenLBRACE= 1;
	int TokenRBRACE= 2;
	int TokenLBRACKET= 3;
	int TokenRBRACKET= 4;
	int TokenLPAREN= 5;
	int TokenRPAREN= 6;
	int TokenSEMICOLON= 7;
	int TokenOTHER= 8;
	int TokenCOLON= 9;
	int TokenQUESTIONMARK= 10;
	int TokenCOMMA= 11;
	int TokenEQUAL= 12;
	int TokenLESSTHAN= 13;
	int TokenGREATERTHAN= 14;
	int TokenIF= 109;
	int TokenDO= 1010;
	int TokenFOR= 1011;
	int TokenTRY= 1012;
	int TokenCASE= 1013;
	int TokenELSE= 1014;
	int TokenBREAK= 1015;
	int TokenCATCH= 1016;
	int TokenWHILE= 1017;
	int TokenRETURN= 1018;
	int TokenSTATIC= 1019;
	int TokenSWITCH= 1020;
	int TokenFINALLY= 1021;
	int TokenSYNCHRONIZED= 1022;
	int TokenGOTO= 1023;
	int TokenDEFAULT= 1024;
	int TokenNEW= 1025;
	int TokenCLASS= 1026;
	int TokenINTERFACE= 1027;
	int TokenENUM= 1028;
	int TokenIDENT= 2000;
}
