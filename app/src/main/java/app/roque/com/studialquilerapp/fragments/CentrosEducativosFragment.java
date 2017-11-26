package app.roque.com.studialquilerapp.fragments;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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
    private SharedPreferences sharedPreferences;

    public CentrosEducativosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_centros_educativos, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        centroList = (RecyclerView) view.findViewById(R.id.recyclerviewCentros);
        centroList.setLayoutManager(new GridLayoutManager(getContext(),2));
        centroList.setItemAnimator(new DefaultItemAnimator());
        centroList.setAdapter(new CentrosEducativosAdapter(getActivity()));
        initialize();
        return view;
    }

    private int dpToPx(int dp){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,r.getDisplayMetrics()));
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
