package com.teamtreehouse.blog.model;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//REP THE STRUCTURE OF THE DATA YOU'RE WORKING WITH
public class BlogEntry {

    //add necessary fields title, content, date
    private String title;
    private String date;
    private String content;
    private String slug;
    private String entry;
    private List<Comment> comments;

    public BlogEntry( String title, String content, String date) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.entry = entry;
        this.slug = generateSlug(title);
        comments = new ArrayList<>();
    }

    // Generate a slug based on the title
    private String generateSlug(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        // Normalize the string and replace spaces with hyphens
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\w\\s-]", "") // Remove special characters
                .trim()
                .replaceAll("\\s+", "-") // Replace spaces with hyphens
                .toLowerCase();
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }


    public String getSlug() {
        return slug;
    }


    public String getEntry() {
        return entry;
    }

    //setter methods
    public void setTitle(String title) {
        this.title = title;
    }

    public void setEntry(String entry) {
        this.entry = entry;
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
