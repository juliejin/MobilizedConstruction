//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.19
//
package com.amazonaws.mobile.content;

/**
 * Dictates the download policy when calling
 * {@link ContentManager#getContent(String, long, ContentDownloadPolicy, boolean, ContentProgressListener)} )}
 */
public enum ContentDownloadPolicy {
    /** Download the file if not currently in the cache. */
    DOWNLOAD_IF_NOT_CACHED,
    /** Download the file if not currently in the cache or a newer file exists remotely. */
    DOWNLOAD_IF_NEWER_EXIST,
    /** Download the file remotely, never retrieve it from the local cache. */
    DOWNLOAD_ALWAYS,
    /** Always only retrieve the file from the local cache, never download the file remotely. */
    DOWNLOAD_NEVER,
    /** Retrieve the file from the local cache if it exists, but if not locally cached then
     * download the remote content metadata and return a remote content item containing the
     * metadata. */
    DOWNLOAD_METADATA_IF_NOT_CACHED
}
