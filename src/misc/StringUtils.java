package misc;

public class StringUtils {

	static public String removeAccents(String string) {
	    char[] charsData = new char[string.length()];
	    string.getChars(0, charsData.length, charsData, 0);
	 
	    char c;
	    for (int i = 0; i < charsData.length; i++) {
	        if ((c = charsData[i]) >= 'A' && c <= 'Z') {
	            charsData[i] = (char) (c - 'A' + 'a');
	        } else {
	            switch (c) {
	            case '\u00e0':
	            case '\u00e2':
	            case '\u00e4':
	                charsData[i] = 'a';
	                break;
	            case '\u00e7':
	                charsData[i] = 'c';
	                break;
	            case '\u00e8':
	            case '\u00e9':
	            case '\u00ea':
	            case '\u00eb':
	                charsData[i] = 'e';
	                break;
	            case '\u00ee':
	            case '\u00ef':
	                charsData[i] = 'i';
	                break;
	            case '\u00f4':
	            case '\u00f6':
	                charsData[i] = 'o';
	                break;
	            case '\u00f9':
	            case '\u00fb':
	            case '\u00fc':
	                charsData[i] = 'u';
	                break;
	            }
	        }
	    }
	 
	    return new String(charsData);
	}}
