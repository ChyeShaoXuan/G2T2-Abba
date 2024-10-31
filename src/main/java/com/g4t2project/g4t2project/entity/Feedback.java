package com.g4t2project.g4t2project.entity;

import jakarta.persistence.*;

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int feedbackId;

    @OneToOne(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
    private CleaningTask cleaningTask;

    private int rating;
    private String comment;

    protected Feedback() {}

    public Feedback(int rating, String comment, CleaningTask cleaningTask) {
        this.rating = rating;
        this.comment = comment;
        this.cleaningTask = cleaningTask;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CleaningTask getCleaningTask() {
        return cleaningTask;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }
    
    
}
