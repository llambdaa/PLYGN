package project.triangulator.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import project.triangulator.image.OutlinedImage;
import project.triangulator.image.TriangulatedImage;
import project.triangulator.math.Circumcircle;
import project.triangulator.math.Line;
import project.triangulator.math.Triangle;
import project.triangulator.math.Vector2D;

public class DelaunayTriangulator {

	public static TriangulatedImage triangulate( OutlinedImage base ) {
		
		/**
		 * This map contains all triangles and their
		 * circumcircles produced by this algorithm.
		 * These triangles make up the triangulated
		 * image.
		 */
		Map< Triangle , Circumcircle > triangles = new HashMap<>();
		
		/**
		 * Anchor triangles are needed so that there are
		 * circumcircles that can contain the inserted
		 * vertices. Then, these vertices are used to
		 * construct new triangles.
		 */
		Vector2D alpha = new Vector2D( 0 , 0 );
		Vector2D beta = new Vector2D( base.getInternal().getWidth() , 0 );
		Vector2D gamma = new Vector2D( 0 , base.getInternal().getHeight() );
		Vector2D delta = new Vector2D( beta.getX() , gamma.getY() );
		
		Triangle epsilon = new Triangle( alpha , beta , gamma );
		Triangle zeta = new Triangle( beta , gamma , delta );
		
		triangles.put( epsilon , new Circumcircle( epsilon ) );
		triangles.put( zeta , new Circumcircle( zeta ) );
		
		/**
		 * When vertices are inserted, it happens that
		 * these are contained within the circumcircles
		 * of already generated triangles. If this is the
		 * case, this triangulation with these triangles
		 * isn't valid anymore, so that the regarded
		 * triangles have to be removed and replaced
		 * at a later point in time.
		 */
		List< Triangle > invalid = new ArrayList<>();
		
		/**
		 * When invalid triangles get removed,
		 * a convex hull arises. The hull's edges
		 * are part of new triangles which have
		 * the lastly inserted vertex as one of
		 * its vertices.
		 */
		List< Line > hull = new ArrayList<>();
		base.getVertices().forEach( vertex -> {

			/**
			 * When the vertex gets inserted,
			 * triangles get invalidated.
			 * They have to be found so that
			 * the newly arisen convex hull's
			 * edges can be used to retriangulate
			 * properly.
			 */
			triangles.forEach( ( triangle , circumcircle ) -> {

				if ( circumcircle.contains( vertex ) ) {

					invalid.add( triangle );

				}

			} );

			/**
			 * The convex hull is defined by
			 * edges that are only shared by
			 * one of the invalidated triangles.
			 */
			invalid.forEach( invalidated -> {

				continuable:
				for ( Line edge : invalidated.getEdges() ) {

					for ( Triangle other : invalid ) {

						/**
						 * If an edge is shared by more than one
						 * invalidated triangles it is contained
						 * within the convex hull and therefore
						 * can't be used for proper triangulation.
						 */
						if ( !other.equals( invalidated ) && other.hasEdge( edge ) ) {

							continue continuable;

						}

					}

					/**
					 * The edge only shared by one invalidated
					 * triangle and therefore is part of the
					 * convex hull.
					 */
					hull.add( edge );

				}

			} );

			/**
			 * The invalidated triangles are no longer
			 * needed and therefore get deleted.
			 */
			invalid.forEach( triangles::remove );

			/**
			 * The convex hull's edges have to be part of
			 * the triangles that use the newly inserted
			 * vertex as one of theirs.
			 */
			hull.forEach( edge -> {

				/**
				 * Degenerate triangles can't be used to
				 * generate a circumcircle so that containment
				 * checks with vertices can be made.
				 */
				if ( ! ( ( edge.getA().getX() == edge.getB().getX() && edge.getB().getX() == vertex.getX() ) || ( edge.getA().getY() == edge.getB().getY() && edge.getB().getY() == vertex.getY() ) ) ) {

					Triangle triangle = new Triangle( edge.getA() , edge.getB() , vertex );
					triangles.put( triangle , new Circumcircle( triangle ) );

				}

			} );

			invalid.clear();
			hull.clear();

		} );
		
		return new TriangulatedImage( base.clone().getInternal() , new ArrayList<>( triangles.keySet() ) );
		
	}
	
}
