package com.example.book.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.book.databinding.ActivityCategoryAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {

    //view binding
    private ActivityCategoryAddBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

//    private ArrayList<ModelCategory> categoryArrayList;



    private static final String TAG = "CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setCanceledOnTouchOutside(false);

        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //handle click, begin upload category
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private String category = "";

    //status
//     private String status1 = "0";
//     int status = Integer.parseInt(status1);

    private void validateData() {

        //get data
        category = binding.edtCategory.getText().toString().trim();
        if (TextUtils.isEmpty(category)){
            Toast.makeText(this, "Vui lòng nhập danh mục sách...", Toast.LENGTH_SHORT).show();
        }
        else {
            ktAddCategoryFirebase();
        }
    }

    private void ktAddCategoryFirebase() {
        progressDialog.setMessage("Kiểm tra danh mục");
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.orderByChild("category").equalTo(category)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            progressDialog.dismiss();
                            Toast.makeText(CategoryAddActivity.this, "Danh mục đã có hay chưa có, " +
                                    "nếu đã có thì tạm dừng, còn chưa có thì sẽ xuống bước kế tiếp " +
                                    "và tạo danh mục mới cho bạn và đưa bạn về trang admin", Toast.LENGTH_LONG).show();
                        }
                        else {
                            addCategoryFirebase();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }



    private void addCategoryFirebase() {

        progressDialog.setMessage("Thêm danh mục");
        progressDialog.show();


        // id firebase tự sinh ra và ta lấy id đó xuống và lưu lại
        String id =  FirebaseDatabase.getInstance().getReference().push().getKey();
        //thời gian
        long timestamp = System.currentTimeMillis();

        //setup upload database
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" +id);
        hashMap.put("category", "" + category);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", "" + firebaseAuth.getUid());
//        hashMap.put("status", status);//status

        //Đưa lên database.... Database Root > Categories > catogoryId > category info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(""+id)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //success
                        Log.d(TAG, "onSuccess: Upload thành công: "+category);
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "Danh mục: "+category+ " upload thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CategoryAddActivity.this, DashboardAdminActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }


}