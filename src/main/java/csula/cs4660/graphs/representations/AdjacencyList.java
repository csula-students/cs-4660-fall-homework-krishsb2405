package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;

/**
 * Adjacency list is probably the most common implementation to store the unknown
 * loose graph
 *
 * TODO: please implement the method body
 */
public class AdjacencyList implements Representation {
    private Map<Node<Integer>, Collection<Edge>> adjacencyList;
    Node<Integer> fromNode;
	Node<Integer> toNode;
	Edge fromEdge;
	Edge toEdge;
    public AdjacencyList(File file) {
    	try
    	{
    		Scanner scanner = new Scanner(file);
    		
    		int numberOfNodes = 0;
    		
    		while(scanner.hasNextLine())
    		{
    			String line = scanner.nextLine();
    			if(!line.contains(":"))
    			{
    				line.trim();
    				numberOfNodes = Integer.parseInt(line);
    				adjacencyList = new HashMap<Node<Integer>,Collection<Edge>>(numberOfNodes);
    			}
    			else
    			{
    				String[] edgeAttributes = line.split(":");
    				fromNode = new Node<Integer>(Integer.parseInt(edgeAttributes[0]));
    				toNode = new Node<Integer>(Integer.parseInt(edgeAttributes[1]));
    				int value = Integer.parseInt(edgeAttributes[2]);
    				
    				Edge fromEdge = new Edge(fromNode,toNode,value);
    				Edge toEdge = new Edge(toNode,fromNode,value);
    				Collection<Edge> fromEdges = null;
    				Collection<Edge> toEdges = null;
     				if(!adjacencyList.containsKey(fromNode))
    				{
    					adjacencyList.put(fromNode,null);
    				}
    				if(adjacencyList.containsKey(fromNode))
    				{
    					if(adjacencyList.get(fromNode) == null)
    					{
	    					fromEdges = new ArrayList<Edge>();
	    					fromEdges.add(fromEdge);
	    					adjacencyList.put(fromNode,fromEdges);
    					}
    					else
    					{
    						fromEdges = adjacencyList.get(fromNode);
    						fromEdges.add(fromEdge);
    						adjacencyList.put(fromNode,fromEdges);
    					}
    				}
    			}
    		}
    		scanner.close();
    	}
    	catch(FileNotFoundException fnfe){
    		
    	}
    }

    public AdjacencyList() {

    }

    @Override
    public boolean adjacent(Node x, Node y) {
    	Iterator entries = adjacencyList.entrySet().iterator();
    	while(entries.hasNext())
    	{
    		Entry thisEntry = (Entry)entries.next();
    		Object key = thisEntry.getKey();
    		Object value = thisEntry.getValue();
    		Collection<Edge> edge = (Collection<Edge>)value;
    		Node currentNode = (Node)key;
    		for(Edge e:edge)
    		{
    			if((x.equals(e.getFrom()) && y.equals(e.getTo())) ||
    					(x.equals(e.getTo()) && y.equals(e.getFrom())))
    			{
    			 	return true;
    			}
    		}
    	}
        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
    	Collection<Edge> edges = adjacencyList.get(x);
    	List<Node> nodes = new ArrayList<Node>();
    	if(edges!=null)
    	{
	    	for(Edge edge:edges)
	     	{
	    		nodes.add(edge.getTo());
	    	}
    	}
        return nodes;
    }

    @Override
    public boolean addNode(Node x) {
    	if(adjacencyList.get(x)==null)
    	{
    		adjacencyList.put(x, null);
    		return true;
    	}
        return false;
    }

    @Override
    public boolean removeNode(Node x) {
    	boolean nodeFound = false;
    	boolean edgeFound = false;
    	Iterator entries = adjacencyList.entrySet().iterator();
    	while(entries.hasNext())
    	{
    		Entry thisEntry = (Entry)entries.next();
    		Object key = thisEntry.getKey();
    		Object value = thisEntry.getValue();
    		Collection<Edge> edge = (Collection<Edge>)value;
    		Node currentNode = (Node)key;
    		Iterator<Edge> iterate = edge.iterator();
        	while(iterate.hasNext())
        	{
        		Edge e = iterate.next();
        	
        		if(e.getFrom().equals(x) || e.getTo().equals(x))
        		{
        			iterate.remove();
        			edgeFound = true;
        		}
        	}
    	}
    	if(adjacencyList.containsKey(x))
    	{
    		adjacencyList.remove(x);
    		nodeFound = true;
    	}
    	if(nodeFound && edgeFound)
    		return true;
    	else
    		return false;
    	
    }

    @Override
    public boolean addEdge(Edge x) {
    	Node from = x.getFrom();
    	Node to = x.getTo();
    	Collection<Edge> edges = adjacencyList.get(from);
    	boolean present = false;
    	for(Edge edge : edges)
    	{
    		if(edge.getFrom().equals(from) && edge.getTo().equals(to))
    			present = true;
    	}
    	if(present)
    		return false;
    	else
    	{
    		edges.add(x);
    		adjacencyList.put(x.getFrom(), edges);
    		return true;
    	}    
    }
    //hi

    @Override
    public boolean removeEdge(Edge x) {
    	Node from = x.getFrom();
    	Collection<Edge> edges = adjacencyList.get(from);
    	for(Edge edge:edges)
    	{
    		if(edge.equals(x))
    		{
    			edges.remove(edge);
    			adjacencyList.put(from, edges);
    			return true;
    		}
    	}
    	return false;
    }

    @Override
    public int distance(Node from, Node to) {
        return 0;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
