
public class NumArray {
    private TreeNode root;
    private int[] nums;
    
    private class TreeNode {
        private TreeNode left;
        private TreeNode right;
        private int lo;
        private int hi;
        private int sum;
        
        public TreeNode(int i, int j) {
            lo = i;
            hi = j;
        }
    }
    
    public NumArray(int[] nums) {
        this.nums = nums;
        root = buildNode(0, nums.length - 1);
    }
    
    private TreeNode buildNode(int lo, int hi) {
        TreeNode nd = new TreeNode(lo, hi);
        if (lo == hi)  nd.sum = nums[lo];
        else {
            int mid = (lo + hi) / 2;
            nd.left = buildNode(lo, mid);
            nd.right = buildNode(mid + 1, hi);
            nd.sum = nd.left.sum + nd.right.sum;
        }
        return nd;
    }
    
    public void update(int i, int val) {
        updateNode(i, val, root);
        nums[i] = val;
    }
    
    
    private void updateNode(int i, int val, TreeNode nd) {
        if (nd == null || i < nd.lo || i > nd.hi) return;
        if (nd.left == null && nd.right == null) {
            nd.sum = val;
            return;
        }
        nd.sum = nd.sum + val - nums[i];
        updateNode(i, val, nd.left);
        updateNode(i, val, nd.right);
    }
    
    public int sumRange(int i, int j) {
        return sumRangeNode(i, j, root);
    }
    
    private int sumRangeNode(int i, int j, TreeNode nd) {
        if (nd == null || nd.lo > j || nd.hi < i) return 0;
        if (nd.lo > i) i = nd.lo;
        if (nd.hi < j) j = nd.hi;
        if (nd.lo == i && nd.hi == j) return nd.sum;
        return sumRangeNode(i, j, nd.left) + sumRangeNode(i, j, nd.right);
    }
    
    public static void main(String[] args) {
    	//TreeNode nd = new TreeNode(0);
    	//nd.left = new TreeNode(1);
    	//nd.right = new TreeNode(2);
    	//nd.left.left = new TreeNode(3);
    	//nd.left.right = new TreeNode(4);
    	//nd.right.left = new TreeNode(5);
    	//nd.right.right = new TreeNode(6);
    	int[] nums = {};
    	NumArray s = new NumArray(nums);
    	//s.update(1, 2);
    	//System.out.println(s.sumRange(0, 2));
    }
}