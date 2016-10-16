package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
    			}
    			else
    			{
    				String[] edgeAttributes = line.split(":");
    				int value = Integer.parseInt(edgeAttributes[2]);
    				boolean presentFromNode = false;
    				boolean presentToNode = false;
    				int count = 0;
    				Node<Integer> fromNode = new Node<Integer>(Integer.parseInt(edgeAttributes[0]));
    				Node<Integer> toNode = new Node<Integer>(Integer.parseInt(edgeAttributes[1]));
       				new Edge(fromNode,toNode,value);
       				for(int i=0;i<numberOfNodes;i++)
       				{
       					if(fromNode.equals(nodes[i]))
       					{
       						presentFromNode = true;
       						break;
       					}
       				}
       				if(presentFromNode==false)
       				{
       					for(int i=0;i<numberOfNodes;i++)
       					{
       						if(nodes[i]==null)
       						{
       							nodes[i] = fromNode;
       							break;
       						}
       					}
       				}
       				for(int i=0;i<numberOfNodes;i++)
       				{
       					if(toNode.equals(nodes[i]))
       					{
       						presentToNode = true;
       						break;
       					}
       				}
       				if(presentToNode==false)
       				{
       					for(int i=0;i<numberOfNodes;i++)
       					{
       						if(nodes[i]==null)
       						{
       							nodes[i] = toNode;
       							break;
       						}
       					}
       				}
    				int fromNodeInt = fromNode.getData();
    				int toNodeInt = toNode.getData();
    				adjacencyMatrix[fromNodeInt][toNodeInt] = value;
    			}
    		}
    		scanner.close();
    	}
    	catch(FileNotFoundException fnfe){
    		
    	}
    }
    public AdjacencyMatrix(){
    	adjacencyMatrix = new int[0][0];
    }
    public AdjacencyMatrix(int numberOfNodes) {
    	adjacencyMatrix = new int[numberOfNodes][numberOfNodes];
		nodes = new Node[numberOfNodes];
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
    	Node<Integer> xInt = x;
    	List<Node> neighborsNodes = new ArrayList<Node>();
    	Node neighbor;
    	for(int i=0;i<numberOfNodes;i++)
    	{
    		if(xInt.getData()==i)
    		{
	    		for(int j=0;j<numberOfNodes;j++)
	    		{
	    			if(adjacencyMatrix[i][j]!=0)
	    			{
	    				neighborsNodes.add(new Node(j));
	    			}
	    		}
    		}
    	}
        return neighborsNodes;
    }

    @Override
    public boolean addNode(Node x) {
    	Node<Integer> xInt = x;
    	boolean present = false;
    	for(int i=0;i<numberOfNodes;i++)
    	{
    		if(x.equals(nodes[i]))
    		{
    			present = true;
    		}
    	}
    	
    	if(!present)
    	{
    		nodes = Arrays.copyOf(nodes, nodes.length+1);
    		nodes[nodes.length-1] = x;
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
    	Node<Integer> from = x.getFrom();
    	Node<Integer> to = x.getTo();
		if(adjacencyMatrix[from.getData()][to.getData()]!=0)
		{
			return false;
		}
		else
		{
			adjacencyMatrix[from.getData()][to.getData()]=x.getValue();
			new Edge(x.getFrom(),x.getTo(),x.getValue());
			return true;
		}
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
    	Node<Integer> fromInt = from;
    	Node<Integer> toInt = to;
    	int weight = adjacencyMatrix[fromInt.getData()][toInt.getData()];
        return weight;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
