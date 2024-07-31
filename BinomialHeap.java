import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import BinomialHeapTest.TestHeap;

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
	public int numTrees;
	public int link_counter = 0; 
	public int deleted_ranks = 0;

	/**
	 * 
	 * pre: trees degree is identical and b > a
	 *
	 * Link two binomial trees with the same degree
	 *
	 */
	public HeapNode Link(HeapNode thisNode, HeapNode otherNode) {
		this.link_counter += 1;
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
//		if (b.next != b) {
//			a.next = b.next;
//		}
		if (a.child != null) {
			b.next = a.child.next; // b next will be the smallest
			a.child.next = b; // a child will point to b
		}
		else {
			b.next = b;
			
		};//new code
		b.parent = a;
		a.child = b;
		a.next =a;
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
	public void deleteMin() {
		HeapNode minNode = this.min;
		this.deleted_ranks += minNode.rank;
		// Single root
		if (minNode == minNode.next) {
			// Single node
			if (this.size == 1) {
				this.last = null;
				this.min = null;
				this.numTrees = 0;
				this.size = 0;
				return;
			}
			// Single root with children
			else { //
				this.last = minNode.child;
				// Update parent to be null
				HeapNode newRoot = this.last;
				while (newRoot.parent != null) {
					newRoot.parent = null;
					newRoot = newRoot.next;
				}
			}
		}
		// Multiple roots
		else {
			// Disconnect min from heap
			HeapNode node = this.last;
			while (node.next != minNode) {
				node = node.next;
			}
			node.next = minNode.next;
			// Last was the minimum - change last to be previous node
			if (this.last == this.min) {
				this.last = node;
			}

			// Min node has children
			if (minNode.child != null) {
				BinomialHeap heap2 = new BinomialHeap(); // Create temp heap for min children
				heap2.last = minNode.child;
				// Update parent to be null
				HeapNode newRoot = heap2.last;
				while (newRoot.parent != null) {
					newRoot.parent = null;
					newRoot = newRoot.next;
				}
				this.meld(heap2);
				return;
			}
		}

		// Min node has no children || node is a single root
		this.size -= 1;
		this.min = this.last;
		HeapNode tempNode = this.last.next;
		this.numTrees = 1;
		while (tempNode != this.last) {
			if (tempNode.item.key < this.min.item.key) {
				this.min = tempNode;
			}
			tempNode = tempNode.next;
			this.numTrees += 1;
		}

		return;
	}

	/**
	 * 
	 * Return the minimal HeapItem, null if empty.
	 *
	 */
	public HeapItem findMin()
	{
		if (this.empty()) {
			return null;
		}
		return this.min.item; 
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
		item.key -= diff;
		HeapNode node = item.node;
		HeapItem tempitem;
		while(node.parent != null && node.item.key < node.parent.item.key) {
			// Switch keys 
			tempitem =node.item;
			node.item = node.parent.item;
			node.parent.item = tempitem;
			// Continue to node parent
			node = node.parent;
			 

		}
		
		if (this.min.item.key > item.key) {
			this.min = node;
		}
		
		
		return; 
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{    
		int diff = item.key - this.min.item.key +1;
		this.decreaseKey(item, diff);
		this.deleteMin();
		
		return; 
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public HeapNode[] createArray(HeapNode last) {
		HeapNode[] roots = new HeapNode[last.rank +1];
		HeapNode node = last.next;
		for (int i=0 ; i< roots.length; i++) {
			if (node.rank == i) {
				roots[i] = node;
				node = node.next;
			}
		}	
		return roots;	
	}
	
	public void meld(BinomialHeap heap2)
	{		
		// Empty heap
		if (this.size == 0) {
			this.last = heap2.last;
			this.last.next = this.last;
			this.min = heap2.min;
			this.size = heap2.size;
			this.numTrees = heap2.numTrees;
			
			return;
		}
		
		int minRank = Math.min(this.last.rank, heap2.last.rank);
		int maxRank = Math.max(this.last.rank, heap2.last.rank);
		HeapNode carry = null;
		HeapNode[] roots = new HeapNode[maxRank + 2];
		HeapNode[] bigHeap;
		HeapNode[] smallHeap;
		

		if (Math.max(this.last.rank, heap2.last.rank) == this.last.rank) {
			bigHeap = createArray(this.last);
			smallHeap = createArray(heap2.last);
		}
		
		else {
			bigHeap = createArray(heap2.last);
			smallHeap = createArray(this.last);
		}
		int i;
		// Iterate over ranks of minimal degree tree
		for ( i = 0; i <= minRank; i++) {
			
			if (bigHeap[i] != null || smallHeap[i]!= null) {
				// Two trees with the same degree (i)
				if (bigHeap[i] != null && smallHeap[i]!= null) {
					if (carry == null) {
						carry = Link(bigHeap[i], smallHeap[i]);
					}
					
					else { // Three trees with the same degree (i)
						if (bigHeap[i].item.key <= smallHeap[i].item.key 
								&& bigHeap[i].item.key <= carry.item.key) {
							HeapNode tmpCarry = Link(smallHeap[i], carry);
							roots[i] = bigHeap[i];
							carry = tmpCarry;
						}
						
						if (smallHeap[i].item.key <= bigHeap[i].item.key 
								&& smallHeap[i].item.key <= carry.item.key) {
							HeapNode tmpCarry = Link(bigHeap[i], carry);
							roots[i] = smallHeap[i];
							carry = tmpCarry;
						}
						
						if (carry.item.key <= bigHeap[i].item.key 
								&& carry.item.key <= smallHeap[i].item.key) {
							HeapNode tmpCarry = Link(bigHeap[i], smallHeap[i]);
							roots[i] = carry;
							carry = tmpCarry;
						}
					}
					
					
				}
				
				else {
					// One tree degree i (bigHeapPointer)
					if (bigHeap[i] != null) {
						if (carry != null) {
							carry = Link(carry, bigHeap[i]);
						}
						
						else {
							roots[i] = bigHeap[i];	
						}
					
					}
					// One tree degree i (smallHeapPointer)
					else {
						if (carry != null) {
							carry = Link(carry, smallHeap[i]);
						}
						
						else {
							roots[i] = smallHeap[i];
							}
						}
						
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
		int j = i;
		if (carry != null) {
			// while a tree with degree j exists and tree is not the last tree in stack
			while (j <bigHeap.length  && bigHeap[j] != null ) {
				
				carry = Link(bigHeap[j], carry);
				j++;
			}
			roots[j] = carry; // carry will not be used anymore
			j += 1;
		}
		
		// No more carry. Just insert the rest of big stack trees as is
		for (int k = j; k <= maxRank; k++) {
			if (bigHeap[k] != null) {
				roots[k] = bigHeap[k];
				
			}	
		}
		// Create heap
		int curr = 0;
		this.numTrees = 1;
		
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
					this.numTrees += 1;
				
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
	
//	public void meld(BinomialHeap heap2)
//	{		
//		// Empty heap
//		if (this.size == 0) {
//			this.last = heap2.last;
//			this.last.next = this.last;
//			this.min = heap2.min;
//			this.size = heap2.size;
//			this.numTrees = heap2.numTrees;
//			
//			return;
//		}
//		
//		int minRank = Math.min(this.last.rank, heap2.last.rank);
//		int maxRank = Math.max(this.last.rank, heap2.last.rank);
//		HeapNode carry = null;
//		HeapNode[] roots = new HeapNode[maxRank + 2];
//		HeapNode bigHeapPointer;
//		HeapNode smallHeapPointer;
//		HeapNode nextBigHeapPointer;
//		HeapNode nextSmallHeapPointer;
//
//		if (Math.max(this.last.rank, heap2.last.rank) == this.last.rank) {
//			bigHeapPointer = this.last.next;
//			smallHeapPointer = heap2.last.next;
//		}
//		
//		else {
//			smallHeapPointer = this.last.next;
//			bigHeapPointer = heap2.last.next;
//		}
//		
//		// Iterate over ranks of minimal degree tree
//		for (int i = 0; i <= minRank; i++) {
//			nextBigHeapPointer = bigHeapPointer.next;
//			nextSmallHeapPointer = smallHeapPointer.next;
//			if (bigHeapPointer.rank == i || smallHeapPointer.rank == i) {
//				// Two trees with the same degree (i)
//				if (bigHeapPointer.rank == smallHeapPointer.rank) {
//					if (carry == null) {
//						carry = Link(bigHeapPointer, smallHeapPointer);
//					}
//					
//					else { // Three trees with the same degree (i)
//						if (bigHeapPointer.item.key <= smallHeapPointer.item.key 
//								&& bigHeapPointer.item.key <= carry.item.key) {
//							HeapNode tmpCarry = Link(smallHeapPointer, carry);
//							roots[i] = bigHeapPointer;
//							carry = tmpCarry;
//						}
//						
//						if (smallHeapPointer.item.key <= bigHeapPointer.item.key 
//								&& smallHeapPointer.item.key <= carry.item.key) {
//							HeapNode tmpCarry = Link(bigHeapPointer, carry);
//							roots[i] = smallHeapPointer;
//							carry = tmpCarry;
//						}
//						
//						if (carry.item.key <= bigHeapPointer.item.key 
//								&& carry.item.key <= smallHeapPointer.item.key) {
//							HeapNode tmpCarry = Link(bigHeapPointer, smallHeapPointer);
//							roots[i] = carry;
//							carry = tmpCarry;
//						}
//					}
//					
//					bigHeapPointer = nextBigHeapPointer;
//					smallHeapPointer = nextSmallHeapPointer;
//				}
//				
//				else {
//					// One tree degree i (bigHeapPointer)
//					if (bigHeapPointer.rank == i) {
//						if (carry != null) {
//							carry = Link(carry, bigHeapPointer);
//						}
//						
//						else {
//							roots[i] = bigHeapPointer;	
//						}
//						bigHeapPointer = nextBigHeapPointer;
//					}
//					// One tree degree i (smallHeapPointer)
//					else {
//						if (carry != null) {
//							carry = Link(carry, smallHeapPointer);
//						}
//						
//						else {
//							roots[i] = smallHeapPointer;
//							}
//						}
//						smallHeapPointer = nextSmallHeapPointer;
//					}
//				}
//			
//			else { // Non of the trees have degree i
//				if (carry != null) {
//					roots[i] = carry;
//					carry = null;
//				}
//			}
//		}
//		
//		// Small heap is finished. insert the rest of big heap items
//		// Carry exists
//		int j = minRank + 1;
//		
//		if (carry != null) {
//			// while a tree with degree j exists and tree is not the last tree in stack
//			while (bigHeapPointer.rank == j && bigHeapPointer != bigHeapPointer.next) {
//				nextBigHeapPointer = bigHeapPointer.next;
//				carry = Link(bigHeapPointer, carry);
//				j++;
//				if (bigHeapPointer.rank < maxRank) {
//					bigHeapPointer = nextBigHeapPointer;}
//				else {break;}
//			}
//			roots[j] = carry; // carry will not be used anymore
//			j += 1;
//		}
//		
//		// No more carry. Just insert the rest of big stack trees as is
//		for (int k = j; k <= maxRank; k++) {
//			if (k == bigHeapPointer.rank) {
//				roots[k] = bigHeapPointer;
//				bigHeapPointer = bigHeapPointer.next;
//			}	
//		}
//		// Create heap
//		int curr = 0;
//		this.numTrees = 1;
//		
//		while (roots[curr] == null) {
//			curr += 1;
//		}
//		int firstNodeIndx = curr;
//		HeapNode prev = roots[curr];
//		this.size = (int) Math.pow(2 , roots[curr].rank); 
//		this.min = roots[curr];
//		this.last = prev;
//		
//		curr += 1;
//		
//			while (roots.length > curr) {
//				if (roots[curr] != null) {
//					prev.next = roots[curr];
//					prev = prev.next;
//					this.last = roots[curr];
//					this.size += (int) Math.pow(2 , roots[curr].rank);
//					this.numTrees += 1;
//				
//					if (this.min.item.key > roots[curr].item.key) {
//						this.min = roots[curr];
//					}	
//				}	
//				
//				curr += 1;
//			}
//		
//		if (roots[curr -1]!=null) {
//		roots[curr -1].next = roots[firstNodeIndx];}
//		else {roots[curr -2].next = roots[firstNodeIndx];}
//		
//		return;    		
//	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return this.size; 
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		
		return this.size==0; 
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return this.numTrees; 
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
		
		
		
		 System.out.print("first expiriment: big to small ");
		 System.out.print("\n");
		 for (int i = 1; i <= 5; i++){
			 BinomialHeap heap = new BinomialHeap();
	           
	            int n = (int)Math.pow(3, i + 7)-1;
	            long start = System.currentTimeMillis();
	            for (int j=0; j<=n-1; j++){
	                heap.insert(n-j, Integer.toString(n-j));
	            }
	            long end = System.currentTimeMillis();
	            long elapsedTime = end - start;
	            System.out.print("i =  ");
	            System.out.print(i);
	            System.out.print(", Num of Trees: ");
	            System.out.print(heap.numTrees());
	            System.out.print(", Elapsed Time (ms): ");
	            System.out.print(elapsedTime);
	            System.out.print(", num of links: ");
	            System.out.print(heap.link_counter);
	            System.out.print(", sum of ranks: ");
	            System.out.print(heap.deleted_ranks);
	            System.out.print("\n");

	        }
		 System.out.print("finished 1");
		 System.out.print("\n");
		 
		 
		 System.out.print("first expiriment - small to big: ");
		 System.out.print("\n");
		 for (int i = 1; i <= 5; i++){
			 BinomialHeap heap = new BinomialHeap();
	           
	            int n = (int)Math.pow(3, i + 7)-1;
	            long start = System.currentTimeMillis();
	            for (int j=1; j<=n; j++){
	                heap.insert(j, Integer.toString(j));
	            }
	            long end = System.currentTimeMillis();
	            long elapsedTime = end - start;
	            System.out.print("i =  ");
	            System.out.print(i);
	            System.out.print(", Num of Trees: ");
	            System.out.print(heap.numTrees());
	            System.out.print(", Elapsed Time (ms): ");
	            System.out.print(elapsedTime);
	            System.out.print(", num of links: ");
	            System.out.print(heap.link_counter);
	            System.out.print(", sum of ranks: ");
	            System.out.print(heap.deleted_ranks);
	            System.out.print("\n");

	        }
		 System.out.print("finished 1.5");
		 System.out.print("\n");
		 
		 System.out.print("seconde expiriment: ");
		 System.out.print("\n");
		 System.out.print("\n");
		 
		 for (int i = 1; i <= 5; i++){
			 BinomialHeap heap = new BinomialHeap();
	           
			 int n = (int)Math.pow(3, i + 7)-1;
	         List<Integer> nums = new ArrayList<>();

	         for (int j=1; j<=n; j++){
	             nums.add(j);
	         }

	  
	         int sumRunkDeleted = 0;
	         Collections.shuffle(nums);
	         long start = System.currentTimeMillis();
	         for (int j=0; j<=n-1; j++){
	             heap.insert(nums.get(j), Integer.toString(nums.get(j)));
	         }
	         for (int j=0; j<=n/2; j++){
	             sumRunkDeleted += heap.findMin().node.rank;
	             heap.deleteMin();
	         }

	         long end = System.currentTimeMillis();
	         long elapsedTime = end - start;
	            System.out.print("i =  ");
	            System.out.print(i);
	            System.out.print(", Num of Trees: ");
	            System.out.print(heap.numTrees());
	            System.out.print(", Elapsed Time (ms): ");
	            System.out.print(elapsedTime);
	            System.out.print(", num of links: ");
	            System.out.print(heap.link_counter);
	            System.out.print(", sum of ranks: ");
	            System.out.print(heap.deleted_ranks);
	
	            System.out.print("\n");

	        }
		 System.out.print("finished 2");
		 System.out.print("\n");
		 System.out.print("third expiriment big to small: ");
		 System.out.print("\n");
		 System.out.print("\n");
		 
		 
		 for (int i = 1; i <= 5; i++){
			 BinomialHeap heap = new BinomialHeap();
	           
			 int n = (int)Math.pow(3, i + 7)-1;

         long start = System.currentTimeMillis();
         for (int j=0; j<=n-1; j++){
             heap.insert(n-j, Integer.toString(n-j));
         }
         while (heap.size() != Math.pow(2,5) -1){
             heap.deleteMin();
         }
         long end = System.currentTimeMillis();
         long elapsedTime = end - start;
         System.out.print("i =  ");
         System.out.print(i);
         System.out.print(", Num of Trees: ");
         System.out.print(heap.numTrees());
         System.out.print(", Elapsed Time (ms): ");
         System.out.print(elapsedTime);
         System.out.print(", num of links: ");
         System.out.print(heap.link_counter);
         System.out.print(", sum of ranks: ");
         System.out.print(heap.deleted_ranks);
         System.out.print("\n");
         
		 }
		 
		 System.out.print("\n");
		 System.out.print("finished 3");
		 
		 
		 System.out.print("third expiriment  small to big: ");
		 System.out.print("\n");
		 System.out.print("\n");
		 
		 
		 for (int i = 1; i <= 5; i++){
			 BinomialHeap heap = new BinomialHeap();
	           
			 int n = (int)Math.pow(3, i + 7)-1;

         long start = System.currentTimeMillis();
         for (int j=1; j<=n; j++){
             heap.insert(j, Integer.toString(j));
         }
         while (heap.size() != Math.pow(2,5) -1){
             heap.deleteMin();
         }
         long end = System.currentTimeMillis();
         long elapsedTime = end - start;
         System.out.print("i =  ");
         System.out.print(i);
         System.out.print(", Num of Trees: ");
         System.out.print(heap.numTrees());
         System.out.print(", Elapsed Time (ms): ");
         System.out.print(elapsedTime);
         System.out.print(", num of links: ");
         System.out.print(heap.link_counter);
         System.out.print(", sum of ranks: ");
         System.out.print(heap.deleted_ranks);
         System.out.print("\n");
         
		 }
		 
		 System.out.print("\n");
		 System.out.print("finished 3.5");
		 System.out.print("\n");
		 System.out.print("finished all!");

		 
		 
		 
		 

         
         
	}}
	

