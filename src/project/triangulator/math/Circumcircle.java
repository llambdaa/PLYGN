package project.triangulator.math;

public class Circumcircle {

	public Vector2D center;
	private double radius;
	
	public Circumcircle( Triangle triangle ) {
		
		Line a = new Line( triangle.getA() , triangle.getB() );
		Line b = new Line( triangle.getB() , triangle.getC() );
		
		this.center = a.getBisector().getIntersection( b.getBisector() );
		this.radius = this.center.getDistance( triangle.getC() );
		
	}
	
	public Vector2D getCenter() {
		
		return this.center;
		
	}
	
	public double getRadius() {
		
		return this.radius;
		
	}
	
	public boolean contains( Vector2D vector ) {
		
		return vector.getDistance( this.center ) <= this.radius;
		
	}
	
}
