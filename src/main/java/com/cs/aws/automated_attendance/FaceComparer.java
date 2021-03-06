package com.cs.aws.automated_attendance;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Value;

public class FaceComparer {
    private AmazonRekognition rekognitionClient;
    private ManageCollection mc;
    private AmazonSNS snsClient;

    public FaceComparer() {
        AWSCredentials credentials;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
        }

        rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        snsClient = AmazonSNSClientBuilder.standard().withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        System.out.println("\nAWS Rekognition Initialized...");

        mc = new ManageCollection(rekognitionClient);

        loadTargetImages();

    }

    /**
     *
     */
    private void loadTargetImages() {
        try {
            mc.createCollection();
            mc.addFacesToCollection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     */
    public String compare(Image source){
        String name = mc.searchFacesByImageResult(source);
        return name;
    }
}
