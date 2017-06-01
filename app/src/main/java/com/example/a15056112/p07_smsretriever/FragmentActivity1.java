package com.example.a15056112.p07_smsretriever;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentActivity1 extends Fragment {

    Button btnRetrieveNumber;
    TextView tvNumber;
    EditText etNumber;
    Button btnEmail;
    String emailSMS;

    public FragmentActivity1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_activity1, container, false);

        tvNumber =  (TextView) view.findViewById(R.id.textViewNumber);
        btnRetrieveNumber = (Button) view.findViewById(R.id.btnRetrieveSmsNumber);
        etNumber = (EditText) view.findViewById(R.id.editTextNumber);
        btnEmail = (Button) view.findViewById(R.id.buttonEmail);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jason_lim@rp.edu.sg"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Email from SMS");
                email.putExtra(Intent.EXTRA_TEXT, emailSMS);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email,
                        "Choose an Email client :"));
            }
        });

        btnRetrieveNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionCheck = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);

                if (permissionCheck != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, 0);
                    // stops the action from proceeding further as permission not
                    //  granted yet
                    return;
                }

                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"date","address", "body", "type"};

                ContentResolver cr = getActivity().getContentResolver();

                String filter = "address LIKE ?";
                String[] filterArgs = {"%" + etNumber.getText().toString() + "%"};

                Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);
                String smsBody = "";

                if (cursor.moveToFirst()) {
                    do {
                        long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat
                                .format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox: ";
                        } else {
                            type = "Sent: ";
                        }

                        smsBody += type + " " + address + "\n at " + date
                                + "\n\"" + body + "\"\n\n";

                    } while (cursor.moveToNext());
                }

                tvNumber.setText(smsBody);
                emailSMS = smsBody;
            }
        });

        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the read SMS
                    //  as if the btnRetrieve is clicked
                    btnRetrieveNumber.performClick();

                } else {
                    // permission denied... notify user
                    Toast.makeText(getActivity(), "Permission not granted",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
