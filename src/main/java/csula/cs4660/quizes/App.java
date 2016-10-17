package csula.cs4660.quizes;

import csula.cs4660.quizes.models.DTO;
import csula.cs4660.quizes.models.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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
    	

    	//initialization
    	State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
       
    	visited.put(initialState, true);
    	queue.add(initialState);
        
    	//BFS
    	while(!queue.isEmpty()){
    		State currentState = (State)queue.remove();
    		
    		if(currentState.getId().equals("e577aa79473673f6158cc73e0e5dc122")){
    			List<State> path = new ArrayList<State>();
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
    			System.out.println();
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
    	
    	//Djikstra's 
    	HashMap<State,Integer> distanceOfEachState = new HashMap<State,Integer>();
    	List<State> openStates = new ArrayList<State>();
    	List<State> closedStates = new ArrayList<State>();
    	HashMap<State, State> parents = new HashMap<State,State>();
    	initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
    	openStates.add(initialState);
    	distanceOfEachState.put(initialState, 0);
    	while(!openStates.isEmpty()){
    		State evaluationState = getStateWithLowestDistance(distanceOfEachState,openStates);
    		openStates.remove(evaluationState);
    		closedStates.add(evaluationState);
    		
    		if(evaluationState.getId().equals("e577aa79473673f6158cc73e0e5dc122")){
    			List<State> path = new ArrayList<State>();
    			System.out.println("Djikstra's Path :");
    			while(!evaluationState.getId().equals(initialState.getId())){
    				path.add(evaluationState);
    				evaluationState = parents.get(evaluationState);
    			}
    			Collections.reverse(path);
    			int depth = 0;
    			for(State state : path){
    				System.out.print(state.getLocation().getName()+":");
    				depth += 1;
    			}
    			System.out.print("-"+depth);
    			System.out.println();
    		}
    		for(State neighbor:Client.getState(evaluationState.getId()).get().getNeighbors()){
    			boolean found = false;
        		for(State closedState : closedStates){
        			if(neighbor.equals(closedState)){
        				found = true;
        			}
        		}
    			if(!found){
    				Optional<DTO> dto = Client.stateTransition(neighbor.getId(),evaluationState.getId());
    				int effect = dto.get().getEvent().getEffect();
    				int newEffect = distanceOfEachState.get(evaluationState) + effect;
    				if(distanceOfEachState.containsKey(neighbor)){
    					if(distanceOfEachState.get(neighbor)>newEffect){
    						distanceOfEachState.put(neighbor, newEffect);
    						parents.put(neighbor, evaluationState);
    						openStates.add(neighbor);
    					}
    				}else{
    					distanceOfEachState.put(neighbor, newEffect);
    					parents.put(neighbor, evaluationState);
    					openStates.add(neighbor);
    				}
    			}
    		}
    	}
    }

	private static State getStateWithLowestDistance(HashMap<State, Integer> distanceOfEachState,
			List<State> openStates) {
		// TODO Auto-generated method stub
    	int min = distanceOfEachState.get(openStates.get(0));
    	int temp = min;
    	State shortestState = null;
    	for(State n : openStates){
    		if(distanceOfEachState.containsKey(n)){
    			if(distanceOfEachState.get(n)<=min){
    				min = distanceOfEachState.get(n);
    				shortestState = n;
    			}
    		}
    	}
    	if(temp==min){
    		shortestState = openStates.get(0);
    	}
    	return shortestState;
		
	}

}
