package br.com.siecola.aws_batch_job_processor.repository;

import br.com.siecola.aws_batch_job_processor.model.Job;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

@EnableScan
public interface JobRepository extends BaseRepository<Job, String> {

}