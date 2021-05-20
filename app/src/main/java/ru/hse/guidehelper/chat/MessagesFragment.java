package ru.hse.guidehelper.chat;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import de.hdodenhof.circleimageview.CircleImageView;
import ru.hse.guidehelper.MessageViewHolder;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.databinding.FragmentMessagesBinding;

import static android.app.Activity.RESULT_OK;

public class MessagesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentMessagesBinding mBinding;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private View view;
    private ImageButton mBackButton;

    private static Chat chat = null;
    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD = "messages";
    public static final String ANONYMOUS = "anonymous";
    private static final int REQUEST_IMAGE = 2;

    public static void setChat(Chat chat) {
        MessagesFragment.chat = chat;
    }

    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("== 101 ==");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        System.out.println("== 102 ==");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("== 103 ==");
        // подгружать мессаги не из Firebase, а из своей БД
        mBinding = FragmentMessagesBinding.inflate(inflater, container, false);
        view = mBinding.getRoot();

        CircleImageView companionAvatar = view.findViewById(R.id.companionAvatar);
        TextView companionName = view.findViewById(R.id.companionName);
        Glide.with(MessagesFragment.this.getContext()).load(chat.getDialogPhoto()).into(companionAvatar);
        companionName.setText(chat.getUsers().get(0).getName());

        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child(MESSAGES_CHILD).child(chat.getId()).push();
        DatabaseReference messagesRef = mDatabase.getReference().child(MESSAGES_CHILD).child(chat.getId());
        System.out.println(mDatabase.getReference().child(MESSAGES_CHILD).child(chat.getId()).get());


        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>().setQuery(messagesRef, Message.class).build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                return new MessageViewHolder(inflater.inflate(R.layout.message, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(MessageViewHolder viewHolder, int position, Message message) {
                viewHolder.bindMessage(message);
            }
        };

        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mBinding.messageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBinding.messageRecyclerView.setAdapter(mFirebaseAdapter);
        mFirebaseAdapter.registerAdapterDataObserver(new MyScrollToBottomObserver(mBinding.messageRecyclerView, mFirebaseAdapter, mLinearLayoutManager));
        mBinding.messageEditText.addTextChangedListener(new MyButtonObserver(mBinding.sendButton));
        mBinding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(mBinding.messageEditText.getText().toString(), getUserName(), getUserPhotoUrl());
                mDatabase.getReference().child(MESSAGES_CHILD).child(chat.getId()).push().setValue(message);
                chat.setLastMessage(message);
                // отправить мессагу в БД
                mBinding.messageEditText.setText("");
            }
        });
        mBinding.addMessageImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE);
        });

        mBackButton = view.findViewById(R.id.backButton);
        mBackButton.setOnClickListener(view -> MessagesFragment.this.requireActivity().onBackPressed());

        System.out.println("== 104 ==");

        return view;
    }

    @Override
    public void onPause() {
        System.out.println("1");
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        System.out.println("2");
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        System.out.println("3");
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                final Uri uri = data.getData();
                Log.d(TAG, "Uri: " + uri.toString());
                final FirebaseUser user = mFirebaseAuth.getCurrentUser();
                Message tempMessage = new Message(null, getUserName(), getUserPhotoUrl());
                mDatabase.getReference().child(MESSAGES_CHILD).push()
                        .setValue(tempMessage, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
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
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference
                .putFile(uri)
                .addOnSuccessListener((Activity) view.getContext(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Message message = new Message(null, getUserName(), getUserPhotoUrl());
                                        mDatabase.getReference().child(MESSAGES_CHILD).child(key).setValue(message);
                                    }
                                });
                    }
                })
                .addOnFailureListener((Activity) view.getContext(),
                        e -> Log.w(TAG, "Image upload task was not successful.", e));
    }

    @Nullable
    private String getUserPhotoUrl() {
        System.out.println("4");
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            return user.getPhotoUrl().toString();
        }
        return null;
    }

    private String getUserName() {
        System.out.println("5");
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return ANONYMOUS;
    }
}
