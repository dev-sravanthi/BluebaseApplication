package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bluebase.activities.R;

import util.ScrollTextView;
import util.Utility;

public class CRMMenu extends AppCompatActivity {

    String token, login_id;
    boolean networkAvailability = false;
    LinearLayout ll_enquiryform,ll_costsheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_menu);

        Intent i = getIntent();
        token = i.getStringExtra("token");
        login_id = i.getStringExtra("login_id");

        ScrollTextView scrolltext = findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability = Utility.isConnectingToInternet(CRMMenu.this);

        if (networkAvailability == true) {
            findviewids();
        } else {
            Utility.getAlertNetNotConneccted(CRMMenu.this, "Internet Connection");
        }

    }

    private void findviewids() {
        ll_costsheet=findViewById(R.id.ll_costsheet);
        ll_enquiryform=findViewById(R.id.ll_enquiryform);

        ll_enquiryform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EnquiryCandidateList.class);
                i.putExtra("token", token);
                i.putExtra("login_id", login_id);
                startActivity(i);
                finish();
            }
        });

        ll_costsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CostSheet.class);
                i.putExtra("token", token);
                i.putExtra("login_id", login_id);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent MainActivity = new Intent(getBaseContext(), MenuScreen.class);
        MainActivity.putExtra("token",token);
        MainActivity.putExtra("login_id",login_id);
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        CRMMenu.this.finish();
    }

}