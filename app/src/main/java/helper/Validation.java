package helper;

/**
 * Created by TobiasOlsson on 15-12-29.
 */
public class Validation {

    public static final int passwordMaxLength = 25;
    public static final int passwordMinLength = 4;

    public static boolean validateEmail(String email){

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean validatePassword(String password){
        if (password.isEmpty() || password.length() < passwordMinLength || password.length() > passwordMaxLength) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean validateName(String name){
        if(name.isEmpty() || name.length() < 3){
            return false;
        }
        else{
            return true;
        }
    }
}
