import java.util.*;
import java.io.*;

public class Token {
	static boolean asfound = false;
	static boolean trace   = false;
	public static final int SYM = 1000;
	public static final int NUM = 2000;
	private String line;
	private int cursor;
	private int len;
	private ArrayList<TokType> types = new ArrayList<TokType>();

	public Token(String line){
		types.add(new Number());
		types.add(new Decl());
		types.add(new Symbol());
		types.add(new Comment1());
		types.add(new Relation());
		types.add(new Block());
		types.add(new Unknown());
		this.line = line;
		len = line.length();
	}
	public List<String> tokenize() {
		cursor=0;
		String tok;
		ArrayList<String> tokens = new ArrayList<String>();
		while(cursor<len-1){
			if(trace)System.err.println("~29 "+cursor);
			Iterator<TokType> typit = types.iterator();
			while(cursor<len-1 && isWhite(cursor))++cursor;
			if(cursor>=len-1){
				if(trace||asfound)System.out.println("Done");
				break;
			}
			while(typit.hasNext()){
				if(trace)System.err.println("  ~38 "+cursor);
				TokType tt = typit.next();
				if(tt.yours(line,cursor)){
					tok=tt.token(line,cursor);
					cursor += tok.length();
					tokens.add(tok);
					String type = tt.getClass().getName();
					type = type.substring(6,type.length());
					if(asfound)System.out.println(type+": Found ."+tok+".");
					break;
				}
			}
		}
		return tokens;
	}
	private static void usage(){
		System.out.println("Usage: java Token file [?ft] (help, as found, trace)");
		System.exit(0);
	}
	public static void main(String args[]){
		String dothis = "";
		if(args.length>1){
			if(args[1].indexOf('?')>=0){usage();System.exit(0);}
			if(args[1].indexOf('f')>=0)asfound=true;
			if(args[1].indexOf('t')>=0)trace=true;
		}
		if(args.length>0){
			try {
			  File myObj = new File( args[0] );
			  Scanner myReader = new Scanner(myObj);
			  while (myReader.hasNextLine()) {
				dothis += myReader.nextLine()+"\n";
			  }
			  myReader.close();
//System.out.println("~73\n"+dothis);
//System.exit(0);
			} catch (FileNotFoundException e) {
			  System.out.println("Input file not found: "+args[0]);
			  System.exit(1);
			}
		}
		else {usage();System.exit(0);}
/*  old test strings. Using testfile now.		
		String dothis = "  foo 30  bar X17  More >= /* foo bar vudo some  ";
		String dothis = "  foo 30  bar X17  More  // foo bar   vudo some  ";
		String dothis = "    // foo bar   vudo some  ";
		String dothis = "  <= >= != == > <  ";
		//               012345678901234567890123456789
		System.out.println("@"+dothis+"@");
		System.out.println(" 01234567890123456789012345678901234567890123456789");
		System.out.println("             1         2         3         4");
*/
		Token t = new Token(dothis);
		List<String> tokens = t.tokenize();
		int toklen = tokens.size();
		System.out.println(toklen+" tokens...");
		for(int i=0; i<toklen; ++i) 
				System.out.println(i +": "+tokens.get(i));
	}
// BASE CLASS for all tokens
	private abstract class TokType{
		abstract boolean yours(String text, int first);
		abstract String token(String text, int first);
	}
//HANDY TOOLS for token types
	private boolean isWhite(int where) {
		if(where>=len)return false;
		return Character.isWhitespace(line.charAt(where));
	}
	char getBalanceChar(String text, int from){
		char x = text.charAt(from);
		if(x=='['){ return ']'; }
		if(x=='('){ return ')'; }
		if(x=='{'){ return '}'; }
		if(x=='<'){ return '>'; }
		return 0;   // x is not a balanced matchable
	}
	int balancedAt(String text, int from, char bchar, char echar){
			int depth = 1;
			int last = from+1;
			while( last<=len-1 && depth>0 ){
				if(text.charAt(last)==bchar)++depth;
				if(text.charAt(last)==echar)--depth;
				++last;
			}
			if(depth==0)return last;
			return -1;
	}
	int isOf(String text, int first, String[] sa){
		for(int i=0; i<sa.length; ++i){
			if(text.startsWith(sa[i],first))return i;
		}
		return -1;
	}
	int search(int from, String end){
		int eln = end.length();
		while(from+eln < len){
			if(end.equals(line.substring(from,from+eln)))return from+eln-1;
			++from;
		}
		return -1;
	}
	int isSym(String text, int from){
		if(Character.isLetter(text.charAt(from))) {
			while(from<=len 
					&& Character.isLetterOrDigit(text.charAt(from)))++from;
		}
		return from;
	}
	int isSymList(String text, int from){
		from = isSym(text,from);
		while(from<=len){
			while(from<=len && isWhite(from))++from;
			from = isSym(text,from);
			while(isWhite(from))++from;
			if( text.charAt(from) != ',' )break;
			else ++from;
		}
		return from;
	}
/* 
System.err.print("~141 ");
System.err.print("~142 from="+from);

QUICKY STARTER...(but see Prototype, below, for more details)
	public class . extends TokType{
		boolean yours(String text, int first){
		}
		String token(String text, int first) {
		}
	}
*/
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
		int last;
		boolean yours(String text, int first){
			if(first+1 >= len)return false;
			String tok = text.substring(first,first+2); 
			if(tok.equals("/*")){
//				last = search(last+2,"*/");  // C style
//				if(last==-1)return false;    // C style
				last = search(first,"\n");   // tc
				return true;
			}
			return false;
		}
		String token(String text, int first) {
			if(last >= len)last = len-1;
//System.err.println("~175 first,last,len= "+first+" "+last+" "+len);
			return text.substring(first,last);
		}
	}
	public class Comment2 extends TokType{     // C++ one line style
		boolean yours(String text, int first){
			if(first+1 >= len)return false;
			String tok = text.substring(first,first+2); 
			return tok.equals("//");
		}
		String token(String text, int first) {
			int last=first+2;  // 2 chars matched so far
			last=search(last,"\n");
			return text.substring(first,last+1);
		}
	}

	public class Relation extends TokType{
		String[] rln = {">=", "<=", "==", "!=",">", "<"};
		int x;
		boolean yours(String text, int first){
			x=isOf(text,first,rln);
			return x>=0;
		}
		String token(String text, int first) {
			return rln[x];
		}
	}
	public class Unknown extends TokType{
		boolean yours(String text, int first){ return true; }
		String token(String text, int first) {
			System.err.print("Unknown: "+text.substring(first,len-1));
			System.err.println("  cursor: "+cursor+", exiting");
			System.exit(1);
			return null;
		}
			
	}
	public class Block extends TokType{
		int last;
		boolean yours(String text, int first){
			if( text.charAt(first)=='[' ){
				last = balancedAt(text,first, '[', ']' );
				if(last<0 ) return false;
				return true;
			}
			return false;
		}
		String token(String text, int first) {
			return text.substring(first,last);
		}
	}
	public class Decl extends TokType{
		int last;
		boolean yours(String text, int first){
			int space = len-first;
			if(space>=3 && 
				text.substring(first,last=first+3).equals("int"))return true;
			else if(space>=4 && 
				text.substring(first,last=first+4).equals("char"))return true;
			else return false;
		}
		String token(String text, int first) {
			char c;
			while(isWhite(last))++last;
			if(text.charAt(last)=='('){
				last = balancedAt(text,last,'(',')');
				if(last<0){
					System.err.println("Err: Unbalanced (\n");
					return "(";
				}
			}
			while(isWhite(last))++last;
			c = text.charAt(last);
			if(Character.isLetter(c)){
				last = isSymList(text,last);
			}
			return text.substring(first,last);
		}
	}
/*  paste here
PROTOTYPE (These are inner classes):
	public class <tokname> extends TokType{
		boolean yours(String text, int first){
			boolean yesno = <test code>;
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
//System.err.println("~163 "+text.substring(first,first+9));
