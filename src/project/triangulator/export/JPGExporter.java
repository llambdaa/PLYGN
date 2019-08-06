package project.triangulator.export;

import project.triangulator.image.ColorizedImage;
import project.triangulator.image.Image;
import project.triangulator.math.Vector2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JPGExporter {

    public static void export( ColorizedImage image , String path ) {

        /**
         * The triangles are mapped to their respective dominant color.
         * Their corresponding places in the source image aren't colored yet.
         */
        Image result = new Image( new BufferedImage( image.getWidth() , image.getHeight() , BufferedImage.TYPE_INT_RGB ) );
        image.getTriangles().forEach( ( triangle , color ) -> {

            /**
             * The scan area of the image has to be
             * cropped for each triangle so that it
             * only contains the most relevant pixels.
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

                        result.setRGB( x , y , color );

                    }

                }

            }

        } );

        try {

            /**
             * Now that the image data has been written to an object,
             * the object's data are written into an actual file.
             */
            ImageIO.write( result.getInternal() , "jpg" , new File( path ) );

        } catch ( IOException exception ) {

            exception.printStackTrace();

        }

    }

}