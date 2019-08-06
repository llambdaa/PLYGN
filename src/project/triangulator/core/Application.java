package project.triangulator.core;

import project.triangulator.export.classifier.ExportFormat;
import project.triangulator.image.Image;
import project.triangulator.transform.classifier.Colorizer;
import project.triangulator.transform.classifier.Outliner;
import project.triangulator.transform.classifier.Triangulator;

public class Application {

	/**
	 * Manual: 
	 * 
	 * VERTEX_MINIMUM_DISTANCE: 	  -vmd | -vertexMinimumDistance 	= <value>
	 * VERTEX_MAXIMUM_COUNT: 	   vmc | -vertexMaximumCount		= <value>
	 * OUTLINE_THRESHOLD: 		   -ot | -outlineThreshold 		= <value>
	 * OUTLINER: 			    -o | -outliner 	 	  	= <enumeration|name>
	 * TRIANGULATOR:	   	    -t | -triangulator 	  		= <enumeration|name>
	 * COLORIZER: 		   	    -c | -colorizer    	  		= <enumeration|name>
	 * EXPORT_FORMAT   		   -ef | -exportFormat	  		= <enumeration|name>
	 * IMPORT_PATH: 		   -ip | -importPath = <value>
	 * EXPORT_PATH: 		   -ep | -exportPath = <value>
	 */
	
	public static int VERTEX_MINIMUM_DISTANCE = 0;
	public static int    VERTEX_MAXIMUM_COUNT = 0;
	public static int 	OUTLINE_THRESHOLD = 0;
	private static Outliner		     OUTLINER;
	private static Triangulator 	 TRIANGULATOR;
	private static Colorizer	    COLORIZER;
	private static ExportFormat	EXPORT_FORMAT;
	private static String 		  IMPORT_PATH;
	private static String 		  EXPORT_PATH;
	
	public static void main( String ... arguments ) {
		
		for ( String argument : arguments ) {
			
			String[] split = argument.split( "=" );
			String key = split[ 0 ].trim();
			String value = split[ 1 ].trim();
			
			if ( key.matches( "-vertexMinimumDistance|-vmd" ) ) {

				if ( value.matches( "^\\d+$" ) ) {
					
					Application.VERTEX_MINIMUM_DISTANCE = Math.max( Integer.parseInt( value ) , 0 );
					
				}
				
			}  else if ( key.matches( "-vertexMaximumCount|-vmc" ) ) {

				if ( value.matches( "^\\d+$" ) ) {

					Application.VERTEX_MAXIMUM_COUNT = Math.max( Integer.parseInt( value ) , 0 );

				}

			} else if ( key.matches( "-outlineThreshold|-ot" ) ) {
				
				if ( value.matches( "^\\d+$" ) ) {
					
					Application.OUTLINE_THRESHOLD = Math.max( Math.min( Integer.parseInt( value ) , 255 ) , 0 );
					
				}
				
			} else if ( key.matches( "-outliner|-o" ) ) {

				try {

					Application.OUTLINER = value.matches( "^\\d+$" ) ? Outliner.values()[ Integer.parseInt( value ) ] : Outliner.valueOf( value );

				} catch ( IndexOutOfBoundsException | IllegalArgumentException exception ) {

					Application.OUTLINER = Outliner.SOBEL_VERTICAL;

				}
				
			} else if ( key.matches( "-triangulator|-t" ) ) {

				try {

					Application.TRIANGULATOR = value.matches( "^\\d+$" ) ? Triangulator.values()[ Integer.parseInt( value ) ] : Triangulator.valueOf( value );

				} catch ( IndexOutOfBoundsException | IllegalArgumentException exception ) {

					Application.TRIANGULATOR = Triangulator.DELAUNAY;

				}
				
			} else if ( key.matches( "-colorizer|-c" ) ) {

				try {

					Application.COLORIZER = value.matches( "^\\d+$" ) ? Colorizer.values()[ Integer.parseInt( value ) ] : Colorizer.valueOf( value );

				} catch ( IndexOutOfBoundsException | IllegalArgumentException exception ) {

					Application.COLORIZER = Colorizer.CLUSTERED;

				}
				
			} else if ( key.matches( "-exportFormat|-ef" ) )  {

				try {

					Application.EXPORT_FORMAT = value.matches( "^\\d+$" ) ? ExportFormat.values()[ Integer.parseInt( value ) ] : ExportFormat.valueOf( value );

				} catch ( IndexOutOfBoundsException | IllegalArgumentException exception ) {

					Application.EXPORT_FORMAT = ExportFormat.SVG;

				}

			} else if ( key.matches( "-importPath|-ip" ) ) {
				
				Application.IMPORT_PATH = value;
				
			} else if ( key.matches( "-exportPath|-ep" ) ) {
				
				Application.EXPORT_PATH = value;
				
			}
			
		}
		
		System.out.println( "Program arguments: \n-vmd=" + Application.VERTEX_MINIMUM_DISTANCE +
							"\n-vmc=" + Application.VERTEX_MAXIMUM_COUNT +
							"\n -ot=" + Application.OUTLINE_THRESHOLD +
							"\n  -o=" + Application.OUTLINER +
							"\n  -t=" + Application.TRIANGULATOR +
							"\n  -c=" + Application.COLORIZER +
							"\n -et=" + Application.EXPORT_FORMAT +
							"\n -ip=" + Application.IMPORT_PATH +
							"\n -ep=" + Application.EXPORT_PATH );
		
		if ( Application.IMPORT_PATH != null ) {
			
			if ( Application.EXPORT_PATH != null ) {
				
				Image image = Image.load( Application.IMPORT_PATH );
				image.outline( Application.OUTLINER )
				     .triangulate( Application.TRIANGULATOR )
				     .colorize( Application.COLORIZER )
				     .export( Application.EXPORT_FORMAT , Application.EXPORT_PATH );
				
			} else {
				
				System.out.println( "ERROR: Due to a missing export path the result couldn't be exported. \nThe program will terminate." );
				
			}
			
		} else {
			
			System.out.println( "ERROR: Due to a missing import path no file could be loaded. \nThe program will terminate." );
			
		}
		
	}

}
