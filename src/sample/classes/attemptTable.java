/* creates attempt objects to be inserted into table*/

package sample.classes;

public class attemptTable {
    private int ID;
    private String userName;
    private Float Score;

    public attemptTable(int id, String user, Float score){
        setID(id);
        setUserName(user);
        setScore(score);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Float getScore() {
        return Score;
    }

    public void setScore(Float score) {
        Score = score;
    }
}
