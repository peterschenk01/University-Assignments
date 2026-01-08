import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.*;
import java.util.stream.*;

public class Koans {
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(fibonacciStream().limit(40).toArray()));
	}

	public static <T> long distinctEntries(Collection<T> entries) {
		return entries.stream().distinct().count();
	}
	
	public static long countEvenNumbers(int[] numbers) {
		return Arrays.stream(numbers).filter(x -> x % 2 == 0).count();
	}
	
	public static int[] intsFromStrings(String... strings) {
		return Arrays.stream(strings).mapToInt(x -> Integer.parseInt(x)).toArray();
	}
	
	public static double[] randomSum(int n) {
		return DoubleStream.iterate(0.0, x -> x = x + Math.random()).limit(n).toArray();
	}
	
	public static double dotProduct(double[] v1, double[] v2) {
		if(v1.length != v2.length)
			throw new IllegalArgumentException();
		
		return IntStream.range(0, v1.length).mapToDouble(x -> v1[x] * v2[x]).sum();
	}
	
	public static Map<Integer, List<String>> stringsForLength(Collection<String> strings){
		return strings.stream().collect(Collectors.groupingBy(String::length));
	}
	
	public static LongStream collatzSeries(int start) {
		return LongStream.iterate(start, x -> {
			if(x % 2 == 0)
				return x / 2;
			else 
				return x * 3 + 1;
		});
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
	
	public static LongStream collatzTruncated(int start) {
		Predicate<Long> p = duplicateChecker();
		
		return collatzSeries(start).takeWhile(n -> !p.test(n));
	}
	
	public static boolean collatzOrbit(int start) {
		if(collatzTruncated(start).anyMatch(n -> n == 1))
			return true;
		else 
			return false;
	}
	
	public static boolean collatzTrueForLimit(int limit) {
		return IntStream.rangeClosed(1, limit).parallel().allMatch(n -> collatzOrbit(n));
	}
	
	public static long fib(long n) {
		if(n == 0) {
			return 0;
		} else if (n == 1) {
			return 1;
		} else {
			return fib(n-1) + fib(n-2);
		}
	}
	
	public static LongStream fibonacciStream() {
		return LongStream.iterate(0, x -> x = x + 1).map(x -> fib(x));
	}
}
