package croack.util;


public class StringExtractor 
{
	
	private String text = null;
	private int position = 0;
	
	
	public StringExtractor(String text)
	{
		this.text = text;
	}
	
	
	public void moveCursor(int chars)
	{
		position += chars;
	}
	
	
	public void cursorToBegin() {
		position = 0;
	}
	
	
	public void cursorToEnd() {
		position = text.length();
	}
	
	
	public int getLength() {
		return text.length();
	}
	
	
	public String extract(String initialMark, String finalMark)
	{
		int p1 = text.indexOf(initialMark, position);
		int p2 = text.indexOf(finalMark, p1 + initialMark.length());
		
		if (p1 != -1 && p2 != -1) {
			position = p2 + finalMark.length();
			return text.substring(p1 + initialMark.length(), p2);
		}
		else {
			return null;
		}
	}
	
	public String extractTo(String finalMark)
	{
		int p2 = text.indexOf(finalMark, position);

		if (p2 != -1) {
			String s = text.substring(position, p2);
			position = p2;
			return s;
		}
		else {
			return null;
		}
	}
	
	
	public String extractToEnd()
	{
		String r = text.substring(position);
		position = text.length();
		return r;
	}
	
	
	public String extractFromBegin(String initialMark, String finalMark)
	{
		cursorToBegin();
		return extract(initialMark, finalMark);
	}
	
	
	public String extract(int initialPosition, int finalPosition)
	{
		return text.substring(initialPosition, finalPosition);
	}
	
	
	public String extract(int fromPosition)
	{
		return text.substring(fromPosition);
	}
	
	
	public boolean advanceTo(String mark)
	{
		int np = text.indexOf(mark, position);
		
		if (np != -1) {
			position = np;
			return true;
		}
		else {
			return false;
		}
	}

	
	public boolean advanceToAndSkip(String mark)
	{
		try {
			position = text.indexOf(mark, position) + mark.length();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	
	public boolean contains(String substring)
	{
		return text.contains(substring);
	}
	
	
	public boolean containsFrom(String substring)
	{
		return text.indexOf(substring, position) != -1;
	}
	
	
	
	public static String extract(String text, String initialMark, String finalMark)
	{
		StringExtractor se = new StringExtractor(text);
		return se.extract(initialMark, finalMark);
	}
	
	
	public StringExtractor create(String initialMark, String finalMark)
	{
		return new StringExtractor(extract(initialMark, finalMark));
	}
	
	
	public static StringExtractor create(String text, String initialMark, String finalMark)
	{
		StringExtractor se = new StringExtractor(text);
		return se.create(initialMark, finalMark);
	}

	
}
