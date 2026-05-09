package io.github.tuanhiep.bigdata.linearregression.training;

import Jama.Matrix;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.Iterator;

import static io.github.tuanhiep.bigdata.linearregression.training.LinearRegressionTrainingMapper.convertToMatrixWritable;


public class LinearRegressionTrainingReducer extends MapReduceBase implements Reducer<LongWritable, MatrixWritable, LongWritable, MatrixWritable> {

    @Override
    public void reduce(LongWritable key, Iterator<MatrixWritable> value,
                       OutputCollector<LongWritable, MatrixWritable> output, Reporter reporter)
            throws IOException {

        Matrix sum = null;
        while (value.hasNext()) {
            Writable[][] matrix = value.next().get();
            if (sum == null) {
                sum = convertToMatrix(matrix);
            } else {
                sum = sum.plus(convertToMatrix(matrix));
            }

        }
        output.collect(key, convertToMatrixWritable(sum));

    }

    /**
     * Convert Writable[][] to Matrix Jama
     *
     * @param matrix
     * @return
     */
    public static Matrix convertToMatrix(Writable[][] matrix) {
        int row = matrix.length;
        int column = matrix[0].length;
        double[][] da = new double[row][column];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < column; j++) {

                da[i][j] = ((DoubleWritable) matrix[i][j]).get();
            }

        return new Matrix(da);
    }

}
