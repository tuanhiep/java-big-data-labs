package io.github.tuanhiep.bigdata.linearregression.training;

import Jama.Matrix;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.*;

import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class LinearRegressionTrainingJob {

    public static void main(String[] args) throws IOException, InterruptedException {

        // Delete folder output if it exists
        File index = new File("src/main/resources/output");
        if (index.exists()) {
            String[] entries = index.list();
            for (String s : entries) {
                File currentFile = new File(index.getPath(), s);
                currentFile.delete();
            }
            index.delete();
        }
        // Start configure the job MapReduce
        JobConf conf = new JobConf(LinearRegressionTrainingJob.class);
        conf.setJobName("linearRegression");
        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(MatrixWritable.class);
        conf.setMapperClass(LinearRegressionTrainingMapper.class);
        conf.setCombinerClass(LinearRegressionTrainingReducer.class);
        conf.setReducerClass(LinearRegressionTrainingReducer.class);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));
        try {
            JobClient.runJob(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sleep(3000);
        // Compute weights for linear regression model
        LinearRegressionModelWriter classifier = new LinearRegressionModelWriter();
        Matrix XtX = classifier.readMatrixFromFile("src/main/resources/model-parameters/result_0.csv");
        Matrix Xty = classifier.readMatrixFromFile("src/main/resources/model-parameters/result_1.csv");
        // Calculate the weights of linear regression model by normal equation
        Matrix weights = (XtX.inverse()).times(Xty);
        classifier.writeMatrixToFile(weights, "src/main/resources/model-parameters/weights.csv");


    }
}
