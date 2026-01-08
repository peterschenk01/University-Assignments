import org.junit.*;
import static org.junit.Assert.*;

public class RealFunctionTest {
  @Test
  public void realFunctionConstant(){
    RealFunction f = RealFunction.constant(3.21);

    assertEquals(3.21, f.apply(5.2), 1e-10);
    assertEquals(3.21, f.apply(-2.9), 1e-10);
  }

  @Test
  public void realFunctionLinear(){
    RealFunction f = RealFunction.linear(2.4,3.3);

    assertEquals(15.78, f.apply(5.2), 1e-10);
    assertEquals(-3.66, f.apply(-2.9), 1e-10);
  }

  @Test
  public void realFunctionSine(){
    RealFunction f = RealFunction.sine(3.1,2.1);

    assertEquals(-2.7018848944821229861, f.apply(2), 1e-10);
    assertEquals(-2.2554772020115240464, f.apply(-4.1), 1e-10);
  }

  @Test
  public void realFunctionExp(){
    RealFunction f = RealFunction.exp();

    assertEquals(8.1661699125676500734, f.apply(2.1), 1e-10);
    assertEquals(5.60279643753726754e-9, f.apply(-19), 1e-10);
  }

  @Test 
  public void realFunctionAddTo(){
    RealFunction f = RealFunction.sine(2.1,-1.5);
    RealFunction g = RealFunction.linear(-9.1,0.8);
    RealFunction a = f.addTo(g);

    assertEquals(26.914085678, a.apply(-3.1), 1e-5);
    assertEquals(0.6775011812, a.apply(+0.01), 1e-5);
  }

  @Test 
  public void realFunctionComposeWith(){
    RealFunction f = RealFunction.sine(2.1,-1.5);
    RealFunction g = RealFunction.linear(-9.1,0.8);
    RealFunction a = f.composeWith(g);

    assertEquals(0.94599720377109849636, a.apply(-3.1), 1e-5);
    assertEquals(-1.8355284947290766127, a.apply(0.01), 1e-10);
  }

  @Test 
  public void realFunctionMultiplyWith(){
    RealFunction f = RealFunction.sine(2.1,-1.5);
    RealFunction g = RealFunction.linear(-9.1,0.8);
    RealFunction h = RealFunction.exp();
    RealFunction p = f.multiplyWith(g,h);

    assertEquals(-2.7391029781411840338, p.apply(-3.1), 1e-10);
    assertEquals(-0.022557109492763107232, p.apply(0.01), 1e-10);
  }

  @Test
  public void realFunctionApproxDiff(){

    RealFunction f = RealFunction.sine(2.1,-1.5);
    RealFunction fd = f.approxDiff(1e-5);

    assertEquals(0.19639782101202483368, fd.apply(-3.1), 1e-3);
    assertEquals(-3.1496456316444814162, fd.apply(0.01), 1e-3);
  }

  @Test
  public void realFunctionMax(){
    RealFunction f = RealFunction.sine(2.1,-1.5);
    RealFunction g = RealFunction.sine(2.1,-1.5).approxDiff(1e-5);
    RealFunction h = RealFunction.linear(-9.1,0.8);
    RealFunction m = f.max(g,h);

    assertEquals(29.01, m.apply(-3.1), 1e-5); // cos
    assertEquals(0.709, m.apply(0.01), 1e-5); // sin
    assertEquals(3.1472172480411621551, m.apply(10.5), 1e-5); // linear

  }
  
}
