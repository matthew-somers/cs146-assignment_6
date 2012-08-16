package a6;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

public class A6
{
    

  /**
      Prints a matrix with small integer values
      @param a the matrix
   */

    public static void print2D(int[][] a)                          {
      for (int i=0; i<a.length; i++)                      {    
        for (int j=0; j<a.length; j++)                          
          System.out.print(String.format("%3d", a[i][j]));              
        System.out.println();                             }        }

  
   /**
       Performs various tests on an instances of 
         the Graph class with a given adjacency matrix
       @param a the adjacency matrix
    */
    
    public static void test(int[][] a)                           {
      Graph g = null;
      try                                                    {           
        g = new Graph(a);                                    }
      catch(IllegalArgumentException e)                      {
        System.out.println(e.getMessage()); 
        System.out.println(); 
        return;                                              }
      a[0][a.length-1] = -1;
      int adj[][] = g.getAdjacencyMatrix();
      print2D(adj);
      System.out.println("is undirected: " + g.isUndirected());
      System.out.println("is unweighted: " + g.isUnweighted());
      int[][] d = g.floyd();
      print2D(d);
      List<Integer> path = g.findCheapestPath(0,d.length-1);
      System.out.println(
          (g.isUnweighted()?"path ":"cheapest path ")+
          "from 0 to " + 
          (d.length-1) + ":  " + path);
      if (!g.isUndirected())                              {
        System.out.println("     ProtoFloyd");
        d = g.protoFloyd();                               }
        print2D(d);
      try                                                    {           
        System.out.println(
          "representative for each vertex: " +        
          Arrays.toString(g.findConnectedComponents())); 
        path = g.findCheapestPath(0,4);
        System.out.println(
                "cheapest path from 0 to 4:  " + path);      }
        catch(IllegalArgumentException e)                    {
            System.out.println(e.getMessage());              }
      try                                                    {           
        path = g.findCheapestPath(-4,0);
        System.out.println(
              "cheapest path from -4 to 0:  " + path);       }
      catch(IllegalArgumentException e)                      {
        System.out.println(e.getMessage());                  }
      path = g.findCheapestPath(d.length-1,0);
      System.out.println(
          "cheapest path from " + (d.length-1) + 
          " to 0:  " + path);
      try                                                    {           
        System.out.println((d.length-1) + " and 0 are"
          + (g.isAdjacent(d.length-1,0)?" ":" not ")
          + "adjacent");      
        System.out.println("-5 and 5 are"
                  + (g.isAdjacent(-5,5)?" ":" not ")
                  + "adjacent");                             }
      catch(IllegalArgumentException e)                      {
          System.out.println(e.getMessage());                }
      try                                                    {           
         path = g.findCheapestPath(0,4);
          System.out.println(
                  "cheapest path from 0 to 4:  " + path);    }
      catch(IllegalArgumentException e)                      {
        System.out.println(e.getMessage());                  }
      System.out.println();     
    }
    
    /**
        Performs various tests on instances of the Graph class
        @param args is ignored
     */

    public static void main(String[] args)                       {
      int[][] a = {{0,1,9},{9,0,1},{1,9,0}};        
      Graph g = new Graph(a);
      test(a);
      int aCopy[][] = g.getAdjacencyMatrix();
      aCopy[0][2] = 100000;
      System.out.println("0 is adjacent to 2: " + 
              g.isAdjacent(0,2));
      System.out.println();
      
      int[][] b = {{0,1,2},{3,0,4},{5,-6,0}};        
      test(b);      

      int[][] b2 = {{0,1,2},{3,0,4},{5,6,0}};        
      test(b2);      

      int[][] a2 = {
              {0,1,9,9,9,9,9,9},
              {9,0,1,9,9,9,9,9},
              {9,9,0,1,9,9,9,9},
              {9,9,9,0,1,9,9,9},
              {9,9,9,9,0,1,9,9},
              {9,9,9,9,9,0,1,9},
              {9,9,9,9,9,9,0,1},
              {1,9,9,9,9,9,9,0}};        
      test(a2);

      int[][] aa = {{0,1,100000,100000},
                    {1,0,100000,100000},
                    {100000,100000,0,1},
                    {100000,100000,1,0}};     
      test(aa);     
  
      int[][] ab = {{0,1,0,0},{1,0,0,0},{0,0,0,1},{0,0,1,0}};       
      test(ab);     
      
      int[][] ragged = {{0,2,3,4}, {1,0,1}, {2,3,0,4}, {3,2,4,0}};
      test(ragged);
      
      Random r = new Random(66);
      int[][] r1 = new int[10][10];
      for (int i=0; i<r1.length; i++)
        for (int j=i+1; j<r1.length; j++)     {
          r1[i][j] = 1 + r.nextInt(99);
          r1[j][i] = r1[i][j];                } 
      test(r1);

      r1 = new int[10][10];
      for (int i=0; i<r1.length; i++)
        for (int j=i+1; j<r1.length; j++)     {
          r1[i][j] = r.nextInt(2);
          r1[j][i] = r1[i][j];                } 
      test(r1);

      r = new Random(16);
      r1 = new int[10][10];
      for (int i=0; i<r1.length; i++)
        for (int j=i+1; j<r1.length; j++)     {
          r1[i][j] = r.nextInt(6)/5;
          r1[j][i] = r1[i][j];                } 
      test(r1);
    }
  
     
}
