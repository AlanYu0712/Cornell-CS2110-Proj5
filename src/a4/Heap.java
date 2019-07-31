package a4;

import java.util.Comparator;
import java.util.NoSuchElementException;


import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class Heap<E, P> implements PriorityQueue<E, P>{
	/** arrayList that store data for the heap */
	private ArrayList<E> HeapArray;
	
	/** comparator cmp. */
	private Comparator<P> cmp;
	
	/** HashMap that takes element(E) as the key and priority(P) as the value. */
	private HashMap<E, P> PriorityMap;
	
	/** HashMap that takes element(E) as the key and index (Integer) as the value. */
	private HashMap<E, Integer> IndexMap;
	
	/**
	 * constructor of class Heap
	 * @param c comparator to determine the order of heap
	 */
	public Heap(Comparator<P> c) {
		this.cmp = c;
		this.HeapArray = new ArrayList<E>();
		this.PriorityMap = new HashMap<E, P>();
		this.IndexMap = new HashMap<E, Integer>();
	}
	
	@Override
	/** return the provided comparator */
	public Comparator<? super P> comparator() {
		return cmp;
	}

	@Override
	/** return size of the heap */
	public int size() {
		return this.HeapArray.size();
	}

	@Override
	/** remove and return the largest from the Heap. */
	public E poll() throws NoSuchElementException {
		if (HeapArray.isEmpty()) {
			throw new NoSuchElementException();
		}
		
		// initialization
		E tmp = HeapArray.get(0);
		int last = HeapArray.size() - 1;
		int cur = 0;
				
		// remove the last leaf and put its element into root
		IndexMap.remove(HeapArray.get(cur));
		PriorityMap.remove(HeapArray.get(cur));
		
		// point index(0) to the last element in the heap
		IndexMap.replace(HeapArray.get(last), 0);
		swap(0, last);
		HeapArray.remove(HeapArray.size() - 1);
		
		// swap the "fake root" with its child until it is in the right position
		bubbleDown(cur);
		return tmp;
		
	}
	
	/**
	 * make the last leaf (which is at root position) moving downward until it reach the 
	 * right position according to priority
	 * @param cur: index of the root which is 0
	 */
	private void bubbleDown(int cur) {
		// initialize index of the maxChild
		int maxChild = 0;
		
		// invariant P: (left(root) != null and root < left(root)) or
		// 				(right(root) != null and root < right(root))
		while((left(cur) != -1) && compare(cur, left(cur)) < 0  || 
				(right(cur) != -1) && compare(cur, right(cur)) < 0 ) {
			
			// swap with left node
			if (right(cur) == -1) {
				maxChild = left(cur);
				swapIndexMap(IndexMap, cur, maxChild);
				swap(cur, maxChild);
				cur = maxChild;
			
			} else {
				// swap with the larger between left and right
				if (compare(left(cur), right(cur)) < 0) {
					maxChild = right(cur);
					swapIndexMap(IndexMap, cur, maxChild);
					swap(cur, maxChild);
					cur = maxChild;
					
				} else {
					maxChild = left(cur);
					swapIndexMap(IndexMap, cur, maxChild);
					swap(cur, maxChild);
					cur = maxChild;
					
				}
			}
		}
	}
	
	/**
	 * helper function that replace the old index with new index for the indexMap
	 * @param indexMap HashMap that contains element and its index in the arrayList
	 * @param cur current root index
	 * @param max index of its child which has larger priority
	 */
	private void swapIndexMap(HashMap<E, Integer> indexMap, int cur, int max) {
		// TODO Auto-generated method stub
		indexMap.replace(HeapArray.get(cur), max);
		indexMap.replace(HeapArray.get(max), cur);
		
	}

	@Override
	/** return the largest element from the heap. */
	public E peek() throws NoSuchElementException {
		if (HeapArray.isEmpty()) {
			throw new NoSuchElementException();
		} else {
			return HeapArray.get(0);
		}
	}

	@Override
	/** add element e to this with priority p. */
	public void add(E e, P p) throws IllegalArgumentException {
		if (PriorityMap.containsKey(e)) {
			throw new IllegalArgumentException();
		}
		
		// initialization
		int ind = size();
		HeapArray.add(e);
		PriorityMap.put(e, p);
		
		// move the new element upwards by comparing with its parent
		bubbleUp(e, ind);
	}
	
	/**
	 * given e and its index (the last element) make the node moving upwards 
	 * to its right position
	 * @param e element
	 * @param index index of e
	 */
	private void bubbleUp(E e, int index) {
		if (index == 0) {
			IndexMap.put(e, index);
			return;
		}
		if (compare(index, parent(index)) <= 0) {
			IndexMap.put(e, index);
			return;
		}
		
		// update indexMap making parent pointing to new index
		IndexMap.replace(HeapArray.get(parent(index)), index);
		// swap between current node and its parent
		swap(index, parent(index));
		// recursion
		bubbleUp(e, parent(index));
	}

	@Override
	public void changePriority(E e, P p) throws NoSuchElementException {
		if (!HeapArray.contains(e)) {
			throw new NoSuchElementException();
		}
		
		// change the priority of e with p
		PriorityMap.replace(e, p);
		
		// moving the e into right position
		bubbleUp(e, IndexMap.get(e));
		bubbleDown(IndexMap.get(e));
	}
	
	/**
	 * swap value between index i and j of a arrayList
	 * @param i: index i
	 * @param j: index j
	 */
	private void swap(int i, int j) {
		E temp = this.HeapArray.get(j);
		this.HeapArray.set(j, HeapArray.get(i));
		this.HeapArray.set(i, temp);
	}
	
	/**
	 * compare two element of index i and j according to their priority
	 * @param i
	 * @param j
	 * @return positive if HeapArray[i] larger than HeapArray[j]; 0 if HeapArray[i] equals to HeapArray[j];
	 * negative if HeapArray[i] smaller than HeapArray[j]
	 */
	private int compare(int i, int j) {
		return cmp.compare(PriorityMap.get(HeapArray.get(i)), PriorityMap.get(HeapArray.get(j)));
	}
	
	/**
	 * find the index of left child given the parent index
	 * return -1 if no child
	 * @param i: index of parent
	 * @return  index of left child from arryList
	 */
	private int left(int i) {
		if (2 * i + 1 >= HeapArray.size()) {
			return -1;
		}
		return 2 * i + 1;
	}
	
	/**
	 * find the index of right child given parent index
	 * return -1 if no child
	 * @param i index of parent
	 * @return index of right child 
	 */
	private int right(int i) {
		if (2 * i + 2 >= HeapArray.size()) {
			return -1;
		}
		return 2 * i + 2;
	}
	
	/**
	 * find index of parent given child index
	 * @param i index of the child
	 * @return index of parent
	 */
	private int parent(int i) {
		return (i - 1) / 2;
	}
	
    /**
     * Return "[s0, s1, .., sn]" where (s0, s1, .., sn) are strings representing
     * the objects contained in this, in order.  Uses toString to convert the
     * objects to strings.
     *
     * For example, if this contains 6 3 8 in that order, the result is "[6, 3, 8]".
     */
	public String toString() {
		String res = "[";
		int i = 0;
		while (i < HeapArray.size()) {
			if (i != 0) {
				res += ", ";
			}
			res += HeapArray.get(i);
			i++;
		}
		return res + "]";
	}
	public P getPriority(E e) {
		return PriorityMap.get(e);
	}
	
	/**
	 * testing if the invariant of the heap class is maintained
	 */
	public void assertInvariant() {
		for (int i = 0; i < HeapArray.size(); i++) {
			if (left(i) != -1) {
				assertTrue(compare(i, left(i)) >= 0);
			}
			if (right(i) != -1) {
				assertTrue(compare(i, right(i)) >= 0);
			}
			if (i != 0) {
				assertTrue(compare(parent(i), i) >= 0);
			}
		}
		
	}
}
