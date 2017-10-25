package com.mobilizedconstruction.model;


import java.io.Serializable;
import java.util.Vector;

/**
 * Created by Michelle_Yu on 10/24/17.
 */

public class Report implements Serializable
{
    public Vector<Image> reportImages;
    public ReportDO reportDO;

    public Report(ReportDO reportdo) {
        reportImages = new Vector<Image>();
        reportDO = reportdo;
    }

    public void insertImage(Image image){
        reportImages.add(image);
    }
}
