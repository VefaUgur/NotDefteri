package com.example.colorsplit;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DominantColorFinder {

    private final int[] colourFrequencyList = {0,0,0,0,0,0,0}; //red-blue-green
    private String[] colourNames = {"BLUE","RED","GREEN","BLACK","WHITE","YELLOW","MAGENTA"};
    private Image image;
    List<MainColour> colors = new ArrayList<>();

    public DominantColorFinder() {
        colors.add(new MainColour(0,0,255));
        colors.add(new MainColour(255,0,0));
        colors.add(new MainColour(0,255,0));
        colors.add(new MainColour(0,0,0));
        colors.add(new MainColour(255,255,255));
        colors.add(new MainColour(255,255,0));
        colors.add(new MainColour(255,0,255));
    }

    public void findDominant(Image image){
        Bitmap bitmap = image.getBitmap();

        for(int x = 0 ; x<bitmap.getHeight(); x++){
            for(int y = 0 ; y<bitmap.getWidth(); y++){

                int colour = bitmap.getPixel(x, y);
                int red = Color.red(colour);
                int blue = Color.blue(colour);
                int green = Color.green(colour);

                int index = findMinumum(red,green,blue);
                colourFrequencyList[index]++;
            }

        }
        int max= colourFrequencyList[0];
        int maxIndex = 0;
       for(int i=1 ; i<colourFrequencyList.length; i++){
           if(colourFrequencyList[i]> max){
               max = colourFrequencyList[i];
               maxIndex = i;
           }
       }

        image.setDistance(colourFrequencyList[maxIndex]);
        image.setDominantC(colourNames[maxIndex]);
    }

    public int  findMinumum(int red, int green, int blue){
        List<Integer> distances = new ArrayList<>();

        for(MainColour color : colors){
            int dist = Math.abs(color.R - red) +  Math.abs( color.G - green) + Math.abs( color.B - blue );
            distances.add(dist);
        }
        return  distances.indexOf(Collections.min(distances)); //minumum
    }
}
