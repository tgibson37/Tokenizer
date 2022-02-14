import java.util.*;
public class Token {
	public static final int SYM = 1000;
	public static final int NUM = 2000;
	private String line;
	private int cursor;
	private int len;
	private ArrayList<TokType> types = new ArrayList<TokType>();

	public Token(String line){
//find a better way
		types.add(new Number());
		types.add(new Symbol());
		this.line = line;
		len = line.length();
	}
	public List<String> tokenize() {
		cursor=0;
		String tok;
		ArrayList<String> tokens = new ArrayList<String>();
		while(cursor<len-1){
			Iterator<TokType> typit = types.iterator();
			while(cursor<len-1 && isWhite())++cursor;
			while(typit.hasNext()){
				TokType tt = typit.next();
				if(tt.yours(line,cursor)){
					tok=tt.token(line,cursor);
					cursor += tok.length();
					tokens.add(tok);
				}
			}
		}
		return tokens;
	}
	private boolean isWhite() {
		if(cursor>=len)return false;
		return Character.isWhitespace(line.charAt(cursor)); 
	}
	public static void main(String args[]){
		String dothis = "  foo 30    bar X17 \n More   \n   ";
		System.out.println("=>>"+dothis+"<<=");
		Token t = new Token(dothis);
		List<String> tokens = t.tokenize();
		int toklen = tokens.size();
		System.out.println("toklen = "+toklen);
		for(int i=0; i<toklen; ++i) System.out.println(tokens.get(i));
	}
	
	private abstract class TokType{
		abstract boolean yours(String text, int first);
		abstract String token(String text, int first);
	}
// Token Types
	private class Number extends TokType{
		boolean yours(String text, int first){
			char x = text.charAt(first);
			return Character.isDigit(x);
		}
		String token(String text, int first) {
			int last=first+1;
			while(Character.isDigit(text.charAt(last))){
				++last;
			}
			return text.substring(first,last);
		}
	}
	private class Symbol extends TokType{
		boolean yours(String text, int first){
			char x = text.charAt(first);
			return Character.isLetter(x);
		}
		String token(String text, int first) {
			int last=first+1;
			while(Character.isLetterOrDigit(text.charAt(last))){
				++last;
			}
			return text.substring(first,last);
		}
	}
/*
PROTOTYPE (These are inner classes):
	private class <tokname> extends TokType{
		boolean yours(String text, int first){
			boolean yesno = <test>;
			return yesno;
		}
		String token(String text, int first) {
			int last=first+1;
			while( <test charAt(last) is part of token> ){
				++last;
			}
			return text.substring(first,last);
		}
	}
AND: visit line 12 and add to types list. Order may be important.
*/
}
