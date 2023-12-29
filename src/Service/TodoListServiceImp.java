package Service;

import Entity.TodoList;
import Repository.TodoListRepositoryImp;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TodoListServiceImp implements TodoListService {
    private TodoListRepositoryImp todoListRepositoryImp;

    public TodoListServiceImp(TodoListRepositoryImp todoListRepositoryImp) {
        this.todoListRepositoryImp = todoListRepositoryImp;
    }

    @Override
    public void AddTodoListService(TodoList todoList) {
        this.todoListRepositoryImp.save(todoList);
    }

    @Override
    public boolean UpdateTodoListService(UUID number, TodoList updateTodoList) {
        if (this.todoListRepositoryImp.update(number, updateTodoList)) {
            System.out.println("Success Update TODO");
        } else {
            System.out.println("Invalid Update TODO");
        }
        return true;
    }

    @Override
    public TodoList[] getTodoListSortedCategories() {
        TodoList[] allTodoLists = todoListRepositoryImp.findAll();

        Arrays.sort(allTodoLists, Comparator.comparing(todoList -> {
            if (todoList != null) {
                String category = todoList.getCategories() != null ? 
                        todoList.getCategories().name() : "";
                return switch (category) {
                    case "IMPORTANT" -> 1;
                    case "MEDIUM" -> 2;
                    case "NORMAL" -> 3;
                    default -> 4;
                };
            }
            return 4;
        }));

        return allTodoLists;
    }

    public void intervalTime(LocalDateTime notificationTime) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool
        (1);

        LocalDateTime now = LocalDateTime.now();

        long initialDelay = calculateInitialDelay(now, notificationTime);
        long delay = calculateDelay(notificationTime);

        Runnable task = () -> {
            try {
                File audioFile = new File("C:\\Users\\rian yuliawan\\"
                        + "OneDrive\\Dokumen\\NetBeansProjects\\login"
                        + "\\src\\Asset\\ring.wav");
                AudioInputStream audioInputStream = AudioSystem
                        .getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();

                clip.open(audioInputStream);
                clip.start();

                final Clip[] clipArray = { clip };

                SwingUtilities.invokeLater(() -> {
                    JOptionPane pane = new JOptionPane
                    ("Hello, this is a notification!", JOptionPane.
                            PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    JDialog dialog = pane.createDialog(null, 
                            "Notification");

                    pane.addPropertyChangeListener(e -> {
                        if (e.getPropertyName().equals(JOptionPane.
                                VALUE_PROPERTY)) {

                            clipArray[0].stop();
                            clipArray[0].close();
                        }
                    });

                    dialog.setVisible(true);
                });

            } catch (UnsupportedAudioFileException | LineUnavailableException | 
                    IOException e) {
                e.printStackTrace();
            }
            executor.shutdown();
        };

        executor.scheduleAtFixedRate(task, initialDelay, delay, 
                TimeUnit.SECONDS);
    }

    private static long calculateInitialDelay(LocalDateTime now, LocalDateTime 
            notificationTime) {
        long initialDelay = Duration.between(now, 
                notificationTime).getSeconds();
        return Math.max(initialDelay, 0);
    }

    private static long calculateDelay(LocalDateTime notificationTime) {
        LocalDateTime now = LocalDateTime.now();
        long delay = Duration.between(now, 
                notificationTime).getSeconds();
        return Math.max(delay, 1);
    }

    public TodoList[] getTodoLists() {
        return todoListRepositoryImp.findAll();
    }
    @Override
    public boolean RemoveTodoListService(UUID number) {
        return todoListRepositoryImp.deleteUUID(number);
    }
}