package project.triangulator.color;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import project.triangulator.math.Triangle;
import project.triangulator.math.Vector2D;

public class UnclusteredPalettePicker {

	public static List< Integer > pick( BufferedImage image , Triangle triangle ) {
		
		/**
		 * This map correlates a specific color to the amount
		 * of occurrences in the given triangular restriction area.
		 * Technically speaking, it is a color histogram.
		 */
		Map< Integer , Integer > palette = new HashMap<>();
		
		/**
		 * The scan area of the image has to be
		 * cropped so that it only contains the
		 * most relevant pixels.
		 */
		int minX = Math.max( ( int ) Math.floor( Math.min( triangle.getA().getX() , Math.min( triangle.getB().getX() , triangle.getC().getX() ) ) ) , 0 );
		int maxX = Math.min( ( int ) Math.ceil( Math.max( triangle.getA().getX() , Math.max( triangle.getB().getX() , triangle.getC().getX() ) ) ) , image.getWidth() - 1 );
		
		int minY = Math.max( ( int ) Math.floor( Math.min( triangle.getA().getY() , Math.min( triangle.getB().getY() , triangle.getC().getY() ) ) ) , 0 );
		int maxY = Math.min( ( int ) Math.ceil( Math.max( triangle.getA().getY() , Math.max( triangle.getB().getY() , triangle.getC().getY() ) ) ) , image.getHeight() - 1 );
		
		for ( int y = minY; y <= maxY; y++ ) {
			
			for ( int x = minX; x <= maxX; x++ ) {
				
				/**
				 * The restricted area is rectangular rather
				 * than triangular. Therefore it contains 
				 * some unnecessary image information, which
				 * have to be filtered.
				 */
				if ( triangle.contains( new Vector2D( x , y ) ) ) {
					
					int color = image.getRGB( x , y );
					
					/**
					 * The color gets inserted into the histogram by either
					 * putting the color into it mapped with an initial
					 * value or by incrementing the existing counter.
					 */
					palette.put( color , palette.containsKey( color ) ? palette.get( color ) + 1 : 1 );
					
				}
				
			}
			
		}
		
		/**
		 * When the algorithm converges, the histogram has to be sorted
		 * in descending order.
		 */
		List< Map.Entry< Integer , Integer > > sorted = new ArrayList<>( palette.entrySet() );
		sorted.sort( ( a , b ) -> Integer.compare( b.getValue() , a.getValue() ) );
		
		/**
		 * After sorting the histogram, the actual amount of occurrences doesn't
		 * matter any longer. Only the ranking of the most common colors is important, so
		 * the histogram must be translated into a one-dimensional collection.
		 */
		return sorted.stream().map( Map.Entry::getKey ).collect( Collectors.toList() );
		
	}
	
}
