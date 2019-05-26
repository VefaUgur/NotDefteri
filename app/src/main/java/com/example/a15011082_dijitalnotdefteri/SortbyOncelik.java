package com.example.a15011082_dijitalnotdefteri;

import java.util.Comparator;

public class SortbyOncelik implements Comparator<Note> {
    @Override
    public int compare(Note o1, Note o2) {
        return o1.getOncelik().compareTo(o2.getOncelik());
    }
}
