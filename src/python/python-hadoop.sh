hadoop jar /opt/hadoop-1.1.1/contrib/streaming/hadoop-streaming-1.1.1.jar -file mapper.py -mapper mapper.py -file reducer.py -reducer reducer.py -input /user/ebooks/* -output /user/ebooks_wordcount
