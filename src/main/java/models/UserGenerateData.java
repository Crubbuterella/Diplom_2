package models;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerateData extends User {
    public User getRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(9) + "@mail.ru";
        String password = RandomStringUtils.randomAlphabetic(9);
        String name = RandomStringUtils.randomAlphabetic(9);
        return new User(email, password, name);
    }
}
