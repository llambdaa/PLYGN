package project.triangulator.math;

public class Vector2D {

	private double x;
	private double y;
	
	public Vector2D( double x , double y ) {
		
		this.x = x;
		this.y = y;
		
	}
	
	public double getX() {
		
		return this.x;
		
	}
	
	public double getY() {
		
		return this.y;
		
	}
	
	public Vector2D subtract( Vector2D vector ) {
		
		return new Vector2D( this.x - vector.x , this.y - vector.y );
		
	}
	
	public double getDistance( Vector2D vector ) {
		
		double x = vector.x - this.x;
		double y = vector.y - this.y;
		
		return Math.sqrt( x * x + y * y );
		
	}
	
	public double getDot( Vector2D vector ) {
		
		return this.x * vector.x + this.y * vector.y;
		
	}
	
	public boolean equals( Vector2D vector ) {
		
		return vector != null && this.x == vector.x && this.y == vector.y;
		
	}
	
}
