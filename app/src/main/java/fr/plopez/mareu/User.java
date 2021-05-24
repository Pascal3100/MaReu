package fr.plopez.mareu;

import androidx.annotation.Nullable;

public class User {

    private final Integer age;
    private final String name;

    public User(Integer age, String name) {
        this.age = age;
        this.name = name;
    }

    @Nullable
    public Integer getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}
