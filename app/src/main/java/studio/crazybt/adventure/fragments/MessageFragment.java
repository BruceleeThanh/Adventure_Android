package studio.crazybt.adventure.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.one.EmojiOneProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.MessageListAdapter;
import studio.crazybt.adventure.helpers.ConvertTimeHelper;
import studio.crazybt.adventure.helpers.DrawableHelper;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.Conversation;
import studio.crazybt.adventure.models.Message;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.JsonUtil;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.RealmUtils;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

/**
 * Created by Brucelee Thanh on 03/10/2016.
 */

public class MessageFragment extends Fragment {

    private View rootView;

    // Toolbar
    @BindView(R.id.tbMessage)
    Toolbar tbMessage;
    @BindView(R.id.ivBackMessage)
    ImageView ivBackMessage;
    @BindView(R.id.civProfileImage)
    CircleImageView civProfileImage;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.tvStatusMessage)
    TextView tvStatusMessage;
    @BindView(R.id.ivInfoMessage)
    ImageView ivInfoMessage;

    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    @BindView(R.id.ivEmoticon)
    ImageView ivEmoticon;
    @BindView(R.id.eetMessage)
    EmojiEditText eetMessage;
    @BindView(R.id.ivSendMessage)
    ImageView ivSendMessage;

    private EmojiPopup emojiPopup;

    private MessageListAdapter messageAdapter;
    private List<Message> lstMessages;

    private Conversation conversation;
    private User partner;
    private String idUser;
    private String token;
    private String partnerSocketId;

    private AdventureRequest adventureRequest;
    private Socket socket;

    private boolean isOpened = false;
    private static final int TYPING_TIMER_LENGTH = 600;
    private boolean isTyping = false;
    private Handler typingHandler = new Handler();

    public static MessageFragment newInstance(User partner) {
        Bundle args = new Bundle();
        args.putParcelable(ApiConstants.KEY_PARTNER, partner);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            EmojiManager.install(new EmojiOneProvider());
            rootView = inflater.inflate(R.layout.fragment_message, container, false);
        }
        loadInstance();
        initSocket();
        initControls();
        initEvents();
        initActionBar();
        setupPopUpEmoji();
        initMessageList();
        conversationInitializeRequest();
        return rootView;
    }


    private void loadInstance() {
        if (getArguments() != null) {
            partner = getArguments().getParcelable(ApiConstants.KEY_PARTNER);
        }
    }

    private void initSocket() {
        RealmUtils application = (RealmUtils) getActivity().getApplication();
        socket = application.getSocket();
        socket.on(ApiConstants.SOCKET_JOIN_ROOM, onJoinRoom);
        socket.on(ApiConstants.SOCKET_LEAVE_ROOM, onLeaveRoom);
        socket.on(ApiConstants.SOCKET_NEW_MESSAGE, onNewMessage);
        socket.on(ApiConstants.SOCKET_TYPING, onTyping);
        socket.on(ApiConstants.SOCKET_STOP_TYPING, onStopTyping);
        socket.connect();
    }


    private void initControls() {
        ButterKnife.bind(this, rootView);
        conversation = new Conversation();
        idUser = SharedPref.getInstance(getContext()).getString(ApiConstants.KEY_ID, null);
        token = SharedPref.getInstance(getContext()).getAccessToken();
    }

    private void initEvents() {
        setKeyboardListener();
    }

    private void initActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbMessage);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        }
        setHasOptionsMenu(true);

        StringUtil.setText(tvProfileName, partner.getFullName());
        PicassoHelper.execPicasso_ProfileImage(getContext(), partner.getAvatar(), civProfileImage);
    }

    private void initMessageList() {
        lstMessages = new ArrayList<>();
        rvMessage.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter = new MessageListAdapter(rootView.getContext(), lstMessages, partner.getLastName());
        rvMessage.setAdapter(messageAdapter);
    }

    @OnClick(R.id.ivSendMessage)
    protected void onSendMessageClick() {
        if (!StringUtil.isEmpty(eetMessage)) {
            String content = eetMessage.getText().toString();
            lstMessages.add(new Message(null, conversation.getId(), idUser, content, new Date()));
            messageAdapter.notifyItemInserted(lstMessages.size() - 1);
            rvMessage.smoothScrollToPosition(lstMessages.size() - 1);
            emitChatToRoom(content);
            eetMessage.setText("");
            isTyping = false;
        }
    }

    @OnFocusChange(R.id.eetMessage)
    protected void onMessageFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            rvMessage.scrollToPosition(lstMessages.size() - 1);
        }
    }

    @OnClick(R.id.ivEmoticon)
    protected void onEmoticonClick() {
        emojiPopup.toggle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick(R.id.ivBackMessage)
    protected void onBackMessageClick() {
        emitLeaveRoom();
        ConversationFragment conversationFragment = (ConversationFragment) FragmentController.getFragment(getActivity(), ConversationFragment.class.getName());
        conversationFragment.conversationRequest();
        getActivity().onBackPressed();
    }

    @OnTextChanged(R.id.eetMessage)
    protected void onMessageTextChanged(CharSequence s, int start, int before, int count) {
        if (!isTyping) {
            isTyping = true;
            JSONObject data = new JSONObject();
            try {
                data.put(ApiConstants.KEY_ID_CONVERSATION, conversation.getId());
                data.put(ApiConstants.KEY_ID_USER, idUser);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit(ApiConstants.SOCKET_TYPING, data.toString());
        }

        typingHandler.removeCallbacks(onTypingTimeout);
        typingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
    }

    public void setKeyboardListener() {
        final View activityRootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // keyboard show
                    if (!isOpened) {
                        rvMessage.scrollToPosition(lstMessages.size() - 1);
                    }
                    isOpened = true;
                } else if (isOpened) {
                    isOpened = false;
                }
            }
        });
    }

    private void conversationInitializeRequest() {
        adventureRequest = new AdventureRequest(getContext(), Request.Method.POST, ApiConstants.getUrl(ApiConstants.API_CONVERSATION_INITIALIZE), getConversationInitializeParams(), false);
        getConversationInitializeResponse();
    }

    private Map<String, String> getConversationInitializeParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.KEY_TOKEN, token);
        params.put(ApiConstants.KEY_ID_PARTNER, partner.getId());
        return params;
    }

    private void getConversationInitializeResponse() {
        adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
            @Override
            public void onAdventureResponse(JSONObject response) {

                JSONObject data = JsonUtil.getJSONObject(response, ApiConstants.DEF_DATA);
                partnerSocketId = JsonUtil.getString(data, ApiConstants.KEY_PARTNER_SOCKET_ID, null);
                partner.setLastVisitedAt(JsonUtil.getString(data, ApiConstants.KEY_PARTNER_VISITED_AT, null));

                JSONObject jConversation = JsonUtil.getJSONObject(data, ApiConstants.KEY_CONVERSATION);
                conversation = new Conversation(
                        JsonUtil.getString(jConversation, ApiConstants.KEY_ID, null),
                        partner
                );

                JSONArray messages = JsonUtil.getJSONArray(data, ApiConstants.KEY_MESSAGES);
                if (messages != null) {
                    lstMessages.clear();
                    int length = messages.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject item = JsonUtil.getJSONObject(messages, i);
                        lstMessages.add(new Message(
                                JsonUtil.getString(item, ApiConstants.KEY_ID, null),
                                JsonUtil.getString(item, ApiConstants.KEY_ID_CONVERSATION, null),
                                JsonUtil.getString(item, ApiConstants.KEY_OWNER, null),
                                JsonUtil.getString(item, ApiConstants.KEY_CONTENT, null),
                                JsonUtil.getString(item, ApiConstants.KEY_CREATED_AT, null)));
                    }
                }
                bindingConversationInitialize();
            }

            @Override
            public void onAdventureError(int errorCode, String errorMsg) {
                ToastUtil.showToast(errorMsg);
            }
        });
    }

    private void bindingConversationInitialize() {
        emitJoinRoom();
        if (!StringUtil.isNull(partner.getLastVisitedAt())) {
            StringUtil.setText(tvStatusMessage, ConvertTimeHelper.convertISODateToPrettyDateDiff(partner.getLastVisitedAt()));
        } else {
            StringUtil.setText(tvStatusMessage, getResources().getString(R.string.label_online));
        }
        notifyInsertedMessage();
    }

    private void bindingJoinRoom(String idConversation, String idUser) {
        if (idUser.equals(partner.getId())) {
            StringUtil.setText(tvStatusMessage, getResources().getString(R.string.label_online));
        }
    }

    private void bindingLeaveRoom(String idConversation, String idUser) {
        if (idUser.equals(partner.getId())) {
            //StringUtil.setText(tvStatusMessage, getResources().getString(R.string.label_online));
        }
    }

    private void bindingComingMessage(String idConversation, String idUser, String content) {
        if (idUser.equals(partner.getId())) {
            lstMessages.add(new Message(
                    idConversation,
                    idUser,
                    content,
                    new Date()
            ));
            notifyInsertedMessage();
        }
    }

    private void addTyping(String idUser) {
        if (idUser.equals(partner.getId())) {
            lstMessages.add(null);
            notifyInsertedMessage();
        }
    }

    private void removeTyping(String idUser) {
        if (idUser.equals(partner.getId())) {
            int index = lstMessages.size() - 1;
            for(int i = index; i >= 0; i--){
                if(lstMessages.get(i) == null){
                    lstMessages.remove(i);
                    messageAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void notifyInsertedMessage() {
        int size = lstMessages.size();
        messageAdapter.notifyItemInserted(size - 1);
        rvMessage.scrollToPosition(size - 1);
    }

    private void emitJoinRoom() {
        JSONObject data = new JSONObject();
        try {
            data.put(ApiConstants.KEY_ID_CONVERSATION, conversation.getId());
            data.put(ApiConstants.KEY_ID_USER, idUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(ApiConstants.SOCKET_JOIN_ROOM, data.toString());
    }

    private void emitLeaveRoom() {
        JSONObject data = new JSONObject();
        try {
            data.put(ApiConstants.KEY_ID_CONVERSATION, conversation.getId());
            data.put(ApiConstants.KEY_ID_USER, idUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(ApiConstants.SOCKET_LEAVE_ROOM, data.toString());
    }

    private void emitChatToRoom(String content) {
        JSONObject data = new JSONObject();
        try {
            data.put(ApiConstants.KEY_ID_CONVERSATION, conversation.getId());
            data.put(ApiConstants.KEY_OWNER, idUser);
            data.put(ApiConstants.KEY_CONTENT, content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(ApiConstants.SOCKET_CHAT_TO_ROOM, data.toString());
    }

    private Emitter.Listener onJoinRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = new JSONObject();
                    try {
                        data = new JSONObject(args[0].toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String idConversation = JsonUtil.getString(data, ApiConstants.KEY_ID_CONVERSATION, conversation.getId());
                    String idPartner = JsonUtil.getString(data, ApiConstants.KEY_ID_USER, partner.getId());
                    bindingJoinRoom(idConversation, idPartner);
                }
            });
        }
    };

    private Emitter.Listener onLeaveRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = new JSONObject();
                    try {
                        data = new JSONObject(args[0].toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String idConversation = JsonUtil.getString(data, ApiConstants.KEY_ID_CONVERSATION, conversation.getId());
                    String idPartner = JsonUtil.getString(data, ApiConstants.KEY_ID_USER, partner.getId());
                    removeTyping(idPartner);
                    bindingLeaveRoom(idConversation, idPartner);

                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = new JSONObject();
                    try {
                        data = new JSONObject(args[0].toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String idConversation = JsonUtil.getString(data, ApiConstants.KEY_ID_CONVERSATION, conversation.getId());
                    String idPartner = JsonUtil.getString(data, ApiConstants.KEY_OWNER, partner.getId());
                    String content = JsonUtil.getString(data, ApiConstants.KEY_CONTENT, null);
                    bindingComingMessage(idConversation, idPartner, content);
                    removeTyping(idPartner);
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = new JSONObject();
                    try {
                        data = new JSONObject(args[0].toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String idConversation = JsonUtil.getString(data, ApiConstants.KEY_ID_CONVERSATION, conversation.getId());
                    String idPartner = JsonUtil.getString(data, ApiConstants.KEY_ID_USER, partner.getId());
                    addTyping(idPartner);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = new JSONObject();
                    try {
                        data = new JSONObject(args[0].toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String idConversation = JsonUtil.getString(data, ApiConstants.KEY_ID_CONVERSATION, conversation.getId());
                    String idPartner = JsonUtil.getString(data, ApiConstants.KEY_ID_USER, partner.getId());
                    removeTyping(idPartner);
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!isTyping)
                return;

            isTyping = false;
            JSONObject data = new JSONObject();
            try {
                data.put(ApiConstants.KEY_ID_CONVERSATION, conversation.getId());
                data.put(ApiConstants.KEY_ID_USER, idUser);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit(ApiConstants.SOCKET_STOP_TYPING, data.toString());
        }
    };

    private void setupPopUpEmoji() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
                    @Override
                    public void onEmojiPopupShown() {
                        ivEmoticon.setImageResource(R.drawable.ic_keyboard_96);
                    }
                }).setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override
                    public void onEmojiPopupDismiss() {
                        ivEmoticon.setImageResource(R.drawable.ic_lol_96);
                    }
                }).setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
                    @Override
                    public void onKeyboardClose() {
                        emojiPopup.dismiss();
                    }
                }).build(eetMessage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //socket.disconnect();
        socket.off(ApiConstants.SOCKET_JOIN_ROOM, onJoinRoom);
        socket.off(ApiConstants.SOCKET_LEAVE_ROOM, onLeaveRoom);
        socket.off(ApiConstants.SOCKET_NEW_MESSAGE, onNewMessage);
        socket.off(ApiConstants.SOCKET_TYPING, onTyping);
        socket.off(ApiConstants.SOCKET_STOP_TYPING, onStopTyping);
    }
}
