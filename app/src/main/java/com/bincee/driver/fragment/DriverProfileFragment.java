package com.bincee.driver.fragment;


import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bincee.driver.MyApp;
import com.bincee.driver.R;
import com.bincee.driver.activity.SplashActivity;
import com.bincee.driver.api.EndPoints;
import com.bincee.driver.api.model.DriverProfileResponse;
import com.bincee.driver.api.model.Event;
import com.bincee.driver.api.model.LoginResponse;
import com.bincee.driver.api.model.MyResponse;
import com.bincee.driver.api.model.UploadImageResponce;
import com.bincee.driver.helper.MyPref;
import com.bincee.driver.helper.StorageUtils;
import com.bincee.driver.base.BFragment;
import com.bincee.driver.databinding.FragmentParentProfileBinding;
import com.bincee.driver.helper.ActivityResultHelper;
import com.bincee.driver.helper.ImageFilePath;
import com.bincee.driver.helper.PermissionHelper;
import com.bincee.driver.observer.EndpointObserver;

import java.io.File;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverProfileFragment extends BFragment {


    private MyAdapter adapter;
    private VM vm;

    ActivityResultHelper activityResultHelper;
    PermissionHelper permissionHelper;

    FragmentParentProfileBinding binding;

    public DriverProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MyAdapter();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parent_profile, container, false);
        vm = ViewModelProviders.of(this).get(VM.class);
        vm.parentErrorListner.observe(this, throwableEvent -> {
            Throwable data = throwableEvent.getData();
            if (data != null) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Error").setMessage(data.getMessage())
                        .setPositiveButton("OK", (dialog, which) -> {
                        }).show();
            }
        });

        binding.setModal(vm);
        binding.setHandlers(new MyListners() {
            @Override
            public void onProfileClicked(View view) {
                showImageSelection();
            }

            @Override
            public void onEditClicked(View view) {

                if (binding.editTextName.getVisibility() != View.VISIBLE || binding.editTextContact.getVisibility() != View.VISIBLE) {

                    binding.editTextName.setVisibility(View.VISIBLE);
                    binding.editTextContact.setVisibility(View.VISIBLE);
                    binding.buttonEdit.setText("SAVE");
                } else {

                    binding.editTextName.setVisibility(View.GONE);
                    binding.editTextContact.setVisibility(View.GONE);
                    binding.buttonEdit.setText("EDIT");

                    vm.updateProfileText(binding.editTextName.getText().toString(), binding.editTextContact.getText().toString());

                }


            }
        });
        vm.loader.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
            }
        });

        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.profile_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuItemLogout) {

            MyApp.instance.user = null;
            MyPref.logout(getContext());

            Intent intent = new Intent(getActivity(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (activityResultHelper != null) {
            activityResultHelper.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void showImageSelection() {
        new AlertDialog.Builder(getContext())
                .setTitle("Select Image From")
                .setItems(new String[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                permissionHelper = new PermissionHelper();
                                permissionHelper.with(DriverProfileFragment.this).permissionId(52)
                                        .requiredPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                                        .setListner(new PermissionHelper.PermissionCallback() {
                                            @Override
                                            public void onPermissionGranted() {
                                                Intent intent = new Intent();
                                                intent.setType("image/*");
                                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 25);

                                                activityResultHelper = new ActivityResultHelper();
                                                activityResultHelper.with(DriverProfileFragment.this)
                                                        .setRequestCode(25)
                                                        .OnResult(new ActivityResultHelper.Result() {
                                                            @Override
                                                            public void ResultOk(Intent data) {


                                                                String realPath = ImageFilePath.getPath(getContext(), data.getData());

                                                                Log.i(DriverProfileFragment.this.getClass().getSimpleName(), "onActivityResult: file path : " + realPath);
                                                                vm.updateProfilePic(realPath);


                                                            }

                                                            @Override
                                                            public void Failed() {

                                                            }
                                                        })
                                                        .start();

                                            }

                                            @Override
                                            public void onPermissionFailed() {

                                                MyApp.showToast("Read External Storage Permission Required");
                                            }
                                        }).request();

                                break;
                            case 1:
                                permissionHelper = new PermissionHelper();
                                permissionHelper.with(DriverProfileFragment.this)
                                        .permissionId(69)
                                        .requiredPermissions(new String[]{Manifest.permission.CAMERA})
                                        .setListner(new PermissionHelper.PermissionCallback() {
                                            @Override
                                            public void onPermissionGranted() {


                                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                File imageFile = StorageUtils.offlineImageNewFile(getActivity(), UUID.randomUUID().toString());
                                                Uri uri = StorageUtils.fileToUri(getContext(), imageFile);
                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                                    startActivityForResult(takePictureIntent, 66);


                                                    activityResultHelper = new ActivityResultHelper();
                                                    activityResultHelper.with(DriverProfileFragment.this)
                                                            .setRequestCode(66)
                                                            .OnResult(new ActivityResultHelper.Result() {
                                                                @Override
                                                                public void ResultOk(Intent data) {


                                                                    vm.updateProfilePic(imageFile.getAbsolutePath());
                                                                }

                                                                @Override
                                                                public void Failed() {

                                                                }
                                                            }).start();
                                                }
                                            }

                                            @Override
                                            public void onPermissionFailed() {
                                                MyApp.showToast("Camera Permission Required");

                                            }
                                        }).request();

                                break;
                        }
                    }
                })
                .create().show();

    }

    private class MyAdapter extends RecyclerView.Adapter<VH> {
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.registered_kid_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    private class VH extends RecyclerView.ViewHolder {
        public VH(@NonNull View itemView) {
            super(itemView);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setActivityTitle("MY PROFILE");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AndroidNetworking.cancel(DriverProfileFragment.class.getSimpleName());
    }

    public static class VM extends ViewModel {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        public MutableLiveData<String> name = new MutableLiveData<>();
        public MutableLiveData<String> profileUrl = new MutableLiveData<>();
        public MutableLiveData<String> contact = new MutableLiveData<>();
        public MutableLiveData<Boolean> loader = new MutableLiveData<>();

        public MutableLiveData<Event<Throwable>> parentErrorListner = new MutableLiveData<>();


        public VM() {
            getProfile();
        }

        public void getProfile() {
            loader.setValue(true);

            EndpointObserver<MyResponse<DriverProfileResponse>> endpointObserver = MyApp.endPoints.getDriverProfile(MyApp.instance.user.getValue().id + "")
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<MyResponse<DriverProfileResponse>>() {
                        @Override
                        public void onComplete() {
                            loader.setValue(false);

                        }

                        @Override
                        public void onData(MyResponse<DriverProfileResponse> response) throws Exception {
                            loader.setValue(false);

                            if (response.status == 200) {
                                name.setValue(response.data.fullname);
                                profileUrl.setValue(response.data.photo);
                                contact.setValue(response.data.phone_no);

                                LoginResponse.User user = MyApp.instance.user.getValue();

                                user.profilepic = (response.data.photo);
                                user.fullName = (response.data.fullname);

                                MyApp.instance.user.setValue(user);
                                MyPref.SAVE_USER(getApplicationContext(), user);


                            } else {
                                throw new Exception(response.status + "");
                            }
                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            e.printStackTrace();
                            loader.setValue(false);
                            parentErrorListner.setValue(new Event<>(e));


                        }
                    });
            compositeDisposable.add(endpointObserver);
        }

        @Override
        protected void onCleared() {
            super.onCleared();
            compositeDisposable.clear();
        }

        public void updateProfilePic(String realPath) {


            File file = new File(realPath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            loader.setValue(true);

//            EndpointObserver<MyResponse> observer = MyApp.endPoints.uploadImage(body)
//                    .flatMap(uploadImageResponceResponse -> {
////                                loader.setValue(true);
//                                return MyApp.endPoints.updateProfile(MyApp.instance.user.id + "", "" + uploadImageResponceResponse.data.path);
//                            }
//                    )
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeWith(new EndpointObserver<MyResponse>() {
//                        @Override
//                        public void onComplete() {
//                            loader.setValue(false);
//                        }
//
//                        @Override
//                        public void onData(MyResponse o) {
//
//                            getProfile();
//                        }
//
//                        @Override
//                        public void onHandledError(Throwable e) {
//                            e.printStackTrace();
//                            loader.setValue(false);
//                            parentErrorListner.setValue(new Event<>(e));
//
//
//                        }
//                    });
//            compositeDisposable.add(observer);


            AndroidNetworking.upload(EndPoints.BaseUrl + EndPoints.AVATAR_UPLOAD)
                    .addMultipartFile("image", file)
                    .setTag(DriverProfileFragment.class.getSimpleName())
                    .build().getAsObject(UploadImageResponce.class
                    , new ParsedRequestListener<UploadImageResponce>() {
                        @Override
                        public void onResponse(UploadImageResponce uploadImageResponceResponse) {

                            if (uploadImageResponceResponse.status != 200) {
//
                                onError(new ANError(uploadImageResponceResponse.message));
                                return;
                            }
                            EndpointObserver<MyResponse> endpointObserver = MyApp.endPoints
                                    .updateProfile(MyApp.instance.user.getValue().id + "", "" + uploadImageResponceResponse.data.path)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new EndpointObserver<MyResponse>() {
                                        @Override
                                        public void onComplete() {
                                            loader.setValue(false);
                                        }

                                        @Override
                                        public void onData(MyResponse o) throws Exception {

                                            if (o.status == 200) {
                                                getProfile();
                                            } else {
                                                throw new Exception(o.status + "");
                                            }
                                        }

                                        @Override
                                        public void onHandledError(Throwable e) {
                                            e.printStackTrace();
                                            loader.setValue(false);
                                            parentErrorListner.setValue(new Event<Throwable>(e));
                                        }
                                    });
                            compositeDisposable.add(endpointObserver);
                        }

                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                            loader.setValue(false);
                            parentErrorListner.setValue(new Event<Throwable>(anError));
                        }
                    });


        }

        public void updateProfileText(String name, String contact) {
            loader.setValue(true);


            EndpointObserver<MyResponse> updateObserver = MyApp.endPoints.updateProfileText(MyApp.instance.user.getValue().id + "", contact, name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<MyResponse>() {
                        @Override
                        public void onComplete() {
                            loader.setValue(false);
                        }

                        @Override
                        public void onData(MyResponse o) throws Exception {
                            VM.this.name.setValue(name);
                            VM.this.contact.setValue(contact);

                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            e.printStackTrace();
                            MyApp.showToast(e.getMessage());

                        }
                    });
            compositeDisposable.add(updateObserver);
        }
    }

    public interface MyListners {
        void onProfileClicked(View view);

        void onEditClicked(View view);

    }

}
