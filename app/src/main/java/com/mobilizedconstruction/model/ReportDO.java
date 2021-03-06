package com.mobilizedconstruction.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;





@DynamoDBTable(tableName = "mobilizedconstructio-mobilehub-516637937-Report")

public class ReportDO implements Serializable{
    private Integer _reportID;
    private String _comment;
    private String _dateCreated;
    private Integer _imageCount;
    private Double _latitude;
    private Double _longitude;
    private Integer _roadDirection;
    private Integer _roadHazard;
    private Integer _severity;
    private String _userID;

    public ReportDO(int reportID, String comment, String dateCreated, int imageCount,
                    Double latitude, Double longitude, int roadDirection, int roadHazard,
                    int severity, String userID){
        _reportID = reportID;
        _comment = comment;
        _dateCreated = dateCreated;
        _imageCount = imageCount;
        _latitude = latitude;
        _longitude = longitude;
        _roadDirection =roadDirection;
        _roadHazard = roadHazard;
        _severity = severity;
        _userID = userID;
    }

    @DynamoDBHashKey(attributeName = "Report ID")
    @DynamoDBAttribute(attributeName = "Report ID")


    public int getReportID() {
        return _reportID;
    }

    public void setReportID(final int _reportID) {
        this._reportID = _reportID;
    }
    @DynamoDBAttribute(attributeName = "Comment")
    public String getComment() {
        return _comment;
    }

    public void setComment(final String _comment) {
        this._comment = _comment;
    }
    @DynamoDBAttribute(attributeName = "Date Created")
    public String getDateCreated() {
        return _dateCreated;
    }

    public void setDateCreated(final String _dateCreated) {
        this._dateCreated = _dateCreated;
    }
    @DynamoDBAttribute(attributeName = "Image Count")
    public int getImageCount() {
        return _imageCount;
    }

    public void setImageCount(final int _imageCount) {
        this._imageCount = _imageCount;
    }
    @DynamoDBAttribute(attributeName = "Latitude")
    public Double getLatitude() {
        return _latitude;
    }

    public void setLatitude(final Double _latitude) {
        this._latitude = _latitude;
    }
    @DynamoDBAttribute(attributeName = "Longitude")
    public Double getLongitude() {
        return _longitude;
    }

    public void setLongitude(final Double _longitude) {
        this._longitude = _longitude;
    }
    @DynamoDBAttribute(attributeName = "Road Direction")
    public int getRoadDirection() {
        return _roadDirection;
    }

    public void setRoadDirection(final int _roadDirection) {
        this._roadDirection = _roadDirection;
    }
    @DynamoDBAttribute(attributeName = "Road Hazard")
    public int getRoadHazard() {
        return _roadHazard;
    }

    public void setRoadHazard(final int _roadHazard) {
        this._roadHazard = _roadHazard;
    }
    @DynamoDBAttribute(attributeName = "Severity")
    public int getSeverity() {
        return _severity;
    }

    public void setSeverity(final int _severity) {
        this._severity = _severity;
    }
    @DynamoDBAttribute(attributeName = "User ID")
    public String getUserID() {
        return _userID;
    }

    public void setUserID(final String _userID) {
        this._userID = _userID;
    }

}
