package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Object oriented representation of graph is using OOP approach to store nodes
 * and edges
 */
public class ObjectOriented implements Representation {
    private Collection<Node> nodes;
    private Collection<Edge> edges;
    int numberOfNodes = 0;
    private Node fromNode;
    private Node toNode;
    public ObjectOriented(File file) {
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
    				nodes = new ArrayList<Node>();
    				edges = new ArrayList<Edge>();
    			}
    			else
    			{
    				String[] edgeAttributes = line.split(":");
    				fromNode = new Node<>(Integer.parseInt(edgeAttributes[0]));
    				toNode = new Node<>(Integer.parseInt(edgeAttributes[1]));
    				int value = Integer.parseInt(edgeAttributes[2]);
    				Edge edge = new Edge(fromNode,toNode,value);
    				boolean nodePresent=false;
    				boolean edgePresent=false;
    				if(nodes.isEmpty())
    					nodes.add(fromNode);
    				else{
	    				for(Node n:nodes){
	    					if(fromNode.equals(n)){
	    						nodePresent = true;
	    					}	
	    				}
	    				if(!nodePresent)
	    					nodes.add(fromNode);
    				}
    				if(edges.isEmpty())
    					edges.add(edge);
    				else{
	    				for(Edge e:edges){
	    					if(e.equals(edge)){
	    						edgePresent = true;
	    					}
	    				}
	    				if(!edgePresent)
	    					edges.add(edge);
    				}
    			}
    		}
    		scanner.close();
    	}
    	catch(FileNotFoundException fnfe){
    		
    	}
    }

    public ObjectOriented() {

    }

    @Override
    public boolean adjacent(Node x, Node y) {
    	for(Edge edge:edges)
    	{
    		if(edge.getFrom().equals(x) && edge.getTo().equals(y))
    		{
    			return true;
    		}
    		if(edge.getTo().equals(x) && edge.getFrom().equals(y))
    		{
    			return true;
    		}
    	}
        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
    	List<Node> neighborsList = new ArrayList<Node>(); 
    	boolean present = false;
    	for(Edge edge:edges)
    	{
    		if(edge.getFrom().equals(x)){
    			neighborsList.add(edge.getTo());
    			present = true;
    		}
    	}
    	
    	return neighborsList;
    	
    }

    @Override
    public boolean addNode(Node x) {
    	boolean present = false;
    	for(Node node:nodes){
    		if(node.equals(x)){
    			present = true;
    		}
    	}
    	if(present==true){
    		return false;
    	}
    	else{
    		nodes.add(x);
    		return true;
    	}
    }

    @Override
    public boolean removeNode(Node x) {
    	boolean nodeFound = false;
    	Iterator<Edge> iterate = edges.iterator();
    	while(iterate.hasNext())
    	{
    		Edge e = iterate.next();
    	
    		if(e.getFrom().equals(x) || e.getTo().equals(x))
    		{
    			iterate.remove();
    		}
    	}
    	for(Node node:nodes){
    		if(node.equals(x)){
    			nodes.remove(node);
    			nodeFound = true;
    			break;
    		}
    	}
    	if(nodeFound)
    		return true;
    	return false;
    }

    @Override
    public boolean addEdge(Edge x) {
    	boolean present = false;
    	for(Edge edge:edges)
    	{
    		if(edge.equals(x))
    			present = true;
    	}
    	if(present)
    		return false;
    	else
    	{
    		edges.add(x);
    		return true;
    	}
    }

    @Override
    public boolean removeEdge(Edge x) {
    	boolean present = false;
    	for(Edge edge:edges)
    	{
    		if(edge.equals(x)){
    			edges.remove(x);
    			present = true;
    			break;
    		}
    	}
    	if(present)
    		return true;
    	else
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

    @Override
    public Optional<Node> getNode(Node node) {
        Iterator<Node> iterator = nodes.iterator();
        Optional<Node> result = Optional.empty();
        while (iterator.hasNext()) {
            Node next = iterator.next();
            if (next.equals(node)) {
                result = Optional.of(next);
            }
        }
        return result;
    }
}
