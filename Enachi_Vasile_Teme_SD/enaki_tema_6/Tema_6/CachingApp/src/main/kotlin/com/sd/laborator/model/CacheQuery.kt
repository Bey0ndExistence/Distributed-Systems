package com.sd.laborator.model

class CacheQuery(private var id: Int, private var timestamp: String, private var query: String, private var result: String) {

    var cacheID: Int
        get() {
            return id
        }
        set(value) {
            id = value
        }
    var cacheTimestamp: String
        get() {
            return timestamp
        }
        set(value) {
            timestamp = value
        }
    var cacheQuery: String
        get() {
            return query
        }
        set(value) {
            query = value
        }
    var cacheResult: String
        get() {
            return result
        }
        set(value) {
            result = value
        }

    override fun toString(): String {
        return "Cache [id=$cacheID, timestamp=$timestamp, query=$query, result=$result]"
    }

}