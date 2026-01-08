import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.*;

public class Koans {
	
	public static void mapArray(int[] array, IntFunction<Integer> f) {
		if( array == null || array.length == 0 || f == null)
			throw new IllegalArgumentException();
		
		for(int i = 0; i < array.length; i++) {
			array[i] = f.apply(array[i]);
		}
	}
	
	public static double[] fillArray(int length, Supplier<Double> s) {
		double[] array = new double[length];
		for(int i = 0; i < array.length; i++)
			array[i] = s.get();
		return array;
	}

	public static int[] iterateFunction(int length, int first, IntFunction<Integer> f) {
		int[] array = new int[length];
		array[0] = first;
		for(int i = 1; i < length; i++)
			array[i] = f.apply(array[i - 1]);
		return array;
	}

	public static <T> T min(T[] elements, Comparator<T> c){
		if(elements == null || elements.length == 0 || c == null)
			throw new IllegalArgumentException();
		T min = null;
		for(T element : elements) {
			if(element != null && (min == null || c.compare(element, min) < 0))
				min = element;
		}
		return min;
	}
	
	public static DoubleFunction<Double> createMultiplier(double d) {
		DoubleFunction<Double> f = (n) -> n = n * d;
		return f;
	}
	
	public static void forEachArray(String[] strings, Consumer<String> c) {
		for(String s : strings)
			c.accept(s);
	}
	
	public static <T> Predicate<T> duplicateChecker(){
		Set<T> set = new HashSet<>();
		Predicate<T> p = x -> {
			if(set.contains(x))
				return true;
			else {
				set.add(x);
				return false;
			}
		};
		return p;
	}
	
}