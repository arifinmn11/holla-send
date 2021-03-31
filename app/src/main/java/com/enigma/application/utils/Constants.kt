package com.enigma.application.utils

import java.util.regex.Pattern

class Constants {
    companion object {
        // URL
         val BASE_URL = "http://10.10.12.19:8080/"
//        val BASE_URL = "http://10.10.14.79:8080/"
        // val BASE_URL = "http://192.168.43.164:8080/"
        // val BASE_URL = "http://10.10.14.45:8080/"

        // SHARED PREFERENCES TAG
        const val TOKEN = "TOKEN"
        const val USERNAME = "USERNAME"
        const val EMAIL = "EMAIL"
        const val PASSWORD = "PASSWORD"
        const val PREREFERENCES = "PREFERENCES"
        const val ACTIVITY_ID = "ACTIVITY_ID"
        const val FIRST_NAME = "FIRST_NAME"
        const val LAST_NAME = "LAST_NAME"
        const val ADDRESS = "ADDRESS"
        const val IDENTIFICATION_NUMBER = "IDENTIFICATION_NUMBER"
        const val IDENTIFICATION_CATEGORY = "IDENTIFICATION_CATEGORY"
        const val MIN_RADIUS = "MIN_RADIUS"

        // VALIDATION REGEX
        val VALID_EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        // BOTTOM MENU TAG
        const val MENU_HISTORY = "History"
        const val MENU_HOME = "Home"
        const val MENU_PROFILE = "Profile"

        // VALIDATION TAG
        const val VALIDATION_EMAIL = "VALIDATION_EMAIL"
        const val VALIDATION_PASSWORD = "VALIDATION_PASSWORD"
        const val VALIDATION_CONFIRM_PASSWORD = "VALIDATION_CONFIRM_PASSWORD"
        const val VALIDATION_NEW_PASSWORD = "VALIDATION_NEW_PASSWORD"
        const val VALIDATION_USERNAME = "VALIDATION_USERNAME"
        const val VALIDATION_SUCCESS = "VALIDATION_SUCCESS"
        const val VALIDATION_LASTNAME = "VALIDATION_LASTNAME"
        const val VALIDATION_FIRSTNAME = "VALIDATION_FIRSTNAME"
        const val VALIDATION_IDENTIFICATION = "VALIDATION_IDENTIFICATION"
        const val VALIDATION_NO_IDENTIFICATION = "VALIDATION_NO_IDENTIFICATION"
        const val VALIDATION_ADDRESS = "VALIDATION_ADDRESS"
    }
}