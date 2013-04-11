import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MarketRatings extends Configured implements Tool{
  public static class MapClass extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>{
    private Text loc = new Text();
    private Text rating = new Text();

    @Override
    public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
      String[] rows = value.toString().split(",");
      if(rows.length > 31){
        String city = rows[4];
        String state = rows[6];

        int count = 0;
        int rated = 0;
        for (int col=11; col<=31; col++) //columns 11-31 contain data about what the market offers
        {
          if(rows[col].equals("Y")) 
            count++;
        }

        count=(count*100)/21; //gets 1-100 rating of the market

        if(count > 0){
          rated = 1;
        }

        loc.set(city + ", " + state);
        rating.set(1 + "\t" + rated + "\t" + count); //numTotal, numRated, rating
        output.collect(loc, rating);
      }
    }
  }

  public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text>
  {
    @Override
    public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
      int rating = 0;
      int numRated = 0;
      int numTotal = 0;

      while(values.hasNext()){
        String tokens[] = (values.next().toString()).split("\t");
        int tot = Integer.parseInt(tokens[0]);
        int num = Integer.parseInt(tokens[1]); //gets number of markets
        int val = Integer.parseInt(tokens[2]); //gets rating

        if(val > 0) //filters out markets with no data
        {
          rating = (rating*numRated + val*num)/(numRated+num);
          numRated = numRated + num;
        }
        numTotal = numTotal + tot;
      }

      if(rating>0)
        output.collect(key, new Text(numTotal + "\t" + numRated + "\t" + rating));
    }
  }

  static int printUsage(){
    System.out.println("MarketRatings [-m <maps>] [-r <reduces>] <input> <output>");
    return 0;
  }

  @Override
  public int run(String[] args) throws IOException
  {
    return 0;
  }

  public static void main(String[] args) throws IOException{
    JobConf conf = new JobConf(MarketRatings.class);
    conf.setJobName("MarketRatings");

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);

    conf.setMapperClass(MapClass.class);
    conf.setReducerClass(Reduce.class);

    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);

    FileInputFormat.setInputPaths(conf, new Path(args[0]));
    FileOutputFormat.setOutputPath(conf, new Path(args[1]));

    JobClient.runJob(conf);
  }
}
