package br.com.siecola.aws_batch_job_processor.service;

import br.com.siecola.aws_batch_job_processor.model.Invoice;
import br.com.siecola.aws_batch_job_processor.repository.InvoiceRepository;
import br.com.siecola.aws_batch_job_processor.repository.JobRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class JobExecutor {
    private static final Logger log = LoggerFactory.getLogger(JobExecutor.class);

    private ObjectMapper objectMapper;
    private JobRepository jobRepository;
    private InvoiceRepository invoiceRepository;

    public JobExecutor(ObjectMapper objectMapper, JobRepository jobRepository, InvoiceRepository invoiceRepository) {
        this.objectMapper = objectMapper;
        this.jobRepository = jobRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public void executeJob(String jobId, String objectContent) {
        try {
            List<Invoice> myObjects = objectMapper.readValue(objectContent, new TypeReference<List<Invoice>>(){});

            //TODO - save each invoice

            //TODO - update job status and number of registers
        } catch (IOException e) {
            log.error("Failed to deserialize object content");
        }
    }
}
