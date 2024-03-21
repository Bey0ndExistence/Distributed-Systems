package com.sd.laborator.interfaces

interface BlacklistInterface {
    fun isAllowed(location: String): Boolean
    fun updateBlacklist(updatedBlacklist: Set<String>): Unit
}