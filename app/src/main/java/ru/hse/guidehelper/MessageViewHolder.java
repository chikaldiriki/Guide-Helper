package ru.hse.guidehelper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.hse.guidehelper.model.Message;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "MessageViewHolder";

    TextView messageLeftTextView, messageRightTextView;
    ImageView messageLeftImageView, messageRightImageView;
    TextView messageLeftTime, messageRightTime;
    FlexboxLayout leftBubble, rightBubble;

    public MessageViewHolder(View v) {
        super(v);
        messageLeftTextView = itemView.findViewById(R.id.messageLeftTextView);
        messageLeftImageView = itemView.findViewById(R.id.messageLeftImageView);
        messageLeftTime = itemView.findViewById(R.id.leftMessageTime);
        messageRightTextView = itemView.findViewById(R.id.messageRightTextView);
        messageRightImageView = itemView.findViewById(R.id.messageRightImageView);
        messageRightTime = itemView.findViewById(R.id.rightMessageTime);
        leftBubble = itemView.findViewById(R.id.leftBubble);
        rightBubble = itemView.findViewById(R.id.rightBubble);
    }

    public void bindMessage(Message message) {
        System.out.println(message.getDispatchTime());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (message.getName().equals(user.getEmail())) {
            messageRightTime.setText(message.getDispatchTimeString());
            leftBubble.setVisibility(FlexboxLayout.INVISIBLE);
            rightBubble.setVisibility(FlexboxLayout.VISIBLE);
            if (message.getText() != null) {
                messageRightTextView.setText(message.getText());
            } else if (message.getImageUrl() != null) {
                Glide.with(messageRightImageView.getContext()).load(message.getImageUrl()).into(messageRightImageView);
            }
        } else {
            messageLeftTime.setText(message.getDispatchTimeString());
            leftBubble.setVisibility(FlexboxLayout.VISIBLE);
            rightBubble.setVisibility(FlexboxLayout.INVISIBLE);
            if (message.getText() != null) {
                messageLeftTextView.setText(message.getText());
            } else if (message.getImageUrl() != null) {
                Glide.with(messageLeftImageView.getContext()).load(message.getImageUrl()).into(messageLeftImageView);
            }
        }
    }
}
