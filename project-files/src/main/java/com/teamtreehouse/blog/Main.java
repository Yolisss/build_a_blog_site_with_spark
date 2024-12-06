package com.teamtreehouse.blog;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
//used to render your hbs template


public class Main {
    public static void main(String[] args){
        //access static files css, js, img
        staticFileLocation("/public");

        get("/", (req, res) ->{
            //create a model to pass data to the template
           Map<String, Object> model = new HashMap<>();

           model.put("entries", new String[]{
                   "The best day I've ever had",
                   "The absolute worst day I've ever had",
                   "That time at the mall",
                   "Dude, where's my car?"
           });
           //model's info will be passed into index.hbs
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());
    }
}