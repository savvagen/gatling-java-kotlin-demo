package com.example.data

import io.gatling.javaapi.core.Session
import java.util.function.Function

internal object KotlinTemplates {

    val userTemplate = Function { _: Session ->
        fakeUser()
    }

}