package csula.cs4660.quizes;

import csula.cs4660.quizes.models.DTO;
import csula.cs4660.quizes.models.State;

import java.util.*;
public class App {
    public static void main(String[] args) {
        // to get a state, you can simply call `Client.getState with the id`
    	BFS();
    	Dijkstra();
    }

	private static void Dijkstra() {
		System.out.println();
		System.out.println("Dijkstra's path:");
		State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
		HashMap<String,Integer> distanceOfEachState = new HashMap<String,Integer>();
    	List<State> openStates = new ArrayList<State>();
    	List<State> closedStates = new ArrayList<State>();
    	HashMap<State, State> parents = new HashMap<State,State>();
    	initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
    	openStates.add(initialState);
    	distanceOfEachState.put(initialState.getId(), 0);
    	while(!openStates.isEmpty()){
    		State evaluationState = getStateWithHighestEffectValue(distanceOfEachState,openStates);
    		openStates.remove(evaluationState);
    		closedStates.add(evaluationState);
    		
    		if(evaluationState.getId().equals("e577aa79473673f6158cc73e0e5dc122")){
    			System.out.println("found solution with depth of "+findDepth(parents, evaluationState, initialState));
    			break;
    		}
    		State[] neighbors = Client.getState(evaluationState.getId()).get().getNeighbors();
    		for(State neighbor:neighbors){    			
    			boolean found = false;
        		for(State closedState : closedStates){
        			if(neighbor.equals(closedState)){
        				found = true;
        			}
        		}
    			if(!found){
    				Optional<DTO> dto = Client.stateTransition(neighbor.getId(),evaluationState.getId());
    				int effect = dto.get().getEvent().getEffect();
    				int totalEffectOfEachState = distanceOfEachState.get(evaluationState.getId()) + effect;
    				if(distanceOfEachState.containsKey(neighbor.getId())){
    					if(distanceOfEachState.get(neighbor.getId())<totalEffectOfEachState){
    						distanceOfEachState.put(neighbor.getId(), totalEffectOfEachState);
    						parents.put(neighbor, evaluationState);
    						openStates.add(neighbor);
    					}
    				}else{
    					distanceOfEachState.put(neighbor.getId(), totalEffectOfEachState);
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
        boolean reachedGoal = false;
        while (!frontier.isEmpty() && !reachedGoal) {
            State current = frontier.poll();
            exploredSet.add(current);
            
            // for every possible action
            for (State neighbor: Client.getState(current.getId()).get().getNeighbors()) {
                // state transition
            	if (neighbor.getId().equals("e577aa79473673f6158cc73e0e5dc122")) {
                    // construct actions from endTile
                    System.out.println("found solution with depth of " + findDepth(parents, current, initialState));
                    reachedGoal = true;
                    break;
                }
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
        path.add(start);
        Collections.reverse(path);
        for(int i =0;i < path.size()-1;i++){
        	State firstState = path.get(i);
        	State secondState = path.get(i+1);
        	Optional<DTO> dto = Client.stateTransition(firstState.getId(), secondState.getId());
        	System.out.println(firstState.getLocation().getName()+":"+secondState.getLocation().getName()+":"+dto.get().getEvent().getEffect());
        }
        return depth;
    }
    
    private static State getStateWithHighestEffectValue(HashMap<String, Integer> distanceOfEachState,
			List<State> openStates) {
		// TODO Auto-generated method stub
    	int max = distanceOfEachState.get(openStates.get(0).getId());
    	int temp = max;
    	State highestEffectState = null;
    	for(State n : openStates){
    		if(distanceOfEachState.containsKey(n.getId())){
    			int effect = distanceOfEachState.get(n.getId());
    			if(effect>=max){
    				max = distanceOfEachState.get(n.getId());
    				highestEffectState = n;
    			}
    		}
    	}
    	if(temp==max){
    		highestEffectState = openStates.get(0);
    	}
    	return highestEffectState;
		
	}
}
