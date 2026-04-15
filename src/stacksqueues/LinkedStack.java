package project20280.stacksqueues;

import project20280.interfaces.Stack;
import project20280.list.DoublyLinkedList;

public class LinkedStack<E> implements Stack<E> {

    DoublyLinkedList<E> ll;

    public static void main(String[] args) {
    }

    public LinkedStack() {
        // TODO
    }

    @Override
    public int size() {
        return ll.size();
    }

    @Override
    public boolean isEmpty() {
        return ll.isEmpty();
    }

    @Override
    public void push(E e) {
        // TODO
    }

    @Override
    public E top() {
        // TODO
        return null;
    }

    @Override
    public E pop() {
        // TODO
        return null;
    }

    public String toString() {
        return ll.toString();
    }
}
