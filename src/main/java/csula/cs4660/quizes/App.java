package csula.cs4660.quizes;

import csula.cs4660.quizes.models.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import csula.cs4660.graphs.*;
/**
 * Here is your quiz entry point and your app
 */
public class App {
    public static void main(String[] args) {
        // to get a state, you can simply call `Client.getState with the id`
    	
    	// All declarations
    	Queue<State> queue = new LinkedList<State>();
    	HashMap<State, Boolean> visited = new HashMap<State,Boolean>();
    	HashMap<State, State> parent = new HashMap<State,State>();
    	List<State> path = new ArrayList<State>();

    	//initialization
    	State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
        State goalState = Client.getState("e577aa79473673f6158cc73e0e5dc122").get();

    	visited.put(initialState, true);
    	queue.add(initialState);
        
    	//BFS
    	while(!queue.isEmpty()){
    		State currentState = (State)queue.remove();
    		
    		if(Client.getState(currentState.getId()).equals(Client.getState(goalState.getId()))){
    			
    			System.out.println("BFS Path :");
    			while(!currentState.getId().equals(initialState.getId())){
    				path.add(currentState);
    				currentState = parent.get(currentState);
    			}
    			Collections.reverse(path);
    			int depth = 0;
    			for(State state : path){
    				System.out.print(state.getLocation().getName()+":");
    				depth += 1;
    			}
    			System.out.print("-"+depth);
    		}
    		State[] neighboringStates = Client.getState(currentState.getId()).get().getNeighbors();
    		for(State eachNeighbor : neighboringStates){
    			if(!visited.containsKey(eachNeighbor)){
    				queue.add(eachNeighbor);
    				visited.put(eachNeighbor, true);
    				parent.put(eachNeighbor, currentState);
    			}
    		}
    	}
    }
}
