package com.bincee.driver.fragment;


import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bincee.driver.BuildConfig;
import com.bincee.driver.HomeActivity;
import com.bincee.driver.MyApp;
import com.bincee.driver.R;
import com.bincee.driver.api.EndPoints;
import com.bincee.driver.api.firestore.FireStoreHelper;
import com.bincee.driver.api.firestore.Ride;
import com.bincee.driver.api.model.SendNotificationBody;
import com.bincee.driver.api.model.SendNotificationResponce;
import com.bincee.driver.api.model.Student;
import com.bincee.driver.api.model.notification.Notification;
import com.bincee.driver.base.BFragment;
import com.bincee.driver.dialog.MarkAttendanceDialog;
import com.bincee.driver.dialog.MarkStudentAbdentDialog;
import com.bincee.driver.observer.EndpointObserver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bincee.driver.api.firestore.Ride.SHIFT_AFTERNOON;
import static com.bincee.driver.api.model.notification.Notification.ATTANDACE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragemnt extends BFragment {


    private static final String TAG = AttendanceFragemnt.class.getSimpleName();
    private static AttendanceFragemnt attendanceFragemnt;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.buttonStartRide)
    Button buttonStartRide;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    private ItemTouchHelper itemTouchHelper;


    public AttendanceFragemnt() {
        // Required empty public constructor
    }

    public static AttendanceFragemnt getInstance() {
        if (attendanceFragemnt == null) {
            attendanceFragemnt = new AttendanceFragemnt();
        }
        return attendanceFragemnt;
    }

    MyAttandanceAdapter attandanceAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attandanceAdapter = new MyAttandanceAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        setActivityTitle("MARK STUDENTS");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_fragemnt, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(attandanceAdapter);
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(-1, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped: " + direction);


                if (viewHolder instanceof VH) {
                    VH vh = (VH) viewHolder;
                    vh.swiped(direction);
                }


            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView
                    , @NonNull RecyclerView.ViewHolder viewHolder
                    , float dX, float dY
                    , int actionState
                    , boolean isCurrentlyActive) {
                Log.d(TAG, "onChildDraw: " + isCurrentlyActive + " dX=" + dX);

                if (viewHolder instanceof VH) {
                    VH vh = (VH) viewHolder;
                    vh.translate(dX, isCurrentlyActive);
                }


            }
        });
        itemTouchHelper.attachToRecyclerView(recycleView);


        getRide().observe(getViewLifecycleOwner(), new Observer<Ride>() {
            @Override
            public void onChanged(Ride ride) {
                if (ride != null && ride.shift.equalsIgnoreCase(SHIFT_AFTERNOON)) {
                    buttonStartRide.setVisibility(View.VISIBLE);

                    if (ride.students != null && ride.students.size() > 0 && getHomeActivity().liveData.currentRoute.getValue() == null) {


//                        for (Student student : ride.students) {
//                            if (student.present == Student.UNKNOWN) {
//                                buttonStartRide.setVisibility(View.GONE);
//
//                                return;
//                            }
//
//                        }


                    } else {

                    }


                } else {
                    buttonStartRide.setVisibility(View.GONE);

                }


            }
        });


    }

    @OnClick(R.id.buttonStartRide)
    public void onViewClicked() {

//   TODO     162352

        HomeActivity.LiveData liveData = getHomeActivity().liveData;
        Ride ride = liveData.ride.getValue();
        if (ride.shift.equalsIgnoreCase(SHIFT_AFTERNOON)) {

            if (getHomeActivity().liveData.currentRoute.getValue() != null) return;

            if (!liveData.isAttandanceMarked()) {
                MyApp.showToast("Please mark all students before proceeding");
            } else if (getHomeActivity().liveData.getAbsentStudents().size() > 0) {

                MarkStudentAbdentDialog absentDialog = new MarkStudentAbdentDialog(getContext(), getHomeActivity().liveData.getAbsentStudents());
                absentDialog.setListner(new MarkStudentAbdentDialog.Listner() {
                    @Override
                    public void yes() {

                        checkIfRouteExists();

                    }

                    @Override
                    public void no() {

                    }
                });
                absentDialog.show();

            } else {

                checkIfRouteExists();

            }


        }


    }

    public void checkIfRouteExists() {
        getHomeActivity().checkIfRouteExists();
    }

    private void startRide() {

        getHomeActivity().startRide(true, true);
    }

//    public void setStudentList(List<Student> students) {
//        this.students = students;
//        if (attandanceAdapter != null) {
//            attandanceAdapter.notifyDataSetChanged();
//        }
//    }

    private class MyAttandanceAdapter extends RecyclerView.Adapter<VH> {


        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getContext()).inflate(R.layout.attendance_recycler_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {

            holder.bind();

        }

        @Override
        public int getItemCount() {
            return getStudents().size();
        }
    }

    private List<Student> getStudents() {
        Ride ride = getRide().getValue();
        return ride != null ? ride.students : new ArrayList<>();
    }

    private MutableLiveData<Ride> getRide() {
        return getHomeActivity().liveData.ride;
    }

    private HomeActivity getHomeActivity() {
        return (HomeActivity) getActivity();
    }

    public class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewGreenSelected)
        ImageView imageViewGreenSelected;
        @BindView(R.id.imageViewRedSelected)
        ImageView imageViewRedSelected;
        @BindView(R.id.viewRed)
        View viewRed;
        @BindView(R.id.viewGreen)
        View viewGreen;
        @BindView(R.id.imageViewCross)
        ImageView imageViewCross;
        @BindView(R.id.imageViewCheck)
        ImageView imageViewCheck;
        @BindView(R.id.textViewName)
        TextView textViewName;
        @BindView(R.id.textViewLocation)
        TextView textViewLocation;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        @BindView(R.id.rootView)
        FrameLayout rootView;

        @BindView(R.id.textViewDuration)
        TextView textViewDuration;
        @BindView(R.id.textViewDistance)
        TextView textViewDistance;

        public VH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (BuildConfig.FLAVOR.equalsIgnoreCase("client")) {
                textViewDuration.setVisibility(View.GONE);
                textViewDistance.setVisibility(View.GONE);
            } else {
                textViewDuration.setVisibility(View.VISIBLE);
                textViewDistance.setVisibility(View.VISIBLE);
            }

        }

        public void bind() {

            Student student = getStudents().get(getAdapterPosition());
            textViewName.setText(student.fullname);
            textViewLocation.setText(student.address);

            if (student.present == Student.UNKNOWN) {

                imageViewRedSelected.setAlpha(0f);
                imageViewGreenSelected.setAlpha(0f);
                setTextColorBlack();

            } else if (student.present == Student.PRESENT) {


                markPresent();

            } else if (student.present == Student.ABSENT) {

                markAbsent();
            }

            textViewDistance.setText(student.distance + "");
            textViewDuration.setText(student.duration + "");
        }

        private void setTextColorBlack() {
            textViewName.setTextColor(Color.BLACK);
            textViewLocation.setTextColor(Color.BLACK);
        }

        private void markAbsent() {
            imageViewRedSelected.setAlpha(1f);
            imageViewGreenSelected.setAlpha(0f);
            imageViewCheck.setImageBitmap(null);
            imageViewCross.setImageDrawable(getResources().getDrawable(R.drawable.corss));


            setTextColorWhite();
            getStudents().get(getAdapterPosition()).present = Student.ABSENT;


        }

        private void updateStudentStatus() {
            Ride ride = getRide().getValue();
            if (ride.shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {

                ride.students.get(getAdapterPosition()).status = Student.STATUS_MORNING_ONTHEWAY;

            } else {

//                if (ride.students.get(getAdapterPosition()).status < Student.STATUS_AFTERNOON_INTHEBUS) {
//
//                    ride.students.get(getAdapterPosition()).status = Student.STATUS_AFTERNOON_INTHEBUS;
//                } else {
//
//                }

            }

            HomeActivity activity = getHomeActivity();
            activity.liveData.ride.setValue(ride);
            activity.updateAttendance();
        }

        private void markPresent() {
            imageViewRedSelected.setAlpha(0f);
            imageViewGreenSelected.setAlpha(1f);

            imageViewCheck.setImageDrawable(getResources().getDrawable(R.drawable.check));
            imageViewCross.setImageBitmap(null);

            setTextColorWhite();
            getStudents().get(getAdapterPosition()).present = Student.PRESENT;


        }

        private void sendNotification(int parentId, String title, String body) {

            Student student = getStudents().get(getAdapterPosition());


            FireStoreHelper.getToken(parentId + "")
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();


                            List<String> tokens = new ArrayList<>();

                            for (DocumentSnapshot documentSnapshot : documents) {

                                if (documentSnapshot != null) {

                                    String token = documentSnapshot.getString("token");
                                    if (token != null) {
                                        tokens.add(token);

                                    }
                                }
                            }

                            if (tokens.size() > 0) {
                                Notification.Notific notification = new Notification.Notific(title, body, ATTANDACE);
                                notification.data = new Notification.Data(student.id);

                                MyApp.endPoints.sendNotification(EndPoints.FIREBAE_URL,
                                        new SendNotificationBody(tokens, notification))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new EndpointObserver<SendNotificationResponce>() {
                                            @Override
                                            public void onComplete() {

                                            }

                                            @Override
                                            public void onData(SendNotificationResponce o) throws Exception {
//                                            MyApp.showToast("Notification Sent " + student.fullname);

                                            }

                                            @Override
                                            public void onHandledError(Throwable e) {

                                                e.printStackTrace();
//                                            MyApp.showToast("Notification Sent Failed " + student.fullname);

                                            }
                                        });
                            }


                        }
                    });
        }

        private void setTextColorWhite() {
            textViewName.setTextColor(Color.WHITE);
            textViewLocation.setTextColor(Color.WHITE);
        }

        @OnClick(R.id.imageViewCross)
        public void onCrossClicked() {

            showAttDialog();
        }


        @OnClick(R.id.imageViewCheck)
        public void onCheckClicked() {

            showAttDialog();

        }

        @OnClick(R.id.rootView)
        public void onRootViewClicked() {

            showAttDialog();

        }


        private void showAttDialog() {

            if (getHomeActivity().liveData.ride.getValue().shift.equalsIgnoreCase(SHIFT_AFTERNOON))
                return;


            MarkAttendanceDialog markAttendanceDialog = new MarkAttendanceDialog(getContext())
                    .setStudent(getStudents().get(getAdapterPosition()))
                    .setListner(new MarkAttendanceDialog.Listner() {
                        @Override
                        public void markAbsent() {
                            VH.this.markAbsent();
                            if (getRide().getValue().shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {
                                updateStudentStatus();

                                Student student = getStudents().get(getAdapterPosition());
//                                getHomeActivity().liveData.sendNotificationToAll("dsd","sdsds");

                                sendNotification(getStudents().get(getAdapterPosition()).parent_id, "Kid is absent", student.fullname + " is absent ");

                                //TODO


                            } else {
                            }

                        }

                        @Override
                        public void markPresent() {
                            VH.this.markPresent();

                            if (getRide().getValue().shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {
                                updateStudentStatus();

                                Student student = getStudents().get(getAdapterPosition());
                                sendNotification(getStudents().get(getAdapterPosition()).parent_id, "On the way", " Bus is on the way to school and will be there in ETA " + Math.round(student.duration) + " minutes");


                            } else {
//                                sendNotification(getStudents().get(getAdapterPosition()).parent_id, "In the bus", student.fullname + " is in the bus and will reach in around ETA " + Math.abs(student.duration));
                            }


                        }
                    });
            markAttendanceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    attandanceAdapter.notifyDataSetChanged();

                }
            });
            markAttendanceDialog
                    .show();
        }

        public void translate(float dX, boolean isCurrentlyActive) {

            imageViewRedSelected.setVisibility(View.VISIBLE);
            imageViewGreenSelected.setVisibility(View.VISIBLE);

            int width = VH.this.itemView.getWidth();

            float alpha = Math.abs(dX / (width / 2));

            if (dX > 0) {

                imageViewGreenSelected.setAlpha(alpha);
                imageViewRedSelected.setAlpha(0f);

            } else {

                imageViewGreenSelected.setAlpha(0f);
                imageViewRedSelected.setAlpha(alpha);

            }

            setTextColorBlack();


        }

        public void swiped(int direction) {
            if (direction == ItemTouchHelper.LEFT) {

                if (getHomeActivity().liveData.ride.getValue().shift.equalsIgnoreCase(SHIFT_AFTERNOON)) {

                    markAbsent();
                } else {
                    showAttDialog();
                }
            } else if (direction == ItemTouchHelper.RIGHT) {
//                showAttDialog();
                if (getHomeActivity().liveData.ride.getValue().shift.equalsIgnoreCase(SHIFT_AFTERNOON)) {

                    markPresent();
                } else {
                    showAttDialog();
                }
            }
        }
    }
}
