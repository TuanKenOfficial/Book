package com.example.book.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.Adapter.AdapterPdfUser;
import com.example.book.Model.book.ModelPdf;
import com.example.book.databinding.FragmentBooksUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BooksUsersFragment extends Fragment {

    private FragmentBooksUserBinding binding;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private ArrayList<ModelPdf> pdfArrayList;
    public AdapterPdfUser adapterPdfUser;

    public BooksUsersFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBooksUserBinding.inflate(LayoutInflater.from(getContext()),container, false);

        //auth
        mAuth = FirebaseAuth.getInstance();
        //recycleview
        recyclerView = binding.booksRv ;
        pdfArrayList = new ArrayList<>();
        adapterPdfUser = new AdapterPdfUser(getContext(),pdfArrayList);
        recyclerView.setAdapter(adapterPdfUser);


        loadPdfUserDownloads("downloadsCount");


        return binding.getRoot();
    }

    private void loadPdfUserDownloads(String downloadsCount) {
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.orderByChild(downloadsCount).limitToLast(10) // load 10 lượt xem sách hoặc nhiều hơn
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pdfArrayList.clear();

                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelPdf model = ds.getValue(ModelPdf.class);

                            pdfArrayList.add(model);
                        }
                        adapterPdfUser = new AdapterPdfUser(getContext(), pdfArrayList);
//                adapterPdfUser.notifyDataSetChanged();
                        binding.booksRv.setAdapter(adapterPdfUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}