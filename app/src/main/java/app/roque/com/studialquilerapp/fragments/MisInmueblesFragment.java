package app.roque.com.studialquilerapp.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import app.roque.com.studialquilerapp.activity.CrearInmuebleActivity;
import app.roque.com.studialquilerapp.adapters.InmueblesAdapter;
import app.roque.com.studialquilerapp.models.Inmueble;
import app.roque.com.studialquilerapp.services.ApiService;
import app.roque.com.studialquilerapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisInmueblesFragment extends Fragment {

    private static final String TAG = MisInmueblesFragment.class.getSimpleName();
    private RecyclerView inmuebleList;
    // SharedPreferences
    private SharedPreferences sharedPreferences;
    private FloatingActionButton fabAgregar;


    public MisInmueblesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_inmuebles,container, false);

        inmuebleList = (RecyclerView) view.findViewById(R.id.misInmueblesRecycler);
        fabAgregar = (FloatingActionButton)view.findViewById(R.id.fabAgregar);

        inmuebleList.setLayoutManager(new LinearLayoutManager(getContext()));
        inmuebleList.setAdapter(new InmueblesAdapter(getActivity()));

        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CrearInmuebleActivity.class);
                startActivity(intent);
            }
        });

        initialize();
        return view;
    }



    private void initialize() {

        // init SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String usuario_id = sharedPreferences.getString("usuario_id", null);
        Log.d(TAG, "usuario_id: " + usuario_id);

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Inmueble>> call = service.getUsuariosInmuebles(usuario_id);

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
