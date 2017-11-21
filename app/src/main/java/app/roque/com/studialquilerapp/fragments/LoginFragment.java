package app.roque.com.studialquilerapp.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.roque.com.studialquilerapp.R;
import app.roque.com.studialquilerapp.activity.CrearCuentaActivity;
import app.roque.com.studialquilerapp.activity.NavDrawerActivity;
import app.roque.com.studialquilerapp.models.Usuario;
import app.roque.com.studialquilerapp.services.ApiService;
import app.roque.com.studialquilerapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private Button btnLogin;
    private Button btnGoRegister;
    private EditText inputUsername;
    private EditText inputPassword;
    private SharedPreferences sharedPreferences;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        inputUsername = (EditText) view.findViewById(R.id.username_input);
        inputPassword = (EditText) view.findViewById(R.id.password_input);
        btnLogin = (Button) view.findViewById(R.id.login_button);
        btnGoRegister = (Button) view.findViewById(R.id.register_button);

        // init SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        // username remember
        String username = sharedPreferences.getString("username", null);
        if(username != null){
            inputUsername.setText(username);
            inputPassword.requestFocus();
        }

        // islogged remember
        if(sharedPreferences.getBoolean("islogged", false)){
            // Go to Dashboard
            goDashboard();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!username.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin();

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(view.getContext(),
                            "Campos incompletos!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnGoRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getContext(),
                        CrearCuentaActivity.class);
                startActivity(i);
            }
        });


        return  view;
    }

    private void checkLogin() {

        final String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Debe completar todo los campos", Toast.LENGTH_SHORT).show();
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<Usuario> call = null;
        call = service.loginUser(username, password);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                try {
                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);
                    if (response.isSuccessful()) {
                        Usuario responseMessage = response.body();
                        assert responseMessage !=null;
                        Integer usuario_id = responseMessage.getId();
                        String nombres = responseMessage.getNombres();
                        String correo = responseMessage.getCorreo();
                        String imagen = responseMessage.getImagen();

                        Log.d(TAG, "responseMessage: " + responseMessage);
                        Log.d(TAG, "correo: " + correo);
                        Log.d(TAG, "usuario_id: " + usuario_id);
                        Log.d(TAG, "imagen: " + imagen);
                        // Save to SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        boolean success = editor
                                .putString("usuario_id", String.valueOf(usuario_id))
                                .putString("username", username)
                                .putString("nombres", nombres)
                                .putString("correo", correo)
                                .putString("imagen", imagen)
                                .putBoolean("islogged", true)
                                .commit();
                        // Go to Dashboard


                        goDashboard();
                    } else {
                        //progressDialog.dismiss();
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        Toast.makeText(getContext(), "Correo o contraseña incorrectos!", Toast.LENGTH_SHORT).show();
                        //throw new Exception();
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private  void goDashboard(){
        Intent intent = new Intent(getActivity(), NavDrawerActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
