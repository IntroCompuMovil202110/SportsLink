package com.movil.sportslink.controlador;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.movil.sportslink.R;
import com.movil.sportslink.adapters.EncuentrosAdapter;
import com.movil.sportslink.adapters.MiEncuentroAdapter;
import com.movil.sportslink.modelo.Encuentro;
import com.movil.sportslink.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

import static com.movil.sportslink.controlador.SignUpActivity.PATH_USERS;

public class EncuentrosUsuarioFragment extends Fragment {
    ListView listEncuentros;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    ArrayList<String> codigoEnc;
    ArrayList<Encuentro> encuentrosUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_encuentros_usuario, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        listEncuentros = view.findViewById(R.id.encuentrosUser);
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        encuentrosUsuario = new ArrayList<>();
        obtenerEncuentrosUsuario();
    }




    private void obtenerEncuentrosUsuario(){
        FirebaseUser user = mAuth.getCurrentUser();
        myRef = database.getReference(PATH_USERS + user.getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuario = snapshot.getValue(Usuario.class);
                 codigoEnc = usuario.getEncuentros();
                 if(usuario.getEncuentros() != null && !usuario.getEncuentros().isEmpty()){

                     obtenerObjectosEncuentro(new FirebaseCallBack() {
                         @Override
                         public void onCallBack(List<Encuentro> encuentros) {
                             if(encuentros != null && !encuentros.isEmpty()){

                                 MiEncuentroAdapter miEncuentroAdapter = new MiEncuentroAdapter((ArrayList<Encuentro>) encuentros,getContext());
                                 //EncuentrosAdapter miEncuentroAdapter2 = new EncuentrosAdapter((ArrayList<Encuentro>) encuentros,getContext());
                                 listEncuentros.setAdapter(miEncuentroAdapter);
                                 System.out.println(listEncuentros.toString());
                             }
                         }


                     });
                 }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void obtenerObjectosEncuentro(FirebaseCallBack firebaseCallBack){
        DatabaseReference encReference = database.getReference("encuentros/");
        encReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleSnapshot: snapshot.getChildren()){
                    Encuentro enc = singleSnapshot.getValue(Encuentro.class);
                    System.out.println("COMPARANDO " + enc.getId() + "CON ALGO EN " + codigoEnc.toString());
                    if(codigoEnc.contains(enc.getId())){
                        encuentrosUsuario.add(enc);
                    }
                }
                System.out.println("THE THING RETURN IS " + encuentrosUsuario.toString());
                firebaseCallBack.onCallBack(encuentrosUsuario);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FirebaseCallBack{
        void onCallBack(List<Encuentro> encuentros);
    }

}