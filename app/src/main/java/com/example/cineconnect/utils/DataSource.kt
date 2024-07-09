package com.example.cineconnect.utils

import com.example.cineconnect.model.entities.CastList
import com.example.cineconnect.model.entities.Genre
import com.example.cineconnect.model.entities.MovieList

class DataSource {
    fun loadPoster(): List<String> {
        return listOf<String>(
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://cineconnect.blob.core.windows.net/poster/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
        )
    }

    fun loadMovie(): List<MovieList> {
        return listOf<MovieList>(
//
        )
    }

    fun loadCast(): List<CastList> {
        return listOf<CastList>(
//            CastList(30614,false,"male","Acting","Ryan Gosling","Ryan Gosling","/lyUyVARQKhGxaxy0FbPJCQRpiaW.jpg","Colt Seavers",0),
//            CastList(30614,false,"male","Acting","Ryan Gosling","Ryan Gosling","/lyUyVARQKhGxaxy0FbPJCQRpiaW.jpg","Colt Seavers",0),
//            CastList(30614,false,"male","Acting","Ryan Gosling","Ryan Gosling","/lyUyVARQKhGxaxy0FbPJCQRpiaW.jpg","Colt Seavers",0),
//            CastList(30614,false,"male","Acting","Ryan Gosling","Ryan Gosling","/lyUyVARQKhGxaxy0FbPJCQRpiaW.jpg","Colt Seavers",0),
//            CastList(30614,false,"male","Acting","Ryan Gosling","Ryan Gosling","/lyUyVARQKhGxaxy0FbPJCQRpiaW.jpg","Colt Seavers",0),
//            CastList(30614,false,"male","Acting","Ryan Gosling","Ryan Gosling","/lyUyVARQKhGxaxy0FbPJCQRpiaW.jpg","Colt Seavers",0),
//            CastList(30614,false,"male","Acting","Ryan Gosling","Ryan Gosling","/lyUyVARQKhGxaxy0FbPJCQRpiaW.jpg","Colt Seavers",0),
        )
    }

    fun loadGenre(): List<Genre> {
        return listOf<Genre>(
//            Genre(1,"Action"),
//            Genre(2,"Adventure"),
//            Genre(3,"Animation"),
//            Genre(4,"Biography"),
        )
    }

    val json = """
            {
    "id": 937287,
    "adult": false,
    "backdrop_path": "/bxmiZSQhQ581Y3vbEgpTtmEjt67.jpg",
    "budget": 55000000,
    "homepage": "https://www.amazon.com/salp/challengers",
    "original_language": "en",
    "original_title": "Challengers",
    "overview": "Tennis player turned coach Tashi has taken her husband, Art, and transformed him into a world-famous Grand Slam champion. To jolt him out of his recent losing streak, she signs him up for a Challenger event — close to the lowest level of pro tournament — where he finds himself standing across the net from his former best friend and Tashi's former boyfriend.",
    "poster_path": "/H6vke7zGiuLsz4v4RPeReb9rsv.jpg",
    "release_date": "2024-04-18",
    "revenue": 0,
    "runtime": 132,
    "status": "Released",
    "tagline": "Her game. Her rules.",
    "title": "Challengers",
    "genres": [
        {
            "id": 18,
            "name": "Drama"
        },
        {
            "id": 10749,
            "name": "Romance"
        }
    ],
    "casts": [
        {
            "adult": false,
            "gender": "female",
            "id": 505710,
            "known_for_department": "Acting",
            "name": "Zendaya",
            "original_name": "Zendaya",
            "profile_path": "/3WdOloHpjtjL96uVOhFRRCcYSwq.jpg",
            "character": "Tashi Donaldson",
            "order": 0
        },
        {
            "adult": false,
            "gender": "male",
            "id": 1206334,
            "known_for_department": "Acting",
            "name": "Josh O'Connor",
            "original_name": "Josh O'Connor",
            "profile_path": "/fiDjDWCGSZ7xDaN1rKAP4gvRn1a.jpg",
            "character": "Patrick Zweig",
            "order": 1
        },
        {
            "adult": false,
            "gender": "male",
            "id": 1423520,
            "known_for_department": "Acting",
            "name": "Mike Faist",
            "original_name": "Mike Faist",
            "profile_path": "/xRl1Pa8a80L3QUT7LrTtlTZv1l4.jpg",
            "character": "Art Donaldson",
            "order": 2
        },
        {
            "adult": false,
            "gender": "male",
            "id": 3352136,
            "known_for_department": "Acting",
            "name": "Darnell Appling",
            "original_name": "Darnell Appling",
            "profile_path": "/g4unqS1W07uKzoVIaQAEnVyk6W0.jpg",
            "character": "Umpire (New Rochelle Final)",
            "order": 3
        },
        {
            "adult": false,
            "gender": "others",
            "id": 4643864,
            "known_for_department": "Acting",
            "name": "Bryan Doo",
            "original_name": "Bryan Doo",
            "profile_path": "/jQK9DYYqn0qUhAK0JPq4QLNCxWY.jpg",
            "character": "Art's Physiotherapist",
            "order": 4
        },
        {
            "adult": false,
            "gender": "others",
            "id": 4643870,
            "known_for_department": "Acting",
            "name": "Shane T Harris",
            "original_name": "Shane T Harris",
            "profile_path": "/jRioqQxc9GPzPmV48ikmRItrH8N.jpg",
            "character": "Art's Security Guard",
            "order": 5
        },
        {
            "adult": false,
            "gender": "female",
            "id": 20975,
            "known_for_department": "Acting",
            "name": "Nada Despotovich",
            "original_name": "Nada Despotovich",
            "profile_path": "/40Bqh3VoQlzdVmATLzfgSsdk6bX.jpg",
            "character": "Tashi's Mother",
            "order": 6
        },
        {
            "adult": false,
            "gender": "others",
            "id": 4687627,
            "known_for_department": "Acting",
            "name": "Joan Mcshane",
            "original_name": "Joan Mcshane",
            "profile_path": "default.jpg",
            "character": "Line Judge (New Rochelle Final)",
            "order": 7
        },
        {
            "adult": false,
            "gender": "others",
            "id": 1227356,
            "known_for_department": "Acting",
            "name": "Chris Fowler",
            "original_name": "Chris Fowler",
            "profile_path": "/fkomQEOKlSrfSUPgBmrfKtzmVqj.jpg",
            "character": "TV Sports Commentator (Atlanta 2019)",
            "order": 8
        },
        {
            "adult": false,
            "gender": "others",
            "id": 4538305,
            "known_for_department": "Acting",
            "name": "Mary Joe Fernández",
            "original_name": "Mary Joe Fernández",
            "profile_path": "default.jpg",
            "character": "TV Sports Commentator (Atlanta 2019)",
            "order": 9
        }
    ],
    "directors": [
        {
            "adult": false,
            "gender": "male",
            "id": 78160,
            "known_for_department": "Directing",
            "name": "Luca Guadagnino",
            "original_name": "Luca Guadagnino",
            "profile_path": "/lO2GD4s6fRloZLEhsZgBlhJQasE.jpg"
        }
    ],
    "rating": [
        {
            "avr": {
                "rate__avg": 0
            },
            "total": 0,
            "rating": {
                "0.5": 0,
                "1.0": 0,
                "1.5": 0,
                "2.0": 0,
                "2.5": 0,
                "3.0": 0,
                "3.5": 0,
                "4.0": 0,
                "4.5": 0,
                "5.0": 0
            }
        }
    ],
    "review_count": 0,
    "favourite_count": 0
}
        """


}