package com.bidjidevelops.hd.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by You on 20/04/2017.
 */

public class GsonComment {
    @SerializedName("comentar")
    public List<Commentar> DataComment;
    @SerializedName("result")
    public Integer result;

    @SerializedName("msg")
    public String msg;

    public class Commentar {
        @SerializedName("idpertanyaan")
        public String idpertanyaan;

        @SerializedName("id_comment")
        public String idComment;

        @SerializedName("id_user")
        public String idUser;

        @SerializedName("pertanyaan")
        public String pertanyaan;

        @SerializedName("gbr_pertanyaan")
        public String gbrPertanyaan;

        @SerializedName("status_pertanyaan")
        public String statusPertanyaan;

        @SerializedName("Username")
        public String username;

        @SerializedName("School")
        public String school;

        @SerializedName("Image")
        public String image;

        @SerializedName("waktu_soal")
        public String waktuSoal;

        @SerializedName("commentar")
        public String commentarnya;
    }
}
