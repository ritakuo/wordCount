import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import static org.apache.hadoop.metrics2.impl.MsInfo.Context;

public class wordCount {
    //MR infra will call this class and pass in text
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>{
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer st = new StringTokenizer(value.toString());
            Text wordOut = new Text();
            IntWritable one = new IntWritable(1);
            while(st.hasMoreTokens()){
                wordOut.set(st.nextToken());
                context.write(wordOut, one);
            }

        }
    }
    public static class SumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text term, Iterable<IntWritable> ones, Context context)throws IOException, InterruptedException {
            int count = 0;
            Iterator<IntWritable> iterator = ones.iterator();
            while(iterator.hasNext()){
                count ++;
                iterator.next();
            }
            IntWritable output = new IntWritable (count);
            context.write(term,  output);
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        String[]  otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

        if(otherArgs.length !=2){
            System.err.println("Usage: WordCount <input_file> <output_directory>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf, "Word Count");
        job.setJarByClass(wordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(SumReducer.class);
        job.setNumReduceTasks(3);//will output to 10 files
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        boolean status = job.waitForCompletion(true);
        if (status){
            System.exit(0);
        }else{
            System.exit(1);
        }

    }
}
