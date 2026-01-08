package imagefilter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.EventQueue;

public class ImageUtils{

  private static int returnValue;

  public static double[][] loadImage(){
    return loadImage(null);
  }

  public static double[][] loadImage(String path){

    File inputFile = null;

    if (path == null){

      JFileChooser fileChooser = new JFileChooser();

      try{
        EventQueue.invokeAndWait(() -> 
            {
              fileChooser.setDialogTitle("Open gray value image...");
              FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG 8 bit Gray Value Images", "png");
              fileChooser.setFileFilter(filter);

              returnValue = fileChooser.showOpenDialog(null);
            });

      } catch (InterruptedException e){
        e.printStackTrace();
      } catch (InvocationTargetException e){
        e.printStackTrace();
      }
      if (returnValue != JFileChooser.APPROVE_OPTION)
        return null;

      inputFile = fileChooser.getSelectedFile();

      fileChooser.setVisible(false);

    }else{

      inputFile = new File(path);
    }

    BufferedImage image = null;
    byte[] data = null;

    try{
      image = ImageIO.read(inputFile);

      if (image.getType() != BufferedImage.TYPE_BYTE_GRAY)        
        return null;

      data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

    } catch (IOException e){
      throw new UncheckedIOException(e);
    }

    double[][] pixels = new double[image.getWidth()][image.getHeight()];

    for (int x = 0; x < image.getWidth(); x++){
      for (int y = 0; y < image.getHeight(); y++){
        pixels[x][y] = ((int)(data[y*image.getWidth()+x] & 0xFF))/255.0;
      }
    }

    return pixels;
  }

  public static void saveImage(double[][] pixels){
    saveImage(pixels, null);
  }

  public static void saveImage(double[][] pixels, String path){

    File outputFile = null;
    if (path == null){
      JFileChooser fileChooser = new JFileChooser();
      try{
        EventQueue.invokeAndWait(() -> 
            {
              fileChooser.setDialogTitle("Save result as...");
              FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG 8 bit Gray Value Images", "png");
              fileChooser.setFileFilter(filter);

              returnValue = fileChooser.showSaveDialog(null);
            });

      } catch (InterruptedException e){
        e.printStackTrace();
      } catch (InvocationTargetException e){
        e.printStackTrace();
      }
      if (returnValue != JFileChooser.APPROVE_OPTION)
        return;

      outputFile = fileChooser.getSelectedFile();
      fileChooser.setVisible(false);
    }else{
      outputFile = new File(path);
    }

    int width = pixels.length;
    int height = pixels[0].length;
    byte[] data = new byte[width*height];

    for (int x = 0; x < width; x++){
      for (int y = 0; y < height; y++){
         data[y*width+x] = (byte) ((int)((pixels[x][y]*255)));
      }
    }

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    image.getRaster().setDataElements(0,0,width,height,data);
    try{
      ImageIO.write(image, "png", outputFile);
    } catch (IOException e){
      throw new UncheckedIOException(e);
    }

  }



}
