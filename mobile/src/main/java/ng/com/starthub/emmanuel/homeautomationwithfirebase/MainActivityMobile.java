package ng.com.starthub.emmanuel.homeautomationwithfirebase;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MainActivityMobile extends Activity {

    private final static String TAG = MainActivityMobile.class.getSimpleName();

    private Button allOnButton ;
    private Button allOffButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mobile);

        Switch device_1_switch = findViewById(R.id.device1_switch);
        Switch device_2_switch = findViewById(R.id.device2_switch);
        Switch device_3_switch = findViewById(R.id.device3_switch);
        Switch device_4_switch = findViewById(R.id.device4_switch);
        TextView monitor_1_textView = findViewById(R.id.textView7);

        allOnButton = findViewById(R.id.all_on_btn);
        allOffButton = findViewById(R.id.all_off_btn);


        //Reference the database table and column

        final FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference dbRef = fbDatabase.getReference("home_db");
        final DatabaseReference device1_Ref = dbRef.child("device1");
        final DatabaseReference device2_Ref = dbRef.child("device2");
        final DatabaseReference device3_Ref = dbRef.child("device3");
        final DatabaseReference device4_Ref = dbRef.child("device4");
        final DatabaseReference monitor1_Ref = dbRef.child("monitor1");


        Log.d(TAG, "Listening to database... ");
        device1_Ref.addValueEventListener(new SwitchChangeEventListener(device_1_switch));
        device2_Ref.addValueEventListener(new SwitchChangeEventListener(device_2_switch));
        device3_Ref.addValueEventListener(new SwitchChangeEventListener(device_3_switch));
        device4_Ref.addValueEventListener(new SwitchChangeEventListener(device_4_switch));
        monitor1_Ref.addValueEventListener(new MonitorChangeEventListener(monitor_1_textView));


        //Set the value in the data =base when the switch is checked.
        device_1_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                device1_Ref.setValue(isChecked);
                Log.d(TAG, "set data on database... device 1: " + device1_Ref.getKey());
            }
        });

        device_2_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                device2_Ref.setValue(isChecked);
                Log.d(TAG, "set data on database... device 2: " + device2_Ref.getKey());
            }
        });
        device_3_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                device3_Ref.setValue(isChecked);
                Log.d(TAG, "set data on database... device 3: " + device3_Ref.getKey());
            }
        });
        device_4_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                device4_Ref.setValue(isChecked);
                Log.d(TAG, "set data on database... device 4: " + device4_Ref.getKey());
            }
        });



        allOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnAllOnOff(dbRef, false);
            }

        });

        allOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnAllOnOff(dbRef, true);
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("Alert");
        String tkn = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(this, "Successfully subscribe!", LENGTH_SHORT).show();
        Log.d(TAG, "Token [" + tkn + "]");

        /*Button btn = findViewById(R.id.send_alert);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().subscribeToTopic("alarm");
                String tkn = FirebaseInstanceId.getInstance().getToken();
                Toast.makeText(MainActivityMobile.this, "Successfully subscribe!", LENGTH_SHORT).show();
                Log.d("App", "Token [" + tkn + "]");
            }
        });
*/
    }

    /*
      Turn on or off all switch
     */
    private void turnAllOnOff(final DatabaseReference reference, final boolean state) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/device1", state);
        childUpdates.put("/device2", state);
        childUpdates.put("/device3", state);
        childUpdates.put("/device4", state);
        reference.updateChildren(childUpdates);

        if(state){
            allOnButton.setClickable(false);
            allOffButton.setClickable(true);
        }else {
            allOnButton.setClickable(true);
            allOffButton.setClickable(false);
        }


    }
}
