package com.semperhilaris.slopeone

import com.google.gson.Gson
import spark.Spark.*


/**
 * Slope One webservice.
 */
fun main(args: Array<String>) {

    val slopeOne = SlopeOne()
    val gson = Gson()

    get("/status", { req, res ->
        res.status(200)
        res.type("application/json")
        gson.toJson(slopeOne.status())
    })

    get("/clear", { req, res ->
        slopeOne.clear()
        res.status(200)
        res.type("application/json")
        gson.toJson("Data cleared.")
    })

    put("/ratings", { req, res ->
        val updateRequest = gson.fromJson(req.body(), UpdateRequest::class.java)
        slopeOne.update(updateRequest)
        res.status(201)
        res.type("application/json")
        gson.toJson("Ratings added.")
    })

    post("/predict", { req, res ->
        val predictRequest = gson.fromJson(req.body(), PredictRequest::class.java)
        val predictResponse = slopeOne.predict(predictRequest)
        res.status(201)
        res.type("application/json")
        gson.toJson(predictResponse)
    })

}