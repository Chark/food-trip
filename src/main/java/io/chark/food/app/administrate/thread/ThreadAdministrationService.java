package io.chark.food.app.administrate.thread;

import io.chark.food.domain.comment.Thread;
import io.chark.food.domain.comment.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreadAdministrationService {

    private final ThreadRepository threadRepository;

    @Autowired
    public ThreadAdministrationService(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    public List<Thread> getThreads(){
        return threadRepository.findAll();
    }
}
