package com.mobilizedconstruction.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

@DynamoDBTable(tableName = "mobilizedconstructio-mobilehub-516637937-Report")

public class ReportDO implements Serializable {
    private Integer _reportID;
    private String _comment;
    private String _dateCreated;
    private Integer _imageCount;
    private Integer _roadDirection;
    private Integer _severity;
    private String _userID;
    private Vector<Image> reportImages;

    public ReportDO(){}

    public ReportDO(Integer reportID, String comment, String dateCreated, Integer imageCount,
                    Integer roadDirection, Integer severity, String userID ){
        _reportID = reportID;
        _comment = comment;
        _dateCreated = dateCreated;
        _imageCount = imageCount;
        _roadDirection = roadDirection;
        _severity = severity;
        _userID = userID;
        reportImages = new Vector<Image>();
    }

    public void insertImage(Image image){
        reportImages.add(image);
    }

    public Vector<Image> getImages(){
        return reportImages;
    }

    @DynamoDBHashKey(attributeName = "Report ID")
    @DynamoDBAttribute(attributeName = "Report ID")
    public Integer getReportID() {
        return _reportID;
    }

    public void setReportID(final Integer _reportID) {
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
    public Integer getImageCount() {
        return _imageCount;
    }

    public void setImageCount(final Integer _imageCount) {
        this._imageCount = _imageCount;
    }
    @DynamoDBAttribute(attributeName = "Road Direction")
    public Integer getRoadDirection() {
        return _roadDirection;
    }

    public void setRoadDirection(final Integer _roadDirection) {
        this._roadDirection = _roadDirection;
    }
    @DynamoDBAttribute(attributeName = "Severity")
    public Integer getSeverity() {
        return _severity;
    }

    public void setSeverity(final Integer _severity) {
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


