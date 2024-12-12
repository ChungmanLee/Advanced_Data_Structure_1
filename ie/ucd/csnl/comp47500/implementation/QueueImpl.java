package ie.ucd.csnl.comp47500.implementation;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ie.ucd.csnl.comp47500.constant.Message;
import ie.ucd.csnl.comp47500.interfaces.Queue;

public class QueueImpl<E> implements Queue<E>{
	 private static class Node<E> {
	        E data;
	        Node<E> next;

	        Node(E data) {
	            this.data = data;
	            this.next = null;
	        }
	    }

	    private Node<E> front;
	    private Node<E> rear;
	    private int size;

	    public QueueImpl() {
	        this.front = null;
	        this.rear = null;
	        this.size = 0;
	    }

	    @Override
	    public boolean add(E e) {
	        if (offer(e)) {
	            return true;
	        } else {
	            throw new IllegalStateException(Message.QUEUE_FULL);
	        }
	    }

	    @Override
	    public boolean offer(E e) {
	        Node<E> newNode = new Node<>(e);
	        if (isEmpty()) {
	            front = newNode;
	        } else {
	            rear.next = newNode;
	        }
	        rear = newNode;
	        size++;
	        return true;
	    }

	    @Override
	    public E remove() {
	        if (isEmpty()) {
	            throw new NoSuchElementException(Message.QUEUE_EMPTY_EXCEPTION);
	        }
	        E element = peek();
	        front = front.next;
	        size--;
	        if (isEmpty()) {
	            rear = null;
	        }
	        return element;
	    }

	    @Override
	    public E poll() {
	        if (isEmpty()) {
	            return null;
	        }
	        return remove();
	    }

	    @Override
	    public E element() {
	        if (isEmpty()) {
	            throw new NoSuchElementException(Message.QUEUE_EMPTY_EXCEPTION);
	        }
	        return peek();
	    }

	    @Override
	    public E peek() {
	        if (isEmpty()) {
	            return null;
	        }
	        return front.data;
	    }

	    @Override
	    public Iterator<E> iterator() {
	        return new Iterator<E>() {
	            private Node<E> current = front;

	            @Override
	            public boolean hasNext() {
	                return current != null;
	            }

	            @Override
	            public E next() {
	                if (!hasNext()) {
	                    throw new NoSuchElementException();
	                }
	                E data = current.data;
	                current = current.next;
	                return data;
	            }
	        };
	    }

	    @Override
	    public boolean isEmpty() {
	        return size == 0;
	    }

	    @Override
	    public boolean contains(Object o) {
	        Node<E> current = front;
	        while (current != null) {
	            if (o == null ? current.data == null : o.equals(current.data)) {
	                return true;
	            }
	            current = current.next;
	        }
	        return false;
	    }

		@Override
		public int size() {
			return size;
		}
}
