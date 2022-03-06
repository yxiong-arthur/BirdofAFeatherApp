package com.swift.birdsofafeather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swift.birdsofafeather.model.db.AppDatabase;
import com.swift.birdsofafeather.model.db.Class;
import com.swift.birdsofafeather.model.db.Session;
import com.swift.birdsofafeather.model.db.SessionStudent;
import com.swift.birdsofafeather.model.db.Student;
import com.swift.birdsofafeather.model.db.StudentWithClasses;
import com.swift.birdsofafeather.model.db.UUIDConverter;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CourseDashboard  extends AppCompatActivity {

    private AppDatabase db;
    private UUID studentId;
    private StudentWithClasses myself;
    private Set<Class> myClasses;
    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager courseLayoutManager;
    private CourseViewAdapter courseViewAdapter;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        this.future = backgroundThreadExecutor.submit(() -> {
            db = AppDatabase.singleton(getApplicationContext());

            SharedPreferences preferences = Utils.getSharedPreferences(this);
            String UUIDString = preferences.getString("student_id", "");
            studentId = UUID.fromString(UUIDString);

            List<Session> mySessions = db.sessionDao().getAllSessions();

            runOnUiThread(() -> {
                // Set up the recycler view to show our database contents
                courseRecyclerView = findViewById(R.id.persons_view);

                courseLayoutManager = new LinearLayoutManager(this);
                courseRecyclerView.setLayoutManager(courseLayoutManager);

                courseViewAdapter = new CourseViewAdapter(mySessions);
                courseRecyclerView.setAdapter(courseViewAdapter);
            });
        });
    }

    public void onNewSession(View view){
        UUID newSessionId = UUID.randomUUID();
        Session newSession = new Session(newSessionId);
        db.sessionDao().insert(newSession);

        SessionStudent studentInSession = new SessionStudent(newSessionId, studentId);
        db.sessionStudentDao().insert(studentInSession);

        SharedPreferences preferences = Utils.getSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_session_id", UUIDConverter.fromUUID(newSessionId));
        editor.apply();

        finish();
    }
}
