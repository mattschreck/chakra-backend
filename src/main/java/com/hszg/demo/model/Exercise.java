package com.hszg.demo.model;

public class Exercise {

    private Long id;          // einfache ID

    // Felder, die FullCalendar nutzt
    private String title;     // z.B. "Bizepscurls"
    private String start;     // z.B. "2025-01-31" (nur Datum) oder "2025-01-31T00:00:00"

    // Zusätzliche Felder fürs Training
    private int weight;       // Gewicht in kg
    private int repetitions;  // Anzahl Wiederholungen
    private int sets;         // Anzahl Sätze

    // Leerer Konstruktor
    public Exercise() {}

    // Voll-Konstruktor
    public Exercise(Long id, String title, String start, int weight, int repetitions, int sets) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.weight = weight;
        this.repetitions = repetitions;
        this.sets = sets;
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Falls du im Frontend nur "2025-02-01" sendest, dann interpretiert FullCalendar das als All-Day-Event.
     * Wenn du "2025-02-01T12:00:00" sendest, bekommt man eine genaue Uhrzeit.
     */
    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getRepetitions() {
        return repetitions;
    }
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getSets() {
        return sets;
    }
    public void setSets(int sets) {
        this.sets = sets;
    }
}
