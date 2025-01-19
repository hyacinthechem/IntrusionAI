package src.RequestFeatures;
public class UserType {

    public boolean validUser;
    public static final String rowHeader = "USER-TYPE";


    public UserType(boolean validUser){
        this.validUser = validUser;
    }

    public boolean isValidUser(){
        return validUser;
    }

    public boolean isInvalidUser(){
        return !validUser;
    }

    public String getRowHeader(){
        return rowHeader;
    }

    public String toString(){
        return validUser? "valid_user" : "invalid_user";
    }
}
