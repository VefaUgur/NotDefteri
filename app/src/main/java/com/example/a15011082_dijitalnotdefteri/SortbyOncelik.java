package com.example.a15011082_dijitalnotdefteri;

import java.util.Comparator;

public class SortbyOncelik implements Comparator<Note> {
    @Override
    public int compare(Note o1, Note o2) {
        String date1[],date2[];
        date1 = o1.getTarih().split(" ");
        date2 = o2.getTarih().split(" ");

        int dateResultHour= date1[4].compareTo(date2[4]);
        int dateResultDay =  date1[1].compareTo(date2[1]);

        if (o1.getOncelik().compareTo(o2.getOncelik()) != 0)
        {
            return o1.getOncelik().compareTo(o2.getOncelik());
        }else if(dateResultDay != 0 ){

            return dateResultDay;
        }
        return dateResultHour;
    }
}

