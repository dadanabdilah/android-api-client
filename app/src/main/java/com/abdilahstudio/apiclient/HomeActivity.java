package com.abdilahstudio.apiclient;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdilahstudio.apiclient.adapter.UserAdapter;
import com.abdilahstudio.apiclient.model.ApiResponse;
import com.abdilahstudio.apiclient.model.User;
import com.abdilahstudio.apiclient.response.ApiClient;
import com.abdilahstudio.apiclient.response.ApiService;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {
    private EditText editTextTglLahir;
    private int mYear, mMonth, mDay;
    private static final int PICK_PHOTO_REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private final List<User> userList = new ArrayList<>();

    private Uri selectedPhotoUri;

    private View dialogAddView, dialogUpdateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(userAdapter);

        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });

        ApiClient.init(this);
        fetchUsers();
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add User");
        dialogAddView = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        final EditText editTextName = dialogAddView.findViewById(R.id.editTextName);
        final EditText editTextEmail = dialogAddView.findViewById(R.id.editTextEmail);
        final EditText editTextTglLahir = dialogAddView.findViewById(R.id.editTglLahir);

        editTextTglLahir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog(editTextTglLahir);
                }
            }
        });

        editTextTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextTglLahir);
            }
        });

        Button buttonSelectPhoto = dialogAddView.findViewById(R.id.buttonSelectPhoto);
        buttonSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });

        builder.setView(dialogAddView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String tglLahir = editTextTglLahir.getText().toString();
                addUser(name, email, tglLahir);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    public void showUpdateDialog(final User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update User");

        // Ensure the layout is correctly inflated
        dialogUpdateView = getLayoutInflater().inflate(R.layout.dialog_update_user, null);

        // Ensure dialogUpdateView is not null before accessing its children
        if (dialogUpdateView != null) {
            final EditText inputName = dialogUpdateView.findViewById(R.id.editTextName);
            final EditText inputEmail = dialogUpdateView.findViewById(R.id.editTextEmail);
            final EditText inputTglLahir = dialogUpdateView.findViewById(R.id.editTglLahir);
            final ImageView imageViewPhoto = dialogUpdateView.findViewById(R.id.imageViewPhoto);

            // Ensure all view references are not null
            if (inputName != null && inputEmail != null && inputTglLahir != null && imageViewPhoto != null) {
                inputName.setText(user.getName());
                inputEmail.setText(user.getEmail());
                inputTglLahir.setText(user.getTglLahir());

                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(imageViewPhoto);

                inputTglLahir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            showDatePickerDialog(inputTglLahir);
                        }
                    }
                });

                inputTglLahir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog(inputTglLahir);
                    }
                });

                Button buttonSelectPhoto = dialogUpdateView.findViewById(R.id.buttonSelectPhoto);
                buttonSelectPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPhoto();
                    }
                });

                builder.setView(dialogUpdateView);
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputName.getText().toString();
                        String email = inputEmail.getText().toString();
                        String tglLahir = inputTglLahir.getText().toString();
                        updateUser(user.getId(), name, email, tglLahir);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();
            } else {
                Log.e("showUpdateDialog", "One or more view references are null");
            }
        } else {
            Log.e("showUpdateDialog", "Failed to inflate dialog_update_user layout");
        }
    }

    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedPhotoUri = data.getData();
            try {
                File file = new File(getRealPathFromURI(selectedPhotoUri));
                if (dialogAddView != null) {
                    Glide.with(this).load(file).into((ImageView) dialogAddView.findViewById(R.id.imageViewPhoto));
                } else if (dialogUpdateView != null) {
                    Glide.with(this).load(file).into((ImageView) dialogUpdateView.findViewById(R.id.imageViewPhoto));
                }
            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addUser(String name, String email, String tglLahir) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        MultipartBody.Part photoPart = null;
        if (selectedPhotoUri != null) {
            try {
                File file = new File(getRealPathFromURI(selectedPhotoUri));
                RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedPhotoUri)), file);
                photoPart = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        RequestBody namePart = RequestBody.create(MultipartBody.FORM, name);
        RequestBody emailPart = RequestBody.create(MultipartBody.FORM, email);
        RequestBody tglLahirPart = RequestBody.create(MultipartBody.FORM, tglLahir);

        Call<ApiResponse> call = apiService.insertUser(namePart, emailPart, photoPart, tglLahirPart);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(HomeActivity.this, "Success : " + message, Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    String message = response.body().getMessage();
                    Toast.makeText(HomeActivity.this, "Failed : " + message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Failed to add user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser(int id, String name, String email, String tglLahir) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        MultipartBody.Part photoPart = null;
        if (selectedPhotoUri != null) {
            try {
                File file = new File(getRealPathFromURI(selectedPhotoUri));
                RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedPhotoUri)), file);
                photoPart = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        RequestBody idPart = RequestBody.create(MultipartBody.FORM, String.valueOf(id));
        RequestBody namePart = RequestBody.create(MultipartBody.FORM, name);
        RequestBody emailPart = RequestBody.create(MultipartBody.FORM, email);
        RequestBody tglLahirPart = RequestBody.create(MultipartBody.FORM, tglLahir);

        Call<ApiResponse> call = apiService.updateUser(idPart, namePart, emailPart, photoPart, tglLahirPart);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    String message = response.body().getMessage();
                    Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Failed to update user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteUser(User user) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        RequestBody idPart = RequestBody.create(MultipartBody.FORM, String.valueOf(user.getId()));
        Call<ApiResponse> call = apiService.deleteUser(idPart);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                    fetchUsers(); // Refresh the list
                } else {
                    String message = response.body() != null ? response.body().getMessage() : "Failed to delete user";
                    Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUsers() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<User>> call = apiService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body());
                    userAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }
}