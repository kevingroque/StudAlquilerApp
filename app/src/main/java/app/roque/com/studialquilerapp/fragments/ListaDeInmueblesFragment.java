package app.roque.com.studialquilerapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import app.roque.com.studialquilerapp.R;
import app.roque.com.studialquilerapp.activity.MapsInmueblesActivity;
import app.roque.com.studialquilerapp.adapters.InmueblesAdapter;
import app.roque.com.studialquilerapp.models.Inmueble;
import app.roque.com.studialquilerapp.services.ApiService;
import app.roque.com.studialquilerapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaDeInmueblesFragment extends Fragment {

    private static final String TAG = ListaDeInmueblesFragment.class.getSimpleName();
    private RecyclerView inmuebleList;
    private FloatingActionButton fabGoMap;

    public ListaDeInmueblesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_de_inmuebles,container, false);
        inmuebleList = (RecyclerView) view.findViewById(R.id.recyclerview);
        fabGoMap = (FloatingActionButton)view.findViewById(R.id.fabGoMap);

        inmuebleList.setLayoutManager(new LinearLayoutManager(getContext()));
        inmuebleList.setAdapter(new InmueblesAdapter(getActivity()));

        fabGoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapsInmueblesActivity.class);
                startActivity(intent);
            }
        });

        initialize();
        return view;
    }



    private void initialize() {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Inmueble>> call = service.getImuebles();

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Inmueble> inmuebles = response.body();
                        Log.d(TAG, "inmuebles: " + inmuebles);

                        InmueblesAdapter adapter = (InmueblesAdapter) inmuebleList.getAdapter();
                        adapter.setInmuebles(inmuebles);
                        adapter.notifyDataSetChanged();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

}
