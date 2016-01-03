package helper;

/**
 * Created by TobiasOlsson on 15-12-29.
 */
public class Validation {

    public static boolean validateEmail(String email){

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean validatePassword(String password){
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
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
