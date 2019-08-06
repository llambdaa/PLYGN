package project.triangulator.image;

import project.triangulator.export.BMPExporter;
import project.triangulator.export.JPGExporter;
import project.triangulator.export.PNGExporter;
import project.triangulator.export.SVGExporter;
import project.triangulator.export.classifier.ExportFormat;
import project.triangulator.filter.classifier.Filter;
import project.triangulator.math.Triangle;
import project.triangulator.process.Process;
import project.triangulator.process.ProcessLogger;
import project.triangulator.transform.classifier.Outliner;

import java.awt.image.BufferedImage;
import java.util.Map;

public class ColorizedImage extends Image {

    private Map< Triangle, Integer > triangles;

    public ColorizedImage( BufferedImage internal , Map< Triangle , Integer > triangles ) {

        super( internal );
        this.triangles = triangles;

    }

    public Map< Triangle , Integer > getTriangles() {

        return this.triangles;

    }

    public ColorizedImage filter( Filter filter ) {

        return new ColorizedImage( super.filter( filter ).internal , this.triangles );

    }

    public OutlinedImage outline( Outliner outliner ) {

        return super.outline( outliner );

    }

    public void export( ExportFormat format , String path ) {

        ProcessLogger.log( Process.EXPORT , path );

        if ( format.equals( ExportFormat.BMP ) ) {

            BMPExporter.export( this , path );

        } else if ( format.equals( ExportFormat.JPG ) ) {

            JPGExporter.export( this , path );

        } else if ( format.equals( ExportFormat.PNG ) ) {

            PNGExporter.export( this , path );

        } else if ( format.equals( ExportFormat.SVG ) ) {

            SVGExporter.export( this , path );

        }

    }

}
