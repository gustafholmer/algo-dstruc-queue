// Gustaf Holmer guho0000

import java.util.Collection;
import java.util.Iterator;

public class MyALDAQueue<E> implements ALDAQueue<E> {


    private int theCurrentCapacity;
    private int theChangedCapacity;
    private int modCount = 0;

    private int originalCap;
    private int sizeOfContainer;
    private Node<E> headNode;


    private static class Node<E> {
        E data;
        Node<E> next;

        public Node(E dataIN) {
            this.data = dataIN;
        }
    }

    /*
     *
     * Constructor
     *
     */
    public MyALDAQueue(int capacity) {
        clear();

        if (capacity <= 0) {
            throw new java.lang.IllegalArgumentException(
                    "No valid capacity argument inputed");
        }
        theCurrentCapacity = theChangedCapacity = originalCap = capacity;

    }


    @Override
    public void add(E element) {


        if (element == null) {
            throw new NullPointerException(
                    "No element added");
        } else if (isFull()) {
            throw new IllegalStateException(
                    "Not enough capacity");
        }



        if (headNode == null) {
            headNode = new Node<>(element);
        } else {
            for (Node tempNode = headNode; tempNode != null; tempNode = tempNode.next) {
                if (tempNode.next == null) {
                    tempNode.next = new Node(element);
                    break;
                }


            }

        }

        sizeOfContainer++;
        theChangedCapacity--;
        modCount++;

    }


    @Override
    public void addAll(Collection<? extends E> c) {
        if (isFull()) {
            throw new IllegalStateException(
                    "Not enough capacity");
        }

        for (E objectInC : c) {
            add(objectInC);
            sizeOfContainer++;
            //theCurrentCapacity--;
        }

        modCount++;

    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException(
                    "No elements in the container");
        }
        E returnData = headNode.data;
        headNode = headNode.next;
        sizeOfContainer--;
        theChangedCapacity++;
        modCount++;

        return returnData;
    }

    @Override
    public E peek() {
        E returnData;
        if (headNode == null) {
            returnData = null;
        } else {
            returnData = headNode.data;
        }
        return returnData;
    }

    @Override
    public void clear() {
        headNode = null;
        sizeOfContainer = 0;
        theChangedCapacity = originalCap;
        theCurrentCapacity = originalCap;

        modCount++;
    }

    @Override
    public int size() {
        return sizeOfContainer;
    }

    @Override
    public boolean isEmpty() {
        return theChangedCapacity == originalCap;
    }

    @Override
    public boolean isFull() {
        return theChangedCapacity == 0;
    }

    /**
     * Set when creating the queue.
     */
    @Override
    public int totalCapacity() {
        return theCurrentCapacity;
    }

    @Override
    public int currentCapacity() {
        return theChangedCapacity;
    }

    @Override
    public String toString() {
        String outData = "";

        for (Node tempNode = headNode; tempNode != null; tempNode = tempNode.next) {
            outData += tempNode.data;
            if (tempNode.next != null) {
                outData += ", ";
            }
        }

        return "[" + outData + "]";
    }

/*
*
* method for deleting nodes without following queue removal rules.
*
 */
    private void deleteNodeAtPosition (int indexInput) {
        int iterationCount = 0;
        Node prevNode = null;

        for (Node tempNode = headNode; tempNode != null; tempNode = tempNode.next) {
            if (indexInput == 0) {
                headNode = tempNode.next;
                break;
            } else if (iterationCount == indexInput && prevNode != null) {
                prevNode.next = tempNode.next;
                break;
            }
            iterationCount++;

            prevNode = tempNode;
        }

    }


    /**
     * Move all elements equal to e to the end of the queue.
     *
     * @param e
     * @return the number of elements moved.
     * @throws NullPointerException if e is null.
     */
    @Override
    public int discriminate(E e) {

        int elementsMoved = 0;

        if (e == null) {
            throw new NullPointerException(
                    "Element is null");
        }

        int iterationCounter = -1;
        String positionElements = "";

        for (Node tempNode = headNode; tempNode != null; tempNode = tempNode.next) {
            iterationCounter++;
            if (tempNode.data == e || tempNode.data.equals(e)) {
                positionElements += iterationCounter;
            }
        }

        int indexDec = 0;
        for (int i = 0; i < positionElements.length(); i++) {

                deleteNodeAtPosition(Integer.parseInt(positionElements.substring(i, i+1))-indexDec);
                indexDec++;

            elementsMoved++;
            sizeOfContainer--;
            theChangedCapacity++;

        }
        for (int i = 0; i < elementsMoved; i++ ) {
            add(e);
        }
        return elementsMoved;
    }


    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<E> iterator() {

        return new MyALDAQueueIterator();
    }


    private class MyALDAQueueIterator implements java.util.Iterator {

        private Node<E> currentNode = headNode; // headNode.next;
        private int theExpectedModCount = modCount;

        private boolean endOfList = false;

        @Override
        public boolean hasNext() {
            if (isEmpty()) {
                endOfList = false;
            } else if (currentNode == null) {
                endOfList = false;
            } else if (currentNode.next == null) {
                endOfList = true;
            } else {
                endOfList = true;
            }
            return endOfList;
        }

        @Override
        public E next() {
            if (theExpectedModCount != modCount)
                throw new java.util.ConcurrentModificationException();

            if (!hasNext())
                throw new java.util.NoSuchElementException( );

            E dataToReturn = currentNode.data;
            currentNode = currentNode.next;

            return dataToReturn;
        }

    }

}
