package com.example.cineconnect.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Utils {
    companion object {
        const val COMMENT_ID = "comment_id"
        const val ARG_LIST = "argList"
        const val QUERY = "query"
        const val CONTAINER_ID = "container_id"
        const val FIRST_TIME_LAUNCH = "first_time_launch"
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"

        const val MOVIE_ID = "movie_id"
        const val PERSON_ID = "person_id"
        const val GENRE_ID = "genre_id"
        const val GENRE_NAME = "genre_name"
        const val MOVIE_NAME = "movie_name"
        const val REVIEW_ID = "review_id"
        const val TITLE = "title"
        const val TYPE = "type"

        const val LOG_TAG_MAIN = "LOG_TAG_MAIN"

        const val PROFILE_LINK = "https://cineconnect.blob.core.windows.net/profile"
        const val POSTER_LINK = "https://cineconnect.blob.core.windows.net/poster"
        const val BACKDROP_LINK = "https://cineconnect.blob.core.windows.net/backdrop"
        const val USER_PROFILE_LINK = "https://cineconnect.blob.core.windows.net/user-profile"


        private val emailRegex = Regex(
            "^\\s*([a-zA-Z0-9.+_\\-]+)@([a-zA-Z0-9.\\-]+)\\.\\w{2,6}\\s*$"
        )

        fun isValidEmail(email: String): Boolean {
            return emailRegex.matches(email)
        }

        fun convertTime(date: String): String {
            val formattedDate: String

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                val formatter =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val date: Date = formatter.parse(date)!!
                formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
            } else {
                val instant = Instant.parse(date)
                val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.US)
                formattedDate = formatter.format(instant.toEpochMilli())
            }
            return formattedDate
        }

        fun getRelativeTime(timestamp: String): String {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val dateTime = sdf.parse(timestamp)
                val calendar = Calendar.getInstance()
                if (dateTime != null) {
                    calendar.time = dateTime
                }

                val now = Calendar.getInstance()

                val duration = (now.timeInMillis - calendar.timeInMillis) / 1000 / 60

                return when {
                    duration < 60 -> "${duration}m"
                    duration < 1440 -> "${duration / 60}h"
                    duration < 10080 -> "${duration / 1440}h"
                    calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) -> {
                        val formatter = SimpleDateFormat("dd MMMM", Locale.getDefault())
                        formatter.format(calendar.time)
                    }

                    else -> {
                        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                        formatter.format(calendar.time)
                    }
                }
            } else {
                val dateTime =
                    LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                val now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))

                val duration = ChronoUnit.MINUTES.between(dateTime, now)

                return when {
                    duration < 60 -> "${duration}m "
                    duration < 1440 -> "${duration / 60}h"
                    duration < 10080 -> "${duration / 1440}d"
                    dateTime.year == now.year -> dateTime.format(DateTimeFormatter.ofPattern("dd MMMM"))
                    else -> dateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                }
            }
        }


    }

}