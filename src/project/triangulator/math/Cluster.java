package project.triangulator.math;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	
	private Vector3D centroid;
	private List< Vector3D > members;
	
	/**
	 * This variables describes how much
	 * this cluster has been moved since
	 * the last iteration of the clustering
	 * algorithm.
	 */
	private double delta;
	
	public Cluster( Vector3D centroid ) {
		
		this.centroid = centroid;
		this.members = new ArrayList<>();
		
	}
	
	public Vector3D getCentroid() {
		
		return this.centroid;
		
	}
	
	public List< Vector3D > getMembers() {
		
		return this.members;
		
	}
	
	public double getDelta() {
		
		return this.delta;
		
	}
	
	public void move( Vector3D destination ) {

		this.delta = this.centroid.getDistance( destination );
		this.centroid.setX( destination.getX() );
		this.centroid.setY( destination.getY() );
		this.centroid.setZ( destination.getZ() );
		
	}

}
