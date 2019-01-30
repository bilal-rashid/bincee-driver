package com.bincee.driver.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.MyApp;
import com.bincee.driver.R;
import com.bincee.driver.api.model.DriverProfileResponse;
import com.bincee.driver.api.model.Event;
import com.bincee.driver.api.model.LoginResponse;
import com.bincee.driver.api.model.MyResponse;
import com.bincee.driver.api.model.ShiftItem;
import com.bincee.driver.base.BFragment;
import com.bincee.driver.databinding.FragmentHomeBinding;
import com.bincee.driver.observer.EndpointObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BFragment {


    private static HomeFragment instance;
    private FragmentHomeBinding binding;
    private VM model;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }


    @Override
    public void onResume() {
        super.onResume();

        setActivityTitle("HOME");


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(VM.class);

        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null && activity.liveData != null) {
            activity.liveData.driverProfile.observe(this, new Observer<DriverProfileResponse>() {
                @Override
                public void onChanged(DriverProfileResponse driverProfileResponse) {

                    model.driverProfile.setValue(driverProfileResponse);

                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setModel(model);
        binding.setLifecycleOwner(getViewLifecycleOwner());


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model.loaderGetShift.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.progressBarShift.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
        model.loaderError.observe(getViewLifecycleOwner(), new Observer<Event<Throwable>>() {
            @Override
            public void onChanged(Event<Throwable> throwableEvent) {
                if (throwableEvent.isHandled) {
                    MyApp.showToast(throwableEvent.getData().getMessage());
                }
            }
        });


        binding.buttonStartRide.setEnabled(false);
        model.shiftList.observe(this, shiftItems -> {
            binding.spinnerCarNumber.setAdapter(new ArrayAdapter<ShiftItem>(getContext()
                    , android.R.layout.simple_list_item_1, shiftItems));
            binding.buttonStartRide.setEnabled(true);
        });

        binding.buttonStartRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShiftItem shiftItem = model
                        .shiftList
                        .getValue()
                        .get(binding.spinnerCarNumber.getSelectedItemPosition());
                ((HomeActivity) getActivity()).liveData.startRide(shiftItem);
            }
        });


    }

    public static class VM extends ViewModel {

        public MutableLiveData<LoginResponse.User> user = new MutableLiveData<>();
        public MutableLiveData<DriverProfileResponse> driverProfile = new MutableLiveData<>();
        public MutableLiveData<List<ShiftItem>> shiftList = new MutableLiveData<>();

        public MutableLiveData<Boolean> loader = new MutableLiveData<>();
        public MutableLiveData<Boolean> loaderGetShift = new MutableLiveData<>();

        private CompositeDisposable compositeDisposable;

        public MutableLiveData<Event<Throwable>> errorListener = new MutableLiveData<>();
        public MutableLiveData<Event<Throwable>> loaderError = new MutableLiveData<>();


        public VM() {
            compositeDisposable = new CompositeDisposable();
            getStudents();
            user.setValue(getUser());
            getShifts();


        }


        private void getShifts() {
            loaderGetShift.setValue(true);
            EndpointObserver<MyResponse<List<ShiftItem>>> endpointObserver = MyApp.endPoints.listShift()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<MyResponse<List<ShiftItem>>>() {
                        @Override
                        public void onComplete() {

                            loaderGetShift.setValue(false);

                        }

                        @Override
                        public void onData(MyResponse<List<ShiftItem>> o) throws Exception {


                            if (o.status == 200) {
                                shiftList.setValue(o.data);
                            } else {
                                throw new Exception(o.status + "");
                            }
                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            e.printStackTrace();
                            loaderError.setValue(new Event<>(e));
                        }
                    });
            compositeDisposable.add(endpointObserver);
        }

        @Override
        protected void onCleared() {
            super.onCleared();
            compositeDisposable.clear();
        }

        private LoginResponse.User getUser() {
            return MyApp.instance.user;
        }

        private void getStudents() {
//            MyApp.endPoints.createRide(MyApp.instance.user.id,);
        }


    }

}
