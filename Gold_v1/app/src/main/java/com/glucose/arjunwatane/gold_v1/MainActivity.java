package com.glucose.arjunwatane.gold_v1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    String uname = null;
    int ID ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String username = getIntent().getStringExtra("Username");
        uname = username;
        ID = getIntent().getIntExtra("ID", -1);


        //Arjun start: Show initial fragment when app opens (MainFragment)
        MainFragment fragment = MainFragment.newInstance(uname,ID);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        //Arjun end

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.navName);
        name.setText(username);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.tab_glucose)
        {
            // Handle glucose prediction here

            float spectrum_data[] = /*{1,2,3,4,5,6,7,8,9};*/ {20786, 22982, 24737, 27783, 30402, 33121, 36458, 39278, 42190, 45837, 48959, 52177, 54922, 59106, 64337, 68522, 73877, 78666, 85670, 91633, 99217, 109639, 119289, 130717, 144562, 161307, 181470, 203736, 232894, 261293, 299851, 346545, 396061, 463378, 540485, 646063, 769623, 927617, 1115225, 1349835, 1633047, 2003267, 2435535, 2962147, 3606504, 4338174, 5226389, 6237366, 7413775, 8781768, 10274220, 11899290, 13663080, 15494320, 17344540, 19245310, 21095460, 23102830, 25074310, 27123380, 29116540, 31178250, 33072780, 34918910, 36622110, 38173670, 39638360, 40908320, 41889410, 42599870, 43064510, 43252110, 43094280, 42587660, 41788000, 40602260, 39191400, 37564950, 35794230, 33890060, 32022770, 30012520, 28119700, 26200440, 24393030, 22638970, 21204180, 19823770, 18673050, 17561250, 16641260, 15837300, 15136580, 14574920, 14074970, 13667460, 13297970, 13003200, 12782890, 12595830, 12434630, 12375390, 12343640, 12332340, 12355470, 12422420, 12503620, 12592930, 12708270, 12860350, 13012530, 13172520, 13372430, 13534480, 13707780, 13854840, 14000010, 14077030, 14169200, 14205770, 14178130, 14127140, 14046740, 13867660, 13699200, 13438130, 13178830, 12890260, 12557840, 12213020, 11812130, 11402290, 10980060, 10573740, 10170910, 9717760, 9338133, 8941632, 8590854, 8216653, 7868422, 7558775, 7226572, 6905526, 6579508, 6267933, 6001092, 5699383, 5446116, 5193727, 4954932, 4705318, 4491984, 4285898, 4068493, 3847887, 3685878, 3481897, 3324028, 3161071, 3017044, 2870704, 2755319, 2632548, 2524484, 2428663, 2340027, 2232400, 2145814, 2062947, 1987875, 1918994, 1856451, 1790958, 1758838, 1701694, 1662837, 1618954, 1581214, 1534799, 1505923, 1473308, 1451811, 1411900, 1369480, 1339429, 1318009, 1288135, 1268096, 1235680, 1215672, 1186706, 1166303, 1130628, 1112656, 1093751, 1060367, 1042057, 1034654, 1007525, 981908};

            GlucoseFragment fragment = GlucoseFragment.newInstance("test1", "test2", spectrum_data);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        }
        else if (id == R.id.tab_oxygen)
        {
            OxygenFragment fragment = new OxygenFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.tab_skin)
        {
            SkinFragment fragment = new SkinFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.tab_bluetooth)
        {
            BluetoothFragment fragment = new BluetoothFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            //fragment.getGlucoseSpectrum


        }
        else if (id == R.id.tab_log)
        {
            GlucoseLogFragment fragment = GlucoseLogFragment.newInstance(uname,ID);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.tab_main)
        {
            MainFragment fragment = MainFragment.newInstance(uname,ID);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
