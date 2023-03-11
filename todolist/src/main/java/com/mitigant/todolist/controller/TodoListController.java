package com.mitigant.todolist.controller;

import com.mitigant.todolist.entity.Item;
import com.mitigant.todolist.model.ItemModel;
import com.mitigant.todolist.service.TodoListService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoListController {

    private TodoListService todoListService;

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping("/addItem")
    public ResponseEntity<Item> addItem(@RequestBody ItemModel itemModel) {
        try {
            return ResponseEntity.ok(todoListService.addItem(itemModel));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/changeDescription")
    public ResponseEntity<Item> changeItemDescription(@PathVariable Integer id,
                                                      @RequestParam String description) {
        try {
            return ResponseEntity.ok(todoListService.changeItemDescription(id, description));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalAccessException exception) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/changeStatus")
    public ResponseEntity<Item> changeItemStatus(@PathVariable Integer id,
                                                 @RequestParam String status) {
        try {
            return ResponseEntity.ok(todoListService.changeItemStatus(id, status));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalAccessException exception) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Integer id) {

        try {
            return ResponseEntity.ok(todoListService.getItem(id));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Page<Item>> getItems(@RequestParam(required = false) boolean getAll,
                                               @RequestParam(required = false) Integer pageNumber,
                                               @RequestParam(required = false) Integer pageSize) {
        return ResponseEntity.ok(todoListService.getItems(getAll, pageNumber, pageSize));
    }
}
