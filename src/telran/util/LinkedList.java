package telran.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedList<T> extends AbstractCollection<T> implements List<T> {
	Node<T> head;
	Node<T> tail;
 public static class Node<T> {
	 T data;
	 Node<T> prev;
	 Node<T> next;
	 Node(T data) {
		 this.data = data;
	 }
 }
	@Override
	public boolean add(T obj) {
		Node<T> node = new Node<>(obj);
		addNode(size, node);
		return true;
	}

	

	

	
	private class LinkedListIterator implements Iterator<T> {
		Node<T> current = head;
		Node<T> toReturn = null;
		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			toReturn = current;
			current = current.next;
			return toReturn.data;
		}
		@Override
		public void remove() {
			if(toReturn == null) {
				throw new IllegalStateException();
			}
			removeNode(toReturn);
			toReturn = null;
		}
		
	}
	@Override
	public Iterator<T> iterator() {
		
		return new LinkedListIterator();
	}

	@Override
	public T get(int index) {
		List.checkIndex(index, size, true);
		return getNode(index).data;
	}

	@Override
	public void add(int index, T obj) {
		List.checkIndex(index, size, false);
		Node<T> node = new Node<>(obj);
		addNode(index,node);

	}

	@Override
	public T remove(int index) {
		List.checkIndex(index, size, true);
		Node<T> removed = getNode(index);
		T res = removed.data;
		removeNode(removed);
		return res;
	}

	private void removeNode(Node<T> removed) {
		if(head == removed) {
			removeHead();
		} else if (tail == removed) {
			removeTail();
		} else {
			removeMiddle(removed);
		}
		setNulls(removed);
		size--;
	}

	private void setNulls(Node<T> removed) {
		removed.next = null;
		removed.prev = null;
		removed.data = null;
		
		
	}

	private void removeMiddle(Node<T> removed) {
		Node<T> prev = removed.prev;
		Node<T> next = removed.next;
		prev.next = next;
		next.prev = prev;
		
	}

	private void removeTail() {
		tail = tail.prev;
		tail.next = null;
		
	}

	private void removeHead() {
		if(head == tail) {
			head = tail = null;
		} else {
			head = head.next;
			head.prev = null;
		}
		
	}

	@Override
	public int indexOf(T pattern) {
		Node<T> current = head;
		int index = 0;
		while(index < size && !Objects.equals(current.data, pattern)) {
			index++;
			current = current.next;
		}
		return index < size ? index : -1;
	}

	@Override
	public int lastIndexOf(T pattern) {
		Node<T> current = tail;
		int index = size - 1;
		while(index >= 0 && !Objects.equals(current.data, pattern)) {
			index--;
			current = current.prev;
		}
		return index;
	}
	private Node<T> getNode(int index) {
		return index < size / 2 ? getNodeFromHead(index) : getNodeFromTail(index);
	}

	private Node<T> getNodeFromTail(int index) {
		Node<T> current = tail;
		for(int i = size - 1; i > index; i--) {
			current = current.prev;
		}
		return current;
	}

	private Node<T> getNodeFromHead(int index) {
		Node<T> current = head;
		for(int i = 0; i < index; i++) {
			current = current.next;
		}
		return current;
	}
	private void addNode(int index, Node<T> node) {
		if(index == 0) {
			addHead(node);
		} else if(index == size) {
			addTail(node);
		} else {
			addMiddle(node, index);
		}
		size++;
	}

	private void addMiddle(Node<T> node, int index) {
		Node<T> nodeNext = getNode(index);
		Node<T> nodePrev = nodeNext.prev;
		nodeNext.prev = node;
		nodePrev.next = node;
		node.prev = nodePrev;
		node.next = nodeNext;
		
		
	}

	private void addTail(Node<T> node) {
		//head cannot be null
		tail.next = node;
		node.prev = tail;
		tail = node;
		
	}

	private void addHead(Node<T> node) {
		if(head == null) {
			head = tail = node;
		} else {
			node.next = head;
			head.prev = node;
			head = node;
		}
		
	}
}
