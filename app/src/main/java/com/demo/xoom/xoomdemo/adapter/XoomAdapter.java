package com.demo.xoom.xoomdemo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.xoom.xoomdemo.R;
import com.demo.xoom.xoomdemo.network.Country;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Manish on 8/11/2018.
 */

public class XoomAdapter extends RecyclerView.Adapter<XoomAdapter.ViewHolder> {
    public static final String IMAGE_URL = "https://www.countryflags.io/{code}/flat/64.png";
    private List<Country> countryList;
    private String finalURL;
    private SharedPreferences prefs;
    private Set<String> countrySet;

    public XoomAdapter(List<Country> countryList, Context context) {
        this.countryList = countryList;
        countrySet = new HashSet<>();
        prefs = context.getSharedPreferences("XOOMAPP", Context.MODE_PRIVATE);
        countrySet.addAll(prefs.getAll().keySet());
    }

    @NonNull
    @Override
    public XoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.xoom_row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull XoomAdapter.ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.name.setText(country.getCountryName());

        holder.favorite.setImageDrawable(holder.favorite.getContext().
                getResources().getDrawable(countryList.get(position).isFavorite() ? android.R.drawable.star_on : android.R.drawable.star_off));
        finalURL = IMAGE_URL.replace("{code}", country.getCountryCode());
        Picasso.get()
                .load(finalURL)
                .placeholder(R.mipmap.loading_icon)
                .error(R.mipmap.ic_no_preview)
                .into(holder.flag);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView flag;
        private ImageView favorite;
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            flag = itemView.findViewById(R.id.flag);
            favorite = itemView.findViewById(R.id.favorite);
            name = itemView.findViewById(R.id.name);
            favorite.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            boolean isFavorite = !countryList.get(getAdapterPosition()).isFavorite();
            countryList.get(getAdapterPosition()).setFavorite(isFavorite);
            notifyItemChanged(getAdapterPosition());
            toggleDataSave(isFavorite, getAdapterPosition());
        }
    }

    private void toggleDataSave(boolean saveData, int position) {
        Country country = countryList.get(position);
        Gson gson = new Gson();
        if (saveData) {
            String json = gson.toJson(country);
            prefs.edit().putString(country.getCountryCode(), json).apply();
        } else {
            prefs.edit().remove(country.getCountryCode()).apply();
        }
    }

    public synchronized void updateList(List<Country> countryList) {
        //Using copyOnWriteArrayList to avoid ConcurrentModificationException
        List<Country> tempList = new CopyOnWriteArrayList<>(countryList);
        for (Country country : tempList) {
            if (countrySet.contains(country.getCountryCode())) {
                tempList.remove(country);
            }
        }
        if (this.countryList == null || this.countryList.isEmpty()) {
            for (String code : countrySet) {
                Gson gson = new Gson();
                String json = prefs.getString(code, "");
                Country country = gson.fromJson(json, Country.class);
                this.countryList.add(country);
            }

            this.countryList.addAll(tempList);
            notifyItemRangeInserted(0, tempList.size());
        } else {
            int oldListCount = this.countryList.size();
            this.countryList.addAll(tempList);
            notifyItemChanged(oldListCount + 1, this.countryList);
        }
    }
}