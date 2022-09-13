package lk.ijse.kids.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.kids.entity.Book;
import lk.ijse.kids.exception.InvalidBookException;

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
        } catch (InvalidBookException.BlankBookIdException e) {
            new Alert(Alert.AlertType.ERROR, "Id can't be empty").show();
            txtId.requestFocus();
            txtId.selectAll();
        } catch (InvalidBookException.BlankBookTitleException e) {
            new Alert(Alert.AlertType.ERROR, "Title can't be empty").show();
            txtTitle.requestFocus();
            txtTitle.selectAll();
        } catch (InvalidBookException.BlankBookGenreException e) {
            new Alert(Alert.AlertType.ERROR, "Genre can't be empty").show();
            txtGenre.requestFocus();
            txtGenre.selectAll();
        } catch (InvalidBookException.BlankBookAuthorException e) {
            new Alert(Alert.AlertType.ERROR, "Author can't be empty").show();
            txtAuthor.requestFocus();
            txtAuthor.selectAll();
        } catch (InvalidBookException.BlankBookPriceException e) {
            new Alert(Alert.AlertType.ERROR, "Price can't be empty").show();
            txtPrice.requestFocus();
            txtPrice.selectAll();
        } catch (InvalidBookException.BlankBookPublishedDateException e) {
            new Alert(Alert.AlertType.ERROR, "Published date can't be empty").show();
            txtDate.requestFocus();
        } catch (InvalidBookException.InvalidBookIdException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid book id").show();
            txtId.requestFocus();
            txtId.selectAll();
        } catch (InvalidBookException.InvalidBookAuthorException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid author name(s)").show();
            txtAuthor.requestFocus();
            txtAuthor.selectAll();
        } catch (InvalidBookException.InvalidBookPriceException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid price").show();
            txtPrice.requestFocus();
            txtPrice.selectAll();
        } catch (InvalidBookException.InvalidBookPublishedDateException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date, a future date can't be set as published date").show();
            txtDate.requestFocus();
        }
    }

    private void insertBook(String id,
                            String title,
                            String author,
                            String genre,
                            String strPrice,
                            LocalDate publishedDate,
                            String description) throws InvalidBookException.BlankBookIdException, InvalidBookException.InvalidBookIdException, InvalidBookException.BlankBookTitleException, InvalidBookException.BlankBookAuthorException, InvalidBookException.InvalidBookAuthorException, InvalidBookException.BlankBookGenreException, InvalidBookException.InvalidBookPriceException, InvalidBookException.InvalidBookPublishedDateException, InvalidBookException.BlankBookPriceException, InvalidBookException.BlankBookPublishedDateException {

        if (id == null || id.isBlank()){
            throw new InvalidBookException.BlankBookIdException();
        }else if (!id.matches("BK\\d{3}")){
            throw new InvalidBookException.InvalidBookIdException();
        }else if (title == null || title.isBlank()){
            throw new InvalidBookException.BlankBookTitleException();
        }else if (author == null || author.isBlank()){
            throw new InvalidBookException.BlankBookAuthorException();
        }else if (!author.matches("[A-Za-z ][A-Za-z ,]+")){
            throw new InvalidBookException.InvalidBookAuthorException();
        }else if (genre == null || genre.isBlank()) {
            throw new InvalidBookException.BlankBookGenreException();
        }else if (strPrice == null){
            throw new InvalidBookException.BlankBookPriceException();
        }else if (!strPrice.matches("\\d+([.]\\d{1,2})?")) {
            throw new InvalidBookException.InvalidBookPriceException();
        }else if(new BigDecimal(strPrice).compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidBookException.InvalidBookPriceException();
        }else if (publishedDate == null){
            throw new InvalidBookException.BlankBookPublishedDateException();
        }else if (publishedDate.isAfter(LocalDate.now())){
            throw new InvalidBookException.InvalidBookPublishedDateException();
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
