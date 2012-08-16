package a6;
import java.util.*;

/*
 * NOTE: Having the missing edges be represented by Integer.MAX_VALUE was
 * giving me stack overflow errors, so I changed them to 100000.
 * If your computer has more memory, it might be able to handle it.
 */


/**
 * 
 * @author Matthew Somers
 * A class representing a graph's adjacency matrix with integer values.
 */
public class Graph 
{
	
	/**
	 * This graph's adjacency matrix.
	 */
	private final int adjacencymatrix[][];
	
	/**
	 * the path to make a reduced adjacency matrix d in Floyd's algorithm
	 */
	private final int path[][];
	
	/**
	 * A flag for whether the adjacency matrix consists of 1's and 0's.
	 */
	private boolean unweighted;
	
	/**
	 * A flag for whether each of the adjacency matrix's edges have the same weight
	 * going to a vertex as they do coming back.
	 */
	private boolean undirected;
	
	
	/**
	 * The graph's constructor.
	 * It initializes the matrices and sets flags.
	 * @param matrix the adjacency matrix to be copied in.
	 */
	public Graph(int[][] matrix)
	{
		/**
		 * Simple loop counter for rows.
		 */
		int i;
		
		/**
		 * Simple loop counter for columns.
		 */
		int j;

		//instantiate rows
		adjacencymatrix = new int[matrix.length][];
		path = new int[matrix.length][];
		
		unweighted = true; //set flag before loop, default assumption
		undirected = true; //set flag before loop, probably not true
		
		//main creation loop for checks
		for (i = 0; i < matrix.length; i++)
		{
			//instantiate columns
			adjacencymatrix[i] = new int[matrix[i].length];
			path[i] = new int[matrix[i].length];
			
			for (j = 0; j < matrix[i].length; j++)
			{
				//all checks
				
				if ((matrix.length != matrix[i].length) || 
					(matrix[i][j] < 0))
					throw new IllegalArgumentException(
						"Matrix must be a square with nonnegative edge costs.");
				
				if ((matrix[i][j] != 1) && (matrix[i][j] != 0))
					unweighted = false;
				
				if (matrix[i][j] != matrix[j][i])
					undirected = false;
				
				//actually copy
				adjacencymatrix[i][j] = matrix[i][j];
				
				path[i][j] = -1;
			}
		}
	}

	/**
	 * Checks whether 2 vertices are adjacent.
	 * @param u the first vertex
	 * @param v the second vertex
	 * @return true or false depending on whether they're adjacent
	 */
	public boolean isAdjacent(int u, int v)
	{
		if ((u < adjacencymatrix.length) && (v < adjacencymatrix.length) 
				&& (u >= 0) && (v >= 0))
		{
			if (adjacencymatrix[u][v] != Integer.MAX_VALUE)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Simple getter for unweighted flag.
	 * @return true or false depending on whether adjacency matrix 
	 * is made of 0's and 1's.
	 */
	public boolean isUnweighted()
	{
		return unweighted;
	}
	
	/**
	 * Simple getter for the undirected flag.
	 * @return true or false depending on whether every existing edge
	 * goes both ways
	 */
	public boolean isUndirected()
	{
		return undirected;
	}
	
	/**
	 * Getter for the adjacency matrix.
	 * @return a copy of the adjacency matrix so the original can't be modified.
	 */
	public int[][] getAdjacencyMatrix()
	{
		/**
		 * A copy of the adjacency matrix so it can't be modified when returned.
		 */
		int[][] d = new int[adjacencymatrix.length][adjacencymatrix.length];
		
		/**
		 * Simple loop counter for rows.
		 */
		int i;
		
		/**
		 * Simple loop counter for columns.
		 */
		int j;
		
		for (i = 0; i < adjacencymatrix.length; i++)
			for (j = 0; j < adjacencymatrix.length; j++)
				d[i][j] = adjacencymatrix[i][j];
		
		return d;
	}
	
	/**
	 * The all-pairs shortest path algorithm.
	 * @return A matrix d consisting of the shortest distances to other vertices.
	 */
	public int[][] floyd()
	{
		/**
		 * A reduced version of the adjacency matrix using Floyd's trick
		 * of moving through up to a vertex k.
		 */
		int[][] d = new int[adjacencymatrix.length][adjacencymatrix.length];
		
		/**
		 * Simple loop counter for rows.
		 */
		int i;
		
		/**
		 * Simple loop counter for columns.
		 */
		int j;
		
		/**
		 * Variable representing the maximum vertex the path can use.
		 */
		int k;
		
		for (i = 0; i < adjacencymatrix.length; i++)
			for (j = 0; j < adjacencymatrix.length; j++)
				d[i][j] = adjacencymatrix[i][j];

		
		//main algorithm loop
		for (k = 0; k < d.length; k++)
			for (i = 0; i < d.length; i++)
				for (j = 0; j < d.length; j++)
				{
					if (isUnweighted())
					{
						//set main diagonal to 1's
						if (i == j)
							d[i][j] = 1;
						
						else if (d[i][j] < d[i][k] + d[j][k])
							d[i][j] = 1;
					}
					
					else //is weighted
					{
						if (d[i][j] > (d[i][k] + d[k][j]))
						{
							d[i][j] = (d[i][k] + d[k][j]);
							path[i][j] = k;
						}
					}
				}

		return d;
	}
	
	/**
	 * A different version of the Floyd's algorithm.
	 * NOTE: This one is not working correctly.
	 * I've decided to leave the work in the method commented out,
	 * as I can only assume I was on the right track.
	 * For now, the uncommented out part is regular Floyd so the result matrix
	 * is still correct.
	 * @return the same matrix d as regular Floyd.
	 */
	public int[][] protoFloyd()
	{
		/**
		 * A reduced version of the adjacency matrix using a modified 
		 * version of Floyd's trick.
		 * This traverses up to k vertices when making the path.
		 */
		int[][] d = new int[adjacencymatrix.length][adjacencymatrix.length];
		
		/**
		 * Simple loop counter for rows.
		 */
		int i;
		
		/**
		 * Simple loop counter for columns.
		 */
		int j;
		
		/**
		 * Variable representing the maximum number of vertices 
		 * a path can traverse.
		 */
		int k;
		
		/**
		 * Essentially the recursive representation of the normal key floyd
		 * algorith's d[i][k] + d[k][j].
		 * This time though, we don't know how far we'll have to go.
		 */
		int sumofpaths;
		
		/**
		 * A list to help keep track of which node's have been visited in 
		 * recursion.
		 */
		LinkedList<Integer> visited = new LinkedList<Integer>();
		
		for (i = 0; i < adjacencymatrix.length; i++)
			for (j = 0; j < adjacencymatrix.length; j++)
				d[i][j] = adjacencymatrix[i][j];

		
		//main algorithm loop
		for (k = 0; k < d.length; k++)
			for (i = 0; i < d.length; i++)
				for (j = 0; j < d.length; j++)
				{
					if (d[i][j] > (d[i][k] + d[k][j]))
					{
						d[i][j] = (d[i][k] + d[k][j]);
						path[i][j] = k;
					}
					
					/*
					visited.add(i);
					sumofpaths = 0;
					sumofpaths = protorecursion(d, visited, sumofpaths, i, j, j, k, 1);
					
					
					if (d[i][j] > sumofpaths)
					{
						d[i][j] = sumofpaths;
						path[i][j] = k;
					}
					
					visited.clear();
					*/
				}

		return d;
	}
	
	/**
	 * A helper method for my not working version of protofloyd().
	 * @param d the original adjacency matrix
	 * @param visited a list to keep track of which node's have been visited
	 * @param sumofpaths the sum of the path up to k
	 * @param start the starting vertex
	 * @param j the current row
	 * @param end the goal vertex
	 * @param k the current maximum number of times we can traverse
	 * @param p the current number of times we've traversed a vertex
	 * @return the sumofpaths of however many vertices we've traversed
	 */
	public int protorecursion(int[][] d, LinkedList<Integer> visited, 
		int sumofpaths, int start, int j, int end, int k, int p)
	{	
		if (visited.contains(j))
			return sumofpaths;
		
		visited.add(j);
		
		if (p >= k-1)
		{
			sumofpaths += d[start][end];
			return sumofpaths;
		}
		
		p++;
		
		/**
		 * the next potential column
		 */
		int m;
		
		for (m = 0; m < d.length; m++)
		{
			if (!visited.contains(m))
			{
				sumofpaths += d[j][m];
				return protorecursion(d, visited, sumofpaths, start, m, end, k, p);
			}
		}
		
		return sumofpaths;
	}
	
	/**
	 * Uses the path matrix produced with Floyd to reconstruct a path.
	 * A missing path is not represented accurately.
	 * @param start the starting vertex
	 * @param end the ending vertex
	 * @return a list of the path from start to end.
	 */
	public List<Integer> findCheapestPath(int start, int end)
	{
		/**
		 * The cheapest path from start to end reconstructed from path matrix
		 */
		LinkedList<Integer> cheapestpath = new LinkedList<Integer>();
		
		if ((start >= adjacencymatrix.length) || (end >= adjacencymatrix.length) 
				|| (start < 0) || (end < 0))
		{
			throw new IllegalArgumentException("One of the input vetices is not in this graph.");
		}
		
		cheapestpath.add(start);
		
		//success on first try!
		if (path[start][end] == -1)
		{
			cheapestpath.add(end);
			return cheapestpath;
		}
		
		//recursion
		findCheapestPathRecursion(start, path[start][end], cheapestpath);
		findCheapestPathRecursion(path[start][end], end, cheapestpath);
		
		return cheapestpath;
	}
    
	/**
	 * A helper method for finding the cheapest path.
	 * @param start the starting point of this recursion
	 * @param end the ending point of this recursion
	 * @param cheapestpath the path list carried through recursion
	 * @return the path list to be sent back to caller
	 */
	private List<Integer> findCheapestPathRecursion(int start, int end, LinkedList<Integer> cheapestpath)
	{	
		//success!
		if (path[start][end] == -1)
		{
			cheapestpath.add(end);
			return cheapestpath;
		}
		
		//recursion
		findCheapestPathRecursion(start, path[start][end], cheapestpath);
		findCheapestPathRecursion(path[start][end], end, cheapestpath);
		
		return cheapestpath;
	}
	
	/**
	 * Uses path matrix to find whether vertices are connected or not.
	 * A single representative is chosen from a partition.
	 * @return an array consisting of each vertex's representative
	 */
	public int[] findConnectedComponents()
	{
		if (isUndirected())
			throw new IllegalArgumentException("This method requires a directed graph.");
		
		/**
		 * An array to hold the representatives of each vertex.
		 */
		int[] components = new int[path.length];
		
		/**
		 * Simple loop counter for rows.
		 */
		int i;
		
		/**
		 * Simple loop counter for columns.
		 */
		int j;
		
		/**
		 * A third loop counter.
		 */
		int k;
		
		/**
		 * The current rep initialized to it's starting value.
		 */
		int rep = 0;
		
		/**
		 * A flag for whether a new segment was found.
		 */
		boolean newsegment;

		for (i = 0; i < path.length; i++)
		{
			//aka a vertex is disconnected
			if (path[0][i] == -2)
			{
				newsegment = true; //until proven otherwise
				
				for (j = 0; j < path.length; j++)
				{
					for (k = 0; k < path.length; k++)
					{
						// is this section already in the solution?
						if ((components[k] == j) && (path[j][i] != -2))
						{
							//just another part of a previous section
							rep = j;
							newsegment = false;
						}
					}
				}
				
				//exhausted possible duplicates
				if (newsegment)
				{
					rep = i;
				}
			}
			
			components[i] = rep;
		}
		
		return components;
	}
}
