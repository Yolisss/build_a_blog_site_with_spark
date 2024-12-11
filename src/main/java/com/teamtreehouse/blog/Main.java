package com.teamtreehouse.blog;

import com.teamtreehouse.blog.dao.BlogDao;
import com.teamtreehouse.blog.dao.NotFoundException;
import com.teamtreehouse.blog.dao.SimpleBlogDao;
import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
//used to render your hbs template


public class Main {
    private static final String FLASH_MESSAGE_KEY = "flash_message";
    private static final String ADMIN_ROLE = "admin"; // Declare a constant

    public static void main(String[] args){
        //access static files css, js, img
        staticFileLocation("/public");


        BlogDao dao = new SimpleBlogDao();

        before((req, res) ->{
            if(req.cookie("admin") != null){
                //we're going to add an attribute to the req so the
                //rest of the req can use it
                req.attribute("admin", req.cookie("admin"));
            }
        });

        before("/edit/:slug", (req, res) -> {
            // Debugging the cookie to make sure it's being sent correctly
            System.out.println("Admin cookie: " + req.cookie("admin"));

            if(req.attribute("admin") == null){

                req.session().attribute("redirectAfterLogin", req.uri());

                res.redirect("/password");
                halt();
            }
        });

        before("/new", (req, res) -> {
            if (req.cookie("admin") == null) {
                // Save the current URL the user was trying to access
                req.session().attribute("redirectAfterLogin", req.uri());
                res.redirect("/password");  // Redirect to the password page
                halt();  // Stop further processing
            }
        });

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
            if (req.cookie("admin") == null) {  // Check if the admin cookie exists
                res.redirect("/password");  // Redirect to the password page if not logged in
                return null;  // Stop further execution of this route
            }

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

        get("/password", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String flashMessage = getFlashMessage(req);  // Retrieve the flash message from the session
            if (flashMessage != null) {
                model.put("flashMessage", flashMessage);  // Add it to the model if it's present
                req.session().removeAttribute(FLASH_MESSAGE_KEY);  // Remove it after showing it once
            }
            return new ModelAndView(model, "password.hbs");
        }, new HandlebarsTemplateEngine());

        // POST route to handle password submission
        post("/password", (req, res) -> {
            String password = req.queryParams("password");

            if (ADMIN_ROLE.equals(password)) {
                res.cookie("admin", "true");  // Set the admin cookie if the password is correct

                // Retrieve the original page the user tried to access
                String redirectAfterLogin = req.session().attribute("redirectAfterLogin");
                if (redirectAfterLogin != null) {
                    req.session().removeAttribute("redirectAfterLogin");
                    res.redirect(redirectAfterLogin);  // Redirect to the originally requested page
                } else {
                    res.redirect("/new");  // Default to the /new page if no redirect URL is found
                }
            } else {
                setFlashMessage(req, "Invalid password, please try again!");  // Set the flash message for invalid password
                res.redirect("/password");  // Redirect back to the password page
            }
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
    //SETTER
    private static void setFlashMessage(Request req, String message) {
        req.session().attribute(FLASH_MESSAGE_KEY, message);  // Set the flash message in the session
    }

    //GETTER
    private static String getFlashMessage(Request req) {
        if (req.session(false) == null) {
            return null;
        }
        // Return the flash message if it exists
        return req.session().attribute(FLASH_MESSAGE_KEY);
    }
}