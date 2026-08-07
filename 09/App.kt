///usr/bin/env jbang "$0" "$@" ; exit $?
// let's call it App.kt
//JAVA 25+
//KOTLIN 2.4.10
//COMPILE_OPTIONS -jvm-target=25
//SOURCES *.kt
//DEPS io.javalin:javalin:7.2.2
//DEPS org.slf4j:slf4j-simple:2.0.16
//DEPS com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2
//DEPS org.jetbrains.exposed:exposed-core:1.0.0
//DEPS org.jetbrains.exposed:exposed-jdbc:1.0.0
//DEPS com.h2database:h2:2.2.224

import io.javalin.Javalin

fun main(args: Array<String>) {

    AppCfg.database()
    val svc = TodoSvc()
    val ctl = TodoCtl(svc)
    val app = Javalin.create {
        AppCfg.cors(it)
        AppCfg.api(ctl, it.routes)
    }
    svc.init()
    app.start(7070)
}
