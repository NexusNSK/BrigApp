package ru.markov.application.data.database;


import org.springframework.data.mongodb.repository.MongoRepository;


public interface WorkersRepo extends MongoRepository<Worker,String> {
    public Worker findByCategory(int workerCategory);

}
