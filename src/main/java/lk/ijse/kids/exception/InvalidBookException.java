package lk.ijse.kids.exception;

public class InvalidBookException extends Exception{

    public InvalidBookException() {
    }

    public InvalidBookException(String message) {
        super(message);
    }

    public InvalidBookException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBookException(Throwable cause) {
        super(cause);
    }

    public static class BlankBookIdException extends InvalidBookException{}
    public static class BlankBookTitleException extends InvalidBookException{}
    public static class BlankBookAuthorException extends InvalidBookException{}
    public static class BlankBookGenreException extends InvalidBookException{}
    public static class BlankBookPriceException extends InvalidBookException{}
    public static class BlankBookPublishedDateException extends InvalidBookException{}

    public static class InvalidBookIdException extends InvalidBookException{}
    public static class InvalidBookAuthorException extends InvalidBookException{}
    public static class InvalidBookPriceException extends InvalidBookException{}
    public static class InvalidBookPublishedDateException extends InvalidBookException{}
}


