package com.app.sealteamdelivery.network;

import com.app.sealteamdelivery.model.AvailableInformation;
import com.app.sealteamdelivery.model.CancelOrderInformation;
import com.app.sealteamdelivery.model.DelivaryCostInformation;
import com.app.sealteamdelivery.model.DeviceTokenInformation;
import com.app.sealteamdelivery.model.HistoryInformation;
import com.app.sealteamdelivery.model.LocationInformation;
import com.app.sealteamdelivery.model.LoginInformation;
import com.app.sealteamdelivery.model.checkversion.CheckVersion;
import com.app.sealteamdelivery.model.currentDriver.Driver;
import com.app.sealteamdelivery.model.currentorder.CurrentOrders;
import com.app.sealteamdelivery.model.historyResult.HistoryResult;
import com.app.sealteamdelivery.model.rateInfo.RateInfo;
import com.app.sealteamdelivery.model.result.Result;
import com.app.sealteamdelivery.model.resultDriverRate.ResultDriverRate;
import com.app.sealteamdelivery.model.user.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InterfaceRetro {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/driver/login?")
    Call<User> login(@Body LoginInformation loginInformation);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/driver/addLocation?")
    Call<Result> addLocation(@Body LocationInformation locationInformation, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/driver/addDeviceToken?")
    Call<Result> addDeviceToken(@Body DeviceTokenInformation deviceTokenInformation, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/driver/changeAvailability")
    Call<Result> changeAvailability(@Body AvailableInformation availableInformation, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/api/order/current/{driver_id}")
    Call<CurrentOrders> currentOrders(@Path("driver_id")int driver_id, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/api/order/changeStatus")
    Call<Result> changeStatus(@Query("driver_id") int driver_id,@Query("status") int status,@Query("order_id") int order_id, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/api/order/cancelorder?")
    Call<Result> cancelOrder(@Body CancelOrderInformation cancelOrderInformation, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/orders/DriverRate/{driver_id}")
    Call<ResultDriverRate> getResultDriverRate(@Path("driver_id")int driver_id, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/order/Response?")
    Call<Result> sentDelivaryCost(@Body DelivaryCostInformation delivaryCostInformation, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/order/history?")
    Call<HistoryResult> getHistory(@Body HistoryInformation historyInformation, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/order/RateResturant?")
    Call<Result> rateResturant(@Body RateInfo rateInfo, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/driver/changePassword?")
    Call<Result> changePassword(@Query("driver_id") int driver_id,@Query("oldpassword") String oldpassword,@Query("password") String password,@Query("confirm-password") String confirm_password, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/api/driver/getData/{driver_id}")
    Call<Driver> getDriverData(@Path("driver_id")int driver_id, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/api/driver/appVersion")
    Call<CheckVersion> checkVersion(@Header("Authorization") String auth);

}
