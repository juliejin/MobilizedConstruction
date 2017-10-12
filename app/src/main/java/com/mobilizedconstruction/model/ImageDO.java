package com.mobilizedconstruction.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "mobilizedconstructio-mobilehub-516637937-Image")

public class ImageDO {
    private String _userId;
    private Double _imageIndex;
    private Set<Double> _dateTaken;
    private Double _reportID;
    private String _roadHazard;
    private String _comment;
    private byte[] _imageFile;
    private Double _latitude;
    private Double _longitude;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "image index")
    @DynamoDBAttribute(attributeName = "image index")
    public Double getImageIndex() {
        return _imageIndex;
    }

    public void setImageIndex(final Double _imageIndex) {
        this._imageIndex = _imageIndex;
    }
    @DynamoDBAttribute(attributeName = "Date Taken")
    public Set<Double> getDateTaken() {
        return _dateTaken;
    }

    public void setDateTaken(final Set<Double> _dateTaken) {
        this._dateTaken = _dateTaken;
    }
    @DynamoDBAttribute(attributeName = "Report ID")
    public Double getReportID() {
        return _reportID;
    }

    public void setReportID(final Double _reportID) {
        this._reportID = _reportID;
    }
    @DynamoDBAttribute(attributeName = "Road Hazard")
    public String getRoadHazard() {
        return _roadHazard;
    }

    public void setRoadHazard(final String _roadHazard) {
        this._roadHazard = _roadHazard;
    }
    @DynamoDBAttribute(attributeName = "comment")
    public String getComment() {
        return _comment;
    }

    public void setComment(final String _comment) {
        this._comment = _comment;
    }
    @DynamoDBAttribute(attributeName = "image file")
    public byte[] getImageFile() {
        return _imageFile;
    }

    public void setImageFile(final byte[] _imageFile) {
        this._imageFile = _imageFile;
    }
    @DynamoDBAttribute(attributeName = "latitude")
    public Double getLatitude() {
        return _latitude;
    }

    public void setLatitude(final Double _latitude) {
        this._latitude = _latitude;
    }
    @DynamoDBAttribute(attributeName = "longitude")
    public Double getLongitude() {
        return _longitude;
    }

    public void setLongitude(final Double _longitude) {
        this._longitude = _longitude;
    }

}
