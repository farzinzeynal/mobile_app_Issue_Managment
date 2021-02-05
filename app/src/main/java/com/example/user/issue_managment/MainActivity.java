package com.example.user.issue_managment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.models.ProjectModel;
import com.example.user.utils.StaticValues;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {



    List<ProjectModel> list;
    Button button_daxilOl;
    ProgressBar progressBar_login;
    EditText editText_username, editText_password;
    AlertDialog.Builder builder1;

    private static  final String LOGIN_USER = "http://"+StaticValues.IPV4+":8080/api/token/authenticate";
    private static  final String GET_ISSUE = "http://"+StaticValues.IPV4+":8080/api/issue/";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        button_daxilOl = findViewById(R.id.button_daxil_ol);
        editText_username =  findViewById(R.id.editText_username);
        editText_password =  findViewById(R.id.editText_parol);
        progressBar_login = findViewById(R.id.progressBar_login);
        list = new ArrayList<>();


        progressBar_login.setVisibility(View.GONE);

        button_daxilOl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (editText_username.length() == 0) {
                    editText_username.setError("İstifadəçi adı yazılmalıdır");
                    return;}

                if (editText_password.length() == 0) {
                    editText_password.setError("Parol yazılmalıdır");
                    return;}


                 loginUserCostom();

            }
        });




    }


    private void loginUserCostom()
    {

        progressBar_login.setVisibility(View.VISIBLE);

        String username = editText_username.getText().toString();
        String password = editText_password.getText().toString();


        JSONObject jsonObject = new JSONObject();

        try {


            jsonObject.put("username",username);
            jsonObject.put("password",password);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_USER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {


                try {

                    if(response.getString("token")!=null)
                    {
                        StaticValues.token = response.getString("token");
                        StaticValues.username = response.getString("username");
                        StaticValues.surename = response.getString("nameSurename");

                        startActivity( new Intent(MainActivity.this,MainPage.class));
                        progressBar_login.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    builder1 =  new AlertDialog.Builder(MainActivity.this);
                    builder1.setCancelable(true);
                    builder1.setTitle("Diqqət");
                    builder1.setMessage("İstifadəçi adı və ya şifrə yanlışdır");
                    builder1.setIcon(R.mipmap.diqqet_ico);
                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder1.show();
                    progressBar_login.setVisibility(View.GONE);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(getApplicationContext()," Xeta :"+error.toString(),Toast.LENGTH_LONG).show();
                progressBar_login.setVisibility(View.GONE);

                builder1 =  new AlertDialog.Builder(MainActivity.this);
                builder1.setCancelable(true);
                builder1.setTitle("Diqqət");
                builder1.setMessage("İstifadəçi adı və ya şifrə yanlışdır");
                builder1.setIcon(R.mipmap.diqqet_ico);
                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder1.show();
                progressBar_login.setVisibility(View.GONE);
            }


            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "2e96e0a4ff05ba86dc8f778ac49a8dc0");
                return headers;
            }
        }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);


    }



    public void ShowInfo()
    {
        Toast.makeText(getApplicationContext(),"OK : ",Toast.LENGTH_LONG).show();

    }
}
