package project.triangulator.math;

public class Line {
	
	private Vector2D a;
	private Vector2D b;
	
	public Line( Vector2D a , Vector2D b ) {
		
		this.a = a;
		this.b = b;
		
	}
	
	public Vector2D getA() {
		
		return this.a;
		
	}
	
	public Vector2D getB() {
		
		return this.b;
		
	}
	
	public Straight getBisector() {
		
		double slope = 0F;
		double deltaY = b.getY() - a.getY();
		
		if ( deltaY == 0 ) {
			
			slope = Float.POSITIVE_INFINITY;
			
		} else {
			
			slope = -1F / ( deltaY / ( b.getX() - a.getX() ) );
			
		}
		
		Vector2D midpoint = new Vector2D( ( b.getX() + a.getX() ) / 2F , ( b.getY() + a.getY() ) / 2F );
		double intercept = midpoint.getY() - slope * midpoint.getX();
		
		return new Straight( midpoint , slope , intercept );
		
	}
	
	public boolean equals( Line line ) {
		
		return ( this.a.equals( line.a ) && this.b.equals( line.b ) ) || ( this.a.equals( line.b ) && this.b.equals( line.a ) );
		
	}

}
