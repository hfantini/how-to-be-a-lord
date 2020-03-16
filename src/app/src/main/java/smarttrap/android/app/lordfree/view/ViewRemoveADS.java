/*

	+ =====================================================================
	|
	|	== SMARTTRAP @ 2016
	|   == HOW TO BE A LORD - APP
	|	====================================
	|   ==    FILE: ViewRemoveADS.java
	|	==    DATE (YYYY-MM-DD): 2016-12-28 | TIME (HH:MM): 13:57
	|   ==   SINCE: 0.0.4a
	|	==  AUTHOR: Henrique Fantini
	|   == CONTACT: smart.trap@outlook.com
	|   ====================================
	|   ==

*/

package smarttrap.android.app.lordfree.view;

// == IMPORT LIBS
// ==========================================================================================

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import smarttrap.android.app.lordfree.R;
import smarttrap.android.app.lordfree.info.ADSKey;
import smarttrap.android.app.lordfree.info.GlobalAttr;
import smarttrap.android.app.lordfree.info.GlobalOptions;
import smarttrap.android.lib.core.ads.ADSContainer;
import smarttrap.android.lib.core.ads.ADSContainerGoogleBanner;
import smarttrap.android.lib.core.hardware.HardwareUtil;
import smarttrap.android.lib.core.interfac.ILoadableContent;

// == CLASS
// ==========================================================================================

public class ViewRemoveADS extends AppCompatActivity implements ILoadableContent
{
    // == DECLARATION
    // ======================================================================================

    // == CONST =============================================================================

    // == VAR ===============================================================================

    private Bitmap bgImage;

    // == CLASS CONSTRUCTOR(S)
    // ======================================================================================

    public ViewRemoveADS()
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
        setContentView(R.layout.activity_view_remove_ads);

        // == CONFIGURE COMPONENTS

        ImageButton btnBack = (ImageButton) findViewById( R.id.btnAdsBack );
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        ImageButton btnRemoveADS = (ImageButton) findViewById( R.id.btnAdsRemove );
        btnRemoveADS.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=smarttrap.android.app.lord"));
                getApplicationContext().startActivity(viewIntent);
            }
        });


        // SETTING BG IMAGE
        ImageView bgContainer = (ImageView) findViewById(R.id.imgAdsBackground);
        bgContainer.setImageBitmap(bgImage);

        //CONFIGURE PREMIUM/FREE VERSION

        if(GlobalOptions.PREMIUM_VERSION == false)
        {
            //ADS
            LinearLayout layAds = (LinearLayout) findViewById(R.id.layAdsADSBar);
            ADSContainer container = new ADSContainerGoogleBanner(getApplicationContext(), layAds, AdSize.SMART_BANNER, ADSKey.ADS_ADS_KEY, GlobalOptions.ADS_TESTMODE);

            try
            {
                container.loadContent( getApplicationContext() );
                container.loadADS();
            }
            catch(Exception e)
            {
                //THROW EXCEPTION
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadContent(Context context)
    {
        Bitmap bgImageLarger = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_ads_bg);
        bgImage = Bitmap.createScaledBitmap(bgImageLarger, HardwareUtil.getScreenSize().getXValue(), HardwareUtil.getScreenSize().getYValue(), false);
    }

    // == CLASS EVENTS
    // ======================================================================================

    // == GETTERS AND SETTERS
    // ======================================================================================

}
