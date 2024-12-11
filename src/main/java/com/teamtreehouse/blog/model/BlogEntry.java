package com.teamtreehouse.blog.model;

import java.io.IOException;
import com.github.slugify.Slugify;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//similar to courseIdea.java

//REP THE STRUCTURE OF THE DATA YOU'RE WORKING WITH
public class BlogEntry {

    //add necessary fields title, content, date
    private String title;
    private String date;
    private String slug;
    private String entry;
    private List<Comment> comments;

    public BlogEntry( String title, String entry, String date) {
        this.title = title;
        try {
            Slugify slugify = new Slugify();
            //storing title in slug field for url
            slug = slugify.slugify(title);
            System.out.println("Generated slug: " + slug); // Log the slug to verify

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.date = date;
        this.entry = entry;
        System.out.println("Entry from BlogPost: " + entry);
        System.out.println(entry);
        comments = new ArrayList<>();
        System.out.println("Grabbing list of comments: " + comments);

    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }



    public String getSlug() {
        return slug;
    }


    public String getEntry() {
        return entry;
    };

    //setter methods
    public void setTitle(String title) {
        this.title = title;
        try {
            Slugify slugify = new Slugify();
            this.slug = slugify.slugify(title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setEntry(String entry) {
        this.entry = entry;
        System.out.println("setEntry called. Updated entry: " + this.entry); // Debug log
    }
    //implement methods to manage comments
//add
    public void addComment(Comment comment){
        comments.add(comment);
    }
    //delete
    public void deleteComment(Comment comment){
        comments.remove(comment);
    };

    //edit
    public void editComment(Comment oldComment, String newContent, LocalDate newDate){
        int index = comments.indexOf(oldComment);

        //if the comment exists, update it
        if(index != -1){
            Comment commentToEdit = comments.get(index);

            //using setters to update content and date
            commentToEdit.setContent(newContent);
            commentToEdit.setDate(newDate);
        }
    };

    //retrieves entire list of comments
    public List<Comment> getComments() {
        return comments;
    }




//    public boolean addComment(Comment comment) {
//        // Store these comments!
//        return false;
//    }
}
