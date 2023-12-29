package Repository;

import Entity.TodoList;

import java.util.UUID;

public class TodoListRepositoryImp implements TodoListRepository {

    public TodoList[] todoLists = new TodoList[10];

    @Override
    public void save(TodoList todoList) {
        int currentIndex = 0;
        boolean added = false;
        while (currentIndex < this.todoLists.length && !added) {
            if (this.todoLists[currentIndex] == null) {
                this.todoLists[currentIndex] = todoList;
                added = true;
            }
            currentIndex++;
        }
        if (!added) {
            throw new IllegalStateException
                    ("Cannot add more items as the capacity is full!");
        }
    }



    @Override
    public TodoList[] findAll() {
        return this.todoLists;
    }

    @Override
    public boolean update(UUID number, TodoList updateTodoList) {
        for (int i = 0; i < this.todoLists.length; i++) {
            if (this.todoLists[i] != null && this.todoLists[i]
                    .getNoIdentity().equals(number)) {
                this.todoLists[i] = updateTodoList;
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean deleteUUID(UUID number) {
        boolean removed = false;
        int foundIndex = -1;

        for (int i = 0; i < this.todoLists.length; i++) {
            if (this.todoLists[i] != null && this.todoLists[i].getNoIdentity() 
                    != null && this.todoLists[i].getNoIdentity().equals(number)) {
                foundIndex = i;
                removed = true;
                break;
            }
        }


        if (foundIndex != -1) {
            for (int j = foundIndex; j < this.todoLists.length - 1; j++) {
                this.todoLists[j] = this.todoLists[j + 1];
            }
            this.todoLists[this.todoLists.length - 1] = null;
        }

        return removed;
    }
}