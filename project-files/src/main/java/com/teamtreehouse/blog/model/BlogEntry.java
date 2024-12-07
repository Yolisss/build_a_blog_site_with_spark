package com.teamtreehouse.blog.model;

import java.util.ArrayList;
import java.util.List;


//REP THE STRUCTURE OF THE DATA YOU'RE WORKING WITH
public class BlogEntry {

    //add necessary fields title, content, date
    private String title;
    private String content;
    private String date;
    private String author;
    private int id;
    private String slug;
    private List<Comment> comments;

    public BlogEntry(String author, String content, String date, int id, String slug, String title) {
        comments = new ArrayList<>();
        this.author = author;
        this.content = content;
        this.date = date;
        this.id = id;
        this.slug = slug;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
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
    public void editComment(Comment oldComment, String newContent, String newDate){
        int index = comments.indexOf(oldComment);

        //if the comment exists, update it
        if(index != -1){
            Comment commentToEdit = comments.get(index);

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
