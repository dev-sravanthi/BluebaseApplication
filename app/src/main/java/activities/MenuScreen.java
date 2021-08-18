package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.bluebase.activities.R;
import util.ScrollTextView;
import util.Utility;

public class MenuScreen extends AppCompatActivity {

    ImageButton imgbtn_candidatelist,imgbtn_feedbacklist,imgbtn_costsheet;
    String token,candidateId;
    boolean networkAvailability=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        candidateId=i.getStringExtra("candidateId");

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(MenuScreen.this);

        if(networkAvailability==true){
            findviewids();
        }else{
            Utility.getAlertNetNotConneccted(MenuScreen.this, "Internet Connection");
        }

    }

    private void findviewids() {
        imgbtn_candidatelist=findViewById(R.id.imgbtn_candidatelist);
        imgbtn_feedbacklist=findViewById(R.id.imgbtn_feedbacklist);
        imgbtn_costsheet=findViewById(R.id.imgbtn_costsheet);

        imgbtn_candidatelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),CandidateList.class);
                i.putExtra("token",token);
                i.putExtra("login_id",candidateId);
                startActivity(i);
                finish();
            }
        });

        imgbtn_feedbacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),FeedbackCandidateList.class);
                i.putExtra("token",token);
                i.putExtra("login_id",candidateId);
                startActivity(i);
                finish();
            }
        });

        imgbtn_costsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),FeedbackCandidateList.class);
                i.putExtra("token",token);
                i.putExtra("login_id",candidateId);
                startActivity(i);
                finish();
            }
        });
    }
}
