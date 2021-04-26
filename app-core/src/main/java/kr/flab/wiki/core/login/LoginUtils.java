package kr.flab.wiki.core.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginUtils {

    private LoginUtils(){}

    public static boolean isValidEmail(String email){

        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if(matcher.matches()){
            return true;
        }
        return false;
    }
}
