package csula.cs4660.games;

import csula.cs4660.games.models.MiniMaxState;

import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

public class MiniMax {
	@SuppressWarnings("unchecked")
    public static Node<MiniMaxState> getBestMove(Graph graph, Node<MiniMaxState> root, Integer depth, Boolean max) {
        // TODO: implement minimax to retrieve best move
        // NOTE: you should mutate graph and node as you traverse and update value
    	Node<MiniMaxState> bestValue;
    	if(depth==0){
    		return root;
    	}
    	if(max){
    		bestValue = new Node<>(new MiniMaxState(Integer.MIN_VALUE,Integer.MIN_VALUE)); 
    		for(Node<MiniMaxState> move:graph.neighbors(root)){
				Node<MiniMaxState> value = getBestMove(graph, move, depth-1, false);
    			if(value.getData().getValue()>bestValue.getData().getValue()){
    				bestValue = value;
    				Node<MiniMaxState> parent = graph.getNode(root).get();
    				parent.getData().setValue(bestValue.getData().getValue());
    			}
    		}
    		return bestValue;
    	}
    	else{
    		bestValue = new Node<>(new MiniMaxState(Integer.MAX_VALUE,Integer.MAX_VALUE));
    		for(Node<MiniMaxState> move:graph.neighbors(root)){
    			Node<MiniMaxState> value = getBestMove(graph, move, depth-1, true);
    			if(value.getData().getValue()<bestValue.getData().getValue()){
    				bestValue = value;
    				Node<MiniMaxState> parent = graph.getNode(root).get();
    				parent.getData().setValue(bestValue.getData().getValue());
    			}
    		}
    		return bestValue;
    	}
    }
}
