package com.mobilizedconstruction.model;

import java.io.File;

/**
 * Created by apple on 24/10/2017.
 */

public class Image {
    private String FilePath;
    private Integer index;
    private File imageFile;
    private ReportImageDO reportImage;
    public Image(String filePath, File imageFile, Integer index, Double lng, Double lat, Integer reportID){
        this.index = index;
        this.FilePath = filePath;
        this.imageFile = imageFile;
        this.reportImage = new ReportImageDO(reportID, index, lng, lat);

    }
    public void SetImageUrl(String URL){
        this.reportImage.setImageURL(URL);
    }

    public void SetRoadHazard(Integer roadHazard){
        this.reportImage.setRoadHazard(roadHazard);
    }

    public String getFilePath(){
        return FilePath;
    }

    public ReportImageDO GetReportImage(){
        return reportImage;
    }

}

