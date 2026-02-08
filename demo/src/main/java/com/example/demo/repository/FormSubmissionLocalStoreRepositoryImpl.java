package com.example.demo.repository;

import com.example.demo.model.FormSubmission;
import com.example.demo.repository.interfaces.IFormSubmissionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("localStore")
public class FormSubmissionLocalStoreRepositoryImpl implements IFormSubmissionRepository {

    // The in-memory database is moved from the Service to the Repository
    private final List<FormSubmission> dataSource = new ArrayList<>();

    @Override
    public List<FormSubmission> findAll() {
        return new ArrayList<>(dataSource);
    }

    @Override
    public Optional<FormSubmission> findById(String id) {
        return dataSource.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    @Override
    public void save(FormSubmission submission) {
        dataSource.add(submission);
    }

    @Override
    public void update(FormSubmission submission) {
        // Since it's in-memory and we are working with object references,
        // the object in the list is already updated via the service.
        // For a real DB (JPA), you would use save() or merge().
    }

    @Override
    public void deleteById(String id) {
        dataSource.removeIf(s -> s.getId().equals(id));
    }
}