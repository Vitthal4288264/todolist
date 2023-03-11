package com.mitigant.todolist.service;

import com.mitigant.todolist.entity.Item;
import com.mitigant.todolist.entity.Status;
import com.mitigant.todolist.model.ItemModel;
import com.mitigant.todolist.repo.TodoListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TodoListServiceImplTest {

    @InjectMocks
    private TodoListServiceImpl todoListService;

    @Mock
    private TodoListRepository todoListRepository;

    @Test
    @DisplayName("Test to get items with invalid page number")
    void getItemsWithInvalidPageNumber() {
        assertThrows(IllegalArgumentException.class, () -> todoListService.getItems(true, -1, 10));
    }

    @Test
    @DisplayName("Test to get all items with default page size")
    void getAllItemsWithDefaultPageSize() {
        when(todoListRepository.findAll(any(Pageable.class))).thenReturn(null);
        todoListService.getItems(true, null, null);
        verify(todoListRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Test to get all items with custom page size")
    void getAllItemsWithCustomPageSize() {
        when(todoListRepository.findAll(any(Pageable.class))).thenReturn(null);
    }

    @Test
    @DisplayName("Test to get not done items with custom page size")
    void getNotDoneItemsWithCustomPageSize() {
        when(todoListRepository.findAllByStatusEquals(any(), any())).thenReturn(null);
        todoListService.getItems(false, null, 5);
        verify(todoListRepository, times(1)).findAllByStatusEquals(any(), any());
    }

    @Test
    @DisplayName("Test to get not done items with default page size")
    void getNotDoneItemsWithDefaultPageSize() {
        when(todoListRepository.findAllByStatusEquals(any(), any())).thenReturn(null);
        todoListService.getItems(false, null, null);
        verify(todoListRepository, times(1)).findAllByStatusEquals(any(), any());
    }

    @Test
    @DisplayName("Test to get an item with an invalid id")
    void getItemWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> todoListService.getItem(-1));
    }

    @Test
    @DisplayName("Test to get an item with a null id")
    void getItemWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> todoListService.getItem(null));
    }

    @Test
    @DisplayName("Test to get a non-existing item")
    void getNonExistingItem() {
        when(todoListRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> todoListService.getItem(1));
    }

    @Test
    @DisplayName("Test to get an existing item")
    void getExistingItem() {
        Item item = new Item();
        item.setId(1);
        item.setDescription("Test");
        item.setDueDate(LocalDateTime.now());
        item.setStatus(Status.NOT_DONE);
        when(todoListRepository.findById(anyInt())).thenReturn(Optional.of(item));
        Item result = todoListService.getItem(1);
        assertEquals(item, result);
    }

    @Test
    @DisplayName("Test to get an item with a valid id and null description")
    void getItemWithValidIdAndNullDescription() {
        Item item = new Item();
        item.setId(1);
        item.setDescription(null);
        item.setCreationDate(LocalDateTime.now());
        item.setDueDate(LocalDateTime.now());
        item.setLastUpdatedAt(LocalDateTime.now());
        item.setStatus(Status.NOT_DONE);
        when(todoListRepository.findById(anyInt())).thenReturn(Optional.of(item));
        Item result = todoListService.getItem(1);
        assertEquals(item, result);
    }

    @Test
    @DisplayName("Test changing item status with invalid status")
    void changeItemStatusWithInvalidStatus() {
        assertThrows(
                IllegalArgumentException.class,
                () -> todoListService.changeItemStatus(1, "invalid"));
    }

    @Test
    @DisplayName("Test changing item status with invalid id")
    void changeItemStatusWithInvalidId() {
        assertThrows(
                IllegalArgumentException.class, () -> todoListService.changeItemStatus(-1, "done"));
    }

    @Test
    @DisplayName("Test changing item status to done")
    void changeItemStatusToDone() throws IllegalAccessException {
        Item item = new Item();
        item.setId(1);
        item.setDescription("Test");
        item.setDueDate(LocalDateTime.now());
        item.setStatus(Status.NOT_DONE);

        when(todoListRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(todoListRepository.save(any())).thenReturn(item);

        Item result = todoListService.changeItemStatus(1, Status.DONE.name());

        assertEquals(Status.DONE, result.getStatus());
    }

    @Test
    @DisplayName("Test changing item status to not done")
    void changeItemStatusToNotDone() throws IllegalAccessException {
        Item item = new Item();
        item.setId(1);
        item.setDescription("Test");
        item.setDueDate(LocalDateTime.now());
        item.setStatus(Status.DONE);

        when(todoListRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(todoListRepository.save(any())).thenReturn(item);

        Item result = todoListService.changeItemStatus(1, Status.NOT_DONE.name());

        assertEquals(Status.NOT_DONE, result.getStatus());
    }

    @Test
    @DisplayName("Test changing item status to same status")
    void changeItemStatusToSameStatus() throws IllegalAccessException {
        Item item = new Item();
        item.setId(1);
        item.setDescription("Test");
        item.setStatus(Status.NOT_DONE);
        when(todoListRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(todoListRepository.save(any())).thenReturn(item);

        Item result = todoListService.changeItemStatus(1, Status.NOT_DONE.name());

        assertEquals(1, result.getId());
        assertEquals("Test", result.getDescription());
        assertEquals(Status.NOT_DONE, result.getStatus());
    }

    @Test
    @DisplayName("Test if exception is thrown when description is null")
    void changeItemDescriptionNullDescription() {
        assertThrows(
                IllegalArgumentException.class,
                () -> todoListService.changeItemDescription(1, null));
    }

    @Test
    @DisplayName("Test if exception is thrown when id is invalid")
    void changeItemDescriptionInvalidId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> todoListService.changeItemDescription(-1, "test"));
    }

    @Test
    @DisplayName("Test if exception is thrown when item status is PASTDUE")
    void changeItemDescriptionPastDueStatus() {
        Item item = new Item();
        item.setStatus(Status.PAST_DUE);
        when(todoListRepository.findById(anyInt())).thenReturn(Optional.of(item));
        assertThrows(
                IllegalAccessException.class,
                () -> todoListService.changeItemDescription(1, "test"));
    }

    @Test
    @DisplayName("Test if item description is changed successfully")
    void changeItemDescriptionSuccess() throws IllegalAccessException {
        Item item = new Item();
        item.setId(1);
        item.setDescription("Test");
        item.setDueDate(LocalDateTime.now());
        item.setStatus(Status.NOT_DONE);
        when(todoListRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(todoListRepository.save(any())).thenReturn(item);
        Item result = todoListService.changeItemDescription(1, "Test");
        assertEquals("Test", result.getDescription());
    }

    @Test
    @DisplayName("Test if item is not updated when description is same as previous")
    void changeItemDescriptionSameDescription() throws IllegalAccessException {
        Item item = new Item();
        item.setId(1);
        item.setDescription("Test");
        item.setStatus(Status.NOT_DONE);
        when(todoListRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(todoListRepository.save(any())).thenReturn(item);
        Item updatedItem = todoListService.changeItemDescription(1, "Test");
        assertEquals(item, updatedItem);
        verify(todoListRepository, times(1)).findById(anyInt());
        verify(todoListRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test adding an item with null due date to the todo list")
    void addItemWithNullDueDateToTodoList() {
        ItemModel itemModel = new ItemModel();
        itemModel.setDescription("Test");
        itemModel.setDueDate(null);
        assertThrows(IllegalArgumentException.class, () -> todoListService.addItem(itemModel));
    }

    @Test
    @DisplayName("Test adding an item with both null description and due date to the todo list")
    void addItemWithNullDescriptionAndDueDateToTodoList() {
        ItemModel itemModel = new ItemModel();
        itemModel.setDescription(null);
        itemModel.setDueDate(null);
        assertThrows(IllegalArgumentException.class, () -> todoListService.addItem(itemModel));
    }

    @Test
    @DisplayName("Test adding an item with null description to the todo list")
    void addItemWithNullDescriptionToTodoList() {
        ItemModel itemModel = new ItemModel();
        itemModel.setDescription(null);
        itemModel.setDueDate(LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> todoListService.addItem(itemModel));
    }

    @Test
    @DisplayName("Test adding an item with past due date to the todo list")
    void addItemWithPastDueDateToTodoList() {
        ItemModel itemModel = new ItemModel();
        itemModel.setDescription("Test");
        itemModel.setDueDate(LocalDateTime.now().minusDays(1));
        assertThrows(IllegalArgumentException.class, () -> todoListService.addItem(itemModel));
    }

    @Test
    @DisplayName("Test adding a valid item to the todo list")
    void addValidItemToTodoList() {
        ItemModel itemModel = new ItemModel();
        itemModel.setDescription("Test");
        itemModel.setDueDate(LocalDateTime.now().plusDays(1));

        Item item = new Item();
        item.setId(1);
        item.setDescription("Test");
        item.setDueDate(LocalDateTime.now().plusDays(1));
        item.setStatus(Status.NOT_DONE);

        when(todoListRepository.save(any(Item.class))).thenReturn(item);

        Item result = todoListService.addItem(itemModel);

        assertEquals(1, result.getId());
        assertEquals("Test", result.getDescription());
        assertEquals(Status.NOT_DONE, result.getStatus());
    }

    @Test
    @DisplayName("Test changing item status with null status")
    void changeItemStatusWithNullStatus() {
        assertThrows(
                IllegalArgumentException.class, () -> todoListService.changeItemStatus(1, null));
    }

    @Test
    @DisplayName("Test changing item status to PASTDUE")
    void changeItemStatusToPastDue() throws IllegalAccessException {
        assertThrows(
                        IllegalArgumentException.class, () -> todoListService.changeItemStatus(1, Status.PAST_DUE.name()));
    }

    @Test
    @DisplayName("Test changing item description with null input data")
    void changeItemDescriptionWithNullInputData() {
        assertThrows(
                IllegalArgumentException.class,
                () -> todoListService.changeItemDescription(null, null));
    }

    @Test
    @DisplayName("Test changing item description with invalid input data")
    void changeItemDescriptionWithInvalidInputData() {
        assertThrows(
                IllegalArgumentException.class,
                () -> todoListService.changeItemDescription(null, null));
    }

    @Test
    @DisplayName("Test changing item description with non-existent item id")
    void changeItemDescriptionWithNonExistentItemId() {
        when(todoListRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(
                NoSuchElementException.class,
                () -> todoListService.changeItemDescription(1, "description"));
    }

    @Test
    @DisplayName("Test changing item description with past due item")
    void changeItemDescriptionWithPastDueItem() {
        Item item = new Item();
        item.setId(1);
        item.setDescription("Test");
        item.setDueDate(LocalDateTime.now().minusDays(1));
        item.setStatus(Status.PAST_DUE);
        when(todoListRepository.findById(any())).thenReturn(Optional.of(item));
        assertThrows(
                IllegalAccessException.class,
                () -> todoListService.changeItemDescription(1, "Test"));
    }

    @Test
    @DisplayName("Test changing item description with valid input data")
    void changeItemDescriptionWithValidInputData() throws IllegalAccessException {
        Item item = new Item();
        item.setId(1);
        item.setDescription("Test description");
        item.setStatus(Status.NOT_DONE);
        item.setCreationDate(LocalDateTime.now());
        item.setLastUpdatedAt(LocalDateTime.now());

        when(todoListRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(todoListRepository.save(any(Item.class))).thenReturn(item);

        Item result = todoListService.changeItemDescription(1, "Test description");

        assertNotNull(result);
        assertEquals("Test description", result.getDescription());
    }

    //todo add to readme
   /* @Test
       @DisplayName("Test adding an item with invalid values to the todo list")
       void addItemWithInvalidValues() {
           ItemModel itemModel = new ItemModel();
           itemModel.setDueDate(LocalDateTime.parse("22/09/01"));
           assertThrows(Exception.class, () -> todoListService.addItem(itemModel));
       }*/

    @Test
    @DisplayName("Test adding an item with null values to the todo list")
    void addItemWithNullValues() {
        ItemModel itemModel = null;
        assertThrows(IllegalArgumentException.class, () -> todoListService.addItem(itemModel));
    }

    @Test
    @DisplayName("Test adding an item with duplicate values to the todo list")
    void addItemWithDuplicateValues() {
        ItemModel itemModel = new ItemModel();
        itemModel.setDescription("Test");
        itemModel.setDueDate(LocalDateTime.now());

        Item item = new Item();
        item.setDescription("Test");
        item.setDueDate(LocalDateTime.now());

        when(todoListRepository.save(any())).thenReturn(item);

        assertThrows(IllegalArgumentException.class, () -> todoListService.addItem(itemModel));
    }

    @Test
    @DisplayName("Test adding a new item to the todo list")
    void addItem() {
        ItemModel itemModel = new ItemModel();
        itemModel.setDescription("Test");
        itemModel.setDueDate(LocalDateTime.now().plusSeconds(3));

        Item item = new Item();
        item.setId(1);
        item.setDescription("Test");
        item.setDueDate(LocalDateTime.now().plusSeconds(3));
        item.setStatus(Status.NOT_DONE);

        when(todoListRepository.save(any())).thenReturn(item);

        Item result = todoListService.addItem(itemModel);


        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getDescription());
        assertEquals(Status.NOT_DONE, result.getStatus());

        verify(todoListRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test adding an item with valid values to the todo list")
    void addItemWithValidValues() {
        ItemModel itemModel = new ItemModel();
        itemModel.setDescription("Test");
        itemModel.setDueDate(LocalDateTime.now().plusSeconds(5));

        Item item = new Item();
        item.setId(1);
        item.setDescription("Test");
        item.setDueDate(LocalDateTime.now().plusSeconds(5));
        item.setStatus(Status.NOT_DONE);

        when(todoListRepository.save(any())).thenReturn(item);

        Item result = todoListService.addItem(itemModel);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getDescription());
        assertEquals(Status.NOT_DONE, result.getStatus());

        verify(todoListRepository, times(1)).save(any());
    }
}