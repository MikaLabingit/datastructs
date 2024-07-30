



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BinomialHeapTest {

    @Test
    public void testInsertAndFindMin() {
        BinomialHeap heap = new BinomialHeap();

        heap.insert(5, "Hi");
        assertEquals(1, heap.size());
        assertEquals(5, heap.findMin().key);

        heap.insert(3, "Tomer");
        assertEquals(2, heap.size());
        assertEquals(3, heap.findMin().key);

        heap.insert(8, "Harel");
        assertEquals(3, heap.size());
        assertEquals(3, heap.findMin().key);

        // Additional insertions
        heap.insert(1, "Alice");
        assertEquals(4, heap.size());
        assertEquals(1, heap.findMin().key);

        heap.insert(7, "Bob");
        assertEquals(5, heap.size());
        assertEquals(1, heap.findMin().key);
       // PrintHeap.printHeap(heap, true);
        // Add more insert tests as needed
    }

    @Test
    public void testDeleteMin() {
        BinomialHeap heap = new BinomialHeap();

        heap.insert(5, "Hi");
        heap.insert(3, "Tomer");
        heap.insert(8, "Harel");

        assertEquals(3, heap.size());
        assertEquals(3, heap.findMin().key);

        heap.deleteMin();
        assertEquals(2, heap.size());
        assertEquals(5, heap.findMin().key);

        heap.deleteMin();
        assertEquals(1, heap.size());
        assertEquals(8, heap.findMin().key);

        heap.deleteMin();
        assertEquals(0, heap.size());
        assertEquals(null, heap.findMin()); // Assuming findMin returns null for an empty heap

        // Additional deletions
        heap.insert(2, "Charlie");
        heap.insert(6, "David");
        heap.deleteMin();
        assertEquals(1, heap.size());
        assertEquals(6, heap.findMin().key);

        heap.deleteMin();
        assertEquals(0, heap.size());
        assertEquals(null, heap.findMin()); // Assuming findMin returns null for an empty heap

        // Add more deleteMin tests as needed
    }

    // Additional test cases for edge scenarios
    @Test
    public void testMultipleInsertDelete() {
        BinomialHeap heap = new BinomialHeap();

        // Insert elements
        heap.insert(10, "A");
        heap.insert(6, "B");
        heap.insert(12, "C");
        heap.insert(4, "D");

        // Verify size and findMin
        assertEquals(4, heap.size());
        assertEquals(4, heap.findMin().key);

        // Delete the minimum
        heap.deleteMin();

        // Verify size and findMin after deletion
        assertEquals(3, heap.size());
        assertEquals(6, heap.findMin().key);

        // Insert more elements
        heap.insert(7, "E");
        heap.insert(2, "F");

        // Verify size and findMin after more insertions
        assertEquals(5, heap.size());
        assertEquals(2, heap.findMin().key);

        // Delete all elements
        heap.deleteMin();
        heap.deleteMin();
        heap.deleteMin();
        heap.deleteMin();
        heap.deleteMin();

        // Verify size and findMin for an empty heap
        assertEquals(0, heap.size());
        assertEquals(null, heap.findMin());
    }

    @Test
    public void testRandomInserts(){
        BinomialHeap heap = new BinomialHeap();
        // Create a list with numbers 1 to 100
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            list.add(i);
        }

        // Shuffle the list using Collections.shuffle
        Collections.shuffle(list);
        for (int i = 0; i < 100; i++) {
            heap.insert(list.get(i), list.get(i).toString());
        }
        for (int i = 1; i <= 100; i++) {
            assertEquals(i, heap.findMin().key);
            assertEquals(101 - i, heap.size());
            heap.deleteMin();
        }
    }
    @Test
    public void testDeleteItem(){
        BinomialHeap heap = new BinomialHeap();
        // Create a list with numbers 1 to 100
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            list.add(i);
        }
        BinomialHeap.HeapItem z = null;
        // Shuffle the list using Collections.shuffle
        Collections.shuffle(list);
        for (int i = 1; i < 100; i++) {
            if (i == 30)
            {
              z = heap.insert(list.get(i), list.get(i).toString());
            }
            else{heap.insert(list.get(i), list.get(i).toString());}
        }
        System.out.println(heap.findMin().key);
        System.out.println(heap.findMin().node);
        System.out.println(z.key);
        System.out.println(z.node);
        heap.decreaseKey(z, 1000);
        System.out.println(heap.findMin().key);
        System.out.println(heap.findMin().node);
        System.out.println(z.key);
        System.out.println(z.node);
    }

    public class TestHeap extends BinomialHeap{
        public static int counter = 0;

        public HeapNode connect_two_nodes(HeapNode node1,HeapNode node2){
            HeapNode min = node2;
            HeapNode max = node1;
            if (node1.item.key <=node2.item.key){
                min = node1;
                max = node2;
            }

            if (node1.rank!=0){
                HeapNode prevChild = min.child;
                HeapNode prevChildNext = min.child.next;
                // inserting max to the linked list
                prevChild.next = max;
                max.next = prevChildNext;

            }
            max.parent = min;
            min.child = max;
            min.rank++;
            counter++;
            return min;
        }
        public static void zero(){
            counter = 0;
        }
    }
    @Test
    public void FirstExperiment()  throws Exception{
        for (int i = 1; i < 7; i++){
            TestHeap heap = new TestHeap();
            TestHeap.zero();
            int n = (int)Math.pow(3, i + 5)-1;
            long start = System.currentTimeMillis();
            TimeUnit.SECONDS.sleep(1);
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
            System.out.print(TestHeap.counter);
            System.out.print("\n");

        }
    }
    @Test
    public void SecondExperiment()  throws Exception{
        for (int i = 1; i < 7; i++){
            TestHeap heap = new TestHeap();
            TestHeap.zero();
            int n = (int)Math.pow(3, i + 5)-1;
            List<Integer> nums = new ArrayList<>();

            for (int j=1; j<=n; j++){
                nums.add(j);
            }
            int sumRunkDeleted = 0;
            long start = System.currentTimeMillis();
            TimeUnit.SECONDS.sleep(1);
            Collections.shuffle(nums);
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
            System.out.print(TestHeap.counter);
            System.out.print(", sum of deleted ranks: ");
            System.out.print(sumRunkDeleted);
            System.out.print("\n");

        }
    }
    @Test
    public void ThirdExperiment()  throws Exception{
        for (int i = 1; i < 7; i++){
            TestHeap heap = new TestHeap();
            TestHeap.zero();
            int n = (int)Math.pow(3, i + 5)-1;

            int sumRunkDeleted = 0;
            long start = System.currentTimeMillis();
            TimeUnit.SECONDS.sleep(1);
            for (int j=0; j<=n-1; j++){
                heap.insert(n-j, Integer.toString(n-j));
            }
            while (heap.size() != Math.pow(2,5) -1){
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
            System.out.print(TestHeap.counter);
            System.out.print(", sum of deleted ranks: ");
            System.out.print(sumRunkDeleted);
            System.out.print("\n");

        }
    }
}
