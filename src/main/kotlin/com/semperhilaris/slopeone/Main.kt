package com.semperhilaris.slopeone

import com.google.gson.Gson
import spark.Spark.*

/**
 * Slope One webservice.
 */
fun main(args: Array<String>) {

    val slopeOneGlobal = SlopeOne()
    val gson = Gson()

    get("/status", { req, res ->
        res.status(200)
        res.type("application/json")
        gson.toJson(slopeOneGlobal.status())
    })

    get("/clear", { req, res ->
        slopeOneGlobal.clear()
        res.status(200)
        res.type("application/json")
        gson.toJson("Data cleared.")
    })

    put("/ratings", { req, res ->
        val updateRequest = gson.fromJson(req.body(), UpdateRequest::class.java)
        slopeOneGlobal.update(updateRequest)
        res.status(201)
        res.type("application/json")
        gson.toJson("Ratings added.")
    })

    post("/predict", { req, res ->
        val predictRequest = gson.fromJson(req.body(), PredictRequest::class.java)
        val predictResponse = slopeOneGlobal.predict(predictRequest)
        res.status(201)
        res.type("application/json")
        gson.toJson(predictResponse)
    })

    post("/predict-with-ratings", { req, res ->
        val slopeOneLocal = SlopeOne()
        val predictWithRatingsRequest = gson.fromJson(req.body(), PredictWithRatingsRequest::class.java)
        slopeOneLocal.update(predictWithRatingsRequest.ratings)
        val predictResponse = slopeOneLocal.predict(predictWithRatingsRequest.predict)
        slopeOneLocal.clear()
        res.status(201)
        res.type("application/json")
        gson.toJson(predictResponse)
    })

}