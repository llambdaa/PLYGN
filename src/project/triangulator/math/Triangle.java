package project.triangulator.math;

import java.util.Arrays;
import java.util.List;

public class Triangle {

	private Vector2D a;
	private Vector2D b;
	private Vector2D c;
	
	public Triangle( Vector2D a , Vector2D b , Vector2D c ) {
		
		this.a = a;
		this.b = b;
		this.c = c;
		
	}
	
	public Vector2D getA() {
		
		return this.a;
		
	}
	
	public Vector2D getB() {
		
		return this.b;
		
	}
	
	public Vector2D getC() {
		
		return this.c;
		
	}
	
	public List< Line > getEdges() {
		
		return Arrays.asList( new Line( this.a , this.b ) , new Line( this.b , this.c ) , new Line( this.c , this.a ) );
		
	}
	
	public List< Vector2D > getVertices() {
		
		return Arrays.asList( this.a , this.b , this.c );
		
	}
	
	public boolean contains( Vector2D vector ) {
		
		Vector2D a = this.c.subtract( this.a );
		Vector2D b = this.b.subtract( this.a );
		Vector2D c = vector.subtract( this.a );
		
		double alpha = a.getDot( a );
		double beta = a.getDot( b );
		double gamma = a.getDot( c );
		double delta = b.getDot( b );
		double epsilon = b.getDot( c );
		
		double denominator = alpha * delta - beta * beta;
		double u = ( delta * gamma - beta * epsilon ) / denominator;
		double v = ( alpha * epsilon - beta * gamma ) / denominator;
		
		return u >= 0 && v >= 0 && u + v <= 1;
		
	}
	
	public boolean hasEdge( Line edge ) {
		
		for ( Line line : this.getEdges() ) {
			
			if ( line.equals( edge ) ) {
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public boolean hasVertex( Vector2D vertex ) {
		
		return this.a.equals( vertex ) || this.b.equals( vertex ) || this.c.equals( vertex );
		
	}
	
}
