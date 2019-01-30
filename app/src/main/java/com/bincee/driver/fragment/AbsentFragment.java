package com.bincee.driver.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.R;
import com.bincee.driver.api.firestore.Ride;
import com.bincee.driver.api.model.AbsenteStdent;
import com.bincee.driver.api.model.MyResponse;
import com.bincee.driver.api.model.Student;
import com.bincee.driver.base.BFragment;
import com.bincee.driver.databinding.FragmentAbsentBinding;
import com.bincee.driver.databinding.RecyclerRowMarkStudentAbsentBinding;
import com.bincee.driver.helper.ImageBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbsentFragment extends BFragment {


    private FragmentAbsentBinding binding;
    private List<AbsenteStdent> absentStudents = new ArrayList<>();
    private static AbsentFragment absentFragment;

    public AbsentFragment() {
        // Required empty public constructor
    }

    public static AbsentFragment getInstance() {
        if (absentFragment == null) {
            absentFragment = new AbsentFragment();
        }
        return absentFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_absent, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getHomeActivity().textViewTitle.setText("ABSENT");

        getHomeActivity().liveData.ride.observe(getViewLifecycleOwner(), new Observer<Ride>() {
            @Override
            public void onChanged(Ride ride) {

//                absentStudents = getHomeActivity().liveData.getAbsentStudents();
            }
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleView.setAdapter(new RecyclerView.Adapter<VH>() {


            @NonNull
            @Override
            public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new VH(getLayoutInflater().inflate(R.layout.recycler_row_mark_student_absent, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull VH holder, int position) {

                holder.bind(absentStudents.get(holder.getAdapterPosition()));
            }

            @Override
            public int getItemCount() {
                return absentStudents.size();
            }
        });


        HomeActivity.LiveData liveData = getHomeActivity().liveData;
        MutableLiveData<MyResponse<List<AbsenteStdent>>> absentResponse = liveData.absentResponse;

        if (absentResponse.getValue() != null) {

        } else if (liveData.ride.getValue() != null) {
            liveData.getAbsentList(liveData.ride.getValue().shiftId);

        }

        absentResponse.observe(getViewLifecycleOwner(), new Observer<MyResponse<List<AbsenteStdent>>>() {
            @Override
            public void onChanged(MyResponse<List<AbsenteStdent>> myResponse) {

                if (myResponse != null) {
                    absentStudents = myResponse.data;

                    binding.recycleView.getAdapter().notifyDataSetChanged();
                } else {
                    liveData.getAbsentList(liveData.ride.getValue().shiftId);
                }

            }
        });

//        getAbsentList(data.data, shiftItem);


    }

    private HomeActivity getHomeActivity() {
        return (HomeActivity) getActivity();
    }

    private class VH extends RecyclerView.ViewHolder {

        RecyclerRowMarkStudentAbsentBinding binding;

        public VH(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }

        public void bind(AbsenteStdent student) {
            binding.textViewName.setText(student.fullname);
//            binding.textViewAddress.setText(student.);
            binding.textViewAddress.setVisibility(View.GONE);

            ImageBinder.setImageUrl(binding.imageViewProfileImage, student.photo);


        }
    }
}
