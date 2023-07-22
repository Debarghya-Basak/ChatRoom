package com.dbtapps.chatroom.utilities;

import com.airbnb.lottie.LottieAnimationView;

public class LoadingAnimationController {

    public static void animationStart(LottieAnimationView loadingAnimation){
        loadingAnimation.animate().alpha(1).setDuration(1000).start();
    }

    public static void animationStop(LottieAnimationView loadingAnimation){
        loadingAnimation.animate().alpha(0).setDuration(1000).start();
    }

}
