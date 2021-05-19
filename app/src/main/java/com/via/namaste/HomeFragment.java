package com.via.namaste;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.via.namaste.adapters.AdapterHome;
import com.via.namaste.databasehelper.DataManager;
import com.via.namaste.models.ModelMainCategory;

import java.util.ArrayList;
import java.util.List;

import static com.via.namaste.Config.SEND_CAT_ID;

public class HomeFragment extends Fragment implements AdapterHome.clickInterfaces {
    public static int countShowAds = 0;
    RecyclerView recMainCategory;
    DataManager dataManager;
    List<ModelMainCategory> mainCategoryList;
    AdapterHome adapterMainCategory;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        dataManager = DataManager.getInstance(getContext());
        mainCategoryList = new ArrayList<>();
        mainCategoryList = dataManager.getAllMainCategory();

        init();
        recMainCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapterMainCategory = new AdapterHome(mainCategoryList, getActivity());
        recMainCategory.setAdapter(adapterMainCategory);
        adapterMainCategory.setListeners(this);
        return view;

    }

    private void init() {
        recMainCategory = view.findViewById(R.id.recMainCategory);
    }


    @Override
    public void onRecItemClick(int i, ModelMainCategory modelMultiListData) {
        countShowAds++;
        Intent intent = new Intent(getContext(), ActivityWorkoutList.class);
        intent.putExtra(SEND_CAT_ID, modelMultiListData.id);
        startActivity(intent);
    }
}
