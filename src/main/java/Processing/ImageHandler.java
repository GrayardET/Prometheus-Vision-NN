package Processing;

import NeuralNetwork.ConvNet.FeatureBlock;
import org.json.simple.JSONObject;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageHandler {

    public static BufferedImage ToGray(BufferedImage inputImage) {
        BufferedImage image = inputImage;
            int width = image.getWidth();
            int height = image.getHeight();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color c = new Color(image.getRGB(j, i));
                    int red = (int) (c.getRed() * 0.299);
                    int green = (int) (c.getGreen() * 0.587);
                    int blue = (int) (c.getBlue() * 0.114);
                    Color newColor = new Color(red + green + blue,
                            red + green + blue, red + green + blue);
                    image.setRGB(j, i, newColor.getRGB());
                }
            }
        return image;
    }

    public static int[][] extractRGB(BufferedImage image){

            int imageHeight = image.getHeight();
            int imageWidth = image.getWidth();

            int[][] pixelRGBs = new int[imageHeight][imageWidth];

            for (int i = 0; i < imageHeight; i++) {
                for (int j = 0; j < imageWidth; j++) {
                    pixelRGBs[i][j] = image.getRGB(j, i);
                }
            }

            return pixelRGBs;
    }


    public static FeatureBlock ImageToFeatureBlock(int[][] RGB){
        return new FeatureBlock( "Original", RGB);
    }


    /**
     * map image to feature block
     * @param image the input BufferedImage
     * @param option Grayscale?
     * @return featureBlock
     */
    public static FeatureBlock ImgToFeatureBlock(BufferedImage image, Option option, boolean flip){
        FeatureBlock result;
        int[][] RGBValue = new int[400][400];
        if(option == Option.Grayscale) {
            try {
                if(!flip)
                    RGBValue = ImageHandler.extractRGB(ToGray(image));
                else
                    RGBValue = ImageHandler.extractRGB(flip(ToGray(image)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            if(!flip)
                RGBValue = ImageHandler.extractRGB(image);
            else
                RGBValue = ImageHandler.extractRGB(flip(image));
        }
        result = ImageHandler.ImageToFeatureBlock(RGBValue);

        return result;
    }

    public static BufferedImage flip(BufferedImage input){
        BufferedImage flippedImage = new BufferedImage(input.getWidth(),input.getHeight(),input.getType());
        flippedImage.setData(input.getData());
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-input.getWidth(null),0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        flippedImage = op.filter(input,null);
        return flippedImage;
    }


    //Showing colored image might be wrong.
    public static void show(int[][] RGB, int height, int width, Option option) {
        BufferedImage b;
        if(option == Option.Grayscale) {
            b = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }else{
            b = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        }
        for(int x = 0; x < height; x++) {
            for(int y = 0; y < width; y++) {
                b.setRGB(y, x, RGB[x][y]);
            }
        }

        show(b);
    }

    public static void show(BufferedImage image) {
        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".")+1));
    }

    /**
     * resize one image to a given height and width
     * @param inputFile source image
     * @param outputPath outputPath
     * @param newH target height
     * @param newW target width
     * @throws IOException cannot find target directory
     */
    public static void resize(File inputFile, String outputPath, int newH, int newW) throws IOException{
        BufferedImage inputImg = ImageIO.read(inputFile);

        BufferedImage outputImg = new BufferedImage(newW,newH, inputImg.getType());

        //scales inputImg to outputImg
        Graphics2D graphics2D = outputImg.createGraphics();
        graphics2D.drawImage(inputImg, 0, 0, newW, newH, null);
        graphics2D.dispose();

        //extracts extension of output file
        String format = getExtension(inputFile.getName()).get();
        outputPath = outputPath + inputFile.getName();
        ImageIO.write(outputImg, format, new File(outputPath));
    }

    public static BufferedImage resize(File inputFile, int newH, int newW) throws IOException {
        BufferedImage inputImg = ImageIO.read(inputFile);

        BufferedImage outputImg = new BufferedImage(newW,newH, inputImg.getType());

        //scales inputImg to outputImg
        Graphics2D graphics2D = outputImg.createGraphics();
        graphics2D.drawImage(inputImg, 0, 0, newW, newH, null);
        graphics2D.dispose();

        return outputImg;
    }


    /**
     * resize a folder of images to a given height and width
     * @param inputPath
     * @param outputPath
     * @param newHeight target height
     * @param newWidth target width
     */
    public static void resize(String inputPath, String outputPath, int newHeight, int newWidth){
        File directory = new File(inputPath);
        File[] files = directory.listFiles();
        for(File file:files){
            try {
                System.out.println("Resizing image: "+ (inputPath + file.getName()));
                resize(file, outputPath, newHeight, newWidth);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Method creates a json file with specified filename and location, then outputs it in a directory
     * @param filename name of the file
     * @param location keep the information for labeling of the image
     * @param outputDirectory directory to output the json file
     */
    public static void writeJson(String filename, String location, String outputDirectory){
        JSONObject obj = new JSONObject();
        obj.put("location", location);
        String name = outputDirectory + filename.replace(".jpg", ".json");
        try {
            FileWriter fileWriter = new FileWriter(name);
            fileWriter.write(obj.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method creates a json file for every file in the directory(specifically containing images) with a location
     * then output them all into a directory
     * @param inputDirectory
     * @param location the label of image, this will be read for training purposes.
     * @param outputDirectory
     */
    public static void ImgToJson(String inputDirectory, String location, String outputDirectory){
        File input = new File(inputDirectory);
        File[] files = input.listFiles();
        for(File file: files){
            writeJson(file.getName(), location, outputDirectory);
        }
    }



    public static void main(String args[]) throws IOException {
        resize("open\\", "openScaled\\",400, 400);
//        writeJson("testJson", "open", "testJson\\");
        ImgToJson("openScaled\\", "open", "openScaled\\");
    }
}
