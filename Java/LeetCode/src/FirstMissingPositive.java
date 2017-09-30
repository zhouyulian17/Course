
public class FirstMissingPositive {
	
    public int firstMissingPositive(int[] nums) {
        int i = 0;
        while (i < nums.length) {
        	int j = nums[i] - 1;
            if (j != i && nums[i] > 0 && nums[i] < nums.length + 1 && nums[j] -1 != j) {
            	exch(nums, i, nums[i] - 1);
            }
            else i++;
        }
        if (nums.length == 0 || nums[0] != 1) return 1;
        for (i = 0; i < nums.length; i++) {
            if (nums[i] - 1 != i) break;
        }
        return i + 1;
    }
    
    private void exch(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    public static void main(String[] args) {
    	int[] nums = {1,1};
    	FirstMissingPositive fmp = new FirstMissingPositive();
    	System.out.println(fmp.firstMissingPositive(nums));
    }
}
