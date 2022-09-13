package lk.ijse.kids.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.kids.entity.Book;
import lk.ijse.kids.exception.BlankFieldException;
import lk.ijse.kids.exception.InvalidBookException;
import lk.ijse.kids.exception.InvalidFieldException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

public class MainFormController {
    public TextField txtId;
    public TextField txtTitle;
    public TextField txtAuthor;
    public TextField txtGenre;
    public TextField txtPrice;
    public DatePicker txtDate;
    public TextField txtDescription;
    public TableView tblBooks;
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

    public void initialize(){
        textFieldMap = new HashMap<>();
        textFieldMap.put("id", txtId);
        textFieldMap.put("title", txtTitle);
        textFieldMap.put("author", txtAuthor);
        textFieldMap.put("price", txtPrice);
        textFieldMap.put("genre", txtGenre);
        textFieldMap.put("description", txtDescription);
        textFieldMap.put("date", txtDate);
    }

    public void btnNewOnAction(ActionEvent actionEvent) {
        root.getChildren().forEach(node -> {
            if (node instanceof TextField || node instanceof DatePicker) node.setDisable(false);
            if (node instanceof TextField) ((TextField) node).clear();
        });
        btnSave.setDisable(false);
        txtDate.setValue(LocalDate.now());
        txtId.requestFocus();
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
        if (id == null || id.isBlank()){
            throw new BlankFieldException("Book's id can't be empty", "id");
        }else if (!id.matches("BK\\d{3}")){
            throw new InvalidFieldException("Invalid book id", "id");
        }else if (title == null || title.isBlank()){
            throw new BlankFieldException("Book's title can't be empty", "title");
        }else if (author == null || author.isBlank()){
            throw new BlankFieldException("Book's author can't be empty", "author");
        }else if (!author.matches("[A-Za-z ][A-Za-z ,]+")){
            throw new InvalidFieldException("Invalid author","author");
        }else if (genre == null || genre.isBlank()) {
            throw new BlankFieldException("Book's genre can't be empty","genre");
        }else if (strPrice == null || strPrice.isBlank()){
            throw new BlankFieldException("Book's price can't be empty","price");
        }else if (!strPrice.matches("\\d+([.]\\d{1,2})?")) {
            throw new InvalidFieldException("Invalid book price", "price");
        }else if(new BigDecimal(strPrice).compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidFieldException("Book price can't be a negative value or zero","price");
        }else if (publishedDate == null){
            throw new BlankFieldException("Book's published date can't be empty", "date");
        }else if (publishedDate.isAfter(LocalDate.now())){
            throw new InvalidFieldException("Book's published date can't be a future date","date");
        }
    }


    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnExportOnAction(ActionEvent actionEvent) {
    }

    public void btnImportOnAction(ActionEvent actionEvent) {
    }

    public void btnPrintOnAction(ActionEvent actionEvent) {
    }
}
