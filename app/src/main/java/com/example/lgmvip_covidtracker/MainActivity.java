package com.example.lgmvip_covidtracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final Hashtable<String, List<Model>> dataDict = new Hashtable<>();
    private final List<Model> LocList = new ArrayList<>();
    private RVAdapter rvAdapter;
    private Spinner stateSelectionSpin;
    private RecyclerView recyclerView;
    private ViewPager2 viewPager2;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.StateListRV);

        viewPager2 = findViewById(R.id.OtherDataVP);


        stateSelectionSpin = findViewById(R.id.StateSelectView);
        stateSelectionSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = (String) parent.getItemAtPosition(pos);
                ShowData(item);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        fetchAllData();

    }

    private void fetchAllData() {
        //fetchWorldData();
        fetchNationalData();
        fetchRegionalData();
    }


    private void fetchNationalData() {
        String url = "https://disease.sh/v3/covid-19/countries/India?strict=true";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String loc = jsonObject.getString("country");
                int active = jsonObject.getInt("active");
                int confirmed = jsonObject.getInt("cases");
                int deceased = jsonObject.getInt("deaths");
                int recovered = jsonObject.getInt("recovered");
                Model natModel = new Model(loc, active, confirmed, deceased, recovered);
                LocList.add(natModel);
                SetupViewPager();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(request);
    }


    /*private void fetchWorldData() {
        String url = "https://disease.sh/v3/covid-19/all";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int active = jsonObject.getInt("active");
                int confirmed = jsonObject.getInt("cases");
                int deceased = jsonObject.getInt("deaths");
                int recovered = jsonObject.getInt("recovered");
                Model wModel = new Model("Worldwide", active, confirmed, deceased, recovered);
                LocList.add(wModel);
                SetupViewPager();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(request);
    }*/

    private void fetchRegionalData() {
        String url = "https://data.covid19india.org/state_district_wise.json";
        //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);

                Iterator<String> iterStates = jsonObject.keys();
                List<CharSequence> StateList = new ArrayList<>();
                iterStates.next();
                while (iterStates.hasNext()) {
                    String StateName = iterStates.next();
                    List<Model> modelList = new ArrayList<>();
                    JSONObject districtJSON = jsonObject.getJSONObject(StateName).getJSONObject("districtData");
                    Iterator<String> iterDist = districtJSON.keys();
                    while (iterDist.hasNext()) {
                        String district = iterDist.next();
                        JSONObject distDataJSON = districtJSON.getJSONObject(district);
                        int active = distDataJSON.getInt("active");
                        int confirmed = distDataJSON.getInt("confirmed");
                        int deceased = distDataJSON.getInt("deceased");
                        int recovered = distDataJSON.getInt("recovered");
                        modelList.add(new Model(district, active, confirmed, deceased, recovered));
                    }
                    dataDict.put(StateName, modelList);
                    StateList.add(StateName);
                }
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, StateList);
                stateSelectionSpin.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, Throwable::printStackTrace);

        requestQueue.add(request);
    }

    void ShowData(String selection) {
        rvAdapter = new RVAdapter(dataDict.get(selection));
        recyclerView.setAdapter(rvAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

    }

    void SetupViewPager() {
        VPAdapter vpAdapter = new VPAdapter(LocList);
        viewPager2.setAdapter(vpAdapter);
        viewPager2.setClipToPadding(false);
    }

}