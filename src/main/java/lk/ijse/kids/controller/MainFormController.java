package lk.ijse.kids.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.kids.entity.Book;
import lk.ijse.kids.exception.BlankFieldException;
import lk.ijse.kids.exception.InvalidBookException;
import lk.ijse.kids.exception.InvalidFieldException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MainFormController {
    public TextField txtId;
    public TextField txtTitle;
    public TextField txtAuthor;
    public TextField txtGenre;
    public TextField txtPrice;
    public DatePicker txtDate;
    public TextField txtDescription;
    public TableView<Book> tblBooks;
    public Label lblSelectStatus;
    public Label lblStatus;
    public ProgressBar pgb;
    public Button btnNew;
    public Button btnSave;
    public Button btnDelete;
    public Button btnExport;
    public Button btnImport;
    public Button btnPrint;
    public AnchorPane root;
    private HashMap<String, Node> textFieldMap;
    private List<Book> selectedBooks;

    public void initialize() {
        textFieldMap = new HashMap<>();
        textFieldMap.put("id", txtId);
        textFieldMap.put("title", txtTitle);
        textFieldMap.put("author", txtAuthor);
        textFieldMap.put("price", txtPrice);
        textFieldMap.put("genre", txtGenre);
        textFieldMap.put("description", txtDescription);
        textFieldMap.put("date", txtDate);
        selectedBooks = new ArrayList<>();

        tblBooks.setEditable(true);
        TableColumn<Book, Boolean> firstCol = (TableColumn<Book, Boolean>) tblBooks.getColumns().get(0);
        firstCol.setCellFactory(bookBookTableColumn -> new CheckBoxTableCell<>());
        firstCol.setCellValueFactory(param -> {
            SimpleBooleanProperty chk = new SimpleBooleanProperty();
            chk.addListener((observableValue, oldValue, newValue) -> {
                if (newValue){
                    selectedBooks.add(param.getValue());
                }else{
                    selectedBooks.remove(param.getValue());
                }
                updateSelectedStatus();
            });
            return chk;
        });
        tblBooks.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblBooks.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("title"));
        tblBooks.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblBooks.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("price"));

        loadAllBooksFromDB();

        tblBooks.getSelectionModel().selectedItemProperty().
                addListener((observableValue, previousSelectedRow, currentSelectedRow) -> {
            if (currentSelectedRow == null)return;

            txtId.setText(currentSelectedRow.getId());
            txtTitle.setText(currentSelectedRow.getTitle());
            txtAuthor.setText(currentSelectedRow.getAuthor());
            txtGenre.setText(currentSelectedRow.getGenre());
            txtPrice.setText(currentSelectedRow.getPrice().toString());
            txtDate.setValue(currentSelectedRow.getPublishedDate());
            txtDescription.setText(currentSelectedRow.getDescription());

            root.getChildren().forEach(node -> {
                if (node instanceof TextField || node instanceof DatePicker) node.setDisable(true);
            });
            btnSave.setDisable(true);
        });
    }

    private void updateSelectedStatus(){
        lblSelectStatus.setText(String.format("%d/%d SELECTED", selectedBooks.size(), tblBooks.getItems().size()));
        btnDelete.setDisable(selectedBooks.isEmpty());
        btnExport.setDisable(selectedBooks.isEmpty());
    }

    private void loadAllBooksFromDB() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep9_kids", "root", "mysql")) {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Book");

            while (rst.next()) {
                String id = rst.getString("id");
                String title = rst.getString("title");
                String author = rst.getString("author");
                String genre = rst.getString("genre");
                BigDecimal price = rst.getBigDecimal("price");
                LocalDate publishedDate = rst.getDate("published_date").toLocalDate();
                String description = rst.getString("description");
                tblBooks.getItems().add(new Book(id, title, author, genre, price, publishedDate, description));
            }
            updateSelectedStatus();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load books");
            e.printStackTrace();
        }
    }

    public void btnNewOnAction(ActionEvent actionEvent) {
        root.getChildren().forEach(node -> {
            if (node instanceof TextField || node instanceof DatePicker) node.setDisable(false);
            if (node instanceof TextField) ((TextField) node).clear();
        });
        btnSave.setDisable(false);
        txtDate.setValue(LocalDate.now());
        txtId.requestFocus();
        tblBooks.getSelectionModel().clearSelection();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        try {
            insertBook(txtId.getText(), txtTitle.getText(), txtAuthor.getText(), txtGenre.getText(),
                    txtPrice.getText(), txtDate.getValue(), txtDescription.getText());
        } catch (InvalidBookException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            textFieldMap.get(e.getField()).requestFocus();
            if (textFieldMap.get(e.getField()) instanceof TextField)
                ((TextField) textFieldMap.get(e.getField())).selectAll();
        }
    }

    private void insertBook(String id,
                            String title,
                            String author,
                            String genre,
                            String strPrice,
                            LocalDate publishedDate,
                            String description) throws BlankFieldException, InvalidFieldException {

        /* Data Validation Logic */
        if (id == null || id.isBlank()) {
            throw new BlankFieldException("Book's id can't be empty", "id");
        } else if (!id.matches("BK\\d{3}")) {
            throw new InvalidFieldException("Invalid book id", "id");
        } else if (title == null || title.isBlank()) {
            throw new BlankFieldException("Book's title can't be empty", "title");
        } else if (author == null || author.isBlank()) {
            throw new BlankFieldException("Book's author can't be empty", "author");
        } else if (!author.matches("[A-Za-z ][A-Za-z ,]+")) {
            throw new InvalidFieldException("Invalid author", "author");
        } else if (genre == null || genre.isBlank()) {
            throw new BlankFieldException("Book's genre can't be empty", "genre");
        } else if (strPrice == null || strPrice.isBlank()) {
            throw new BlankFieldException("Book's price can't be empty", "price");
        } else if (!strPrice.matches("\\d+([.]\\d{1,2})?")) {
            throw new InvalidFieldException("Invalid book price", "price");
        } else if (new BigDecimal(strPrice).compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidFieldException("Book price can't be a negative value or zero", "price");
        } else if (publishedDate == null) {
            throw new BlankFieldException("Book's published date can't be empty", "date");
        } else if (publishedDate.isAfter(LocalDate.now())) {
            throw new InvalidFieldException("Book's published date can't be a future date", "date");
        }

        try (Connection connection =
                     DriverManager.getConnection("jdbc:mysql://localhost:3306/dep9_kids",
                             "root", "mysql")) {

            /* Business Validation */
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM Book WHERE id=?");
            stm.setString(1, id);
            ResultSet rst = stm.executeQuery();
            if (rst.next()) throw new InvalidFieldException("Book id already exists", "id");

            PreparedStatement stm2 = connection.
                    prepareStatement("INSERT INTO Book VALUES (?, ?, ?, ?, ?, ?, ?)");
            stm2.setString(1, id);
            stm2.setString(2, title);
            stm2.setString(3, author);
            stm2.setString(4, genre);
            stm2.setBigDecimal(5, new BigDecimal(strPrice));
            stm2.setDate(6, Date.valueOf(publishedDate));
            stm2.setString(7, description);

            stm2.executeUpdate();

            Book newBook = new Book(id, title, author, genre, new BigDecimal(strPrice), publishedDate, description);
            tblBooks.getItems().add(newBook);
            updateSelectedStatus();

            btnNew.fire();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save the book").show();
            e.printStackTrace();
        }
    }


    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Optional<ButtonType> response = new Alert(Alert.AlertType.CONFIRMATION,
                String.format("There are %d records selected. Are you sure to delete?", selectedBooks.size()),
                ButtonType.YES, ButtonType.NO).showAndWait();
        if (response.get() == ButtonType.NO) return;

        try (Connection connection =
                     DriverManager.getConnection("jdbc:mysql://localhost:3306/dep9_kids",
                             "root", "mysql")) {
            PreparedStatement stm = connection.prepareStatement("DELETE FROM Book WHERE id=?");
            for (Book book : selectedBooks) {
                stm.setString(1, book.getId());
                stm.executeUpdate();
            }
            tblBooks.getItems().removeAll(selectedBooks);
            selectedBooks.clear();
            updateSelectedStatus();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete records").show();
            e.printStackTrace();
        }
    }

    public void btnExportOnAction(ActionEvent actionEvent) {
    }

    public void btnImportOnAction(ActionEvent actionEvent) {
    }

    public void btnPrintOnAction(ActionEvent actionEvent) {
    }
}
