package edu.iastate.cs228.hw1;

/**
 *  
 * @author Tanay Parikh 
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		super(pts);
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		mergeSortRec(0, points.length - 1);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param first   lowest index
	 * @param last  highest index
	 */
	private void mergeSortRec(int first, int last)
	{
	    if(first < last) 
	    {
	        int medium = (first + last) / 2;
	        mergeSortRec(first, medium);
	        mergeSortRec(medium + 1, last);
	        merge(first, medium, last);
	    }
	}
	
	/**
	 * Merging the 2 arrays which is
	 * low - mid and mid - high.
	 * 
	 * @param first    first array lowest index
	 * @param medium   medium index
	 * @param last     second array highest index
	 */
	private void merge(int first, int medium, int last) {
	    
	    // Variables needed..
        int firstIndex = 0, secondIndex = 0;
	    
	    // First left array.
	    Point[] firstPoints = new Point[(medium - first) + 1];
	    for (int i = 0; i < firstPoints.length; i++) 
	    {
            firstPoints[i] = points[first + i];
	    }
	    
	    // Second right array.
	    Point[] secondPoints = new Point[last - medium];
	    for (int i = 0; i < secondPoints.length; i++)
        {
            secondPoints[i] = points[medium + i + 1];
        }
        
	    // Merging both arrays..
	    for (int i = first; i < last + 1; i++) 
	    {
	        if (firstIndex < firstPoints.length && secondIndex < secondPoints.length) 
	        {
	            if (firstPoints[firstIndex].compareTo(secondPoints[secondIndex]) < 0) 
	            {
	               points[i] = firstPoints[firstIndex++];
	            } 
	            else 
	            {
	                points[i] = secondPoints[secondIndex++];
	            }
	        } 
	        else if (firstIndex < firstPoints.length) 
	        {
	            points[i] = firstPoints[firstIndex++];
	        } 
	        else if (secondIndex < secondPoints.length) 
	        {
	            points[i] = secondPoints[secondIndex++];
	        }
	    }
	    
	}


}
