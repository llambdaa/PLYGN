package project.triangulator.image;

import java.awt.image.BufferedImage;
import java.util.List;

import project.triangulator.filter.classifier.Filter;
import project.triangulator.math.Vector2D;
import project.triangulator.process.Process;
import project.triangulator.process.ProcessLogger;
import project.triangulator.transform.DelaunayTriangulator;
import project.triangulator.transform.classifier.Outliner;
import project.triangulator.transform.classifier.Triangulator;

public class OutlinedImage extends Image {

	private List< Vector2D > vertices;
	
	public OutlinedImage( BufferedImage internal , List< Vector2D > nodes ) {
		
		super( internal );
		this.vertices = nodes;
		
	}
	
	public List< Vector2D > getVertices() {
		
		return this.vertices;
		
	}
	
	public OutlinedImage filter( Filter filter ) {
		
		return new OutlinedImage( super.filter( filter ).internal , this.vertices );
		
	}
	
	@Deprecated
	public OutlinedImage outline( Outliner outliner ) {
		
		return this;
		
	}
	
	public TriangulatedImage triangulate( Triangulator triangulator ) {
		
		ProcessLogger.log( Process.TRIANGULATION , triangulator );
		
		if ( triangulator.equals( Triangulator.DELAUNAY ) ) {
			
			return DelaunayTriangulator.triangulate( this );
			
		}
		
		return null;
		
	}
	
}
