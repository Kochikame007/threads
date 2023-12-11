package org.example.optimization;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

public class Latency {
    public static final String SOURCE_FILE = "./src/main/resources/many-flowers.jpg";
    public static final String OUTPUT_FILE = "./src/main/resources/output-flowers.jpg";

    public static void main(String[] args)  {

        IIORegistry registry = IIORegistry.getDefaultInstance();
        Iterator<ImageWriterSpi> serviceProviders = registry.getServiceProviders(ImageWriterSpi.class, false);
        while(serviceProviders.hasNext()) {
            ImageWriterSpi next = serviceProviders.next();
            System.out.printf("description: %-27s   format names: %s%n",
                    next.getDescription(Locale.ENGLISH),
                    Arrays.toString(next.getFormatNames())
            );
        }


       try {
           BufferedImage i = ImageIO.read(new File(SOURCE_FILE));
           BufferedImage resultImage = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
           System.out.println("start");
           recolorSingleThreadedImage(i, resultImage);
           File outputFile = new File(OUTPUT_FILE);
           boolean resultvalue = ImageIO.write(resultImage, "jpg", outputFile);
           System.out.println("end " + resultvalue);
       }catch(IOException e) {
           e.printStackTrace();
       }

    }

    public static void recolorSingleThreadedImage(BufferedImage image, BufferedImage result) {
        recolorImage(image, result, 0, 0, image.getWidth(), image.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage result, int leftCorner, int rightCorner, int width, int height) {
        for(int i = leftCorner ; i<leftCorner+width && i<originalImage.getWidth();i++){
            for(int k = rightCorner ; k<rightCorner+height && k<originalImage.getHeight();k++){
                recolorPixel(originalImage , result , i,k);
            }
        }
    }

    public static void recolorPixel(BufferedImage original, BufferedImage result, int x , int y){
            int rgb = original.getRGB(x,y);

            int red = getRed(rgb);
            int green = getGreen(rgb);
            int blue = getBlue(rgb);

            int newRed;
            int newGreen;
            int newBlue;

            if(isShadeOfGrey(red, green, blue)){
                newRed = Math.min(255, red+10);
                newGreen = Math.max(0, green-80);
                newBlue = Math.max(0 , blue-20);
            }else{
                newRed = red;
                newGreen = green;
                newBlue = blue;
            }
            int newRGB = createRgb(newRed,newGreen,newBlue);
            setRGB(result ,x,y,newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGrey(int red, int blue, int green) {
        return Math.abs(red - green) < 30 && Math.abs(green - blue) < 30 && Math.abs(red - blue) < 30;
    }

    public static int createRgb(int red, int blue, int green) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0XFF000000;

        return rgb;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0X0000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        return (rgb & 0X000000FF);
    }
}
