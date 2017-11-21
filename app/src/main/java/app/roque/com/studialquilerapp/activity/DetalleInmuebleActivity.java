package app.roque.com.studialquilerapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import app.roque.com.studialquilerapp.R;
import app.roque.com.studialquilerapp.models.Inmueble;
import app.roque.com.studialquilerapp.services.ApiService;
import app.roque.com.studialquilerapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleActivity extends AppCompatActivity {

    private static final String TAG = DetalleInmuebleActivity.class.getSimpleName();

    private Integer id;

    private ImageView fotoImage;
    private TextView tipoText, detallesText,precioText, direccionText, distritoText, departamentoText, dormText, bañosText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_inmueble);

        showToolbar("Detalles",true);
        fotoImage = (ImageView)findViewById(R.id.imageDetalle);
        tipoText = (TextView)findViewById(R.id.tipoInmuebleDetalle);
        detallesText = (TextView)findViewById(R.id.detalleInmueble);
        precioText = (TextView)findViewById(R.id.precioDetalle);
        direccionText = (TextView)findViewById(R.id.direccionDetalle);
        distritoText = (TextView)findViewById(R.id.distritoDetalle);
        departamentoText = (TextView)findViewById(R.id.departamentoDetalle);
        dormText = (TextView)findViewById(R.id.numDormDetalle);
        bañosText = (TextView)findViewById(R.id.numBaniosDetalle);

        id = getIntent().getExtras().getInt("ID");
        Log.e(TAG, "id:" + id);

        initialize();
    }

    private void initialize() {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Inmueble> call = service.showInmueble(id);

        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        Inmueble inmueble = response.body();
                        Log.d(TAG, "inmueble: " + inmueble);

                        String url = ApiService.API_BASE_URL + "images/inmuebles/" + inmueble.getImagen();
                        Picasso.with(DetalleInmuebleActivity.this).load(url).into(fotoImage);

                        tipoText.setText(inmueble.getTipo());
                        detallesText.setText(inmueble.getDescripcion());
                        precioText.setText("Precio: S/. " + inmueble.getPrecio());
                        direccionText.setText(inmueble.getDireccion());
                        distritoText.setText(inmueble.getDistrito());
                        departamentoText.setText(inmueble.getDepartamento());
                        dormText.setText(inmueble.getNum_dormitorios());
                        bañosText.setText(inmueble.getNum_banios());


                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(DetalleInmuebleActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(DetalleInmuebleActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
