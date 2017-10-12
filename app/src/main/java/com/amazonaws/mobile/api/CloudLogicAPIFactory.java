package com.amazonaws.mobile.api;

//
//  CloudLogicAPIFactory.java
//
//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.19
//

/**
 * Produces instances of Cloud Logic API configuration.
 */
public class CloudLogicAPIFactory {

    private CloudLogicAPIFactory() {}

    /**
     * Gets the configured micro-service instances.
     * @return
     */
    public static CloudLogicAPIConfiguration[] getAPIs() {
        final CloudLogicAPIConfiguration[] apis = new CloudLogicAPIConfiguration[] {
                new CloudLogicAPIConfiguration("POST",
                                              "post new report",
                                              "https://c64y3aljf0.execute-api.us-east-1.amazonaws.com/Development",
                                              new String[] {
                                                  "/items",
                                                  "/path-564",
                                                  "/items/123",
                                                  "/path-564/123",
                                              },
                                              com.amazonaws.mobile.api.idc64y3aljf0.POSTMobileHubClient.class),
                new CloudLogicAPIConfiguration("DELETE",
                                              "delete image or report",
                                              "https://74hhuj5dud.execute-api.us-east-1.amazonaws.com/Development",
                                              new String[] {
                                                  "/items",
                                                  "/path-566",
                                                  "/items/123",
                                                  "/path-566/123",
                                              },
                                              com.amazonaws.mobile.api.id74hhuj5dud.DELETEMobileHubClient.class),
                new CloudLogicAPIConfiguration("GET",
                                              "get report or image",
                                              "https://gxqn4tmv45.execute-api.us-east-1.amazonaws.com/Development",
                                              new String[] {
                                                  "/items",
                                                  "/path-568",
                                                  "/items/123",
                                                  "/path-568/123",
                                              },
                                              com.amazonaws.mobile.api.idgxqn4tmv45.GETMobileHubClient.class),
        };

        return apis;
    }
}
