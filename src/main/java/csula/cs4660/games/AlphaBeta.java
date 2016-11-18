package csula.cs4660.games;

import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

public class AlphaBeta {
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Node getBestMove(Graph graph, Node<MiniMaxState> source, Integer depth, Integer alpha, Integer beta, Boolean max) {
        // TODO: implement your alpha beta pruning algorithm here
    	Node<MiniMaxState> v;
    	if(depth ==0)
    		return source;
    	if(max){
    		v = new Node<>(new MiniMaxState(Integer.MIN_VALUE,Integer.MIN_VALUE));
    		for(Node<MiniMaxState> eachNeighbor:graph.neighbors(source)){
    			Node<MiniMaxState> v1 = getBestMove(graph, eachNeighbor, depth-1, alpha, beta, false);
    			if(v1.getData().getValue()>v.getData().getValue()){
    				v = v1;
    			}
    			Node<MiniMaxState> parent = graph.getNode(source).get();
    			parent.getData().setValue(v.getData().getValue());
    			if(v.getData().getValue()>=beta){
    				
    				return v;
    			}
    			alpha = Math.max(alpha,v.getData().getValue());
    		}
    		return v;
    	}
    	else{
    		v = new Node(new MiniMaxState(Integer.MAX_VALUE,Integer.MAX_VALUE));
    		for(Node eachNeighbor:graph.neighbors(source)){
    			Node<MiniMaxState> v1 = getBestMove(graph, eachNeighbor, depth-1, alpha, beta, true);
    			if(v1.getData().getValue()<v.getData().getValue()){
    				v = v1;
    			}
    			Node<MiniMaxState> parent = graph.getNode(source).get();
    			parent.getData().setValue(v.getData().getValue());
    			if(v.getData().getValue()<=alpha){
    					return v;
    			}
    			beta = Math.min(beta, v.getData().getValue());
    		}
    		return v;
    	}
        
    }
}
