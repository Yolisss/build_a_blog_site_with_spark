package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;

import java.util.ArrayList;
import java.util.List;

//methods inside handle data storage and retrieval
public class SimpleBlogDao implements BlogDao {
    private List<BlogEntry> blogs;

    public SimpleBlogDao(){
        blogs = new ArrayList<>();
    };



    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        boolean isDuplicate = blogs.stream()
                .anyMatch(blog -> blog.getSlug().equals(blogEntry.getSlug()));
        if (isDuplicate) {
            return false; // or throw an exception
        }
        return blogs.add(blogEntry);
    }


    @Override
    public List<BlogEntry> findAllEntries(){
      return blogs;
    };

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        // Use stream to find the entry or throw an exception if not found
        return blogs.stream()
                .filter(blog -> blog.getSlug().equals(slug))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
