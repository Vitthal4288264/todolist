
package com.mitigant.todolist.repo;

import com.mitigant.todolist.entity.Item;
import com.mitigant.todolist.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface TodoListRepository extends JpaRepository<Item, Integer> {

    Page<Item> findAllByStatusEquals(Status status, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update item i set i.status = :status where i.due_date < :today", nativeQuery = true)
    void updateItemStatusToPastDue(@Param(value = "status") String status, @Param(value = "today") LocalDateTime today);

}
