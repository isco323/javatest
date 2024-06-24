package com.example.testdiplom.repos;

import com.example.testdiplom.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Integer> {
    List<Message> findByName(String name);
}
