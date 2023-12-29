package Service;

import Entity.TodoList;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TodoListService {
    public void AddTodoListService(TodoList todoList);
    boolean UpdateTodoListService(UUID number, TodoList updateTodoList);
    TodoList[] getTodoListSortedCategories();
    void intervalTime(LocalDateTime localDateTime);
    boolean RemoveTodoListService(UUID number);
}       