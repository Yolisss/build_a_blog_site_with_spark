package com.teamtreehouse.blog.model;

import java.time.LocalDate;

//REP THE STRUCTURE OF THE DATA YOU'RE WORKING WITH
public class Comment {
    private String author;
    private String content;
    private LocalDate date;

    public Comment(String author, String content, LocalDate date) {
        this.author = author;
        this.content = content;
        this.date = date;
        System.out.println("Author: " + author);
        System.out.println("Content: " + content);
        System.out.println("Date: " + date);

    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setContent(String newContent) {
        this.content = newContent;
    }

    public void setDate(LocalDate newDate) {
        this.date = newDate;
    }
}