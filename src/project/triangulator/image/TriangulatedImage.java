package project.triangulator.image;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

import project.triangulator.color.ClusteredPalettePicker;
import project.triangulator.color.UnclusteredPalettePicker;
import project.triangulator.filter.classifier.Filter;
import project.triangulator.math.Triangle;
import project.triangulator.process.Process;
import project.triangulator.process.ProcessLogger;
import project.triangulator.transform.classifier.Colorizer;
import project.triangulator.transform.classifier.Outliner;

public class TriangulatedImage extends Image {

	private List< Triangle > triangles;
	
	public TriangulatedImage( BufferedImage internal , List< Triangle > triangles ) {
		
		super( internal );
		this.triangles = triangles;
		
	}
	
	public List< Triangle > getTriangles() {
		
		return this.triangles;
		
	}
	
	public TriangulatedImage filter( Filter filter ) {
		
		return new TriangulatedImage( super.filter( filter ).internal , this.triangles );
		
	}
	
	public OutlinedImage outline( Outliner outliner ) {
		
		return super.outline( outliner );
		
	}
	
	public ColorizedImage colorize( Colorizer colorizer ) {
	
		ProcessLogger.log( Process.COLORIZATION , colorizer );
		
		if ( colorizer.equals( Colorizer.UNCLUSTERED ) ) {

			return new ColorizedImage( this.internal , this.triangles.stream().collect( Collectors.toMap( triangle -> triangle , triangle -> UnclusteredPalettePicker.pick( this.internal , triangle ).get( 0 ) ) ) );
			
		} else if ( colorizer.equals( Colorizer.CLUSTERED ) ) {

			return new ColorizedImage( this.internal , this.triangles.stream().collect( Collectors.toMap( triangle -> triangle , triangle -> ClusteredPalettePicker.pick( this.internal , triangle ).get( 0 ) ) ) );
			
		}
		
		return null;
		
	}
	
}