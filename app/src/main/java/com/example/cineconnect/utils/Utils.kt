package com.example.cineconnect.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

class Utils {
    companion object {
        const val FIRST_TIME_LAUNCH = "first_time_launch"
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"

        const val MOVIE_ID = "movie_id"
        const val PERSON_ID = "person_id"
        const val GENRE_ID = "genre_id"
        const val GENRE_NAME = "genre_name"
        const val MOVIE_NAME = "movie_name"
        const val REVIEW_ID = "review_id"


        const val LOG_TAG_MAIN = "LOG_TAG_MAIN"

        const val PROFILE_LINK = "https://cineconnect.blob.core.windows.net/profile"
        const val POSTER_LINK = "https://cineconnect.blob.core.windows.net/poster"
        const val BACKDROP_LINK = "https://cineconnect.blob.core.windows.net/backdrop"

        fun convertTime(date: String): String {
            var formattedDate = ""

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                val formatter =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val date: Date = formatter.parse(date)
                formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
            } else {
                val instant = Instant.parse(date)
                val formatter = SimpleDateFormat("dd MMMM yyyy")
                formattedDate = formatter.format(instant.toEpochMilli())
            }
            return formattedDate
        }

    }

}