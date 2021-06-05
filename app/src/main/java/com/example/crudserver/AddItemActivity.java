package com.example.crudserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.crudserver.constants.Constants;
import com.example.crudserver.model.Item;
import com.example.crudserver.model.Result;
import com.example.crudserver.service.APIService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddItemActivity extends AppCompatActivity {

    Button btnSubmit;

    EditText edName, edBrand, edPrice;


    private boolean isEdit = false;

    private Item item;
    private int position;

    private String tempName = "",
            tempBrand = "",
            tempPrice = "";

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        edName = findViewById(R.id.ed_name);
        edPrice = findViewById(R.id.ed_price);
        edBrand = findViewById(R.id.ed_brand);

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    editData();
                } else {
                    addNewData();

                }
            }

        });

        item = getIntent().getParcelableExtra("item");
        if (item != null) {
            position = getIntent().getIntExtra("position", 0);
            isEdit = true;
        } else {
//            item = new Item();
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit) {
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            if (item != null) {
                edName.setText(item.getName());
                edBrand.setText(item.getBrand());
                edPrice.setText("" + item.getPrice());

                tempPrice = "" + item.getPrice();
                tempName = item.getName();
                tempBrand = item.getBrand();
            }
        } else {
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSubmit.setText(btnTitle);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void addNewData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait.......");
        progressDialog.show();

        String name = edName.getText().toString().trim();
        String brand = edBrand.getText().toString().trim();
        Integer price = Integer.parseInt(edPrice.getText().toString().trim());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        final Call<Result> result = apiService.create(Constants.TOKEN,
                name,
                brand,
                price);
        result.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                Result jsonResult = response.body();
                Toast.makeText(AddItemActivity.this, jsonResult.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddItemActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void editData() {

        if (!checkItemData()) {
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ........");
        progressDialog.show();

        String name = edName.getText().toString().trim();
        String brand = edBrand.getText().toString().trim();
        Integer price = Integer.parseInt(edPrice.getText().toString().trim());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        final Call<Result> result = apiService.update(Constants.TOKEN, item.getId(), name, brand, price);

        result.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();

                Result jsonResult = response.body();
                Log.d("MainActivity", jsonResult.toString());

                Toast.makeText(AddItemActivity.this, jsonResult.getMessage(), Toast.LENGTH_LONG).show();

                finish();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = "Cancel";
            dialogMessage = "Do you want to cancel ?";
        } else {
            dialogMessage = "Are you sure to delete this item ?";
            dialogTitle = "Delete item";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            deleteItem(item.getId());
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void deleteItem(int id) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        final Call<Result> result = apiService.delete(Constants.TOKEN, id);

        result.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();

                Result jsonResult = response.body();

                Toast.makeText(AddItemActivity.this, jsonResult.getMessage(), Toast.LENGTH_LONG).show();

                finish();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }

    private boolean checkItemData() {
        boolean isOK = true;

        if (TextUtils.isEmpty(edName.getText().toString().trim())) {
            isOK = false;
            edName.setText("Please input item name");
        } else if (TextUtils.isEmpty(edBrand.getText().toString().trim())) {
            isOK = false;
            edBrand.setText("Please input item brand");

        } else if (TextUtils.isEmpty(edPrice.getText().toString().trim())) {
            isOK = false;
            edPrice.setText("Please input item price");
        }

        return isOK;
    }

}
