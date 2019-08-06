package project.triangulator.filter;

import project.triangulator.color.ColorComposer;
import project.triangulator.color.ColorDecomposer;
import project.triangulator.image.Image;

public class GreyscaleFilter {

	public static int filter( Image image , int x , int y ) {

		/**
		 * In order to make a greyscale image,
		 * the color channel values must be
		 * averaged.
		 */
		int value = image.getRGB( x , y );
		int average = ( ColorDecomposer.getRed( value ) +
						ColorDecomposer.getGreen( value ) +
						ColorDecomposer.getBlue( value ) ) / 3;

		return ColorComposer.compose( average , average , average );

	}

}
