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
//		if (b.next != b) {
//			a.next = b.next;
//		}
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
	public void deleteMin() {
		HeapNode minNode = this.min;

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
	    
		
		
		BinomialHeap heap = new BinomialHeap();
        int score = 0;

//        heap.insert(5, "Hi");
//        if (heap.size() == 1) {
//            System.out.println("Test 1 passed");
//            score += 5;
//        } else {
//            System.out.println("Test 1 failed");
//        }
//        if (heap.findMin().key == 5) {
//            System.out.println("Test 2 passed");
//            score += 5;
//        } else {
//            System.out.println("Test 2 failed");
//        }
//
//        heap.insert(3, "Tomer");
//        if (heap.size() == 2) {
//            System.out.println("Test 3 passed");
//            score += 5;
//        } else {
//            System.out.println("Test 3 failed");
//        }
//        if (heap.findMin().key == 3) {
//            System.out.println("Test 4 passed");
//            score += 5;
//        } else {
//            System.out.println("Test 4 failed");
//        }
//
//        heap.insert(8, "Harel");
//        if (heap.size() == 3) {
//            System.out.println("Test 5 passed");
//            score += 5;
//        } else {
//            System.out.println("Test 5 failed");
//        }
//        if (heap.findMin().key == 3) {
//            System.out.println("Test 6 passed");
//            score += 5;
//        } else {
//            System.out.println("Test 6 failed");
//        }
//
//        // Additional insertions
//        heap.insert(1, "Alice");
//        if (heap.size() == 4) {
//            System.out.println("Test 7 passed");
//            score += 5;
//        } else {
//            System.out.println("Test 7 failed");
//        }
//        if (heap.findMin().key == 1) {
//            System.out.println("Test 8 passed");
//            score += 5;
//        } else {
//            System.out.println("Test 8 failed");
//        }
//
//        heap.insert(7, "Bob");
//        if (heap.size() == 5) {
//            System.out.println("Test 9 passed");
//            score += 5;
//        } else {
//            System.out.println("Test 9 failed");
//        }
//        if (heap.findMin().key == 1) {
//            System.out.println("Test 10 passed");
//            score += 5;
//        } else {
//            System.out.println("Test 10 failed");
//        }
//
//        // Add more insert tests as needed
//
//        System.out.println("Total score: " + score);
//        
       // int score = 0;
        BinomialHeap binomialHeap = new BinomialHeap();
        // Test 1: Insert elements
        try {
            binomialHeap.insert(10, "Ten");
            binomialHeap.insert(4, "Four");
            binomialHeap.insert(15, "Fifteen");
            binomialHeap.insert(20, "Twenty");
            binomialHeap.insert(8, "Eight");
            System.out.println("Test 1 passed");
            score += 5;
        } catch (Exception e) {
            System.out.println("Test 1 failed");
        }

        // Test 2: Check size
        if (binomialHeap.size() == 5) {
            System.out.println("Test 2 passed");
            score += 5;
        } else {
            System.out.println("Test 2 failed");
        }

//        // Test 3: Check findMin
        if (binomialHeap.findMin().key == 4) {
            System.out.println("Test 3 passed");
            score += 5;
        } else {
            System.out.println("Test 3 failed");
        }
//
//        // Test 4: Check deleteMin
        try {
            binomialHeap.deleteMin();
            if (binomialHeap.findMin().key == 8) {
                System.out.println("Test 4 passed");
                score += 5;
            } else {
                System.out.println("Test 4 failed");
            }
        } catch (Exception e) {
            System.out.println("Test 4 failed");
        }
        
        int numTrees = binomialHeap.numTrees();
        System.out.println("Number of trees (sup 1 ) : " + numTrees);

        // Test 5: Insert more elements
        try {
            binomialHeap.insert(3, "Three");
            binomialHeap.insert(12, "Twelve");
            System.out.println("Test 5 passed");
            score += 5;
        } catch (Exception e) {
            System.out.println("Test 5 failed");
        }
         numTrees = binomialHeap.numTrees();
        System.out.println("Number of trees (sup 2 ) : " + numTrees);

        // Test 6: Check size again
        if (binomialHeap.size() == 6) {
            System.out.println("Test 6 passed");
            score += 5;
        } else {
            System.out.println("Test 6 failed");
        }

//        // Test 7: Check decreaseKey
      
        try {
            HeapItem item = binomialHeap.insert(18, "Eighteen");
            binomialHeap.decreaseKey(item, 17);
            if (binomialHeap.findMin().key == 1) {
                System.out.println("Test 7 passed");
                score += 5;
            } else {
                System.out.println("Test 7 failed");
            }
        } catch (Exception e) {
            System.out.println("Test 7e failed");
        }
         numTrees = binomialHeap.numTrees();
        System.out.println("Number of trees (sup 3 ) : " + numTrees);

       
        try {
            HeapItem item = binomialHeap.findMin();
            binomialHeap.delete(item);
            if (binomialHeap.findMin().key != 1) {
                System.out.println("Test 8 passed");
                score += 5;
            } else {
                System.out.println("Test 8 failed");
            }
        } catch (Exception e) {
            System.out.println("Test e8 failed");
        }
        numTrees = binomialHeap.numTrees();
        System.out.println("Number of trees (sup 2 ) : " + numTrees);


//        // Test 9: Check empty
        if (!binomialHeap.empty()) {
            System.out.println("Test 9 passed");
            score += 5;
        } else {
            System.out.println("Test 9 failed");
        }
//
//     // Test 10: Meld two heaps
        try {
            BinomialHeap binomialHeap2 = new BinomialHeap();
            if (binomialHeap2.empty()) {
                System.out.println("empty is working");}
            binomialHeap2.insert(5, "Five");
            binomialHeap2.deleteMin();
            if (binomialHeap2.empty()) {
            	System.out.println("empty is working again ");}
        	binomialHeap2.insert(5, "Five");
            binomialHeap2.insert(25, "Twenty-five");
            binomialHeap.meld(binomialHeap2);
            numTrees = binomialHeap.numTrees();
            System.out.println("Number of trees (sup 1 ) : " + numTrees);

            if (binomialHeap.size() == 8 && binomialHeap.findMin().key == 3) {
                System.out.println("Test 10 passed");
                score += 5;
            } else {
                System.out.println("Test 10 failed");
            }
        } catch (Exception e) {
            System.out.println("Test 10 failed");
        }
//        
//        // Test 11: Check numtrees
        try {
             numTrees = binomialHeap.numTrees();
            System.out.println("Number of trees: " + numTrees);
            score += 5; // Assume correct implementation, adjust based on actual implementation
        } catch (Exception e) {
            System.out.println("Test 11 failed");
        }

        // Test 12: Check size after meld
        if (binomialHeap.size() == 8) {
            System.out.println("Test 12 passed");
            score += 5;
        } else {
            System.out.println("Test 12 failed");
        }

//        
//        // Test 13: Insert another element
        try {
            binomialHeap.insert(1, "One");
            if (binomialHeap.findMin().key == 1) {
                System.out.println("Test 13 passed");
                score += 5;
            } else {
                System.out.println("Test 13 failed");
            }
        } catch (Exception e) {
            System.out.println("Test 13 failed");
        }
//        
//        
//        // Test 14: Check decreaseKey on non-existent item
        try {
            HeapItem fakeItem = new HeapItem();
            fakeItem.key=100;
            fakeItem.info="Fake";
            fakeItem.node = null;
            
            binomialHeap.decreaseKey(fakeItem, 10);
            System.out.println("Test 14 failed");
        } catch (Exception e) {
            System.out.println("Test 14 passed");
            score += 5;
        }

        try {
            BinomialHeap emptyHeap = new BinomialHeap();
            emptyHeap.deleteMin();
            System.out.println("Test 15 failed");
        } catch (Exception e) {
            System.out.println("Test 15 passed");
            score += 5;
        }
       
        try {
            BinomialHeap emptyHeap = new BinomialHeap();
            if (emptyHeap.findMin() == null) {
                System.out.println("Test 16 passed");
                score += 5;
            } else {
                System.out.println("Test 16 failed");
            }
        } catch (Exception e) {
            System.out.println("Test e16 failed");
        }

//     // Test 17: Insert an item, delete it, and check heap properties
        try {
            int initialSize = binomialHeap.size();
            HeapItem itemToDelete = binomialHeap.insert(18, "Eighteen");
            if (binomialHeap.size() == initialSize + 1) {
                binomialHeap.delete(itemToDelete);
                if (binomialHeap.size() == initialSize && binomialHeap.findMin().key == 1) {
                    System.out.println("Test 17 passed");
                    score += 5;
                } else {
                    System.out.println("Test 17 failed after deletion");
                }
            } else {
                System.out.println("Test 17 failed after insertion");
            }
        } catch (Exception e) {
            System.out.println("Test 17 failed");
        }

        // Test 18: Check min after multiple operations
        if (binomialHeap.findMin().key == 1) {
            System.out.println("Test 18 passed");
            score += 5;
        } else {
            System.out.println("Test 18 failed");
        }
        // Test 19: Check size after multiple operations
        if (binomialHeap.size() == 9) { // Adjust based on actual operations and expected size
            System.out.println("Test 19 passed");
            score += 5;
        } else {
            System.out.println("Test 19 failed");
        }

        // Test 20: Check empty after multiple operations
        if (!binomialHeap.empty()) {
            System.out.println("Test 20 passed");
            score += 5;
        } else {
            System.out.println("Test 20 failed");
        }

        // Print final score
        System.out.println("Final Score: " + score + "/100");
    }
    }
//	public static void main(String[] args) {
//		BinomialHeap heap = new BinomialHeap();
//		
//	    heap.insert(3, "Mika");
//	    System.out.println("Inserted: 3 Mika, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(17, "Hadas");
//	    System.out.println("Inserted: 17 Hadas, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(1, "Shaked");
//	    System.out.println("Inserted: 1 Shaked, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(25, "Or");
//	    System.out.println("Inserted: 25 Or, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(30, "Shiran");
//	    System.out.println("Inserted: 30 Shiran, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(12, "Dolev");
//	    System.out.println("Inserted: 12 Dolev, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(5, "Lior");
//	    System.out.println("Inserted: 5 Lior, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(21, "Dana");
//	    System.out.println("Inserted: 21 Dana, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(8, "Avi");
//	    System.out.println("Inserted: 8 Avi, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(19, "Noa");
//	    System.out.println("Inserted: 19 Noa, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(14, "Yael");
//	    System.out.println("Inserted: 14 Yael, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(6, "Roni");
//	    System.out.println("Inserted: 6 Roni, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(28, "Tal");
//	    System.out.println("Inserted: 28 Tal, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(4, "Gal");
//	    System.out.println("Inserted: 4 Gal, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(22, "Eli");
//	    System.out.println("Inserted: 22 Eli, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(11, "Nir");
//	    System.out.println("Inserted: 11 Nir, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(2, "Tom");
//	    System.out.println("Inserted: 2 Tom, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(9, "Gil");
//	    System.out.println("Inserted: 9 Gil, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(26, "Amit");
//	    System.out.println("Inserted: 26 Amit, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(15, "Guy");
//	    System.out.println("Inserted: 15 Guy, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(24, "Roy");
//	    System.out.println("Inserted: 24 Roy, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(7, "Ido");
//	    System.out.println("Inserted: 7 Ido, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(13, "Alon");
//	    System.out.println("Inserted: 13 Alon, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(16, "Bar");
//	    System.out.println("Inserted: 16 Bar, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(29, "Adi");
//	    System.out.println("Inserted: 29 Adi, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(0, "Shai");
//	    System.out.println("Inserted: 0 Shai, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(20, "Aviad");
//	    System.out.println("Inserted: 20 Aviad, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(18, "Eran");
//	    System.out.println("Inserted: 18 Eran, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(27, "Lior");
//	    System.out.println("Inserted: 27 Lior, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(10, "Neta");
//	    System.out.println("Inserted: 10 Neta, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	    
//	    heap.insert(23, "Yair");
//	    System.out.println("Inserted: 23 Yair, Size: " + heap.size + ", Min: " + (heap.min != null ? heap.min.item.key : "null") + ", Last: " + (heap.last != null ? heap.last.item.info : "null"));
//	
//	
//		//System.out.println(heap.size + " " + heap.min.item.key + " " + heap.last.item.info);
//		//System.out.println(heap.last.parent.item.key+ " " + heap.last.next.item.key + " " + heap.last.child.item.key);
//		
//
//	}

