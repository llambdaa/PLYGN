package project.triangulator.color;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import project.triangulator.math.Cluster;
import project.triangulator.math.Pair;
import project.triangulator.math.Triangle;
import project.triangulator.math.Vector2D;
import project.triangulator.math.Vector3D;

/**
 * This class provides a k-means
 * clustering implementation in third
 * dimension for clustering similar colors,
 * which are translated into three dimensional
 * vectors beforehand.
 */
public class ClusteredPalettePicker {

    private static final int CLUSTERS = 5;

    public static List< Integer > pick( BufferedImage source , Triangle triangle ) {

        /**
         * The map contains three-dimensional
         * representations of colors found in
         * the given area of the target image
         * together with its metadata like
         * the cluster's index it is currently
         * affiliated with and the distances
         * to each cluster's centroid.
         *
         * Translation:
         *
         * 		red = x ,
         * 		green = y ,
         * 		blue = z
         */
        Map< Vector3D , Pair< Double[] , Integer > > colors = new HashMap<>();

        /**
         * The scan area of the image has to be
         * cropped so that it only contains the
         * most relevant pixels.
         */
        int minX = Math.max( ( int ) Math.floor( Math.min( triangle.getA().getX() , Math.min( triangle.getB().getX() , triangle.getC().getX() ) ) ) , 0 );
        int maxX = Math.min( ( int ) Math.ceil( Math.max( triangle.getA().getX() , Math.max( triangle.getB().getX() , triangle.getC().getX() ) ) ) , source.getWidth() - 1 );

        int minY = Math.max( ( int ) Math.floor( Math.min( triangle.getA().getY() , Math.min( triangle.getB().getY() , triangle.getC().getY() ) ) ) , 0 );
        int maxY = Math.min( ( int ) Math.ceil( Math.max( triangle.getA().getY() , Math.max( triangle.getB().getY() , triangle.getC().getY() ) ) ) , source.getHeight() - 1 );

        for ( int y = minY; y <= maxY; y++ ) {

            for ( int x = minX; x <= maxX; x++ ) {

                /**
                 * The restricted area is rectangular rather
                 * than triangular. Therefore it contains
                 * some unnecessary image information, which
                 * have to be filtered.
                 */
                if ( triangle.contains( new Vector2D( x , y ) ) ) {

                    int color = source.getRGB( x , y );

                    /**
                     * The space transformation is done by
                     * decomposing the color value into its
                     * three color channels. Their values are
                     * then simply used as vector coordinates.
                     */
                    colors.put( new Vector3D( ColorDecomposer.getRed( color ) , ColorDecomposer.getGreen( color ) , ColorDecomposer.getBlue( color ) ) ,
                                new Pair<>( new Double[ ClusteredPalettePicker.CLUSTERS ] , -1 ) );

                }

            }

        }

        /**
         * 1. Step: Initialization
         *
         * For the number of color clusters wanted, the
         * same amount of randomly distributed centroids
         * are generated. These will move until the algorithm
         * converges.
         * Furthermore the distance of each color vector to
         * each of the centroids has to be calculated initially.
         *
         * -----
         *
         * Each centroid is saved with its surrounding vectors and
         * the distance it moved since the last iteration internally,
         * so that, at the end, its clear which color belongs to which
         * cluster.
         */
        List< Cluster > clusters = new ArrayList<>();

        /**
         * There are no anchor points so that the centroids
         * have to be randomly distributed in color space.
         */
        Random generator = new Random();

        for ( int i = 0; i < ClusteredPalettePicker.CLUSTERS; i++ ) {

            clusters.add( new Cluster( new Vector3D( generator.nextInt( 256 ) , generator.nextInt( 256 ) , generator.nextInt( 256 ) ) ) );

        }

        /**
         * When the centroids are randomly distributed at the start,
         * the distance of each of them to every color vector has
         * an initial value.
         */
        colors.forEach( ( color , metadata ) -> {

            for ( int i = 0; i < metadata.getA().length; i++ ) {

                metadata.getA()[ i ] = color.getDistance( clusters.get( i ).getCentroid() );

            }

            /**
             * The index representing the closest centroid
             * is set to its initial value.
             * Since the algorithm didn't even start,
             * there is no (valid) nearest centroid.
             */
            metadata.setB( -1 );

        } );

        /**
         * 2. Step: Assignment
         *
         * Each vector must be assigned to a certain cluster.
         * If no vector changes its affiliation in this step,
         * the algorithm ultimately converges.
         *
         * -----
         *
         * An indicator is needed, which shows whether the algorithm
         * converged or not.
         */
        boolean converged = false;

        while( !converged ) {

            /**
             * The indicator gets set to #false# if there is
             * any vector that changed its affiliation.
             * Otherwise it will remain #true# and the algorithm
             * stops due to convergence.
             */
            converged = true;

            for ( Map.Entry< Vector3D , Pair< Double[] , Integer > > entry : colors.entrySet() ) {

                /**
                 * Each color vector has metadata assigned
                 * to it like the (assumed) distances of
                 * the centroids to itself.
                 */
                Vector3D color = entry.getKey();
                Double[] distances = entry.getValue().getA();

                /**
                 * The currently closest centroid can be described
                 * by an index in a collection holding it.
                 */
                int index = entry.getValue().getB();

                /**
                 * Since the color vector is only assigned to the closest centroid,
                 * it must be kept track of the currently calculated nearest centroid
                 * and its distance to the color vector.
                 */
                int nearest = -1;
                double reference = Double.POSITIVE_INFINITY;

                for ( int i = 0; i < distances.length; i++ ) {

                    /**
                     * Since the direction the centroid moved in the last iteration
                     * is unknown, following is considered.
                     *
                     * The centroid has moved #delta#, in the best case closer to
                     * the color vector. It may now have entered a sphere surrounding
                     * this vector with a radius of #distances[index]# meaning it is now
                     * the closest centroid.
                     *
                     * Hence, if the assumed distance of the moved centroid is smaller
                     * then the distance to the assumed currently closest centroid (#distance[index]#),
                     * its actual distance to the color vector has to be calculated to confirm
                     * the assumption that is now has entered the hypothetical sphere.
                     */
                    distances[ i ] = distances[ i ] - clusters.get( i ).getDelta();

                    if ( distances[ i ] < reference ) {

                        nearest = i;

                    }

                }

                distances[ nearest ] = color.getDistance( clusters.get( nearest ).getCentroid() );

                /**
                 * If another centroid is now the closest one to the
                 * color vector, the vector has to be reassigned.
                 */
                if ( nearest != index ) {

                    /**
                     * If this iteration is not the first one,
                     * the color vector is assigned to a cluster.
                     */
                    if ( index != -1 ) {

                        /**
                         * First, the color vector has to be removed from
                         * the cluster it was previously assigned to.
                         */
                        clusters.get( index ).getMembers().remove( color );

                    }

                    /**
                     * After that, the color vector must be assigned to
                     * the new cluster, the one that is closest to it now.
                     */
                    clusters.get( nearest ).getMembers().add( color );

                    /**
                     * The centroid the color vector is associated with
                     * has to be redefined.
                     */
                    entry.getValue().setB( nearest );

                    /**
                     * Since now a rearrangement occurred, the algorithm
                     * didn't converge in this iteration.
                     */
                    converged = false;

                }

            }

            /**
             * 3. Step: Update
             *
             * When colors are assigned to a cluster in the previous step,
             * the centroids of the clusters conceptually change
             * their positions, they have to be recalculated.
             */
            clusters.forEach( cluster -> {

                List< Vector3D > members = cluster.getMembers();
                int denominator = members.size();

                /**
                 * If there is no member, no data point affiliated
                 * with this cluster, it is considered invalid and
                 * hence cannot be moved due to computation since
                 * there is no data to be computed.
                 */
                if ( denominator > 0 ) {

                    int x = 0;
                    int y = 0;
                    int z = 0;

                    for ( Vector3D color : members ) {

                        x += color.getX();
                        y += color.getY();
                        z += color.getZ();

                    }

                    Vector3D destination = new Vector3D( x / denominator , y / denominator , z / denominator );
                    cluster.move( destination );

                }

            } );

        }

        /**
         * When the algorithm converges, the arisen clusters must be evaluated
         * and sorted by the amount of affiliated surrounding colors in
         * descending order.
         */
        clusters.sort( ( a , b ) -> Integer.compare( b.getMembers().size() , a.getMembers().size() ) );

        /**
         * After sorting the clusters, their position in space
         * has to be translated into a usable color value.
         */
        return clusters.stream().map( cluster -> ColorComposer.compose( ( int ) cluster.getCentroid().getX() ,
                                                                        ( int ) cluster.getCentroid().getY() ,
                                                                        ( int ) cluster.getCentroid().getZ() ) ).collect( Collectors.toList() );

    }

}
