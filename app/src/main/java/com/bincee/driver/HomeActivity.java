package com.bincee.driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bincee.driver.activity.ContectUsActivity;
import com.bincee.driver.activity.ProfileActivity;
import com.bincee.driver.activity.RouteDesignerActivity;
import com.bincee.driver.activity.SplashActivity;
import com.bincee.driver.api.EndPoints;
import com.bincee.driver.api.firestore.Ride;
import com.bincee.driver.api.model.AbsenteStdent;
import com.bincee.driver.api.model.DriverProfileResponse;
import com.bincee.driver.api.model.GetSchoolResponce;
import com.bincee.driver.api.model.SendNotificationBody;
import com.bincee.driver.api.model.SendNotificationResponce;
import com.bincee.driver.api.model.ShiftItem;
import com.bincee.driver.api.model.Student;
import com.bincee.driver.api.model.LoginResponse;
import com.bincee.driver.api.model.MyResponse;
import com.bincee.driver.api.model.notification.Notification;
import com.bincee.driver.base.BA;
import com.bincee.driver.databinding.ActivityHomeBinding;
import com.bincee.driver.dialog.FinishRideDialog;
import com.bincee.driver.dialog.SelectRouteDialog;
import com.bincee.driver.dialog.SelectRouteDialogBuilder;
import com.bincee.driver.dialog.SendNotificationDialog;
import com.bincee.driver.dialog.SendNotificationToAll;
import com.bincee.driver.fragment.AbsentFragment;
import com.bincee.driver.fragment.AttendanceFragemnt;
import com.bincee.driver.fragment.HomeFragment;
import com.bincee.driver.fragment.MapFragment;
import com.bincee.driver.fragment.RouteDesignerFragment;
import com.bincee.driver.helper.DateHelper;
import com.bincee.driver.helper.ImageBinder;
import com.bincee.driver.helper.LatLngHelper;
import com.bincee.driver.helper.MyPref;
import com.bincee.driver.helper.PermissionHelper;
import com.bincee.driver.observer.EndpointObserver;
import com.bincee.driver.api.model.CreateRideBody;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.RouteLeg;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bincee.driver.api.model.Student.PRESENT;
import static com.bincee.driver.api.model.Student.STATUS_AFTERNOON_INTHEBUS;
import static com.bincee.driver.api.model.notification.Notification.RIDE;
import static com.bincee.driver.api.model.notification.Notification.UPDATE_STATUS;
import static com.bincee.driver.fragment.MapFragment.MAPBOX_TOKEN;

/**
 * The type Home activity.
 */
public class HomeActivity extends BA {

    /**
     * The constant MY_POWER.
     */
    public static final String MY_POWER = "- My Power";
    /**
     * The constant HOME.
     */
    public static final String HOME = "- Home";
    /**
     * The constant MY_PROFILE.
     */
    public static final String MY_PROFILE = "- My Profile";
    /**
     * The constant CONTACT_US.
     */
    public static final String CONTACT_US = "- Contact Bincee";
    public static final String UPDATE_MY_LOCATION = "- Update My location";
    public static final String LOGOUT = "- Logout";

    /**
     * The Image view profile pic.
     */
    @BindView(R.id.imageViewProfilePic)
    ImageView imageViewProfilePic;
    /**
     * The Text view username.
     */
    @BindView(R.id.textViewUsername)
    TextView textViewUsername;
    /**
     * The Recycle view.
     */
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    /**
     * The Navigation view.
     */
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    /**
     * The Toolbar.
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /**
     * The Text view title.
     */
    @BindView(R.id.textViewTitle)
    public TextView textViewTitle;
    /**
     * The Drawer layout.
     */
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    /**
     * The Bottom navigation view.
     */
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    private List<String> menuItem;

    /**
     * The Live data.
     */
    public LiveData liveData;
    /**
     * The Binding.
     */
    ActivityHomeBinding binding;
    private FirebaseFirestore db;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private PermissionHelper permissionHelper;
    private String TAG = HomeActivity.class.getSimpleName();
    /**
     * The User document.
     */
    public DocumentReference rideDocument;
    private boolean fetchingFromFS = false;
    private ProgressDialog progressDialog;
    private CollectionReference history;

    /**
     * The Custom root.
     */
    public boolean customRoot = false;
    /**
     * The Current route.
     */
    private boolean creatingRoute = false;
    private ProgressDialog createRouteDialog;
    private Observer<Location> tempMyLocationObserver;
    private AlertDialog waiting_for_locationDialog;
    /**
     * The Navigation route.
     */
    public DirectionsRoute navigationRoute;
    private int REQUEST_CHECK_SETTINGS = 963;
    private SendNotificationToAll sendNotificationToAll;
    private SendNotificationDialog sendNotificationDialog;
    private TimerTask creatRouteTask;
    Timer timer = new Timer();
    private String ROUTE_DESIGNER = "- Route Designer";


    /**
     * Start.
     *
     * @param context the context
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        Mapbox.getInstance(this, MAPBOX_TOKEN);
        liveData = ViewModelProviders.of(this).get(LiveData.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setVm(liveData);
        liveData.user.observe(this, user -> binding.userLayout.textViewUsername.setText(user.username));
        liveData.driverProfile.observe(this, new Observer<DriverProfileResponse>() {
            @Override
            public void onChanged(DriverProfileResponse driverProfileResponse) {
                if (driverProfileResponse != null) {
                    ImageBinder.setImageUrl(binding.userLayout.imageViewProfilePic, driverProfileResponse.photo);
                    binding.userLayout.textViewUsername.setText(driverProfileResponse.fullname);
                }

            }
        });

        db = FirebaseFirestore.getInstance();

        if (MyApp.instance.user == null) return;

        rideDocument = db.collection("ride").document(MyApp.instance.user.id + "");
        history = db.collection("history");


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait...");
        progressDialog.show();

        fetchingFromFS = true;
        rideDocument.get().addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                fetchingFromFS = false;
                progressDialog.dismiss();

                if (task.isSuccessful()) {

                    DocumentSnapshot result = task.getResult();
                    if (result != null) {
                        Ride ride = result.toObject(Ride.class);
                        if (ride != null) {
                            liveData.ride.setValue(ride);

                            startRide(false, false);

                        }


                    }

                } else {
                    task.getException().printStackTrace();

                }

            }
        });


        textViewTitle.setText("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.threel_line);


        menuItem = new ArrayList<String>();

//        menuItem.add(MY_POWER);
        menuItem.add(MY_PROFILE);
        menuItem.add(ROUTE_DESIGNER);
//        menuItem.add("- FAQ");
        menuItem.add(CONTACT_US);
//        menuItem.add(UPDATE_MY_LOCATION);
        menuItem.add(LOGOUT);

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

        bottomNavigationView.setItemIconTintList(null);


        liveData.students.observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                if (students == null || students.size() == 0) return;
//                MyApp.showToast(students.size() + " Items");

                Ride ride = null;
                if (liveData.ride.getValue() == null) {
                    ride = new Ride();
                }
                ride.rideId = UUID.randomUUID().toString();
                ride.startTime = Timestamp.now();
                ride.rideInProgress = true;
                ride.students = students;
                ride.driverId = MyApp.instance.user.id;
                ride.schoolLatLng = new GeoPoint(liveData.schoolResponce.getValue().lat, liveData.schoolResponce.getValue().lng);
                ShiftItem shift = liveData.selectedShift.getValue();
                ride.shiftId = shift.shift_id;


                liveData.ride.setValue(ride);


                try {
                    if (DateHelper.isMorningShift(shift)) {
                        ride.shift = Ride.SHIFT_MORNING;
                    } else {
                        ride.shift = Ride.SHIFT_AFTERNOON;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                rideDocument.set(ride)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: User Ride Stated Succefully"))
                        .addOnFailureListener(HomeActivity.this, Throwable::printStackTrace);

//
//                SelectRouteDialog selectRouteDialog = new SelectRouteDialogBuilder()
//                        .setContext(HomeActivity.this)
//                        .setListner(i -> {
//
//                            switch (i) {
//                                case 1:
//
//                                    customRoot = false;
//                                    if (liveData.ride.getValue().shift.equalsIgnoreCase(Ride.SHIFT_MORNING)){
//                                        startRide(true, true);
//
//                                    }else {
//                                        startRide(true, false);
//
//                                    }
//
//
//                                    break;
//                                case 2:
//
//                                    customRoot = true;
//                                    break;
//                            }
//
//                        })
//                        .createSelectRouteDialog();
//                selectRouteDialog.show();


                if (liveData.ride.getValue().shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {
                    startRide(true, true);

                } else {
                    startRide(true, false);

                }


            }
        });

        liveData.createRideListener.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                binding.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);

            }
        });


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                List<Location> locations = locationResult.getLocations();
//                for (Location location : locations) {
//                    // Update UI with location data
//                    // ...
//                }

                if (locations.size() > 0 && locations.get(0) != null) {
                    Location myLocation = locations.get(0);
                    HomeActivity.this.liveData.myLocaton.setValue(myLocation);

                    MapFragment.getInstance().setMyLocation(liveData.myLocaton.getValue());

                    Ride ride = liveData.ride.getValue();
                    if (ride != null && ride.rideInProgress) {

                        ride.latLng = new GeoPoint(myLocation.getLatitude(), myLocation.getLongitude());
                        liveData.ride.setValue(ride);

                        if (ride.students != null) {

                            if (ride.shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {

                                for (Student student : ride.students) {

                                    if (student.status == Student.STATUS_MORNING_BUS_IS_COMMING) {

                                        Location studentLocation = new Location("studentLocation");
                                        studentLocation.setLatitude(student.lat);
                                        studentLocation.setLongitude(student.lng);

                                        if (myLocation.distanceTo(studentLocation) < 200) {
                                            //send notification to student
                                            Log.d(TAG, "At Student Location" + student.fullname);
                                            student.status = Student.STATUS_MORNING_ATYOURLOCATION;
                                            liveData.sentNotificationToStudent(student, "Bus is here", "Bus has arrived to pickup " + student.fullname + " and will leave in 5 minutes");

                                        }
                                    }
                                }

                                if (liveData.isGoingSchool()) {
                                    Ride value = liveData.ride.getValue();
                                    GeoPoint schoolLatLng = value.schoolLatLng;

                                    Location school = LatLngHelper.toLocation(schoolLatLng);
                                    if (myLocation.distanceTo(school) < 200 && !value.rechtoSchoolNotificationSent) {

                                        for (Student student : ride.students) {
                                            student.status = Student.STATUS_MORNING_REACHED;
                                        }


                                        if (sendNotificationToAll == null || !sendNotificationToAll.isShowing()) {
                                            sendNotificationToAll = new SendNotificationToAll(HomeActivity.this);
                                            sendNotificationToAll.setStudents(ride.students);
                                            sendNotificationToAll.setListner(new SendNotificationToAll.Listner() {
                                                @Override
                                                public void yes() {

                                                    liveData.sendNotificationToAll("Reached", "Bus has Reached the school");
                                                    Ride ride = liveData.ride.getValue();
                                                    ride.rechtoSchoolNotificationSent = true;
                                                    liveData.ride.setValue(ride);


                                                }

                                                @Override
                                                public void cancel() {
                                                    liveData.sendNotificationToAll("Reached", "Bus has Reached the school");
                                                    Ride ride = liveData.ride.getValue();
                                                    ride.rechtoSchoolNotificationSent = true;
                                                    liveData.ride.setValue(ride);


                                                }
                                            });
                                            sendNotificationToAll.show();

                                        }

                                    }


                                }


                            } else {

                                for (Student student : ride.students) {

                                    if (student.status == STATUS_AFTERNOON_INTHEBUS) {

                                        Location studentLocation = new Location("studentLocation");
                                        studentLocation.setLatitude(student.lat);
                                        studentLocation.setLongitude(student.lng);


                                        if (myLocation.distanceTo(studentLocation) < 500) {

                                            student.status = Student.STATUS_AFTERNOON_ALMOSTTHERE;
                                            liveData.sentNotificationToStudent(student, "Almost there", student.fullname + "  will reach home in " + Math.round(student.duration) + " minutes");

                                        }
                                    } else if (student.status == Student.STATUS_AFTERNOON_ALMOSTTHERE) {
                                        Location studentLocation = new Location("studentLocation");
                                        studentLocation.setLatitude(student.lat);
                                        studentLocation.setLongitude(student.lng);

                                        if (myLocation.distanceTo(studentLocation) < 200) {
                                            //send notification to student
                                            student.status = Student.STATUS_AFTERNOON_ATYOURDOORSTEP;

                                            sendNotificationDialog = new SendNotificationDialog(HomeActivity.this);
                                            sendNotificationDialog.setListner(new SendNotificationDialog.Listner() {
                                                @Override
                                                public void send() {

                                                    liveData.sentNotificationToStudent(student, "At your doorstep", "Please open the door " + student.fullname + " is waiting outside");

                                                }

                                                @Override
                                                public void cancel() {

                                                }
                                            }).show();


                                        }
                                    }
                                }

                            }
                        }


                        rideDocument.set(ride).addOnCompleteListener(task ->
                                Log.d(TAG, "Updated"));


                    }


                }
            }


        };

        mFusedLocationClient = new FusedLocationProviderClient(this);

        setupBottemNavListner();


    }


    private void sendNotification(Student student, String title, String body) {
        FirebaseFirestore.getInstance().collection("token")
                .document(student.parent_id + "").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {


                    String token = documentSnapshot.getString("token");
                    if (token != null) {

                        Notification.Notific notification = new Notification.Notific(title, body, UPDATE_STATUS);
                        notification.data = new Notification.Data(student.id);
                        MyApp.endPoints.sendNotification(EndPoints.FIREBAE_URL, new SendNotificationBody(token, notification))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new EndpointObserver<SendNotificationResponce>() {
                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onData(SendNotificationResponce o) throws Exception {

//                                        MyApp.showToast("Notification Sent " + student.fullname);
                                    }

                                    @Override
                                    public void onHandledError(Throwable e) {

                                        e.printStackTrace();
//                                        MyApp.showToast("Notification Sent Failed" + student.fullname);

                                    }
                                });

                    }
                }

            }
        });
    }

    /**
     * Start ride.
     *
     * @param sendNotification
     */
    public void startRide(boolean sendNotification, boolean movetoMap) {
        List<Student> students = liveData.ride.getValue().students;

        Student lastStudent = students.get((students.size() - 1));
        Location myLocation = liveData.myLocaton.getValue();
        if (myLocation != null) {


            if (liveData.ride.getValue().shift.equals(Ride.SHIFT_MORNING) || liveData.isAttandanceMarked()) {

                if (creatRouteTask != null) {
                    creatRouteTask.cancel();
                }


                createRouteDialog = new ProgressDialog(HomeActivity.this);
                createRouteDialog.setCancelable(false);
                createRouteDialog.setMessage("Creating Route");
                createRouteDialog.show();

                createRoute(students, myLocation, sendNotification, movetoMap, false);


                creatRouteTask = new TimerTask() {
                    @Override
                    public void run() {

                        createRoute(students, liveData.myLocaton.getValue(), false, false, true);

                    }
                };
                timer.scheduleAtFixedRate(creatRouteTask, 60 * 1000, 60 * 1000);

//            fetchRoute(mylocation, lastLocation, students);
            } else {
                bottomNavigationView.setSelectedItemId(R.id.bottomNavigationAttendance);
            }


        } else {
            tempMyLocationObserver = new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    HomeActivity.this.waiting_for_locationDialog.dismiss();
                    liveData.myLocaton.removeObserver(tempMyLocationObserver);
                    startRide(sendNotification, movetoMap);
                }
            };
            waiting_for_locationDialog = new AlertDialog.Builder(HomeActivity.this)
                    .setCancelable(false)
                    .setMessage("Waiting for location")
                    .show();

            liveData.myLocaton.observe(this, tempMyLocationObserver);

//            MyApp.showToast("My Location Null");
        }
    }

    private void createRoute(List<Student> students, Location myLocation, boolean sendNotification, boolean movetoMap, boolean refreshRoute) {
        Point mylocation = Point.fromLngLat(myLocation.getLongitude(), myLocation.getLatitude());
        GeoPoint schoolLatLng = liveData.ride.getValue().schoolLatLng;
        Point lastLocation = Point.fromLngLat(schoolLatLng.getLongitude(), schoolLatLng.getLatitude());
        getRoute(mylocation, lastLocation, students, sendNotification, movetoMap, refreshRoute);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (creatRouteTask != null) {
            creatRouteTask.cancel();
        }
        timer.cancel();
    }

    private void setupBottemNavListner() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.bottomNavigationMap:
//                    List<Student> studentList = liveData.students.getValue();
                    Fragment fragment;
                    Ride ride = liveData.ride.getValue();
                    if (ride == null) {

                        fragment = HomeFragment.getInstance();
                    } else if (ride.rideInProgress) {
                        fragment = MapFragment.getInstance();
//                        fragment = NavigationFragment.getInstance();
                    } else {
                        fragment = HomeFragment.getInstance();
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, fragment)
                            .commit();
                    break;
                case R.id.bottomNavigationAttendance:

                    Fragment instance;

                    if (customRoot) {
                        instance = RouteDesignerFragment.getInstance();
                    } else {
                        instance = AttendanceFragemnt.getInstance();
                    }


                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, instance)
                            .commit();
                    break;
                case R.id.bottomNavigationPowerScreen:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, AbsentFragment.getInstance())
                            .commit();
                    break;
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationMap);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.LEFT, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Finish ride.
     */
    public void finishRide() {

        if (creatRouteTask != null) {
            creatRouteTask.cancel();
        }

        liveData.currentRoute.setValue(null);
        Ride ride = liveData.ride.getValue();
        ride.rideInProgress = false;
        ride.endTime = Timestamp.now();

        liveData.ride.setValue(ride);
        rideDocument.set(ride).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                rideDocument.delete();
            }
        });
        history.add(ride).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                liveData.ride.setValue(null);
            }
        });

        liveData.students.setValue(new ArrayList<>());


        new FinishRideDialog(this).setListner(new FinishRideDialog.Listner() {
            @Override
            public void logout() {
                HomeActivity.this.logout();


            }

            @Override
            public void cancel() {
                recreate();

            }
        }).show();


    }

    /**
     * Update attendance.
     */
    public void updateAttendance() {
        rideDocument.set(liveData.ride.getValue());
    }

    /**
     * Route selected.
     */
    public void routeSelected() {
        customRoot = false;
        if (liveData.ride.getValue().shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {
            startRide(true, true);

        } else {
            startRide(true, false);
        }

    }

    /**
     * The type Navigation vh.
     */
    public class NavigationVH extends RecyclerView.ViewHolder {
        /**
         * The Text view.
         */
        @BindView(R.id.textView)
        TextView textView;

        /**
         * Instantiates a new Navigation vh.
         *
         * @param itemView the item view
         */
        public NavigationVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Bind.
         */
        public void bind() {
            textView.setText(menuItem.get(getAdapterPosition()));
        }

        /**
         * On menu item clicked.
         */
        @OnClick(R.id.textView)
        public void onMenuItemClicked() {

            drawerLayout.closeDrawer(Gravity.LEFT);
            String text = textView.getText().toString();
            if (text.equalsIgnoreCase(MY_POWER)) {


//                bottomNavigationView.setSelectedItemId(R.id.bottomNavigationPowerScreen);

            } else if (text.equalsIgnoreCase(HOME)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, HomeFragment.getInstance())
                        .commit();
            } else if (text.equalsIgnoreCase(MY_POWER)) {
                ProfileActivity.start(HomeActivity.this);

            } else if (text.equalsIgnoreCase(CONTACT_US)) {
                ContectUsActivity.start(HomeActivity.this);
            } else if (text.equalsIgnoreCase(MY_PROFILE)) {
                ProfileActivity.start(HomeActivity.this);
            } else if (text.equalsIgnoreCase(LOGOUT)) {

                logout();

            } else if (text.equalsIgnoreCase(UPDATE_MY_LOCATION)) {


            } else if (text.equalsIgnoreCase(ROUTE_DESIGNER)) {


                RouteDesignerActivity.start(HomeActivity.this);

            }

        }
    }

    private void logout() {
        MyPref.logout(HomeActivity.this);

        Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * The type Live data.
     */
    public static class LiveData extends ViewModel {

        /**
         * The My locaton.
         */
        public MutableLiveData<Location> myLocaton = new MutableLiveData<>();
        public MutableLiveData<DirectionsRoute> currentRoute = new MutableLiveData<>();
        private CompositeDisposable compositeDisposable = new CompositeDisposable();

        /**
         * The User.
         */
        public MutableLiveData<LoginResponse.User> user = new MutableLiveData<>();
        /**
         * The Students.
         */
        public MutableLiveData<List<Student>> students = new MutableLiveData<>();
        /**
         * The Create ride listener.
         */
        public MutableLiveData<Boolean> createRideListener = new MutableLiveData<>();
        /**
         * The Selected shift.
         */
        public MutableLiveData<ShiftItem> selectedShift = new MutableLiveData<>();
        /**
         * The Ride.
         */
        public MutableLiveData<Ride> ride = new MutableLiveData<>();
        /**
         * The School responce.
         */
        public MutableLiveData<GetSchoolResponce> schoolResponce = new MutableLiveData<>();

        /**
         * The Driver profile.
         */
        public MutableLiveData<DriverProfileResponse> driverProfile = new MutableLiveData<>();


        public MutableLiveData<MyResponse<List<AbsenteStdent>>> absentResponse = new MutableLiveData<>();


        private String TAG = this.getClass().getSimpleName();

        /**
         * Instantiates a new Live data.
         */
        public LiveData() {
            compositeDisposable = new CompositeDisposable();
            user.setValue(MyApp.instance.user);
        }

        private void getSchoolProfile(String schoolId) {
            EndpointObserver<MyResponse<GetSchoolResponce>> endpointObserver = MyApp.endPoints.getSchool(schoolId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<MyResponse<GetSchoolResponce>>() {
                        @Override
                        public void onComplete() {


                        }

                        @Override
                        public void onData(MyResponse<GetSchoolResponce> schoolResponce) throws Exception {
                            if (schoolResponce.status == 200 && schoolResponce.data != null) {
                                LiveData.this.schoolResponce.setValue(schoolResponce.data);
                            } else {
                                throw new Exception(schoolResponce.status + "");
                            }

                        }

                        @Override
                        public void onHandledError(Throwable e) {

                            e.printStackTrace();
                        }
                    });

            compositeDisposable.add(endpointObserver);

        }

        /**
         * Start ride.
         *
         * @param shiftItem the shift item
         */
        public void startRide(ShiftItem shiftItem) {

            selectedShift.setValue(shiftItem);

            createRideListener.setValue(true);


            CreateRideBody createRideBody = new CreateRideBody(MyApp.instance.user.id, shiftItem.shift_id);

            EndpointObserver<MyResponse<List<Student>>> endpointObserver = MyApp.endPoints
                    .createRide(MyApp.instance.user.id, shiftItem.shift_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<MyResponse<List<Student>>>() {
                        @Override
                        public void onComplete() {

                            createRideListener.setValue(false);

                        }

                        @Override
                        public void onData(MyResponse<List<Student>> data) throws Exception {

                            if (data.status == 200) {
                                LiveData.this.students.setValue(data.data);
                                getAbsentList(shiftItem.shift_id);
                            } else {
                                throw new Exception(data.status + "");
                            }

                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            e.printStackTrace();
                            MyApp.showToast(e.getMessage());

                        }
                    });

            compositeDisposable.add(endpointObserver);

        }

        public void getAbsentList(int shiftId) {

            MyApp.endPoints.getAbsentList(MyApp.instance.user.id, shiftId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EndpointObserver<MyResponse<List<AbsenteStdent>>>() {
                        @Override
                        public void onComplete() {

                            compositeDisposable.add(this);
                        }

                        @Override
                        public void onData(MyResponse<List<AbsenteStdent>> o) throws Exception {
                            if (o.status == 200) {

                                absentResponse.setValue(o);

                            } else {
                                throw new Exception(o.status + "");
                            }
                        }

                        @Override
                        public void onHandledError(Throwable e) {

                            e.printStackTrace();
                            MyApp.showToast(e.getMessage());
                        }
                    });

        }

        /**
         * Gets driver profile.
         */
        public void getDriverProfile() {
            EndpointObserver<MyResponse<DriverProfileResponse>> endpointObserver = MyApp.endPoints
                    .getDriverProfile(MyApp.instance.user.id + "")
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<MyResponse<DriverProfileResponse>>() {
                        @Override
                        public void onComplete() {
//                            loader.setValue(false);

                        }

                        @Override
                        public void onData(MyResponse<DriverProfileResponse> response) throws Exception {
//                            loader.setValue(false);

                            if (response.status == 200) {

                                driverProfile.setValue(response.data);
                                getSchoolProfile(response.data.school_id + "");

                            } else {
                                throw new Exception(response.status + "");
                            }
                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            e.printStackTrace();
//                            loader.setValue(false);
//                            errorListener.setValue(new Event<>(e));


                        }
                    });
            compositeDisposable.add(endpointObserver);
        }


        private void sendNotificationToAll(String title, String message) {

            Ride ride = this.ride.getValue();
            List<Student> studentsObj = ride.students;
            List<Integer> studentParentIds = new ArrayList<>();

            for (Student student : studentsObj) {

                studentParentIds.add(student.parent_id);
            }


            CollectionReference token = FirebaseFirestore.getInstance()
                    .collection("token");

//        for (Student student : students) {
//            token.whereArrayContains("userId", student.parent_id);
//        }

            token.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
                    List<String> tokens = new ArrayList<>();


                    for (DocumentSnapshot parentToken : queryDocumentSnapshots.getDocuments()) {


                        try {
                            Integer parentId = Integer.parseInt(parentToken.getId());

                            if (studentParentIds.contains(parentId)) {
                                String token = parentToken.getString("token");
                                tokens.add(token);
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }


                    }
                    if (tokens.size() > 0)
                        MyApp.endPoints.sendNotification(EndPoints.FIREBAE_URL
                                , new SendNotificationBody(tokens, new Notification.Notific(title, message, RIDE)))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new EndpointObserver<SendNotificationResponce>() {
                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onData(SendNotificationResponce o) throws Exception {

                                        Log.d(TAG, "Notification Sent to All");
//                                        MyApp.showToast("Notification Sent to All");

                                    }

                                    @Override
                                    public void onHandledError(Throwable e) {

                                        e.printStackTrace();

//                                        MyApp.showToast("Notification Failed Sent to All");


                                    }

                                });


                }
            });


        }

        private void sentNotificationToStudent(Student student, String title, String message) {


            Task<DocumentSnapshot> token = FirebaseFirestore.getInstance()
                    .collection("token").document(student.parent_id + "").get();

            token.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot != null && documentSnapshot.exists() && documentSnapshot.getString("token") != null) {
                        Notification.Notific notification = new Notification.Notific(title, message, RIDE);
                        notification.data = new Notification.Data(student.id);

                        MyApp.endPoints.sendNotification(EndPoints.FIREBAE_URL
                                , new SendNotificationBody(documentSnapshot.getString("token")
                                        , notification))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new EndpointObserver<SendNotificationResponce>() {
                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onData(SendNotificationResponce o) throws Exception {

                                        Log.d(TAG, "Notification Sent to All");
//                                        MyApp.showToast("Notification Sent to All");

                                    }

                                    @Override
                                    public void onHandledError(Throwable e) {

                                        e.printStackTrace();

//                                        MyApp.showToast("Notification Failed Sent to All");


                                    }

                                });
                    }


                }
            });


        }


        /**
         * Send notification to all students.
         */
        public void sendNotificationToAllStudents() {


            Ride value = ride.getValue();
            List<Student> students = value.students;


            for (Student student : students) {
                if (ride.getValue().shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {
                    sentNotificationToStudent(student
                            , "Bus is coming"
//                            , "Bus is on its way to pickup minutes");
                            , "Bus is on its way to pickup " + student.fullname + " and will be there in ETA (" + Math.round(student.duration) + ") minutes");
                } else {


                    if (student.present == PRESENT) {

                        sentNotificationToStudent(student
                                , "In the bus"
                                , student.fullname + " is in the bus and will reach around ETA " + Math.round(student.duration) + " minutes");
                    }
                    student.status = STATUS_AFTERNOON_INTHEBUS;

                }
            }

            value.students = students;
            ride.setValue(value);


        }


        /**
         * Is going school boolean.
         * k agar wo 3 status se agay barh chuka hai to phir school location pe popup ay
         *
         * @return the boolean
         */
        public boolean isGoingSchool() {
            for (Student student : ride.getValue().students) {
                if (student.status < Student.STATUS_MORNING_ONTHEWAY) {
                    return false;
                }
            }
            return true;
        }

        public boolean isAttandanceMarked() {
            for (Student student : ride.getValue().students) {
                if (student.present == Student.UNKNOWN) {

                    return false;
                }

            }
            return true;
        }

        public List<Student> getAbsentStudents() {
            List<Student> students = new ArrayList<>();

            for (Student student : ride.getValue().students) {

                if (student.present == Student.ABSENT) {
                    students.add(student);
                }

            }

            return students;
        }

        public void sendNotificationTOALlPresentStudents(String text) {


            List<Student> students = ride.getValue().students;


            for (Student student : students) {

//                if (ride.getValue().shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {
//
//                    sentNotificationToStudent(student
//                            , "Bus is coming"
////                            , "Bus is on its way to pickup minutes");
//                            , "Bus is on its way to pickup " + student.fullname + " and will be there in ETA (" + Math.round(student.duration) + ") minutes");
//
//                } else {


                if (student.present == PRESENT) {

                    sentNotificationToStudent(student, "Alert", text);


                }


//                }


            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        liveData.getDriverProfile();
        permissionHelper = new PermissionHelper();
        permissionHelper
                .with(this)
                .permissionId(12)
                .requiredPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .setListner(new PermissionHelper.PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {


                        startLocationUpdates();

                    }

                    @Override
                    public void onPermissionFailed() {

                        MyApp.showToast("Location Permission Failed");
                    }
                }).request();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                mFusedLocationClient.requestLocationUpdates(getLocatonRequest(), mLocationCallback, null /* Looper */);
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(HomeActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }

    private LocationRequest getLocatonRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(1);
        return locationRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * Make a request to the Mapbox Directions API. Once successful, pass the route to the
     * route layer.
     *
     * @param origin           the starting point of the route
     * @param destination      the desired finish point of the route
     * @param sendNotification
     * @param movetoMap
     */
    private void getRoute(Point origin, Point destination, List<Student> wayPoints, boolean sendNotification, boolean movetoMap, boolean refreshRoute) {

        MapboxDirections.Builder builder = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(MAPBOX_TOKEN);


        for (Student point : wayPoints) {

            builder.addWaypoint(Point.fromLngLat(point.lng, point.lat));

        }

        MapboxDirections client = builder
                .build();

        creatingRoute = true;


        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                creatingRoute = false;


                createRouteDialog.dismiss();
                liveData.currentRoute.setValue(response.body().routes().get(0));
//                Toast.makeText(HomeActivity.this, liveData.currentRoute.getValue().distance() + "", Toast.LENGTH_SHORT).show();

                Ride ride = liveData.ride.getValue();

                List<RouteLeg> legs = liveData.currentRoute.getValue().legs();
                double sumDistance = 0;
                double sumDuration = 0;
                for (int i = 0; i < legs.size() - 1; i++) {
                    RouteLeg routeLeg = legs.get(i);
                    sumDuration += (routeLeg.duration() / 60);
                    sumDistance += (routeLeg.distance() / 1000);

                    ride.students.get(i).distance = sumDistance;
                    ride.students.get(i).duration = sumDuration;

                }

                liveData.ride.setValue(ride);

                if (sendNotification) {

                    liveData.sendNotificationToAllStudents();
                }

                if (!refreshRoute) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (movetoMap) {
                                bottomNavigationView.setSelectedItemId(R.id.bottomNavigationMap);

                            } else {
                                bottomNavigationView.setSelectedItemId(R.id.bottomNavigationAttendance);

                            }

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                createRouteDialog.dismiss();

                creatingRoute = false;

                throwable.printStackTrace();
                Toast.makeText(HomeActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//        List<Point> points = new ArrayList<>();
//        for (Student point : wayPoints) {
//            points.add(Point.fromLngLat(point.lng, point.lat));
//        }

//        MapboxMatrix directionsMatrixClient = MapboxMatrix.builder()
//                .accessToken(MAPBOX_TOKEN)
//                .profile(DirectionsCriteria.PROFILE_DRIVING)
//                .coordinates(points)
//                .build();
//        directionsMatrixClient.enqueueCall(new Callback<MatrixResponse>() {
//            @Override
//            public void onResponse(Call<MatrixResponse> call, Response<MatrixResponse> response) {
//
//
//            }
//
//            @Override
//            public void onFailure(Call<MatrixResponse> call, Throwable t) {
//
//                t.printStackTrace();
//            }
//        });


    }

    private void fetchRoute(Point origin, Point destination, List<Student> students) {

        NavigationRoute.Builder builder = NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)

                .destination(destination)
                .alternatives(true);

//        builder.addWaypointTargets(students.toArray(students));

        for (Student point : students) {

            builder.addWaypoint(Point.fromLngLat(point.lng, point.lat));


        }

        builder
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                        navigationRoute = response.body().routes().get(0);
//                        startNavigation(navigationRoute);

                        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationMap);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        t.printStackTrace();

                        MyApp.showToast(t.getMessage());
                    }
                });

    }

}
