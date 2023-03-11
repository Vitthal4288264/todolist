package com.mitigant.todolist.service;


import com.mitigant.todolist.entity.Status;
import com.mitigant.todolist.repo.TodoListRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TodoListPurgeServiceImpl implements TodoListPurgeService{

    private TodoListRepository todolistRepository;

    public TodoListPurgeServiceImpl(TodoListRepository todolistRepository) {
        this.todolistRepository = todolistRepository;
    }

    @Scheduled(cron = "${todolist.cron-expression}")
    @Override
    public void updateEligibleItemsToPurgeStatus() {
        todolistRepository.updateItemStatusToPastDue(Status.PAST_DUE.name(), LocalDateTime.now());
    }

}
