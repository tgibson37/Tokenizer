import java.util.*;
public class Token {
	public static final int SYM = 1000;
	public static final int NUM = 2000;
	private String line;
	private int cursor=0;
	private int len;

	public Token(String line){
		this.line = line;
		len = line.length();
	}
	public List<String> tokenize() {
		ArrayList<String> tokens = new ArrayList<String>();
		while(cursor<len-1) {
			while(isWhite())++cursor;
			String tok = getTok();
			if(tok!=null) {
				tokens.add(tok);
				cursor += tok.length();
			}
			else return tokens;
		}
		return tokens;
	}
	private boolean isWhite() {
		if(cursor>=len)return false;
		return Character.isWhitespace(line.charAt(cursor)); 
	}
	private String getTok() {
		if(cursor>=len)return null;
		int type = setType(line.charAt(cursor));
		int last = cursor+1;
		while(last < len) {
			if( isOfType(line.charAt(last),type) ) ++last;
			else break;
		} 
		return line.substring(cursor,last);
	}
	private boolean isOfType(char x, int type) {
		boolean yesno;
		switch (type) {
		case SYM: yesno = Character.isLetterOrDigit(x); break;
		case NUM:   yesno = Character.isDigit(x); break;
		case 0:
		default: yesno = false; break;
		}
		return yesno;
	}
	static int setType(char a){
		int type;
		if(Character.isLetter(a)) type = SYM;
		else if(Character.isDigit(a)) type = NUM;
		else type=0;
		return type;
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
}
