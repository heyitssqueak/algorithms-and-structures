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
        if(data == null) {
            throw new NullPointerException("the data inserted is null");
        }

        Node<E> newNode = new Node<>(data);

        if(this.root == null) {
            this.root = newNode;
        } else {
            insertionHelper(root, newNode);
        }
    }

    /**
     * This is a helper method for {@link #insert(E data)}. It recurses down the tree to
     * find the spot to insert the node passed.
     * @param subtree results as parent of NewNode
     * @param newNode new data to be added in Node<E> form
     */
    public void insertionHelper(Node<E> subtree, Node<E> newNode) {

        // System.out.println("HELPER METHOD subtree: " + subtree.data);
        // System.out.println("HELPER METHOD newNode: " + newNode.data);

        // Checks if inserted value is null, throws error if it is
        if(root.data.compareTo(newNode.data) == 0) {
            throw new IllegalArgumentException("value already inserted");

        } else if(newNode.data.compareTo(subtree.data) < 0) {   // newNode < subtree, travel to the left
            if(subtree.leftChild == null) {                     // if no children, new node is inserted here
                subtree.leftChild = newNode;
                newNode.parent = subtree; 

            } else {                                            // current node has children, recurse down the tree
                insertionHelper(subtree.leftChild, newNode);
            }
        } else if(newNode.data.compareTo(subtree.data) > 0) {   // newNode > subtree, travel to the right
            if(subtree.rightChild == null) {                    // if no children, new node inserted here
                subtree.rightChild = newNode;
                newNode.parent = subtree;

            } else {                                            // current node has children, recurse down the tree
                insertionHelper(subtree.rightChild, newNode);
            }
        }
    }

    public void delete(E data) throws NullPointerException, IllegalArgumentException {
        if(data == null) {
            throw new NullPointerException("the data inserted is null");
        }

        deletionHelper(this.root, data);
    }

    public void deletionHelper(Node<E> subtree, E data) {
        // System.out.println(subtree.data + " " + data);
        // checking if we're at the node to be deleted
        if(subtree.data == data) { // THIS PART WORKS !!!!!!!!!!!!!!

            Node<E> child = null;
            Node<E> parent = subtree.parent;

            if(subtree.leftChild == null && subtree.rightChild == null) { // subtree has no children

                // System.out.println("NO CHILDREN");

                if(subtree.parent.leftChild == subtree) { // subtree is left child
                    subtree.parent.leftChild = null;
                } else /*(subtree.parent.rightChild == subtree)*/ { // subtree is right child
                    subtree.parent.rightChild = null;
                }

                subtree.parent = null;

            } else if(subtree.leftChild == null ^ subtree.rightChild == null) { // subtree has one child
                // to hold the child node while the parent (subtree) is deleted
                // System.out.println("ONE CHILD");

                if(subtree.leftChild == null) {
                    child = subtree.rightChild;
                    
                    if(child.data.compareTo(parent.data) > 0) {
                        parent.rightChild = child;
                    } else /*(childTree.compareTo(parent) < 0)*/ {
                        parent.leftChild = child;
                    } 

                } else { /*(subtree.rightChild == null)*/
                    // System.out.println("RIGHT CHILD");
                    child = subtree.leftChild;

                    // System.out.println(child.data + " " + parent.data);

                    if(child.data.compareTo(parent.data) > 0) {
                        parent.rightChild = child;
                    } else /*(childTree.compareTo(parent) < 0)*/ {
                        parent.leftChild = child;
                    } 
                }

                subtree.parent = null;

            } else { // subtree has two children
                // TODO deletionHelper two children subcase

                child = subtree.rightChild;

                // subcase 1: replacement node (child) has no children of its own OR only right child
                if((child.leftChild == null && child.rightChild == null) || child.leftChild == null) {
                    // moving left branch of deleted node to the left branch of the replacement (child) node
                    child.leftChild = subtree.leftChild;

                    // reassigning parent/child edge
                    if(parent.leftChild == subtree) {
                        parent.leftChild = child;
                    } else {
                        parent.rightChild = child;
                    }
                } else { // subcase 2: the replacement node has a left child, so we find inorder successor
                    child = this.findInorderSuccessor(subtree);

                    // remove connections to new child node
                    if(child.parent.leftChild == child) {
                        child.parent.leftChild = null;
                    } else {
                        child.parent.rightChild = null;
                    }

                    // setting child with connections of node to be deleted
                    child.leftChild = subtree.leftChild;
                    subtree.leftChild.parent = child;
                    child.rightChild = subtree.rightChild;
                    subtree.rightChild.parent = child;

                    // deleting subtree connections to tree and setting them to be child's
                    child.parent = subtree.parent;
                    // changing parent's connection to child

                    // if(parent.leftChild == subtree) {
                    //     parent.leftChild = child;
                    // } else {
                    //     parent.rightChild = child;
                    // }

                    if(parent.leftChild == subtree) parent.leftChild = child; else parent.rightChild = child;
                }
            }
        } else { // recurse further down the tree
            if(data.compareTo(subtree.data) < 0) { 
                deletionHelper(subtree.leftChild, data);
            } else if(data.compareTo(subtree.data) > 0) {
                deletionHelper(subtree.rightChild, data);
            }
            
            // System.out.println("RECURSING");
        }
    }

    /**
     * This is a method to find the in-order successor for a node with two children. It
     * iterates down the left side of the tree until it finds the largest value and
     * returns it.
     * 
     * TODO this java doc header
     */
    public Node<E> findInorderSuccessor(Node<E> subtree) {
        Node<E> min = subtree;

        subtree = subtree.rightChild;

        while(subtree.leftChild != null) {
            subtree = subtree.leftChild;
            min = subtree;
        }

        return min;
    }

    /**
     * "borrowed" someone else's code to try and find a way to visualize bst
     * and it doesn't even fucking work
     * @param args
     */
    public String traverse(Node<E> node) {
        String rslt = "";
        boolean hasRightNode = false;
        boolean hasLeftNode = false;
        if(node.rightChild != null) {
            hasRightNode = true;
            rslt += "(";
            rslt += traverse(node.rightChild);
        }
        if(node.leftChild != null) {
            hasLeftNode = true;
            if(hasRightNode) {
                rslt += ",";
            } else {
                rslt += "(";
            }
            rslt += traverse(node.leftChild);
        }
        if (hasLeftNode || hasRightNode) {
            rslt += ")";
        }
        rslt += node.data;
        return rslt;
    }

    public static void main(String[] args) {

        Random rand = new Random();

        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        for(int i = 0; i < 20; i++) {
            // TODO random number but I can't get it because i'm a fucking moron
            tree.insert(rand.nextInt(100));
        }
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

        System.out.println(tree.traverse(tree.root));
    }

    // TODO get junit 5 installed and working because middleton fucking blocked github

    // public class BinarySearchTreeTestSuite {
    //     @Test
    //     void addition() {
    //         assertEquals(2, 2);
    //     }
    // }
}
