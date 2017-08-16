/*
 AdMobAdsListener.java
 Copyright 2015 AppFeel. All rights reserved.
 http://www.appfeel.com
 
 AdMobAds Cordova Plugin (cordova-admob)
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to
 deal in the Software without restriction, including without limitation the
 rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 sell copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package com.appfeel.cordova.admob;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

@SuppressLint("DefaultLocale")
public class AdMobRewardedVideoAdListener implements RewardedVideoAdListener {
    private String adType = "rewarded";
    private AdMobAds admobAds;
    private int rewardAmount = 0;
    private String rewardType = "";
    public AdMobRewardedVideoAdListener(String adType, AdMobAds admobAds) {
        this.adType = adType;
        this.admobAds = admobAds;
    }

    /**
     * Gets a string error reason from an error code.
     */
    public String getErrorReason(int errorCode) {
        String errorReason = "Unknown";
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                errorReason = "Internal error";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                errorReason = "Invalid request";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                errorReason = "Network Error";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                errorReason = "No fill";
                break;
        }
        return errorReason;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        admobAds.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": left application");
                String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdLeftApplication, { 'adType': '%s' });", adType);
                admobAds.webView.loadUrl(event);
            }
        });
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        final int code = errorCode;
        admobAds.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String reason = getErrorReason(code);
                Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": failed to load ad (" + reason + ")");
                String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdFailedToLoad, { 'adType': '%s', 'error': %d, 'reason': '%s' });", adType, code, reason);
                admobAds.webView.loadUrl(event);
            }
        });
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        admobAds.onAdLoaded(adType);
        admobAds.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": ad loaded");
                String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdLoaded, { 'adType': '%s' });", adType);
                admobAds.webView.loadUrl(event);
            }
        });
    }

    @Override
    public void onRewardedVideoAdOpened() {
        admobAds.onAdOpened(adType);
        admobAds.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": ad opened");
                String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdOpened, { 'adType': '%s' });", adType);
                admobAds.webView.loadUrl(event);
            }
        });
    }

    @Override
    public void onRewarded(RewardItem reward) {
        final String rewardType = reward.getType();
        final int rewardAmount = reward.getAmount();
        this.rewardType = rewardType;
        this.rewardAmount = rewardAmount;
        //admobAds.onAdRewarded(adType);
        admobAds.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": ad rewarded : "+rewardType+ " "+ rewardAmount);
                String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdRewarded, { 'adType': '%s','rewardType': '%s','rewardAmount': %d });", adType,rewardType,rewardAmount);
                admobAds.webView.loadUrl(event);
            }
        });
    }

    @Override
    public void onRewardedVideoAdClosed() {
        final String rewardType = this.rewardType;
        final int rewardAmount = this.rewardAmount;

        admobAds.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": ad closed after clicking on it");
                String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdClosed, { 'adType': '%s','rewardType': '%s','rewardAmount': %d  });", adType,rewardType,rewardAmount);
                admobAds.webView.loadUrl(event);
            }
        });
    }

    @Override
    public void onRewardedVideoStarted() {
        //admobAds.onAdStarted(adType);
        this.rewardType = "";
        this.rewardAmount = 0;
        admobAds.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": ad started");
                String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdStarted, { 'adType': '%s' });", adType);
                admobAds.webView.loadUrl(event);
            }
        });
    }

}
