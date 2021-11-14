import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import org.junit.jupiter.api.Test;


// important learning note: if I want to use the class Node<E extends Comparable<E>>
// then I have to also add the <E extends Comparable<E>> code to the overarching
// BinarySearchTree class

public class BinarySearchTree<E extends Comparable<E>> {
    /**
     * Custom written node class for BinarySearchTree with parent, left and right children.
     * Primarily for practice coding b/c I haven't coded in a long time.
     */
    private static class Node<E> {
        public E data;
        public Node<E> parent;
        public Node<E> leftChild;
        public Node<E> rightChild;

        public Node(E data) {
            this.data = data;
        }

        public boolean hasChildren() {
            if(this.leftChild == null || this.rightChild == null) return false; else return true;
        }

        /**
         * Prints out a string of the nodes in level-order; so starting with the top-most
         * node and reading left-to-right on each level moving down. [Uses FIFO queue to]
         * organize.
         */
        public void levelOrderTraversal() {
            Queue<Node<E>> queue = new LinkedList<>();
            Node<E> root = this;
            queue.add(root);

            String string = "[ ";

            // adds children of currNode to the queue left-to-right
            // ensures that level-order is maintained since parent node is checked to add
            // children, then sibling node is checked before moving onto children nodes
            while(!queue.isEmpty()) {
                Node<E> currNode = queue.remove();
               
                // prevents nulls from adding to the queue
                if(currNode.leftChild != null) {
                    queue.add(currNode.leftChild);
                }
                if(currNode.rightChild!= null) {
                   queue.add(currNode.rightChild);
                }
                
                string += (currNode.data);
                if(!queue.isEmpty()) {
                    string += ", ";
                }
            }

            System.out.println(string + " ]");
        }
    }

    Node<E> root;

    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * Inserts a node into the tree. Will add value to root if root is null. If root isn't
     * null, this method calls a helper method {@link #insertionHelper(Node<E> subtree, Node<E> newNode)}
     * to recursively search down the tree for the position to place the node-to-be inserted.
     */
    public void insert(E data) throws NullPointerException, IllegalArgumentException {
        if(data == null) throw new NullPointerException("the data inserted is null");

        Node<E> newNode = new Node<>(data);

        if(this.root == null) this.root = newNode; else insertionHelper(root, newNode);
    }

    /**
     * This is a helper method for {@link #insert(E data)}. It recurses down the tree to
     * find the spot to insert the node passed.
     */
    public void insertionHelper(Node<E> subtree, Node<E> newNode) {
        // Checks if inserted value is null, throws error if it is
        if(root.data.compareTo(newNode.data) == 0) {
            throw new IllegalArgumentException("value already inserted");

        } else if(newNode.data.compareTo(subtree.data) < 0) {   // newNode < subtree, travel to the left
            if(subtree.leftChild == null) {                     // if no children, new node is inserted as left child
                subtree.leftChild = newNode;
                newNode.parent = subtree; 

            } else {                                            // current node has children, recurse down the tree
                insertionHelper(subtree.leftChild, newNode);
            }
        } else if(newNode.data.compareTo(subtree.data) > 0) {   // newNode > subtree, travel to the right
            if(subtree.rightChild == null) {                    // if no children, new node inserted as right child
                subtree.rightChild = newNode;
                newNode.parent = subtree;

            } else {
                insertionHelper(subtree.rightChild, newNode);   // current node has children, recurse down the tree
            }
        }
    }

    /**
     * Deletes a node from the tree given that the node exists in the tree. Throws an error
     * otherwise. Calls {@link #deletionHelper(Node<E> subtree, E data)} to recursively 
     * search for the node to remove.
     */
    public void delete(E data) throws NullPointerException, IllegalArgumentException {
        if(data == null) throw new NullPointerException("the data inserted is null");

        deletionHelper(this.root, data);
    }

    /**
     * This is a helper method for {@link #delete(E data)}. It recurses down the tree 
     * looking for the node that holds the data-to-be deleted. Once found, it will default
     * to subtree's right node as its successor, unless the right node has two children,
     * in which case it will find the data's inorder successor and use that instead as the
     * replacement node.
     */
    public void deletionHelper(Node<E> subtree, E data) {
        // checking if we're at the node to be deleted
        if(subtree.data == data) {

            Node<E> child = null;
            Node<E> parent = subtree.parent;

            if(subtree.leftChild == null && subtree.rightChild == null) { // case 1: deleted node has no children
                if(subtree.parent.leftChild == subtree) subtree.parent.leftChild = null; else subtree.parent.rightChild = null;
                subtree.parent = null;

            } else if(subtree.leftChild == null ^ subtree.rightChild == null) { // case 2: deleted node has one child
                // checking which side of the deleted node has children
                if(subtree.leftChild == null) {
                    child = subtree.rightChild;
                    // checking which side of the parent is deleted node on, then replacing that connection with replacement node
                    if(child.data.compareTo(parent.data) > 0) parent.rightChild = child; else parent.leftChild = child;

                } else {
                    child = subtree.leftChild;
                    if(child.data.compareTo(parent.data) > 0) parent.rightChild = child; else parent.leftChild = child;
                }

                subtree.parent = null;

            } else { // case 3: subtree has two children
                child = subtree.rightChild;

                // subcase 3.1: replacement node (child) has no children of its own OR only right child
                if((child.leftChild == null && child.rightChild == null) || child.leftChild == null) {
                    // moving left branch of deleted node to the left branch of the replacement (child) node
                    child.leftChild = subtree.leftChild;

                    // reassigning parent/child edge
                    if(parent.leftChild == subtree) parent.leftChild = child; else parent.rightChild = child;

                } else { // subcase 3.2: the replacement node has a left child, so we find inorder successor
                    child = this.findInorderSuccessor(subtree);

                    // remove connections to new child node
                    if(child.parent.leftChild == child) child.parent.leftChild = null; else child.parent.rightChild = null;

                    // setting child with connections of node to be deleted
                    child.leftChild = subtree.leftChild;
                    subtree.leftChild.parent = child;
                    child.rightChild = subtree.rightChild;
                    subtree.rightChild.parent = child;

                    // deleting subtree connections to tree and setting them to be child's
                    child.parent = subtree.parent;

                    // changing parent's connection to child
                    if(parent.leftChild == subtree) parent.leftChild = child; else parent.rightChild = child;
                }
            }

        } else { // recurse further down the tree
            if(data.compareTo(subtree.data) < 0) deletionHelper(subtree.leftChild, data); else deletionHelper(subtree.rightChild, data);
        }
    }

    /**
     * Recurses down the tree until it finds a node that matches the data passed through
     * the parameter. Will throw an error if data value isn't found.
     */
    public Node<E> find(E data) throws IllegalArgumentException {
        Node<E> currNode = this.root;

        while(currNode.data != data ^ !currNode.hasChildren()) {
            System.out.println(currNode.data);
            if(currNode.data == data) return currNode;
            if(data.compareTo(currNode.data) < 0) currNode = currNode.leftChild; else currNode = currNode.rightChild;
        }

        throw new IllegalArgumentException("your data value isn't in the tree");
    }

    /**
     * Finds the inorder successor of node passed through. Uses its right child, then 
     * continuously iterates to the left until it finds the smallest value with respect
     * to the currNode.
     * @return next largest value in the tree with respect to currNode
     */
    public Node<E> findInorderSuccessor(Node<E> currNode) {
        Node<E> min = currNode;

        currNode = currNode.rightChild;

        while(currNode.leftChild != null) {
            currNode = currNode.leftChild;
            min = currNode;
        }

        return min;
    }

    public static void main(String[] args) {
        //Random rand = new Random();

        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        // for(int i = 0; i < 20; i++) {
        //     tree.insert(rand.nextInt(100));
        // }

        tree.insert(15);
        tree.insert(21);
        tree.insert(31);
        tree.insert(2);
        tree.insert(1);
        tree.insert(7);
        tree.insert(20);
        tree.insert(25);
        tree.insert(100);

        // System.out.println(tree.root.data);
        // System.out.println(tree.root.leftChild.data);
        // System.out.println(tree.root.rightChild.data);

        // System.out.println(tree.root.leftChild.leftChild.data); // should be 0
        // System.out.println(tree.root.leftChild.rightChild.data); // should be 2

        // System.out.println(tree.root.rightChild.rightChild.data); // should be 6

        tree.root.levelOrderTraversal();

        // System.out.println(tree.root.rightChild.rightChild.rightChild.data);
        // System.out.println(tree.root.rightChild.rightChild.leftChild.data);

        // tree.delete(100);
        // tree.root.levelOrderTraversal();

        // tree.delete(100);

        // tree.root.levelOrderTraversal();
        // System.out.println(tree.root.rightChild.rightChild.rightChild); // null, removed 100 leaf node
        // System.out.println(tree.root.rightChild.rightChild.data); // should be 31, rightmost node

        // tree.delete(1);

        // tree.root.levelOrderTraversal();
        // System.out.println(tree.root.leftChild.leftChild);
        // System.out.println(tree.root.leftChild.data); // should be 2
        // System.out.println(tree.root.leftChild.rightChild.data); // should be 7

        // tree.delete(31);

        // tree.root.levelOrderTraversal();

        // tree.delete(7);
        // tree.root.levelOrderTraversal();

        // tree.delete(2);
        // tree.root.levelOrderTraversal();

        // System.out.println(tree.root.leftChild.data); // should be 1
        // System.out.println(tree.root.rightChild.data); // should be 21
        // System.out.println(tree.root.rightChild.leftChild.data); // should be 20
        // System.out.println(tree.root.rightChild.rightChild.data); // should be 25

        // tree.delete(2);
        // tree.root.levelOrderTraversal();
        // System.out.println(tree.root.leftChild.data); // 7
        // System.out.println(tree.root.leftChild.leftChild.data); // 1

        // tree.delete(21);
        // tree.root.levelOrderTraversal();
        // System.out.println(tree.root.rightChild.data); // 25
        // System.out.println(tree.root.rightChild.rightChild.data); // 31
        // System.out.println(tree.root.rightChild.leftChild.data); // 20
        // System.out.println(tree.root.rightChild.rightChild.rightChild.data); // 100

        // System.out.println(tree.traverse(tree.root));

        tree.find(100);
        tree.find(69);
    }

    // TODO get junit 5 installed and working because middleton fucking blocked github

    // public class BinarySearchTreeTestSuite {
    //     @Test
    //     void addition() {
    //         assertEquals(2, 2);
    //     }
    // }
}
