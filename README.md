# Hadoop MapReduce Example
## How good are a city's farmer's markets?

Code associated with the [youtube
video](http://www.youtube.com/watch?v=KwW7bQRykHI)

**Please note, i am not the author of this video. I simplied copied the code from the video since people were asking for it**

## Assumptions
You have already provisioned a hadoop 1.1.1 cluster

See [vagrant hadoop](https://github.com/DorkScript/vagrant-hadoop) for ideas

## Usage
This project uses [gradle](http://www.gradle.org) as it's build tool

## Compile

```sh
#build the java jar file
gradle jar
```

## Output
Grab the MarketRatings.jar file from `build/libs/MarketRatings.jar`
