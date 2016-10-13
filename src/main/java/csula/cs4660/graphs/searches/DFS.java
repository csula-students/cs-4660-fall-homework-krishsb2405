package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Depth first search
 */
public class DFS implements SearchStrategy {
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
    	Stack<Node> stack = new Stack<Node>();
    	List<Edge> result = new ArrayList<Edge>();
    	HashMap<Node,Boolean> visited = new HashMap<Node,Boolean>();
    	HashMap<Node,Edge> previous = new HashMap<Node,Edge>();
    	stack.push(source);
    	visited.put(source, true);
    	while(!stack.isEmpty()){
    		Node currentNode = stack.peek();
    		List<Node> neighborsOfCurrentNode = new ArrayList<Node>();
    		neighborsOfCurrentNode = graph.neighbors(currentNode);
    		Node n = null;
    		if(neighborsOfCurrentNode.size()>=1){
    			for(Node n1:neighborsOfCurrentNode){
    				if(!visited.containsKey(n1)){
    					n = n1;
    					break;
    				}
    			}
    		}
    		if(!visited.containsKey(n) && n!=null){
				stack.push(n);
				visited.put(n, true);
				previous.put(n, new Edge(currentNode,n,graph.distance(currentNode, n)));
    		}
    		else{
    			stack.pop();
    		}
    	}
    	
    	Stack<Edge> path = new Stack<Edge>();
    	while(dist!=source){
    		path.push(previous.get(dist));
    		dist = previous.get(dist).getFrom();
    	}
    	while(!path.isEmpty()){
    		result.add(path.pop());
    	}
        return result;
    }
}
