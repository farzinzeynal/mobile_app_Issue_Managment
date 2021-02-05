package com.example.user.issue_managment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.user.fragments.IssueFragment;
import com.example.user.fragments.ProjectFragment;
import com.example.user.utils.StaticValues;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressLint("ResourceType")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_project)
        {
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProjectFragment()).commit();
        }
        else if (id == R.id.nav_issue)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new IssueFragment()).commit();
        }

        else if (id == R.id.nav_tools)
        {
            builder = new AlertDialog.Builder(MainPage.this);
            builder.setMessage("Alətlər paneli növbəti versiyada açılacaq");
            builder.setCancelable(true);
            builder.setIcon(R.mipmap.about_icon);
            builder.setTitle("Alətlət");
            builder.setPositiveButton(
                    "Ok", new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
            builder.show();

        }
        else if (id == R.id.nav_about)
        {
            builder = new AlertDialog.Builder(MainPage.this);
            builder.setMessage(R.string.about);
            builder.setCancelable(true);
            builder.setIcon(R.mipmap.about_icon);
            builder.setTitle("Məlumat");
            builder.setPositiveButton(
                    "Ok", new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        else if (id == R.id.nav_privacy)
        {
            builder = new AlertDialog.Builder(MainPage.this);
            builder.setMessage("Bu tətbiq üçün hələlik hər hansı isfadə şərti mövcüd deyil ");
            builder.setCancelable(true);
            builder.setIcon(R.mipmap.about_icon);
            builder.setTitle("İstifadə şərtləri");
            builder.setPositiveButton(
                    "Ok", new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}





/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            builder = new AlertDialog.Builder(MainPage.this);
            builder.setMessage("Çıxmaq istədiyinizdən əminsiz mi?");
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
                            finish();
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
        return super.onKeyDown(keyCode, event);

    }*/


