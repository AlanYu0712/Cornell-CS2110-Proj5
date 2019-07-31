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
		
		System.out.println("s"+start+"N"+end);
		
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
		
		Heap<N, Integer> candidate = new Heap<N, Integer>(c);
		ArrayList<N> visited = new ArrayList<N>();
		LinkedHashMap<N, Integer> costStorage = new LinkedHashMap<N, Integer>();
		HashMap<N, N> Pred=new HashMap<N, N>();
		ArrayList<N> result = new ArrayList<N>();
		
		N heir=start;
		boolean guard = true;
		costStorage.put(start, 0);
		Pred.put(start, start);
		visited.add(start);
		int cost=0;
		

		
		if(start.equals(end)) {
			guard=false;
			result.add(start);
			return result;
		}
		
		if(!dfs(start).contains(end)) {
			guard=false;
			return result;
		}
		
		
		
//		candidate.add(heir, 0);
		
		
		
		while(guard) {
			
			Iterator<? extends E> I=heir.outgoing().values().iterator();
			
			
			for (Map.Entry<N, Integer> entry : costStorage.entrySet()) {
			    N mapKey = entry.getKey();
//			    System.out.println("1");
			    cost = entry.getValue();
			}
			
			while(I.hasNext()) {
				E neighbor = I.next();
				int actualCost = neighbor.label()+cost;

				
				System.out.println("new visited:"+visited);
				System.out.println("place:"+neighbor.target());
				if(!visited.contains(neighbor.target())) {
					System.out.println("actual:"+actualCost);
					System.out.println("ed"+candidate.getPriority(neighbor.target()));
					if(!candidate.toArray().contains(neighbor.target())){
						candidate.add(neighbor.target(), actualCost);
					}else {
						System.out.println("actual:"+actualCost);
						System.out.println("ed"+candidate.getPriority(neighbor.target()));
						if(candidate.getPriority(neighbor.target())>actualCost) {
							candidate.changePriority(neighbor.target(), actualCost);
					}

				}
					
					System.out.println("candi"+candidate.toString());
					
				}
			
			}
			
			heir=candidate.peek();
			System.out.println("heir:"+heir);
			costStorage.put(heir, candidate.getPriority(candidate.peek()));
			System.out.println(candidate.getPriority(candidate.peek()));
			
			
			
			for (Map.Entry<N, Integer> entry : costStorage.entrySet()) {
			    N mapKey = entry.getKey();
//			    System.out.println("Key = " + mapKey + ", Value = " + entry.getValue());
			    cost = entry.getValue();
			}
			System.out.println(cost);
			
			Iterator<? extends E> O=heir.incoming().values().iterator();
			while(O.hasNext()) {
				E trace = O.next();
				
				int difference = cost-trace.label();
				System.out.println("label is: "+trace.label());
				System.out.println("difference is:"+difference);
				
				System.out.println("collection:"+costStorage);
				
				if(costStorage.containsValue(difference)&&visited.contains(trace.source()))
					Pred.put(heir, trace.source());			
			}
			
			System.out.println("pred"+Pred.toString());
			
			visited.add(candidate.poll());
			System.out.println(heir);
				
			if(candidate==null||heir.equals(end)) {
				guard=false;
			}
		}
		
		System.out.println("Final p"+Pred.toString());
		
		N detect=Pred.get(heir);
		
		result.add(heir);
		
		while(heir!=Pred.get(heir)) {
			result.add(detect);		
			heir=detect;
			detect=Pred.get(heir);
//			System.out.println("finheir:"+heir);
//			System.out.println("findetec:"+detect);
			if(heir.equals(detect))
				break;
		}
		
		
		
		Collections.reverse(result);
//		result.remove(0);
		
		System.out.println(result);
		
		return result;
		
		
//		int Path_Lenght = 0;
//		Heap<N,Integer> h= new Heap<N,Integer>(c);
//		ArrayList<N> Path= new ArrayList<N>();
//		if(start.equals(end)) {
//			Path.add(start);
//			return Path;
//		}
//		h.add(start, 0);
//			while(!(start.equals(end))) {   
//			      
//				Path.add(start); 
//		
//				Iterator<? extends E> I=start.outgoing().values().iterator();
//				if(I.hasNext()==false) {
//					 ArrayList<N> a =new ArrayList<N>();
//					 return a;
//				}
//					while(I.hasNext()) {
//						E e= I.next();
//						if(e.target().equals(end)) {
//							Path.add(end);
//							return Path; 
//						}
//						try {
//						h.add(e.target(),-(e.label()+Path_Lenght));
//						}
//						catch(IllegalArgumentException i) {
//							h.changePriority(e.target(),-(e.label()+Path_Lenght) );
//						}
//					}
//				try {
//				Path_Lenght = Path_Lenght- h.getPriority(h.peek());
//				start=h.poll(); 
//				}
//				catch(NoSuchElementException e) {
//					break;
//				}
//				if(start.equals(end)) {
//					Path.add(start);
//					return Path;
//				}
//			}
//		return Path;
	}
	
}
