package com.abdilahstudio.apiclient.response;

// model
import com.abdilahstudio.apiclient.model.ApiResponse;
import com.abdilahstudio.apiclient.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("insert_user.php")
    Call<ApiResponse> insertUser(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part photo,
            @Part("tglLahir") RequestBody tglLahir
    );
    @GET("get_user.php")
    Call<List<User>> getUsers();

    @Multipart
    @POST("update_user.php")
    Call<ApiResponse> updateUser(
            @Part("id") RequestBody id,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part photo,
            @Part("tglLahir") RequestBody tglLahir
    );

    @Multipart
    @POST("delete_user.php")
    Call<ApiResponse> deleteUser(
            @Part("id") RequestBody id
    );
}
