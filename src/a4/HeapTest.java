package a4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import java.util.Comparator;
import java.util.NoSuchElementException;

class IntComparator implements Comparator<Integer>{
	int i1;
	int i2;
	
	public IntComparator() {
	}
	
	@Override
	public int compare(Integer i, Integer j) {
		this.i1 = i;
		this.i2 = j;
		if (this.i1 > this.i2)
			return 1;
		if (this.i1 < this.i2)
			return -1;
		else
			return 0;
	}
	
	public boolean equals() {
		if (compare(this.i1, this.i2) == 0)
			return true;
		return false;
	}

}

class doubleComparator implements Comparator<Double> {
	double d1;
	double d2;
	
	@Override
	public int compare(Double i, Double j) {
		this.d1 = i;
		this.d2 = j;
		if (this.d1 > this.d2)
			return 1;
		if (this.d1 < this.d2)
			return -1;
		else
			return 0;
	}
	
	public boolean equals() {
		if (compare(this.d1, this.d2) == 0)
			return true;
		return false;
	}
	
}


class HeapTest {

	@Test
	void testAdd() {
//		Comparator<Integer> c;
//		Heap<Integer, Integer> h1 = new Heap<Integer, Integer>(c);
		Comparator<Integer> cmp = new IntComparator();
		Heap<Integer, Integer> h1 = new Heap<Integer, Integer>(cmp);
		
		h1.add(3, 3);
		h1.add(4, 4);
		h1.add(5, 5);
		h1.add(6, 6);
		
		assertEquals(4, h1.size());
		assertThrows(IllegalArgumentException.class, () -> h1.add(3, 5));
		assertEquals("[6, 5, 4, 3]", h1.toString());
		
		h1.add(2, -1);
		assertEquals("[6, 5, 4, 3, 2]", h1.toString());
		assertEquals(5, h1.size());
		
		int max = h1.poll();
		assertEquals(6, max);
		assertEquals("[5, 3, 4, 2]", h1.toString());

		int max2 = h1.poll();
		assertEquals(5, max2);
		assertEquals("[4, 3, 2]", h1.toString());
		
		h1.assertInvariant();

	}
	
	@Test
	void testPoll() {
		Comparator<Integer> cmp = new IntComparator();
		Heap<String, Integer> heap = new Heap<String, Integer>(cmp);
		
		heap.add("a", 3);
		heap.add("b", 4);
		heap.add("c", 5);
		heap.add("d", 6);
		assertEquals("[d, c, b, a]", heap.toString());
		assertEquals("d", heap.peek());
		heap.assertInvariant();
		
		assertThrows(IllegalArgumentException.class, () -> heap.add("d", 5));
		assertEquals(4, heap.size());
		
		String max = heap.poll();
		assertEquals("d", max);
		assertEquals("[c, a, b]", heap.toString());
		assertEquals("c", heap.peek());
		assertEquals(3, heap.size());

		String max2 = heap.poll();
		assertEquals("c", max2);
		assertEquals("[b, a]", heap.toString());
		assertEquals("b", heap.peek());
		assertEquals(2, heap.size());
		
		String max3 = heap.poll();
		assertEquals("b", max3);
		assertEquals("[a]", heap.toString());
		assertEquals("a", heap.peek());
		assertEquals(1, heap.size());
		
		String max4 = heap.poll();
		assertEquals("a", max4);
		assertEquals("[]", heap.toString());
		assertEquals(0, heap.size());
		heap.assertInvariant();
		
		assertThrows(NoSuchElementException.class, () -> heap.poll());
		assertThrows(NoSuchElementException.class, () -> heap.peek());
	}
	
	@Test
	void testChangePriority() {
		Comparator<Double> c = new doubleComparator();
		Heap<Double, Double> h3 = new Heap<Double, Double>(c);
		
		h3.add(3.5, 20.5);
		h3.add(4.5, 15.5);
		h3.add(5.5, 10.5);
		h3.add(6.5, 5.5);
		h3.add(7.5, 0.5);
		assertEquals("[3.5, 4.5, 5.5, 6.5, 7.5]", h3.toString());
		assertEquals(3.5, h3.peek());
		assertEquals(5, h3.size());
	
		h3.changePriority(3.5, 0.2);
		assertEquals("[4.5, 6.5, 5.5, 3.5, 7.5]", h3.toString());
		assertEquals(4.5, h3.peek());
		assertEquals(5, h3.size());
		
		h3.changePriority(7.5, 30.0);
		assertEquals("[7.5, 4.5, 5.5, 3.5, 6.5]", h3.toString());
		assertEquals(7.5, h3.peek());
		assertEquals(5, h3.size());
		
		h3.changePriority(6.5, 25.0);
		assertEquals("[7.5, 6.5, 5.5, 3.5, 4.5]", h3.toString());
		assertEquals(7.5, h3.peek());
		assertEquals(5, h3.size());
		h3.assertInvariant();
		
		assertThrows(NoSuchElementException.class, () -> h3.changePriority(99.9, 25.0));
		
		//second test
		Heap<Double, Double> h4 = new Heap<Double, Double>(c);
		h4.add(3.5, 20.5);
		h4.add(4.5, 10.5);
		h4.add(5.5, 15.5);
		h4.add(6.5, 3.5);
		h4.add(7.5, 5.5);
		
		assertEquals("[3.5, 4.5, 5.5, 6.5, 7.5]", h4.toString());
		assertEquals(3.5, h4.peek());
		assertEquals(5, h4.size());
		
		h4.changePriority(3.5, 0.2);
		assertEquals("[5.5, 4.5, 3.5, 6.5, 7.5]", h4.toString());
		assertEquals(5.5, h4.peek());
		
		h4.changePriority(7.5, 30.0);
		assertEquals("[7.5, 5.5, 3.5, 6.5, 4.5]", h4.toString());
		assertEquals(7.5, h4.peek());
		
		h4.changePriority(5.5, 0.02);
		assertEquals("[7.5, 4.5, 3.5, 6.5, 5.5]", h4.toString());
		h4.assertInvariant();
		
		
}
	
	
	
	
	
	
	
	
	

}
