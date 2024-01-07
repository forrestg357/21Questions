public class Tree<E> {
		
	private E data;
	private Tree<E> left;
	private Tree<E> right;
	
	//Constructor 1
	public Tree(E data, Tree<E> left, Tree<E> right) {
		
		this.data = data;
		this.right = right;
		this.left = left;
	}
	
	//Constructor 2
	public Tree(E data) {
		this.data = data;
		this.right = null;
		this.left = null;
	}
	
	//Constructor 3
	public Tree() {
		
		this.data = null;
		this.right = null;
		this.left = null;
	}
	
	//Sets the data of the tree
	public void setData(E data) {
		this.data = data;
	}
	
	//returns the data at the root of the tree
	public E getData() {
		return this.data;
	}
	
	//Sets the left subtree
	public void setLeft(Tree<E> left) {
		this.left = left;
	}
	
	//Gets the left subtree
	public Tree<E> getLeft() {
		return this.left;
	}
	
	//Sets the right subtree
	public void setRight(Tree<E> right) {
		this.right = right;
	}
	
	//Gets the right subtree
	public Tree<E> getRight() {
		return this.right;
	}
}
