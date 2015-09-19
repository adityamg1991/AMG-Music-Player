package muzic.coffeemug.com.muzic.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;

import muzic.coffeemug.com.muzic.Utilities.Constants;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Utilities.MuzicApplication;
import muzic.coffeemug.com.muzic.R;

/**
 * Created by aditya on 09/09/15.
 */
public class TrackOptionsDialog {

    private String arrOptions[] = {"Share", "Delete"};
    private MaterialDialog.Builder mBuilder;
    private Track mTrack;
    private Context mContext;
    private boolean isDataAvailable = true;
    private ResultReceiver mResultReceiver;


    public TrackOptionsDialog(Track track, Context context, ResultReceiver resultReceiver) {

        this.mTrack = track;
        this.mContext = context;
        this.mResultReceiver = resultReceiver;
        if(TextUtils.isEmpty(mTrack.getData())) {
            isDataAvailable = false;
        }

        mBuilder= new MaterialDialog.Builder(context);
        mBuilder.title(track.getTitle());
        mBuilder.items(arrOptions);
        mBuilder.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View view,
                                    int which, CharSequence text) {
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

        if(isDataAvailable) {

            new MaterialDialog.Builder(mContext)
                    .title(mTrack.getTitle())
                    .content("Are you sure you want to delete this song ?")
                    .positiveText("Yes")
                    .positiveColorRes(R.color.app_theme)
                    .negativeText("No")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {

                            File file = new File(mTrack.getData());
                            if (file.exists()) {

                                if (file.delete()) {
                                    mContext.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                            MediaStore.MediaColumns.DATA + "='" + mTrack.getData() + "'", null);

                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable(Constants.DELETED_TRACK, mTrack);
                                    mResultReceiver.send(Activity.RESULT_OK, bundle);
                                    return;
                                }
                            }

                            MuzicApplication.getInstance().showToast("Error occurred while deleting File", mContext);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }


    private void shareTrack() {

        if(isDataAvailable) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("audio/*");
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + mTrack.getData()));
            mContext.startActivity(Intent.createChooser(share, "Share " + mTrack.getTitle()));
        }
    }


    public void show() {
        if(null != mBuilder) {
            mBuilder.show();
        }
    }

}
