package com.example.alejandro.findmyplace.saved_places;

import com.example.alejandro.findmyplace.Place;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by diego on 11/04/18.
 */

public class SortingManager<T extends Place>{

    private T[] array;
    private Class aClass;
    private LatLng fromLocation;

    public SortingManager(T[] array, Class aClass, LatLng fromLocation) {
        this.array = array;
        this.aClass = aClass;
        this.fromLocation = fromLocation;
    }

    public List<T> mergeSort(){
        mergeSort(array,0,array.length-1);
        return new LinkedList<>(Arrays.asList(array));
    }

    //Merge Sort Algorithm
    private void mergeSort(T[] array, int beginningIndex, int endingIndex) {
        if(beginningIndex<endingIndex){

            int halfIndex = (beginningIndex + endingIndex) / 2;

            mergeSort(array,beginningIndex,halfIndex);
            mergeSort(array,halfIndex+1,endingIndex);

            merge(array,beginningIndex,halfIndex,endingIndex);
        }
    }

    //Second Part of Merge Sort Algorithm
    private void merge(T[] array, int beginningIndex, int halfIndex, int endingIndex){
        //Calculates the size of temporary arrays to be used
        int firstHalfSize = halfIndex - beginningIndex + 1;
        int secondHalfSize = endingIndex - halfIndex;

        //Temporary arrays

        T[] firstHalf = (T[]) Array.newInstance(aClass,firstHalfSize);
        T[] secondHalf = (T[]) Array.newInstance(aClass,secondHalfSize);

        //Transfer data to temporary arrays from original array
        for (int i=0; i<firstHalfSize; i++){
            firstHalf[i] = array[beginningIndex + i];
        }
        for (int i=0; i<secondHalfSize; i++){
            secondHalf[i] = array[halfIndex + 1+ i];
        }

        int i = 0;
        int j = 0;
        int k = beginningIndex;

        //Sorts elements
        while (i < firstHalfSize && j < secondHalfSize) {
            // Change this condition accordingly
            if (firstHalf[i].getDistance(fromLocation)<=secondHalf[j].getDistance(fromLocation)) {
                array[k] = firstHalf[i];
                i++;
            } else {
                array[k] = secondHalf[j];
                j++;
            }
            k++;
        }

        while (i < firstHalfSize) {
            array[k] = firstHalf[i];
            i++;
            k++;
        }

        while (j < secondHalfSize) {
            array[k] = secondHalf[j];
            j++;
            k++;
        }
    }


}

