package RadioInfo.view;

import RadioInfo.model.ParsingError;

import javax.swing.*;

/**
 * Presents error message to the end user
 * @version 1.0
 * @author Isidor Nygren
 */
public class ErrorDialog {
    /**
     * Presents an error message via a ParsingError object
     * @param error the error to present
     */
    public ErrorDialog(ParsingError error){
        JOptionPane.showMessageDialog(new JFrame(), error.toString(),
                error.getTitle(), JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Presents an error message to the end user through a message dialog frame
     * @param title the top title of the frame
     * @param message the message to print in the error
     */
    public ErrorDialog(String title, String message){
        JOptionPane.showMessageDialog(new JFrame(), "Error: " +
                message, title, JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Presents an error message to the end user through a message dialog frame
     * with an exception
     * @param title the top title of the frame
     * @param message the message to print in the error
     * @param exception the exception that occured
     */
    public ErrorDialog(String title, String message, Exception exception){
        JOptionPane.showMessageDialog(new JFrame(), "Error: " +
                message + "\n" + exception.toString(), title, JOptionPane.ERROR_MESSAGE);
    }
}
