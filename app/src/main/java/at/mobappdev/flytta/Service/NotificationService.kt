package at.mobappdev.flytta.Service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class NotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        //TODO: change to TAG
        Log.d("notification service", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
       sendRegistrationToServer(token)
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d("notification service", "sendRegistrationTokenToServer($token)")

    }


}