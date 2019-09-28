package com.example.audrey.eventsearch_hw9_muzewu;

import java.util.Comparator;

public class upcEvents {
    public String eventName;
    public String eventArtist;
    public String eventDate;
    public String eventTime;
    public String eventType;
    public String eventUrl;
    public String timeString;


    //constructor
    public upcEvents(String eventName,String eventArtist,String eventTime,String eventType,String eventUrl){
        this.eventName = eventName;
        this.eventArtist = eventArtist;
        this.eventType = eventType;
        this.eventUrl = eventUrl;
        this.timeString= eventTime;
    }



}

//==================================================
class SortbyEventNameASC implements Comparator<upcEvents>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(upcEvents a, upcEvents b)
    {
        return a.eventName.compareTo(b.eventName);
    }
}
class SortbyEventNameDES implements Comparator<upcEvents>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(upcEvents a, upcEvents b)
    {
        return b.eventName.compareTo(a.eventName);
    }
}

//=================================================

class SortbyTimeASC implements Comparator<upcEvents>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(upcEvents a, upcEvents b)
    {
        return a.timeString.compareTo(b.timeString);
    }
}

class SortbyTimeDES implements Comparator<upcEvents>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(upcEvents a, upcEvents b)
    {
        return b.timeString.compareTo(a.timeString);
    }
}

//=================================================

class SortbyArtistASC implements Comparator<upcEvents>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(upcEvents a, upcEvents b)
    {
        return a.eventArtist.compareTo(b.eventArtist);
    }
}

class SortbyArtistDES implements Comparator<upcEvents>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(upcEvents a, upcEvents b)
    {
        return b.eventArtist.compareTo(a.eventArtist);
    }
}

//====================================================

class SortbyTypeASC implements Comparator<upcEvents>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(upcEvents a, upcEvents b)
    {
        return a.eventType.compareTo(b.eventType);
    }
}

class SortbyTypeDES implements Comparator<upcEvents>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(upcEvents a, upcEvents b)
    {
        return b.eventType.compareTo(a.eventType);
    }
}



