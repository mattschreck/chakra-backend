package com.hszg.demo.model;

public class Exercise {

    private String title;
    private String start;        // Datum/Start, z.B. "2023-02-10"
    private Integer weight;
    private Integer repetitions;
    private Integer sets;

    public Exercise() {
    }

    public Exercise(String title, String start, Integer weight, Integer repetitions, Integer sets) {
        this.title = title;
        this.start = start;
        this.weight = weight;
        this.repetitions = repetitions;
        this.sets = sets;
    }

    // Getter / Setter
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }

    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getRepetitions() {
        return repetitions;
    }
    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Integer getSets() {
        return sets;
    }
    public void setSets(Integer sets) {
        this.sets = sets;
    }
}