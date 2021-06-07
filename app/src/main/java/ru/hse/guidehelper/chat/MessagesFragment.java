package ru.hse.guidehelper.chat;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.hse.guidehelper.MessageViewHolder;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.databinding.FragmentMessagesBinding;
import ru.hse.guidehelper.model.Chat;
import ru.hse.guidehelper.model.Message;

import static android.app.Activity.RESULT_OK;

public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding mBinding;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private View view;

    private static Chat chat = null;
    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD = "messages";
    public static final String ANONYMOUS = "anonymous";
    private static final int REQUEST_IMAGE = 2;

    public static void setChat(Chat chat) {
        MessagesFragment.chat = chat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMessagesBinding.inflate(inflater, container, false);
        view = mBinding.getRoot();

        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
        navView.setVisibility(View.GONE);

        CircleImageView companionAvatar = view.findViewById(R.id.companionAvatar);
        TextView companionName = view.findViewById(R.id.companionName);
        if (chat.getDialogPhoto() == null) {
            Glide.with(MessagesFragment.this.requireContext()).load(R.drawable.ic_account_circle_black_36dp).into(companionAvatar);
        } else {
            Glide.with(MessagesFragment.this.requireContext()).load(chat.getDialogPhoto()).into(companionAvatar);
        }
        companionName.setText(chat.getUsers().get(0).getName());

        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child(MESSAGES_CHILD).child(chat.getId()).push();
        DatabaseReference messagesRef = mDatabase.getReference().child(MESSAGES_CHILD).child(chat.getId());

        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>().setQuery(messagesRef, Message.class).build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @NotNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                return new MessageViewHolder(inflater.inflate(R.layout.message, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NotNull MessageViewHolder viewHolder, int position, @NotNull Message message) {
                viewHolder.bindMessage(message);
            }
        };

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mLinearLayoutManager.setStackFromEnd(true);

        mBinding.messageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBinding.messageRecyclerView.setAdapter(mFirebaseAdapter);
        mFirebaseAdapter.registerAdapterDataObserver(new MyScrollToBottomObserver(mBinding.messageRecyclerView, mFirebaseAdapter, mLinearLayoutManager));
        mBinding.messageEditText.addTextChangedListener(new MyButtonObserver(mBinding.sendButton));

        mBinding.sendButton.setOnClickListener(view -> {
            Message message = new Message(mBinding.messageEditText.getText().toString(), getUserMail(), getUserPhotoUrl());
            mDatabase.getReference().child(MESSAGES_CHILD).child(chat.getId()).push().setValue(message);
            mBinding.messageEditText.setText("");
        });

        mBinding.addMessageImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE);
        });

        ImageButton mBackButton = view.findViewById(R.id.backButton);
        mBackButton.setOnClickListener(view -> {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.requireView().getWindowToken(), 0);
            MessagesFragment.this.requireActivity().onBackPressed();
        });

        return view;
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            final Uri uri = data.getData();
            Log.d(TAG, "Uri: " + uri.toString());
            final FirebaseUser user = mFirebaseAuth.getCurrentUser();
            Message tempMessage = new Message(null, getUserMail(), getUserPhotoUrl());
            mDatabase.getReference().child(MESSAGES_CHILD).push()
                    .setValue(tempMessage, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, @NotNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.w(TAG, "Unable to write message to database.", databaseError.toException());
                                return;
                            }
                            String key = databaseReference.getKey();
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference(user.getUid()).child(key).child(uri.getLastPathSegment());
                            putImageInStorage(storageReference, uri, key);
                        }
                    });
        }
    }*/

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference
                .putFile(uri)
                .addOnSuccessListener((Activity) view.getContext(), taskSnapshot -> Objects
                        .requireNonNull(Objects
                                .requireNonNull(taskSnapshot.getMetadata())
                                .getReference())
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri1) {
                                Message message = new Message(null, getUserMail(), getUserPhotoUrl());
                                mDatabase.getReference().child(MESSAGES_CHILD).child(key).setValue(message);
                            }
                        }))
                .addOnFailureListener((Activity) view.getContext(),
                        e -> Log.w(TAG, "Image upload task was not successful.", e));
    }

    @Nullable
    private String getUserPhotoUrl() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            return user.getPhotoUrl().toString();
        }
        return null;
    }

    private String getUserMail() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return ANONYMOUS;
    }
}
