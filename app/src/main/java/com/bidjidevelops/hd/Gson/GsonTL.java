package com.bidjidevelops.hd.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by You on 19/04/2017.
 */

public class GsonTL {
    @SerializedName("soal")
    public List<Soal> dataSoal;


    public class Soal {
        @SerializedName("idpertanyaan")
        public String idpertanyaan;

        @SerializedName("id_user")
        public String idUser;

        @SerializedName("pertanyaan")
        public String pertanyaan;

        @SerializedName("gbr_pertanyaan")
        public String gbr_pertanyaan;

        @SerializedName("status_pertanyaan")
        public String statusPertanyaan;

        @SerializedName("Username")
        public String username;
        @SerializedName("waktu_soal")
        public String waktuSoal;

        @SerializedName("School")
        public String school;

        @SerializedName("Image")
        public String image;


    }
}
