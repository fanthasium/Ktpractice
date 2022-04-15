package com.example.kotlin.data

class UserData {
    var mkDate: String? = null
    var email: String? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(UserData.class)  데이터 옮겨짐 !! 중요 !!
    }

    constructor(email: String?, mkDate: String?) {
        this.mkDate = mkDate
        this.email = email
    }

    override fun toString(): String {
        return "User{" +
                "MkDate='" + mkDate + '\'' +
                ", email='" + email + '\'' +
                '}'
    }
}


