
/*

	+ =====================================================================
	|
	|	== SMARTTRAP @ 2016
	|   == HOW TO BE A LORD - APP
	|	====================================
	|   ==    FILE: .java
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;

import com.google.android.gms.ads.AdSize;

import smarttrap.android.app.lordfree.info.ADSKey;
import smarttrap.android.app.lordfree.info.GlobalAttr;
import smarttrap.android.app.lordfree.info.GlobalOptions;
import smarttrap.android.lib.core.activity.ViewLoaderBase;
import smarttrap.android.lib.core.ads.ADSContainer;
import smarttrap.android.lib.core.ads.ADSContainerGoogleBanner;

// == CLASS
// ==========================================================================================

public class ViewLoader extends ViewLoaderBase
{

    // == DECLARATION
    // ======================================================================================

    // == CONST =============================================================================

    // == VAR ===============================================================================

    // == CLASS CONSTRUCTOR(S)
    // ======================================================================================

    // == CLASS METHODS
    // ======================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setMinimumLoadingTime(5000);

        GlobalAttr.defaultAppContext = getApplicationContext();

        if(GlobalOptions.PREMIUM_VERSION == false)
        {
            try
            {
                ADSContainer container = new ADSContainerGoogleBanner(getApplicationContext(), layADSContainer, AdSize.SMART_BANNER, ADSKey.ADS_LDR_KEY, GlobalOptions.ADS_TESTMODE);
                container.loadContent(getApplicationContext());
                container.loadADS();
            }
            catch(Exception e)
            {
                //EXCEPTION
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadContent(Context context)
    {
        ActivityInfo.viewMenu = new Intent(this, ViewMenu.class);
        ActivityInfo.viewMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

        ActivityInfo.viewApp = new Intent(this, ViewApp.class);

        ActivityInfo.viewRemoveADS = new Intent(this, ViewRemoveADS.class);

        ActivityInfo.viewCredits = new Intent(this, ViewCredits.class);
    }

    // == CLASS EVENTS
    // ======================================================================================

    @Override
    protected void onStartLoader() throws Exception
    {
        super. onStartLoader();
    }

    @Override
    protected void onFinishLoader() throws Exception
    {
        super.onFinishLoader();

        startActivity( ActivityInfo.viewMenu );
        ActivityCompat.finishAffinity(this);
    }

    // == GETTERS AND SETTERS
    // ======================================================================================

}
