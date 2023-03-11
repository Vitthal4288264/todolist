package com.mitigant.todolist.service;

import com.mitigant.todolist.entity.Item;
import com.mitigant.todolist.entity.Status;
import com.mitigant.todolist.model.ItemModel;
import com.mitigant.todolist.repo.TodoListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoListServiceImpl implements TodoListService {

    private TodoListRepository todolistRepository;

    public TodoListServiceImpl(TodoListRepository todolistRepository) {
        this.todolistRepository = todolistRepository;
    }

    @Override
    public Item addItem(ItemModel itemModel) throws IllegalArgumentException {
        isValidModel(itemModel);
        Item item = new Item();
        item.setDescription(itemModel.getDescription());
        item.setDueDate(itemModel.getDueDate());
        return todolistRepository.save(item);
    }

    @Override
    public Item changeItemDescription(Integer id, String description) throws IllegalAccessException {
        isValidId(id);
        isValidDescription(description);
        Item item = get(id);
        checkIfTodoListCanBeUpdated(item);

        item.setDescription(description);
        return todolistRepository.save(item);
    }

    @Override
    public Item changeItemStatus(Integer id, String status) throws IllegalAccessException {
        isValidId(id);
        isValidStatus(status);
        Item item = get(id);
        checkIfTodoListCanBeUpdated(item);

        //in case of same status update
        if (item.getStatus().name().equals(status)) return item;

        item.setStatus(Status.valueOf(status));
        return todolistRepository.save(item);
    }

    @Override
    public Item getItem(Integer id) {
        isValidId(id);
        return get(id);
    }

    @Override
    public Page<Item> getItems(boolean getAll, Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(
                pageNumber == null ? 0 : pageNumber,
                pageSize == null ? 10 : pageSize);
        return getAll ? todolistRepository.findAll(pageable) :
                todolistRepository.findAllByStatusEquals(Status.NOT_DONE, pageable);

    }


    //**************** helper methods ****************
    //todo: check & refactor below

    private void checkIfTodoListCanBeUpdated(Item item) throws IllegalAccessException {
        if (item.getStatus().equals(Status.PAST_DUE)) {
            throw new IllegalAccessException("Cannot process on this todo list");
        }
    }

    private Item get(Integer id) {
        Optional<Item> item = todolistRepository.findById(id);
        if (item.isEmpty()) throw new NoSuchElementException("Todo not found for given id");
        return item.get();
    }

    private void isValidModel(ItemModel item) {
        if (Objects.isNull(item) ||
                Objects.isNull(item.getDescription()) ||
                Objects.isNull(item.getDueDate()) ||
                        item.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid Request");
        }
    }

    private void isValidId(Integer val) {
        if (Objects.isNull(val) || val < 1) throw new IllegalArgumentException("Invalid Input");
    }

    private void isValidDescription(String val){
        if (Objects.isNull(val)) throw new IllegalArgumentException("Invalid Request");
    }
    private void isValidStatus(String val){
        if (Objects.isNull(val) || !(val.equalsIgnoreCase(Status.DONE.name().toLowerCase()) ||
                val.equalsIgnoreCase(Status.NOT_DONE.name().toLowerCase()))) throw new IllegalArgumentException("Invalid Request");
    }

}
