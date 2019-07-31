package a5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import a4.Heap;
import common.NotImplementedError;
import graph.Edge;
import graph.Node;
import graph.LabeledEdge;

/** We've provided depth-first search as an example; you need to implement Dijkstra's algorithm.
 */
public class GraphAlgorithms  {
	/** Return the Nodes reachable from start in depth-first-search order */
	public static <N extends Node<N,E>, E extends Edge<N,E>>
	List<N> dfs(N start) {
		
		Stack<N> worklist = new Stack<N>();
		worklist.add(start);
		
		Set<N>   visited  = new HashSet<N>();
		List<N>  result   = new ArrayList<N>();
		while (!worklist.isEmpty()) {
			// invariants:
			//    - everything in visited has a path from start to it
			//    - everything in worklist has a path from start to it
			//      that only traverses visited nodes
			//    - nothing in the worklist is visited
			N next = worklist.pop();
			visited.add(next);
			result.add(next);
			for (N neighbor : next.outgoing().keySet())
				if (!visited.contains(neighbor))
					worklist.add(neighbor);
		}
		return result;
	}
	
	/**
	 * Return a minimal path from start to end.  This method should return as
	 * soon as the shortest path to end is known; it should not continue to search
	 * the graph after that. 
	 * 
	 * @param <N> The type of nodes in the graph
	 * @param <E> The type of edges in the graph; the weights are given by e.label()
	 * @param start The node to search from
	 * @param end   The node to find
	 */
	public static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	List<N> shortestPath(N start, N end) {
		
		System.out.println("Start:"+start+" End:"+end);
		
		//set comparator for the smallest the number the larger the priority
		Comparator<Integer> c = new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				if(o1>o2)
					return -1;
				else if(o1<o2)
					return 1;
				else {
					return 0;
				}
			}
		};
		
		//Setting up basic variables and collection
		Heap<N, Integer> candidate = new Heap<N, Integer>(c); //stores all the neighbors available 
		ArrayList<N> visited = new ArrayList<N>(); //store the nodes that were visited
		LinkedHashMap<N, Integer> costStorage = new LinkedHashMap<N, Integer>(); //store the history of the cost when visiting each node
		HashMap<N, N> Pred=new HashMap<N, N>(); //The predecessor/parent node of a node, use to trace end to start
		ArrayList<N> result = new ArrayList<N>(); //organized list of path that is to be returned
		
		//initialize variables and collections
		N heir=start; //The current node it is on
		boolean guard = true; //runs the main loop
		costStorage.put(start, 0); //initialize the first data of the the cost Storage
		Pred.put(start, start); //initialize the first data of the Pred relation collection
		visited.add(start); //initial start is visited
		int cost=0; //the current cost of from start to the current node
		

		
		//if start is end, return the node start/end
		if(start.equals(end)) {
			guard=false;
			result.add(start);
			return result;
		}
		
		//if start and end is not connected, return empty path list
		if(!dfs(start).contains(end)) {
			guard=false;
			return result;
		}
		
		
		
		//start searching neighbors and preserve the node with the edge with least weight
		while(guard) {
			
			Iterator<? extends E> I=heir.outgoing().values().iterator();
			
			//get the last value of the costStorage collection
			for (Map.Entry<N, Integer> entry : costStorage.entrySet()) {
			    N mapKey = entry.getKey();
			    cost = entry.getValue();
			}
			
			//loop through all possible neighbors
			while(I.hasNext()) {
				E neighbor = I.next();
				int actualCost = neighbor.label()+cost;
			
//				System.out.println("new visited:"+visited);
//				System.out.println("place:"+neighbor.target());
				
				//if visited list doesn't contains neighbor(this neighbor is not visited)
				if(!visited.contains(neighbor.target())) {
					
					//if neighbor is not stored in the candidate least
					if(!candidate.toArray().contains(neighbor.target())){
						
						candidate.add(neighbor.target(), actualCost);
					}else {
						//compare with the priority of the recorded neighbor, and replace is cost to node is smaller
						
//						System.out.println("current actual cost:"+actualCost);
//						System.out.println("recorded cost:"+candidate.getPriority(neighbor.target()));
						
						if(candidate.getPriority(neighbor.target())>actualCost) {
							
							candidate.changePriority(neighbor.target(), actualCost);
						}
//						System.out.println("I am Fin: "+candidate.getPriority(neighbor.target()));
					}
					
//					System.out.println("candidate is "+candidate.toString());				
				}
			
			}
			
			//set the lowest cost node as the current node
			heir=candidate.peek();
//			System.out.println("heir:"+heir);
			
			//add new data cost Storage with cost to heir (current node)
			costStorage.put(heir, candidate.getPriority(candidate.peek()));
					
			//get the current cost to current node (heir)
			for (Map.Entry<N, Integer> entry : costStorage.entrySet()) {
				
			    N mapKey = entry.getKey();
			    cost = entry.getValue();
			}
			
			//iterator for the incoming edges of the current node(heir)
			Iterator<? extends E> O=heir.incoming().values().iterator();
			
			//find predecessor for the current node(heir)
			while(O.hasNext()) {
				
				E trace = O.next();
				
				int difference = cost-trace.label();
//				System.out.println("label is: "+trace.label());
//				System.out.println("difference is:"+difference);
//				
//				System.out.println("collection:"+costStorage);
//				
//				System.out.println("label:"+trace.label()+" of "+trace.source());
//				System.out.println("the match value:"+costStorage.get(trace.source()));
				
				//matches with data
				if(costStorage.containsValue(difference)&&visited.contains(trace.source()))
					if(costStorage.get(trace.source())==difference) {
						Pred.put(heir, trace.source());			
//						System.out.println("the source added is:"+trace.source());
					}
			}
			
//			System.out.println("predecessor collection"+Pred.toString());
			
			//add current node(heir) to visited, remove it from the candidate collection
			visited.add(candidate.poll());
			
			//break loop if ran all possible neighbors
			if(candidate==null||heir.equals(end)) {
				guard=false;
			}
		}
		
//		System.out.println("Final predecessor collection"+Pred.toString());
		
		//substitute i for heir
		N i=heir;
		
		N detect=Pred.get(i);
		
		result.add(i);
		
		//use key->value->key to organize the final pathway
		while(i!=Pred.get(i)) {
			
			result.add(detect);		
			i=detect;
			detect=Pred.get(i);
			if(i.equals(detect))
				break;
		}
		
		//reverse result pathways to start --> end
		Collections.reverse(result);
		
		return result;
	}
	
}
