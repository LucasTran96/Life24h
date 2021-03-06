package com.family.life24h.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.family.life24h.Adapter.aRclvSelectCountries;
import com.family.life24h.Models.objApplication.objAreaCode;
import com.family.life24h.R;
import com.family.life24h.Utils.AreaCode;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Views.UISettings.UIEditProfile;

import java.util.ArrayList;

/*
 *  Date created: 02/03/2020
 *  Last updated: 02/03/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UISelectCountries extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private final Context context = this;

    private RecyclerView rclvCountries;
    private aRclvSelectCountries adapter;

    private SearchView svCountries;

    private ArrayList<objAreaCode> allAreaCode;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_countries);

        getDataBundle();

        allAreaCode = getAllAreaCode();

        rclvCountries = findViewById(R.id.rclvCountries);
        svCountries = findViewById(R.id.searchName);

        setupRclvCountries(allAreaCode);

        svCountries.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>0){
                    final int size = AreaCode.countryAreaCodes.length;
                    ArrayList<objAreaCode> areaCodes = new ArrayList<>();

                    for(int i = 0; i<size ; i++){
                        if(allAreaCode.get(i).getCountriesName().toLowerCase().contains(newText.toLowerCase()))
                            areaCodes.add(allAreaCode.get(i));
                    }

                    setupRclvCountries(areaCodes);
                }else{
                    setupRclvCountries(allAreaCode);
                }

                return false;
            }
        });
    }

    private void getDataBundle() {
        Bundle bundle = getIntent().getBundleExtra(keyUtils.data);
        if(bundle != null){
            type = bundle.getString(keyUtils.dataType, "");
            if(type.matches(""))
                finish();
        }

    }

    private void setupRclvCountries(ArrayList<objAreaCode> allAreaCode) {
        adapter = new aRclvSelectCountries(context,allAreaCode);
        rclvCountries.setAdapter(adapter);
        rclvCountries.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        adapter.setOnClickListener(new aRclvSelectCountries.actionClick() {
            @Override
            public void onClick(int position) {
                if(type.matches(UISetupProfile.TAG))
                    UISetupProfile.areaCode = adapter.getObjectAreaCode(position);
                else
                    UIEditProfile.areaCode = adapter.getObjectAreaCode(position);

                finish();
            }
        });

    }

    private ArrayList<objAreaCode> getAllAreaCode(){
        ArrayList<objAreaCode> areaCodes = new ArrayList<>();
        final int size = AreaCode.countryAreaCodes.length;

        for(int i = 0; i<size ; i++){
            areaCodes.add(new objAreaCode("+" + AreaCode.countryAreaCodes[i], AreaCode.countryNames[i]));
        }

        return areaCodes;
    }
}
