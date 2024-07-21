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
		return; // should be replaced by student code
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
		HeapNode thisNode = this.last.next;
		HeapNode otherNode = heap2.last.next;
		
		// Iterate over ranks of minimal degree tree
		for (int i = 0; i <= Math.min(this.last.rank, heap2.last.rank); i++) {
			HeapNode carry = new HeapNode();
			HeapNode append = new HeapNode();
			
			if (thisNode.rank == i || otherNode.rank == i) {
				// Two trees with the same degree (i)
				if (thisNode.rank == otherNode.rank) {
					if (carry.rank == -1) {
						carry = Link(thisNode, otherNode);
					}
					
					else { // Three trees with the same degree (i)
						if (thisNode.item.key <= otherNode.item.key 
								&& thisNode.item.key <= carry.item.key) {
							HeapNode tmpCarry = Link(otherNode, carry);
							if (append.rank != -1) {
								append.next = thisNode;
							}
							append = thisNode;
							carry = tmpCarry;
						}
						
						if (otherNode.item.key <= thisNode.item.key 
								&& otherNode.item.key <= carry.item.key) {
							HeapNode tmpCarry = Link(thisNode, carry);
							if (append.rank != -1) {
								append.next = otherNode;
							}
							append = otherNode;
							carry = tmpCarry;
						}
						
						if (carry.item.key <= thisNode.item.key 
								&& carry.item.key <= otherNode.item.key) {
							HeapNode tmpCarry = Link(thisNode, otherNode);
							if (append.rank != -1) {
								append.next = carry;
							}
							append = carry;
							carry = tmpCarry;
						}
					}
					
					thisNode = thisNode.next;
					otherNode = otherNode.next;
				}
				else {
					// One tree degree i (thisNode)
					if (thisNode.rank == i) {
						if (carry.rank != -1) {
							carry = Link(carry, thisNode);
						}
						
						else {
							if (append.rank != -1) {
								append.next = thisNode;
							}
							append = thisNode;
						}
						thisNode = thisNode.next;
					}
					// One tree degree i (otherNode)
					else {
						if (carry.rank != -1) {
							carry = Link(carry, otherNode);
						}
						
						else {
							if (append.rank != -1) {
								append.next = otherNode;
							}
							append = otherNode;
						}
						otherNode = otherNode.next;
					}
				}
			}
			
			else { // Non of the trees have degree i
				if (carry.rank != -1) {
					if (append.rank != -1) {
						append.next = carry;
					}
					append = carry;
				}
			}
		}
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
		
		public HeapNode(HeapItem item, HeapNode next, HeapNode parent, int rank) {
			this.item = item;
			this.child = null;
			this.next = next;
			this.parent = parent;
			this.rank = rank;
		}
		
		public HeapNode() {
			this.item = null;
			this.child = null;
			this.next = null;
			this.parent = null;
			this.rank = -1;
		}
		
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public static class HeapItem{
		public HeapNode node;
		public int key;
		public String info;
		
		public HeapItem(HeapNode node, int key, String info) {
			this.node = node;
			this.key  = key;
			this.info = info;
		}
	}
}
