package proyecto2016.eafit.autoalert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistreActivity extends Activity implements View.OnClickListener  {

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(this);

    }
    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid username, if the user entered one.
                     if (TextUtils.isEmpty(username)) {
                             //editTextUsername.setError(getString(R.string.error_field_required));
                                editTextUsername.setError("Ingrese un valor");
                                 focusView = editTextUsername;
                                 cancel = true;

                      }
                             // Check for a valid password, if the user entered one.
                     if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                                //editTextPassword.setError(getString(R.string.error_field_required));
                                  editTextPassword.setError("Minimo 5 Caracteres");
                                  focusView = editTextPassword;
                                  cancel = true;
                      }

                        if (TextUtils.isEmpty(password)) {
                            editTextPassword.setError("Ingrese un valor");
                            focusView = editTextPassword;
                            cancel = true;
                        }

            // Check for a valid email address.
                        if (TextUtils.isEmpty(email)) {
                            editTextEmail.setError("Ingrese un valor");
                            focusView = editTextEmail;
                            cancel = true;
                                } else if (!isEmailValid(email)) {
                                    //editTextEmail.setError(getString(R.string.error_invalid_email));
                                    editTextEmail.setError("Ingrese un Email valido");
                                    focusView = editTextEmail;
                                    cancel = true;
                        }

    if (isEmailValid(email) & isPasswordValid(password)) {

                   StringRequest stringRequest = new StringRequest(Request.Method.POST, DataObject.REGISTER_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.contains(DataObject.REGISTRE_SUCCESS) ) {

                                                //Toast.makeText(RegistreActivity.this, response, Toast.LENGTH_LONG).show();
                                                Toast.makeText(RegistreActivity.this, "Usuario Registrado Correctamente", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(RegistreActivity.this, LoginActivity.class);
                                                startActivity(i);
                            //startActivity(intent);
                    } else {
                        //If the server response is not success
                        //Displaying an error message on toast
                        //Toast.makeText(RegistreActivity.this, response, Toast.LENGTH_LONG).show();
                        Toast.makeText(RegistreActivity.this, "Fallo Registro, Usuario o Email ya existen", Toast.LENGTH_LONG).show();
                    }

                }

            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegistreActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put(DataObject.KEY_USERNAME, username);
            params.put(DataObject.KEY_PASSWORD, password);
            params.put(DataObject.KEY_EMAIL, email);
            return params;
        }

    };
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
}

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
    }

    public String getHttpPost(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
