package br.com.siecola.aws_batch_job_processor.service;

import br.com.siecola.aws_batch_job_processor.exception.S3ObjectDownloadException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class S3ObjectDownloader {

    private static final Logger log = LoggerFactory
            .getLogger(S3ObjectDownloader.class);

    @Value("${amazon.aws.s3.bucket.name}")
    private String awsBucketName;

    private AmazonS3 amazonS3;

    public S3ObjectDownloader(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String getObject(String key) throws S3ObjectDownloadException {
        try {
            S3Object object = amazonS3.getObject(
                    new GetObjectRequest(awsBucketName, key));
            return readStream(object.getObjectContent());
        } catch (AmazonServiceException e) {
            log.error("Failed to download content");
            throw new S3ObjectDownloadException();
        } catch (SdkClientException e) {
            log.error("Can't connect to AWS S3");
            throw new S3ObjectDownloadException();
        } catch (IOException e) {
            log.error("Failed to read the content");
            throw new S3ObjectDownloadException();
        }
    }

    private String readStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader((
                new InputStreamReader(inputStream)));
        String line;

        while((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }
}