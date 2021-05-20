package edu.iastate.cs228.hw1;

/**
 *  
 * @author Tanay Parikh
 *
 */

/**
 * 
 * This class implements insertion sort.   
 *
 */

public class InsertionSorter extends AbstractSorter 
{
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts  
	 */
	public InsertionSorter(Point[] pts) 
	{
		super(pts); 
	}	

	
	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.  
	 */
	@Override 
	public void sort()
	{
        for (int i = 1; i < points.length; i++) 
        {
            Point current = points[i];
            int k = i - 1;
            while (k >= 0 && points[k].compareTo(current) > 0) 
            {
                points[k + 1] = points[k];
                k--;
            }
            points[k + 1] = current;
        }
	}		
}
