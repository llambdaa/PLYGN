package project.triangulator.export;

import project.triangulator.image.ColorizedImage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SVGExporter {

    public static void export( ColorizedImage image , String path ) {

        try {

            File target = new File( path );
            target.createNewFile();

            try ( BufferedWriter writer = new BufferedWriter( new FileWriter( target ) ) ) {

                /**
                 * The target file needs a specific
                 * header file in order to work (properly).
                 */
                writer.write( "<?xml version=\"1.0\" standalone=\"no\"?>\n" +
                              "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" +
                              "<svg viewBox=\"0 0 " + image.getWidth() + " " + image.getHeight() + "\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" shape-rendering=\"optimizeSpeed\">\n" );

                image.getTriangles().forEach( ( triangle , color ) -> {

                    try {

                        /**
                         * Each triangle can be understood as a polygon which
                         * has multiple vertices.
                         */
                        writer.write( "\t<polygon fill=\"#" + Integer.toHexString( color ) + "\" points=\"" +
                                      ( int ) triangle.getA().getX() + "," + ( int ) triangle.getA().getY() + " " +
                                      ( int ) triangle.getB().getX() + "," + ( int ) triangle.getB().getY() + " " +
                                      ( int ) triangle.getC().getX() + "," + ( int ) triangle.getC().getY() + "\"/>\n" );

                    } catch ( IOException exception ) {

                        exception.printStackTrace();

                    }

                } );

                /**
                 * Since the file's whole content is contained within
                 * the <svg></svg> tags, the starting tag has to be closed.
                 */
                writer.write( "</svg>" );

            }

        } catch ( IOException exception ) {

            exception.printStackTrace();

        }

    }

}