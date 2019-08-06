package project.triangulator.color;

public class ColorDecomposer {
	
	public static int getRed( int color ) {
		
		return ( color >> 16 ) & 0xFF;
		
	}
	
	public static int getGreen( int color ) {
		
		return ( color >> 8 ) & 0xFF;
		
	}
	
	public static int getBlue( int color ) {
		
		return color & 0xFF;
		
	}
	
}
