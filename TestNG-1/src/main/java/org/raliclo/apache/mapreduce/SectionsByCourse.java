package org.raliclo.apache.mapreduce;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SectionsByCourse {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        java.nio.file.Path path = Paths.get("output");
        if (Files.exists(path)) {
            FileUtils.deleteDirectory(path.toFile());
        }

        Job job = new Job();
        job.setJarByClass(SectionsByCourse.class);
        job.setJobName("SectionsByCourse");

        job.setMapperClass(SectionsByCourseMapper.class);
        job.setCombinerClass(SectionsByCourseCombiner.class);
        job.setReducerClass(SectionsByCourseReducer.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

//    public static void main(String[] args) {
//        Configuration conf = new Configuration();
//        GenericOptionsParser optionParser = new GenericOptionsParser(conf, args);
//        String[] remainingArgs = optionParser.getRemainingArgs();
//        if ((remainingArgs.length != 2) && (remainingArgs.length != 4)) {
//            System.err.println("Usage:  <in> <out> [-skip skipPatternFile]");
//            System.exit(2);
//        }
//
//        Job job = Job.getInstance(conf, "word count");
//        job.setJarByClass(WordCount2.class);
//        job.setMapperClass(TokenizerMapper.class);
//        job.setCombinerClass(IntSumReducer.class);
//        job.setReducerClass(IntSumReducer.class);
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(IntWritable.class);
//
//        List<String> otherArgs = new ArrayList<String>();
//        for (int i=0; i < remainingArgs.length; ++i) {
//            if ("-skip".equals(remainingArgs[i])) {
//                job.addCacheFile(new Path(remainingArgs[++i]).toUri());
//                job.getConfiguration().setBoolean("wordcount.skip.patterns", true);
//            } else {
//                otherArgs.add(remainingArgs[i]);
//            }
//        }
//        FileInputFormat.addInputPath(job, new Path(otherArgs.get(0)));
//        FileOutputFormat.setOutputPath(job, new Path(otherArgs.get(1)));
//
//        System.exit(job.waitForCompletion(true) ? 0 : 1);
//    }
}
