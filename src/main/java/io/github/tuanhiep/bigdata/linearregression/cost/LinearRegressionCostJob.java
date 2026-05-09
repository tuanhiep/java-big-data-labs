package io.github.tuanhiep.bigdata.linearregression.cost;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LinearRegressionCostJob {
    public static void main(String[] args) {
        JobConf conf = new JobConf(LinearRegressionCostJob.class);
        conf.setJobName("linearRegression");
        try {
            //put weights parameters to distributed cache so that every node can get it
            DistributedCache.addCacheFile(new URI("src/main/resources/model-parameters/weights.csv"), conf);
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(FloatWritable.class);
        conf.setMapperClass(LinearRegressionCostMapper.class);
        conf.setCombinerClass(LinearRegressionCostReducer.class);
        conf.setReducerClass(LinearRegressionCostReducer.class);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));
        try {
            JobClient.runJob(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
