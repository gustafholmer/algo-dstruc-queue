/*
 * This file should not be changed in *ANY* way except the package statement
 * that you may change to whatever suits you. Note that you should *NOT* have
 * any package statement in your code when you submit it for marking.
 *
 * The file should not be included when you send in your code. The test
 * program will supply it in the same directory as your code.
 */


import java.util.Collection;

/**
 * This is the list interface you should implement. It is a simplified version
 * of <a href="http://docs.oracle.com/javase/8/docs/api/java/util/Queue.html">
 * java.util.Queue</a> where you will find more documentation for most of the
 * methods.
 * <p>
 * Please note that you should implement all the functionality of the queue on
 * your own using a <em>singly</em> linked list. You may, of course, create
 * temporary linked lists in methods, but you are not allowed to use any arrays,
 * ArrayLists, LinkedLists, Vectors, or other collections anywhere in your code.
 * <p>
 * The test code assumes that the class has a single constructor that takes the
 * capacity of the queue as a parameter.
 * <p>
 * Do NOT rename this interface!
 *
 * @author Henrik
 */
public interface ALDAQueue<E> extends Iterable<E> {

    public void add(E element);

    public void addAll(Collection<? extends E> c);

    public E remove();

    public E peek();

    public void clear();

    public int size();

    public boolean isEmpty();

    public boolean isFull();

    /**
     * Set when creating the queue.
     */
    public int totalCapacity();

    public int currentCapacity();

    /**
     * Move all elements equal to e to the end of the queue.
     *
     * @param e
     * @throws NullPointerException if e is null.
     * @return the number of elements moved.
     */
    public int discriminate(E e);

}