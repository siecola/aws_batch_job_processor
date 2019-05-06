package br.com.siecola.aws_batch_job_processor.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository <T, ID extends Serializable>
        extends Repository<T, ID> {

    void delete(T delete);

    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T save);
}