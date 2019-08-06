package project.triangulator.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import project.triangulator.color.ColorDecomposer;
import project.triangulator.core.Application;
import project.triangulator.filter.LaplaceFilter;
import project.triangulator.filter.classifier.Filter;
import project.triangulator.image.Image;
import project.triangulator.image.OutlinedImage;
import project.triangulator.math.Vector2D;

public class LaplaceOutliner {

	public static OutlinedImage outline( Image base ) {

		/**
		 * Each vertex that surpasses the threshold
		 * is authorized to be part of the vertex cloud
		 * which is used for triangulating the image.
		 */
		List< Vector2D > filtered = new ArrayList<>();

		/**
		 * The "Sobel-Operator" is defined for greyscale images
		 * because it is trivial to detect edges with their help.
		 */
		Image source = base.filter( Filter.GREYSCALE );

		for ( int y = 0; y < base.getHeight(); y++ ) {

			outer:
			for ( int x = 0; x < base.getWidth(); x++ ) {

				/**
				 * The "Laplace-Operator" determines a value
				 * defining the significance of an edge.
				 * This value must exceed the threshold so that
				 * the regarding pixel can be considered a
				 * proper vertex.
				 */
				if ( ColorDecomposer.getRed( LaplaceFilter.filter( source , x , y ) ) >= Application.OUTLINE_THRESHOLD ) {

					filtered.add( new Vector2D( x , y ) );

				}

			}

		}

		/**
		 * The list contains each vertex that is part
		 * of the vertex cloud with which the image
		 * triangulation can be done.
		 */
		List< Vector2D > cloud = new ArrayList<>();

		/**
		 * In order to perform a randomized vicinity allocation,
		 * random vertices must be created.
		 */
		Random generator = new Random();

		for ( int i = 0; i < Application.VERTEX_MAXIMUM_COUNT; i++ ) {

			/**
			 * The randomly generated vertex is a reference point
			 * for distance calculations which decide which of the
			 * filtered vertices is the closest one to this position.
			 * The closest vertex will be part of the vertex cloud.
			 */
			Vector2D reference = new Vector2D( generator.nextInt( base.getWidth() ) , generator.nextInt( base.getHeight() ) );

			/**
			 * The nearest vertex to the reference vertex is found by
			 * calculating the each distance and checking it against
			 * the reference distance variable.
			 */
			Vector2D nearest = null;
			double distance = Double.POSITIVE_INFINITY;

			continuable:
			for ( Vector2D vertex : filtered ) {

				/**
				 * If a vertex is already part of the cloud,
				 * it mustn't be added again because this will
				 * lead to issues when triangulating the image.
				 */
				if ( !cloud.contains( vertex ) ) {

					double delta = vertex.getDistance( reference );

					if ( delta < distance ) {

						/**
						 * In combination to the random distribution of the
						 * reference vertices, possible cloud vertices must maintain
						 * a minimum distance from each other.
						 * Otherwise the result will contain loads of squished triangles.
						 */
						for ( Vector2D element : cloud ) {

							if ( vertex.getDistance( element ) < Application.VERTEX_MINIMUM_DISTANCE ) {

								continue continuable;

							}

						}

						nearest = vertex;
						distance = delta;

					}

				}

			}

			/**
			 * The closest vertex to the reference vertex is
			 * part of the vertex cloud.
			 */
			if ( nearest != null ) {

				cloud.add( nearest );

			}

		}

		return new OutlinedImage( base.clone().getInternal() , cloud );
		
	}
	
}
