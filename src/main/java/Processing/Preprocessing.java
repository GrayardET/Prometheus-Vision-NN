package Processing;
import NeuralNetwork.ConvNet.FeatureBlock;
import NeuralNetwork.ConvNet.FeatureLayer;
import NeuralNetwork.math;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class Preprocessing {
    private JSONObject json;
    private FeatureLayer featureLayer= new FeatureLayer();
    private ArrayList<double[]> Y = new ArrayList<>();    // True Labels of the images.


    public Preprocessing(String path, Option option, boolean ifFlip) throws Exception {
        File file = new File(path);
        if (!file.isDirectory()){
            BufferedImage image = ImageIO.read(file);
            int w = image.getWidth();
            int h = image.getHeight();
            // if size doesn't match, resize the image to 400x400
            if(w != 400 || h != 400){
                BufferedImage resizedImg = ImageHandler.resize(file, 400,400);
                FeatureBlock tmp = ImageHandler.ImgToFeatureBlock(resizedImg,option, ifFlip);
                this.featureLayer.add(tmp);
            }
        }else {
            File[] files = file.listFiles();

            if (files.length == 0) {
                throw new Exception("There isn't any file in the given directory");
            } else {
                for (File curFile : files) {

                    if (ImageHandler.getExtension(curFile.getName()).get().equals("json")) {
                        String location = "Null";
                        try {
                            json = (JSONObject) new JSONParser().parse(new FileReader(curFile));
                            location = (String) json.get("location");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        switch (location) {
                            case "open":
                                double[] label1 = {1, 0, 0, 0};
                                Y.add(label1);
                                if (ifFlip)
                                    Y.add(label1);
                                break;
                            case "stair":
                                double[] label2 = {0, 1, 0, 0};
                                Y.add(label2);
                                if (ifFlip)
                                    Y.add(label2);
                                break;
                            case "room":
                                double[] label3 = {0, 0, 1, 0};
                                Y.add(label3);
                                if (ifFlip)
                                    Y.add(label3);
                                break;
                            case "hall":
                                double[] label4 = {0, 0, 0, 1};
                                Y.add(label4);
                                if (ifFlip)
                                    Y.add(label4);
                                break;
                        }

                        String imageName;
                        try {
                            imageName = path + curFile.getName().replace(".json", ".jpg");
                            FeatureBlock temp2;
                            BufferedImage image = ImageIO.read(new File(imageName));
                            // check if we want grayscale image as input
                            if (option == Option.Grayscale) {
                                FeatureBlock a = ImageHandler.ImgToFeatureBlock(image, Option.Grayscale, false);
                                featureLayer.add(a);
                                // check if flipped images are added
                                if (ifFlip) {
                                    FeatureBlock b = ImageHandler.ImgToFeatureBlock(image, Option.Grayscale, true);
                                    featureLayer.add(b);
                                }
                            } else {
                                FeatureBlock a = ImageHandler.ImgToFeatureBlock(image, option, false);
                                featureLayer.add(a);
                                if (ifFlip) {
                                    FeatureBlock b = ImageHandler.ImgToFeatureBlock(image, option, true);
                                    featureLayer.add(b);
                                }
                            }
                            System.out.println("Loading image " + imageName);

                        } catch (Exception e) {
                            System.out.println("Unable to find .jpg file");
                            e.printStackTrace();
                        }
                    }
                }
                // make sure that (#images == #labels)}
                if (!(featureLayer.size() == Y.size())) {
                    throw new Exception("#Labels and #images don't match");
                }
            }
        }
        System.out.println("Finished loading images!");
    }


    public FeatureLayer getFeatureLayer(){
        return this.featureLayer;
    }


    // transform Y and return it in a two dimensional form
    public double[][] getY(){
        int size = Y.size();
        double[][] Y_trans = new double[size][4];
        for(int i=0; i<size; i++){
            for(int j=0; j<4;j++){
                double[] temp = Y.get(i);
                Y_trans[i][j] = temp[j];
            }
        }
        return math.T(Y_trans);
    }
}
