// Import required packages.
package org.tensorflow.lite.examples.smartreply;

// Import necessary classes.
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Displays a text box which updates as messages are received.
 */
public class MainActivity extends AppCompatActivity {
  // Create a constant to hold a tag for logging purposes.
  private static final String TAG = "ADPTalksDemo";
  // Create an instance of SmartReplyClient to handle message prediction.
  private SmartReplyClient client;
  // Declare UI elements.
  private TextView messageTextView;
  private EditText messageInput;
  private ScrollView scrollView;
 
  // Create a handler to perform background tasks.
  private Handler handler;

  // Initialize the activity.
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.v(TAG, "onCreate");
    // Set the layout for the activity.
    setContentView(R.layout.tfe_sr_main_activity);

    // Initialize the SmartReplyClient and the Handler.
    client = new SmartReplyClient(getApplicationContext());
    handler = new Handler();

    scrollView = findViewById(R.id.scroll_view);
    messageTextView = findViewById(R.id.message_text);

    // Get UI elements and set up the message input box.
    messageInput = findViewById(R.id.message_input);
    messageInput.setOnKeyListener(
        (view, keyCode, keyEvent) -> {
          if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
            // Send message when pressing Enter on keyboard.
            send(messageInput.getText().toString());
            return true;
          }
          return false;
        });

    // Set up the Send button.
    Button sendButton = findViewById(R.id.send_button);
    sendButton.setOnClickListener((View v) -> send(messageInput.getText().toString()));
  }
   // When the activity is started, load the Smart Reply model in the background.
  @Override
  protected void onStart() {
    super.onStart();
    Log.v(TAG, "onStart");
    
    // Load the model on a background thread.
    handler.post(
        () -> {
          client.loadModel();
        });
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.v(TAG, "onStop");
    
    // Unload the Smart Reply model on a background thread.
    handler.post(
        () -> {
          client.unloadModel();
        });
  }
// Send a message and get suggested replies from the Smart Reply model.
  private void send(final String message) {
    handler.post(
        () -> {
          StringBuilder textToShow = new StringBuilder();
          textToShow.append("Input: ").append(message).append("\n\n");

          // Get suggested replies from the model.
          SmartReply[] ans = client.predict(new String[] {message});
          for (SmartReply reply : ans) {
            textToShow.append("Reply: ").append(reply.getText()).append("\n");
          }
          textToShow.append("------").append("\n");

          // Update the UI with the message and suggested replies.
          runOnUiThread(
              () -> {
                // Show the message and suggested replies on screen.
                messageTextView.append(textToShow);

                // Clear the input box
                messageInput.setText(null);

                // Scroll to the bottom to show latest entry's classification result.
                scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
              });
        });
  }
}
