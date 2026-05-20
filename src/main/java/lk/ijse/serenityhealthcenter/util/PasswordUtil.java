package lk.ijse.serenityhealthcenter.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static  boolean verifyPassword(String plainPassword , String hashPassword){
        try{
            return BCrypt.checkpw(plainPassword, hashPassword);
        }catch (Exception e){
            return  false;
        }
    }


    public  static  boolean isHashed(String password){
        return password != null && password.startsWith("$2a$");
    }
}
