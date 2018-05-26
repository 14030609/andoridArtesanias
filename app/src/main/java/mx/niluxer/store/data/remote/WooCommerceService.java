package mx.niluxer.store.data.remote;

import java.util.List;

import mx.niluxer.store.data.model.MetodosPago;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WooCommerceService {
    @GET("MetodosPago?per_page=100")
    Call<List<MetodosPago>> getMetodosPagos();

    @GET("MetodosPago/")
    Call<MetodosPago> getMetodosPago(@Query("id") int id);

    @POST("MetodosPago")
    @FormUrlEncoded
    Call<MetodosPago> savePost(@Field("name") String title,
                           @Field("type") String body,
                           @Field("regular_price") long userId);

    @POST("MetodosPago")
    Call<MetodosPago> saveMetodosPago(@Body MetodosPago MetodosPago);

    @PUT("MetodosPago/{id}")
    Call<MetodosPago> saveEditedMetodosPago(@Path("id") int id, @Body MetodosPago MetodosPago);

    @DELETE("MetodosPago/{id}?force=true")
    Call<MetodosPago> deleteMetodosPago(@Path("id") int id);
}
