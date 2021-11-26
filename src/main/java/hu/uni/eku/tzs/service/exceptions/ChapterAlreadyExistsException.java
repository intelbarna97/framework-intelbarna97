package hu.uni.eku.tzs.service.exceptions;

public class ChapterAlreadyExistsException extends Exception {
    public ChapterAlreadyExistsException() {
    }

    public ChapterAlreadyExistsException(String message) {
        super(message);
    }

    public ChapterAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChapterAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ChapterAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
