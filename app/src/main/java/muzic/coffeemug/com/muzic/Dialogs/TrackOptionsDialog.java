package muzic.coffeemug.com.muzic.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;


import java.io.File;

import muzic.coffeemug.com.muzic.Utilities.AppConstants;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Utilities.MuzicApplication;

/**
 * Created by aditya on 09/09/15.
 */
public class TrackOptionsDialog {

    private String arrOptions[] = {"Share", "Delete"};
    private AlertDialog.Builder mBuilder;
    private Track mTrack;
    private Context mContext;
    private boolean isDataAvailable = true;
    private ResultReceiver mResultReceiver;
    private MuzicApplication muzicApplication;


    public TrackOptionsDialog(Track track, Context context, ResultReceiver resultReceiver) {

        this.mTrack = track;
        this.mContext = context;
        this.mResultReceiver = resultReceiver;
        muzicApplication = MuzicApplication.getInstance();
        if (TextUtils.isEmpty(mTrack.getData())) {
            isDataAvailable = false;
        }

        mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(track.getTitle());
        mBuilder.setItems(arrOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case 0: {
                        shareTrack();
                        break;
                    }
                    case 1: {
                        deleteTrack();
                        break;
                    }
                }
            }
        });

    }


    private void deleteTrack() {

        if (isDataAvailable) {

            new AlertDialog.Builder(mContext)
                    .setTitle(mTrack.getTitle())
                    .setMessage("Are you sure you want to delete this song ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File(mTrack.getData());
                            if (file.exists()) {

                                if (file.delete()) {
                                    mContext.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                            MediaStore.MediaColumns.DATA + "='" + mTrack.getData() + "'", null);

                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable(AppConstants.DELETED_TRACK, mTrack);
                                    mResultReceiver.send(Activity.RESULT_OK, bundle);
                                    return;
                                }
                            }

                            MuzicApplication.getInstance().showToast("Error occurred while deleting File", mContext);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }


    private void shareTrack() {

        if (isDataAvailable) {
            muzicApplication.shareTrack(mContext, mTrack);
        }
    }


    public void show() {
        if (null != mBuilder) {
            mBuilder.show();
        }
    }

}
