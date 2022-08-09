package com.bobocode.cs;

import com.bobocode.util.ExerciseNotCompletedException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * {@link RecursiveBinarySearchTree} is an implementation of a {@link BinarySearchTree} that is based on a linked nodes
 * and recursion. A tree node is represented as a nested class {@link Node}. It holds an element (a value) and
 * two references to the left and right child nodes.
 * <p><p>
 * <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com/learn">visit our website</a></strong>
 * <p>
 *
 * @param <T> a type of elements that are stored in the tree
 * @author Taras Boychuk
 * @author Maksym Stasiuk
 */
public class RecursiveBinarySearchTree<T extends Comparable<? super T>> implements BinarySearchTree<T> {

    private static class Node<T> {
        T element;
        Node<T> left;
        Node<T> right;

        public Node(T element) {
            this.element = element;
        }
    }

    private Node<T> root;
    private int depth;

    private int size;


    public static <T extends Comparable<T>> RecursiveBinarySearchTree<T> of(T... elements) {
        RecursiveBinarySearchTree<T> tree = new RecursiveBinarySearchTree<>();
        for (T element : elements) {
            tree.insert(element);
        }
        return tree;
    }

    @Override
    public boolean insert(T element) {

        Node<T> node = new Node<>(element);
        if (root == null) {
            root = node;
            size++;
            return true;
        }

        Node<T> current = root;
        while (current != null) {

            if (current.element.compareTo(element) > 0) {
                if (current.left == null) {
                    current.left = node;
                    size++;
                    return true;
                }
                current = current.left;
            } else if (current.element.compareTo(element) < 0) {
                if (current.right == null) {
                    current.right = node;
                    size++;
                    return true;
                }
                current = current.right;
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean contains(T element) {
        Objects.requireNonNull(element);

        Node<T> current = root;
        while (current != null) {
            if (current.element.compareTo(element) == 0) {
                return true;
            } else if (current.element.compareTo(element) < 0) {
                current = current.right;
            } else if (current.element.compareTo(element) > 0) {
                current = current.left;
            }
        }



        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int depth() {
        Node<T> node = root;
        depth = findDepth(node, 0);
        return depth;
    }

    private int findDepth(Node<T> node, int i) {
        if (node == null) {
            return i;
        }

        if (node.left == null && node.right == null) {
            return i;
        }

        return Math.max(findDepth(node.left, i + 1), findDepth(node.right, i + 1));
    }

    @Override
    public void inOrderTraversal(Consumer<T> consumer) {

        Stack<Node<T>> stack = new Stack<>();
        Node<T> current = root;

        while (!stack.empty() || current != null) {

            if (current != null) {
                stack.push(current);
                current = current.left;
            } else {
                Node<T> node = stack.pop();
                consumer.accept(node.element);
                current = node.right;
            }
        }
    }
}
