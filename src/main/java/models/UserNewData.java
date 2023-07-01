package models;

import org.apache.commons.lang3.RandomStringUtils;

public class UserNewData {
    private String email;
    private String password;
    private String name;

    public UserNewData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;

    }

    public UserNewData() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserNewData random() {
        return new UserNewData(RandomStringUtils.randomAlphabetic(9) + "@mail.ru",
                RandomStringUtils.randomAlphabetic(9), RandomStringUtils.randomAlphabetic(9));

    }
}
