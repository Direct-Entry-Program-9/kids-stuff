package lk.ijse.kids.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.kids.entity.Book;
import lk.ijse.kids.exception.BlankFieldException;
import lk.ijse.kids.exception.InvalidBookException;
import lk.ijse.kids.exception.InvalidFieldException;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    public void initialize(){

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
        String id = txtId.getText();
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String genre = txtGenre.getText();
        String strPrice = txtPrice.getText();
        String description = txtDescription.getText();

        try {
            insertBook(id, title, author, genre, strPrice, txtDate.getValue(), description);
        } catch (BlankFieldException e) {
            switch (e.getField()){
                case "id":
                    new Alert(Alert.AlertType.ERROR, "Book id can't be empty").show();
                    txtId.requestFocus();
                    txtId.selectAll();
                    break;
                case "title":
                    new Alert(Alert.AlertType.ERROR, "Book title can't be empty").show();
                    txtTitle.requestFocus();
                    txtTitle.selectAll();
                    break;
                case "author":
                    break;
                case "genre":
                    break;
                case "price":
                    break;
                case "date":
                    break;
            }
        } catch (InvalidFieldException e) {
            switch (e.getField()){
                case "id":
                    new Alert(Alert.AlertType.ERROR, "Invalid book id").show();
                    txtId.requestFocus();
                    txtId.selectAll();
                    break;
                case "title":
                    break;
                case "author":
                    break;
                case "genre":
                    break;
                case "price":
                    break;
                case "date":
                    break;
            }
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
            throw new BlankFieldException("id");
        }else if (!id.matches("BK\\d{3}")){
            throw new InvalidFieldException("id");
        }else if (title == null || title.isBlank()){
            throw new BlankFieldException("title");
        }else if (author == null || author.isBlank()){
            throw new BlankFieldException("author");
        }else if (!author.matches("[A-Za-z ][A-Za-z ,]+")){
            throw new InvalidFieldException("author");
        }else if (genre == null || genre.isBlank()) {
            throw new BlankFieldException("genre");
        }else if (strPrice == null || strPrice.isBlank()){
            throw new BlankFieldException("price");
        }else if (!strPrice.matches("\\d+([.]\\d{1,2})?")) {
            throw new InvalidFieldException("price");
        }else if(new BigDecimal(strPrice).compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidFieldException("price");
        }else if (publishedDate == null){
            throw new BlankFieldException("date");
        }else if (publishedDate.isAfter(LocalDate.now())){
            throw new InvalidFieldException("date");
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
