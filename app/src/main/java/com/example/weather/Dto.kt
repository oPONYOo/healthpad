package com.example.weather

import com.google.gson.annotations.SerializedName

data class Dto (
    @SerializedName("기관명") var name: String,
    @SerializedName("주소") var address: String,
    @SerializedName("사무실 연락처") var phoneNumber: String
        )




