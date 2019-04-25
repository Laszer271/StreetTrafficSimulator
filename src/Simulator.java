
public class Simulator {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		
		Car.Direction dir = Car.Direction.NORTH;
		Car.Direction dir2 = Car.Direction.NORTH;
		Car.Direction dir3 = Car.Direction.SOUTH;
		
		if(dir == dir2) System.out.print("ok");
		if(dir == dir3 || dir2 == dir3) System.out.print("nieok");
		/*
		int temp = 78;
		boolean test1 = true;
		int distance = 215;
		
		long beg = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; ++i);
		
		System.out.println("timeEmpty:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; i++);
		
		System.out.println("timeEmpty2:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; ++i) {
			test1 = !test1;
		}
		System.out.println("timeNegate:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; i++) {
			test1 = !test1;
		}
		System.out.println("timeNegate2:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; ++i) {
			test1 = !test1;
			if(test1) temp += distance;
			else 	  temp -= distance;
		}
		System.out.println("timeIfs:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; i++) {
			test1 = !test1;
			if(test1) temp += distance;
			else 	  temp -= distance;
		}
		System.out.println("timeIfs2:" + (System.currentTimeMillis() - beg));
		
		int dir = 1;
		beg = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; i++) {
			dir = ~dir + 1;
			temp += dir * distance;
		}
		System.out.println("timeDir:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; ++i) {
			dir = ~dir + 1;
			temp += dir * distance;
		}
		System.out.println("timeDir2:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; i++) {
			int j = i%3;
		}
		
		System.out.println("ModuloTest:" + (System.currentTimeMillis() - beg));
		
		beg = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; i++) {
			int j = 2863%2567;
		}
		
		System.out.println("ModuloTest2:" + (System.currentTimeMillis() - beg));
		*/
	}

}
