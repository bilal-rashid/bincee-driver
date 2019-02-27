package com.bincee.driver.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.MyApp;
import com.bincee.driver.R;
import com.bincee.driver.api.firestore.FireStoreHelper;
import com.bincee.driver.api.firestore.Ride;
import com.bincee.driver.api.model.Student;
import com.bincee.driver.base.BFragment;
import com.bincee.driver.dialog.MyProgressDialog;
import com.bincee.driver.helper.ImageBinder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RouteDesignerFragment extends BFragment {


    private static RouteDesignerFragment routeDesignerFragment;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.buttonStartRide)
    Button buttonStartRide;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    private Adapter adapter;
    private MyProgressDialog progressDialog;

    public RouteDesignerFragment() {
        // Required empty public constructor
    }

    public static RouteDesignerFragment getInstance() {
        if (routeDesignerFragment == null) {

            routeDesignerFragment = new RouteDesignerFragment();
        }

        return routeDesignerFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route_designer, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new Adapter();
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(adapter);
        SimpleItemTouchHelperCallback simpleItemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchHelperCallback);
        touchHelper.attachToRecyclerView(recycleView);

        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {

            Ride value = activity.liveData.ride.getValue();

            if (value != null) {

                if (value.routeCreated) {
                    buttonStartRide.setVisibility(View.GONE);

                } else {
                    buttonStartRide.setVisibility(View.VISIBLE);

                }

                if (value.shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {
                    textViewTitle.setText("Preferred Pickup Route");
                } else {
                    textViewTitle.setText("Preferred Drop off Route");
                }
                activity.setBackButton();
                activity.bottomNavigationView.setEnabled(false);


                HashMap<String, List<Student>> stringListHashMap = new HashMap<>();
                stringListHashMap.put("students", value.students);

                progressDialog = new MyProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.show();

                FireStoreHelper.getRouteDesigner(activity.liveData.ride.getValue().shiftId + "")
                        .get().addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {

                                DocumentSnapshot result = task.getResult();


                                List<Student> students = new Gson().fromJson(new Gson().toJson(result.get("students")), new TypeToken<List<Student>>() {
                                }.getType());

                                if (allStudentsMatched(students, value.students)) {

                                    adapter.setData(students);

                                } else {

                                    adapter.setData(value.students);
                                    showChangedMessage();

                                }

                            } else {
                                MyApp.showToast("No Route Found");
                                adapter.setData(value.students);

                            }
                        } else {

                            new AlertDialog.Builder(getContext())
                                    .setMessage(task.getException().getMessage())
                                    .show();

                        }

                    }
                });

            } else {
                buttonStartRide.setVisibility(View.GONE);

            }

        }

    }

    private void showChangedMessage() {
        MyApp.showToast("Some student changed");
    }

    public static boolean allStudentsMatched(List<Student> studentsFirebase, List<Student> studentsRide) {

        for (Student studentFirebase : studentsFirebase) {

            if (!isInList(studentFirebase, studentsRide)) {

                return false;
            }

        }

        for (Student studentRide : studentsRide) {

            if (!isInList(studentRide, studentsFirebase)) {
                return false;

            }

        }


        if (studentsFirebase.size() != studentsRide.size()) {
            return false;
        }

        return true;

    }

    public static boolean isInList(Student studentFirebase, List<Student> studentsRide) {
        for (Student studentRide : studentsRide) {

            if (studentFirebase.id == studentRide.id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        setActivityTitle("ROUTE DESIGNER");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            HomeActivity activity = (HomeActivity) getActivity();
            if (activity != null) {
                activity.getSupportFragmentManager().popBackStack();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            activity.setThreeLine();
            activity.bottomNavigationView.setEnabled(true);

        }
    }

    @OnClick(R.id.buttonStartRide)
    public void onViewClicked() {

        HomeActivity homeActivity = (HomeActivity) getActivity();
        if (homeActivity != null) {
            Ride ride = homeActivity.liveData.ride.getValue();
            ride.students = adapter.data;

            homeActivity.liveData.ride.setValue(ride);

            HashMap<String, List<Student>> stringListHashMap = new HashMap<>();

            stringListHashMap.put("students", ride.students);

            FireStoreHelper.getRouteDesigner(ride.shiftId + "")
                    .set(stringListHashMap);

            homeActivity.startRideAfterRouteDesigner();
            homeActivity.getSupportFragmentManager().popBackStack();

        }

    }

    private class Adapter extends RecyclerView.Adapter<VH> implements ItemTouchHelperAdapter {


        public List<Student> data = new ArrayList<>();

        public Adapter() {
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.route_designer_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.textViewNumber.setText((holder.getAdapterPosition() + 1) + "");
            holder.textViewName.setText(data.get(position).fullname);
            holder.textViewAddress.setText(data.get(position).address);

            ImageBinder.roundedCornerCenterCorpKid(holder.imageView14, data.get(position).photo);


        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(data, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(data, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;

        }

        @Override
        public void onItemDismiss(int position) {

        }

        public void setData(List<Student> data) {

            this.data = data;
            notifyDataSetChanged();

        }
    }


    public class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewNumber)
        TextView textViewNumber;
        @BindView(R.id.textViewName)
        TextView textViewName;
        @BindView(R.id.textViewAddress)
        TextView textViewAddress;
        @BindView(R.id.imageView14)
        ImageView imageView14;

        public VH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final ItemTouchHelperAdapter mAdapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

    }

    public interface ItemTouchHelperAdapter {

        boolean onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }

}
