import java.util.*;

public class Token {
	public static final int ALPHA = 1000;
	public static final int NUM = 2000;
// returns number of tokens
	public static List<String> tokenize(String line) {
		int nxtTok;
		int tlen = line.length();
		int cursor = 0;
		int first,last;
		ArrayList<String> tokens = new ArrayList<String>();
		while(cursor<tlen-1) {
			first = cursor;
			last = getTokLast(line,first);
			if(last>=first) {
				String token = line.substring(first,last);
				tokens.add(token);
				cursor = skip(line,last+1,' ',tlen);
			}
			else return tokens;
		}
		return tokens;
	}
	
	private static int skip(String line, int cursor, char separator, int tlen){
		while(cursor<tlen && line.charAt(cursor)==separator)++cursor;
		return cursor;
	}
	
	private static int getTokLast(String line, int first) {
		int last = first+1;   // have at least one already
		int tlen = line.length();
		int type = setType(line.charAt(first));
		while(last < tlen) {
			if( isOfType(line.charAt(last),type) ) ++last;   // include this char
			else break;                                     // no, done
		} 
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
		List<String> tokens = tokenize(dothis);
		System.out.println(dothis);
		int toklen = tokens.size();
		for(int i=0; i<toklen; ++i) System.out.println(tokens.get(i));
	}
}
