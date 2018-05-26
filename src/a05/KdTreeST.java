package a05;

/*
 * Ryan Wheeler
 * Romela Azizyan
 */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;


public class KdTreeST<Value> {

	private Node root;
	private int size;

	private class Node {// makes a node

		private Point2D point; // pair of points for key
		private Value val; // Associated with the ST
		private Node left, right;// left and right trees
		private RectHV rect;// rectangle associated with the node

		public Node(Point2D point, Value val, RectHV rect) {
			this.point = point;
			this.val = val;
			this.rect = rect;

		}
	}

	public KdTreeST() { // construct an empty symbol table of points

	}

	public boolean isEmpty() {// is the symbol table empty?

		return size == 0;

	}

	public int size() {// number of points

		return size;

	}

	public void put(Point2D p, Value val) {// associate the value val with point p

		if (p == null || val == null)
			throw new IllegalArgumentException("Can't be null");
		root = put(root, p, val, true);

	}

	private Node put(Node x, Point2D p, Value val, boolean test) {// put helper method, decides where to place points.
		// root node
		if (x == null) {
			size++;
			return new Node(p, val,
					new RectHV(-Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
		}
		// decides where to place to point
		double comp = comparePoints(x, p, test);
		if (comp < 0)
			x.left = put(x.left, p, val, !test);
		else if (comp > 0)
			x.right = put(x.right, p, val, !test);
		else if (x.point.equals(p))
			x.val = val;
		else
			x.right = put(x.right, p, val, !test);
		return x;

	}

	public Value get(Point2D p) {// value associated with point p

		if (p == null)
			throw new NullPointerException("Point can not be null");
		return get(root, p, true);

	}

	private Value get(Node x, Point2D p, boolean test) {// get helper class, finds point.

		if (x == null)
			return null;

		double comp = comparePoints(x, p, test);
		if (comp < 0)
			return get(x.left, p, !test);
		else if (comp > 0)
			return get(x.right, p, !test);
		else if (x.point.equals(p))
			return x.val;
		else
			return get(x.right, p, !test);

	}

	public double comparePoints(KdTreeST<Value>.Node node, Point2D p, boolean test) {// compares x or y value depending
																						// on level
		if (test) {
			return Double.compare(p.x(), node.point.x());
		} else {
			return Double.compare(p.y(), node.point.y());
		}

	}

	public boolean contains(Point2D p) {// does the symbol table contain point p?

		if (p == null)
			throw new IllegalArgumentException("arguemnt to contains() is null");
		return get(p) != null;

	}

	public Iterable<Point2D> points() {// all points in the symbol table

		Queue<Point2D> points = new Queue<Point2D>();
		Queue<Node> queue = new Queue<Node>();
		queue.enqueue(root);

		while (!queue.isEmpty()) {
			Node x = queue.dequeue();
			if (x == null)
				continue;
			points.enqueue(x.point);
			queue.enqueue(x.left);
			queue.enqueue(x.right);
		}
		return points;
	}

	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle

		if (rect == null)
			throw new NullPointerException("Rectangle can not be null");

		Queue<Point2D> rangeQueue = new Queue<Point2D>();
		range(rect, rangeQueue, root);
		return rangeQueue;

	}

	private void range(RectHV rect, Queue<Point2D> queue, KdTreeST<Value>.Node node) {// searches if in range

		if (node == null)
			return;
		if (!rect.intersects(node.rect))
			return;
		if (rect.contains(node.point))
			queue.enqueue(node.point);
		range(rect, queue, node.left);
		range(rect, queue, node.right);

	}

	public Point2D nearest(Point2D p) {// searches for nearest neighbor
		RectHV rect = new RectHV(-Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		return nearest(root, p, rect, true);
	}

	private Point2D nearest(Node x, Point2D p, RectHV rect, boolean test) {// helper method for a nearest neighbor in
																			// the tree to p, and returns null if set is empty
		Point2D incumbent = x.point;
		Point2D challenger;
		RectHV leftRect, rightRect;
		double distance = incumbent.distanceTo(p);
		
		if (test) {
			leftRect = new RectHV(rect.xmin(), rect.ymin(), x.point.x(), rect.ymax());
			rightRect = new RectHV(x.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
		} else {
			leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.point.y());
			rightRect = new RectHV(rect.xmin(), x.point.y(), rect.xmax(), rect.ymax());
		}

		double comp = comparePoints(x, p, test);
		if (comp <= 0) {
			if (x.left != null) {
				if ((challenger = nearest(x.left, p, leftRect, !test)).distanceTo(p) < distance) {
					incumbent = challenger;
					distance = challenger.distanceTo(p);
				}
			}
			if (x.right != null) {
				if (rightRect.distanceTo(p) < distance) {
					if ((challenger = nearest(x.right, p, rightRect, !test)).distanceTo(p) < distance) {
						incumbent = challenger;
						distance = challenger.distanceTo(p);
					}
				}
			}
		}

		else {
			if (x.right != null) {
				if ((challenger = nearest(x.right, p, rightRect, !test)).distanceTo(p) < distance) {
					incumbent = challenger;
					distance = challenger.distanceTo(p);
				}
			}
			if (x.left != null) {
				if (leftRect.distanceTo(p) < distance) {
					if ((challenger = nearest(x.left, p, leftRect, !test)).distanceTo(p) < distance) {
						incumbent = challenger;
						distance = challenger.distanceTo(p);
					}
				}
			}
		}
		return incumbent;
	}

	public static void main(String[] args) {// unit testing of the methods (not graded)

	}
}
