package project20280.stacksqueues;

import project20280.interfaces.Queue;
import project20280.list.DoublyLinkedList;

public class LinkedQueue<E> implements Queue<E> {

    private DoublyLinkedList<E> ll;

    public static void main(String[] args) {
    }

    public LinkedQueue() {
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
    public void enqueue(E e) {
        // TODO
    }

    @Override
    public E first() {
        // TODO
        return null;
    }

    @Override
    public E dequeue() {
        // TODO
        return null;
    }

    public String toString() {
        return ll.toString();
    }
}
