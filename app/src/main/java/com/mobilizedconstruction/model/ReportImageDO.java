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
    private Integer _index;
    private String _imageURL;

    public ReportImageDO(int reportID, int index){
        _reportID = reportID;
        _index = index;
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
    public Integer getIndex() {
        return _index;
    }

    public void setIndex(final Integer _index) {
        this._index = _index;
    }
    @DynamoDBAttribute(attributeName = "ImageURL")
    public String getImageURL() {
        return _imageURL;
    }

    public void setImageURL(final String _imageURL) {
        this._imageURL = _imageURL;
    }

}
