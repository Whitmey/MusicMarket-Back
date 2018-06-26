package app.model;

public class User {

    private String id;
    private String username;
    private String password;
    private Double balance;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String id, String username, Double balance) {
        this.id = id;
        this.username = username;
        this.balance = balance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
