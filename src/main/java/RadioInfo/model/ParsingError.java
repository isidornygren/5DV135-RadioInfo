package RadioInfo.model;
/**
 * Error model used in returning parsing data
 * @version 1.0
 * @author Isidor Nygren
 */
public class ParsingError {
    Exception exception;
    String errorString;

    /**
     * Creates an error object with a string
     * @param errorString the string to show to the user
     */
    public ParsingError(String errorString){
        this.errorString = errorString;
    }

    /**
     * Creates an error object with a string and an exception
     * @param errorString the string to show to the user
     * @param exception the exception to show to the user
     */
    public ParsingError(String errorString, Exception exception){
        this.errorString = errorString;
        this.exception = exception;
    }

    /**
     * Checks wether the error was a result from an exception
     * @return true if it was the result from an exception otherwise false
     */
    public boolean isException(){
        return (this.exception != null);
    }

    /**
     * Builds the error to a human readable string
     * @return the string
     */
    public String toString(){
        if(this.isException()){
            return errorString + "\n" + exception.toString();
        }else{
            return errorString;
        }
    }
}
