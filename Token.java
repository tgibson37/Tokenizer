import java.util.*;
public class Token {
	boolean asfound = true;
	boolean trace   = false;
	public static final int SYM = 1000;
	public static final int NUM = 2000;
	private String line;
	private int cursor;
	private int len;
	private ArrayList<TokType> types = new ArrayList<TokType>();

	public Token(String line){
//a better way: Class.getClasses() returns array of public inner classes
		types.add(new Number());
		types.add(new Symbol());
		types.add(new Comment1());
		types.add(new Unknown());
		this.line = line;
		len = line.length();
	}
	private boolean isWhite() {
		if(cursor>=len)return false;
		return Character.isWhitespace(line.charAt(cursor));
	}
	public List<String> tokenize() {
		cursor=0;
		String tok;
		ArrayList<String> tokens = new ArrayList<String>();
		while(cursor<len-1){
			if(trace)System.err.println("~29 "+cursor);
			Iterator<TokType> typit = types.iterator();
			while(cursor<len-1 && isWhite())++cursor;
			if(cursor>=len-1){
				if(trace||asfound)System.out.println("Done");
				break;
			}
			while(typit.hasNext()){
				if(trace)System.err.println("  ~33 "+cursor);
				TokType tt = typit.next();
				if(tt.yours(line,cursor)){
					tok=tt.token(line,cursor);
					cursor += tok.length();
					tokens.add(tok);
					if(asfound)System.out.println("Found =>"+tok+"<=");
					break;
				}
			}
		}
		return tokens;
	}
	public static void main(String args[]){
		String dothis = "  foo 30  bar X17  More  /* foo bar */  vudo some  ";
//		String dothis = "  foo 30  bar X17  More  // foo bar   vudo some  ";
//		String dothis = "    // foo bar   vudo some  ";
		//               012345678901234567890123456789
		System.out.println("=>>"+dothis+"<<=");
		System.out.println("   01234567890123456789012345678901234567890123456789");
		System.out.println("             1         2         3         4");
		Token t = new Token(dothis);
		List<String> tokens = t.tokenize();
		int toklen = tokens.size();
		System.out.println(toklen+" tokens...");
		for(int i=0; i<toklen; ++i) System.out.println(tokens.get(i));
	}
	private abstract class TokType{
		abstract boolean yours(String text, int first);
		abstract String token(String text, int first);
	}
/******************** Token Types ******************/
	public class Number extends TokType{
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
	public class Symbol extends TokType{
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

	public class Comment1 extends TokType{
		boolean yours(String text, int first){
			if(first+1 >= len)return false;
			String tok = text.substring(first,first+2); 
			return tok.equals("/*");
		}
		String token(String text, int first) {
			int last=first+2;  // 2 chars matched so far
			last=search(last,"*/");
			return text.substring(first,last+2);
		}
	}
	public class Unknown extends TokType{
		boolean yours(String text, int first){ return true; }
		String token(String text, int first) {
			System.err.println("Unknown: "+text.substring(first,len-1));
			System.err.println("  cursor: "+cursor+", exiting");
			System.exit(1);
			return null;
		}
			
	}
//handy tool for inner classes
	int search(int from, String end){
		int eln = end.length();
		while(from+eln < len){
			if(end.equals(line.substring(from,from+eln)))break;
			++from;
		}
		return from+eln-1;
	}
/*
		for(int i=from; i+ln<len; ++i){
			if end.equals(line.substring(i,i+ln))break;
		}
*/
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
//System.err.print(" ~23 "+cursor+" "+line.substring(cursor,len));
