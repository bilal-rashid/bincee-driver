package com.findxain.uberdriver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.findxain.uberdriver.activity.ContectUsActivity;
import com.findxain.uberdriver.activity.ProfileActivity;
import com.findxain.uberdriver.api.model.LoginResponse;
import com.findxain.uberdriver.base.BA;
import com.findxain.uberdriver.databinding.ActivityHomeBinding;
import com.findxain.uberdriver.fragment.AttendanceFragemnt;
import com.findxain.uberdriver.fragment.HomeFragment;
import com.findxain.uberdriver.fragment.MapFragment;
import com.findxain.uberdriver.fragment.MyPowerFragemnt;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BA {

    public static final String MY_POWER = "- My Power";
    public static final String HOME = "- Home";
    public static final String MY_PROFILE = "- My Profile";
    public static final String CONTACT_US = "- Contact Bincee";
    @BindView(R.id.imageViewProfilePic)
    ImageView imageViewProfilePic;
    @BindView(R.id.textViewUsername)
    TextView textViewUsername;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textViewTitle)
    public TextView textViewTitle;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    private List<String> menuItem;

    LiveData liveData;
    ActivityHomeBinding binding;

    public static void start(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        liveData = ViewModelProviders.of(this).get(LiveData.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setVm(liveData);
        liveData.user.observe(this, user -> binding.userLayout.textViewUsername.setText(user.username));


        textViewTitle.setText("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.threel_line);


        menuItem = new ArrayList<String>();

//        menuItem.add(HOME);
        menuItem.add(MY_POWER);
        menuItem.add(MY_PROFILE);
        menuItem.add("- Route Designer");
//        menuItem.add("- Settings");
        menuItem.add("- FAQ");
//        menuItem.add("- About Us");
        menuItem.add(CONTACT_US);

        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setLayoutFrozen(true);
        recycleView.setAdapter(new RecyclerView.Adapter<NavigationVH>() {
            @NonNull
            @Override
            public NavigationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new NavigationVH(LayoutInflater.from(HomeActivity.this).inflate(R.layout.naviation_row, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull NavigationVH holder, int position) {
                holder.bind();

            }

            @Override
            public int getItemCount() {
                return menuItem.size();
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.bottomNavigationMap:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, HomeFragment.getInstance())
                            .commit();
                    break;
                case R.id.bottomNavigationAttendance:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, AttendanceFragemnt.getInstance())
                            .commit();
                    break;
                case R.id.bottomNavigationPowerScreen:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, MyPowerFragemnt.getInstance())
                            .commit();
                    break;
            }
            return true;
        });

        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationMap);

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frameLayout, HomeFragment.getInstance())
//                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.LEFT, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class NavigationVH extends RecyclerView.ViewHolder {
        @BindView(R.id.textView)
        TextView textView;

        public NavigationVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            textView.setText(menuItem.get(getAdapterPosition()));
        }

        @OnClick(R.id.textView)
        public void onMenuItemClicked() {

            drawerLayout.closeDrawer(Gravity.LEFT);
            if (textView.getText().toString().equalsIgnoreCase(MY_POWER)) {


                bottomNavigationView.setSelectedItemId(R.id.bottomNavigationPowerScreen);
            } else if (textView.getText().toString().equalsIgnoreCase(HOME)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, HomeFragment.getInstance())
                        .commit();
            } else if (textView.getText().toString().equalsIgnoreCase(MY_POWER)) {
                ProfileActivity.start(HomeActivity.this);

            } else if (textView.getText().toString().equalsIgnoreCase(CONTACT_US)) {
                ContectUsActivity.start(HomeActivity.this);
            } else if (textView.getText().toString().equalsIgnoreCase(MY_PROFILE)) {
                ProfileActivity.start(HomeActivity.this);
            }


        }
    }


    public static class LiveData extends ViewModel {

        public MutableLiveData<LoginResponse.User> user = new MutableLiveData<>();

        public LiveData() {
            user.setValue(MyApp.instance.user);
        }
    }

}
