package activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.bluebase.activities.R;

public class NewEnquiryForm extends AppCompatActivity {
    EditText ed_client,ed_address,ed_city,ed_clientname,ed_designation,ed_contactno,ed_emailid,ed_service,ed_feedback,ed_followupdate;
    Spinner spin_clienttype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_enquiry_form);
    }
}
