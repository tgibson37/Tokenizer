import java.util.*;

public class Token {
	public static final int ALPHA = 1000;
	public static final int NUM = 2000;
// returns number of tokens
	public static int tokenize(String line, String[] tokens, int TOKLEN) {
		int nxtTok;
		int tlen = line.length();
		int cursor = 0;
		int first,last;
		for(nxtTok=0; nxtTok<TOKLEN && cursor<tlen-1; ++nxtTok) {
			first = cursor;
			last = getTokLast(line,first);
			String token = line.substring(first,last+1);
			tokens[nxtTok] = token;
			cursor = skip(line,last+1,' ',tlen);
		}
		return nxtTok;
	}
	
	private static int skip(String line, int cursor, char separator, int tlen){
		while(cursor<tlen && line.charAt(cursor)==separator)++cursor;
		return cursor;
	}
	
	private static int getTokLast(String line, int first) {
		int last = first;
		int tlen = line.length();
		int type = setType(line.charAt(first));
		do {
			if( isOfType(line.charAt(last+1),type) ) ++last;   // include this char
			else break;                                     // no, done
		} while(last < tlen-1);   // limit test, stop before last char..
		return last;
	}

	private static boolean isOfType(char x, int type) {
		boolean yesno;
		switch (type) {
		case ALPHA: yesno = Character.isLetter(x); break;
		case NUM:   yesno = Character.isDigit(x); break;
		case 0:
		default: yesno = false; break;
		}
		return yesno;
	}
			
	static int setType(char a){
		int type;
		if(Character.isLetter(a)) type = ALPHA;
		else if(Character.isDigit(a)) type = NUM;
		else type=0;
		return type;
	}

	public static void main(String args[]){
		int TOKLEN = 10;
		String tok[] = new String[TOKLEN];
		String dothis = "foo 30 bar";
		int toklen = tokenize(dothis,tok,TOKLEN);
		System.out.println(dothis);
		for(int i=0; i<toklen; ++i) System.out.println(tok[i]);
	}
}
