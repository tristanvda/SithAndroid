package com.grietenenknapen.sithandroid.ui.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.application.SithApplication;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.service.PlayerService;
import com.grietenenknapen.sithandroid.ui.PresenterActivity;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.adapters.EditablePlayerAdapter;
import com.grietenenknapen.sithandroid.ui.helper.DefaultTextWatcher;
import com.grietenenknapen.sithandroid.ui.helper.ItemOffsetDecoration;
import com.grietenenknapen.sithandroid.ui.presenters.PlayerPresenter;
import com.grietenenknapen.sithandroid.ui.views.AnimGridLayoutManager;
import com.grietenenknapen.sithandroid.util.ActivityUtils;
import com.grietenenknapen.sithandroid.util.FontCache;
import com.grietenenknapen.sithandroid.util.ItemClickSupport;
import com.grietenenknapen.sithandroid.util.ResourceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerActivity extends PresenterActivity<PlayerPresenter, PlayerPresenter.View> implements PlayerPresenter.View,
        DefaultTextWatcher.OnTextChangedListener {
    private static final String PRESENTER_TAG = "player_presenter";
    private static final int ACTION_DELAY = 250;

    @BindView(R.id.bottom_sheet)
    View bottomSheet;
    @BindView(R.id.playerRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.playerNameEdit)
    EditText playerNameEditText;
    @BindView(R.id.playerPhoneEdit)
    EditText playerPhoneEditText;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.playerConfirm)
    ImageButton conFirmButton;
    @BindView(R.id.playerDelete)
    ImageButton deleteButton;

    private TextWatcher textWatcherName;
    private TextWatcher textWatcherPhone;

    private BottomSheetBehavior bottomSheetBehavior;
    private EditablePlayerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        ButterKnife.bind(this);
        initLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerNameEditText.removeTextChangedListener(textWatcherName);
        playerPhoneEditText.removeTextChangedListener(textWatcherPhone);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerNameEditText.addTextChangedListener(textWatcherName);
        playerPhoneEditText.addTextChangedListener(textWatcherPhone);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ItemClickSupport.removeFrom(recyclerView);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private void initLayout() {
        Typeface starWars = FontCache.get("fonts/Starjedi.ttf", this);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    presenter.cancelCurrentAction();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        bottomSheet.post(new Runnable() {
            @Override
            public void run() {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new AnimGridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        RecyclerView.ItemDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.card_margin);
        recyclerView.addItemDecoration(itemDecoration);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                presenter.onListItemClicked(position);
            }
        });

        textWatcherName = new DefaultTextWatcher(playerNameEditText, this);
        textWatcherPhone = new DefaultTextWatcher(playerPhoneEditText, this);

        playerPhoneEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.onSaveClicked();
                }
                return false;
            }
        });

        playerPhoneEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    getPresenter().onSaveClicked();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void displayPlayers(List<Player> players) {
        adapter = new EditablePlayerAdapter(ResourceUtils.getDefaultCardItemSize(getWindowManager()));
        recyclerView.setAdapter(adapter);
        adapter.setData(players, true);
    }

    @Override
    public void displayError(int messageResId) {
        //TODO: display the error
    }

    @Override
    public void showNewPlayer(final String name, final String number, boolean doNotHide) {
        playerPhoneEditText.setText(name);
        playerNameEditText.setText(number);
        playerNameEditText.requestFocus();
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED && !doNotHide) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
    }

    @Override
    public void showExistingPlayer(final Player player, final boolean doNotHide) {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED && !doNotHide) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            playerPhoneEditText.setText("");
            playerNameEditText.setText("");
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            playerNameEditText.requestFocus();
            playerPhoneEditText.setText(player.getTelephoneNumber());
            playerNameEditText.setText(player.getName());
        }
    }

    @Override
    public void hideBottomSheet() {
        bottomSheet.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.hideKeyboard(PlayerActivity.this);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }, ACTION_DELAY);
    }

    @Override
    public void hideSaveButton() {
        conFirmButton.setVisibility(View.GONE);
    }

    @Override
    public void hideDeleteButton() {
        deleteButton.setVisibility(View.GONE);
    }

    @Override
    public void showDeleteButton() {
        deleteButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSaveButton() {
        conFirmButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void scrollToBottom() {
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    protected String getPresenterTag() {
        return PRESENTER_TAG;
    }

    @Override
    protected PresenterFactory<PlayerPresenter> getPresenterFactory() {
        return new PlayerPresenterFactory(((SithApplication) getApplicationContext()).getPlayerService());
    }

    @Override
    protected PlayerPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public void onTextChanged(View v) {
        switch (v.getId()) {
            case R.id.playerNameEdit:
                presenter.updateNewName(playerNameEditText.getText().toString().trim());
                break;
            case R.id.playerPhoneEdit:
                presenter.updateNewPhoneNumber(playerPhoneEditText.getText().toString().trim());
                break;
        }
    }

    @OnClick(R.id.playerConfirm)
    public void onConfirmClicked() {
        presenter.onSaveClicked();
    }

    @OnClick(R.id.playerDelete)
    public void onDeleteClicked() {
        presenter.onDeleteClicked();
    }


    private static class PlayerPresenterFactory implements PresenterFactory<PlayerPresenter> {
        private final PlayerService playerService;

        public PlayerPresenterFactory(PlayerService playerService) {
            this.playerService = playerService;
        }

        @Override
        public PlayerPresenter createPresenter() {
            return new PlayerPresenter(playerService);
        }
    }
}
