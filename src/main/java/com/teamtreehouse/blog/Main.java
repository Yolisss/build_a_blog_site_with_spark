package com.teamtreehouse.blog;

import com.teamtreehouse.blog.dao.BlogDao;
import com.teamtreehouse.blog.dao.NotFoundException;
import com.teamtreehouse.blog.dao.SimpleBlogDao;
import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
//used to render your hbs template


public class Main {
    public static void main(String[] args){
        //access static files css, js, img
        staticFileLocation("/public");

        BlogDao dao = new SimpleBlogDao();



        BlogEntry entry1 = new BlogEntry("The best day Ive ever had", "This is the content for the best day blog.", "01-01-2024");
        dao.addEntry(entry1);


        //INDEX PAGE
        //the (req, res) -> {} is known as lambda function
        get("/", (req, res) ->{
            //create a model to pass data to the template
            Map<String, Object> model = new HashMap<>();

            model.put("entries", dao.findAllEntries());
            //model's info will be passed into index.hbs
            //connects model with index.hbs
            return new ModelAndView(model, "index.hbs");
            //renders the template with the provided data
            }, new HandlebarsTemplateEngine());

        //GET DETAIL
        get("/detail/:slug", (req, res) ->{
            //needs title, date, content
            Map<String, Object> model = new HashMap<>();
            //fetches blog entry(contains title, entry,date) from dao
            model.put("detail", dao.findEntryBySlug(req.params("slug")));
            //passed to handlebar template
            return new ModelAndView(model, "detail.hbs");
        }, new HandlebarsTemplateEngine());

        //GRAB NEW BLOG
        get("/new", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "new.hbs");
        }, new HandlebarsTemplateEngine());

        //ADD NEW BLOG
        post("/new", (req, res) -> {
            String title = req.queryParams("title"); // Get title from form
            String entry = req.queryParams("entry");
            String date = req.queryParams("date");// Get content from form
            BlogEntry newEntry = new BlogEntry(title, entry, date); // Create new BlogPost object
            dao.addEntry(newEntry); // Add new post to DAO
            res.redirect("/"); // Redirect to the page with all blog posts
            return null;
        });

        //GRAB EDIT PAGE
        get("/edit/:slug", (req, res) -> {
            String slug = req.params("slug");
            System.out.print(slug);
            // Get the blog entry by slug
            BlogEntry blogEntry = dao.findEntryBySlug(slug);
            // Prepare the model to pass to the template
            Map<String, Object> model = new HashMap<>();
            // Pass the current blog entry to the template
            model.put("edit", blogEntry);

            return new ModelAndView(model, "edit.hbs");
        }, new HandlebarsTemplateEngine());


        //POST BLOG THAT WAS EDITED
        //RUN ROUTE TO SEE CHANGES MADE
        //CONFIRM WHETHER CONTENT IS FOR ENTRY
        post("/edit/:slug", (req, res) ->{
            String slug = req.params("slug"); //retrieve slug from url
            String newTitle = req.queryParams("title"); //get updated title
            String newEntry = req.queryParams("entry");




            BlogEntry blogEntry = dao.findEntryBySlug(slug); // Get the existing blog entry

            blogEntry.setTitle(newTitle);
            blogEntry.setEntry(newEntry);

            res.redirect("/detail/" + blogEntry.getSlug());
            return null; //nothing to return, we just redirected the user
        });

        //POST NEW COMMENT
        post("/detail/:slug", (req, res) -> {
            String slug = req.params("slug");
            String author = req.queryParams("name"); // Get title from form
            String content = req.queryParams("comment");
            LocalDate date = LocalDate.now();

            System.out.println("Slug from post: " + slug);
            System.out.println("Name from post: " + author);
            System.out.println("Content from post: " + content);

            //find blogEntry using slug
            BlogEntry blogEntry = dao.findEntryBySlug(slug);

            Comment newComment = new Comment(author, content, date); // Create new BlogPost object

            blogEntry.addComment(newComment); // Add new post to DAO
            res.redirect("/detail/" + slug);
            return null;
        });


        exception(NotFoundException.class, (exc, req, res) ->{
            res.status(404);
            //renders the template with the provided data
            HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
            String html = engine.render(
                    new ModelAndView(null, "not-found.hbs"));
            res.body(html);
        });

    }
}