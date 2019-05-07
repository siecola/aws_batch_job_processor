package br.com.siecola.aws_batch_job_processor.service;

import br.com.siecola.aws_batch_job_processor.enums.JobStatus;
import br.com.siecola.aws_batch_job_processor.model.Invoice;
import br.com.siecola.aws_batch_job_processor.model.Job;
import br.com.siecola.aws_batch_job_processor.repository.InvoiceRepository;
import br.com.siecola.aws_batch_job_processor.repository.JobRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class JobExecutor {
    private static final Logger log = LoggerFactory.getLogger(JobExecutor.class);

    private ObjectMapper objectMapper;
    private JobRepository jobRepository;
    private InvoiceRepository invoiceRepository;
    private AmazonS3 amazonS3;

    @Value("${amazon.aws.s3.bucket.name}")
    private String awsBucketName;

    public JobExecutor(ObjectMapper objectMapper, JobRepository jobRepository,
                       InvoiceRepository invoiceRepository, AmazonS3 amazonS3) {
        this.objectMapper = objectMapper;
        this.jobRepository = jobRepository;
        this.invoiceRepository = invoiceRepository;
        this.amazonS3 = amazonS3;
    }

    public void executeJob(String objectKey, String objectContent) {
        Optional<Job> optJob = jobRepository.findById(objectKey);

        if (optJob.isPresent()) {
            Job job = optJob.get();
            job.setStatus(JobStatus.IN_PROGRESS);
            jobRepository.save(job);

            try {
                List<Invoice> invoices = objectMapper.readValue(objectContent,
                        new TypeReference<List<Invoice>>(){});
                for(Invoice invoice : invoices) {
                    invoice.setJobId(objectKey);
                    invoiceRepository.save(invoice);
                }
                job.setNumberOfRegisters(invoices.size());
                job.setStatus(JobStatus.FINISHED);

                log.info("Job processed");
            } catch (IOException e) {
                log.error("Failed to deserialize object content");
                job.setStatus(JobStatus.ERROR);
            }
            jobRepository.save(job);
        } else {
            log.error("Job not found");
        }

        amazonS3.deleteObject(awsBucketName, objectKey);
        log.info("Object deleted - key: {}", objectKey);
    }
}