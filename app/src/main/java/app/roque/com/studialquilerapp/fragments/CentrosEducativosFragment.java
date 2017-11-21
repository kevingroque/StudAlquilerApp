package app.roque.com.studialquilerapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import app.roque.com.studialquilerapp.R;
import app.roque.com.studialquilerapp.adapters.CentrosEducativosAdapter;
import app.roque.com.studialquilerapp.models.CentroEducativo;
import app.roque.com.studialquilerapp.services.ApiService;
import app.roque.com.studialquilerapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CentrosEducativosFragment extends Fragment {

    private static final String TAG = CentrosEducativosFragment.class.getSimpleName();
    private RecyclerView centroList;


    public CentrosEducativosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_centros_educativos, container, false);

        centroList = (RecyclerView) view.findViewById(R.id.recyclerviewCentros);
        centroList.setLayoutManager(new LinearLayoutManager(getContext()));
        centroList.setAdapter(new CentrosEducativosAdapter(getActivity()));

        initialize();
        return view;
    }

    private void initialize() {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<CentroEducativo>> call = service.getCentrosEducativos();

        call.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<CentroEducativo> centroEducativos = response.body();
                        Log.d(TAG, "centrosEducativos: " + centroEducativos);

                        CentrosEducativosAdapter adapter = (CentrosEducativosAdapter) centroList.getAdapter();
                        adapter.setCentrosEducativos(centroEducativos);
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
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

}
