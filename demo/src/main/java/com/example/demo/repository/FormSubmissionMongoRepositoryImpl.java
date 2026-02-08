package com.example.demo.repository;

import com.example.demo.model.FormSubmission;
import com.example.demo.repository.interfaces.IFormSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("mongoStore")
public class FormSubmissionMongoRepositoryImpl implements IFormSubmissionRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<FormSubmission> findAll() {
        return mongoTemplate.findAll(FormSubmission.class);
    }

    @Override
    public Optional<FormSubmission> findById(String id) {
        FormSubmission submission = mongoTemplate.findById(id, FormSubmission.class);
        return Optional.ofNullable(submission);
    }

    @Override
    public void save(FormSubmission submission) {
        mongoTemplate.save(submission);
    }

    @Override
    public void update(FormSubmission submission) {
        // MongoTemplate.save() performs an "upsert"
        // (updates if ID exists, inserts if not)
        mongoTemplate.save(submission);
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, FormSubmission.class);
    }
}