package com.bobocode.cs;

import com.bobocode.util.ExerciseNotCompletedException;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * {@link ArrayList} is an implementation of {@link List} interface. This resizable data structure
 * based on an array and is simplified version of {@link java.util.ArrayList}.
 * <p><p>
 * <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com/learn">visit our website</a></strong>
 * <p>
 *
 * @author Serhii Hryhus
 */
public class ArrayList<T> implements List<T> {

    public static final int DEFAULT_CAPACITY = 5;
    private int capacity = 5;
    private int size;
    private Object[] arr;

    /**
     * This constructor creates an instance of {@link ArrayList} with a specific capacity of an array inside.
     *
     * @param initCapacity - the initial capacity of the list
     * @throws IllegalArgumentException – if the specified initial capacity is negative or 0.
     */
    public ArrayList(int initCapacity) {
        if (initCapacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = initCapacity;
        this.arr = new Object[capacity];
    }

    /**
     * This constructor creates an instance of {@link ArrayList} with a default capacity of an array inside.
     * A default size of inner array is 5;
     */
    public ArrayList() {
        this.arr = new Object[capacity];
    }

    /**
     * Creates and returns an instance of {@link ArrayList} with provided elements
     *
     * @param elements to add
     * @return new instance
     */
    public static <T> List<T> of(T... elements) {
        List<T> list = new ArrayList<>();
        for (T element : elements) {
            list.add(element);
        }

        return list;
    }

    /**
     * Adds an element to the array.
     *
     * @param element element to add
     */
    @Override
    public void add(T element) {
        if (size >= arr.length) {
            resize();
        }

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                arr[i] = element;
                size++;
                return;
            }
        }
    }

    private void resize() {
        this.capacity = ((capacity * 3) / 2) + 1;
        Object[] newArr = new Object[capacity];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        this.arr = newArr;
    }

    /**
     * Adds an element to the specific position in the array where
     *
     * @param index   index of position
     * @param element element to add
     */
    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (size >= arr.length) {
            resize();
        }

        Object[] rightArr = new Object[arr.length - index];
        System.arraycopy(arr, index, rightArr, 0, arr.length - index);

        arr[index] = element;

        System.arraycopy(rightArr, 0, arr, index + 1, rightArr.length - 1);
        size++;
    }

    /**
     * Retrieves an element by its position index. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index index of element
     * @return en element
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtype"})
    public T get(int index) {
//        if (index < 0 || index >= size) {
//            throw new IndexOutOfBoundsException();
//        }
        // this equals to this
        Objects.checkIndex(index, size);
        return (T) arr[index];
    }

    /**
     * Returns the first element of the list. Operation is performed in constant time O(1)
     *
     * @return the first element of the list
     * @throws java.util.NoSuchElementException if list is empty
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtype"})
    public T getFirst() {
        int i = 0;
        while (i < arr.length) {
            if (arr[i] != null) {
                return (T) arr[i];
            }
            i++;
        }
        throw new NoSuchElementException();
    }

    /**
     * Returns the last element of the list. Operation is performed in constant time O(1)
     *
     * @return the last element of the list
     * @throws java.util.NoSuchElementException if list is empty
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtype"})
    public T getLast() {
        int i = arr.length - 1;
        while (i >= 0) {
            if (arr[i] != null) {
                return (T) arr[i];
            }
            i--;
        }
        throw new NoSuchElementException();
    }

    /**
     * Changes the value of array at specific position. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index   position of value
     * @param element a new value
     */
    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        arr[index] = element;
    }

    /**
     * Removes an elements by its position index. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index element index
     * @return deleted element
     */
    @Override
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        if (index < 0 || index >= arr.length) {
            throw new IndexOutOfBoundsException();
        }

        T elem = (T) arr[index];
        // index = 0
        System.arraycopy(arr, index + 1, arr, index, arr.length - index - 1);
        arr[--size] = null;
        return elem;
    }

    /**
     * Checks for existing of a specific element in the list.
     *
     * @param element is element
     * @return If element exists method returns true, otherwise it returns false
     */
    @Override
    public boolean contains(T element) {
        if (element == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (arr[i].equals(element)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a list is empty
     *
     * @return {@code true} if list is empty, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return amount of saved elements
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Removes all list elements
     */
    @Override
    public void clear() {
        arr = new Object[DEFAULT_CAPACITY];
        size = 0;
    }
}
