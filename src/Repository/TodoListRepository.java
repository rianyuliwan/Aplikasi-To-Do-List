package Repository;

import Entity.TodoList;

import java.util.UUID;

public interface TodoListRepository {
    void save(TodoList todoList);
    TodoList[] findAll();
    boolean update(UUID number, TodoList updateTodoList);
    boolean deleteUUID(UUID number);
}