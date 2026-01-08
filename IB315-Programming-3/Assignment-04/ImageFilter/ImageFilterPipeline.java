package imagefilter;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ImageFilterPipeline {

  private final String[] imageFiles;

  double[][] loadedImage;

  double[][] filteredImage;

  public ImageFilterPipeline(String[] imageFiles){
    this.imageFiles = imageFiles;
  }

  private void loadImages(ReentrantLock loadedImageLock, Condition loadedImageReceived, Condition loadedImageProduced){
	  for(String imageFile : imageFiles) {
		  double[][] localLoadedImage = ImageUtils.loadImage(imageFile);
		  loadedImageLock.lock();
		  
		  try {
			  while(loadedImage != null) 
				  loadedImageReceived.await();
			  
			  loadedImage = localLoadedImage;
			  
			  loadedImageProduced.signal();
		  } catch(InterruptedException e) {
			  e.printStackTrace();
			  Thread.currentThread().interrupt();
			  return;
		  } finally {
			  loadedImageLock.unlock();
		  }
	  }

  }

  private void filterImages(ReentrantLock loadedImageLock, ReentrantLock filteredImageLock, Condition loadedImageReceived, Condition loadedImageProduced, 
		  					Condition filteredImageReceived, Condition filteredImageProduced){
	  for(int i = 0; i < imageFiles.length; i++) {
		  double[][] localImageLoaded = null;
		  loadedImageLock.lock();
		  
		  try {
			  while(loadedImage == null)
				  loadedImageProduced.await();
				
			  localImageLoaded = loadedImage;
			  loadedImage = null;
				  
			  loadedImageReceived.signal();
		  } catch (InterruptedException e) {
			  e.printStackTrace();
			  Thread.currentThread().interrupt();
		  } finally {
			  loadedImageLock.unlock();
		  }
		  
		  double[][] localFilteredImage = ImageFilter.filterImage(localImageLoaded, localImageLoaded.length, localImageLoaded[0].length, v -> 1.0 - v);
		  filteredImageLock.lock();
		  
		  try {
			  while(filteredImage != null)
				  filteredImageReceived.await();
			  
			  filteredImage = localFilteredImage;
			  
			  filteredImageProduced.signal();
		  } catch(InterruptedException e) {
			  e.printStackTrace();
			  Thread.currentThread().interrupt();
		  } finally {
			  filteredImageLock.unlock();
		  }
	  }
  }

  private void saveImages(ReentrantLock filteredImageLock, Condition filteredImageReceived, Condition filteredImageProduced){
	  for(String imageFile : imageFiles) {
		  double[][] localFilteredImage = null;
		  filteredImageLock.lock();
		  
		  try {
			  while(filteredImage == null)
				  filteredImageProduced.await();
			  
			  localFilteredImage = filteredImage;
			  filteredImage = null;
			  
			  filteredImageReceived.signal();
		  } catch(InterruptedException e) {
			  e.printStackTrace();
			  Thread.currentThread().interrupt();
		  } finally {
			  filteredImageLock.lock();
		  }
		  
		  ImageUtils.saveImage(localFilteredImage, "filtered-" + imageFile);
	  }
  }

  public void start(){
	  ReentrantLock loadedImageLock = new ReentrantLock();
	  ReentrantLock filteredImageLock = new ReentrantLock();
	  
	  Condition loadedImageReceived = loadedImageLock.newCondition();
	  Condition loadedImageProduced = loadedImageLock.newCondition();
	  Condition filteredImageReceived = filteredImageLock.newCondition();
	  Condition filteredImageProduced = filteredImageLock.newCondition();
	  
	  (new Thread(() -> loadImages(loadedImageLock, loadedImageReceived, loadedImageProduced))).start();
	  (new Thread(() -> filterImages(loadedImageLock, filteredImageLock, loadedImageReceived, loadedImageProduced, 
		  					filteredImageReceived, filteredImageProduced))).start();
	  
	  saveImages(filteredImageLock, filteredImageReceived, filteredImageProduced);
	  
  }


  
}
