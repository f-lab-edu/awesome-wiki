package kr.flab.wiki.core.login.persistence;

public class UserEntity implements User {

    private String email;
    private String password;

    public UserEntity(String email, String password){
        this.email = "";
        this.password = "";
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
