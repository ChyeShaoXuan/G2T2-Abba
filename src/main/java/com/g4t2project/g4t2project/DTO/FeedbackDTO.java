package com.g4t2project.g4t2project.DTO;

public class FeedbackDTO {
    private int feedbackId;
    private int rating;
    private String comment;

    public FeedbackDTO(int feedbackId, int rating, String comment) {
        this.feedbackId = feedbackId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}

