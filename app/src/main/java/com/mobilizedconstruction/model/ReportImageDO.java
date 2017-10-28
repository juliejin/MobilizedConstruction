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

/**
 * Created by Ling Jin on 09/17/17.
 */

@DynamoDBTable(tableName = "mobilizedconstructio-mobilehub-516637937-ReportImage")

//data structure that matches the database tables
public class ReportImageDO implements Serializable{
    private Integer _reportID;
    private int _index;
    private String _imageURL;
    private Double _latitude;
    private Double _longitude;
    private Integer _roadHazard;

    public ReportImageDO(Integer reportId, int index, Double latitude, Double longitude){
        this._reportID = reportId;
        this._index = index;
        this._latitude = latitude;
        this._longitude = longitude;
    }


    @DynamoDBHashKey(attributeName = "ReportID")
    @DynamoDBAttribute(attributeName = "ReportID")
    public Integer getReportID() {
        return _reportID;
    }

    public void setReportID(final Integer _reportID) {
        this._reportID = _reportID;
    }
    @DynamoDBRangeKey(attributeName = "Index")
    @DynamoDBAttribute(attributeName = "Index")
    public int getIndex() {
        return _index;
    }

    public void setIndex(final int _index) {
        this._index = _index;
    }
    @DynamoDBAttribute(attributeName = "ImageURL")
    public String getImageURL() {
        return _imageURL;
    }

    public void setImageURL(final String _imageURL) {
        this._imageURL = _imageURL;
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
    @DynamoDBAttribute(attributeName = "Road Hazard")
    public Integer getRoadHazard() {
        return _roadHazard;
    }

    public void setRoadHazard(final Integer _roadHazard) {
        this._roadHazard = _roadHazard;
    }

}
