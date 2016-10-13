package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Breadth first search
 */
public class BFS implements SearchStrategy {
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
    	List<Edge> result = new ArrayList<Edge>();
    	Queue<Node> q = new LinkedList<Node>();
    	q.add(source);
    	HashMap<Node, Boolean> visited = new HashMap<Node,Boolean>();
    	visited.put(source, true);
    	HashMap<Node,Edge> previousNode = new HashMap<Node,Edge>();
    	while(!q.isEmpty()){
    		Node currentNode = (Node)q.remove();
    		List<Node> neighborsOfCurrentNode = new ArrayList<Node>();
    		neighborsOfCurrentNode = graph.neighbors(currentNode);
    		for(Node eachNeighbor:neighborsOfCurrentNode){
    			if(!visited.containsKey(eachNeighbor)){
    				q.add(eachNeighbor);
    				visited.put(eachNeighbor,true);
    				previousNode.put(eachNeighbor,new Edge(currentNode,eachNeighbor,graph.distance(currentNode, eachNeighbor)));
    			}
    		}
    	}
    	Stack<Edge> path = new Stack<Edge>();
    	while(dist!=source){
    		path.push(previousNode.get(dist));
    		dist = previousNode.get(dist).getFrom();
    	}
    	while(!path.isEmpty()){
    		result.add(path.pop());
    	}
        return result;
    }
}
