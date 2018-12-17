package com.findxain.uberdriver.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findxain.uberdriver.MyApp;
import com.findxain.uberdriver.R;
import com.findxain.uberdriver.api.model.DriverProfileResponse;
import com.findxain.uberdriver.api.model.Event;
import com.findxain.uberdriver.api.model.MyResponse;
import com.findxain.uberdriver.helper.StorageUtils;
import com.findxain.uberdriver.base.BFragment;
import com.findxain.uberdriver.databinding.FragmentParentProfileBinding;
import com.findxain.uberdriver.helper.ActivityResultHelper;
import com.findxain.uberdriver.helper.ImageFilePath;
import com.findxain.uberdriver.helper.PermissionHelper;
import com.findxain.uberdriver.observer.EndpointObserver;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentProfileFragment extends BFragment {


    private MyAdapter adapter;
    private VM vm;

    ActivityResultHelper activityResultHelper;
    PermissionHelper permissionHelper;

    FragmentParentProfileBinding binding;

    public ParentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MyAdapter();
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
                                permissionHelper.with(ParentProfileFragment.this).permissionId(52)
                                        .requiredPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                                        .setListner(new PermissionHelper.PermissionCallback() {
                                            @Override
                                            public void onPermissionGranted() {
                                                Intent intent = new Intent();
                                                intent.setType("image/*");
                                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 25);

                                                activityResultHelper = new ActivityResultHelper();
                                                activityResultHelper.with(ParentProfileFragment.this)
                                                        .setRequestCode(25)
                                                        .OnResult(new ActivityResultHelper.Result() {
                                                            @Override
                                                            public void ResultOk(Intent data) {


                                                                String realPath = ImageFilePath.getPath(getContext(), data.getData());

                                                                Log.i(ParentProfileFragment.this.getClass().getSimpleName(), "onActivityResult: file path : " + realPath);
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
                                permissionHelper.with(ParentProfileFragment.this)
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
                                                    activityResultHelper.with(ParentProfileFragment.this)
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
                                                MyApp.showToast("Read External Storage Permission Required");

                                            }
                                        })
                                        .request();

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

            EndpointObserver<MyResponse<DriverProfileResponse>> endpointObserver = MyApp.endPoints.getDriverProfile(MyApp.instance.user.id + "")
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

            EndpointObserver<MyResponse> observer = MyApp.endPoints.uploadImage(body)
                    .flatMap(uploadImageResponceResponse -> {
//                                loader.setValue(true);
                                return MyApp.endPoints.updateProfile(MyApp.instance.user.id + "", "http://" + uploadImageResponceResponse.data.path);
                            }
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<MyResponse>() {
                        @Override
                        public void onComplete() {
                            loader.setValue(false);
                        }

                        @Override
                        public void onData(MyResponse o) {


                            getProfile();
                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            e.printStackTrace();
                            loader.setValue(false);
                            parentErrorListner.setValue(new Event<>(e));


                        }
                    });
            compositeDisposable.add(observer);


        }
    }

    public interface MyListners {
        void onProfileClicked(View view);

    }

}
