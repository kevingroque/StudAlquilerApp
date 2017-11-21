package app.roque.com.studialquilerapp.services;

import java.util.List;

import app.roque.com.studialquilerapp.models.CentroEducativo;
import app.roque.com.studialquilerapp.models.Inmueble;
import app.roque.com.studialquilerapp.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by keving on 20/11/2017.
 */

public interface ApiService {

    String API_BASE_URL = "https://proyecto-alquiler-api-kevinghanz.c9users.io/";

    @GET("api/v1/inmuebles")
    Call<List<Inmueble>> getImuebles();

    @GET("api/v1/centros_educativos")
    Call<List<CentroEducativo>> getCentrosEducativos();

    @GET("api/v1/inmuebles/{id}")
    Call<Inmueble> showInmueble(@Path("id") Integer id);

    @GET("api/v1/centros_educativos/{id}/inmuebles")
    Call<List<Inmueble>> getCentrosEducativosInmuebles(@Path("id") Integer id);

    @GET("api/v1/usuarios/{id}")
    Call<Usuario> showUsuario(@Path("id") Integer id);

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<Usuario> loginUser
            (@Field("username")String username,
             @Field("password") String password);

}
