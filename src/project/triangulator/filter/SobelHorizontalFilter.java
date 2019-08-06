package project.triangulator.filter;

import project.triangulator.color.ColorComposer;
import project.triangulator.color.ColorDecomposer;
import project.triangulator.image.Image;
import project.triangulator.math.Matrix;

public class SobelHorizontalFilter {

    private static final Matrix MATRIX = new Matrix( new double[][]{ { 1 , 2 , 1 } , { 0 , 0 , 0 } , { -1 , -2 , -1 } } );
    private static final int WEIGHT = 4;

    public static int filter( Image image , int x , int y ) {

        int width = SobelHorizontalFilter.MATRIX.getWidth();
        int height = SobelHorizontalFilter.MATRIX.getHeight();

        int xOffset = width / 2;
        int yOffset = height / 2;

        int accumulator = 0;

        /**
         * In order for an filter to work, the fields
         * surrounding the target field have to be
         * included into the calculation.
         * Therefore it must be iterated over each
         * field.
         */
        for ( int b = 0; b < height; b++ ) {

            for ( int a = 0; a < width; a++ ) {

                int m = x + a - xOffset;
                int n = y + b - yOffset;

                /**
                 * When accessing a field of the matrix, it may
                 * be outside of the image boundaries.
                 * Since then there is no image value to be read,
                 * this field has to be ignored in this case.
                 */
                if ( m > 0 && m < image.getWidth() && n > 0 && n < image.getHeight() ) {

                    accumulator += ColorDecomposer.getRed( image.getRGB( m , n ) ) * SobelHorizontalFilter.MATRIX.get( a , b );

                }

            }

        }

        int absolute = Math.abs( accumulator ) / SobelHorizontalFilter.WEIGHT;
        return ColorComposer.compose( absolute , absolute , absolute );

    }

}