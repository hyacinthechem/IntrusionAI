

package src;


import ecs100.*;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

/*
    This class takes the features/variables of the network log file and creates an object for the feature
 */

public class Feature<E> {

    private E featureType;
    private List<E> features;

    public Feature(E featureType){
        this.featureType = featureType;
    }

    public E getFeatureType(){
        return featureType;
    }

    public void addFeature(E featureType){
        features.add(featureType);
    }

    public void removeFeature(E featureType){
        features.remove(featureType);
    }

    public E getFeatureIndex(int index){
        return features.get(index);
    }

    @Override
    public String toString(){
        return featureType.toString();
    }

}
