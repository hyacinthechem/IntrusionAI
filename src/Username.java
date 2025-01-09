package src;
import java.util.Objects;
public class Username {

    private final String username;
    public static final String rowHeader = "USERNAME";


    public Username(String username){
        this.username = username;
    }


    public String getUsername(){
        return username;
    }

    public String getRowHeader(){
        return rowHeader;
    }


    @Override
    public String toString(){
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj){
            return true;
        }
        if(obj==null || obj.getClass()!=this.getClass()){
            return false;
        }
        Username other = (Username) obj;
        return this.username.equals(other.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }


}
