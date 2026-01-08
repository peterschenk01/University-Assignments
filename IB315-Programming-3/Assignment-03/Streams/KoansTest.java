import org.junit.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.IntStream;

public class KoansTest {

  @Test
  public void koansCountEvenNumbers(){
    assertEquals( 3, 
        Koans.countEvenNumbers(new int[] {4,1,7,2,5,8,1,11,19}));
    assertEquals( 50, 
        Koans.countEvenNumbers(IntStream.rangeClosed(1,100).toArray()));
  }

  @Test
  public void koansIntsFromStrings(){
    assertArrayEquals(
        new int[] {1,2,3,4,5,6,7},
        Koans.intsFromStrings("1","2","3","4","5","6","7"));
  }

  @Test
  public void koansIntsFromStringsEmpty(){
    assertArrayEquals(
        new int[] {},
        Koans.intsFromStrings());
  }

  @Test
  public void koansDistinctEntries(){
    List<String> entries = List.of("!", "Never", "gonna", "give", "give", "you", "you", "up", "!");

    assertEquals(6, Koans.distinctEntries(entries));

  }

  @Test
  public void koansRandomSum(){

    double[] sum = Koans.randomSum(100);

    assertEquals(100, sum.length);

    assertEquals(sum[0], 0.0, 1e-10);

    for (int i = 1; i < sum.length; i++){
      assertTrue(sum[i]-sum[i-1] > 0.0);
      assertTrue(sum[i]-sum[i-1] < 1.0);
    }

  }

  @Test
  public void koansDotProduct(){
    double[] v1 = {0.98909510799448504783, 0.51080125558694004582, 0.72887316220670377334, 0.19018707158727165309, 0.33141459949323331442, 0.33597903390470381167, 0.39380463271529446683, 0.45792259241150492173, 0.04689367875386725810, 0.03786283831542148175};
   double[] v2 = {0.78768803550160578516, 0.1668398460391853498, 0.6060313936517445804, 0.91682136937839495883, 0.1787814552797922012, 0.79811590975788233511, 0.01019162408664592606, 0.11841618544642980502, 0.27633949342146407424, 0.05302976304691972415};

    assertEquals(1.881014359959148794, Koans.dotProduct(v1,v2), 1e-5);
  }

  @Test
  public void koansCollatzSeries(){
    assertArrayEquals(
        new long[] {6, 3, 10, 5, 16, 8, 4, 2},
        Koans.collatzSeries(6).limit(100).takeWhile(n -> n != 1).toArray());
    assertArrayEquals(
        new long[] {15, 46, 23, 70, 35, 106, 53, 160, 80, 40, 20, 10, 5, 16, 8, 4, 2},
        Koans.collatzSeries(15).limit(100).takeWhile(n -> n != 1).toArray());
  }

  @Test
  public void koansCollatzTruncated(){
    assertArrayEquals(
        new long[] {6, 3, 10, 5, 16, 8, 4, 2, 1 },
        Koans.collatzTruncated(6).limit(100).toArray());
    assertArrayEquals(
        new long[] {15, 46, 23, 70, 35, 106, 53, 160, 80, 40, 20, 10, 5, 16, 8, 4, 2, 1},
        Koans.collatzTruncated(15).limit(100).toArray());
  }

  @Test
  public void koansCollatzOrbit(){
    assertTrue(Koans.collatzOrbit(6));
    assertTrue(Koans.collatzOrbit(16));
    assertTrue(Koans.collatzOrbit(123));
    assertTrue(Koans.collatzOrbit(930192190));
  }

  @Test
  public void koansCollatzTrueForLimit(){
    assertTrue(Koans.collatzTrueForLimit(100_000));
  }

  @Test
  public void koansFibonacciStream(){
    assertArrayEquals(
        new long[] {0, 1, 1, 2, 3, 5, 8, 13, 21, 34 },
       Koans.fibonacciStream().limit(10).toArray());
  }

  
}
