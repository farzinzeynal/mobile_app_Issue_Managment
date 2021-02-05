package com.example.user.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.user.issue_managment.R;

import com.example.user.models.ProjectModel;
import com.example.user.utils.StaticValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerView_Adapter extends  RecyclerView.Adapter<RecyclerView_Adapter.CostomViewHolder>
{

    AlertDialog.Builder builder;

    private static  final String DELETE_PROJECT = "http://"+StaticValues.IPV4+":8080/api/project/";

    private Context mcontext;
    private List<ProjectModel> myList;

    public RecyclerView_Adapter(Context mcontext, List<ProjectModel> myList)
    {
        this.mcontext = mcontext;
        this.myList = myList;
    }




    @NonNull
    @Override
    public CostomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.costom_project_view, null);
        CostomViewHolder holder = new CostomViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull CostomViewHolder holder, int position)
    {
        final ProjectModel projectModel = myList.get(position);


        holder.textView_id.setText(projectModel.getId());
        holder.textView_name.setText(projectModel.getProjectName());
        holder.textView_code.setText(projectModel.getProjectCode());
        holder.textView_proj_manager.setText(projectModel.getManagerName());


        holder.textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                builder = new AlertDialog.Builder(mcontext);
                builder.setMessage("Layihəni silmək istədiyinizdən əminsiniz ?");
                builder.setCancelable(true);
                builder.setIcon(R.mipmap.diqqet_ico);
                builder.setTitle("Diqqət");

                builder.setPositiveButton(
                        "Bəli", new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                deleteProject(projectModel.getId());
                            }
                        });

                builder.setNegativeButton(
                        "Xeyir",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });



                builder.show();

            }
        });


    }


    @Override
    public int getItemCount()
    {
        return myList.size();
    }



    public void deleteProject(String id)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, DELETE_PROJECT+id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {

                try {

                    Boolean isDeleted = response.getBoolean("isDeleted");

                    if(isDeleted=true)
                    {
                        Toast.makeText(mcontext," Layihe silindi ",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mcontext," xeta :"+e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(mcontext," xeta  :"+error.toString(),Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        requestQueue.add(jsonObjectRequest);

    }


    /********************************************* CostomViewHolder CLASS************************************************************************/

    class CostomViewHolder extends RecyclerView.ViewHolder
    {

        TextView textView_id, textView_name, textView_code, textView_proj_manager, textView_delete;


        public CostomViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textView_id = itemView.findViewById(R.id.textView_proj_id);
            textView_name = itemView.findViewById(R.id.textView_proj_name);
            textView_code = itemView.findViewById(R.id.textView_proj_code);
            textView_proj_manager = itemView.findViewById(R.id.textView_proj_manager);
            textView_delete = itemView.findViewById(R.id.textView_delete);

        }

    }

}