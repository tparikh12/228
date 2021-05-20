package edu.iastate.cs228.hw1;

/**
 *  
 * @author Tanay Parikh 
 *
 */

/**
 * 
 * This class implements the version of the quicksort algorithm presented in the lecture.   
 *
 */

public class QuickSorter extends AbstractSorter
{ 
		
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *   
	 * @param pts   input array of integers
	 */
	public QuickSorter(Point[] pts)
	{
		super(pts);
	}
		

	/**
	 * Carry out quicksort on the array points[] of the AbstractSorter class.  
	 * 
	 */
	@Override 
	public void sort()
	{
		quickSortRec(0, points.length - 1);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last)
	{
        if(first < last)  
        {  
            int medium = partition(first, last);  
            quickSortRec(first, medium - 1);  
            quickSortRec(medium + 1, last);  
        }
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last)
	{
        int medium = first;
        while (medium != last) 
        {
            while (medium != last && points[medium].compareTo(points[last]) <= 0)
            {
                last--;
            }
            if (points[medium].compareTo(points[last]) > 0) 
            {
                swap(medium, last);
                medium = last;
            }
            while (medium != first & points[medium].compareTo(points[first]) >= 0)
            {
                first++;
            }
            if (medium == first) 
            {
                break;
            }
            else if (points[medium].compareTo(points[first]) < 0) 
            {
                swap(medium, first);
                medium = first;
            }
        }
        return medium;
	}	

}
