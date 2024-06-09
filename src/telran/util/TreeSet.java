package telran.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TreeSet<T> extends AbstractCollection<T> implements SortedSet<T> {
private static class Node<T> {
	T data;
	Node<T> parent;
	Node<T> left;
	Node<T> right;
	Node(T data) {
		this.data = data;
	}
	
}
private class TreeSetIterator implements Iterator<T> {
	Node<T> current = getLeastFrom(root);
	T toReturn = null;
	@Override
	public boolean hasNext() {
		
		return current != null;
	}

	@Override
	public T next() {
		if(!hasNext()) {
			throw new NoSuchElementException();
		}
		toReturn = current.data;
		current = getCurrent(current);
		return toReturn;
	}
	@Override
	public void remove() {
		if(toReturn == null) {
			throw new IllegalStateException();
		}
		TreeSet.this.remove(toReturn);
		toReturn = null;
	}
	
}
	Node<T> root;
	private Comparator<T> comp;
	public TreeSet(Comparator<T> comp) {
		this.comp = comp;
	}
	@SuppressWarnings("unchecked")
	public TreeSet() {
		this((Comparator<T>)Comparator.naturalOrder());
	}
	@Override
	public T get(T pattern) {
		Node<T> node = getNode(pattern);
		
		return node == null ? null : node.data;
	}

	private Node<T> getNode(T pattern) {
		Node<T> res = null;
		Node<T> node = getParentOrNode(pattern);
		if (node != null && comp.compare(node.data, pattern) == 0) {
			res = node;
		}
		return res;
	}

	private Node<T> getParentOrNode(T pattern) {
		Node<T> current = root;
		Node<T> parent = null;
		int compRes = 0;
		while(current != null && (compRes = comp.compare(pattern, current.data)) != 0) {
			parent = current;
			current = compRes > 0 ? current.right : current.left;
		}
		return current == null ? parent : current;
	}

	public Node<T> getCurrent(Node<T> current) {
		//Algorithm see on the board
		return current.right != null ? getLeastFrom(current.right) :
			getFirstGreaterParent(current);
	}

	private Node<T> getFirstGreaterParent(Node<T> current) {
		Node<T> parent = current.parent;
		while(parent != null && parent.right == current) {
			current = current.parent;
			parent = current.parent;
		}
		return parent;
	}
	private Node<T> getLeastFrom(Node<T> node) {
		if (node != null) {
			
			while(node.left != null) {
				node = node.left;
			}
		}
		return node;
	}

	@Override
	public boolean add(T obj) {
		boolean res = false;
		if(obj == null) {
			throw new NullPointerException();
		}
		if(!contains(obj)) {
			res = true;
			Node<T> node = new Node<>(obj);
			if(root == null) {
				addRoot(node);
			} else {
				addWithParent(node);
			}
			size++;
		}
		return res;
	}

	private void addWithParent(Node<T> node) {
		Node<T> parent = getParent(node);
		if(comp.compare(node.data,parent.data) > 0) {
			parent.right = node;
		} else {
			parent.left = node;
		}
		node.parent = parent;
		
	}

	private Node<T> getParent(Node<T> node) {
		Node<T> parent = getParentOrNode(node.data);
		return parent;
	}

	private void addRoot(Node<T> node) {
		root = node;
		
	}

	@Override
	public boolean remove(T pattern) {
		boolean res = false;
		Node<T> node = getNode(pattern);
		if (node != null) {
			removeNode(node);
			res = true;
		}
		return res;
	}

	private void removeNode(Node<T> node) {
		if(node.left != null && node.right != null) {
			removeJunction(node);
		} else {
			removeNonJunction(node);
		}
		
		size--;
	}

	private void setNulls(Node<T> node) {
		node.data = null;
		node.parent = node.left = node.right = null;
		
	}
	private void removeJunction(Node<T> node) {
		Node<T> substitute = getGreatestFrom(node.left);
		node.data = substitute.data;
		removeNonJunction(substitute);
		
	}
	private void removeNonJunction(Node<T> node) {
		Node<T> parent = node.parent;
		Node<T> child = node.left != null ? node.left : node.right;
		if(parent == null) {
			root = child; //actual root removing
		} else if(node == parent.left) {
			parent.left = child;
		} else {
			parent.right = child;
		}
		if(child != null) {
			child.parent = parent;
		}
		setNulls(node);
		
	}
	@Override
	public boolean contains(T pattern) {
		
		return getNode(pattern) != null;
	}

	

	@Override
	public Iterator<T> iterator() {
		
		return new TreeSetIterator();
	}

	@Override
	public T first() {
		
		return root == null ? null : getLeastFrom(root).data;
	}

	@Override
	public T last() {
		
		return root == null ? null : getGreatestFrom(root).data;
	}

	private Node<T> getGreatestFrom(Node<T> node) {
		if(node != null) {
			while(node.right != null) {
				node = node.right;
			}
		}
		return node;
	}

}
