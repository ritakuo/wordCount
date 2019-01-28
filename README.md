# Use AWS EMR to run a custom ( wordcount) jar

## tl&dr
- I can't find  sample wordcount jar to test out AWS EMR , so I create one. 

- This program contains a standard mapreduce classes. ![edureka](https://www.edureka.co/blog/mapreduce-tutorial/) explains the mapreduce process.
![](https://s3-us-west-2.amazonaws.com/donot-delete-github-image/Screen+Shot+2019-01-27+at+12.20.20+PM.png)

## Pre-req

### Compile the jar
1. clone this repo
2. maven clean, maven install 

## run on AWS EMR
1. Create a EC2 keypair PEM file to used for EMR
2. Create a S3 bucket 
3. Upload the jar in this repo to your s3 bucket ( You can make change and compile your own as well)
4. Upload the input file to the same s3 bucket 
5. Create a EMR cluster, choose EMR version that uses hadoop version 2.7.3 (to use a different hadoop version, change the pom.xml)
6. After EMR provision finish, add a step for custom jar
- for JAR location, point to the jar in the s3 bucket
- for argument:
'''
s3://<your-bucket>/<your-input-file>.txt s3://<your-bucket>/out
'''
7. check output on s3 folder

