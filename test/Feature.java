

package test;


import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

/*
    This class takes the features/variables of the network log file and creates an object for the feature
 */

public class Feature<E> {

    private String columnHeader;
    private E featureType;
    private List<E> features;

    public Feature(E featureType){
        this.featureType = featureType;
    }

    public Feature(){
        this.features = new ArrayList<>();
    }

    public E getFeatureType(){
        return featureType;
    }

    public void addFeature( E featureType){
        features.add(featureType);
    }

    public void setColumnHeader(String header){
        this.columnHeader = header;
    }

    public void removeFeature(E featureType){
        features.remove(featureType);
    }

    public E getFeatureIndex(int index){
        return features.get(index);
    }

    public List<E> getFeaturesList(){
        return features;
    }

    @Override
    public String toString(){
        return columnHeader;
    }
}
