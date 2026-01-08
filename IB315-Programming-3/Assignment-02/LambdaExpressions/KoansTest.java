import org.junit.*;
import static org.junit.Assert.*;

import java.util.Random;
import java.util.function.*;

public class KoansTest {

	@Test
	public void mapArray() {
		int[] array = {1, 2, 3, 4};
		Koans.mapArray(array, (n) -> n = n + 1);
		assertArrayEquals(new int[] {2, 3, 4, 5}, array);
	}
	
	@Test
	public void fillArrayMathPI() {
		double[] array = {Math.PI, Math.PI, Math.PI};
		double[] array2 = Koans.fillArray(3, () -> Math.PI);
		assertArrayEquals(array, array2, 1e-10);
	}
	
	@Test
	public void fillArrayRandom() {
		Random r1 = new Random(0);
		Random r2 = new Random(0);
		double[] array = Koans.fillArray(3, () -> r1.nextDouble());
		assertArrayEquals(new double[] {r2.nextDouble(), r2.nextDouble(), r2.nextDouble()}, array, 1e-10);
	}
	
	@Test
	public void iterateFunctionPlusOne() {
		assertArrayEquals(new int[] {0, 1, 2, 3, 4}, Koans.iterateFunction(5, 0, (n) -> n = n + 1));
	}
	
	@Test
	public void iterateFunctionDouble() {
		assertArrayEquals(new int[] {1, 2, 4, 8, 16}, Koans.iterateFunction(5, 1, (n) -> n = n * 2));
	}
	
	@Test
	public void minInteger() {
		Integer[] array = {4,1,-1,2,0};
		int min = Koans.min(array, (x, y) -> Integer.compare(x, y));
		assertEquals(-1, min);
	}
	
	@Test
	public void minStringLength() {
		String[] array = {"1", "22", "333", "4444", "55555"};
		String min = Koans.min(array, (s1, s2) -> Integer.compare(s1.length(), s2.length()));
		assertEquals("1", min);
	}
	
	@Test
	public void createMultiplier() {
		double d = 3.0;
		DoubleFunction<Double> f = Koans.createMultiplier(5);
		assertEquals(15.0, f.apply(d), 1e-10);
	}
	
	@Test
	public void forEachArray() {
		String[] s = {"a", "b", "c", "d"};
		StringBuilder sb = new StringBuilder();
		Koans.forEachArray(s, s1 -> sb.append(s1));
		assertEquals("abcd", sb.toString());
	}
	
	@Test
	public void duplicateChecker() {
		Predicate<String> p = Koans.duplicateChecker();
		assertFalse(p.test("Foo"));
		assertFalse(p.test("Bar"));
		assertTrue(p.test("Foo"));
		assertFalse(p.test("Baz"));
	}
}
