
// File:   MH_Lexer.java

// Java template file for lexer component of Informatics 2A Assignment 1.
// Concerns lexical classes and lexer for the language MH (`Micro-Haskell').


import java.io.* ;

class MH_Lexer extends GenLexer implements LEX_TOKEN_STREAM {

static class VarAcceptor extends Acceptor implements DFA {

    public String lexClass() {return "VAR" ;} ;
    public int numberOfStates() {return 3 ;} ;

    int next (int state, char c) {
      switch (state) {
        case 0: if (CharTypes.isSmall(c)) return 1; else return 2;
        case 1: if (CharTypes.isSmall(c) || CharTypes.isLarge(c) || CharTypes.isDigit(c) || c == ('\'')) {
                  return 1;
                }
                else {
                  return 2;
                }
      default: return 2 ;
      }
    }

    boolean accepting (int state) {return (state == 1) ;}
    int dead () {return 2 ;}

}

static class NumAcceptor extends Acceptor implements DFA {
    public String lexClass() {return "NUM" ;} ;
    public int numberOfStates() {return 3 ;} ;

    int next (int state, char c) {
      switch (state) {
        case 0: if (c == '0') return 1;
                else if ('1' <= c && c <= '9') return 1;
                else return 2;
        case 1: if (CharTypes.isDigit(c)) return 1; else return 2;
      default: return 2;
      }
    }

    boolean accepting (int state) {return (state == 1) ;}
    int dead () {return 2 ;}
}

static class BooleanAcceptor extends Acceptor implements DFA {
    public String lexClass() {return "BOOLEAN" ;} ;
    public int numberOfStates() {return 9 ;} ;

    int next (int state, char c) {
      switch (state) {
        case 0: if (c == 'T') {
                  return 1;
                }
                else if (c == 'F') {
                  return 5;
                }
                else {
                  return 8;
                }
        case 1: if (c == 'r') return 2; else return 8;
        case 2: if (c == 'u') return 3; else return 8;
        case 3: if (c == 'e') return 4; else return 8;
        case 4: return 8;
        case 5: if (c == 'a') return 6; else return 8;
        case 6: if (c == 'l') return 7; else return 8;
        case 7: if (c == 's') return 3; else return 8;

      default: return 8;
      }
    }

    boolean accepting (int state) {return (state == 4) ;}
    int dead () {return 8 ;}
}

static class SymAcceptor extends Acceptor implements DFA {
    public String lexClass() {return "SYM" ;} ;
    public int numberOfStates() {return 3 ;} ;

    int next (int state, char c) {
      switch (state) {
        case 0: if (CharTypes.isSymbolic(c)) return 1; else return 2;
        case 1: if (CharTypes.isSymbolic(c)) return 1; else return 2;
      default: return 2;
      }
    }

    boolean accepting (int state) {return (state == 1) ;}
    int dead () {return 2 ;}
}

static class WhitespaceAcceptor extends Acceptor implements DFA {
    public String lexClass() {return "" ;} ;
    public int numberOfStates() {return 3 ;} ;

    int next (int state, char c) {
      switch (state) {
        case 0: if (CharTypes.isWhitespace(c)) return 1; else return 2;
        case 1: if (CharTypes.isWhitespace(c)) return 1; else return 2;
      default: return 2;
      }
    }

    boolean accepting (int state) {return (state == 1) ;}
    int dead () {return 2 ;}
}

static class CommentAcceptor extends Acceptor implements DFA {
    public String lexClass() {return "" ;} ;
    public int numberOfStates() {return 4 ;} ;

    int next (int state, char c) {
      switch (state) {
        case 0: if (c == '-') return 1; else return 3;
        case 1: if (c == '-') return 2; else return 3;
        case 2: if (c == '-') {
                  return 2;
                }
                else if (!CharTypes.isSymbolic(c) && !CharTypes.isNewline(c)) {
                  return 2;
                }
                else if (!CharTypes.isNewline(c)) {
                  return 2;
                }
                else {
                  return 3;
                }
      default: return 3;
      }
    }

    boolean accepting (int state) {return (state == 2) ;}
    int dead () {return 3 ;}
}

static class TokAcceptor extends Acceptor implements DFA {

    String tok ;
    int tokLen ;
    private int garbageState;
    private char[] charArray ;

    TokAcceptor (String tok) {this.tok = tok ; tokLen = tok.length() ; this.garbageState = tokLen + 1 ; this.charArray = tok.toCharArray() ;}

    public String lexClass() {return tok ;} ;
    public int numberOfStates() {return tokLen + 2 ;} ;

    int next (int state, char c) {
          if (charArray.length > state && c == charArray[state]) {
            return state + 1;
          }
          else {
            return garbageState ;
          }
    }


    boolean accepting (int state) {return (state == tokLen) ;}
    int dead () {return garbageState ;}
}


    static DFA varAcc = new VarAcceptor() ;
    static DFA numAcc = new NumAcceptor() ;
    static DFA booleanAcc = new BooleanAcceptor() ;
    static DFA symAcc = new SymAcceptor() ;
    static DFA whitespaceAcc = new WhitespaceAcceptor() ;
    static DFA commentAcc = new CommentAcceptor() ;
    static DFA intAcc = new TokAcceptor("Integer") ;
    static DFA boolAcc = new TokAcceptor("Bool") ;
    static DFA ifAcc = new TokAcceptor("if") ;
    static DFA thenAcc = new TokAcceptor("then") ;
    static DFA elseAcc = new TokAcceptor("else") ;
    static DFA l_par_Acc = new TokAcceptor("(") ;
    static DFA r_par_Acc = new TokAcceptor(")") ;
    static DFA semiAcc = new TokAcceptor(";") ;

    static DFA[] MH_acceptors =
  new DFA[] {intAcc, boolAcc, ifAcc, thenAcc, elseAcc, l_par_Acc, r_par_Acc, semiAcc, whitespaceAcc, commentAcc, booleanAcc, varAcc, numAcc, symAcc} ;

    MH_Lexer (Reader reader) {
	super(reader,MH_acceptors) ;
    }
}
