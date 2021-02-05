package com.example.user.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.adapters.CostomListViewAdapter;
import com.example.user.adapters.SpinnerAdapter;
import com.example.user.adapters.SpinnerAdapter_Projecr;
import com.example.user.issue_managment.DetailsAnIssue;
import com.example.user.issue_managment.R;
import com.example.user.models.IssueModel;
import com.example.user.models.ProjectModelForSpinner;
import com.example.user.models.UserModeForSpinner;
import com.example.user.utils.StaticValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class IssueFragment extends Fragment
{
    public IssueFragment()
    {
        //empty constructor
    }

    ArrayList<UserModeForSpinner> sureNameList;
    ArrayList<ProjectModelForSpinner> projectNameList;
    ArrayList<IssueModel> issueModelArrayListString;
    ProgressBar progressBar_issue;
    ListView listView_issue;
    CostomListViewAdapter costomListViewAdapter;
    Button button_refres, button_add;
    EditText editText_issue_descrip, editText_issue_detail;
    Spinner spinner_project, spinner_manager;
    Button button_saxla,button_imtina;
    SpinnerAdapter_Projecr spinnerAdapter_project;
    SpinnerAdapter spinnerAdapter_manager;
    Long manager_id;
    Long project_id;


    private static  final String GET_All_ISSUE = "http://"+StaticValues.IPV4+":8080/api/issue/getAll";
    private static  final String GET_All_PROJECT = "http://"+StaticValues.IPV4+":8080/api/project/getAll";
    private static  final String GET_All_USERS = "http://"+StaticValues.IPV4+":8080/api/users";
    private static  final String ADD_ISSUE = "http://"+StaticValues.IPV4+":8080/api/issue";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  @Nullable ViewGroup container, Bundle savedInstanceState)
    {

       View view = inflater.inflate(R.layout.fragment_issue, container, false);

       progressBar_issue = view.findViewById(R.id.progressBar_issue);
       listView_issue = view.findViewById(R.id.listView_issue);
       button_add = view.findViewById(R.id.button_add);
       button_refres = view.findViewById(R.id.button_reload);
       editText_issue_descrip = view.findViewById(R.id.editText_issue_descrip);
       editText_issue_detail = view.findViewById(R.id.editText_issue_detail);
       spinner_manager = view.findViewById(R.id.spinner_manager);
       spinner_project = view.findViewById(R.id.spinner_project);


       showIssueList();

        listView_issue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                IssueModel selectedIssue = (IssueModel)adapterView.getItemAtPosition(i);
                moveDetail(selectedIssue.getId());
            }
        });



        button_refres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showIssueList();
            }
        });


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showDialog();
            }
        });


        return view;

    }



    private void showIssueList()
    {


        progressBar_issue.setVisibility(View.VISIBLE);

        issueModelArrayListString = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_All_ISSUE, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {


                for(int i = 0; i < response.length(); i++)
                {

                    try {

                        IssueModel issueModel = new IssueModel();

                        JSONObject jsonObjectMain = response.getJSONObject(i);


                        issueModel.setId(jsonObjectMain.getString("id"));
                        issueModel.setDesription(jsonObjectMain.getString("description"));


                        issueModelArrayListString.add(issueModel);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



                costomListViewAdapter = new CostomListViewAdapter(getActivity(),R.layout.costom_listview,issueModelArrayListString);      // create new costomAdapter
                listView_issue.setAdapter(costomListViewAdapter);



                progressBar_issue.setVisibility(View.GONE);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();
                progressBar_issue.setVisibility(View.GONE);
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


    private void addIssue(Long manager_id, Long project_id)
    {

        String issue_decs = editText_issue_descrip.getText().toString();
        String issue_detail = editText_issue_detail.getText().toString();


        JSONObject jsonObject = new JSONObject();

        try {


            jsonObject.put("description",issue_decs);
            jsonObject.put("details",issue_detail);
            jsonObject.put("manag_id",manager_id);
            jsonObject.put("proj_id",project_id);




        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ADD_ISSUE, jsonObject, new Response.Listener<JSONObject>() {
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


    private void moveDetail(String issueId)
    {
        Intent intent = new Intent(getActivity(),DetailsAnIssue.class);
        intent.putExtra("ID",issueId);
        startActivity(intent);
    }




    // Show dialog and fill both spinner
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

                spinnerAdapter_manager = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, sureNameList);
                spinnerAdapter_manager.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_manager.setAdapter(spinnerAdapter_manager);





            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();
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

        ////////////////  ProjectSpinner  //////////////

        projectNameList =  new ArrayList<>();

        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, GET_All_PROJECT, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {


                for(int i = 0; i < response.length(); i++)
                {

                    try {

                        JSONObject jsonObjectMain = response.getJSONObject(i);


                        ProjectModelForSpinner projectModelForSpinner= new ProjectModelForSpinner();

                        projectModelForSpinner.setId(jsonObjectMain.getLong("id"));
                        projectModelForSpinner.setProjectName(jsonObjectMain.getString("projectName"));


                        projectNameList.add(projectModelForSpinner);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                spinnerAdapter_project = new SpinnerAdapter_Projecr(getActivity(), android.R.layout.simple_spinner_item, projectNameList);
                spinnerAdapter_project.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_project.setAdapter(spinnerAdapter_project);





            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(),"Xeta : "+ error.toString(),Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest2);

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

        View view1 = layoutInflater.inflate(R.layout.dialog_issue_add,null);

        editText_issue_descrip = view1.findViewById(R.id.editText_issue_descrip);
        editText_issue_detail = view1.findViewById(R.id.editText_issue_detail);
        spinner_manager = view1.findViewById(R.id.spinner_manager);
        spinner_project = view1.findViewById(R.id.spinner_project);
        button_saxla = view1.findViewById(R.id.button_saxla_);
        button_imtina = view1.findViewById(R.id.button_imtina_);


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

                UserModeForSpinner user = spinnerAdapter_manager.getItem(i);

                manager_id = user.getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        spinner_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                ProjectModelForSpinner projectModelForSpinner = spinnerAdapter_project.getItem(i);

                project_id = projectModelForSpinner.getId();
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

                if (editText_issue_descrip.length() == 0) {
                    editText_issue_descrip.setError("Boş buraxıla bilməz");
                    return;}

                if (editText_issue_detail.length() == 0) {
                    editText_issue_descrip.setError("Boş buraxıla bilməz");
                    return;}


                addIssue(manager_id,project_id);

                dialog.cancel();
            }
        });

    }



}
