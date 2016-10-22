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
import java.util.Optional;
import java.util.Scanner;

/**
 * Object oriented representation of graph is using OOP approach to store nodes
 * and edges
 *
 * TODO: Please fill the body of methods in this class
 */
public class ObjectOriented implements Representation {
    private Collection<Node> nodes;
    private Collection<Edge> edges;
    int numberOfNodes = 0;
    private Node fromNode;
    private Node toNode;
    private HashMap<Node,Collection<Edge>> edgesForOneNode;
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
    				edgesForOneNode = new HashMap<Node,Collection<Edge>>();
    				for(int i = 0; i <numberOfNodes; i ++){
    					edgesForOneNode.put(new Node(i), new ArrayList<Edge>());
    				}
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
	    				if(!nodePresent){
	    					nodes.add(fromNode);
	    				}
	    					
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
    				for(Node n : nodes){
    					Collection<Edge> temp = new ArrayList<Edge>();
    					for(Edge e : edges){
    						if(e.getFrom().equals(n)){
    							
    							temp.add(e);
    							edgesForOneNode.put(n,temp);
    						}
    					}
    					
    				}
    			}
    		}
    		scanner.close();
    	}
    	catch(FileNotFoundException fnfe){
    		
    	}
    }

    public ObjectOriented() {
    	nodes = new ArrayList<Node>(); 
        edges = new ArrayList<Edge>();
        edgesForOneNode = new HashMap<Node,Collection<Edge>>();
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
    	
    	if(edgesForOneNode.containsKey(x)){
    		Collection<Edge> edge = edgesForOneNode.get(x);
    		edge.forEach(e->{
        		if(e.getFrom().equals(x)){neighborsList.add(e.getTo());}
        	});
    	}
    	return neighborsList;
    	
    }

    @Override
    public boolean addNode(Node x) {
    	if(!edgesForOneNode.containsKey(x)){
    		edgesForOneNode.put(x, new ArrayList<Edge>());
    		nodes.add(x);
    		return true;
    	}
    	return false;
    }

    @Override
    public boolean removeNode(Node x) {
    	if(edgesForOneNode.containsKey(x)){
    		Collection<Edge> edgesList = edgesForOneNode.get(x);
    		edgesForOneNode.remove(x);
    		for(Map.Entry<Node,Collection<Edge>> entry:edgesForOneNode.entrySet()){
    			Collection<Edge> tempEdges = entry.getValue();
    			Node for1 = entry.getKey();
    			Iterator<Edge> iterate = tempEdges.iterator();
            	
            	while(iterate.hasNext())
            	{
            		Edge e = iterate.next();
            		if(e.getTo().equals(x))
            		{
            			iterate.remove();
            		}
            	}
    			edgesForOneNode.put(for1, tempEdges);
    		}
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
        			break;
        		}
        	}
        	return true;
    	}
    	return false;
    	
    }

    @Override
    public boolean addEdge(Edge x) {
    	Node from = x.getFrom();
    	Node to = x.getTo();
    	if(edgesForOneNode.containsKey(x.getFrom()) && edgesForOneNode.containsKey(x.getTo())){
    		Collection<Edge> temp = edgesForOneNode.get(from);
    		for(Edge e:temp){
    			if(e.equals(x)){
    				return false;
    			}
    		}
    		edges.add(x);
    		temp.add(x);
    		edgesForOneNode.put(from, temp);
    		return true;
    	}
    	return false;
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
    	for(Edge edge:edges)
    	{
    		if(edge.getFrom().equals(from) && edge.getTo().equals(to))
    		{
    			return edge.getValue();
    		}
    		if(edge.getTo().equals(from) && edge.getFrom().equals(to))
    		{
    			return edge.getValue();
    		}
    	}
        return 0;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
