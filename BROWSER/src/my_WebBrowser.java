
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

/**
 * A basic multi-window web browser.  This class is responsible for
 * creating new windows and for maintaining a list of currently open
 * windows.  The program ends when all windows have been closed.
 * The windows are of type my_BrowserWindow.  The program also requires
 * the class SimpleDialogs.  The first window, which opens when the
 * program starts, goes to "https://www.google.com.
 */
public class my_WebBrowser extends Application {

    public static void main(String[] my_args) {
        launch(my_args);
    }
    //----------------------------------------------------------------------------------------------------
    
    private ArrayList<my_BrowserWindow> my_openWindows;  // list of currently open web browser windows
    private Rectangle2D my_screenRect;                // usable area of the primary screen
    private double my_locationX, my_locationY;           // location for next window to be opened
    private double my_windowWidth, my_windowHeight;      // window size, computed from my_screenRect
    private int my_untitledCount;                     // how many "Untitled" window titles have been used
    
    
    /* Opens a window that will load the my_URL https://www.google.com
     * (the front page of the textbook in which this program is an example).
     * Note that the Stage parameter to this method is never used.
     */
    public void start(Stage stage) {
        
        my_openWindows = new ArrayList<my_BrowserWindow>();  // List of open windows.
        
        my_screenRect = Screen.getPrimary().getVisualBounds();
        
           // (my_locationX,my_locationY) will be the location of the upper left
           // corner of the next window to be opened.  For the first window,
           // the window is moved a little down and over from the top-lefthttps://www.google.html/
           // corner of the primary screen's visible bounds.
        my_locationX = my_screenRect.getMinX() + 30;
        my_locationY = my_screenRect.getMinY() + 20;
        
           // The window size depends on the height and width of the screen's
           // visual bounds, allowing some extra space so that it will be
           // possible to stack several windows, each displaced from the
           // previous one.  (For aesthetic reasons, limit the width to be
           // at most 1.6 times the height.)
        my_windowHeight = my_screenRect.getHeight() - 160;
        my_windowWidth = my_screenRect.getWidth() - 130;
        if (my_windowWidth > my_windowHeight*1.6)
            my_windowWidth = my_windowHeight*1.6;
        
           // Open the first window, showing the front page of this textbook.
        my_newBrowserWindow("https://www.google.com");

    } // end start()
    
    /**
     * Get the list of currently open windows.  The browser windows use this
     * list to construct their Window menus.
     * A package-private method that is meant for use only in my_BrowserWindow.java.
     */
    ArrayList<my_BrowserWindow> getOpenWindowList() {
        return my_openWindows;
    }
    
    /**
     * Get the number of window titles of the form "Untitled XX" that have been
     * used.  A new window that is opened with a null my_URL gets a title of
     * that form.  This method is also used in my_BrowserWindow to provide a
     * title for any web page that does not itself provide a title for the page.
     * A package-private method that is meant for use only in my_BrowserWindow.java.
     */
    int my_getNextUntitledCount() {
        return ++my_untitledCount;
    }
    
    /**
     * Open a new browser window.  If my_url is non-null, the window will load that my_URL.
     * A package-private method that is meant for use only in my_BrowserWindow.java.
     * This method manages the locations for newly opened windows.  After a window
     * opens, the next window will be offset by 30 pixels horizontally and by 20
     * pixels vertically from the location of this window; but if that makes the
     * window extend outside my_screenRect, the horizontal or vertical position will
     * be reset to its minimal value.
     */
    void my_newBrowserWindow(String my_url) {
        my_BrowserWindow window = new my_BrowserWindow(this,my_url);
        my_openWindows.add(window);   // Add new window to open window list.
        window.setOnHidden( e -> {
                // Called when the window has closed.  Remove the window
                // from the list of open windows.
            my_openWindows.remove( window );
            System.out.println("Number of open windows is " + my_openWindows.size());
            if (my_openWindows.size() == 0) {
                // Program ends automatically when all windows have been closed.
                System.out.println("Program will end because all windows have been closed");
            }
        });
        if (my_url == null) {
            window.setTitle("my_Untitled " + my_getNextUntitledCount());
        }
        window.setX(my_locationX);         // set location and size of the window
        window.setY(my_locationY);
        window.setWidth(my_windowWidth);
        window.setHeight(my_windowHeight);
        window.show();
        my_locationX += 30;    // set up location of NEXT window
        my_locationY += 20;
        if (my_locationX + my_windowWidth + 10 > my_screenRect.getMaxX()) {
                // Window would extend past the right edge of the screen,
                // so reset my_locationX to its original value.
            my_locationX = my_screenRect.getMinX() + 30;
        }
        if (my_locationY + my_windowHeight + 10 > my_screenRect.getMaxY()) {
                // Window would extend past the bottom edge of the screen,
                // so reset my_locationY to its original value.
            my_locationY = my_screenRect.getMinY() + 20;
        }
    }
    
    
} // end WebBrowser