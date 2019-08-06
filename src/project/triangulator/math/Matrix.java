package project.triangulator.math;

public class Matrix {
	
	private double[][] internal;
	private int width;
	private int height;
	
	public Matrix( double[][] internal ) {
		
		this.internal = internal;
		this.width = this.internal[ 0 ].length;
		this.height = this.internal.length;
		
	}
	
	public int getWidth() {
		
		return this.width;
		
	}
	
	public int getHeight() {
		
		return this.height;
		
	}
	
	public double get( int x , int y ) {
		
		return this.internal[ y ][ x ];
		
	}

}
