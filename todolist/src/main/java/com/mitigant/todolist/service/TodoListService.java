package com.mitigant.todolist.service;

import com.mitigant.todolist.entity.Item;
import com.mitigant.todolist.model.ItemModel;
import org.springframework.data.domain.Page;

public interface TodoListService {

    Item addItem(ItemModel itemModel) throws IllegalArgumentException;

    Item changeItemDescription(Integer id, String description) throws IllegalAccessException;

    Item changeItemStatus(Integer id, String status) throws IllegalAccessException;

    Item getItem(Integer id);

    Page<Item> getItems(boolean getAll, Integer pageNumber, Integer pageSize);

}
