package imagefilter;

import java.util.concurrent.*;
import java.util.function.*;

public class ImageFilter {

	public static void main(String[] args) throws InterruptedException{
		if(args.length == 0) {
			double[][] image = ImageUtils.loadImage();
			int width = image.length;
			int height = image[0].length;
			double[][] resultImage = filterImageParallel(image, width, height, v -> 1.0 - v, 7);
			ImageUtils.saveImage(resultImage);
		}
		else {
			ImageFilterPipeline pipeline = new ImageFilterPipeline(args);
			pipeline.start();
		}
	}
	
	public static double[][] filterImage(double[][] pixels, int width, int height, DoubleUnaryOperator filter){
		double[][] result = new double[width][height];
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				result[x][y] = filter.applyAsDouble(pixels[x][y]);
			}
		}
		
		return result;
	}
	
	public static void filterImage(double[][] source, double[][] other, int from, int to, int height, DoubleUnaryOperator filter){
		for(int x = from; x >= from && x < to; x++) {
			for(int y = 0; y < height; y++) {
				other[x][y] = filter.applyAsDouble(source[x][y]);
			}
		}
	}
	
	public static double[][] filterImageParallel(double[][] pixels, int width, int height, DoubleUnaryOperator filter, int numThreads) throws InterruptedException{
		ExecutorService threads = Executors.newFixedThreadPool(numThreads);

		double[][] result = new double[width][height];
		
		int spaltenProThread = width / numThreads;
		int rest = width % numThreads;
		int a = 0;
		int b = spaltenProThread;

		for(int i = 0; i < numThreads; i++) {
			final int from = a;
			final int to = b;
			threads.execute(() -> {
				filterImage(pixels, result, from, to, height, filter);
			});
			
			a = to;
			if(numThreads - i == 2 && rest != 0) {
				b = width;
			}
			else {
				b += spaltenProThread;
			}
		}
		
		threads.shutdown();
		
		try{
			while(!threads.awaitTermination(1, TimeUnit.SECONDS))
				System.out.println("Still waiting");
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		
		return result;
	}

}
