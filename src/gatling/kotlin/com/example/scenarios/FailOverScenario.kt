package com.example.scenarios

import io.gatling.javaapi.core.ChainBuilder
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Session
import java.util.concurrent.atomic.AtomicInteger

object FailOverScenario {

    var atomicCounter = AtomicInteger(0)

    private fun counterCheck(): ChainBuilder =
        doIf { _: Session ->
            atomicCounter.get() >= 5
        }.then(stopInjector("Found more than 5 http-errors..."))

    fun scn(): ChainBuilder = exec(counterCheck())
        .pause(1)



}