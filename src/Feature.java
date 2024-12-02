

package src;


import ecs100.*;
import java.util.*;

/*
    This class takes the features/variables of the network log file and creates an object for the feature
 */

public class Feature<E> implements Iterable<E> {

    private E featureType;
    private Set<E> features;

    public Feature(E featureType){
        this.featureType = featureType;
    }

    public E getFeatureType(){
        return featureType;
    }

    @Override
    public String toString(){
        return featureType.toString();
    }

    @Override
    public Iterator<E> iterator(){
        return this.features.iterator();
    }

}
