package app.roque.com.studialquilerapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import app.roque.com.studialquilerapp.R;
import app.roque.com.studialquilerapp.models.Inmueble;
import app.roque.com.studialquilerapp.services.ApiService;
import app.roque.com.studialquilerapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsInmueblesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_inmuebles);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void iniciar() {
        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Inmueble>> call = service.getImuebles();
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                try {

                    int statusCode = response.code();
                    Log.d("CODE STATUS", "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Inmueble> inmuebles = response.body();
                        Log.d("Inmueble", "inmuebles: " + inmuebles);
                        for (Inmueble denuncia : inmuebles) {
                            float lat = denuncia.getLatitud();
                            float lng = denuncia.getLongitud();

                            LatLng cosmo = new LatLng(lat, lng);
                            mMap.addMarker(new MarkerOptions()
                                    .title(denuncia.getDescripcion())
                                    .snippet(denuncia.getDescripcion())
                                    .position(cosmo)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(cosmo));
                            float zoon = 8;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cosmo, zoon));
                        }


                    } else {
                        Log.e("Servidor", "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e("t", "onThrowable: " + t.toString(), t);
                        Toast.makeText(MapsInmueblesActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.e("OnFallo", "onFailure: " + t.toString());
                Toast.makeText(MapsInmueblesActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        iniciar();
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);

        //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getApplicationContext())));

    }
}
