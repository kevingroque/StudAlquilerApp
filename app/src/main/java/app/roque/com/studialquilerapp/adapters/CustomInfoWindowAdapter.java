package app.roque.com.studialquilerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import app.roque.com.studialquilerapp.R;
import app.roque.com.studialquilerapp.models.Inmueble;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;
    private List<Inmueble> inmuebles;

    public CustomInfoWindowAdapter(LayoutInflater inflater){
        this.inmuebles = new ArrayList<>();
        this.inflater = inflater;
    }

    public void setInmuebles(List<Inmueble> inmuebles){
        this.inmuebles = inmuebles;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.
        View v = inflater.inflate(R.layout.infowindow_layout, null);
        String[] info = m.getTitle().split("&");
        String url = m.getSnippet();

        ((TextView)v.findViewById(R.id.info_window_nombre)).setText("Nombres");
        ((TextView)v.findViewById(R.id.info_window_placas)).setText("Descricion");
        ((TextView)v.findViewById(R.id.info_window_estado)).setText("Precio S/.10");
        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

}