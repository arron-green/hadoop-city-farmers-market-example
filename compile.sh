rm -rf classes/
rm -f MarketRatings.jar
mkdir -p classes/
javac -classpath /opt/hadoop-1.1.1/hadoop-core-1.1.1.jar:commons-cli-1.2.jar -d classes MarketRatings.java && jar -cvf MarketRatings.jar -C classes/ .
