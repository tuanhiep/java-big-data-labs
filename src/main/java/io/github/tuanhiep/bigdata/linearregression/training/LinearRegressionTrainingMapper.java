package io.github.tuanhiep.bigdata.linearregression.training;

import Jama.Matrix;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;

public class LinearRegressionTrainingMapper extends MapReduceBase
        implements Mapper<LongWritable, Text, LongWritable, MatrixWritable> {
    /**
     * to map 2 parts of normal equation so that they will be computed by summed up
     *
     * @param key
     * @param value
     * @param output
     * @param reporter
     * @throws IOException
     */
    @Override
    public void map(LongWritable key, Text value, OutputCollector<LongWritable, MatrixWritable> output,
                    Reporter reporter) throws IOException {
        // value represents one sample of data set
        String line = value.toString();
        String[] features = line.split(",");
        int len = features.length;
        // matrix Xi for 1 row = 1 sample of data set
        double[][] xi = new double[1][len];
        // put xi[0][0]=1 for intercept term of linear regression model
        xi[0][0]=1;
        for (int j = 1; j < len; j++) {
            xi[0][j] = Double.valueOf(features[j]);
        }
        Matrix Xi = new Matrix(xi);
        Matrix XiT = Xi.transpose();
        Matrix firstPart = XiT.times(Xi);
        output.collect(new LongWritable(1), convertToMatrixWritable(firstPart));
        double[][] yi = new double[1][1];
        yi[0][0] = Double.valueOf(features[0]);
        Matrix Yi = new Matrix(yi);
        Matrix secondPart = XiT.times(Yi);
        output.collect(new LongWritable(2), convertToMatrixWritable(secondPart));


    }

    /**
     * To convert a Jama matrix to a Hadoop writable matrix.
     *
     * @param matrix
     * @return
     */
    public static MatrixWritable convertToMatrixWritable(Matrix matrix) {
        int row = matrix.getRowDimension();
        int column = matrix.getColumnDimension();
        DoubleWritable[][] myMatrix = new DoubleWritable[row][column];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < column; j++) {

                myMatrix[i][j] = new DoubleWritable(matrix.get(i, j));
            }

        return new MatrixWritable(DoubleWritable.class, myMatrix);
    }


}
