import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.dataset.DataSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import org.canova.api.records.reader.RecordReader;
import org.canova.api.records.reader.impl.CSVRecordReader;
import org.canova.api.split.FileSplit;
import org.deeplearning4j.datasets.canova.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.DataSetIterator;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.*;

  

public class ExecuteServlet extends HttpServlet{

    protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException  {

      try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            System.out.println("In ExecuteServlet");
            String mType = request.getParameter("mType");	
            String dType = request.getParameter("dType");
            String data = request.getParameter("data");
            int count;
            if (dType.equals("3")) count = 3;
            else count = 23;
            int i = 0;
            double inst[] = new double[count];
            for (String str : data.split(","))
                inst[i++] = Double.parseDouble(str);
            if (i == (count)) System.out.println("Input Read Successfully: i: " + i);
            for(double d:inst)System.out.println(d);

            //Creating New Instance here
            Instance instance = new DenseInstance(inst);
            String doc="<!DOCTYPE html><html lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1.0\"/><title>Machine Learning for Medical Data</title><!-- CSS  --><link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\"><link href=\"css\\materialize.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen,projection\"/><link href=\"css\\style.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen,projection\"/></head><body background=\"rsc/parallax.jpg\" onload=\"test();\"><div class=\"navbar-fixed\"><nav class=\"blue darken-1\" role=\"navigation\"><div class=\"nav-wrapper container\"><a id=\"logo-container\" href=\"index.html\" class=\"brand-logo\"><i class=\"material-icons \" style=\"font-size: 30px\">polymer</i></a></div></nav></div><div class=\"container white-text text-darken-1\"><div class=\"section\"><div class=\"row hoverable\"><h4 class=\"center light\">";
            //Creating Document
            out.println(doc);
            //System.out.println("Trying to create instance");
            //INDArray app= Nd4j.create(inst);
            //System.out.println("After creating test inst: " +app);

            if (!mType.equals("ann")) {
                ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("C:/apache-tomcat-8.0.33/webapps/MachineLearningForMedicalDataSets/models/" + mType + "" + dType + ".model"));
                Classifier cls = (Classifier) ois.readObject();
                ois.close();

                Object predictedClassValue=cls.classify(instance);
                out.println("The class predicted by the model is: "+predictedClassValue);
            }
            else
            {
                
            	  INDArray features;
            	   if(count==3)
            	   {
  	            		
                    features=Nd4j.create(inst); 
                    System.out.println("Features are: "+features);
                  }
        				else
        				{
        					System.out.println("Inside ANN Else statment:# 23");
        					double outer[]={0,0,0,0,0,1};
  	        			INDArray outs=Nd4j.create(outer);
  	        			System.out.println("Created Labels for input");
  	        			INDArray tester= Nd4j.create(inst);
  	        			DataSet add= new DataSet();
  	        			add.setFeatures(tester);
  	        			add.setLabels(outs);
  	        			RecordReader testSet= new CSVRecordReader();
         					testSet.initialize(new FileSplit(new File("C:/apache-tomcat-8.0.33/webapps/MachineLearningForMedicalDataSets/models/Feature_test_ann.csv")));
        					DataSetIterator testIter = new RecordReaderDataSetIterator(testSet,269,0,6);
        					DataSet test_set= testIter.next();
        					test_set.addRow(add,268);
        					test_set.normalizeZeroMeanZeroUnitVariance();
        					add=test_set.get(268);
        					features=add.getFeatures();
                  System.out.println("Features are:"+features);
        				}



                MultiLayerConfiguration confFromJson = MultiLayerConfiguration.fromJson(FileUtils.readFileToString(new File("C:/apache-tomcat-8.0.33/webapps/MachineLearningForMedicalDataSets/models/ann"+dType+".json")));
                DataInputStream dis = new DataInputStream(new FileInputStream("C:/apache-tomcat-8.0.33/webapps/MachineLearningForMedicalDataSets/models/ann"+dType+".bin"));
                INDArray newParams = Nd4j.read(dis);
                dis.close();
                MultiLayerNetwork model = new MultiLayerNetwork(confFromJson);
                model.init();
                model.setParams(newParams);
                System.out.println("Model Re-created from stored .json and .bin files");
                INDArray predicted = model.output(features,false);
                System.out.println("Output predicted!: "+predicted);
                int j=0;
                int class_count;
                if(count==3)class_count=3;
                else class_count=6;
                out.println("The class predicted by the Model is: ");
                int max_prob=predicted.getInt(0);
                int max_class=0;
                if(count==3)
                {
                  while(j<class_count)
                  {
                    if(max_prob<predicted.getInt(j)){max_prob=predicted.getInt(j);max_class=j;}
                    j++;
                  }
                  out.println((max_class));

                }
                else
                {
                    while(j<class_count)
                    {
                      if(max_prob<predicted.getInt(j)){max_prob=predicted.getInt(j);max_class=j;}
                      j++;
                    }
                     out.println((max_class+1));
                }
                //response.setContentType("text/html");
           		//out = response.getWriter();
            }

           out.println("</h4>");

        }catch(Exception e){System.out.println("Caught Exception!");e.printStackTrace();}

    }

}
