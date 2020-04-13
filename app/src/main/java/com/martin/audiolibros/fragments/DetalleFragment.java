package com.martin.audiolibros.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.martin.audiolibros.Aplicacion;
import com.martin.audiolibros.Libro;
import com.martin.audiolibros.R;
import com.martin.audiolibros.Servicioaudio;

import java.io.IOException;

public class DetalleFragment extends Fragment implements
        View.OnTouchListener, MediaPlayer.OnPreparedListener,
        MediaController.MediaPlayerControl {
    public static String ARG_ID_LIBRO = "id_libro";
    Libro libroDatos;
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    @Override public View onCreateView(LayoutInflater inflador, ViewGroup
            contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_detalle,
                contenedor, false);
        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt(ARG_ID_LIBRO);
            ponInfoLibro(position, vista);
        } else {
            ponInfoLibro(0, vista);
        }
        return vista;
    }
    private void ponInfoLibro(int id, View vista) {
        Libro libro = ((Aplicacion) getActivity().getApplication())
                .getVectorLibros().elementAt(id);
        ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView) vista.findViewById(R.id.portada))
                .setImageResource(libro.recursoImagen);
        vista.setOnTouchListener(this);

       libroDatos=libro;

        Toast.makeText(getActivity(),libroDatos.urlAudio,Toast.LENGTH_LONG).show();
        Intent servicio = new Intent(getActivity(), Servicioaudio.class);
        servicio.putExtra("entrada",libroDatos.urlAudio);
        servicio.putExtra("titulo",libroDatos.titulo);
        servicio.putExtra("imagen",libroDatos.recursoImagen);
        getActivity().startService(servicio);

        super.onDestroy();



    }
    public void ponInfoLibro(int id) {
        ponInfoLibro(id, getView());
    }
    @Override public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("Audiolibros", "Entramos en onPrepared de MediaPlayer");
        mediaPlayer.start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getView().findViewById(
                R.id.fragment_detalle));
        mediaController.setEnabled(true);
        mediaController.show();
    }
    @Override public boolean onTouch(View vista, MotionEvent evento) {
        mediaController.show();
        return false;
    }
    @Override public void onStop() {
        mediaController.hide();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d("Audiolibros", "Error en mediaPlayer.stop()");
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {

        Toast.makeText(getActivity(),libroDatos.urlAudio,Toast.LENGTH_LONG).show();
        Intent servicio = new Intent(getActivity(), Servicioaudio.class);
        servicio.putExtra("entrada",libroDatos.urlAudio);
        servicio.putExtra("titulo",libroDatos.titulo);
        servicio.putExtra("imagen",libroDatos.recursoImagen);
        getActivity().startService(servicio);

        super.onDestroy();
    }

    @Override public boolean canPause() {
        return true;
    }
    @Override public boolean canSeekBackward() {
        return true;
    }
    @Override public boolean canSeekForward() {
        return true;
    }
    @Override public int getBufferPercentage() {
        return 0;
    }
    @Override public int getCurrentPosition() {
        try {
            return mediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            return 0;
        }
    }
    @Override public int getDuration() {
        return mediaPlayer.getDuration();
    }
    @Override public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
    @Override public void pause() {
        mediaPlayer.pause();
    }
    @Override public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }
    @Override public void start() {
        mediaPlayer.start();
    }
    @Override public int getAudioSessionId() {
        return 0;
    }
}