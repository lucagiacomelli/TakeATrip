package com.takeatrip.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.takeatrip.Activities.ProfiloActivity;
import com.takeatrip.AsyncTasks.LoadGenericImageTask;
import com.takeatrip.Classes.Profilo;
import com.takeatrip.R;
import com.takeatrip.Utilities.DataObject;
import com.takeatrip.Utilities.RoundedImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by david on 08/03/2016.
 */

public class RecyclerViewAdapterFollowing extends RecyclerView
        .Adapter<RecyclerViewAdapterFollowing
        .DataObjectHolder> {

    private static String TAG = "TEST RecAdaptFollowing";

    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;

    private Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView nomeUtente;
        TextView cognomeUtente;
        TextView usernameUtente;
        TextView sessoUtente;
        ImageView imageProfile;


        public DataObjectHolder(View itemView) {
            super(itemView);

            nomeUtente = (TextView) itemView.findViewById(R.id.NomeUtenteFollowing);
            //emailUtente = (TextView) itemView.findViewById(R.id.EmailUserFollowing);
            cognomeUtente = (TextView) itemView.findViewById(R.id.CognomeUtenteFollowing);
            usernameUtente = (TextView) itemView.findViewById(R.id.UsernameUtenteFollowing);
            imageProfile = (RoundedImageView) itemView.findViewById(R.id.ImageProfile);
            sessoUtente = (TextView) itemView.findViewById(R.id.SessoUtenteFollowing);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String username =  usernameUtente.getText().toString().replace("(","").replace(")","");
            Log.d(TAG, "username utente cliccato: " + username);

            for(DataObject object : mDataset){
                Profilo p = object.getProfilo();

                if(p.getUsername().equals(username)){

                    Log.i(TAG, "utente selezionato: " + p);

                    Intent openProfilo = new Intent(v.getContext(), ProfiloActivity.class);

                    //Here pass all the parameter and start the ProfiloActivity
                    openProfilo.putExtra("emailEsterno", p.getId());
                    openProfilo.putExtra("dateOfBirth", p.getDataNascita());
                    openProfilo.putExtra("name", p.getName());
                    openProfilo.putExtra("surname",p.getSurname());
                    openProfilo.putExtra("sesso", p.getSesso());
                    openProfilo.putExtra("username", username);
                    openProfilo.putExtra("lavoro", p.getLavoro());
                    openProfilo.putExtra("descrizione", p.getDescrizione());
                    openProfilo.putExtra("tipo", p.getTipo());
                    openProfilo.putExtra("urlImmagineProfilo", p.getIdImageProfile());
                    openProfilo.putExtra("urlImmagineCopertina", p.getGetIdImageCover());

                    try{
                        v.getContext().startActivity(openProfilo);
                    }
                    catch(Exception e){
                        Log.e(TAG, "Thrown exception " + e);
                    }

                    break;
                }
            }
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public RecyclerViewAdapterFollowing(ArrayList<DataObject> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_following, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.nomeUtente.setText(mDataset.get(position).getNomeFollow());
        holder.cognomeUtente.setText(mDataset.get(position).getCognomeFollow());
        holder.usernameUtente.setText("(" + mDataset.get(position).getUsernameFollow() + ")");

        String sesso = mDataset.get(position).getSessoFollow();

        //holder.sessoUtente.setText(mDataset.get(position).getSessoFollow());

        /*
        holder.emailUtente.setText(mDataset.get(position).getEmailFollow());
       holder.dataUtente.setText(mDataset.get(position).getDataNascitaFollow());
       holder.sessoUtente.setText(mDataset.get(position).getSessoFollow());
       holder.lavoroUtente.setText(mDataset.get(position).getLavoroFollow());
       holder.descrizioneUtente.setText(mDataset.get(position).getDescrizioneFollow());
       holder.tipoUtente.setText(mDataset.get(position).getTipoFollow());
       holder.nazionalitaUtente.setText(mDataset.get(position).getNazionalitaFollow());
       */

        String urlImmagine = mDataset.get(position).getUrlImmagineProfilo();

        ImageView immagineProfilo = holder.imageProfile;
        immagineProfilo.setContentDescription(urlImmagine);

        if (urlImmagine != null && !urlImmagine.equals("null")) {

            URL completeUrl = null;

            try {
                completeUrl = new LoadGenericImageTask(urlImmagine, context).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (completeUrl != null)
                Picasso.with(null).load(completeUrl.toString()).into(immagineProfilo);
        } else {
            if (sesso.equals("M")) {
                immagineProfilo.setImageResource(R.drawable.default_male);
            } else {
                immagineProfilo.setImageResource(R.drawable.default_female);
            }
        }


    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}