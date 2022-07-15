package com.gaborsomogyi;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
  public static void main(String[] args) {
    try {
      if (args.length != 4) {
        System.err.println("Usage: Main [region] [accessKey] [secretKey] [bucket]");
        System.exit(1);
      }

      final String region = args[0];
      final String accessKey = args[1];
      final String secretKey = args[2];
      final String bucket = args[3];

      System.out.println("Obtaining STS token with access key: " + accessKey);
      AWSSecurityTokenService stsClient =
              AWSSecurityTokenServiceClientBuilder.standard()
                      .withRegion(region)
                      .withCredentials(
                              new AWSStaticCredentialsProvider(
                                      new BasicAWSCredentials(accessKey, secretKey)))
                      .build();
      GetSessionTokenResult sessionTokenResult = stsClient.getSessionToken();
      Credentials credentials = sessionTokenResult.getCredentials();
      System.out.println("Credentials from S3: " + credentials);
      System.out.println();

      Configuration configuration = new Configuration();
      System.out.println("Updating STS credentials");
      configuration.set(
              "fs.s3a.aws.credentials.provider",
              "org.apache.hadoop.fs.s3a.TemporaryAWSCredentialsProvider");
      configuration.set("fs.s3a.endpoint.region", region);
      configuration.set("fs.s3a.access.key", credentials.getAccessKeyId());
      configuration.set("fs.s3a.secret.key", credentials.getSecretAccessKey());
      configuration.set("fs.s3a.session.token", credentials.getSessionToken());
      System.out.println("Updated STS credentials successfully");
      System.out.println();

      final var uri = URI.create("s3a://" + bucket);
      final var fs = FileSystem.get(uri, configuration);

      final String fileName = "test_file";
      System.out.println("Creating test file: " + fileName);
      Files.write(Paths.get(fileName), "test_content".getBytes());
      System.out.println("Test file created successfully");

      System.out.println("Uploading file: " + fileName + " to bucket: " + bucket);
      String currentDir = Paths.get("").toAbsolutePath().toString();
      fs.copyFromLocalFile(new Path(fileName).makeQualified(new URI("file:///"), new Path(currentDir)),
              new Path("/", fileName));
      System.out.println("File uploaded successfully");

      System.out.println("Listing files in bucket: " + bucket);
      var it = fs.listFiles(new Path("/"), false);
      while (it.hasNext()) {
        System.out.println("Entry: " + it.next());
      }
      System.out.println("Files listed successfully");

      final String downloadedFileName = "downloaded_" + fileName;
      System.out.println("Downloading file: " + downloadedFileName + " from bucket: " + bucket);
      fs.copyToLocalFile(new Path("/", fileName), new Path(downloadedFileName));
      System.out.println("File downloaded successfully");
    } catch (Exception e) {
      System.err.println("Exception: " + e);
      e.printStackTrace();
    }
  }
}
