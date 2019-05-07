package br.com.siecola.aws_batch_job_processor.repository;

import br.com.siecola.aws_batch_job_processor.model.Invoice;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

@EnableScan
public interface InvoiceRepository extends BaseRepository<Invoice, String> {

}