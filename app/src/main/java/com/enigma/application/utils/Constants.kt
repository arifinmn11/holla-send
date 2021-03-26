package com.enigma.application.utils

import java.util.regex.Pattern

class Constants {
    companion object {
        // URL
        val BASE_URL = "http://10.10.12.251:8080/"
//        val BASE_URL = "http://192.168.43.164:8080/"
//        val BASE_URL = "http://10.10.14.45:8080/"

        // SHARED PREFERENCES TAG
        val TOKEN = "TOKEN"
        val USERNAME = "USERNAME"
        val PASSWORD = "PASSWORD"
        val PREREFERENCES = "PREFERENCES"
        val ACTIVITY_ID = "ACTIVITY_ID"
        val FIRST_NAME = "FIRST_NAME"
        val LAST_NAME = "LAST_NAME"
        val ADDRESS = "ADDRESS"
        val IDENTIFICATION_NUMBER = "IDENTIFICATION_NUMBER"
        val IDENTIFICATION_CATEGORY = "IDENTIFICATION_CATEGORY"

        // VALIDATION REGEX
        val VALID_EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        // BOTTOM MENU TAG
        val MENU_HISTORY = "History"
        val MENU_HOME = "Home"
        val MENU_PROFILE = "Profile"

        // VALIDATION TAG
        val VALIDATION_EMAIL = "VALIDATION_EMAIL"
        val VALIDATION_PASSWORD = "VALIDATION_PASSWORD"
        val VALIDATION_CONFIRM_PASSWORD = "VALIDATION_CONFIRM_PASSWORD"
        val VALIDATION_USERNAME = "VALIDATION_USERNAME"
        val VALIDATION_SUCCESS = "VALIDATION_SUCCESS"
        val VALIDATION_LASTNAME = "VALIDATION_LASTNAME"
        val VALIDATION_FIRSTNAME = "VALIDATION_FIRSTNAME"
        val VALIDATION_IDENTIFICATION = "VALIDATION_IDENTIFICATION"
        val VALIDATION_NO_IDENTIFICATION = "VALIDATION_NO_IDENTIFICATION"
        val VALIDATION_ADDRESS = "VALIDATION_ADDRESS"
    }
}