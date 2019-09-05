package com.devs.celtica.inkless.Publications;

import android.net.Uri;

/**
 * Created by celtica on 21/05/19.
 */

public class TrackForUpload extends Track {
    Uri audioFile;


    public TrackForUpload(String titre, Uri audioFile) {
        this.audioFile = audioFile;
        this.titre=titre;
    }

    public TrackForUpload() {
         this.titre=null;
         this.audioFile=null;

    }

    public boolean isValide(){
        return titre != null && audioFile != null;
    }


}
