package a05;

/*
 * Ryan Wheeler
 * Romela Azizyan
 */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;

public class PointST<Value> {
	
	private RedBlackBST<Point2D, Value> st;
	public PointST() {
		st = new RedBlackBST<Point2D, Value>();
	} // construct an empty symbol table of points

	public boolean isEmpty() {
		return size() == 0;
	} // is the symbol table empty?

	public int size() {
		return st.size();
	} // number of points

	public void put(Point2D p, Value val) {
		if(p == null) throw new NullPointerException();
		st.put(p, val);
	} // associate the value val with point p

	public Value get(Point2D p) {
		if(p == null) throw new NullPointerException();
		return st.get(p);
	} // value associated with point p

	public boolean contains(Point2D p) {
		if(p == null) throw new NullPointerException();
		return st.contains(p);
	} // does the symbol table contain point p?

	public Iterable<Point2D> points() {
		return st.keys();
	} // all points in the symbol table

	public Iterable<Point2D> range(RectHV rect) {
		if(rect == null) throw new NullPointerException();
		Queue<Point2D> queue = new Queue<>();
		for(Point2D p : st.keys()) {
			if(rect.contains(p)) {
				queue.enqueue(p);
			}
		}
		
		return queue;
	} // all points that are inside the rectangle

	public Point2D nearest(Point2D p) {
		if(p == null) throw new NullPointerException();
		Point2D nearest = st.max();
		for(Point2D point : st.keys()) {
			if(p.distanceSquaredTo(point) < p.distanceSquaredTo(nearest)) {
				nearest = point;
			}
		}
		return nearest;
	} // a nearest neighbor to point p; null if the symbol table is empty

	public static void main(String[] args) {
	}
}
