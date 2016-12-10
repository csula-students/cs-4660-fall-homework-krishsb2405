package csula.cs4660.games;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import java.math.*;


class Player {
	static int floodFillCount = 0;
	static Node rootNode = null;
	static Integer Alpha = Integer.MIN_VALUE;
	static Integer Beta = Integer.MAX_VALUE;
	static Map <XY,Tile> tileMap = new HashMap<XY,Tile>();
	static int P;
    private long startTime;
    static final int rows = 20;
    static final int columns = 30;
    static List<Node<Tile>> visitedNodes;
    
    static Map<XY,Tile> tiles = new HashMap<XY,Tile>();
    public static void main(String args[]) {
    	  Scanner in = new Scanner(System.in);
        String currentMove = "", previousMove = "";
    	long startTime = System.currentTimeMillis();
    	//code
        int board[][] = new int[rows][columns];
        Graph graphs = parseIntoTiles(board);
        long endTime = System.currentTimeMillis();
    	System.err.println("Took "+(endTime - startTime) + " ms");
    	List<XY> opponentsLocations = new ArrayList<XY>();
        while (true) {
            int N = in.nextInt(); // total number of players (2 to 4).
            P = in.nextInt(); // your player number (0 to 3).
            int currentRow =0;
            int currentColumn = 0; 
            for (int i = 0; i < N; i++) {
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                if(X0!=-1){
                	board[Y1][X1] = i+1;
                    Optional<Node> beforeUpdatedNode =  graphs.getNode(new Node<Tile>(new Tile(Y1,X1,"0")));
                    Node<Tile> converted = ((Node<Tile>) beforeUpdatedNode.get());
                    converted.setData(new Tile(Y1,X1,(i+1)+""));
                    
                  //((MiniMaxState) graph.getNode(source).get().getData()).setValue(((MiniMaxState)bestValue.getData()).getValue());
                    if(i == P){ 
                        System.err.println(X0+"-"+Y0+"-"+X1+"-"+Y1);
                        currentRow = Y1;
                        currentColumn = X1;
                    }
                    else{
                    	opponentsLocations.add(new XY(Y1, X1));
                    }
                }
            }
            String initialState = currentRow+"+"+currentColumn;
        	for(XY eachOpponentLocation : opponentsLocations){
        	initialState += "#" + eachOpponentLocation.getX() + "+" + eachOpponentLocation.getY();
        	}
            if(EnemyIsInRadius(new XY(currentRow,currentColumn),opponentsLocations,7)){
            	Graph minMaxGraph = new Graph();
            	minMaxGraph = buildGraph(board,currentColumn,currentRow,opponentsLocations);
            	Node best = getBestMove(minMaxGraph, new Node<MiniMaxState>(new MiniMaxState(board, 0 , initialState)), 2, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            	int rowInitial = Integer.parseInt(initialState.split("#")[P].split("\\+")[0]);
            	int columnInitial = Integer.parseInt(initialState.split("#")[P].split("\\+")[1]);
            	int row = Integer.parseInt(((MiniMaxState) best.getData()).getMoves().split("#")[P].split("\\+")[0]);
            	int column = Integer.parseInt(((MiniMaxState) best.getData()).getMoves().split("#")[P].split("\\+")[1]);
            	currentMove = getMove(rowInitial,columnInitial,row,column);
            }
            else{

                Optional<Node> currentNode = graphs.getNode(new Node<Tile>(new Tile(currentRow,currentColumn,(P+1)+"")));
		        Node currentNodeOfPlayer = (Node) currentNode.get();
		        Node<Tile> longestPathNode = null;
		        int max = 0;
		        for(Node<Tile> eachNode : graphs.neighbors(new Node<Tile>(new Tile(currentRow,currentColumn,(P+1)+"")))){
		        	visitedNodes = new ArrayList<Node<Tile>>();
		        	int lengthOfTheChildNode = getLongestPathNode(graphs,eachNode);
		        	if(max < lengthOfTheChildNode){
		                    max = lengthOfTheChildNode;
		                    longestPathNode = eachNode;
		        	}
		        }
		        currentMove = getMove(currentRow,currentColumn,longestPathNode.getData().getX(),longestPathNode.getData().getY());
            }
            
            //debugBoard(board);
            if(currentMove.equals("")){
            	currentMove = avoidBlock(board, currentRow+"+"+currentColumn,previousMove);
            	System.err.println("ran by simple avoid block");
            }
            
            System.out.println(currentMove);
            previousMove = currentMove;
            opponentsLocations =  new ArrayList<XY>();
        }
    }
    private static String getMove(int rowInitial, int columnInitial, int row, int column) {
		// TODO Auto-generated method stub
    	if(row < rowInitial ){
            return "UP";
          }
	    else if(row > rowInitial ){
	              return "DOWN";
	      }
	    else if(column < columnInitial ){
	                  return "LEFT";
	      }
	    else if (column > columnInitial ){
	            return "RIGHT";
	}
	    else{
	            return "";
	      }
	}
	private static int getLongestPathNode(Graph graphs, Node<Tile> eachNode) {
		// TODO Auto-generated method stub
    	visitedNodes.add(eachNode);
        if(! hasUnvisitedNode(graphs.neighbors(eachNode))){
        	return 0;
        }else{
        	int maxDepth = 0;

        	for(Node<Tile> child : graphs.neighbors(eachNode)){
        		if(!visitedNodes.contains(child) && child.getData().getType() == "0" ){
        			maxDepth = Math.max(maxDepth, getLongestPathNode(graphs,child));
                }
        	}
        	return maxDepth + 1;
        }
	}
	private static boolean hasUnvisitedNode(List<Node> neighbors) {
		// TODO Auto-generated method stub
		for(Node<Tile> eachNode : neighbors){
            if(! visitedNodes.contains(eachNode)){
                    return true;
                }
         }
       return false;
	}
	private static boolean EnemyIsInRadius(XY xy, List<XY> opponentsLocations, int i) {
		// TODO Auto-generated method stub
		for(XY eachOpponentLocation : opponentsLocations){
		    int dx = Math.abs(xy.getX() - eachOpponentLocation.getX());
		    int dy = Math.abs(xy.getY() - eachOpponentLocation.getY());
		    if((dx + dy) < 12 ){
		    	return true;
	        }
	    }
	    return false;
	}
	private static Graph parseIntoTiles(int[][] board) {
		// TODO Auto-generated method stub
		Graph graph = new Graph();
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board[0].length;j++){
				tiles.put(new XY(i,j), new Tile(i,j,"0"));
				graph.addNode(new Node<Tile>(new Tile(i,j,"0")));
			}
		}
		for(Map.Entry<XY, Tile> eachTile:tiles.entrySet()){
    		Tile north = getTileInDirection(eachTile.getValue(), 'N');
    		Tile east = getTileInDirection(eachTile.getValue(), 'E');
    		Tile west = getTileInDirection(eachTile.getValue(), 'W');
    		Tile south = getTileInDirection(eachTile.getValue(), 'S');
    		
    		createEdge(graph,eachTile.getValue(),north);
    		createEdge(graph,eachTile.getValue(),east);
    		createEdge(graph,eachTile.getValue(),west);
    		createEdge(graph,eachTile.getValue(),south);
    	}
        return graph;
	}
    private static void createEdge(Graph graph,Tile eachTile ,Tile tile) {
		// TODO Auto-generated method stub
    	if(tile!=null){
			graph.addEdge(new Edge(new Node<Tile>(eachTile),new Node<Tile>(tile),1));
		}
	}

	private static Tile getTileInDirection(Tile eachTile,Character direction) {
		// TODO Auto-generated method stub
    	Tile tile = null;
    	switch(direction){
    	case 'N':
    		tile = getTile(eachTile.getX(),eachTile.getY()-1);
    		break;
    	case 'E':
    		tile = getTile(eachTile.getX()+1,eachTile.getY());
    		break;
    	case 'S':
    		tile = getTile(eachTile.getX(),eachTile.getY()+1);
    		break;
    	case 'W':
    		tile = getTile(eachTile.getX()-1,eachTile.getY());
    		break;
    	}
		return tile;
	}

	private static Tile getTile(int x, int y) {
		XY xy = new XY(x,y);
		return tiles.get(xy);
	}


	private static String avoidBlock(int[][] board, String currentXY,String previousMove) {
	// TODO Auto-generated method stub
		return getPossibleMoves(board,currentXY);
	}
    
    private static String getPossibleMoves(int[][] board, String currentXY) {
        HashMap<String,String> possibleMoves = new HashMap<String, String>();
    	String XY[] = currentXY.split("\\+");
    	int row = Integer.parseInt(XY[0]);
    	int column = Integer.parseInt(XY[1]);
    	
    	if(row + 1 < rows && board[row +1][column] < 1){
    	possibleMoves.put("DOWN",(row+1)+"+"+column);
		}
		if((row -1 < rows && row -1 >= 0) && board[row  - 1][column] < 1){
		possibleMoves.put("UP",(row-1)+"+"+column);
		}
		if(column + 1 < columns && board[row][column + 1] < 1){
		possibleMoves.put("RIGHT",row+"+"+(column+1));
		}
		if((column - 1 < columns && column - 1 >= 0) && board[row][column - 1] < 1){
		possibleMoves.put("LEFT",row+"+"+(column-1));
		}
		String result = possibleMoves.keySet().iterator().next();
		System.err.println(result);
		return result;
    }
    
	public static int[][] deepCopyIntMatrix(int[][] input) {
	    if (input == null)
	        return null;
	    int[][] result = new int[input.length][];
	    for (int r = 0; r < input.length; r++) {
	        result[r] = input[r].clone();
	    }
	    return result;
	}
    private static Graph buildGraph(int[][] board, int currentColumn, int currentRow,
	List<XY> opponentsLocations) {
    	Graph graphMinMax = new Graph();
    	   String initialState = currentRow+"+"+currentColumn;
    	 for(XY eachOpponentLocation : opponentsLocations){
    	         initialState += "#" + eachOpponentLocation.getX() + "+" + eachOpponentLocation.getY();
    	   }
    	     
    	   long startTime = System.currentTimeMillis();
    	       
    	       
    	       Node<MiniMaxState> start = new Node<MiniMaxState>(new MiniMaxState(board,0,initialState));
    	   Queue<Node<MiniMaxState>> frontier = new LinkedList<Node<MiniMaxState>>();
    	frontier.add(start);
    	   

    	TreeMap<String, String> possibleMoves = new TreeMap<String, String>();
    	Node<MiniMaxState> lastNode = start;
    	int level = 1;
    	   while(level > 0 && !frontier.isEmpty()){
    	     Node<MiniMaxState> currentNode = frontier.poll();
    	     if(currentNode.equals(lastNode)){
    	                 lastNode = null;
    	                       level -=1;
    	           }
    	         graphMinMax.addNode(currentNode);
    	         possibleMoves = generatePossibleMove(currentNode);
    	    graphMinMax = addPossibleStates(possibleMoves, graphMinMax,currentNode);

    	        int count = 0;
    	    
    	               for(Node<MiniMaxState> eachNode: graphMinMax.neighbors(currentNode)){
    	             frontier.add(eachNode);
    	             if(count == graphMinMax.neighbors(currentNode).size() - 1){
    	                     if(lastNode == null){
    	                         lastNode = eachNode;
    	                               }
    	                 }
    	      count += 1;
    	         }
    	     }
    	 
    	   
    	       
    	       long endTime = System.currentTimeMillis();
    	   System.err.println("Took this much for graph build"+(endTime - startTime) + " ms");



    	     return graphMinMax;
	}

	private static TreeMap<String, String> generatePossibleMove(Node<MiniMaxState> start) {
	// TODO Auto-generated method stub
	String players[] = start.getData().getMoves().split("#");
	TreeMap<String, String> possibleMoves = new TreeMap<String,String>();
	int i = 0;
        for(String eachPlayer : players){
        	String x = eachPlayer.split("\\+")[0];
        	String y = eachPlayer.split("\\+")[1];
        	if(i == 0){
        	 possibleMoves = getPossibleMovesTreeMap(start.getData().getState(), x +"+"+ y );
        	}else{
        	possibleMoves = findPossibleCombinations(possibleMoves,getPossibleMovesTreeMap(start.getData().getState(),x +"+"+ y));
        	}
        	i++;
        }
	return possibleMoves;
	}
	private static TreeMap<String,String> getPossibleMovesTreeMap(int[][] board, String currentXY) {
        TreeMap<String,String> possibleMoves = new TreeMap<String, String>();
    	String XY[] = currentXY.split("\\+");
    	int row = Integer.parseInt(XY[0]);
    	int column = Integer.parseInt(XY[1]);
    	
    	if(row + 1 < rows && board[row +1][column] < 1){
    		possibleMoves.put("DOWN",(row+1)+"+"+column);
		}
		if((row -1 < rows && row -1 >= 0) && board[row  - 1][column] < 1){
			possibleMoves.put("UP",(row-1)+"+"+column);
		}
		if(column + 1 < columns && board[row][column + 1] < 1){
			possibleMoves.put("RIGHT",row+"+"+(column+1));
		}
		if((column - 1 < columns && column - 1 >= 0) && board[row][column - 1] < 1){
			possibleMoves.put("LEFT",row+"+"+(column-1));
		}
		
		return possibleMoves;
    }
	private static Graph addPossibleStates(TreeMap<String, String> possibleMoves, Graph graphMinMax,
	Node<MiniMaxState> start) {
	int boardCopy[][];
	for(Map.Entry<String, String> entry1 :  possibleMoves.entrySet()){
        	String playerMoves[]  = entry1.getValue().split("#");
        	 boardCopy = deepCopyIntMatrix(start.getData().getState());
        	for(int i = 1; i <= playerMoves.length ; i++ ){
        	int x = Integer.parseInt(playerMoves[i-1].split("\\+")[0]);
        	int y = Integer.parseInt(playerMoves[i-1].split("\\+")[1]);
        	boardCopy[x][y] = i;
        	}
        	Node<MiniMaxState> child = new Node<MiniMaxState>(new MiniMaxState(boardCopy, 0,entry1.getValue()));
        	graphMinMax.addNode(child);
        	graphMinMax.addEdge(new Edge(start,child,1));
        }
	return graphMinMax;
	}

	private static TreeMap<String, String> findPossibleCombinations(TreeMap<String, String> possibleMoves1,
	TreeMap<String, String> possibleMoves2) {
    	TreeMap<String,String> result = new TreeMap<String, String>();
    	for(Map.Entry<String, String> entry1 : possibleMoves1.entrySet()){
    	for(Map.Entry<String, String> entry2 : possibleMoves2.entrySet()){
    	String key = entry1.getKey() + "#"+entry2.getKey();
    	String value = entry1.getValue() + "#" + entry2.getValue();
    	result.put(key, value);
        	}
    	}
    	
	return result;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Node getBestMove(Graph graph, Node<MiniMaxState> source, Integer depth, Integer alpha, Integer beta, Boolean max) {
	        // TODO: implement your alpha beta pruning algorithm here

	    	if(rootNode == null){
	    	rootNode = source;
	    	}
	    	Node bestValue;
	        if (depth == 0 || graph.neighbors(source).size() == 0) {
	        	int value = evaluateState(source);
	        	source.getData().setValue(value);
	        	return source; // return a number
	        }

	        if (max) {
	            bestValue =new Node<>(new MiniMaxState(null, Integer.MIN_VALUE,"")); // negative infinite
	            for (Node eachNode: graph.neighbors(source)) {
	                Node value = getBestMove(graph,eachNode, depth - 1, alpha, beta, !max);
	                if(alpha < ((MiniMaxState)value.getData()).getValue() ){
	       	 alpha = ((MiniMaxState)value.getData()).getValue();
	       	if(beta < alpha){
	       	bestValue = compareNodesMin(bestValue, value);
	       	break;
	       	}
	       	 	}
	                bestValue = compareNodesMax(bestValue, value);
	            }
	            if( !(((MiniMaxState) source.getData()).getMoves()).equals(((MiniMaxState) rootNode.getData()).getMoves())){
	            bestValue = new Node(new MiniMaxState( ((MiniMaxState) source.getData()).getState() , ((MiniMaxState)bestValue.getData()).getValue(), ((MiniMaxState) source.getData()).getMoves() ));
	            }
	            ((MiniMaxState) graph.getNode(source).get().getData()).setValue(((MiniMaxState)bestValue.getData()).getValue());
	            return bestValue;
	        } else {
	        	bestValue =new Node<>(new MiniMaxState(null, Integer.MAX_VALUE,"")); // positive infinite
	        	 for (Node eachNode: graph.neighbors(source)) {
	        	 Node value = getBestMove(graph,eachNode, depth - 1, alpha, beta, !max);
	        	 if(beta > ((MiniMaxState)value.getData()).getValue() ){
	        	 beta = ((MiniMaxState)value.getData()).getValue();
	        	 if(beta < alpha){
	        	 bestValue = compareNodesMin(bestValue, value);
	        	 break;
	        	 }
	        	 }
	                bestValue = compareNodesMin(bestValue, value);
	            }
	        	 if( !(((MiniMaxState) source.getData()).getMoves()).equals(((MiniMaxState) rootNode.getData()).getMoves())){
	 	            bestValue = new Node(new MiniMaxState( ((MiniMaxState) source.getData()).getState() , ((MiniMaxState)bestValue.getData()).getValue(), ((MiniMaxState) source.getData()).getMoves() ));
	 	            }
	        	  ((MiniMaxState) graph.getNode(source).get().getData()).setValue(((MiniMaxState)bestValue.getData()).getValue());
	            return bestValue;
	        }
	       
	    }
	
		// TODO Auto-generated method stub
		private static int evaluateState(Node<MiniMaxState> source) {
            
            Queue<String> frontier = new LinkedList<>();
            int playerCountCellCount,otherPlayersCellCount,wall = 9;
            playerCountCellCount = otherPlayersCellCount = 0;
              int [][] currentBoardState = deepCopyIntMatrix(source.getData().getState());
            String playerCurrentLocation[] = source.getData().getMoves().split("#");
        HashMap<String, Integer> cellWithParents = new HashMap<>();
  int i = 0;
        for(String eachPlayerLocation : playerCurrentLocation){
          if(! cellWithParents.containsKey(eachPlayerLocation)){
                        cellWithParents.put(eachPlayerLocation, i+1);
              }
              frontier.add(eachPlayerLocation);
              i++;
            }
      
            while(! frontier.isEmpty()){
                    String key = frontier.poll();
              String rowColumn[] = key.split("\\+"); 
          int x = Integer.parseInt(rowColumn[0]);
          int y = Integer.parseInt(rowColumn[1]);
          
                    if(x + 1 < rows && (currentBoardState[x + 1][y] == 0 || currentBoardState[x + 1][y] > 4 && currentBoardState[x + 1][y] <= 20)){
                            String newKey = (x+1) + "+" + y;
                            if(currentBoardState[x + 1][y] == 0){
                      cellWithParents.put(newKey, cellWithParents.get(key));
                        currentBoardState[x+1][y] = cellWithParents.get(key) + 4;
                      if(P + 1 + 4 == currentBoardState[x+1][y]){
                          playerCountCellCount += 1;
                        }else{
                                otherPlayersCellCount += 1;
                  }
                      frontier.add(newKey);
                      }else{
                                if(cellWithParents.get(newKey) != cellWithParents.get(key)){
                                            if(currentBoardState[x + 1][y] > 4 && currentBoardState[x + 1][y] != wall){
                                                if(P + 1 + 4 == currentBoardState[x + 1][y]){
                                              playerCountCellCount -= 1;
                                        }else{
                                                otherPlayersCellCount -= 1;
                                  }
                                      currentBoardState[x + 1][y] = wall;
                                  }
                              }
                      }
              }
              if(x - 1 >= 0 && (currentBoardState[x - 1][y] == 0 || currentBoardState[x - 1][y] > 4 && currentBoardState[x - 1][y] <= 20)){
                        String newKey = (x-1) + "+" + y;
                            if(currentBoardState[x - 1][y] == 0){
                      cellWithParents.put(newKey, cellWithParents.get(key));
                        currentBoardState[x-1][y] = cellWithParents.get(key) + 4;
                      if(P + 1 + 4 == currentBoardState[x-1][y]){
                          playerCountCellCount += 1;
                        }else{
                                otherPlayersCellCount += 1;
                  }
                      frontier.add(newKey);
                      }else{
                                if(cellWithParents.get(newKey) != cellWithParents.get(key)){
                                            if(currentBoardState[x - 1][y] > 4 && currentBoardState[x - 1][y] != wall){
                                                if(P + 1 + 4 == currentBoardState[x-1][y]){
                                          playerCountCellCount -= 1;
                                        }else{
                                                otherPlayersCellCount -= 1;
                                  }
                                      currentBoardState[x - 1][y] = wall;
                                  }
                              }
                      }
              }
              if(y + 1 < columns && (currentBoardState[x][y + 1] == 0 || currentBoardState[x][y + 1] > 4 && currentBoardState[x][y + 1] <= 20)){
                  String newKey = x + "+" + (y + 1);
                        if(currentBoardState[x][y+1] == 0){
                  cellWithParents.put(newKey, cellWithParents.get(key));
                        currentBoardState[x][y+1] = cellWithParents.get(key) + 4;
                      if(P + 1 + 4 == currentBoardState[x][y + 1]){
                              playerCountCellCount += 1;
                        }else{
                                otherPlayersCellCount += 1;
                  }
                      frontier.add(newKey);
                      }else{
                                if(cellWithParents.get(newKey) != cellWithParents.get(key)){
                                            if(currentBoardState[x][y + 1] > 4 && currentBoardState[x][y + 1] != wall){
                                          if(P + 1 + 4 == currentBoardState[x][y + 1]){
                                              playerCountCellCount -= 1;
                                        }else{
                                                otherPlayersCellCount -= 1;
                                  }
                                      currentBoardState[x][y + 1] = wall;
                                  }
                              }
                      }
              }
              if(y - 1 >= 0 && (currentBoardState[x][y - 1] == 0 || currentBoardState[x][y - 1] > 4 && currentBoardState[x][y - 1] <= 20)){
                        String newKey = x + "+" + (y - 1);
                        if(currentBoardState[x][y-1] == 0){
                  cellWithParents.put(newKey, cellWithParents.get(key));
                        currentBoardState[x][y-1] = cellWithParents.get(key) + 4;
                      if(P + 1 + 4 == currentBoardState[x][y - 1]){
                              playerCountCellCount += 1;
                        }else{
                                otherPlayersCellCount += 1;
                  }
                      frontier.add(newKey);
                      }else{
                                if(cellWithParents.get(newKey) != cellWithParents.get(key)){
                                            if(currentBoardState[x][y-1] > 4 && currentBoardState[x][y - 1] != wall){
                                              if(P + 1 + 4 == currentBoardState[x][y - 1]){
                                              playerCountCellCount -= 1;
                                        }else{
                                                otherPlayersCellCount -= 1;
                                  }
                                      currentBoardState[x][y-1] = wall;
                                      }
                              }
                      }
              }
      
            }
      //debugBoard(currentBoardState);
            return playerCountCellCount - otherPlayersCellCount; 
      }
	private static Node compareNodesMin(Node<MiniMaxState> bestValue, Node<MiniMaxState> value) {
		if(((MiniMaxState) bestValue.getData()).getValue() < value.getData().getValue()){
		return bestValue;
		}
		return value;
		}

		private static Node<MiniMaxState> compareNodesMax(Node<MiniMaxState> bestValue, Node<MiniMaxState> value) {
		if(( bestValue.getData()).getValue() > value.getData().getValue()){
		return bestValue;
		}
		return value;
		}
}
	class MiniMaxState {
		private int[][] state;
		private String moves;
		private int value;
		
		public MiniMaxState(int[][] state, int value, String moves) {
			this.state = state;
			this.moves = moves;
			this.value = value;
		}
		public int[][] getState() {
			return state;
		}
		public void setState(int[][] state) {
			this.state = state;
		}
		public String getMoves() {
			return moves;
		}
		public void setMoves(String moves) {
			this.moves = moves;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		@Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof MiniMaxState)) return false;

	        MiniMaxState that = (MiniMaxState) o;

	        return Arrays.deepEquals(getState(), that.getState());
	    }
	    
	    @Override
	    public int hashCode() {
	    	return Arrays.deepHashCode(getState());
	    }
		
		


	}


class Graph {
	private Map<Node<Integer>, Collection<Edge>> adjacencyList = new HashMap<Node<Integer>,Collection<Edge>>();
    Node<Integer> fromNode;
	Node<Integer> toNode;
	Edge fromEdge;
	Edge toEdge;
	public boolean adjacent(Node x, Node y) {
    	Iterator entries = adjacencyList.entrySet().iterator();
    	while(entries.hasNext())
    	{
    		Entry thisEntry = (Entry)entries.next();
    		Object key = thisEntry.getKey();
    		Object value = thisEntry.getValue();
    		Collection<Edge> edge = (Collection<Edge>)value;
    		Node currentNode = (Node)key;
    		for(Edge e:edge)
    		{
    			if((x.equals(e.getFrom()) && y.equals(e.getTo())) ||
    					(x.equals(e.getTo()) && y.equals(e.getFrom())))
    			{
    			 	return true;
    			}
    		}
    	}
        return false;
    }
    public List<Node> neighbors(Node x) {
    	Collection<Edge> edges = adjacencyList.get(x);
    	List<Node> nodes = new ArrayList<Node>();
    	if(edges!=null)
    	{
	    	for(Edge edge:edges)
	     	{
	    		nodes.add(edge.getTo());
	    	}
    	}
        return nodes;
    }
    public boolean addNode(Node x) {
    	if(adjacencyList.get(x)==null)
    	{
    		adjacencyList.put(x, null);
    		return true;
    	}
        return false;
    }
    public boolean addEdge(Edge x) {
    	Node from = x.getFrom();
    	Node to = x.getTo();
    	boolean present = false;
    	Collection<Edge> edges = new ArrayList<Edge>();
    	if(adjacencyList.containsKey(from)){
    		if(adjacencyList.get(from)!=null){
    			edges = adjacencyList.get(from);
    		}
    		if(edges != null){
		    	for(Edge edge : edges)
		    	{
		    		if(edge.getFrom().equals(from) && edge.getTo().equals(to))
		    			present = true;
		    	}
    		}
    	}
    	if(present)
    		return false;
    	else
    	{
    		edges.add(x);
    		adjacencyList.put(x.getFrom(), edges);
    		return true;
    	}  
    }

    public int distance(Node from, Node to) {
    	Collection<Edge> allEdges = adjacencyList.get(from);
    	for(Edge e:allEdges){
    		if(e.getTo().equals(to)){
    			return e.getValue();
    		}
    	}
        return 0;
    }

    public Optional<Node> getNode(Node node) {
    	Iterator iterator = adjacencyList.keySet().iterator();
        Optional<Node> result = Optional.empty();
        while (iterator.hasNext()) {
            Node next = (Node)iterator.next();
            if (next.equals(node)) {
                result = Optional.of(next);
            }
        }
        return result;
    }
}


class Edge {
    private Node from;
    private Node to;
    private int value;

    public Edge(Node from, Node to, int value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Edge)) return false;

    Edge edge = (Edge) o;

    if (getValue() != edge.getValue()) return false;
    if (getFrom() != null ? !getFrom().equals(edge.getFrom()) : edge.getFrom() != null)
    return false;
    return !(getTo() != null ? !getTo().equals(edge.getTo()) : edge.getTo() != null);

    }
}

class Tile {
    private final int x;
    private final int y;
    private final String type;

    public Tile(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return type;
    }
    @Override
    public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tile)) return false;

    Tile tile = (Tile) o;

    if (getX() != tile.getX()) return false;
    if (getY() != tile.getY()) return false;
    return getType() != null ? getType().equals(tile.getType()) : tile.getType() == null;

    }

    @Override
    public int hashCode() {
    int result = getX();
    result = 31 * result + getY();
    return result;
    }
}
class Node<T> {
    private T data;

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

	public void setData(T data) {
		this.data = data;
	}

	@Override
	  public boolean equals(Object o) {
	  if (this == o) return true;
	      if (!(o instanceof Node)) return false;

	      Node<?> node = (Node<?>) o;

	      return getData() != null ? getData().equals(node.getData()) : node.getData() == null;

	  }

	  @Override
	  public int hashCode() {
	      return getData() != null ? getData().hashCode() : 0;
	    }
    
}
class XY{
	final int x;
	final int y;
	public XY(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XY other = (XY) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}

