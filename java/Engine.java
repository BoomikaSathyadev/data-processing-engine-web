import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** Core logic — BST, stack, DFS, BFS, Selection Sort, Quick Sort. */
public class Engine {

    private static final int MAX = 100;
    private final Deque<Integer> stack = new ArrayDeque<>();
    public TreeNode root;
    public final List<Integer> arr = new ArrayList<>();

    public boolean insert(int value) {
        if (arr.size() >= MAX) return false;
        root = insertNode(root, value);
        arr.add(value);
        stack.push(value);
        return true;
    }

    private TreeNode insertNode(TreeNode node, int data) {
        if (node == null) return new TreeNode(data);
        if (data < node.data) node.left = insertNode(node.left, data);
        else if (data > node.data) node.right = insertNode(node.right, data);
        return node;
    }

    public Integer undo() {
        if (stack.isEmpty()) return null;
        int value = stack.pop();
        root = deleteNode(root, value);
        arr.remove(Integer.valueOf(value));
        return value;
    }

    public void clear() {
        root = null;
        arr.clear();
        stack.clear();
    }

    public void loadSample() {
        clear();
        for (int v : new int[] {50, 30, 70, 20, 40, 60, 80}) insert(v);
    }

    private TreeNode findMin(TreeNode node) {
        while (node != null && node.left != null) node = node.left;
        return node;
    }

    private TreeNode deleteNode(TreeNode node, int key) {
        if (node == null) return null;
        if (key < node.data) node.left = deleteNode(node.left, key);
        else if (key > node.data) node.right = deleteNode(node.right, key);
        else {
            if (node.left == null && node.right == null) return null;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            TreeNode t = findMin(node.right);
            node.data = t.data;
            node.right = deleteNode(node.right, t.data);
        }
        return node;
    }

    public List<TreeNode> inorderNodes() {
        List<TreeNode> out = new ArrayList<>();
        inorderNodes(root, out);
        return out;
    }

    private void inorderNodes(TreeNode node, List<TreeNode> out) {
        if (node == null) return;
        inorderNodes(node.left, out);
        out.add(node);
        inorderNodes(node.right, out);
    }

    public List<Integer> inorderValues() {
        List<Integer> out = new ArrayList<>();
        for (TreeNode n : inorderNodes()) out.add(n.data);
        return out;
    }

    public List<TreeNode> bfsNodes() {
        List<TreeNode> out = new ArrayList<>();
        if (root == null) return out;
        ArrayDeque<TreeNode> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.remove();
            out.add(cur);
            if (cur.left != null) q.add(cur.left);
            if (cur.right != null) q.add(cur.right);
        }
        return out;
    }

    public List<Integer> bfsValues() {
        List<Integer> out = new ArrayList<>();
        for (TreeNode n : bfsNodes()) out.add(n.data);
        return out;
    }

    public int[] selectionSort() {
        int[] a = toArray();
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++)
                if (a[j] < a[min]) min = j;
            int t = a[min]; a[min] = a[i]; a[i] = t;
        }
        return a;
    }

    public int[] quickSort() {
        int[] a = toArray();
        quickSort(a, 0, a.length - 1);
        return a;
    }

    private void quickSort(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int pivot = a[hi], i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (a[j] < pivot) {
                i++;
                int t = a[i]; a[i] = a[j]; a[j] = t;
            }
        }
        int t = a[i + 1]; a[i + 1] = a[hi]; a[hi] = t;
        int p = i + 1;
        quickSort(a, lo, p - 1);
        quickSort(a, p + 1, hi);
    }

    public int[] toArray() {
        int[] copy = new int[arr.size()];
        for (int i = 0; i < arr.size(); i++) copy[i] = arr.get(i);
        return copy;
    }

}
