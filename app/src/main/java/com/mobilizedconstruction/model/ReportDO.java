package com.mobilizedconstruction.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.io.Serializable;
import java.util.Set;

@DynamoDBTable(tableName = "mobilizedconstructio-mobilehub-516637937-Report")

public class ReportDO implements Serializable{
    private Integer _reportID;
    private String _comment;
    private String _dateCreated;
    private Integer _imageCount;
    private Integer _roadDirection;
    private Integer _severity;
    private String _userID;

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

    /*public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Object createFromParcel(Parcel in) {
            return new ReportDO(in);
        }

        public ReportDO[] newArray(int size) {
            return new ReportDO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_imageCount);
        dest.writeInt(_roadDirection);
        dest.writeInt(_severity);
        dest.writeInt(_reportID);
        dest.writeString(_userID);
        dest.writeArray(_dateCreated.toArray());
    }

    private void readFromParcel(Parcel in ) {
        _imageCount = in.readInt();
        _roadDirection = in.readInt();
        _severity = in.readInt();
        _reportID = in.readInt();
        _userID = in.readString();
        _dateCreated = new HashSet<Integer>();
        for(int i=0;i<3;i++){
            _dateCreated.add(in.readInt());
        }
    }

    private ReportDO(Parcel in){
        readFromParcel(in);
    }
    */
}
