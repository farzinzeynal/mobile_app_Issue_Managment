package com.example.user.fragments;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.user.adapters.RecyclerView_Adapter;
import com.example.user.adapters.SpinnerAdapter;
import com.example.user.issue_managment.R;
import com.example.user.models.ProjectModel;
import com.example.user.models.UserModeForSpinner;
import com.example.user.utils.StaticValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProjectFragment extends Fragment
{


    //Qlobal variables
    ArrayList<String> StringArrayList;
    ArrayList<ProjectModel> projectModelArrayList;
    ArrayList<UserModeForSpinner> sureNameList;
    EditText editText_proj_name, editText_proj_code;
    Spinner spinner_manager;
    Button button_saxla, button_imtina;
    ListView listView_project;
    ProgressBar progressBar_project;
    RecyclerView recyclerView_project;
    RecyclerView_Adapter recyclerView_adapter;
    Button button_project_add;
    Button button_project_reload;
    SpinnerAdapter spinnerAdapter;
    Long manager_id;


    private static  final String GET_All_PROJECT = "http://"+StaticValues.IPV4+":8080/api/project/getAll";
    private static  final String GET_All_USERS = "http://"+StaticValues.IPV4+":8080/api/users";
    private static  final String ADD_PROJECT = "http://"+StaticValues.IPV4+":8080/api/project";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_project, container, false);


        progressBar_project =view.findViewById(R.id.progressBar_project);
        recyclerView_project = view.findViewById(R.id.recycler_project);
        recyclerView_project.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_project.setHasFixedSize(true);
        progressBar_project.setVisibility(View.GONE);
        button_project_add = view.findViewById(R.id.button_project_add);
        button_project_reload = view.findViewById(R.id.button_project_reload);



        showListCostom();

        

        button_project_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               showDialog();

            }
        });


        button_project_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                showListCostom();

            }
        });





        return view;

    }


    private void fillSpinner()
    {

        sureNameList =  new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_All_USERS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {


                for(int i = 0; i < response.length(); i++)
                {

                    try {

                        JSONObject jsonObjectMain = response.getJSONObject(i);


                        UserModeForSpinner userModeForSpinner= new UserModeForSpinner();

                        userModeForSpinner.setId(jsonObjectMain.getLong("id"));
                        userModeForSpinner.setSureName(jsonObjectMain.getString("sureName"));


                        sureNameList.add(userModeForSpinner);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                spinnerAdapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, sureNameList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_manager.setAdapter(spinnerAdapter);





            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();
                progressBar_project.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);

    }


    private void showDialog()
    {




        final AlertDialog.Builder alert;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            alert = new AlertDialog.Builder(getActivity(),android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert = new AlertDialog.Builder(getActivity());
        }


        LayoutInflater layoutInflater = getLayoutInflater();

        View view1 = layoutInflater.inflate(R.layout.dialog_project_add,null);

        editText_proj_name = view1.findViewById(R.id.editText_proj_name);
        editText_proj_code = view1.findViewById(R.id.editText_proj_code);
        spinner_manager = view1.findViewById(R.id.spinner_manager);
        button_saxla = view1.findViewById(R.id.button_saxla);
        button_imtina = view1.findViewById(R.id.button_imtina);


        alert.setView(view1);

        alert.setCancelable(false);




        final AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();


        fillSpinner();




        spinner_manager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                UserModeForSpinner user = spinnerAdapter.getItem(i);

                manager_id = user.getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        button_imtina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               dialog.cancel();
            }
        });

        button_saxla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (editText_proj_name.length() == 0) {
                    editText_proj_name.setError("Boş buraxıla bilməz");
                    return;}

                if (editText_proj_code.length() == 0) {
                    editText_proj_code.setError("Boş buraxıla bilməz");
                    return;}


                addProject(manager_id);

                dialog.cancel();
            }
        });

    }


    private void addProject(Long manager_id)
    {

        String proj_name = editText_proj_name.getText().toString();
        String proj_code = editText_proj_code.getText().toString();


        JSONObject jsonObject = new JSONObject();

        try {


            jsonObject.put("projectName",proj_name);
            jsonObject.put("projectCode",proj_code);
            jsonObject.put("project_manag_id",manager_id);




        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ADD_PROJECT, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {


                try {

                    if(response.getLong("id")!=0)
                    {
                        Toast.makeText(getActivity()," Əməliyyat Uğurludur ",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity()," xeta "+e.toString(),Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(getActivity()," xeta var :"+error.toString(),Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);



    }


    private void   showListCostom()
    {
        progressBar_project.setVisibility(View.VISIBLE);

        projectModelArrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_All_PROJECT, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {


                for(int i = 0; i < response.length(); i++)
                {

                   try {

                        JSONObject jsonObjectMain = response.getJSONObject(i);
                        JSONObject jsonObject = jsonObjectMain.getJSONObject("manager");

                        ProjectModel projectModel = new ProjectModel();

                        projectModel.setId(jsonObjectMain.getString("id"));
                        projectModel.setProjectName(jsonObjectMain.getString("projectName"));
                        projectModel.setProjectCode(jsonObjectMain.getString("projectCode"));
                        projectModel.setManagerName("Manager : " + jsonObject.getString("sureName"));

                        projectModelArrayList.add(projectModel);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                recyclerView_adapter = new RecyclerView_Adapter(getActivity(),projectModelArrayList);      // create new costomAdapter
                recyclerView_project.setAdapter(recyclerView_adapter);

                progressBar_project.setVisibility(View.GONE);

            }


        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();
                progressBar_project.setVisibility(View.GONE);
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


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);


    }




    // unUsed Methods

    private void showList()
    {

        progressBar_project.setVisibility(View.VISIBLE);

        StringArrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_All_PROJECT, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {


                // Toast.makeText(getActivity(),"Response : "+ response.toString(),Toast.LENGTH_LONG).show();
                for(int i = 0; i < response.length(); i++)
                {

                    try {

                        JSONObject jsonObject = response.getJSONObject(i);

                        StringArrayList.add(jsonObject.getString("projectName"));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                ArrayAdapter listAdapter= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, StringArrayList);
                listView_project.setAdapter(listAdapter);

                progressBar_project.setVisibility(View.GONE);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(getActivity(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();

                progressBar_project.setVisibility(View.GONE);

            }
        }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);




    }







}




