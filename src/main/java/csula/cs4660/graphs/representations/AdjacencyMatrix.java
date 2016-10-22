package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import org.apache.commons.lang3.ArrayUtils;



/**
 * Adjacency matrix in a sense store the nodes in two dimensional array
 *
 * TODO: please fill the method body of this class
 */

public class AdjacencyMatrix implements Representation {
    private Node[] nodes;
    private int[][] adjacencyMatrix;
    private int numberOfNodes = 0;
    public AdjacencyMatrix(File file) {
    	try
    	{
    		Scanner scanner = new Scanner(file);
    		while(scanner.hasNextLine())
    		{
    			String line = scanner.nextLine();
    			if(!line.contains(":"))
    			{
    				line.trim();
    				numberOfNodes = Integer.parseInt(line);
    				adjacencyMatrix = new int[numberOfNodes][numberOfNodes];
    				nodes = new Node[numberOfNodes];
    				for(int i = 0 ; i < numberOfNodes ; i++){
    					nodes[i] = new Node(i);
    				}
    			}
    			else
    			{
    				String[] edgeAttributes = line.split(":");
    				int value = Integer.parseInt(edgeAttributes[2]);
    				adjacencyMatrix[Integer.parseInt(edgeAttributes[0])][Integer.parseInt(edgeAttributes[1])]= value;
    			}
    		}
    		scanner.close();
    	}
    	catch(FileNotFoundException fnfe){
    		
    	}
    }
    public AdjacencyMatrix(){
    	adjacencyMatrix = new int[0][0];
    	nodes = new Node[0];
    }
    

    @Override
    public boolean adjacent(Node x, Node y) {
    	Node<Integer> xInt = x;
    	Node<Integer> yInt = y;
    	if(adjacencyMatrix[xInt.getData()][yInt.getData()]!=0)
    				return true;
        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
    	int column = 0;
    	int index = -1;
    	for(int i = 0 ;i<nodes.length;i++){
    		if(nodes[i]!=null){
    			if(nodes[i].equals(x)){
        			index = i;
        			break;
        		}
    		}
    	}
    	List<Node> neighborsNodes = new ArrayList<Node>();
		for(int i :adjacencyMatrix[index]){
    		if(i>0){
    			neighborsNodes.add(nodes[column]);
    		}
    		column+=1;
    	}
		if(neighborsNodes.contains(null)){
			neighborsNodes.remove(neighborsNodes.indexOf(null));
		}
    	return neighborsNodes;
    }

    @Override
    public boolean  addNode(Node x) {
    	if(ArrayUtils.indexOf(nodes, x)==-1)
    	{
    		nodes = Arrays.copyOf(nodes, nodes.length+1);
    		nodes[nodes.length-1] = x;
    		
    		int[][] adjacencyTemp = Arrays.copyOf(adjacencyMatrix, adjacencyMatrix.length+1);
    		adjacencyTemp[adjacencyMatrix.length] = new int[adjacencyMatrix.length];
    		for(int i = 0; i<adjacencyMatrix.length;i++){
    			adjacencyTemp[adjacencyMatrix.length][i] = 0;
    		}
    		int[][] temp = new int[adjacencyMatrix.length+1][adjacencyMatrix.length+1];
    		int t = 0;
    		for(int[] each:adjacencyTemp){
    			int[] temp1 = Arrays.copyOf(each, each.length+1);
    			temp[t] = temp1;
    			t = t + 1;
    		}
    		adjacencyMatrix = new int[adjacencyMatrix.length+1][adjacencyMatrix.length+1];
    		adjacencyMatrix = temp;
    		return true;
    	}
    	return false;
    	
    }

    @Override
    public boolean removeNode(Node x) {
    	Node<Integer> xInt = x;
    	boolean present = false;
    	int position = 0;
    	for(int i=0;i<numberOfNodes;i++)
    	{
    		if(x.equals(nodes[i]))
    		{
    			present = true;
    			nodes[i] = null;
    			position = i;
    			numberOfNodes -= numberOfNodes;
    		}
    	}
    	for(int i=position+1;i<numberOfNodes;i++)
    	{
    		nodes[i-1] = nodes[i];
    	}
    	if(present==true)
    		return true;
        return false;
    }

    @Override
    public boolean addEdge(Edge x) {
    	try{
    	Node from = x.getFrom();
    	Node to = x.getTo();
		if(adjacencyMatrix[ArrayUtils.indexOf(nodes, from)][ArrayUtils.indexOf(nodes, to)]==0)
		{
			adjacencyMatrix[ArrayUtils.indexOf(nodes, from)][ArrayUtils.indexOf(nodes, to)]=x.getValue();
			return true;
		}
    	}catch(Exception e){
    		return false;
    	}
		return false;
    }
    @Override
    public boolean removeEdge(Edge x) {
    	Node<Integer> from = x.getFrom();
    	Node<Integer> to = x.getTo();
		if(adjacencyMatrix[from.getData()][to.getData()]==0)
		{
			return false;
		}
		else
		{
			adjacencyMatrix[from.getData()][to.getData()]=0;
			x.setFrom(null);
			x.setTo(null);
			x.setValue(0);
			return true;
		}
    }

    @Override
    public int distance(Node from, Node to) {
    	
    	int weight = adjacencyMatrix[(int)from.getData()][(int)to.getData()];
        return weight;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
