/**
 * BinomialHeap
 *
 * An implementation of binomial heap over positive integers.
 *
 */
public class BinomialHeap
{
	public int size;
	public HeapNode last;
	public HeapNode min;

	/**
	 * 
	 * pre: trees degree is identical and b > a
	 *
	 * Link two binomial trees with the same degree
	 *
	 */
	public HeapNode Link(HeapNode thisNode, HeapNode otherNode) {
		if (thisNode.item.key < otherNode.item.key) {
			this.LinkHelper(thisNode, otherNode);
			thisNode.rank += 1;
			
			return thisNode;
		}
		else {
			this.LinkHelper(otherNode, thisNode);
			otherNode.rank += 1;
			
			return otherNode;
		}
	}
	
	public void LinkHelper(HeapNode a, HeapNode b) {
		b.next = a.child.next;
		a.child.next = b;
		b.parent = a;
		a.child = b;
	}
	
	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */
	public HeapItem insert(int key, String info) 
	{
		HeapItem item = new HeapItem();
		item.key = key;
		item.info = info;
		
		HeapNode node = new HeapNode();
		node.item = item;
		item.node = node;
		node.next = node;
		
		BinomialHeap heap2 = new BinomialHeap();
		heap2.last = node;
		heap2.size = 1;
		heap2.min = node;
		
		// Meld 
		this.meld(heap2);
		
		return item; 
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		return; // should be replaced by student code

	}

	/**
	 * 
	 * Return the minimal HeapItem, null if empty.
	 *
	 */
	public HeapItem findMin()
	{
		return null; // should be replaced by student code
	} 

	/**
	 * 
	 * pre: 0<diff<item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2)
	{		
		// Empty heap
		if (this.size == 0) {
			this.last = heap2.last;
			this.last.next = this.last;
			this.min = heap2.min;
			this.size = heap2.size;
			
			return;
		}
		
		HeapNode carry = null;
		HeapNode[] roots = new HeapNode[Math.max(this.last.rank, heap2.last.rank)+1];
		HeapNode bigHeapPointer;
		HeapNode smallHeapPointer;

		if (Math.max(this.last.rank, heap2.last.rank) == this.last.rank) {
			bigHeapPointer = this.last.next;
			smallHeapPointer = heap2.last.next;
		}
		
		else {
			smallHeapPointer = this.last.next;
			bigHeapPointer = heap2.last.next;
		}
		
		// Iterate over ranks of minimal degree tree
		for (int i = 0; i <= Math.min(this.last.rank, heap2.last.rank); i++) {
			if (bigHeapPointer.rank == i || smallHeapPointer.rank == i) {
				// Two trees with the same degree (i)
				if (bigHeapPointer.rank == smallHeapPointer.rank) {
					if (carry == null) {
						carry = Link(bigHeapPointer, smallHeapPointer);
					}
					
					else { // Three trees with the same degree (i)
						if (bigHeapPointer.item.key <= smallHeapPointer.item.key 
								&& bigHeapPointer.item.key <= carry.item.key) {
							HeapNode tmpCarry = Link(smallHeapPointer, carry);
							roots[i] = bigHeapPointer;
							carry = tmpCarry;
						}
						
						if (smallHeapPointer.item.key <= bigHeapPointer.item.key 
								&& smallHeapPointer.item.key <= carry.item.key) {
							HeapNode tmpCarry = Link(bigHeapPointer, carry);
							roots[i] = smallHeapPointer;
							carry = tmpCarry;
						}
						
						if (carry.item.key <= bigHeapPointer.item.key 
								&& carry.item.key <= smallHeapPointer.item.key) {
							HeapNode tmpCarry = Link(bigHeapPointer, smallHeapPointer);
							roots[i] = carry;
							carry = tmpCarry;
						}
					}
					
					bigHeapPointer = bigHeapPointer.next;
					smallHeapPointer = smallHeapPointer.next;
				}
				
				else {
					// One tree degree i (bigHeapPointer)
					if (bigHeapPointer.rank == i) {
						if (carry != null) {
							carry = Link(carry, bigHeapPointer);
						}
						
						else {
							roots[i] = bigHeapPointer;	
						}
						bigHeapPointer = bigHeapPointer.next;
					}
					// One tree degree i (smallHeapPointer)
					else {
						if (carry != null) {
							carry = Link(carry, smallHeapPointer);
						}
						
						else {
							roots[i] = smallHeapPointer;
							}
						}
						smallHeapPointer = smallHeapPointer.next;
					}
				}
			
			else { // Non of the trees have degree i
				if (carry != null) {
					roots[i] = carry;
					carry = null;
				}
			}
		}
		
		// Small heap is finished. insert the rest of big heap items
		// Carry exists
		int j = Math.min(this.last.rank, heap2.last.rank) + 1;
		
		if (carry != null) {
			// while a tree with degree j exists and tree is not the last tree in stack
			while (bigHeapPointer.rank == j ) {
				carry = Link(bigHeapPointer, carry);
				j++;
				if (bigHeapPointer.rank < Math.max(this.last.rank, heap2.last.rank)) {
					bigHeapPointer = bigHeapPointer.next;}
				else {break;}
			}
			roots[j] = carry; // carry will not be used anymore
		}
		
		// No more carry. Just insert the rest of big stack trees as is
		for (int k = j + 1; k <= Math.max(this.last.rank, heap2.last.rank); k++) {
			if (k == bigHeapPointer.rank) {
				roots[k] = bigHeapPointer;
				bigHeapPointer = bigHeapPointer.next;
			}	
		}
		
		int curr = 1;
		HeapNode prev = roots[0];
		this.size = (int) Math.pow(2 , roots[0].rank); 
		this.min = roots[0];
		
		while (roots[curr] != null) {
			prev.next = roots[curr];
			this.last = roots[curr];
			this.size += (int) Math.pow(2 , roots[curr].rank);
			
			if (this.min.item.key > roots[curr].item.key) {
				this.min = roots[curr];
			}
				
			curr += 1;
		}
		roots[curr -1].next = roots[0];
		
		return;    		
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return 42; // should be replaced by student code
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		return false; // should be replaced by student code
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return 0; // should be replaced by student code
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public static class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;
	
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public static class HeapItem{
		public HeapNode node;
		public int key;
		public String info;
		
	}
	
	public static void main(String[] args) {
		BinomialHeap heap = new BinomialHeap();
		heap.insert(0, "Mika");
		System.out.println(heap.last.next.item.info);
		heap.insert(1, "Hadas");
		System.out.println(heap.size + " " + heap.min.item.key + " " + heap.last.item.info);
	}
}
