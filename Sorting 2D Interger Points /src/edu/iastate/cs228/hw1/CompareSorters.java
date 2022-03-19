package edu.iastate.cs228.hw1;

/**
 *  
 * @author Tanay Parikh 
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner; 


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{		
		// TODO 
		// 
		// Conducts multiple rounds of comparison of four sorting algorithms.  Within each round, 
		// set up scanning as follows: 
		// 
		//    a) If asked to scan random points, calls generateRandomPoints() to initialize an array 
		//       of random points. 
		// 
		//    b) Reassigns to the array scanners[] (declared below) the references to four new 
		//       PointScanner objects, which are created using four different values  
		//       of the Algorithm type:  SelectionSort, InsertionSort, MergeSort and QuickSort. 
		// 
		// 	
	    PointScanner[] scanners = new PointScanner[4]; 
		Scanner input = new Scanner(System.in);
		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");
		System.out.println();
        System.out.println("keys: 1 (random integers) 2 (file input) 3 (exit)");
        int trail = 1;
        boolean stop = false;
        while(!stop) {
            
            System.out.print("Trail "+trail+": ");
            trail++;
            int trial = input.nextInt();
            switch(trial) {
            
            case 1:
                System.out.print("Enter number of random points: ");
                int length = input.nextInt();
                Point[] points = generateRandomPoints(length, new Random());
                for(int j = 0; j < Algorithm.values().length; j++) {
                    scanners[j] = new PointScanner(points, Algorithm.values()[j]);
                }
                break;
            case 2: 
                System.out.print("Points from a file\n"
                        + "File name: ");
                String filename = input.next();
                for(int j = 0; j < Algorithm.values().length; j++) {
                    scanners[j] = new PointScanner(filename, Algorithm.values()[j]);
                }
                break;
            case 3:
                stop = true;
            
            }
            // For each input of points, do the following. 
            // 
            //     a) Initialize the array scanners[].  
            //
            //     b) Iterate through the array scanners[], and have every scanner call the scan() 
            //        method in the PointScanner class.  
            //
            //     c) After all four scans are done for the input, print out the statistics table from
            //        section 2.
            //
            // A sample scenario is given in Section 2 of the project description. 
            // Scanners are created..
            if(!stop)
            {
                System.out.println();
                System.out.println("algorithm\tsize\ttime\t(ns)\n"
                        + "----------------------------------");
                for(PointScanner scanner: scanners) {
                    scanner.scan();
                    System.out.println(scanner.stats());
                }
                System.out.println("----------------------------------");
                System.out.println();
            }
            
        }
        input.close();
		
	}
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] ◊ [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
	    if(numPts < 1) {
	        throw new IllegalArgumentException();
	    }
		Point[] points = new Point[numPts];
		for(int i = 0; i < numPts; i++) {
		    points[i] = new Point(rand.nextInt(101) - 50, rand.nextInt(101) - 50);
		}
		return points;
	}
	
}
