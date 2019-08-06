package project.triangulator.color;

public class ColorComposer {

	public static int compose( int red , int green , int blue ) {
		
		return ( ( red & 0xFF ) << 16 ) | ( ( green & 0xFF ) << 8 ) | ( blue & 0xFF );
		
	}
	
}
