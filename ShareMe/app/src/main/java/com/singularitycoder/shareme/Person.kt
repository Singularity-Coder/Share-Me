package com.singularitycoder.shareme

data class Person(
    var name: String = "",
    var mobileNumber: String = "",
    var dateStarted: Long = 0,
    var imagePath: String = "",
    var rating: Float = 0f,
    var ratingCount: Int = 0,
)
