
public class Simulator {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		
		
		int temp = 78;
		boolean test1 = true;
		int distance = 215;
		
		long beg = System.currentTimeMillis();
		for(int i = 0; i < 100_000_0000; ++i);
		
		System.out.println("timeEmpty:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 100_000_0000; i++);
		
		System.out.println("timeEmpty2:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 100_000_0000; ++i) {
			test1 = !test1;
		}
		System.out.println("timeNegate:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 100_000_0000; i++) {
			test1 = !test1;
		}
		System.out.println("timeNegate2:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 100_000_0000; ++i) {
			test1 = !test1;
			if(test1) temp += distance;
			else 	  temp -= distance;
		}
		System.out.println("timeIfs:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 100_000_0000; i++) {
			test1 = !test1;
			if(test1) temp += distance;
			else 	  temp -= distance;
		}
		System.out.println("timeIfs2:" + (System.currentTimeMillis() - beg));
		
		int dir = 1;
		beg = System.currentTimeMillis();
		for(int i = 0; i < 100_000_0000; i++) {
			dir = ~dir + 1;
			temp += dir * distance;
		}
		System.out.println("timeDir:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 100_000_0000; ++i) {
			dir = ~dir + 1;
			temp += dir * distance;
		}
		System.out.println("timeDir2:" + (System.currentTimeMillis() - beg));
	}

}
