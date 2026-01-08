@FunctionalInterface
public interface RealFunction {
	public double apply(double x);
	
	public static RealFunction constant(double c) {
		return x -> c;
	}
	
	public static RealFunction linear(double a, double b){
		return x -> a*x + b;
	  }

	public static RealFunction sine(double a, double f){
		return x -> a*Math.sin(f*x);
	}

	public static RealFunction exp(){
		return x -> Math.exp(x);
	}
	
	public default RealFunction addTo(RealFunction f) {
		return x -> this.apply(x) + f.apply(x);
	}
	
	public default RealFunction composeWith(RealFunction f) {
		return x -> this.apply(f.apply(x));
	}
	
	public default RealFunction multiplyWith(RealFunction... f) {
		return x -> {
			double result = this.apply(x);
			for(RealFunction h : f)
				result *= h.apply(x);
			return result;
		};
	}
	
	public default RealFunction approxDiff(double h) {
		return x -> (this.apply(x + h) - this.apply(x)) / h;
	}
	
	public default RealFunction max(RealFunction... f) {
		return x -> {
			double max = this.apply(x);
			for(RealFunction h : f)
				max = Math.max(max, h.apply(x));
			return max;
		};
	}
}
