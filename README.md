This is a project that builds convolutional neural network from scrach using Java that does scene detection. The four categories are stairs, room, hallway, and open. This CNN uses customizable kernels for feature extraction, allowing users to combine basic kernels (like horizontal and vertical detection, Etc.) to form new kernels that detect meaningful characteristics of an image. 
Users can either train their own model from scrach with layers sizes and number of hidden neurons of their choice, or make prediction to images using the current model.

##Usage

To train the CNN:
    Watch my demo video on [Demo Video](https://www.youtube.com/watch?v=hOvhwzl06vo)
    To make prediction using the current model:
    * Use function Parameters.processImage(String pathToImage) in the driver
    * Program driver is under Prometheus-Vision-NN/tree/master/src/main/java/NeuralNetwork/Driver.py