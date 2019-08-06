package project.triangulator.math;

public class Straight {

	private Vector2D support;
	private double slope;
	private double intercept;
	
	public Straight( Vector2D support , double slope , double intercept ) {
		
		this.support = support;
		this.slope = slope;
		this.intercept = intercept;
		
	}
	
	public Vector2D getSupport() {
		
		return this.support;
		
	}
	
	public double getSlope() {
		
		return this.slope;
		
	}
	
	public double getIntercept() {
		
		return this.intercept;
		
	}
	
	public Vector2D getIntersection( Straight straight ) {
		
		double x = 0;
		double y = 0;
		
		if ( this.slope != straight.slope ) {
			
			if ( Double.isFinite( this.slope ) && Double.isFinite( straight.slope ) ) {
				
				x = ( this.intercept - straight.intercept ) / ( straight.slope - this.slope );
				y = this.slope * x + this.intercept;
				
			} else if ( Double.isFinite( this.slope ) && Double.isInfinite( straight.slope ) ) {
				
				x = straight.support.getX();
				y = this.slope * x + this.intercept;
				
			} else if ( Double.isInfinite( this.slope ) && Double.isFinite( straight.slope ) ) {
				
				x = this.support.getX();
				y = straight.slope * x + straight.intercept;
				
			} else {
				
				x = this.support.getX();
				
			}
			
			return new Vector2D( x , y );
			
		}
		
		return null;
		
	}
	
}
