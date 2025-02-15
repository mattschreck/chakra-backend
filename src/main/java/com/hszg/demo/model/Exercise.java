package com.hszg.demo.model;

public class Exercise {
    private String id;          // Neu: z. B. UUID
    private String title;
    private String start;       // Datum als String
    private Integer weight;
    private Integer repetitions;
    private Integer sets;
    private String bodyPart;    // Neu: z. B. "Beine", "Rücken", "Brust"

    public Exercise() {
    }

    // Optionaler Konstruktor
    public Exercise(String id, String title, String start,
                    Integer weight, Integer repetitions, Integer sets) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.weight = weight;
        this.repetitions = repetitions;
        this.sets = sets;
        this.bodyPart = bodyPart;
    }

    // Getter/Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStart() { return start; }
    public void setStart(String start) { this.start = start; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public Integer getRepetitions() { return repetitions; }
    public void setRepetitions(Integer repetitions) { this.repetitions = repetitions; }

    public Integer getSets() { return sets; }
    public void setSets(Integer sets) { this.sets = sets; }

    public String getBodyPart() {
        return bodyPart;
    }
    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }
}
