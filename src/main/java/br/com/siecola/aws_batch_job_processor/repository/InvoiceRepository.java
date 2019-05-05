package br.com.siecola.aws_batch_job_processor.repository;

import br.com.siecola.aws_batch_job_processor.model.Invoice;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;

@EnableScan
@EnableScanCount
public interface InvoiceRepository extends BaseRepository<Invoice, String> {

}