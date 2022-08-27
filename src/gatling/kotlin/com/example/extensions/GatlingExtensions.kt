package com.example.extensions

import com.example.modelds.Model
import com.example.scenarios.FailOverScenario
import com.google.gson.Gson
import io.gatling.core.Predef.*
import io.gatling.javaapi.core.ActionBuilder
import io.gatling.javaapi.core.ChainBuilder
import io.gatling.javaapi.core.CheckBuilder
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Session
import io.gatling.javaapi.http.HttpRequestActionBuilder
import io.gatling.javaapi.http.internal.HttpCheckBuilders
import java.time.Duration
import io.gatling.javaapi.core.internal.Expressions.*
import java.lang.reflect.Type
import java.util.function.Function


val Int.ms: Duration get() = Duration.ofMillis(this.toLong())

val Int.sec: Duration get() = Duration.ofSeconds(this.toLong())

/*
Chain Builder extensions
 */

fun ChainBuilder.incrementErrorCounter(): ChainBuilder {
    return this.exec { session ->
        if (session.asScala().isFailed){ // session.asScala().status().name() == "KO"
            println("Error counter is ${FailOverScenario.atomicCounter.incrementAndGet()}")
        }
        session
    }
}

fun ChainBuilder.stopInjectorIfFailed(): ChainBuilder {
    return this.doIf { session ->
        session.asScala().isFailed
    }.then(
        pause(5).stopInjector("Found failed http-request. The Injector was stopped after 5 sec.")
    )
}

val ChainBuilder.stopInjectorIfFailed: ChainBuilder get() = this.stopInjectorIfFailed()

fun call(actionBuilder: ActionBuilder): ChainBuilder =
    exec(actionBuilder).stopInjectorIfFailed //.incrementErrorCounter().exitHereIfFailed()

infix fun ChainBuilder.call(actionBuilder: ActionBuilder): ChainBuilder =
    this.exec(actionBuilder).stopInjectorIfFailed //.incrementErrorCounter().exitHereIfFailed()


fun ChainBuilder.printSession(): ChainBuilder =
    this.exec { session -> println(session.asScala()); session }

fun ChainBuilder.printSessionVar(key: String): ChainBuilder =
    this.exec { session -> println(session.getString(key)); session }

fun ChainBuilder.forEach(seq: String, attributeName: String, f: (ChainBuilder)-> ChainBuilder) =
    this.foreach(seq, attributeName).on(f.invoke(this))

fun ChainBuilder.asLongAs(cond: String, f: (ChainBuilder)-> ChainBuilder) =
    this.asLongAs(cond).on(f.invoke(this))

fun ChainBuilder.asLongAs(cond: (Session) -> Boolean,  f: (ChainBuilder)-> ChainBuilder) =
    this.asLongAs(cond).on(f.invoke(this))

fun ChainBuilder.repeat(times: String, f: (ChainBuilder)-> ChainBuilder) =
    this.repeat(times).on(f.invoke(this))

fun ChainBuilder.repeat(times: Int, f: (ChainBuilder)-> ChainBuilder) =
    this.repeat(times).on(f.invoke(this))

fun ChainBuilder.repeat(times: String, counterName: String, f: (ChainBuilder)-> ChainBuilder) =
    this.repeat(times, counterName).on(f.invoke(this))

fun ChainBuilder.repeat(times: Int, counterName: String, f: (ChainBuilder)-> ChainBuilder) =
    this.repeat(times, counterName).on(f.invoke(this))

/*
Http Request Builder extensions
 */

fun HttpRequestActionBuilder.bodyOf(body: String): HttpRequestActionBuilder
    = this.body(StringBody(body)).asJson()

fun HttpRequestActionBuilder.bodyOf(bodyFunc: (Session)-> String): HttpRequestActionBuilder
    = this.body(StringBody(bodyFunc)).asJson()

fun HttpRequestActionBuilder.bodyOf(templateFunc: Function<Session, String>): HttpRequestActionBuilder
        = this.body(StringBody(templateFunc)).asJson()

//fun HttpRequestActionBuilder.bodyOf(template: (Session) -> String): HttpRequestActionBuilder
//        = this.body(StringBody(template)).asJson()

/*
status code extensions
 */
val status: CheckBuilder.Find<Int> get()
    = HttpCheckBuilders.status()

val jsonAllItems: CheckBuilder.JsonOfTypeMultipleFind get()
    = CheckBuilder.JsonPath(jsonPath(toStringExpression("$[*]"), defaultJsonPaths()));

val jsonId: CheckBuilder.JsonOfTypeMultipleFind get()
    = CheckBuilder.JsonPath(jsonPath(toStringExpression("$.id"), defaultJsonPaths()));

val all: CheckBuilder.JsonOfTypeMultipleFind get()
    = CheckBuilder.JsonPath(jsonPath(toStringExpression("$.id"), defaultJsonPaths()));


infix fun CheckBuilder.Find<Int>.shouldBe(status: Int) = this.shouldBe(status)

fun CheckBuilder.Find<Int>.isIn(vararg codes: Int) = this.`in`(codes.toMutableList())

fun CheckBuilder.Find<Int>.shouldBeIn(vararg codes: Int) = this.`in`(codes.toMutableList())

infix fun CheckBuilder.Find<Int>.shouldBe(variable: String) = this.isEL(variable)


/*
jsonPath extensions
 */

// Save var to session as String
infix fun CheckBuilder.JsonOfTypeMultipleFind.saveAs(variable: String)
        = this.saveAs(variable)

// Save var to session as Int
infix fun CheckBuilder.JsonOfTypeMultipleFind.saveAsInt(variable: String)
        = this.ofInt().saveAs(variable)

// Save var to session as List<T>
infix fun CheckBuilder.JsonOfTypeMultipleFind.saveAsList(variable: String)
        = this.ofList().saveAs(variable)
//
//infix fun CheckBuilder.JsonOfTypeMultipleFind.haveCount(count: Int)
//        = this.count().shouldBe(count)

infix fun CheckBuilder.JsonOfTypeMultipleFind.shouldBe(expected: String)
        = this.shouldBe(expected)

infix fun CheckBuilder.JsonOfTypeMultipleFind.shouldBe(expected: (Session) -> String)
        = this.shouldBe(expected)

val CheckBuilder.JsonOfTypeMultipleFind.count get()
    = this.count().transform { count -> count.toInt() }

val CheckBuilder.JsonOfTypeMultipleFind.ofInt get() = this.ofInt()
val CheckBuilder.JsonOfTypeMultipleFind.ofString get() = this.ofString()
val CheckBuilder.JsonOfTypeMultipleFind.ofList get() = this.ofList()


/*
Model Extensions
 */

val Model.toJson: String get() = Gson().toJson(this)

fun String.toObj(objType: Type): Model = Gson().fromJson(this, objType)


