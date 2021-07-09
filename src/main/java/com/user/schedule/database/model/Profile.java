package com.user.schedule.database.model;

public class Profile{

    private final int id;
    private final String firstName;
    private final String lastName;
    private final String code;
    private final String role;

    public Profile(int id, String firstName, String lastName, String code, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.code = code;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCode() {
        return code;
    }

    public String getRole() {
        return role;
    }
    public static class UserPass{
        private String firstName;
        private String lastName;

        public UserPass(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }


        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }

    public static class ChangePass{

        private String currentPassword;
        private String newPassword;


        public ChangePass(String currentPassword, String newPassword) {
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
        }
        public String getCurrentPassword() {
            return currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }
    }
}
