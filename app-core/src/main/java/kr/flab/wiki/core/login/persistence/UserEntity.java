package kr.flab.wiki.core.login.persistence;

public class UserEntity implements User {

    private final String email;
    private final String password;

    public UserEntity(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
