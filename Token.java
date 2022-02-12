import java.util.*;

public class Token {
	public static final int ALPHA = 1000;
	public static final int NUM = 2000;
	private static int cursor=0;

	public static List<String> tokenize(String line) {
		int nxtTok;
		int tlen = line.length();
		ArrayList<String> tokens = new ArrayList<String>();
		while(cursor<tlen-1) {
			String tok = getTok(line,cursor);
			if(tok!=null) {
				tokens.add(tok);
				cursor += tok.length();
			}
			else return tokens;
		}
		return tokens;
	}
	
	private static boolean isWhite(String line, int cursor) {
		return Character.isWhitespace(line.charAt(cursor)); 
	}
	
	private static String getTok(String line, int first) {
		while(isWhite(line,first)){++first;++cursor;}
		int type = setType(line.charAt(first));
		int last = first+1;
		int tlen = line.length();
		while(last < tlen) {
			if( isOfType(line.charAt(last),type) ) ++last;
			else break;
		} 
		return line.substring(first,last);
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
		String dothis = "  foo 30 bar X17 ";
		List<String> tokens = tokenize(dothis);
		System.out.println(dothis);
		int toklen = tokens.size();
		for(int i=0; i<toklen; ++i) System.out.println(tokens.get(i));
	}
}
//System.out.println("~16 cursor,toklen = "+cursor+" "+tok.length() );
