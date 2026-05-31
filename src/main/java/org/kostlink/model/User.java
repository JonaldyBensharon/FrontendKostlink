package org.kostlink.model;

public abstract class User {
    protected String username;
    protected String password;
    protected final Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }

    // Polimorfisme routing dashboard
    public abstract void bukaDashboard();
}