package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;

import java.util.ArrayList;
import java.util.List;

//reference simpleCourseIdeaDAO for help

//methods inside handle data storage and retrieval
public class SimpleBlogDao implements BlogDao {
    private List<BlogEntry> blogs;

    public SimpleBlogDao(){
        blogs = new ArrayList<>();
    };

//add our idea to the list
    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        boolean isDuplicate = blogs.stream()
                .anyMatch(blog -> blog.getSlug().equals(blogEntry.getSlug()));
        if (isDuplicate) {
            return false; // or throw an exception
        }
        return blogs.add(blogEntry);
    }

    //returns a brand new list with the added blogs
    @Override
    public List<BlogEntry> findAllEntries(){

        return new ArrayList<>(blogs);
    };

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        // Use stream to find the entry or throw an exception if not found
        return blogs.stream()
                //look at each blog, if slug = the slug we're looking for
                .filter(blog -> blog.getSlug().equals(slug))
                //we're gonna get it back
                .findFirst()
                //else, throw except err
                .orElseThrow(NotFoundException::new);
    }
}
