import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Scapegoat Tree class
 *
 * This class contains an implementation of a Scapegoat tree.
 */

public class SGTree {
    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        int weight = 1;
        public TreeNode left = null;
        public TreeNode right = null;
        public TreeNode parent = null;


        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the subtree rooted at node
     *
     * @param node the root of the subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node) {
        if (node == null)
            return 0;

        int left = 0;
        int right = 0;
        if (node.left != null)
            left = countNodes(node.left);
        if (node.right != null)
            right = countNodes(node.right);

        return left + right + 1;
    }

    /**
     * Builds an array of nodes in the subtree rooted at node
     *
     * @param node the root of the subtree
     * @return array of nodes
     */
    public TreeNode[] enumerateNodes(TreeNode node) {
        int nodeSize = countNodes(node);
        TreeNode[] treeNodes = new TreeNode[nodeSize];
        inorderTraversal(treeNodes, node, new int[]{0});
        return treeNodes;
    }

    private void inorderTraversal(TreeNode[] treeNodes,TreeNode node, int[] count) {
        if (node== null)
            return;
        inorderTraversal(treeNodes, node.left, count);
        treeNodes[count[0]++] = node;
        inorderTraversal(treeNodes, node.right, count);
    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {
        return buildTree(nodeList, 0, nodeList.length - 1, null);
    }

    private TreeNode buildTree(TreeNode[] nodeList, int start, int end, TreeNode parent) {
        if (start > end) return null;

        int mid = start + (end - start) / 2;

        TreeNode node = nodeList[mid];
        node.parent = parent;
        node.left = buildTree(nodeList, start, mid - 1, node);
        node.right = buildTree(nodeList, mid + 1, end, node);

        node.weight = 1;
        if (node.left != null) node.weight += node.left.weight;
        if (node.right != null) node.weight += node.right.weight;

        return node;
    }


    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {

        if (node == null)
            return true;

        if (node.left != null && node.left.weight > (2*node.weight)/3)
            return false;

        if (node.right != null && node.right.weight > (2*node.weight)/3)
            return false;

        return true;
    }


    /**
    * Rebuilds the subtree rooted at node
    *
    * @param node the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node) {
        // Error checking: cannot rebuild null tree
        if (node == null) {
            return;
        }

        TreeNode p = node.parent;
        TreeNode[] nodeList = enumerateNodes(node);
        TreeNode newRoot = buildTree(nodeList);

        if (p == null) {
            root = newRoot;
        } else if (node == p.left) {
            p.left = newRoot;
        } else {
            p.right = newRoot;
        }

        newRoot.parent = p;
    }


    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        insert(key, root);

        TreeNode highestUnbalance = checkUnbalanceNBuild(root, key);

        rebuild(highestUnbalance);

    }


    // Helper method to insert a key into the tree
    private void insert(int key, TreeNode node) {
        node.weight += 1;
        if (key <= node.key) {
            if (node.left == null) {
                node.left = new TreeNode(key);
                node.left.parent = node;
            } else {
                insert(key, node.left);
            }
        } else {
            if (node.right == null) {
                node.right = new TreeNode(key);
                node.right.parent = node;
            } else {
                insert(key, node.right);
            }
        }


    }





    private TreeNode checkUnbalanceNBuild(TreeNode unbalancedNode, int key) {
        if (unbalancedNode == null)
            return null;
        if (!checkBalance(unbalancedNode))
            return unbalancedNode;
        else {
            if (key <= unbalancedNode.key)
                return checkUnbalanceNBuild(unbalancedNode.left, key);
            else
                return checkUnbalanceNBuild(unbalancedNode.right, key);
        }


    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root);
    }
}
