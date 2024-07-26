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
		if (b.next != b) {
			a.next = b.next;
		}
		if (a.child != null) {
			b.next = a.child.next;
			a.child.next = b;
		}
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
		
		int minRank = Math.min(this.last.rank, heap2.last.rank);
		int maxRank = Math.max(this.last.rank, heap2.last.rank);
		HeapNode carry = null;
		HeapNode[] roots = new HeapNode[maxRank + 2];
		HeapNode bigHeapPointer;
		HeapNode smallHeapPointer;
		HeapNode nextBigHeapPointer;
		HeapNode nextSmallHeapPointer;

		if (Math.max(this.last.rank, heap2.last.rank) == this.last.rank) {
			bigHeapPointer = this.last.next;
			smallHeapPointer = heap2.last.next;
		}
		
		else {
			smallHeapPointer = this.last.next;
			bigHeapPointer = heap2.last.next;
		}
		
		// Iterate over ranks of minimal degree tree
		for (int i = 0; i <= minRank; i++) {
			nextBigHeapPointer = bigHeapPointer.next;
			nextSmallHeapPointer = smallHeapPointer.next;
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
					
					bigHeapPointer = nextBigHeapPointer;
					smallHeapPointer = nextSmallHeapPointer;
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
						bigHeapPointer = nextBigHeapPointer;
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
						smallHeapPointer = nextSmallHeapPointer;
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
		int j = minRank + 1;
		
		if (carry != null) {
			// while a tree with degree j exists and tree is not the last tree in stack
			while (bigHeapPointer.rank == j && bigHeapPointer != bigHeapPointer.next) {
				nextBigHeapPointer = bigHeapPointer.next;
				carry = Link(bigHeapPointer, carry);
				j++;
				if (bigHeapPointer.rank < maxRank) {
					bigHeapPointer = nextBigHeapPointer;}
				else {break;}
			}
			roots[j] = carry; // carry will not be used anymore
			j += 1;
		}
		
		// No more carry. Just insert the rest of big stack trees as is
		for (int k = j; k <= maxRank; k++) {
			if (k == bigHeapPointer.rank) {
				roots[k] = bigHeapPointer;
				bigHeapPointer = bigHeapPointer.next;
			}	
		}
		// Create heap
		int curr = 0;
		while (roots[curr] == null) {
			curr += 1;
		}
		int firstNodeIndx = curr;
		HeapNode prev = roots[curr];
		this.size = (int) Math.pow(2 , roots[curr].rank); 
		this.min = roots[curr];
		this.last = prev;
		
		curr += 1;
		
			while (roots.length > curr) {
				if (roots[curr] != null) {
					prev.next = roots[curr];
					prev = prev.next;
					this.last = roots[curr];
					this.size += (int) Math.pow(2 , roots[curr].rank);
				
					if (this.min.item.key > roots[curr].item.key) {
						this.min = roots[curr];
					}
				}	
				curr += 1;
			}
		
		if (roots[curr -1]!=null) {
		roots[curr -1].next = roots[firstNodeIndx];}
		else {roots[curr -2].next = roots[firstNodeIndx];}
		
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
		
	    heap.insert(3, "Mika");
	    System.out.println("Inserted: 3 Mika, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(17, "Hadas");
	    System.out.println("Inserted: 17 Hadas, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(1, "Shaked");
	    System.out.println("Inserted: 1 Shaked, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(25, "Or");
	    System.out.println("Inserted: 25 Or, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(30, "Shiran");
	    System.out.println("Inserted: 30 Shiran, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(12, "Dolev");
	    System.out.println("Inserted: 12 Dolev, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(5, "Lior");
	    System.out.println("Inserted: 5 Lior, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(21, "Dana");
	    System.out.println("Inserted: 21 Dana, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(8, "Avi");
	    System.out.println("Inserted: 8 Avi, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(19, "Noa");
	    System.out.println("Inserted: 19 Noa, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(14, "Yael");
	    System.out.println("Inserted: 14 Yael, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(6, "Roni");
	    System.out.println("Inserted: 6 Roni, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(28, "Tal");
	    System.out.println("Inserted: 28 Tal, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(4, "Gal");
	    System.out.println("Inserted: 4 Gal, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(22, "Eli");
	    System.out.println("Inserted: 22 Eli, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(11, "Nir");
	    System.out.println("Inserted: 11 Nir, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(2, "Tom");
	    System.out.println("Inserted: 2 Tom, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(9, "Gil");
	    System.out.println("Inserted: 9 Gil, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(26, "Amit");
	    System.out.println("Inserted: 26 Amit, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(15, "Guy");
	    System.out.println("Inserted: 15 Guy, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(24, "Roy");
	    System.out.println("Inserted: 24 Roy, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(7, "Ido");
	    System.out.println("Inserted: 7 Ido, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(13, "Alon");
	    System.out.println("Inserted: 13 Alon, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(16, "Bar");
	    System.out.println("Inserted: 16 Bar, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(29, "Adi");
	    System.out.println("Inserted: 29 Adi, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(0, "Shai");
	    System.out.println("Inserted: 0 Shai, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(20, "Aviad");
	    System.out.println("Inserted: 20 Aviad, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(18, "Eran");
	    System.out.println("Inserted: 18 Eran, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(27, "Lior");
	    System.out.println("Inserted: 27 Lior, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(10, "Neta");
	    System.out.println("Inserted: 10 Neta, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	    
	    heap.insert(23, "Yair");
	    System.out.println("Inserted: 23 Yair, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
	
	
		//System.out.println(heap.size + " " + heap.min.item.key + " " + heap.last.item.info);
		//System.out.println(heap.last.parent.item.key+ " " + heap.last.next.item.key + " " + heap.last.child.item.key);
		

	}
}
