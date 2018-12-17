package com.findxain.uberdriver.api;

import com.findxain.uberdriver.api.model.CreateRideResponseItem;
import com.findxain.uberdriver.api.model.DriverProfileResponse;
import com.findxain.uberdriver.api.model.LoginResponse;
import com.findxain.uberdriver.api.model.MyResponse;
import com.findxain.uberdriver.api.model.UploadImageResponce;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EndPoints {

    String BaseUrl = "https://bincee-server.herokuapp.com/api/";

    @FormUrlEncoded
    @POST("auth/login")
    Observable<LoginResponse> login(@Field("username") String username, @Field("password") String pass);


    @GET("school/driver/{driverId}")
    Observable<MyResponse<DriverProfileResponse>> getDriverProfile(@Path("driverId") String driverId);

    @Multipart
    @POST("avatar/upload")
    Observable<MyResponse<UploadImageResponce>> uploadImage(@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("School/driver/{driverId}")
    Observable<MyResponse> updateProfile(@Path("driverId") String driverId, @Field("photo") String imageUrl);


    @FormUrlEncoded
    @POST("ride/create")
    Observable<MyResponse<List<CreateRideResponseItem>>> createRide(@Field("driver_id") String driver_id, @Field("shift") String shift);


}
