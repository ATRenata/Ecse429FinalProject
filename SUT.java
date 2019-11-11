
public class SUT {

	public static int Math(int var1, int var2) {
		int w = var1 / var2;
		int q = 1 / w;
		int var = q / 3;
		if (q > var) { 
			return 1;
		}else {
			return 0;
		}
	}	
	
	public static void main(String[] args) {
		
		int e = Math(1,2);

	}

}
