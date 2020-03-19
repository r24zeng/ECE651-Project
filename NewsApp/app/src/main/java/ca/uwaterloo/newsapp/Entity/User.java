package ca.uwaterloo.newsapp.Entity;

public class User {
    private String username;
    private String password;
    private String department;
    private String faculty;
    private String following;
    private int gender;
    private String name;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(){

    }



    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDepartment() {
        return department;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getFollowing() {
        return following;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
