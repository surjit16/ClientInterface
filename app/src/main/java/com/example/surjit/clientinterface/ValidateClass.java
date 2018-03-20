package com.example.surjit.clientinterface;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ValidateClass extends AsyncTask<String, Void, Boolean> {
    private Context context;
    private String hashKey;
    private ProgressDialog progressDialog;

    public ValidateClass(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Validating Hash Key ...");
        progressDialog.setTitle("On Pogress");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean s) {
        if (s) {
            progressDialog.dismiss();
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Validate HashKey");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("The User with HashKey "+hashKey+" exists.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Done", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    context.startActivity(new Intent(context, HomeActivity.class));
                }
            });

            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"Request Data", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    context.startActivity(new Intent(context, RequestActivity.class));
                }
            });
            alertDialog.show();
        } else {
            progressDialog.dismiss();

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Validate HashKey");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("The User with HashKey "+hashKey+"  does not exists.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    context.startActivity(new Intent(context, HomeActivity.class));
                }
            });
            alertDialog.show();
        }
    }

    @Override
    protected Boolean doInBackground(String... voids) {
        String url_select = "https://uidchain.000webhostapp.com/data.json";
        HttpHandler sh = new HttpHandler();
        hashKey = voids[0];

        String jsonStr = sh.makeServiceCall(url_select);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                JSONArray chain = jsonObj.getJSONArray("chain");
                for (int i = 1; i < chain.length(); i++) {
                    JSONObject c = chain.getJSONObject(i);

                    String hash = c.getString("hash");

                    if (hash.equals(hashKey)) {
                        Log.e("sass1111111a ", " " + hash);
                        return true;
                    }
                }
            } catch (final JSONException e) {

            }
        } else {

            Log.e("Error56 ", " " + jsonStr);
        }
        return false;
    }
}
