package activities;

import android.content.DialogInterface;
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
    String token,login_id;
    boolean networkAvailability=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        login_id=i.getStringExtra("login_id");

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
                i.putExtra("login_id",login_id);
                startActivity(i);
                finish();
            }
        });

        imgbtn_feedbacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),FeedbackCandidateList.class);
                i.putExtra("token",token);
                i.putExtra("login_id",login_id);
                startActivity(i);
                finish();
            }
        });

        imgbtn_costsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),CRMMenu.class);
                i.putExtra("token",token);
                i.putExtra("login_id",login_id);
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

//    @Override
//    public void onBackPressed() {
//        Intent MainActivity = new Intent(getBaseContext(), LoginScreen.class);
//        MainActivity.addCategory(Intent.CATEGORY_HOME);
//        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(MainActivity);
//        MenuScreen.this.finish();
//    }


    private void displayToast() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MenuScreen.this);
        builder.setTitle("Confirmation");
        builder.setIcon(R.drawable.stop_sign);
        builder.setMessage("Are you sure to logout")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i=new Intent(getApplicationContext(),LoginScreen.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {

        displayToast();
    }

}
