# On-Device Smart Reply Model

## ADP TALKS

The On-Device Smart Reply model is a contextually relevant, one-touch response system designed specifically for text chat use cases. It provides users with quick and effortless responses to incoming text messages or emails. The on-device model is targeted towards memory-constrained devices such as phones and watches and has a small memory footprint.

#### ___________________________________________________________________________

## Benefits

The on-device Smart Reply model has several benefits, including:

*  **Faster inference**   as the model resides on the device and does not require internet connectivity

*  **Resource-efficient** as the model has a small memory footprint on the device

*  **Privacy-friendly**   as user data never leaves the device

#### ___________________________________________________________________________

## Use Cases

The On-Device Smart Reply model is best suited for day-to-day chat messages. The model is particularly effective for messages that are similar to those provided in the sample tsv file. In case the model does not trigger any response, the system falls back to suggesting replies from a fixed back-off set that was compiled from popular response intents observed in chat conversations.

#### ___________________________________________________________________________

## How to Use

The On-Device Smart Reply demo app works in the following way:

1. The Android app links to the JNI binary with a predictor library.

2. The `GetSegmentPredictions` function is called with a list of input strings, which can be 1-3 most recent messages of the       conversations.

3. The function performs some preprocessing on input data, such as sentence splitting and normalization, and converts the input string content to tensors.

4. The function runs the prediction model on the input tensors.

5. The function performs post-processing to aggregate the model predictions for the input sentences and returns the appropriate responses.

6. Finally, the responses are sorted in descending order of confidence score and returned to the Android app.
Ops and Functionality Supported

#### ___________________________________________________________________________

## The On-Device Smart Reply model supports several ops, including:

*  **NORMALIZE**  
                 a custom op that normalizes sentences by converting them into lowercase, removing unnecessary punctuations, and     
                 expanding sentences wherever necessary.

*  **SKIP_GRAM** 
                an op inside TensorFlow Lite that converts sentences into a list of skip grams.

*  **EXTRACT_FEATURES** 
                a custom op that hashes skip grams to features represented as integers.

*  **LSH_PROJECTION**  
                an op inside TensorFlow Lite that projects input features to a corresponding bit vector space using Locality
                Sensitive Hashing (LSH).

*  **PREDICT**  
               a custom op that runs the input features through the projection model, computes the appropriate response labels, and aggregates the response labels and weights together.

*  **HASHTABLE_LOOKUP** 
               an op inside TensorFlow Lite that uses label ID from the PREDICT op and looks up the response text from the given label ID.
