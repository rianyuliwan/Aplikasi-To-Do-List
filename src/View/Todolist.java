package View;

import Domain.Categories;
import Entity.TodoList;
import Repository.TodoListRepositoryImp;
import Service.TodoListServiceImp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author rian yuliawan
 */
public class Todolist extends javax.swing.JFrame {

    
    
    public JPanel getJpaneTodo() {
        return jpaneTodo;
        
    }

    /**
     * Creates new form Todolist
     */
    public void setJpaneTodo(JPanel jpaneTodo) {
        this.jpaneTodo = jpaneTodo;
    }

 private void printToFile(String fileName){
        try ( BufferedWriter bufferedWriter = new BufferedWriter
            (new FileWriter(fileName))){

            for (int i = 0; i < this.tableTodo.getColumnCount(); i++){
                bufferedWriter.write(this.tableTodo.getColumnName(i));
                if(i<this.tableTodo.getColumnCount() - 1){
                    bufferedWriter.write("\t\t");
                }else {
                    bufferedWriter.write("\n");
                }
            }

            for (int row = 0; row < this.tableTodo.getRowCount(); row++) {
                for (int col = 0; col < this.tableTodo.getColumnCount(); col++){
                    Object value = this.tableTodo.getValueAt(row, col);
                    if (value != null) {
                        bufferedWriter.write(value.toString());
                    }
                    if (col < this.tableTodo.getColumnCount() - 1) {
                        bufferedWriter.write("\t");
                    } else {
                        bufferedWriter.write("\n");
                    }
                }
            }

            bufferedWriter.flush();
            System.out.println("Success "+fileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Todolist() {
        initComponents();
        TodoListServiceImp todoListServiceImp = new TodoListServiceImp
                            (new TodoListRepositoryImp());

       btnGetSortedTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TodoList[] sortedTasks = todoListServiceImp.
                                        getTodoListSortedCategories();
                updateTable(sortedTasks);
            }
        });

       jpaneTodo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tableTodo.clearSelection();
            }
        });

        for (Categories categories : Categories.values()){
            comboBoxCategories.addItem(categories);
        }

        TableModelTodo tableModel = new TableModelTodo(todoListServiceImp);
        tableTodo.setModel(tableModel);

        ListSelectionModel selectionModel = tableTodo.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final String[] category = {""};
        comboBoxCategories.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Categories selectedCategory = (Categories) 
                        comboBoxCategories.getSelectedItem();

                if (selectedCategory != null) switch (selectedCategory) {
                    case IMPORTANT -> category[0] = Categories.IMPORTANT.name();
                    case MEDIUM -> category[0] = Categories.MEDIUM.name();
                    case NORMAL -> category[0] = Categories.NORMAL.name();
                }
            }
        });
         
         selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    int selectedRow = tableTodo.getSelectedRow();
                    if(selectedRow >=0){
                        Object taskObject = tableTodo.
                                            getValueAt(selectedRow, 0);
                        Object dateObject = tableTodo.
                                            getValueAt(selectedRow, 1);
                        Object descriptionObject = tableTodo.
                                            getValueAt(selectedRow, 2);
                        Object categoryObject = tableTodo.
                                            getValueAt(selectedRow,3);

                        String task = (taskObject!= null) ?
                                                    taskObject.toString() : "";
                        String date = (dateObject != null) ?
                                                    dateObject.toString() : "";
                        String description = (descriptionObject != null) ?
                                            descriptionObject.toString() : "";

                        if (categoryObject != null) {
                            Categories category = (Categories) categoryObject;
                            comboBoxCategories.setSelectedItem(category);
                        }

                        fieldTask.setText(task);
                        fieldDate.setText(date);
                        fieldDescription.setText(description);

                          btnDelete.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int selectedRow = tableTodo.getSelectedRow();

                                if (selectedRow != -1) {
                                    try {
                                        TodoList[] todoLists = todoListServiceImp.
                                                                getTodoLists();

                                        if (todoLists != null &&
                                                selectedRow < todoLists.length){
                                                TodoList selectedTodo = todoLists
                                                                    [selectedRow];

                                            if (selectedTodo != null) {
                                                UUID numberTodoList = selectedTodo.
                                                                    getNoIdentity();

                                                boolean removed = todoListServiceImp.
                                                        RemoveTodoListService
                                                        (numberTodoList);

                                                if (removed) {
                                                    lblMessage.setText
                                                ("Data deleted successfully");

                                                    updateTable(todoListServiceImp.
                                                                getTodoLists());
                                                } else {
                                                    lblMessage.setText
                                                    ("Invalid delete data");
                                                }
                                            } else {
                                                lblMessage.setText
                                                ("Selected data is invalid");
                                            }
                                        } else {
                                            lblMessage.setText
                                            ("Invalid selected row");
                                        }
                                    } catch (NumberFormatException ex) {
                                        JOptionPane.showMessageDialog
                                        (null, "No identity not valid");
                                    }
                                }
                            }
                        });


                      
                        btnUpdate.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int selectedRow = tableTodo.getSelectedRow();

                                if (selectedRow != -1) {
                                    try {
                                        TodoList selectedTodo = todoListServiceImp.
                                                        getTodoLists()[selectedRow];

                                        UUID todoID = selectedTodo.getNoIdentity();

                                        String task = fieldTask.getText();
                                        String date = fieldDate.getText();
                                        String description = fieldDescription.getText();
                                        String time = fieldTime.getText();
                                        String fullDateTime = date+"T"+time;

                                        Categories category = null;

                                        Object categoriesSelected = comboBoxCategories.
                                                                    getSelectedItem();
                                        if(categoriesSelected !=null){
                                            category = (Categories) categoriesSelected;
                                        }

                                        TodoList updatedTodo = new TodoList
                                        (task, description, LocalDateTime.
                                        parse(fullDateTime), category);

                                        boolean isUpdated = todoListServiceImp.
                                                UpdateTodoListService
                                                (todoID, updatedTodo);
                                        todoListServiceImp.intervalTime
                                            (LocalDateTime.parse(date));
                                        if (isUpdated) {
                                            lblMessage.setText
                                            ("Data updated successfully ");
                                        } else {
                                            lblMessage.setText
                                            ("Invalid update data");
                                        }
                                    } catch (Exception ex) {
                                        JOptionPane.showMessageDialog
                                        (null, "Invalid update data: " 
                                                + ex.getMessage());
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                                "Select data to update");
                                }
                            }
                        });




                    }
                }
            }
        });

   btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String task = fieldTask.getText();
                String dateString = fieldDate.getText();
                String timeString = fieldTime.getText();
                String fullTime = dateString+"T"+timeString;

                try {
                    LocalDateTime dueDate = LocalDateTime.parse(fullTime, 
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                    String description = fieldDescription.getText();

                    if (!category[0].isEmpty()) {
                        try {
                            TodoList newTodo = new TodoList(task, description,
                              dueDate, Categories.valueOf(category[0]));
                            try {
                                todoListServiceImp.AddTodoListService(newTodo);
                                todoListServiceImp.intervalTime(dueDate);
                                lblMessage.setText("Successfully add TODO");
                            } catch (IllegalStateException ex) {
                                JOptionPane.showMessageDialog(null,
                                        ex.getMessage());
                            }
                        } catch (IllegalArgumentException ex) {
                            System.err.println("Invalid category selected: "
                                    + category[0]);
                        }
                    } else {
                        System.err.println("No category selected");
                    }

                    fieldTask.setText("");
                    fieldDate.setText("");
                    fieldDescription.setText("");
                } catch (DateTimeParseException ex) {
                    System.err.println("Format invalid: Please enter date in "
                            + "'yyyy-MM-ddTHH:mm:ss' format.");
                }
            }
        });


        saveToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                int result = jFileChooser.showSaveDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    String path = jFileChooser.getSelectedFile().getAbsolutePath();
                    printToFile(path);
                }
            }
        });
    }

    private void updateTable(TodoList[] tasks) {
        TableModelTodo tableModel = new TableModelTodo(tasks);
        tableTodo.setModel(tableModel);
    }
   
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser1 = new com.raven.datechooser.DateChooser();
        jpaneTodo = new javax.swing.JPanel();
        background21 = new login.Background2();
        textAreaScroll1 = new textarea.TextAreaScroll();
        fieldDescription = new textarea.TextArea();
        comboBoxCategories = new combobox.Combobox();
        fieldTask = new swing.textfield1();
        fieldTime = new swing.textfield1();
        fieldDate = new swing.textfield1();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        btnGetSortedTasks = new java.awt.Button();
        saveToFile = new java.awt.Button();
        btnDelete = new java.awt.Button();
        button1 = new java.awt.Button();
        btnUpdate = new java.awt.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTodo = new javax.swing.JTable();
        btnAdd = new java.awt.Button();

        dateChooser1.setDateFormat("yyyy-MM-dd");
        dateChooser1.setTextRefernce(fieldDate);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        fieldDescription.setColumns(20);
        fieldDescription.setRows(5);
        textAreaScroll1.setViewportView(fieldDescription);

        background21.add(textAreaScroll1);
        textAreaScroll1.setBounds(560, 80, 160, 70);

        comboBoxCategories.setLabeText("Categories");
        comboBoxCategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxCategoriesActionPerformed(evt);
            }
        });
        background21.add(comboBoxCategories);
        comboBoxCategories.setBounds(740, 110, 140, 40);

        fieldTask.setLabelText("Task");
        fieldTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldTaskActionPerformed(evt);
            }
        });
        background21.add(fieldTask);
        fieldTask.setBounds(60, 100, 150, 46);

        fieldTime.setLabelText("Time");
        fieldTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldTimeActionPerformed(evt);
            }
        });
        background21.add(fieldTime);
        fieldTime.setBounds(390, 100, 140, 46);

        fieldDate.setLabelText("Date");
        fieldDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDateActionPerformed(evt);
            }
        });
        background21.add(fieldDate);
        fieldDate.setBounds(230, 100, 140, 46);

        jLabel1.setFont(new java.awt.Font("STHupo", 0, 36)); // NOI18N
        jLabel1.setText("To Do List");
        background21.add(jLabel1);
        jLabel1.setBounds(800, 0, 190, 50);

        jLabel2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 24)); // NOI18N
        jLabel2.setText("What's Your Daily Activities?");
        background21.add(jLabel2);
        jLabel2.setBounds(30, 30, 310, 60);
        background21.add(lblMessage);
        lblMessage.setBounds(350, 40, 400, 30);
        lblMessage.getAccessibleContext().setAccessibleName("lblMessage");

        btnGetSortedTasks.setActionCommand("Sorted by Categories");
        btnGetSortedTasks.setLabel("SortedCategories");
        btnGetSortedTasks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetSortedTasksActionPerformed(evt);
            }
        });
        background21.add(btnGetSortedTasks);
        btnGetSortedTasks.setBounds(110, 630, 270, 40);

        saveToFile.setLabel("saveToFile");
        saveToFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveToFileActionPerformed(evt);
            }
        });
        background21.add(saveToFile);
        saveToFile.setBounds(860, 640, 90, 24);

        btnDelete.setLabel("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        background21.add(btnDelete);
        btnDelete.setBounds(670, 630, 170, 40);

        button1.setLabel("Exit");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });
        background21.add(button1);
        button1.setBounds(50, 630, 35, 24);

        btnUpdate.setLabel("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        background21.add(btnUpdate);
        btnUpdate.setBounds(400, 630, 250, 40);

        tableTodo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableTodo);

        background21.add(jScrollPane1);
        jScrollPane1.setBounds(40, 180, 920, 402);

        btnAdd.setActionCommand("Add");
        btnAdd.setLabel("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        background21.add(btnAdd);
        btnAdd.setBounds(900, 120, 36, 24);

        javax.swing.GroupLayout jpaneTodoLayout = new javax.swing.GroupLayout(jpaneTodo);
        jpaneTodo.setLayout(jpaneTodoLayout);
        jpaneTodoLayout.setHorizontalGroup(
            jpaneTodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        jpaneTodoLayout.setVerticalGroup(
            jpaneTodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpaneTodo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpaneTodo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void fieldTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldTaskActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldTaskActionPerformed

    private void fieldDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDateActionPerformed

    private void fieldTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldTimeActionPerformed

    private void comboBoxCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxCategoriesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxCategoriesActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button2ActionPerformed

    private void btnGetSortedTasksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetSortedTasksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGetSortedTasksActionPerformed

    private void saveToFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveToFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveToFileActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_button1ActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Todolist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Todolist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Todolist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Todolist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Todolist().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private login.Background2 background21;
    private java.awt.Button btnAdd;
    private java.awt.Button btnDelete;
    private java.awt.Button btnGetSortedTasks;
    private java.awt.Button btnUpdate;
    private java.awt.Button button1;
    private combobox.Combobox comboBoxCategories;
    private com.raven.datechooser.DateChooser dateChooser1;
    private swing.textfield1 fieldDate;
    private textarea.TextArea fieldDescription;
    private swing.textfield1 fieldTask;
    private swing.textfield1 fieldTime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpaneTodo;
    private javax.swing.JLabel lblMessage;
    private java.awt.Button saveToFile;
    private javax.swing.JTable tableTodo;
    private textarea.TextAreaScroll textAreaScroll1;
    // End of variables declaration//GEN-END:variables

    private static class TableModelTodo extends AbstractTableModel {
        private final String[] COLUMNS = {"TASK", "DATE", "DESCRIPTION", 
                                        "CATEGORIES"};
        private TodoListServiceImp todoListServiceImp;
        private final TodoList[] todoLists;

        public TableModelTodo(TodoList[] tasks) {
            this.todoLists = tasks;
        }


        public TableModelTodo(TodoListServiceImp todoListServiceImp) {
            this.todoListServiceImp = todoListServiceImp;
            this.todoLists = todoListServiceImp.getTodoLists();
        }

        @Override
        public int getRowCount() {
            return todoLists.length;
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern
                                            ("yyyy-MM-dd HH:mm");
            if (todoLists[rowIndex] != null) {
                TodoList todo = todoLists[rowIndex];
                return switch (columnIndex) {
                    case 0 -> todo.getAddTask();
                    case 1 -> todo.getDeadLine().format(formatter);
                    case 2 -> todo.getDescription();
                    case 3 -> todo.getCategories();
                    default -> null;
                };
            }
            return null;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 3) {
                return Categories.class;
            }
            return super.getColumnClass(columnIndex);
        }
    }
}
