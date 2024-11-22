<<<<<<< HEAD
package src;

=======
>>>>>>> 21a2dde32d698303c0263578cb2b402237219f29
import ecs100.*;
import java.util.*;

/*
    This class takes the features/variables of the network log file and creates an object for the feature
 */

public class Feature<E> {

    private E featureType;

    public Feature(E featureType){
        this.featureType = featureType;
    }

    public E getFeatureType(){
        return featureType;
    }

}
