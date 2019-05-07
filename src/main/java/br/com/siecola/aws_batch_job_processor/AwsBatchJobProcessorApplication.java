package br.com.siecola.aws_batch_job_processor;

import br.com.siecola.aws_batch_job_processor.service.JobExecutor;
import br.com.siecola.aws_batch_job_processor.service.S3ObjectDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class AwsBatchJobProcessorApplication implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(
			AwsBatchJobProcessorApplication.class);

	@Autowired
	private Environment environment;

	@Autowired
	private S3ObjectDownloader s3ObjectDownloader;

	@Autowired
	private JobExecutor jobExecutor;

	public static void main(String[] args) {
		SpringApplication.run(AwsBatchJobProcessorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String objectKey = environment.getProperty("transactionKey");
		log.info("Object key: " + objectKey);
		String objectContent = s3ObjectDownloader.getObject(objectKey);

		jobExecutor.executeJob(objectKey, objectContent);
	}
}