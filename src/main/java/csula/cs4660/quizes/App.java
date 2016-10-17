package csula.cs4660.quizes;

import csula.cs4660.quizes.models.DTO;
import csula.cs4660.quizes.models.State;

import java.util.*;
public class App {
    public static void main(String[] args) {
        // to get a state, you can simply call `Client.getState with the id`
    	//BFS();
    	Dijkstra();
    }

	private static void Dijkstra() {
		System.out.println();
		System.out.println("Dijkstra's path:");
		State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
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
    			System.out.println("found solution with depth of "+findDepth(parents, evaluationState, initialState));
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
	private static void BFS() {
		// TODO Auto-generated method stub
		State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
        // to get an edge between state to its neighbor, you can call stateTransition
        Queue<State> frontier = new LinkedList<>();
        Set<State> exploredSet = new HashSet<>();
        Map<State, State> parents = new HashMap<>();
        frontier.add(initialState);
        System.out.println("BFS Path:");
        while (!frontier.isEmpty()) {
            State current = frontier.poll();
            exploredSet.add(current);
            if (current.getId().equals("e577aa79473673f6158cc73e0e5dc122")) {
                // construct actions from endTile
                System.out.println("found solution with depth of " + findDepth(parents, current, initialState));
                break;
            }
            // for every possible action
            for (State neighbor: Client.getState(current.getId()).get().getNeighbors()) {
                // state transition
                if (!exploredSet.contains(neighbor)) {
                    parents.put(neighbor, current);
                    frontier.add(neighbor);
                }
            }
        }
    }

    public static int findDepth(Map<State, State> parents, State current, State start) {
        State c = current;
        int depth = 0;
        List<State> path = new ArrayList<State>();
        while (!c.equals(start)) {
            depth ++;
            path.add(c);
            c = parents.get(c);
        }
        Collections.reverse(path);
        for(int i =0;i < path.size()-1;i++){
        	State firstState = path.get(i);
        	State secondState = path.get(i+1);
        	Optional<DTO> dto = Client.stateTransition(firstState.getId(), secondState.getId());
        	System.out.println(firstState.getLocation().getName()+":"+secondState.getLocation().getName()+":-"+dto.get().getEvent().getEffect()+"                    ---"+dto.get().getEvent().getDescription());
        }
        return depth;
    }
    
    private static State getStateWithLowestDistance(HashMap<State, Integer> distanceOfEachState,
			List<State> openStates) {
		// TODO Auto-generated method stub
    	int min = distanceOfEachState.get(openStates.get(0));
    	int temp = min;
    	State shortestState = null;
    	for(State n : openStates){
    		if(distanceOfEachState.containsKey(n)){
    			if(distanceOfEachState.get(n)>min){
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
