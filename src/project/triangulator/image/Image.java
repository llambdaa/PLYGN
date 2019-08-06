package project.triangulator.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import project.triangulator.filter.GreyscaleFilter;
import project.triangulator.filter.classifier.Filter;
import project.triangulator.process.Process;
import project.triangulator.process.ProcessLogger;
import project.triangulator.transform.LaplaceOutliner;
import project.triangulator.transform.SobelHorizontalOutliner;
import project.triangulator.transform.SobelVerticalOutliner;
import project.triangulator.transform.classifier.Outliner;

/**
 * Regarding the code style that can be seen here:
 * 
 * 	The code is intentionally bloated with for-loops
 *  in exchange for greater processing speed. This
 *  way, multiple million unnecessary enumeration
 *  comparisons inside the loops are avoided 
 *  which speeds up the program but, however, leads 
 *  to slightly more code.
 */
public class Image {

	BufferedImage internal;
	
	public Image( BufferedImage internal ) {
		
		this.internal = internal;
		
	}
	
	public BufferedImage getInternal() {
		
		return this.internal;
		
	}
	
	public int getWidth() {
		
		return this.internal.getWidth();
		
	}
	
	public int getHeight() {
		
		return this.internal.getHeight();
		
	}
	
	public void setRGB( int x , int y , int value ) {
		
		this.internal.setRGB( x , y , value );
		
	}
	
	public int getRGB( int x , int y ) {
		
		return this.internal.getRGB( x , y );
		
	}
	
	public Image clone() {
		
		BufferedImage clone = new BufferedImage( this.getWidth() , this.getHeight() , BufferedImage.TYPE_INT_RGB );
		
		for ( int y = 0; y < this.getHeight(); y++ ) {
			
			for ( int x = 0; x < this.getWidth(); x++ ) {
				
				clone.setRGB( x , y , this.getRGB( x , y ) );
				
			}
			
		}
		
		return new Image( clone );
		
	}
	
	public Image filter( Filter filter ) {
		
		Image result = new Image( new BufferedImage( this.getWidth() , this.getHeight() , BufferedImage.TYPE_INT_RGB ) );
		ProcessLogger.log( Process.FILTER , filter );
		
		if ( filter.equals( Filter.GREYSCALE ) ) {
			
			for ( int y = 0; y < this.getHeight(); y++ ) {
				
				for ( int x = 0; x < this.getWidth(); x++ ) {
					
					result.setRGB( x , y , GreyscaleFilter.filter( this , x ,  y ) );
					
				}
				
			}
			
		}
		
		return result;
		
	}
	
	/**
	 * This method provides metadata for the
	 * image being passed.
	 * 
	 * @return image with metadata
	 */
	public OutlinedImage outline( Outliner outliner ) {
		
		ProcessLogger.log( Process.OUTLINING , outliner );
	
		if ( outliner.equals( Outliner.SOBEL_VERTICAL ) ) {
			
			return SobelVerticalOutliner.outline( this );
			
		} else if ( outliner.equals( Outliner.SOBEL_HORIZONTAL ) ) {
			
			return SobelHorizontalOutliner.outline( this );
			
		} else if ( outliner.equals( Outliner.LAPLACE ) ) {
			
			return LaplaceOutliner.outline( this );
			
		}
		
		return null;
		
	}
	
	public static Image load( String path ) {

		ProcessLogger.log( Process.LOAD , path );
		
		try {
			
			return new Image( ImageIO.read( new File( path ) ) );
			
		} catch ( IOException exception ) {
			
			exception.printStackTrace();
			
		}
		
		return null;
		
	}
	
}
