import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.servlet.http.HttpServlet;
import java.io.*;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Shikhar Srivastava
 */
public class ExecuteServlet extends HttpServlet {

  static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {

        try {
            response.setContentType("text/html");
            System.out.println("In ExecuteServlet");

            String mType = request.getParameter("mType");
            System.out.println("mType: " + mType);
            String imgName = request.getParameter("imgName");
            System.out.println("imgName: " + imgName);
            String[] imgNames = imgName.split("\\|\\|");
            System.out.println("Image(s) selected: ");
            String pathname, threshFilename, outputFilename;
            PrintWriter out=response.getWriter();
            String docstart="<!DOCTYPE html><html lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1.0\"/><meta name=\"theme-color\" content=\"#5e42a6\"><title>ML for BMI</title><link rel=\"icon\" type=\"icon/ico\" href=\"rsc/icon.ico\"/><link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\"><link href=\"css\\materialize.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen,projection\"/><link href=\"css\\style.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen,projection\"/><link href=\"css\\dropify.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen,projection\"/><link href=\"css\\imgcenter.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen,projection\"/></head><body><div class=\"navbar-fixed\"><nav class=\"blue darken-3\" role=\"navigation\"><div class=\"row\"><div class=\"nav-wrapper container col s12\"><div class=\"nav-wrapper container col s12\"><a id=\"logo-container\" href=\"index.html\" class=\"brand-logo\"><i class=\"material-icons \" style=\"font-size: 35px\">polymer</i></a></div></div></div></nav></div><div class=\"parallax-container valign-wrapper hero \"><div class=\"container\"><div class=\"row center\"><h5 class=\"header col s12 light\"><br><br>Classification Result: <br><br><br></h5></div>";
            String docend="<div class=\"parallax\"><img src=\"rsc/lawn.jpg\" alt=\"rsc/lawn.jpg\"></div></div></div><div class=\"section z-depth-5 white  darken-3\"><div class=\"container\"><div class=\"row\"><div class=\"col s12 m12\"><div class=\"icon-block\"><h5 class=\"center light\">ML for BMI</h5><h2 class=\"center light-blue-text\" ><a href=\"index.html\" ><i class=\"material-icons indigo-text\" style=\"font-size: 90px\">polymer</i></a></h2><h5 class=\"light\" align='center'>Using <b>Machine Learning</b> to solve Bio-Medical Imaging problems<br></h5></div></div></div></div></div><div class=\"parallax-container valign-wrapper\"><div class=\"container\"><h5 class=\"light\" align=\"center\"><br><br>Close this <b>page</b> to try other models </h5></div><div class=\"parallax\"><img src=\"rsc/lawn.jpg\" alt=\"Unsplashed background img 2\"></div></div><footer class=\"page-footer red darken-1\"><div class=\"container\"><div class=\"row\"><div class=\"col l6 s12 offset-l8\"><h5 class=\"white-text\">Machine Learning for Bio Medical Imaging</h5><p class=\"grey-text text-lighten-4\">Using Machine Learning models to solve problems in BioMedical Imaging</p></div></div></div><div class=\"footer-copyright\"><div class=\"container\">Made by <a class=\"red-text text-lighten-3\" href=\"#!\">Shikhar Srivastava</a> @IITKGP Summer2016</div></div></footer></body><script src=\"js/jquery-2.2.2.min.js\"></script><script src=\"js/materialize.js\"></script><script src=\"js/init.js\"></script><script src=\"js/dropify.js\"></script><script>$(document).ready(function() {$('.parallax').parallax();});</script><script>!function(n,r){\"function\"==typeof define&&define.amd?define(r):\"object\"==typeof exports?module.exports=r():n.transformicons=r()}(this||window,function(){\"use strict\";var n={},r=\"tcon-transform\",t={transform:[\"click\"],revert:[\"click\"]},e=function(n){return\"string\"==typeof n?Array.prototype.slice.call(document.querySelectorAll(n)):\"undefined\"==typeof n||n instanceof Array?n:[n]},o=function(n){return\"string\"==typeof n?n.toLowerCase().split(\" \"):n},f=function(n,r,f){var c=(f?\"remove\":\"add\")+\"EventListener\",u=e(n),s=u.length,a={};for(var l in t)a[l]=r&&r[l]?o(r[l]):t[l];for(;s--;)for(var d in a)for(var v=a[d].length;v--;)u[s][c](a[d][v],i)},i=function(r){n.toggle(r.currentTarget)};return n.add=function(r,t){return f(r,t),n},n.remove=function(r,t){return f(r,t,!0),n},n.transform=function(t){return e(t).forEach(function(n){n.classList.add(r)}),n},n.revert=function(t){return e(t).forEach(function(n){n.classList.remove(r)}),n},n.toggle=function(t){return e(t).forEach(function(t){n[t.classList.contains(r)?\"revert\":\"transform\"](t)}),n},n});</script></html>";
            //Creating Document
             String fullpath="C:/apache-tomcat-8.0.36/webapps/MachineLearningIITKGP/";
            out.println(docstart);
            for (String name : imgNames) {

               
                pathname ="uploads/" + name;
                System.out.println("Path name: "+pathname);

                //~~~~~~~~~~~~~~~~Reading Uploaded Image~~~~~~~~~~~~
                Mat inputImg = Highgui.imread(fullpath+pathname, Highgui.CV_LOAD_IMAGE_COLOR);
                
                System.out.println("Sample Pixel vals: "+inputImg.get(0,0)[0]);

                // ~~~~~~~~~~~~~~~~~~~~~~Adaptive Thresholding~~~~~~~~~~~~~~~~~~~~~~
                Mat thresholded = AdaptSegmentImg(inputImg);  
                  System.out.println("Thresholded Image Created");

                threshFilename = "output/" + name.substring(0, name.indexOf('.')) + "_thresh" + name.substring(name.indexOf('.'));
                Highgui.imwrite(fullpath+threshFilename, thresholded);
                  System.out.println("Thresholded Image Saved. At: "+fullpath+threshFilename);
                outputFilename = "output/" + name.substring(0, name.indexOf('.')) + "_out" + name.substring(name.indexOf('.'));

                Mat outputImg = new Mat(inputImg.rows(), inputImg.cols(), inputImg.type());
                System.out.println("Creating new outputImg");
                if (mType.equals("nmc") || mType.equals("knn") || mType.equals("nvb") || mType.equals("svm") || mType.equals("sosvm") || mType.equals("rbfsvm") || mType.equals("rff") || mType.equals("rnt"))
                {
                    System.out.println("Using JavaML Classifier....");
                    Classifier model;
                    InputStream is = new ObjectInputStream(
                        new FileInputStream("C:/apache-tomcat-8.0.36/webapps/MachineLearningIITKGP/models/" + mType + ".bin"));
                    ObjectInputStream ois = (ObjectInputStream) is;
                    model = (Classifier) ois.readObject();
                       System.out.println("Loaded Classifier Model...JAVAML");
                    Instance inst;
                    double arr[];
                    for (int i = 0; i < inputImg.rows(); i++) {
                        for (int j = 0; j < inputImg.cols(); j++) {

                            if (thresholded.get(i, j)[0] > 0) {
                                arr = inputImg.get(i, j);
                                inst = new DenseInstance(new double[]{arr[2], arr[1], arr[0]});
                                int predicted = Integer.parseInt(model.classify(inst).toString());
                                if (predicted == 1) {
                                    outputImg.put(i, j, new double[]{255, 0, 0});
                                } else outputImg.put(i, j, new double[]{0, 255, 0});
                            } else outputImg.put(i, j, inputImg.get(i, j));

                        }
                    }
                    System.out.println("Output Image Created..");

                } else {

                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Reading DeepLearning4j Models here~~~~~~~~~~~~~~~~~~~~~

                    MultiLayerConfiguration confFromJson = MultiLayerConfiguration.fromJson(FileUtils.readFileToString(new File("C:/apache-tomcat-8.0.36/webapps/MachineLearningIITKGP/models/"+mType+".json")));
                    DataInputStream dis = new DataInputStream(new FileInputStream("C:/apache-tomcat-8.0.33/webapps/MachineLearningIITKGP/models/"+mType+".bin"));
                    INDArray newParams = Nd4j.read(dis);
                    dis.close();
                    MultiLayerNetwork model = new MultiLayerNetwork(confFromJson);
                    model.init();
                    model.setParams(newParams);
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Model recreated~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                    System.out.println("Model Re-created from stored .json and .bin files");

                    INDArray predicted;
                    int max_prob,k,max_class;
                    for (int i = 0; i < inputImg.rows(); i++) {
                        for (int j = 0; j < inputImg.cols(); j++) {

                            if (thresholded.get(i, j)[0] > 0) {

                                predicted=model.output(Nd4j.create(new double[]{inputImg.get(i,j)[2],inputImg.get(i,j)[1],inputImg.get(i,j)[0]}),false);
                                max_prob=predicted.getInt(0);
                                max_class=0;
                                k=0;
                                while(k<2)
                                {
                                    if(max_prob<predicted.getInt(k)){max_prob=predicted.getInt(k);max_class=k;}
                                    k++;
                                }
                                max_class++;
                                if (max_class == 1) {
                                    outputImg.put(i, j, new double[]{255, 0, 0});
                                } else outputImg.put(i, j, new double[]{0, 255, 0});

                            }
                            else outputImg.put(i, j, inputImg.get(i, j));


                        }
                    }


                }
                //~~~~~~~~~~~~~~~Writing Output Image~~~~~~~~~~~~~~~~~~~
                Highgui.imwrite(fullpath+outputFilename, outputImg);
               System.out.println("Output Image written to: "+fullpath+outputFilename);
               
               //~~~~~~~~~~~~~~~~Writing Image HTML Script to Pop-Up Page~~~~~~~~~~~~~~~~~~~~~~~~
                out.println("<div class=\"row\" align=\"center\"><img class=\"display materialboxed\" width=\"400\" align=\"center\" style=\"float:right\" src=\""+pathname+"\"><img class=\"display materialboxed\" width=\"400\" align=\"center\" style=\"float:left\" src=\""+outputFilename+"\"></div>");

            }
             
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Printing Pop-Up Page-HTML/JavaScript here~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                   out.println(docend);
        }catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception found in main doGet method");
        }






    }
        public Mat AdaptSegmentImg(Mat inputImg)
        {
            Mat result = new Mat();
            ArrayList<Mat> rgb_planes = new ArrayList<>();
            Core.split(inputImg, rgb_planes);
            int win = inputImg.cols()/20;
            win = 2 * ( (win/ 2 )) + 1;
            Imgproc.adaptiveThreshold(rgb_planes.get(1), result, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, win , 1);
            return result;
        }


}

