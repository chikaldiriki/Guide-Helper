package ru.hse.guidehelper;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.hse.guidehelper.chat.Message;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "MessageViewHolder";

    TextView messageLeftTextView, messageRightTextView;
    ImageView messageLeftImageView, messageRightImageView;

    public MessageViewHolder(View v) {
        super(v);
        messageLeftTextView = (TextView) itemView.findViewById(R.id.messageLeftTextView);
        messageLeftImageView = (ImageView) itemView.findViewById(R.id.messageLeftImageView);
        messageRightTextView = (TextView) itemView.findViewById(R.id.messageRightTextView);
        messageRightImageView = (ImageView) itemView.findViewById(R.id.messageRightImageView);
    }

    public void bindMessage(Message message) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (message.getName().equals(user.getEmail())) {
            messageLeftTextView.setVisibility(TextView.INVISIBLE);
            messageLeftImageView.setVisibility(ImageView.INVISIBLE);
            if (message.getText() != null) {
                messageRightTextView.setText(message.getText());
                messageRightTextView.setVisibility(TextView.VISIBLE);
                messageRightImageView.setVisibility(ImageView.GONE);
            } else if (message.getImageUrl() != null) {
                String imageUrl = message.getImageUrl();
                if (imageUrl.startsWith("gs://")) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    Glide
                                            .with(messageRightImageView.getContext())
                                            .load(downloadUrl)
                                            .into(messageRightImageView);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Getting download url was not successful.", e);
                                }
                            });
                } else {
                    Glide.with(messageRightImageView.getContext()).load(message.getImageUrl()).into(messageRightImageView);
                }
                messageRightImageView.setVisibility(ImageView.VISIBLE);
                messageRightTextView.setVisibility(TextView.GONE);
            }
        } else {
            messageRightTextView.setVisibility(TextView.INVISIBLE);
            messageRightImageView.setVisibility(ImageView.INVISIBLE);
            if (message.getText() != null) {
                messageLeftTextView.setText(message.getText());
                messageLeftTextView.setVisibility(TextView.VISIBLE);
                messageLeftImageView.setVisibility(ImageView.GONE);
            } else if (message.getImageUrl() != null) {
                String imageUrl = message.getImageUrl();
                if (imageUrl.startsWith("gs://")) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    Glide
                                            .with(messageLeftImageView.getContext())
                                            .load(downloadUrl)
                                            .into(messageLeftImageView);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Getting download url was not successful.", e);
                                }
                            });
                } else {
                    Glide.with(messageLeftImageView.getContext()).load(message.getImageUrl()).into(messageLeftImageView);
                }
                messageLeftImageView.setVisibility(ImageView.VISIBLE);
                messageLeftTextView.setVisibility(TextView.GONE);
            }
        }
    }
}
