package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * As name, dijkstra search using graph structure
 */
public class DijkstraSearch implements SearchStrategy {
	HashMap<Node,Integer> distanceOfEachNode;
	List<Node> openNodes;
	List<Node> closedNodes;
	HashMap<Node,Edge> parents;
	List<Edge> path;
	@Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
		distanceOfEachNode = new HashMap<Node,Integer>();
		openNodes = new ArrayList<Node>();
		closedNodes = new ArrayList<Node>();
		parents = new HashMap<Node,Edge>();
		path = new ArrayList<Edge>();
    	openNodes.add(source);
    	distanceOfEachNode.put(source, 0);
    	while(!openNodes.isEmpty()){
    		Node evaluationNode = getNodeWithLowestDistance();
    		openNodes.remove(evaluationNode);
    		closedNodes.add(evaluationNode);
    		addNeighbors(evaluationNode, graph);
    	}
    	while(dist!=source){
    		path.add(parents.get(dist));
    		dist = parents.get(dist).getFrom();
    	}
    	Collections.reverse(path);
        return path;
    }
    
    public Node getNodeWithLowestDistance(){
    	int min = distanceOfEachNode.get(openNodes.get(0));
    	int temp = min;
    	Node shortestNode = null;
    	for(Node n : openNodes){
    		if(distanceOfEachNode.containsKey(n)){
    			if(distanceOfEachNode.get(n)<=min){
    				min = distanceOfEachNode.get(n);
    				shortestNode = n;
    			}
    		}
    	}
    	if(temp==min){
    		shortestNode = openNodes.get(0);
    	}
    	return shortestNode;
    }
    
    public void addNeighbors(Node evaluationNode, Graph graph){
    	for(Node eachNeighbor : graph.neighbors(evaluationNode)){
    		boolean found = false;
    		for(Node closedNode : closedNodes){
    			if(eachNeighbor.equals(closedNode)){
    				found = true;
    			}
    		}
    		if(!found){
    			int distance = graph.distance(evaluationNode, eachNeighbor);
    			int newDistance = distanceOfEachNode.get(evaluationNode) + distance;
    			if(distanceOfEachNode.containsKey(eachNeighbor)){
	    			if(distanceOfEachNode.get(eachNeighbor)>newDistance){
	    				distanceOfEachNode.put(eachNeighbor,newDistance);
	    				parents.put(eachNeighbor, new Edge(evaluationNode,eachNeighbor,graph.distance(evaluationNode, eachNeighbor)));
	    				openNodes.add(eachNeighbor);
	    			}
    			}
    			else{
    				distanceOfEachNode.put(eachNeighbor,newDistance);
    				parents.put(eachNeighbor, new Edge(evaluationNode,eachNeighbor,graph.distance(evaluationNode, eachNeighbor)));
    				openNodes.add(eachNeighbor);
    			}
    		}
    	}
    }
}
