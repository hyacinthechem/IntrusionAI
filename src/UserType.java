<<<<<<< HEAD
package src;

=======
>>>>>>> 21a2dde32d698303c0263578cb2b402237219f29
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
