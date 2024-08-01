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
		heap2.numTrees =1;

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

			// update treeNum and new min for current heap, size will be changed later
			this.min =this.last;
			HeapNode temp_node = this.last;

			while (temp_node.next != this.last) {
				temp_node = temp_node.next;
				if (temp_node.item.key < this.min.item.key) {
					this.min = temp_node;
				}
			}
			this.numTrees -= 1;


			// Min node has children
			if (minNode.child != null) {
				BinomialHeap heap2 = new BinomialHeap(); // Create temp heap for min children
				heap2.last = minNode.child;
				heap2.numTrees =minNode.rank;
				heap2.size=  (int) Math.pow(2, minNode.rank) -1;
				this.size = this.size - heap2.size -1; // update original heap's size

				// Update parent to be null and find minimum
				HeapNode newRoot = heap2.last;
				heap2.min = heap2.last;
				while (newRoot.parent != null) {
					newRoot.parent = null;
					if (newRoot.item.key < heap2.min.item.key) {
						heap2.min = newRoot;
					}
					newRoot = newRoot.next;
				}
				this.meld(heap2);
				return;
			}
		}

		// Min node has no children || node is a single root with (children)
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
	public HeapNode[] createArraySmall(HeapNode last) {
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

	public HeapNode[] createArrayBig(HeapNode last,int minRank) {
		int temp_tree_num = this.numTrees;
		HeapNode node = last.next;
		int i =0;
		while (i<= last.rank && (i <= minRank || i == node.rank)) {
			if (i==node.rank) {
				node = node.next;}
			i++;
		}
		HeapNode[] roots = new HeapNode[i +1];
		for (int j=0 ; j < roots.length; j++) {
			if (node.rank == j) {

				roots[j] = node;
				temp_tree_num -= 1;
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
		HeapNode[] bigHeap;
		HeapNode[] smallHeap;
		HeapNode connectNode;
		HeapNode bigHeapLast;
		int temp_tree_num;

		if (this.last.rank > heap2.last.rank) {
			// Create big heap (contains all roots we need to handle)
			temp_tree_num = this.numTrees;
			HeapNode temp_node = this.last.next;
			int l =0;
			while (l<= this.last.rank && (l <= heap2.last.rank || l == temp_node.rank)) {
				if (l==temp_node.rank) {
					temp_node = temp_node.next;}
				l++;
			}
			bigHeap = new HeapNode[l +1];
			temp_node = this.last.next;
			for (int j=0 ; j < bigHeap.length; j++) {
				if (temp_node.rank == j) {

					bigHeap[j] = temp_node;
					temp_tree_num -= 1;
					temp_node = temp_node.next;}
			}
			connectNode = temp_node;
			bigHeapLast = this.last;

			// Create small heap
			smallHeap = createArraySmall(heap2.last);
		}

		else {
			// Create big heap (contains all roots we need to handle)
			temp_tree_num = heap2.numTrees;
			HeapNode temp_node = heap2.last.next;
			int l =0;
			while (l<= heap2.last.rank && (l <= this.last.rank || l == temp_node.rank)) {
				if (l==temp_node.rank) {
					temp_node = temp_node.next;}
				l++;
			}
			bigHeap = new HeapNode[l +1];
			temp_node = heap2.last.next;
			for (int j=0 ; j < bigHeap.length; j++) {
				if (temp_node.rank == j) {

					bigHeap[j] = temp_node;
					temp_tree_num -= 1;
					temp_node = temp_node.next;}
			}
			connectNode = temp_node;
			bigHeapLast = heap2.last;

			// Create small heap
			smallHeap = createArraySmall(this.last);
		}
		HeapNode[] roots = new HeapNode[bigHeap.length + 1];


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

		}
		// No more carry. Just insert the rest of bigHeap trees to roots
		else {
			for (int k = j; k < bigHeap.length; k++) {
				if (bigHeap[k] != null) {
					roots[k] = bigHeap[k];

				}
			}
		}

		// Create heap
		int curr = 0;

		while (roots[curr] == null) {
			curr += 1;
		}
		int firstNodeIndx = curr;
		HeapNode prev = roots[curr];
		HeapNode rootsLast = prev;
		int numTrees = 1;

		curr += 1;

		while (roots.length > curr) {
			if (roots[curr] != null) {
				prev.next = roots[curr];
				prev = prev.next;
				rootsLast = roots[curr];
				numTrees += 1;


			}
			curr += 1;
		}

		// Connect roots and the rest of the original heap
		if (temp_tree_num > 0) {
			rootsLast.next = connectNode;
			this.last = bigHeapLast;
			this.last.next = roots[firstNodeIndx];}
		else {
			rootsLast.next = roots[firstNodeIndx];
			this.last = rootsLast;

		}

		// Update heap parameters
		this.size = this.size + heap2.size;
		this.numTrees = temp_tree_num + numTrees;
		if (this.min.item.key > heap2.min.item.key) {
			this.min = heap2.min;
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
}