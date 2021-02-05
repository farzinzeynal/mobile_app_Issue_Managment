package com.example.user.issue_managment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.utils.StaticValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lombok.SneakyThrows;

public class DetailsAnIssue extends AppCompatActivity {

    private static final String GET_ISSUE = "http://"+StaticValues.IPV4+":8080/api/issue/";
    private static final String DELETE_ISSUE = "http://"+StaticValues.IPV4+":8080/api/issue/";

    TextView textView_id,
            textView_date,
            textView_descrip,
            textView_detail,
            textView_manager,
            textView_project;

    Button button_delete_issue;

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_details_an_issue);


        textView_id = findViewById(R.id.textView_issue_id);
        textView_date = findViewById(R.id.textView_date);
        textView_descrip = findViewById(R.id.textView_issue_descrip);
        textView_detail = findViewById(R.id.textView_details);
        textView_manager = findViewById(R.id.textView_manager);
        textView_project = findViewById(R.id.textView_project);

        button_delete_issue =  findViewById(R.id.button_delete_issue);

        final Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            if (bundle.getString("ID")!=null)
            {
                showIssue(bundle.getString("ID"));
            }
        }


        button_delete_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteIssue(bundle.getString("ID"));

            }
        });

    }





    private void showIssue(String id) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_ISSUE+id, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response)
            {
                try {

                    JSONObject jsonObject = new JSONObject(response.toString());

                    JSONObject jsonObject1= response.getJSONObject("assignee");
                    JSONObject jsonObject2= response.getJSONObject("project");

                    textView_id.setText("ID : "+response.getString("id"));
                    textView_date.setText("Verilmə tarixi : "+response.getString("date"));
                    textView_descrip.setText("Tapşırıq : "+response.getString("description"));
                    textView_detail.setText("Ətraflı : "+response.getString("details"));
                    textView_manager.setText("Layihə rəhbəri : "+jsonObject1.getString("sureName"));
                    textView_project.setText("Layihə : "+jsonObject2.getString("projectName"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), " xeta  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + StaticValues.token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);


    }

    private void deleteIssue(String id)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, DELETE_ISSUE+id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {

                try {

                    Boolean isDeleted = response.getBoolean("isDeleted");

                    if(isDeleted=true)
                    {
                        Toast.makeText(getApplicationContext()," Tapsiriq silindi ",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext()," xeta :"+e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(getApplicationContext()," xeta  :"+error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap headers = new HashMap();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","Bearer "+StaticValues.token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }




}
