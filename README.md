s3-delegation-token
===================

### Introduction
Simple standalone application to get delegation token from S3.

### Build the app
To build, you need Java 11, git and maven on the box.
Do a git clone of this repo and then run:
```
cd s3-delegation-token
mvn clean package
```

### Running the app
```
java -jar target/s3-delegation-token-1.0-SNAPSHOT.jar REGION ACCESS_KEY SECRET_KEY BUCKET
```

### Example output
```
Obtaining STS token with access key: *****
Credentials from S3: {AccessKeyId: *****,SecretAccessKey: *****,SessionToken: **********,Expiration: Wed Jan 11 07:33:56 CET 2023}

Updating STS credentials
Updated STS credentials successfully

0    [main] WARN  org.apache.hadoop.util.NativeCodeLoader  - Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
228  [main] WARN  org.apache.hadoop.metrics2.impl.MetricsConfig  - Cannot locate configuration: tried hadoop-metrics2-s3a-file-system.properties,hadoop-metrics2.properties
239  [main] INFO  org.apache.hadoop.metrics2.impl.MetricsSystemImpl  - Scheduled Metric snapshot period at 10 second(s).
239  [main] INFO  org.apache.hadoop.metrics2.impl.MetricsSystemImpl  - s3a-file-system metrics system started
Creating test file: test_file
Test file created successfully
Uploading file: test_file to bucket: bucket
File uploaded successfully
Listing files in bucket: bucket
Entry: S3ALocatedFileStatus{path=s3a://bucket/test_file; isDirectory=false; length=12; replication=1; blocksize=33554432; modification_time=1673525593000; access_time=0; owner=owner; group=group; permission=rw-rw-rw-; isSymlink=false; hasAcl=false; isEncrypted=true; isErasureCoded=false}[eTag='27565f9a57c128674736aa644012ce67', versionId='']
Files listed successfully
Downloading file: downloaded_test_file from bucket: bucket
File downloaded successfully
1055 [shutdown-hook-0] INFO  org.apache.hadoop.metrics2.impl.MetricsSystemImpl  - Stopping s3a-file-system metrics system...
1055 [shutdown-hook-0] INFO  org.apache.hadoop.metrics2.impl.MetricsSystemImpl  - s3a-file-system metrics system stopped.
1056 [shutdown-hook-0] INFO  org.apache.hadoop.metrics2.impl.MetricsSystemImpl  - s3a-file-system metrics system shutdown complete.
```
