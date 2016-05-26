package hw3.hash;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HashTableVisualizer {

    public static void main(String[] args) {
        /* scale: StdDraw scale
           N:     number of items
           M:     number of buckets */

        double scale = 0.5;
        int N = 2000;
        int M = 100;

        HashTableDrawingUtility.setScale(scale);
        Set<Oomage> oomies = new HashSet<Oomage>();
        for (int i = 0; i < N; i += 1) {
            oomies.add(SimpleOomage.randomSimpleOomage());
        }
        visualize(oomies, M, scale);
    }

    public static void visualize(Set<Oomage> set, int M, double scale) {
        HashTableDrawingUtility.drawLabels(M);

        /* TODO: Create a visualization of the given hash table. Use
           du.xCoord and du.yCoord to figure out where to draw
           Oomages.
         */
        int [] currentPos = new int[M];
        for (int i = 0; i < M; i++) {
            currentPos[i] = 0;
        }

        for (Oomage someOomage: set) {
            int y = (someOomage.hashCode() & 0x7FFFFFFF) % M;
            int x = currentPos[y];
            someOomage.draw(HashTableDrawingUtility.xCoord(x), HashTableDrawingUtility.yCoord(y, M), scale);
            currentPos[y] += 1;
        }

        /* When done with visualizer, be sure to try 
           scale = 0.5, N = 2000, M = 100. */           
    }
} 
