package ie.ucd.csnl.comp47500.implementation;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ie.ucd.csnl.comp47500.constant.Message;
import ie.ucd.csnl.comp47500.interfaces.Deque;

public class DequeImpl<E> implements Deque<E> {
	private Node<E> first;
	private Node<E> last;
	private int size;

	private static class Node<E> {
		E item;
		Node<E> prev;
		Node<E> next;

		Node(Node<E> prev, E element, Node<E> next) {
			this.item = element;
			this.prev = prev;
			this.next = next;
		}
	}

	public DequeImpl() {
		first = last = null;
		size = 0;
	}

	@Override
	public void addFirst(E e) {
		linkFirst(e);
	}

	@Override
	public void addLast(E e) {
		linkLast(e);
	}

	@Override
	public boolean offerFirst(E e) {
		addFirst(e);
		return true;
	}

	@Override
	public boolean offerLast(E e) {
		addLast(e);
		return true;
	}

	@Override
	public E removeFirst() {
		return unlinkFirst();
	}

	@Override
	public E removeLast() {
		return unlinkLast();
	}

	@Override
	public E pollFirst() {
		if (size == 0)
			return null;
		return unlinkFirst();
	}

	@Override
	public E pollLast() {
		if (size == 0)
			return null;
		return unlinkLast();
	}

	@Override
	public E getFirst() {
		if (size == 0)
			throw new NoSuchElementException(Message.QUEUE_EMPTY_EXCEPTION);
		return first.item;
	}

	@Override
	public E getLast() {
		if (size == 0)
			throw new NoSuchElementException(Message.QUEUE_EMPTY_EXCEPTION);
		return last.item;
	}

	@Override
	public E peekFirst() {
		return (size == 0) ? null : first.item;
	}

	@Override
	public E peekLast() {
		return (size == 0) ? null : last.item;
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null) {
					unlink(x);
					return true;
				}
			}
		} else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (o.equals(x.item)) {
					unlink(x);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean removeLastOccurrence(Object o) {
		if (o == null) {
			for (Node<E> x = last; x != null; x = x.prev) {
				if (x.item == null) {
					unlink(x);
					return true;
				}
			}
		} else {
			for (Node<E> x = last; x != null; x = x.prev) {
				if (o.equals(x.item)) {
					unlink(x);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean add(E e) {
		linkLast(e);
		return true;
	}

	@Override
	public boolean offer(E e) {
		return offerLast(e);
	}

	@Override
	public E remove() {
		return removeFirst();
	}

	@Override
	public E poll() {
		return pollFirst();
	}

	@Override
	public E element() {
		return getFirst();
	}

	@Override
	public E peek() {
		return peekFirst();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean modified = false;
		for (E e : c) {
			if (add(e)) {
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public void push(E e) {
		addFirst(e);
	}

	@Override
	public E pop() {
		return removeFirst();
	}

	@Override
	public boolean remove(Object o) {
		return removeFirstOccurrence(o);
	}

	@Override
	public boolean contains(Object o) {
		for (Node<E> x = first; x != null; x = x.next) {
			if (o == null) {
				if (x.item == null)
					return true;
			} else {
				if (o.equals(x.item))
					return true;
			}
		}
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private Node<E> current = first;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public E next() {
				if (!hasNext())
					throw new NoSuchElementException();
				E item = current.item;
				current = current.next;
				return item;
			}
		};
	}

	@Override
	public Iterator<E> descendingIterator() {
		return new Iterator<E>() {
			private Node<E> current = last;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public E next() {
				if (!hasNext())
					throw new NoSuchElementException();
				E item = current.item;
				current = current.prev;
				return item;
			}
		};
	}

	private void linkFirst(E e) {
		final Node<E> f = first;
		final Node<E> newNode = new Node<>(null, e, f);
		first = newNode;
		if (f == null)
			last = newNode;
		else
			f.prev = newNode;
		size++;
	}

	private void linkLast(E e) {
		final Node<E> l = last;
		final Node<E> newNode = new Node<>(l, e, null);
		last = newNode;
		if (l == null)
			first = newNode;
		else
			l.next = newNode;
		size++;
	}

	private E unlinkFirst() {
		final Node<E> f = first;
		if (f == null)
			throw new NoSuchElementException();
		final E element = f.item;
		final Node<E> next = f.next;
		f.item = null;
		f.next = null;
		first = next;
		if (next == null)
			last = null;
		else
			next.prev = null;
		size--;
		return element;
	}

	private E unlinkLast() {
		final Node<E> l = last;
		if (l == null)
			throw new NoSuchElementException(Message.QUEUE_EMPTY_EXCEPTION);
		final E element = l.item;
		final Node<E> prev = l.prev;
		l.item = null;
		l.prev = null;
		last = prev;
		if (prev == null)
			first = null;
		else
			prev.next = null;
		size--;
		return element;
	}

	private E unlink(Node<E> x) {
		final E element = x.item;
		final Node<E> next = x.next;
		final Node<E> prev = x.prev;

		if (prev == null) {
			first = next;
		} else {
			prev.next = next;
			x.prev = null;
		}

		if (next == null) {
			last = prev;
		} else {
			next.prev = prev;
			x.next = null;
		}

		x.item = null;
		size--;
		return element;
	}

	public boolean isEmpty() {
		return size == 0;
		
	}

}
