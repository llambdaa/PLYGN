package project.triangulator.math;

public class Vector3D {

	private double x;
	private double y;
	private double z;
	
	public Vector3D( double x , double y , double z ) {
		
		this.x = x;
		this.y = y;
		this.z = z;
		
	}
	
	public double getX() {
		
		return this.x;
		
	}
	
	public double getY() {
		
		return this.y;
		
	}
	
	public double getZ() {
		
		return this.z;
		
	}

	public void setX( double x ) {

		this.x = x;

	}

	public void setY( double y ) {

		this.y = y;

	}

	public void setZ( double z ) {

		this.z = z;

	}
	
	public double getDistance( Vector3D vector ) {
		
		double x = vector.x - this.x;
		double y = vector.y - this.y;
		double z = vector.z - this.z;
		
		return Math.sqrt( x * x + y * y + z * z );
		
	}
	
	public boolean equals( Vector3D vector ) {
		
		return vector != null && this.x == vector.x && this.y == vector.y && this.z == vector.z;
		
	}
	
}