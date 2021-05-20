package edu.iastate.cs228.hw1;

/**
 *  
 * @author Tanay Parikh 
 *
 */

/**
 * 
 * This class implements selection sort.   
 *
 */

public class SelectionSorter extends AbstractSorter
{
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts  
	 */
	public SelectionSorter(Point[] pts)  
	{
	    super(pts);
	}	

	
	/** 
	 * Apply selection sort on the array points[] of the parent class AbstractSorter.  
	 * 
	 */
	@Override 
	public void sort()
	{
        for (int i = 0; i < points.length - 1; i++) 
        { 
            int minimum = i; 
            for (int k = i + 1; k < points.length; k++) 
            {
                if (points[k].compareTo(points[minimum]) < 0) 
                {
                    minimum = k; 
                }
            }
            swap(minimum, i);
        } 
	}	
}
