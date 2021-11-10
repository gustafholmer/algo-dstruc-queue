/*
 * This file should not be changed in *ANY* way except the package statement
 * that you may change to whatever suits you. Note that you should *NOT* have
 * any package statement in your code when you submit it for marking.
 *
 * The file should *NOT* be included when you send in your code. The test
 * program will supply it in the same directory as your code.
 */

import java.lang.reflect.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/**
 * This class contains JUnit 5 test cases that you can use to test your
 * implementation of the queue.
 *
 * The reason most of the test cases are commented (i.e. hidden) is that it gets
 * too messy if you try to make all of them work at the same time. A better way
 * is to make one test case work, and the uncomment the next one, leaving the
 * ones already working in place to catch any bugs in already working code that
 * might sneek in.
 *
 * When all the tests go through you will *PROBABLY* have a solution that
 * passes, i.e. if you also fulfills the requirements that can't be tested, such
 * as usage of the correct data structure, etc. Note though that the test cases
 * doesn't cover every nook and cranny, so feel free to test it even more. If we
 * find anything wrong with the code that these tests doesn't cover, then this
 * usually means a failed assignment.
 *
 * Depending on settings you may get warnings for import statements that isn't
 * used. These are used by tests that orginally are commented out, so leave the
 * import statments in place.
 *
 * @author Henrik
 */

public class ALDAQueueTest {

    private static final String A_STRING = "A";
    private static final String[] STRINGS = { "A", "B", "C", "D", "E" };

    private static final int DEFAULT_CAPACITY = 100;

    private void testField(java.lang.reflect.Field f) {
        assertTrue(java.lang.reflect.Modifier.isPrivate(f.getModifiers()),
                "All attributes should (probably) be private ");
        assertFalse(f.getType().isArray(), "There is no reason to use any arrays on this assignment");
        assertFalse(java.lang.reflect.Modifier.isStatic(f.getModifiers()),
                "There is (probably) not any reason to use any static attributes");
        for (Class<?> i : f.getType().getInterfaces()) {
            assertFalse(i.getName().startsWith("java.util"),
                    "You should implement the functionality yourself, not use any of the list implementations already available");
        }
    }

    private void testQueueProperties(ALDAQueue<?> queue, boolean empty, boolean full, int size, int totalCapacity,
                                     int currentCapacity) {
        assertEquals(empty, queue.isEmpty());
        assertEquals(full, queue.isFull());
        assertEquals(size, queue.size());
        assertEquals(totalCapacity, queue.totalCapacity());
        assertEquals(currentCapacity, queue.currentCapacity());
    }


    private void testQueueProperties(ALDAQueue<?> queue, boolean empty, boolean full, int size, int totalCapacity,
                                     int currentCapacity, String toString) {
        testQueueProperties(queue, empty, full, size, totalCapacity, currentCapacity);
        assertEquals(toString, queue.toString());
    }

    @Test
    public void testObviousImplementationErrors() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);

        for (Field f : queue.getClass().getDeclaredFields()) {
            testField(f);
        }
    }

    @Test
    public void testZeroCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MyALDAQueue<String>(0);
        });
    }

    @Test
    public void testNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MyALDAQueue<String>(-1);
        });
    }

    @Test
    public void testEmptyQueueProperties() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        testQueueProperties(queue, true, false, 0, DEFAULT_CAPACITY, DEFAULT_CAPACITY, "[]");
    }

    @Test
    public void testPeekOnEmptyQueue() {
        assertEquals(null, new MyALDAQueue<String>(DEFAULT_CAPACITY).peek());
    }

    @Test
    public void testRemoveOnEmptyQueue() {
        assertThrows(NoSuchElementException.class, () -> {
            new MyALDAQueue<Integer>(DEFAULT_CAPACITY).remove();
        });
    }

    @Test
    public void testAddingNull() {
        assertThrows(NullPointerException.class, () -> {
            new MyALDAQueue<String>(DEFAULT_CAPACITY).add(null);
        });
    }

    @Test
    public void testAddingAndRemovingOneElement() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        queue.add(A_STRING);

        testQueueProperties(queue, false, false, 1, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 1, "[" + A_STRING + "]");

        assertEquals(A_STRING, queue.peek());
        testQueueProperties(queue, false, false, 1, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 1, "[" + A_STRING + "]");

        assertEquals(A_STRING, queue.remove());
        testQueueProperties(queue, true, false, 0, DEFAULT_CAPACITY, DEFAULT_CAPACITY, "[]");
    }

    @Test
    public void testAddingAndRemovingSeveralElements() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        for (int i = 0; i < STRINGS.length; i++) {
            queue.add(STRINGS[i]);
            testQueueProperties(queue, false, false, i + 1, DEFAULT_CAPACITY, DEFAULT_CAPACITY - i - 1);
        }
        assertEquals(Arrays.toString(STRINGS), queue.toString());
        for (int i = 0; i < STRINGS.length; i++) {
            assertEquals(STRINGS[i], queue.peek());
            testQueueProperties(queue, false, false, STRINGS.length - i, DEFAULT_CAPACITY, //
                    DEFAULT_CAPACITY - STRINGS.length + i);
            assertEquals(STRINGS[i], queue.remove());
            testQueueProperties(queue, i == STRINGS.length - 1, false, STRINGS.length - i - 1, DEFAULT_CAPACITY, //
                    DEFAULT_CAPACITY - STRINGS.length + i + 1);
        }
    }

    @Test
    public void testOtherTypeOfData() {
        ALDAQueue<Integer> queue = new MyALDAQueue<Integer>(DEFAULT_CAPACITY);
        queue.add(1);
        queue.add(2);
        queue.add(3);
        testQueueProperties(queue, false, false, 3, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 3, "[1, 2, 3]");
    }

    @Test
    public void testAddingAndRemovingSeveralTimes() {
        testAddingAndRemovingSeveralElements();
        testAddingAndRemovingSeveralElements();
        testAddingAndRemovingSeveralElements();
    }

    @Test
    public void testAddingToManyElements() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(2);
        testQueueProperties(queue, true, false, 0, 2, 2, "[]");
        queue.add("A");
        queue.add("B");
        testQueueProperties(queue, false, true, 2, 2, 0, "[A, B]");

        assertThrows(IllegalStateException.class, () -> {
            queue.add("C");
        });
    }

    @Test
    public void testClear() {
        ALDAQueue<Integer> queue = new MyALDAQueue<Integer>(DEFAULT_CAPACITY);
        queue.add(1);
        queue.add(2);
        queue.add(3);
        testQueueProperties(queue, false, false, 3, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 3, "[1, 2, 3]");
        queue.clear();
        testQueueProperties(queue, true, false, 0, DEFAULT_CAPACITY, DEFAULT_CAPACITY, "[]");
        queue.add(4);
        queue.add(5);
        testQueueProperties(queue, false, false, 2, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 2, "[4, 5]");
        assertEquals(Integer.valueOf(4), queue.peek());
        assertEquals(Integer.valueOf(4), queue.remove());
        assertEquals(Integer.valueOf(5), queue.peek());
        assertEquals(Integer.valueOf(5), queue.remove());
        testQueueProperties(queue, true, false, 0, DEFAULT_CAPACITY, DEFAULT_CAPACITY, "[]");
        queue.add(6);
        queue.add(7);
        testQueueProperties(queue, false, false, 2, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 2, "[6, 7]");
        assertEquals(Integer.valueOf(6), queue.peek());
        assertEquals(Integer.valueOf(6), queue.remove());
        assertEquals(Integer.valueOf(7), queue.peek());
        assertEquals(Integer.valueOf(7), queue.remove());
        testQueueProperties(queue, true, false, 0, DEFAULT_CAPACITY, DEFAULT_CAPACITY, "[]");
    }

    @Test
    public void testDiscriminateOnEmptyQueue() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        assertEquals(0, queue.discriminate(A_STRING));
        testQueueProperties(queue, true, false, 0, DEFAULT_CAPACITY, DEFAULT_CAPACITY);
    }

    @Test
    public void testDiscriminateOnFirstElement() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        queue.add(A_STRING);
        queue.add("B");
        queue.add("C");
        assertEquals(1, queue.discriminate(A_STRING));
        testQueueProperties(queue, false, false, 3, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 3, "[B, C, A]");
    }

    @Test
    public void testDiscriminateOnMiddleElement() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        queue.add("B");
        queue.add(A_STRING);
        queue.add("C");
        assertEquals(1, queue.discriminate(A_STRING));
        testQueueProperties(queue, false, false, 3, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 3, "[B, C, A]");
    }

    @Test
    public void testDiscriminateOnLastElement() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        queue.add("B");
        queue.add("C");
        queue.add(A_STRING);
        assertEquals(1, queue.discriminate(A_STRING));
        testQueueProperties(queue, false, false, 3, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 3, "[B, C, A]");
    }

    @Test
    public void testDiscriminateWithNoMatchingElements() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        queue.add("A");
        queue.add("B");
        queue.add("C");
        assertEquals(0, queue.discriminate("D"));
        testQueueProperties(queue, false, false, 3, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 3, "[A, B, C]");
    }

    @Test
    public void testDiscriminateOnMultipleElements() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        queue.add(A_STRING);
        queue.add("B");
        queue.add(A_STRING);
        queue.add("C");
        queue.add(A_STRING);
        assertEquals(3, queue.discriminate(A_STRING));
        testQueueProperties(queue, false, false, 5, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 5, "[B, C, A, A, A]");
    }

    @Test
    public void testDiscriminateOnMultipleElementsOnly() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        for (int n = 0; n < 4; n++) {
            queue.add(new String(A_STRING));
        }
        assertEquals(4, queue.discriminate(A_STRING));
        testQueueProperties(queue, false, false, 4, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 4, "[A, A, A, A]");
    }

    @Test
    public void testDiscriminateOnMultipleElementsNearbyEachother() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        queue.add(A_STRING);
        queue.add(A_STRING);
        queue.add("B");
        queue.add(A_STRING);
        queue.add(A_STRING);
        queue.add("C");
        queue.add(A_STRING);
        queue.add(A_STRING);
        assertEquals(6, queue.discriminate(A_STRING));
        testQueueProperties(queue, false, false, 8, DEFAULT_CAPACITY, DEFAULT_CAPACITY - 8, "[B, C, A, A, A, A, A, A]");
    }

    @Test
    public void testDiscriminateNull() {
        assertThrows(NullPointerException.class, () -> {
            new MyALDAQueue<String>(DEFAULT_CAPACITY).discriminate(null);
        });
    }

    @Test
    public void testAddAll() {
        Collection<String> oracle = new LinkedList<>(Arrays.asList(STRINGS));
        Collection<String> source = new HashSet<>(oracle);

        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        queue.addAll(source);

        while (!queue.isEmpty()) {
            assertTrue(oracle.remove(queue.remove()));
        }

        assertTrue(oracle.isEmpty());
    }

    @Test
    public void testAddAllNull() {
        assertThrows(NullPointerException.class, () -> {
            new MyALDAQueue<String>(DEFAULT_CAPACITY).addAll(null);
        });
    }

    @Test
    public void testIteratorOnEmptyQueue() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        Iterator<String> iter = queue.iterator();
        assertFalse(iter.hasNext());
    }

    @Test
    public void testIteratorOnEmptyQueueMovingToFar() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        Iterator<String> iter = queue.iterator();
        assertFalse(iter.hasNext());

        assertThrows(NoSuchElementException.class, () -> {
            iter.next();
        });
    }

    @Test
    public void testIterator() {
        ALDAQueue<String> queue = new MyALDAQueue<String>(DEFAULT_CAPACITY);
        for (String s : STRINGS) {
            queue.add(s);
        }
        Iterator<String> iter = queue.iterator();
        for (String s : STRINGS) {
            assertTrue(iter.hasNext());
            assertEquals(s, iter.next());
        }
        assertFalse(iter.hasNext());

        assertThrows(NoSuchElementException.class, () -> {
            iter.next();
        });
    }

    @Test
    public void testTwoQueuesInParallel() {
        ALDAQueue<Integer> queue1 = new MyALDAQueue<>(DEFAULT_CAPACITY);
        ALDAQueue<Integer> queue2 = new MyALDAQueue<>(DEFAULT_CAPACITY);

        queue1.add(1);
        queue2.add(2);
        queue1.add(3);
        queue1.add(4);
        queue2.add(5);
        queue2.add(6);
        queue1.add(7);
        queue2.add(8);
        queue1.add(9);

        assertEquals(5, queue1.size());
        assertEquals(4, queue2.size());

        for (Integer i : queue2) {
            assertTrue(i > 1 && i < 9);
        }

        assertEquals((Integer) 1, queue1.remove());
        assertEquals((Integer) 2, queue2.remove());
        assertEquals((Integer) 3, queue1.remove());
        assertEquals((Integer) 5, queue2.remove());
        assertEquals((Integer) 6, queue2.remove());
        assertEquals((Integer) 4, queue1.remove());
        assertEquals((Integer) 7, queue1.remove());
        assertEquals((Integer) 8, queue2.remove());
        assertEquals((Integer) 9, queue1.remove());
    }

    @Test
    public void testRandomOperations() {
        Random rnd = new Random();
        final int SMALL_CAPACITY = 10;

        ALDAQueue<String> queue = new MyALDAQueue<String>(SMALL_CAPACITY);
        Queue<String> oracle = new LinkedList<>();
        for (int n = 0; n < 1000; n++) {
            switch (rnd.nextInt(15)) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:

                    if (!queue.isFull()) {
                        String str = "" + rnd.nextInt(SMALL_CAPACITY);
                        queue.add(str);
                        oracle.add(str);
                    }
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    if (!queue.isEmpty()) {
                        assertEquals(oracle.remove(), queue.remove());
                    }
                    break;
                case 10:
                    while (!queue.isFull()) {
                        String str = "" + rnd.nextInt(SMALL_CAPACITY);
                        queue.add(str);
                        oracle.add(str);
                    }
                    break;
                case 11:
                    queue.clear();
                    oracle.clear();
                    break;
                case 12:
                    if (!queue.isEmpty()) {
                        String str = "" + rnd.nextInt(SMALL_CAPACITY);
                        int count = queue.discriminate(str);
                        for (int m = 0; m < count; m++) {
                            assertTrue(oracle.remove(str));
                        }
                        for (int m = 0; m < count; m++) {
                            oracle.add(str);
                        }
                    }
                    break;
                case 13:
                case 14:
                    // Left if we need more later
            }

            testQueueProperties(queue, oracle.isEmpty(), oracle.size() == SMALL_CAPACITY, oracle.size(), SMALL_CAPACITY,
                    SMALL_CAPACITY - oracle.size(), oracle.toString());
        }

    }

}