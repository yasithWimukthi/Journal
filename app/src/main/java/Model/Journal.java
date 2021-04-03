package Model;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String thought;
    private String imageURL;
    private String userID;
    private String userName;
    private Timestamp timeAdded;

    public Journal() {
    }

    public Journal(String title, String thought, String imageURL, String userID, String userName, Timestamp timeAdded) {
        this.title = title;
        this.thought = thought;
        this.imageURL = imageURL;
        this.userID = userID;
        this.userName = userName;
        this.timeAdded = timeAdded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
}
