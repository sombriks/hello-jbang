# Packaging the project

Once your project is done, it't time to package it.

JBang offers a couple of ways to do that.

## Export a project

Once you're done with scripting, you can export the script as a full-featured 
traditional maven or gradle project:

```bash
jbang init -t cli Hello.java
jbang export maven Hello.java
```

That creates a `Hello` folder with a maven project inside.

## Export a (fat) jar

If everything is done and all you need is to spit out the packaged project:

```bash
jbang export portable Hello.java
```

This produces a jar and a lib folder, with all dependencies needed to run the 
app.

To avoid the inconvenience of carrying the lib folder around, export a single, 
all-inclusive jar file:

```bash
jbang export fatjar Hello.java
```

### What is inside

In both cases, jar and lib folder and _fatjar_, the following command can 
describe what's actually inside the package:

```bash
jar tvf $(jbang info jar Hello.java)
```

## Install as a local app

JBang also allows you to install a project as a local application.

First make sure that your terminal is properly set up:

```bash
jbang app setup
```

You can also add something like this in your .bashrc:

```bash
# JBang
. <(jbang completion)
export PATH=$PATH:$HOME/.jbang/bin
```

Now we're good to go:

```bash
jbang app install --name hellojbang Hello.java
```

Test it:

```bash
hellojbang
```

Congratulations, you just installed a command line tool.

Check the [docs][docs] for more interesting ways to export and use scripts 
regarding package and distribution.

[docs]: https://www.jbang.dev/documentation/jbang/latest/app-installation.html
