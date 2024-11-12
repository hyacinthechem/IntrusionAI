public class UserType {

    public boolean validUser;

    public UserType(boolean validUser){
        this.validUser = validUser;
    }

    public boolean isValidUser(){
        return validUser;
    }

    public boolean isInvalidUser(){
        return !validUser;
    }
}
