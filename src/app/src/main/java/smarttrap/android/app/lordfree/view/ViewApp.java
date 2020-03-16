
/*

	+ =====================================================================
	|
	|	== SMARTTRAP @ 2016
	|   == HOW TO BE A LORD - APP
	|	====================================
	|   ==    FILE: ViewApp.java
	|	==    DATE (YYYY-MM-DD): 2016-12-20 | TIME (HH:MM): 08:36
	|   ==   SINCE: 0.0.1a
	|	==  AUTHOR: Henrique Fantini
	|   == CONTACT: smart.trap@outlook.com
	|   ====================================
	|   ==

*/

package smarttrap.android.app.lordfree.view;

// == IMPORT LIBS
// ==========================================================================================

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.android.gms.ads.AdSize;
import java.util.ArrayList;
import java.util.List;
import smarttrap.android.app.lordfree.R;
import smarttrap.android.app.lordfree.component.MusicSelector;
import smarttrap.android.app.lordfree.enumeration.ActivityState;
import smarttrap.android.app.lordfree.enumeration.AppOverlayState;
import smarttrap.android.app.lordfree.enumeration.AppState;
import smarttrap.android.app.lordfree.info.ADSKey;
import smarttrap.android.app.lordfree.info.GlobalAttr;
import smarttrap.android.app.lordfree.info.GlobalOptions;
import smarttrap.android.app.lordfree.model.PlayableMusic;
import smarttrap.android.app.lordfree.service.MusicPlayerFire;
import smarttrap.android.app.lordfree.service.MusicPlayerRain;
import smarttrap.android.app.lordfree.service.MusicPlayerService;
import smarttrap.android.lib.core.ads.ADSContainer;
import smarttrap.android.lib.core.ads.ADSContainerGoogleBanner;
import smarttrap.android.lib.core.ads.ADSContainerGoogleIntersticial;
import smarttrap.android.lib.core.enumeration.EPlayerState;
import smarttrap.android.lib.core.enumeration.EVisibility;
import smarttrap.android.lib.core.hardware.HardwareUtil;
import smarttrap.android.lib.core.interfac.ILoadableContent;
import smarttrap.android.lib.core.math.DoubleVector;

// == CLASS
// ==========================================================================================

public class ViewApp extends AppCompatActivity implements ILoadableContent
{

    // == DECLARATION
    // ======================================================================================

    // == CONST =============================================================================

    private final int IDLE_TIME = 2000;
    private final int ANIMATION_SPEED = 500;
    private final int DELAY_START_NEXT_MUSIC = 2000;
    private final int DELAY_CHANGE_MUSIC = 200;

    // == VAR ===============================================================================

    // == STATES

    private AppState currentState;
    private AppOverlayState currentOverlayState;
    private ActivityState currentActivityState;

    private Boolean playMusicSound;
    private Boolean playRainSound;
    private Boolean playFireSound;

    private EVisibility leftBarState;
    private EVisibility lastLeftBarState;
    private EVisibility rightBarState;
    private EVisibility lastRightBarState;
    private Boolean isChangeOnLeftBar;

    // == MEDIA

    private Handler handler;

    private VideoView fireplaceVideoView;
    private MediaController fireplaceMediaController;
    private Uri uriFireplace;

    private Intent playerMusicService;
    private Intent playerRainService;
    private Intent playerFireService;

    List<MusicSelector> musicSelectorList;
    int currentMusicPointer;

    // == UI COMPOENENTS
    private ImageView imgControllerBorderTop;
    private ImageView imgControllerBorderBottom;
    private ImageButton btnPlayStop;
    private LinearLayout layOverlayBG;

    private ImageButton btnShowLeftSideBar;
    private LinearLayout laySideBarLeft;
    private RelativeLayout laySidebarLeftPuller;
    private int laySideBarLeftWidth;

    private LinearLayout layOverlayCenter;

    private ImageButton btnShowRightSideBar;
    private LinearLayout laySideBarRight;
    private RelativeLayout laySidebarRightPuller;
    private int laySideBarRightWidth;
    private ImageView layBorderLeftTopOuter;
    private ImageView layBorderLeftBottomOuter;
    private ImageView layBorderRightTopOuter;
    private ImageView layBorderRightBottomOuter;
    private LinearLayout laySidebarRightMusicContainer;

    private ImageButton btnChkMusic;
    private ImageButton btnChkRain;
    private ImageButton btnChkFire;

    // == ADS
    ADSContainerGoogleIntersticial adsFS;

    // == CLASS CONSTRUCTOR(S)
    // ======================================================================================

    public ViewApp()
    {
        super();

        loadContent(GlobalAttr.defaultAppContext);
    }

    // == CLASS METHODS
    // ======================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //REMOVE TITLE BAR
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //DEFINE CONTENT VIEW
        setContentView(R.layout.activity_view_app);

        // == LINKING VIDEO FILE AND SOUNDS

        //CONTROLLER BOARDS
        imgControllerBorderTop = (ImageView) findViewById(R.id.imgAppControlBorderTop);
        imgControllerBorderBottom = (ImageView) findViewById(R.id.imgAppControlBorderBottom);

        //PLAY/STOP BUTTON
        btnPlayStop = (ImageButton) findViewById(R.id.btnAppPlay);
        btnPlayStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(currentState == AppState.APP_STATE_PLAYING)
                {
                    currentState = AppState.APP_STATE_STOPPED;
                }
                else if(currentState == AppState.APP_STATE_STOPPED)
                {
                    currentState = AppState.APP_STATE_PLAYING;
                }

                updateView();
            }
        });

        // == GETTING OTHER COMPONENTS
        layOverlayBG = (LinearLayout) findViewById(R.id.layAppOverlayBackground);
        DoubleVector<Integer> screenSize = HardwareUtil.getScreenSize();

        // LEFT BAR

        laySideBarLeft = (LinearLayout) findViewById(R.id.layAppSidebarLeft);
        RelativeLayout.LayoutParams sideBarLeftParams = (RelativeLayout.LayoutParams) laySideBarLeft.getLayoutParams();
        laySideBarLeftWidth = screenSize.getXValue() / 4;
        sideBarLeftParams.width = laySideBarLeftWidth;
        sideBarLeftParams.leftMargin = -laySideBarLeftWidth;
        laySideBarLeft.setLayoutParams(sideBarLeftParams);
        lastLeftBarState = EVisibility.VISIBILITY_INVISIBLE;
        leftBarState = EVisibility.VISIBILITY_INVISIBLE;
        isChangeOnLeftBar = false;

        btnShowLeftSideBar = (ImageButton) findViewById(R.id.btnToggleLeftSideBar);
        btnShowLeftSideBar.setAlpha(0f);
        btnShowLeftSideBar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                isChangeOnLeftBar = true;
                invertLeftBarState();
                updateView();
            }
        });

        // PULLER

        laySidebarLeftPuller = (RelativeLayout)  findViewById(R.id.layAppSidebarLeftPuller);
        RelativeLayout.LayoutParams sideBarLeftPullerParams = (RelativeLayout.LayoutParams) laySidebarLeftPuller.getLayoutParams();
        sideBarLeftPullerParams.width = screenSize.getXValue() / 8;
        laySidebarLeftPuller.setLayoutParams(sideBarLeftPullerParams);

        // DETAILS
        ImageView imgAppBorderCenterL = (ImageView)  findViewById(R.id.imgAppBorderCenterL);
        imgAppBorderCenterL.getLayoutParams().width = screenSize.getXValue() / 20;

        ImageView imgBorderLeftTopInner = (ImageView) findViewById(R.id.imgBorderLeftTopInner);
        imgBorderLeftTopInner.getLayoutParams().width = sideBarLeftPullerParams.width;

        ImageView imgAppBorderBottomInnerL = (ImageView) findViewById(R.id.imgBorderLeftBottomInner);
        imgAppBorderBottomInnerL.getLayoutParams().width = sideBarLeftPullerParams.width;

        // CONTROLS
        btnChkMusic = (ImageButton) findViewById(R.id.btnChkMusic);
        btnChkMusic.getLayoutParams().height = screenSize.getYValue() / 8;
        btnChkMusic.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(playMusicSound == true)
                {
                    playMusicSound = false;
                }
                else
                {
                    playMusicSound = true;
                }

                updateView();
            }

        });

        btnChkRain = (ImageButton) findViewById(R.id.btnChkRain);
        btnChkRain.getLayoutParams().height = screenSize.getYValue() / 8;
        btnChkRain.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(playRainSound == true)
                {
                    playRainSound = false;
                }
                else
                {
                    playRainSound = true;
                }

                updateView();
            }

        });

        btnChkFire = (ImageButton) findViewById(R.id.btnChkFireplace);
        btnChkFire.getLayoutParams().height = screenSize.getYValue() / 8;
        btnChkFire.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(playFireSound == true)
                {
                    playFireSound = false;
                }
                else
                {
                    playFireSound = true;
                }

                updateView();
            }

        });

        // RIGHT BAR

        laySideBarRight = (LinearLayout) findViewById(R.id.layAppSidebarRight);
        RelativeLayout.LayoutParams sideBarRightParams = (RelativeLayout.LayoutParams) laySideBarRight.getLayoutParams();
        laySideBarRightWidth = screenSize.getXValue() / 4;
        sideBarRightParams.width = laySideBarRightWidth;
        sideBarRightParams.rightMargin = -laySideBarRightWidth;
        laySideBarRight.setLayoutParams(sideBarRightParams);
        rightBarState = EVisibility.VISIBILITY_INVISIBLE;
        lastRightBarState = EVisibility.VISIBILITY_INVISIBLE;

        btnShowRightSideBar = (ImageButton) findViewById(R.id.btnToggleRightSideBar);
        btnShowRightSideBar.setAlpha(0f);
        btnShowRightSideBar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                isChangeOnLeftBar = false;
                invertRightBarState();
                updateView();
            }
        });

        // PULLER

        laySidebarRightPuller = (RelativeLayout)  findViewById(R.id.layAppSidebarRightPuller);
        RelativeLayout.LayoutParams sideBarRightPullerParams = (RelativeLayout.LayoutParams) laySidebarRightPuller.getLayoutParams();
        sideBarRightPullerParams.width = screenSize.getXValue() / 8;
        laySidebarRightPuller.setLayoutParams(sideBarRightPullerParams);

        // DETAILS
        ImageView imgAppBorderCenterR = (ImageView)  findViewById(R.id.imgAppBorderCenterR);
        imgAppBorderCenterR.getLayoutParams().width = screenSize.getXValue() / 20;

        ImageView imgAppBorderUpperInnerR = (ImageView) findViewById(R.id.imgAppBorderUpperInnerR);
        imgAppBorderUpperInnerR.getLayoutParams().width = sideBarRightPullerParams.width;

        ImageView imgAppBorderBottomInnerR = (ImageView) findViewById(R.id.imgAppBorderBottomInnerL);
        imgAppBorderBottomInnerR.getLayoutParams().width = sideBarRightPullerParams.width;

        layBorderLeftTopOuter = (ImageView) findViewById(R.id.imgBorderLeftTopOuter);
        layBorderLeftBottomOuter = (ImageView) findViewById(R.id.imgBorderLeftBottomOuter);
        layBorderRightTopOuter = (ImageView) findViewById(R.id.imgBorderRightTopOuter);
        layBorderRightBottomOuter = (ImageView) findViewById(R.id.imgBorderRightBottomOuter);

        // CONTROLS
        laySidebarRightMusicContainer = (LinearLayout) findViewById(R.id.laySidebarRightMusicContainer);

        // CENTER

        layOverlayCenter = (LinearLayout) findViewById(R.id.layAppOverlayCenter);
        RelativeLayout.LayoutParams sideBarCenterParams = (RelativeLayout.LayoutParams) layOverlayCenter.getLayoutParams();
        sideBarCenterParams.leftMargin = laySideBarLeftWidth;
        sideBarCenterParams.rightMargin = laySideBarRightWidth;
        layOverlayCenter.setLayoutParams(sideBarCenterParams);

        // == LOADING MUSICS

        musicSelectorList = new ArrayList<MusicSelector>();
        musicSelectorList.add( new MusicSelector(this, GlobalAttr.defaultAppContext, new PlayableMusic( 0, "Csárdás - Vittorio Monti", R.raw.music1 ) ) );
        musicSelectorList.add( new MusicSelector(this, GlobalAttr.defaultAppContext, new PlayableMusic( 1, "The Jazz Piano - Bensound", R.raw.music2 ) ) );
        musicSelectorList.add( new MusicSelector(this, GlobalAttr.defaultAppContext, new PlayableMusic( 2, "Serenade for Strings Op22 in E Major - Dvorak", R.raw.music3 ) ) );
        musicSelectorList.add( new MusicSelector(this, GlobalAttr.defaultAppContext, new PlayableMusic( 3, "Laudate Dominum - Mozart", R.raw.music4 ) ) );
        musicSelectorList.add( new MusicSelector(this, GlobalAttr.defaultAppContext, new PlayableMusic( 4, "Suite for Violin - Bughici", R.raw.music5 ) ) );
        musicSelectorList.add( new MusicSelector(this, GlobalAttr.defaultAppContext, new PlayableMusic( 5, "Piano: Drops - Whitesand", R.raw.music6 ) ) );
        musicSelectorList.add( new MusicSelector(this, GlobalAttr.defaultAppContext, new PlayableMusic( 6, "Piano: Infinite - Whitesand", R.raw.music7 ) ) );
        musicSelectorList.add( new MusicSelector(this, GlobalAttr.defaultAppContext, new PlayableMusic( 7, "Chances: Silent Partner - Mouzike", R.raw.music8 ) ) );
        musicSelectorList.add( new MusicSelector(this, GlobalAttr.defaultAppContext, new PlayableMusic( 8, "Please Return - Brian Lowe", R.raw.music9 ) ) );
        musicSelectorList.add( new MusicSelector(this, GlobalAttr.defaultAppContext, new PlayableMusic( 9, "Fantasie-Impromptu Op. 66 - Chopin,", R.raw.music10 ) ) );

        currentMusicPointer = 0;

        for(MusicSelector selector : musicSelectorList)
        {
            laySidebarRightMusicContainer.addView(selector);
        }

        //CONFIGURE PREMIUM/FREE VERSION

        // == ADS
        if(GlobalOptions.PREMIUM_VERSION == false)
        {
            //ADS

            try
            {
                LinearLayout layAds = (LinearLayout) findViewById(R.id.layAppADSBar);
                ADSContainer container = new ADSContainerGoogleBanner(getApplicationContext(), layAds, AdSize.SMART_BANNER, ADSKey.ADS_APP_KEY, GlobalOptions.ADS_TESTMODE);
                container.loadContent(getApplicationContext());
                container.loadADS();
            }
            catch(Exception e)
            {
                //THROW EXCEPTION
                e.printStackTrace();
            }

        }

        //STARTING SERVICES
        playerMusicService = new Intent(this, MusicPlayerService.class);
        playerRainService = new Intent(this, MusicPlayerRain.class);
        playerFireService = new Intent(this, MusicPlayerFire.class);

    }

    private void updateView()
    {
        if(currentState == AppState.APP_STATE_STOPPED)
        {
            // SET COMPONENT STATES

            btnPlayStop.setImageResource( R.drawable.btn_app_play );

            // STOP SOUNDS

            stopAllSounds();
            stopVideo();

            // PLAY ANIMATIONS
            overlayStop();

        }
        else if(currentState == AppState.APP_STATE_PLAYING)
        {
            // SET COMPONENT STATES

            btnPlayStop.setImageResource(R.drawable.btn_app_stop);

            // PLAY SOUNDS

            startAllSounds();
            startVideo();

            if (currentOverlayState == AppOverlayState.APP_OVERLAYSTATE_STOP || currentOverlayState == AppOverlayState.APP_OVERLAYSTATE_PLAY_CONTROLS_HIDE)
            {
                // PLAY ANIMATIONS
                overlayShowControls();
            }
            else if(currentOverlayState == AppOverlayState.APP_OVERLAYSTATE_PLAY_CONTROLS_SHOW)
            {
                //UPDATE CHANGES ON CONTROL OVERLAY

                if(playMusicSound == true)
                {
                    btnChkMusic.setImageResource(R.drawable.chk_music_true);
                }
                else
                {
                    btnChkMusic.setImageResource(R.drawable.chk_music_false);
                }

                if(playRainSound == true)
                {
                    btnChkRain.setImageResource(R.drawable.chk_rain_true);
                }
                else
                {
                    btnChkRain.setImageResource(R.drawable.chk_rain_false);
                }

                if(playFireSound == true)
                {
                    btnChkFire.setImageResource(R.drawable.chk_fireplace_true);
                }
                else
                {
                    btnChkFire.setImageResource(R.drawable.chk_fireplace_false);

                }

                //UPDATE SOUNDS
                updateSounds();

                //UPDATE SIDEBARS
                if(isChangeOnLeftBar == true)
                {
                    if (leftBarState == EVisibility.VISIBILITY_VISIBLE && lastLeftBarState == EVisibility.VISIBILITY_INVISIBLE)
                    {
                        if (rightBarState == EVisibility.VISIBILITY_VISIBLE)
                        {
                            invertRightBarState();
                            hideRightBar();
                        }

                        showLeftBar();
                        lastLeftBarState = null;
                    }
                    else if (leftBarState == EVisibility.VISIBILITY_INVISIBLE && lastLeftBarState == EVisibility.VISIBILITY_VISIBLE)
                    {
                        hideLeftBar();
                        lastLeftBarState = null;
                    }
                }
                else
                {

                    if (rightBarState == EVisibility.VISIBILITY_VISIBLE && lastRightBarState == EVisibility.VISIBILITY_INVISIBLE)
                    {
                        if (leftBarState == EVisibility.VISIBILITY_VISIBLE)
                        {
                            invertLeftBarState();
                            hideLeftBar();
                        }

                        showRightBar();
                        lastRightBarState = null;
                    }
                    else if (rightBarState == EVisibility.VISIBILITY_INVISIBLE && lastRightBarState == EVisibility.VISIBILITY_VISIBLE)
                    {
                        hideRightBar();
                        lastRightBarState = null;
                    }
                }
            }
        }
    }

    private void overlayStop()
    {
        setCurrentOverlayState( AppOverlayState.APP_OVERLAYSTATE_STOP );
        stopIdleTime();

        imgControllerBorderTop.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        imgControllerBorderBottom.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        btnPlayStop.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        layOverlayBG.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        layBorderLeftTopOuter.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        layBorderLeftBottomOuter.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        layBorderRightTopOuter.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        layBorderRightBottomOuter.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);

        btnShowLeftSideBar.setClickable(false);
        btnShowLeftSideBar.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);

        btnShowRightSideBar.setClickable(false);
        btnShowRightSideBar.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);

        if(leftBarState == EVisibility.VISIBILITY_VISIBLE)
        {
            hideLeftBar();
        }

        if(rightBarState == EVisibility.VISIBILITY_VISIBLE)
        {
            hideRightBar();
        }
    }

    private void overlayShowControls()
    {
        setCurrentOverlayState( AppOverlayState.APP_OVERLAYSTATE_PLAY_CONTROLS_SHOW );

        btnShowLeftSideBar.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        btnShowRightSideBar.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        imgControllerBorderTop.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        imgControllerBorderBottom.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        layBorderLeftTopOuter.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        layBorderLeftBottomOuter.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        layBorderRightTopOuter.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        layBorderRightBottomOuter.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);

        btnPlayStop.animate().setDuration(ANIMATION_SPEED).alpha(1.0f);
        layOverlayBG.animate().setDuration(ANIMATION_SPEED).alpha(0.5f).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);

                btnPlayStop.setClickable(true);
                btnShowLeftSideBar.setClickable(true);
                btnShowRightSideBar.setClickable(true);
            }
        });

        resetIdleTime();
    }

    private void overlayHideControls()
    {
        setCurrentOverlayState( AppOverlayState.APP_OVERLAYSTATE_PLAY_CONTROLS_HIDE );

        imgControllerBorderTop.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);
        imgControllerBorderBottom.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);
        layBorderLeftTopOuter.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);
        layBorderLeftBottomOuter.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);
        layBorderRightTopOuter.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);
        layBorderRightBottomOuter.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);

        btnPlayStop.setClickable(false);
        btnPlayStop.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);

        btnShowLeftSideBar.setClickable(false);
        btnShowLeftSideBar.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);

        btnShowRightSideBar.setClickable(false);
        btnShowRightSideBar.animate().setDuration(ANIMATION_SPEED).alpha(0.0f);

        layOverlayBG.animate().setDuration(ANIMATION_SPEED).alpha(0.0f).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {

            }
        });
    }

    private void stopIdleTime()
    {
        if(handler != null)
        {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    private void resetIdleTime()
    {
        stopIdleTime();

        if(leftBarState == EVisibility.VISIBILITY_INVISIBLE && rightBarState == EVisibility.VISIBILITY_INVISIBLE)
        {
            handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    overlayHideControls();
                }

            }, IDLE_TIME);
        }
    }

    private void showLeftBar()
    {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) laySideBarLeft.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.leftMargin, 0);
        animator.setDuration(ANIMATION_SPEED);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                params.leftMargin = (Integer) valueAnimator.getAnimatedValue();
                laySideBarLeft.requestLayout();
            }
        });

        btnShowLeftSideBar.animate().setDuration(ANIMATION_SPEED).rotation(360);
        animator.start();

        leftBarState = EVisibility.VISIBILITY_VISIBLE;

        resetIdleTime();
    }

    private void hideLeftBar()
    {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) laySideBarLeft.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.leftMargin, -laySideBarLeftWidth);
        animator.setDuration(ANIMATION_SPEED);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                params.leftMargin = (Integer) valueAnimator.getAnimatedValue();
                laySideBarLeft.requestLayout();
                resetIdleTime();
            }
        });

        btnShowLeftSideBar.animate().setDuration(ANIMATION_SPEED).rotation(0);
        animator.start();

        leftBarState = EVisibility.VISIBILITY_INVISIBLE;

        if(currentState == AppState.APP_STATE_PLAYING && currentOverlayState == AppOverlayState.APP_OVERLAYSTATE_PLAY_CONTROLS_SHOW)
        {
            resetIdleTime();
        }
    }

    private void showRightBar()
    {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) laySideBarRight.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.rightMargin, 0);
        animator.setDuration(ANIMATION_SPEED);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                params.rightMargin = (Integer) valueAnimator.getAnimatedValue();
                laySideBarRight.requestLayout();
            }
        });

        btnShowRightSideBar.animate().setDuration(ANIMATION_SPEED).rotation(360);
        animator.start();

        rightBarState = EVisibility.VISIBILITY_VISIBLE;

        resetIdleTime();
    }

    private void hideRightBar()
    {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) laySideBarRight.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.rightMargin, -laySideBarRightWidth);
        animator.setDuration(ANIMATION_SPEED);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                params.rightMargin = (Integer) valueAnimator.getAnimatedValue();
                laySideBarRight.requestLayout();
            }
        });

        btnShowRightSideBar.animate().setDuration(ANIMATION_SPEED).rotation(0);
        animator.start();

        rightBarState = EVisibility.VISIBILITY_INVISIBLE;

        if(currentState == AppState.APP_STATE_PLAYING && currentOverlayState == AppOverlayState.APP_OVERLAYSTATE_PLAY_CONTROLS_SHOW)
        {
            resetIdleTime();
        }
    }

    private void invertLeftBarState()
    {
        lastLeftBarState = leftBarState;

        if(leftBarState == EVisibility.VISIBILITY_VISIBLE)
        {
            leftBarState = EVisibility.VISIBILITY_INVISIBLE;
        }
        else if(leftBarState == EVisibility.VISIBILITY_INVISIBLE)
        {
            leftBarState = EVisibility.VISIBILITY_VISIBLE;
        }
    }

    private void invertRightBarState()
    {

        lastRightBarState = rightBarState;

        if(rightBarState == EVisibility.VISIBILITY_VISIBLE)
        {
            rightBarState = EVisibility.VISIBILITY_INVISIBLE;
        }
        else if(rightBarState == EVisibility.VISIBILITY_INVISIBLE)
        {
            rightBarState = EVisibility.VISIBILITY_VISIBLE;
        }

    }

    private void startAllSounds()
    {
        startMusic();
        startRainSound();
        startFireSound();
    }

    private void updateSounds()
    {
        if(playMusicSound == false)
        {
            stopMusic();
        }
        else
        {
            startMusic();
        }

        if(playRainSound == false)
        {
            stopRainSound();
        }
        else
        {
            startRainSound();
        }

        if(playFireSound == false)
        {
            stopFireSound();
        }
        else
        {
            startFireSound();
        }
    }

    private void stopAllSounds()
    {
        stopMusic();
        stopRainSound();
        stopFireSound();
    }

    private void startMusic()
    {
        if(playMusicSound)
        {
            playerMusicService.putExtra("MUSIC_ID", musicSelectorList.get(currentMusicPointer).getMusic().getMusicFileID());
            playerMusicService.putExtra("PLAYER_STATE", EPlayerState.STATE_PLAY.getValue());
            playerMusicService.putExtra("PLAYER_LOOPING", true);
            startService(playerMusicService);

            musicSelectorList.get(currentMusicPointer).setPlaying(true);
        }
    }

    private void stopMusic()
    {
        playerMusicService.putExtra("PLAYER_STATE", EPlayerState.STATE_STOP.getValue());
        startService(playerMusicService);

        musicSelectorList.get(currentMusicPointer).setPlaying(false);
    }

    public void changeMusic(PlayableMusic music)
    {
        if(currentMusicPointer != music.getMusicID())
        {
            if (playMusicSound == true)
            {
                stopMusic();

                currentMusicPointer = music.getMusicID();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        startMusic();
                    }

                }, DELAY_CHANGE_MUSIC);
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.music_mute, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void startRainSound()
    {
        if(playRainSound)
        {
            playerRainService.putExtra("MUSIC_ID", R.raw.rain);
            playerRainService.putExtra("PLAYER_STATE", EPlayerState.STATE_PLAY.getValue());
            playerFireService.putExtra("PLAYER_LOOPING", true);

            startService(playerRainService);
        }
    }

    private void stopRainSound()
    {
        playerRainService.putExtra("PLAYER_STATE", EPlayerState.STATE_STOP.getValue());

        startService(playerRainService);
    }

    private void startFireSound()
    {
        if(playFireSound)
        {
            playerFireService.putExtra("MUSIC_ID", R.raw.fire);
            playerFireService.putExtra("PLAYER_STATE", EPlayerState.STATE_PLAY.getValue());
            playerFireService.putExtra("PLAYER_LOOPING", true);

            startService(playerFireService);
        }
    }

    private void stopFireSound()
    {
        playerFireService.putExtra("PLAYER_STATE", EPlayerState.STATE_STOP.getValue());

        startService(playerFireService);
    }

    private void startVideo()
    {
        // FIREPLACE

        if(fireplaceVideoView == null)
        {
            this.fireplaceVideoView = (VideoView) findViewById(R.id.vViewFireplace);
            this.uriFireplace = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fireplace);

            fireplaceVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    mp.start();
                }
            });

            fireplaceMediaController = new MediaController(ViewApp.this);
            fireplaceMediaController.setVisibility(View.GONE);
            fireplaceVideoView.setMediaController(fireplaceMediaController);
            fireplaceVideoView.setVideoURI(uriFireplace);
            fireplaceVideoView.setKeepScreenOn(true);

            fireplaceVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mp)
                {
                    fireplaceVideoView.seekTo(0);
                    fireplaceVideoView.start();
                }
            });

        }
    }

    private void stopVideo()
    {
        if(fireplaceVideoView != null && fireplaceVideoView.isPlaying() == true)
        {
            fireplaceVideoView.stopPlayback();
            fireplaceVideoView = null;
        }
    }

    @Override
    public void loadContent(Context context)
    {
        //DEFINE STATES

        setCurrentOverlayState( AppOverlayState.APP_OVERLAYSTATE_STOP );
        currentState = AppState.APP_STATE_STOPPED;
        playMusicSound = true;
        playRainSound = true;
        playFireSound = true;

        //FULLSCREEN ADS

        if(GlobalOptions.PREMIUM_VERSION == false)
        {
            try
            {

                //FULLSCREEN BANNER

                adsFS = new ADSContainerGoogleIntersticial(GlobalAttr.defaultAppContext, ADSKey.ADS_FS_KEY, GlobalOptions.ADS_TESTMODE)
                {
                    @Override
                    public void onADSClosed()
                    {
                        super.onADSClosed();
                        finish();
                    }
                };

                adsFS.loadContent(GlobalAttr.defaultAppContext);
                adsFS.loadADS();
            }
            catch (Exception e)
            {
                //THROW EXCEPTION
                e.printStackTrace();
            }
        }
    }

    // == CLASS EVENTS
    // ======================================================================================

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        if(currentState == AppState.APP_STATE_PLAYING && currentOverlayState == AppOverlayState.APP_OVERLAYSTATE_PLAY_CONTROLS_HIDE)
        {
            updateView();
        }
        else if(currentState == AppState.APP_STATE_PLAYING && currentOverlayState == AppOverlayState.APP_OVERLAYSTATE_PLAY_CONTROLS_SHOW)
        {
            resetIdleTime();
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed()
    {
        if(currentState == AppState.APP_STATE_PLAYING)
        {
            if (currentOverlayState == AppOverlayState.APP_OVERLAYSTATE_PLAY_CONTROLS_HIDE)
            {
                updateView();
            }
            else if (currentOverlayState == AppOverlayState.APP_OVERLAYSTATE_PLAY_CONTROLS_SHOW)
            {
                Boolean isAnyBarOpen = false;

                if (leftBarState == EVisibility.VISIBILITY_VISIBLE)
                {
                    isAnyBarOpen = true;
                    invertLeftBarState();
                }

                if (rightBarState == EVisibility.VISIBILITY_VISIBLE)
                {
                    isAnyBarOpen = true;
                    invertRightBarState();
                }

                if (isAnyBarOpen == false)
                {
                    currentState = AppState.APP_STATE_STOPPED;
                }

                updateView();
            }
        }
        else if(currentState == AppState.APP_STATE_STOPPED)
        {
            exit();
        }
    }

    private void exit()
    {
        if(GlobalOptions.PREMIUM_VERSION == false)
        {
            adsFS.showADS();
        }

        stopService(playerMusicService);
        stopService(playerRainService);
        stopService(playerFireService);

        finish();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        stopAllSounds();
        exit();
    }

    // == GETTERS AND SETTERS
    // ======================================================================================


    public AppOverlayState getCurrentOverlayState()
    {
        return currentOverlayState;
    }

    public void setCurrentOverlayState(AppOverlayState currentOverlayState)
    {
        this.currentOverlayState = currentOverlayState;
    }

}
