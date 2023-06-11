package mobile;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.runora_dev.runoraf.Activity.LogInActivity;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class LogInActivityTest {
    @Mock
    private FirebaseAuth firebaseAuth;
    @Mock
    private FirebaseFirestore firebaseFirestore;
    @Mock
    private ProgressDialog progressDialog;
    @Mock
    private static Context context;
    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private SharedPreferences.Editor editor;
    @Mock
    private Task<DocumentSnapshot> task;
    @Mock
    private DocumentSnapshot documentSnapshot;

    private LogInActivity loginActivity;

    @BeforeClass
    public static void setUpBeforeClass() {
        context = ApplicationProvider.getApplicationContext();
        FirebaseApp.initializeApp(context);
    }
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loginActivity = new LogInActivity();
        loginActivity.firebaseAuth = firebaseAuth;
        loginActivity.firebaseFirestore = firebaseFirestore;
        loginActivity.progressDialog = progressDialog;
        loginActivity.context = context;
    }

    @Test
    public void testLoginUser_SuccessfulLogin() {
        // Mock email and password
        String email = "abc@a.com";
        String password = "123456";

        // Mock successful login
        when(firebaseAuth.signInWithEmailAndPassword(email, password))
                .thenReturn(mock(Task.class));
        when(firebaseAuth.getInstance().getUid()).thenReturn("userUid");
        when(firebaseFirestore.collection("user")).thenReturn(mock(CollectionReference.class));
        when(firebaseFirestore.collection("user").document("userUid")).thenReturn(mock(DocumentReference.class));
        when(firebaseFirestore.collection("user").document("userUid").get()).thenReturn(task);
        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(documentSnapshot);
        when(documentSnapshot.getData()).thenReturn(mock(Map.class));

        // Mock SharedPreferences
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);

        // Call the loginUser() method
        loginActivity.loginUser(email, password);

        // Verify that the appropriate methods were called
        verify(progressDialog).show();
        verify(firebaseAuth).signInWithEmailAndPassword(email, password);
        verify(firebaseFirestore).collection("user");
        verify(firebaseFirestore.collection("user").document("userUid")).get();
        verify(sharedPreferences).edit();
        verify(editor).putString(anyString(), anyString());
        verify(editor).commit();
        verify(progressDialog).cancel();
        verify(context).startActivity(any(Intent.class));
    }

    @Test
    public void testLoginUser_FailedLogin() {
        // Mock email and password
        String email = "test@example.com";
        String password = "incorrectPassword";

        // Mock failed login
        when(firebaseAuth.signInWithEmailAndPassword(email, password))
                .thenReturn(mock(Task.class));
        when(task.isSuccessful()).thenReturn(false);
        when(task.getException()).thenReturn(new Exception("Login failed."));

        // Call the loginUser() method
        loginActivity.loginUser(email, password);

        // Verify that the appropriate methods were called
        verify(progressDialog).show();
        verify(firebaseAuth).signInWithEmailAndPassword(email, password);
        verify(progressDialog).cancel();
        verify(context).getApplicationContext();
        verify(context).getString(anyInt());
        verify(Toast.makeText(context, "Login failed.", Toast.LENGTH_LONG)).show();
    }
}
