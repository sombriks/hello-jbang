
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS com.esotericsoftware:minlog:1.3.1
//DEPS com.google.code.gson:gson:2.11.0

import com.esotericsoftware.minlog.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

record Todo(String description, boolean done){}

void main(String... args) {
    Log.info("This is a tiny log message.");

    var list = new ArrayList();
    list.add(new Todo("Do the dishes",true));
    list.add(new Todo("Walk the dog",false));

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String jsonOutput = gson.toJson(list);
    Log.info(jsonOutput);
}
