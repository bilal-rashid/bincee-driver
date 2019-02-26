package com.bincee.driver.api;

import com.bincee.driver.api.model.AbsentListBody;
import com.bincee.driver.api.model.AbsenteStdent;
import com.bincee.driver.api.model.GetSchoolResponce;
import com.bincee.driver.api.model.SendNotificationBody;
import com.bincee.driver.api.model.SendNotificationResponce;
import com.bincee.driver.api.model.Student;
import com.bincee.driver.api.model.DriverProfileResponse;
import com.bincee.driver.api.model.LoginResponse;
import com.bincee.driver.api.model.MyResponse;
import com.bincee.driver.api.model.ShiftItem;
import com.bincee.driver.api.model.UploadImageResponce;
import com.bincee.driver.api.model.CreateRideBody;
import com.squareup.okhttp.Response;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface EndPoints {

    //    String BaseUrl = "https://bincee-server.herokuapp.com/api/";
    String BaseUrl = "http://access.bincee.com/";
    String FIREBAE_URL = "http://fcm.googleapis.com/fcm/send";
    String AVATAR_UPLOAD = "avatar/upload";

    @FormUrlEncoded
    @POST("auth/login")
    Observable<LoginResponse> login(@Field("username") String username, @Field("password") String pass);


    @GET("school/driver/{driverId}")
    Observable<MyResponse<DriverProfileResponse>> getDriverProfile(@Path("driverId") String driverId);

    @Multipart
    @POST(AVATAR_UPLOAD)
    Observable<MyResponse<UploadImageResponce>> uploadImage(@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("School/driver/{driverId}")
    Observable<MyResponse> updateProfile(@Path("driverId") String driverId, @Field("photo") String imageUrl);

    @FormUrlEncoded
    @POST("School/driver/{driverId}")
    Observable<MyResponse> updateProfileText(@Path("driverId") String driverId
            , @Field("phone_no") String phone_no
            , @Field("fullname") String fullname);

    @FormUrlEncoded
    @POST("ride/create")
    Observable<MyResponse<List<Student>>> createRide(@Field("driver_id") int driver_id, @Field("shifts") int shiftId);


    @GET("driver/getShifts/{driverId}")
    Observable<MyResponse<List<ShiftItem>>> listShift(@Path("driverId") String driverId);

    @GET("admin/school/{school_id}")
    Observable<MyResponse<GetSchoolResponce>> getSchool(@Path("school_id") String SchoolId);


    @Headers({
            "Accept: application/json",
            "User-Agent: Your-App-Name",
            "Cache-Control: max-age=640000",
            "Authorization:key=AIzaSyB7NVkDmUfCtfZK7Kde3oP2FfaGGjWVB28"

    })
    @POST
    Observable<SendNotificationResponce> sendNotification(@Url String url, @Body SendNotificationBody sendNotificationBody);


    @FormUrlEncoded
    @POST("ride/absentees")
    Observable<MyResponse<List<AbsenteStdent>>> getAbsentList(@Field("driver_id") int driver_id, @Field("shifts[0]") int shift);


    @FormUrlEncoded
    @POST("users/passwordreset")
    Observable<MyResponse> forgetPassword(@Field("username") String username
            , @Field("selected_option") String selected_option
//            , @Field("email") String email
            , @Field("phone_no") String phone_no
            , @Field("type") String type);


    @GET("school/driver/bus/{driverId}")
    Observable<MyResponse<BusInfo>> listBus(@Path("driverId") String driverId);


}



